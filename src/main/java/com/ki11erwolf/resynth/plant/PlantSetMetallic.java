/*
 * Copyright 2018-2019 Ki11er_wolf
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ki11erwolf.resynth.plant;

import com.ki11erwolf.resynth.ResynthConfig;
import com.ki11erwolf.resynth.plant.block.BlockPlantMetallic;
import com.ki11erwolf.resynth.plant.block.BlockOrganicPlantOre;
import com.ki11erwolf.resynth.plant.item.ItemPlantSeeds;
import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * API used to create new plant sets (this includes the:
 * plant, seeds, produce and seed spawning logic) for
 * vanilla ores that drop the plantOre block (e.g. iron).
 */
@Mod.EventBusSubscriber
public abstract class PlantSetMetallic {

    /**
     * The minecraft plantOre block used to obtain seeds.
     */
    private final ItemStack sourceOre;

    /**
     * The block the plant should place..
     */
    private final BlockOrganicPlantOre plantOre;

    /**
     * The plant block.
     */
    private final BlockPlantMetallic plant;

    /**
     * The plants seeds item.
     */
    private final ItemPlantSeeds seeds;

    /**
     * Modid of the mod if this instance represents a mod plant.
     */
    protected String modid;

    /**
     * Name of the mod plantOre block if this instance represents a mod plant.
     */
    protected String oreBlockName;

    /**
     * Mod block metadata if this instance represents a mod plant.
     */
    protected int oreMetaData;

    /**
     * Default constructor.
     *
     * @param name the name of the plant block.
     * @param sourceOre the minecraft ore block seeds are obtained from.
     */
    public PlantSetMetallic(String name, ItemStack sourceOre){
        this.sourceOre = sourceOre;
        this.plantOre = new BlockOrganicPlantOre(name, this);
        this.plant = new BlockPlantMetallic(plantOre, name){
            @Override
            protected Item getSeedItem(){
                return seeds;
            }

            @Override
            protected float _getGrowthChance(){
                return getPlantGrowthChance();
            }

            @Override
            protected boolean canBonemeal(){
                return canBonemealPlant();
            }
        };
        this.seeds = new ItemPlantSeeds(plant, name, this);
    }

    /**
     * Called when an explosion occurs in the world.
     *
     * <p>
     *     This handles the dropping of seeds
     *     from ore blocks.
     * </p>
     *
     * @param detonateEvent the event created by the explosion.
     */
    @SubscribeEvent
    //1 square kilometer of tnt blasts later
    //It works (i think)... until the next plant breaks it.
    public static void onExplosion(ExplosionEvent.Detonate detonateEvent){
        List<BlockPos> affectedBlocks = detonateEvent.getAffectedBlocks();

        for(BlockPos blockPos : affectedBlocks){
            Block block = detonateEvent.getWorld().getBlockState(blockPos).getBlock();
            IBlockState state = detonateEvent.getWorld().getBlockState(blockPos);
            for(PlantSetMetallic plant : ResynthPlantSetRegistry.getMetallicPlantSets()){
                //Separate logic for non-vanilla plants.
                if(plant.modid != null && plant.doesSourceOreDropSeeds()
                        && ResynthConfig.PLANTS_GENERAL.oreDropSeeds){
                    //Same Block class
                    if(block == ModPlantSetBase.getModBlock(plant.modid, plant.oreBlockName)){
                        //Same block exactly
                        if(block.getMetaFromState(state) == plant.oreMetaData){
                            //Random chance.
                            if(MathUtil.chance(plant.getSourceOreSeedDropChance())){
                                detonateEvent.getWorld().setBlockToAir(blockPos);
                                ItemPlantSeeds.addEffects(detonateEvent.getWorld(), blockPos);
                                detonateEvent.getWorld().spawnEntity(
                                        new EntityItem(detonateEvent.getWorld(),
                                                blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                                                new ItemStack(plant.seeds, 1))
                                );
                            }
                        }
                    }
                } else if(plant.doesSourceOreDropSeeds() && block == Block.getBlockFromItem(plant.sourceOre.getItem())
                        && ResynthConfig.PLANTS_GENERAL.oreDropSeeds){
                    //A Seed plantOre block has been blown up.

                    //Don't spawn seeds from thin air...
                    if(plant.sourceOre.getItem() == Items.AIR)
                        continue;

                    //Random chance.
                    if(MathUtil.chance(plant.getSourceOreSeedDropChance())){
                        detonateEvent.getWorld().setBlockToAir(blockPos);
                        ItemPlantSeeds.addEffects(detonateEvent.getWorld(), blockPos);
                        detonateEvent.getWorld().spawnEntity(
                                new EntityItem(detonateEvent.getWorld(),
                                        blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                                        new ItemStack(plant.seeds, 1))
                        );
                    }
                }

                if(plant.doesPlantOreDropSeeds() && block == plant.plantOre
                        && ResynthConfig.PLANTS_GENERAL.organicOreDropSeeds){
                    //An organic plantOre block has been blown up.

                    //Random chance.
                    if(MathUtil.chance(plant.getProduceSeedDropChance())){
                        detonateEvent.getWorld().setBlockToAir(blockPos);
                        ItemPlantSeeds.addEffects(detonateEvent.getWorld(), blockPos);
                        detonateEvent.getWorld().spawnEntity(
                                new EntityItem(detonateEvent.getWorld(),
                                        blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                                        new ItemStack(plant.seeds, 1))
                        );
                    }
                }
            }
        }
    }

