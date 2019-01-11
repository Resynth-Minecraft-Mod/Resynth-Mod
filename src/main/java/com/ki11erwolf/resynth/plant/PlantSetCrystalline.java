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
import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.plant.block.BlockPlantCrystalline;
import com.ki11erwolf.resynth.plant.item.ItemPlantProduceShard;
import com.ki11erwolf.resynth.plant.item.ItemPlantSeeds;
import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * API used to create new plant sets (this includes the:
 * plant, seeds, produce and seed spawning logic) for
 * vanilla ores that drop the resource directly
 * (e.g. diamond).
 */
@Mod.EventBusSubscriber
public abstract class PlantSetCrystalline {

    /**
     * The plant block.
     */
    private final BlockPlantCrystalline plant;

    /**
     * The produce item.
     */
    private final ItemPlantProduceShard produce;

    /**
     * The seeds item.
     */
    private final ItemPlantSeeds seeds;

    /**
     * Minecraft ore the seeds are dropped from.
     */
    private final Block sourceOre;

    /**
     * Constructor.
     *
     * @param name the name of the plant, seeds and produce.
     * @param sourceOre the minecraft ore block the seeds are dropped from.
     */
    public PlantSetCrystalline(String name, Block sourceOre){
        this.sourceOre = sourceOre;
        this.plant = new BlockPlantCrystalline(name) {

            @Override
            protected Item getSeed() {return seeds;}

            @Override
            protected Item getProduce() {return produce;}

            @Override
            protected float _getGrowthChance() {return getPlantGrowthChance();}

            @Override
            protected boolean canBonemeal() {return canBonemealPlant();}
        };

        this.produce = new ItemPlantProduceShard(name);
        this.seeds = new ItemPlantSeeds(plant, name);
    }

    /**
     * Handles giving players seeds when they mine an ore block.
     *
     * @param event -
     */
    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event){
        if(!ResynthConfig.PLANTS_GENERAL.oreDropSeeds)
            return;

        if(event.getPlayer() == null)
            return;

        if(event.getPlayer().isCreative())
            return;

        //If has silk touch enchantment.
        for(NBTBase tag : event.getPlayer().getHeldItemMainhand().getEnchantmentTagList()){
            if(((NBTTagCompound)tag).getShort("id") == 33)
                return;
        }

        Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
        World world = event.getWorld();
        double x = event.getPos().getX(), y = event.getPos().getY(), z = event.getPos().getZ();

        for(PlantSetCrystalline plant : ResynthPlantSetRegistry.getCrystallinePlantSets()){
            if(!plant.doesOreDropSeeds())
                return;

            if(block == plant.sourceOre){
                //Added log for future testing purposes.
                if(!(event.getPlayer().getHeldItemMainhand().getItem().canHarvestBlock(event.getState())))
                    ResynthMod.getLogger().warn("Ore broken with item that cannot harvest: "
                            + event.getPlayer().getHeldItemMainhand().getItem().getClass().getCanonicalName());

                if(MathUtil.chance(plant.getOreSeedDropChance())){
                    world.spawnEntity(new EntityItem(world, x, y, z, new ItemStack(plant.seeds, 1)));
                    event.setCanceled(true);
                    world.setBlockToAir(event.getPos());
                }
            }
        }
    }

    /**
     * Handles plant produce turning into seeds in water.
     *
     * @param event -
     */
    @SubscribeEvent
    public static void onItemExpire(ItemExpireEvent event){
        Item i = event.getEntityItem().getItem().getItem();
        World world = event.getEntityItem().world;
        BlockPos pos = event.getEntityItem().getPosition();
        int count = event.getEntityItem().getItem().getCount();
        Block b = world.getBlockState(pos).getBlock();

        for(PlantSetCrystalline p : ResynthPlantSets.getCrystallinePlantSets()) {
            if (i == p.getSeeds()) {
                event.setCanceled(true);
                return;
            }

            if(!p.doesProduceDropSeeds() || !ResynthConfig.PLANTS_GENERAL.produceDropSeeds)
                continue;

            if(i == p.getProduce() && b == Blocks.WATER){
                for(int j = 0; j < count; j++){
                    if(MathUtil.chance(p.getProduceSeedDropChance())){
                        event.getEntityItem().world.spawnEntity(
                                new EntityItem(
                                        event.getEntityItem().world,
                                        event.getEntityItem().posX,
                                        event.getEntityItem().posY,
                                        event.getEntityItem().posZ,
                                        new ItemStack(p.getSeeds(), 1)
                                )
                        );
                    }
                }
                return;
            }
        }
    }

    /**
     * @return The plant block.
     */
    public BlockPlantCrystalline getPlant(){
        return this.plant;
    }

    /**
     * @return the seeds item.
     */
    public ItemPlantSeeds getSeeds(){
        return this.seeds;
    }

    /**
     * @return the produce item.
     */
    public ItemPlantProduceShard getProduce(){
        return this.produce;
    }

    /**
     * Adds this plant to the game.
     */
    protected PlantSetCrystalline register(){
        ResynthPlantSetRegistry.addPlantBlock(plant);
        ResynthPlantSetRegistry.addShardProduce(produce);
        ResynthPlantSetRegistry.addSeeds(seeds);
        ResynthPlantSetRegistry.addPlantSet(this);
        return this;
    }

    /**
     * @return the item given when this plants produce item
     * is smelted.
     */
    public abstract ItemStack getResult();

    /**
     * @return true if the given minecraft ore block should drop seeds.
     */
    protected abstract boolean doesOreDropSeeds();

    /**
     * @return the chance the minecraft ore block will drop this plants seeds.
     */
    protected abstract float getOreSeedDropChance();

    /**
     * @return the chance this plant will grow
     * on an approved growth tick.
     */
    protected abstract float getPlantGrowthChance();

    /**
     * @return true if bonemeal can be used on this plant type.
     */
    protected abstract boolean canBonemealPlant();

    /**
     * @return the chance this plants produce will turn into seeds in water.
     */
    protected abstract float getProduceSeedDropChance();

    /**
     * @return true if this plants produce turns into seeds in water.
     */
    protected abstract boolean doesProduceDropSeeds();
}
