/*
 * Copyright 2018 Ki11er_wolf
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
import com.ki11erwolf.resynth.plant.block.BlockPlantOre;
import com.ki11erwolf.resynth.plant.item.ItemPlantSeed;
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

import javax.swing.*;
import java.util.List;

/**
 * The class used to create new metallic plants
 * with seeds item and produce (ore) block.
 */
@Mod.EventBusSubscriber
public abstract class PlantMetallic {

    /**
     * The minecraft ore block used to obtain seeds.
     */
    private final ItemStack seedOre;

    /**
     * The block the plant should place..
     */
    private final BlockPlantOre ore;

    /**
     * The plant block.
     */
    private final BlockPlantMetallic plant;

    /**
     * The plants seeds item.
     */
    private final ItemPlantSeed seeds;

    /**
     * modid if this instance represents a mod plant (e.g. tinkers)
     */
    protected String modid;

    /**
     * name of the mod ore block if this instance represents a mod plant (e.g. tinkers)
     */
    protected String oreBlockName;

    /**
     * mod block metadata if this instance represents a mod plant (e.g. tinkers).
     */
    protected int oreMetaData;

    /**
     * Constructs a new metallic plant
     * instance.
     *
     * @param name the name of the plant block.
     * @param seedOre the ore block seeds are obtained from.
     */
    public PlantMetallic(String name, ItemStack seedOre){
        this.seedOre = seedOre;
        this.ore = new BlockPlantOre(name);
        this.plant = new BlockPlantMetallic(ore, name){
            @Override
            protected Item getSeedItem(){
                return seeds;
            }

            @Override
            protected float getGrowthPeriod(){
                return getFloweringPeriod();
            }

            @Override
            protected boolean canBonemeal(){
                return canBoneMeal();
            }
        };
        this.seeds = new ItemPlantSeed(plant, name, name);
    }

    /**
     * Registers this plant (plant block, seeds and produce)
     * to the game.
     *
     * @return this
     */
    protected PlantMetallic register(){
        ResynthPlantRegistry.addOre(ore);
        ResynthPlantRegistry.addPlant(plant);
        ResynthPlantRegistry.addSeeds(seeds);
        ResynthPlantRegistry.addPlant(this);
        return this;
    }

    /**
     * @return the block this plant places. Its
     * produce.
     */
    public BlockPlantOre getOre(){
        return this.ore;
    }

    /**
     * @return the seeds item used to
     * place this plant.
     */
    public ItemPlantSeed getSeeds(){
        return this.seeds;
    }

    /**
     * @return the plant block.
     */
    public BlockPlantMetallic getPlant(){
        return this.plant;
    }

    /**
     * Called when an explosion occurs in the world.
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
            for(PlantMetallic plant : ResynthPlantRegistry.getMetallicPlants()){
                //Separate logic for non-vanilla plants.
                if(plant.modid != null && plant.doesOreDropSeeds() && ResynthConfig.PLANTS_GENERAL.oreDropSeeds){
                    //Same Block class
                    if(block == ModPlant.getModBlock(plant.modid, plant.oreBlockName)){
                        //Same block exactly
                        if(block.getMetaFromState(state) == plant.oreMetaData){
                            //Random chance.
                            if(MathUtil.chance(plant.getOreSeedDropChance())){
                                detonateEvent.getWorld().setBlockToAir(blockPos);
                                detonateEvent.getWorld().spawnEntity(
                                        new EntityItem(detonateEvent.getWorld(),
                                                blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                                                new ItemStack(plant.seeds, 1))
                                );
                            }
                        }
                    }
                } else if(plant.doesOreDropSeeds() && block == Block.getBlockFromItem(plant.seedOre.getItem())
                        && ResynthConfig.PLANTS_GENERAL.oreDropSeeds){
                    //A Seed ore block has been blown up.

                    //Don't spawn seeds from thin air...
                    if(plant.seedOre.getItem() == Items.AIR)
                        continue;

                    //Random chance.
                    if(MathUtil.chance(plant.getOreSeedDropChance())){
                        detonateEvent.getWorld().setBlockToAir(blockPos);
                        detonateEvent.getWorld().spawnEntity(
                                new EntityItem(detonateEvent.getWorld(),
                                        blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                                        new ItemStack(plant.seeds, 1))
                        );
                    }
                }

                if(plant.doesOrganicOreDropSeeds() && block == plant.ore
                        && ResynthConfig.PLANTS_GENERAL.organicOreDropSeeds){
                    //An organic ore block has been blown up.

                    //Random chance.
                    if(MathUtil.chance(plant.getOrganicOreSeedDropChance())){
                        JOptionPane.showMessageDialog(null,
                                "Spawning seeds 3"
                        );
                        detonateEvent.getWorld().setBlockToAir(blockPos);
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
     * @return the item to give
     * when smelting this plants ore block.
     */
    public abstract ItemStack getResult();

    /**
     * @return how the long the plant takes
     * to grow in general. This chance of
     * growing is 1 in the number provided + 1.
     */
    protected abstract float getFloweringPeriod();

    /**
     * @return true if bonemeal can be used
     * on this plant.
     */
    protected abstract boolean canBoneMeal();

    /**
     * @return the integer chance of seeds
     * dropping from the minecraft ore block.
     */
    protected abstract float getOreSeedDropChance();

    /**
     * @return the integer chance of seeds
     * dropping from the plants ore block (produce).
     */
    protected abstract float getOrganicOreSeedDropChance();

    /**
     * @return true if seeds should drop from the
     * minecraft ore block.
     */
    protected abstract boolean doesOreDropSeeds();

    /**
     * @return true if seeds should drop from
     * the plants ore block (produce).
     */
    protected abstract boolean doesOrganicOreDropSeeds();
}
