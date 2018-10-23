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
import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.plant.block.BlockPlantCrystalline;
import com.ki11erwolf.resynth.plant.item.ItemPlantOreProduce;
import com.ki11erwolf.resynth.plant.item.ItemPlantSeed;
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
 * The class used to create new crystalline plants
 * with seeds item and produce item.
 */
@Mod.EventBusSubscriber
public abstract class PlantCrystalline {

    /**
     * The plant block.
     */
    private final BlockPlantCrystalline plant;

    /**
     * The produce item.
     */
    private final ItemPlantOreProduce produce;

    /**
     * The seeds item.
     */
    private final ItemPlantSeed seeds;

    /**
     * Minecraft ore the seeds are dropped from.
     */
    private final Block seedOre;

    /**
     * Constructor.
     *
     * @param name the name of the plant, seeds and produce.
     * @param seedOre the minecraft ore block the seeds are dropped from.
     */
    public PlantCrystalline(String name, Block seedOre){
        this.seedOre = seedOre;
        this.plant = new BlockPlantCrystalline(name) {

            @Override
            protected Item getSeed() {return seeds;}

            @Override
            protected Item getProduce() {return produce;}

            @Override
            protected float getGrowthPeriod() {return getFloweringPeriod();}

            @Override
            protected boolean canBonemeal() {return canBoneMeal();}
        };

        this.produce = new ItemPlantOreProduce(name);
        this.seeds = new ItemPlantSeed(plant, name, name);
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
    public ItemPlantSeed getSeeds(){
        return this.seeds;
    }

    /**
     * @return the produce item.
     */
    public ItemPlantOreProduce getProduce(){
        return this.produce;
    }

    /**
     * Adds this plant to the game.
     */
    protected PlantCrystalline register(){
        ResynthPlantRegistry.addPlant(plant);
        ResynthPlantRegistry.addProduce(produce);
        ResynthPlantRegistry.addSeeds(seeds);
        ResynthPlantRegistry.addPlant(this);
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
     * @return how long this plant takes to grow.
     */
    protected abstract float getFloweringPeriod();

    /**
     * @return true if bonemeal can be used on this plant.
     */
    protected abstract boolean canBoneMeal();

    /**
     * @return the chance this plants produce will turn into seeds in water.
     */
    protected abstract float getProduceSeedDropChance();

    /**
     * @return true if this plants produce turns into seeds in water.
     */
    protected abstract boolean doesProduceDropSeeds();

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

        for(PlantCrystalline plant : ResynthPlantRegistry.getCrystallinePlants()){
            if(!plant.doesOreDropSeeds())
                return;

            if(block == plant.seedOre){
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

        for(PlantCrystalline p : ResynthPlants.getCrystallinePlants()) {
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
}