    /**
     * Registers this plant set (plant block, seeds and produce)
     * to the game.
     *
     * @return this
     */
    protected PlantSetMetallic register(){
        ResynthPlantSetRegistry.addOrganicOreBlock(plantOre);
        ResynthPlantSetRegistry.addPlantBlock(plant);
        ResynthPlantSetRegistry.addSeeds(seeds);
        ResynthPlantSetRegistry.addPlantSet(this);
        return this;
    }

    /**
     * @return the chance of seeds dropping from the source ore
     * formatted for human reading.
     */
    public String getTextualOreSeedDropChance(){
        return Math.round(getSourceOreSeedDropChance()) + "%";
    }

    /**
     * @return the chance of seeds dropping from the produce
     * formatted for human reading.
     */
    public String getTextualProduceSeedDropChance(){
        return Math.round(getProduceSeedDropChance()) + "%";
    }

    /**
     * @return the chance of the plant growing
     * formatted for human reading.
     */
    public String getTextualPlantGrowthChance(){
        return Math.round(getPlantGrowthChance()) + "%";
    }

    /**
     * @return the produce block this plant places.
     */
    public BlockOrganicPlantOre getPlantOre(){
        return this.plantOre;
    }

    /**
     * @return the seeds item used to
     * place this plant.
     */
    public ItemPlantSeeds getSeeds(){
        return this.seeds;
    }

    /**
     * @return the plant block.
     */
    public BlockPlantMetallic getPlant(){
        return this.plant;
    }

    /**
     * @return the item stack given when the plants
     * produce block is smelted.
     */
    public abstract ItemStack getResult();

    /**
     * @return the chance this plant will grow
     * on an approved growth tick.
     */
    protected abstract float getPlantGrowthChance();

    /**
     * @return true if bonemeal can be used
     * on this plant.
     */
    protected abstract boolean canBonemealPlant();

    /**
     * @return the integer chance of seeds
     * dropping from the minecraft ore block.
     */
    protected abstract float getSourceOreSeedDropChance();

    /**
     * @return the integer chance of seeds
     * dropping from the plants ore block (produce).
     */
    protected abstract float getProduceSeedDropChance();

    /**
     * @return true if seeds should drop from the
     * minecraft ore block.
     */
    protected abstract boolean doesSourceOreDropSeeds();

    /**
     * @return true if seeds should drop from
     * the plants ore block (produce).
     */
    protected abstract boolean doesPlantOreDropSeeds();
}
