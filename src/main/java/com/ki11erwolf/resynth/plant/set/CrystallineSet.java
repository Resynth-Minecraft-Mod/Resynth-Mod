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
package com.ki11erwolf.resynth.plant.set;

import com.ki11erwolf.resynth.plant.block.BlockCrystallinePlant;
import com.ki11erwolf.resynth.plant.item.ItemSeeds;
import com.ki11erwolf.resynth.plant.item.ItemShard;
import com.ki11erwolf.resynth.util.ItemOrBlock;
import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Used to create Crystalline plant sets. These are plant sets
 * that grow resources normally dropped when an ore block is mined.
 * <p/>
 * This class merely handles defining what blocks/items go
 * into a set and how they interact, as well as handling
 * how seeds are obtained (through events).
 */
abstract class CrystallineSet extends PlantSet<BlockCrystallinePlant> {

    /**
     * The name of this plant set type.
     */
    private static final String SET_TYPE_NAME = "crystalline";

    /**
     * The single SeedHooks instance for this plant set type.
     */
    private static final SeedHooks SEED_HOOKS = new SeedHooks();

    /**
     * The properties for this specific plant set instance.
     */
    private final ICrystallineSetProperties setProperties;

    /**
     * @param setName the name of the specific plant set instance (e.g. diamond).
     * @param properties the properties for the specific plant set instance.
     */
    CrystallineSet(String setName, ICrystallineSetProperties properties) {
        super(SET_TYPE_NAME, setName, SEED_HOOKS, properties);
        this.setProperties = properties;

        //Plant
        this.produceItemOrBlock = new ItemOrBlock(new ItemShard(SET_TYPE_NAME, setName, properties));
        this.plantBlock = new BlockCrystallinePlant(SET_TYPE_NAME, setName, properties){
            @Override
            protected ItemSeeds getSeedsItem(){
                return seedsItem;
            }

            @Override
            protected ItemStack getProduce(){
                return new ItemStack(produceItemOrBlock.getItem(), properties.numberOfProduceDrops());
            }
        };
        this.seedsItem = new ItemSeeds(SET_TYPE_NAME, setName, plantBlock, properties);
    }

    // ****************
    // Abstract Getters
    // ****************

    /**
     * Allows the plant set factory to provide the source ore block
     * only when it's needed (when a block is broken in game). This
     * allows providing source ore blocks that are not yet registered
     * to the game. This in turn allows adding source ore blocks from
     * Resynth and other mods.
     *
     * @return the ore block seeds from this set are obtained from.
     */
    abstract ItemStack getSourceOre();

    // **********
    // Seed Hooks
    // **********

    /**
     * Registers game hooks that allow spawning seeds
     * in the world based on user actions.
     */
    private static class SeedHooks extends PlantSetSeedHooks {

        /**
         * Handles spawning seeds in the world when the player
         * breaks a specific ore block.
         *
         * @param event forge event.
         */
        @SubscribeEvent
        @SuppressWarnings("unused")//Reflection
        public void onBlockBroken(BlockEvent.BreakEvent event){
            //Checks and declarations
            if(event.getPlayer() == null || event.getPlayer().isCreative())
                return;

            Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
            IWorld world = event.getWorld();

            double  x = event.getPos().getX(),
                    y = event.getPos().getY(),
                    z = event.getPos().getZ();

            //Sets
            for(PlantSet set : PublicPlantSetRegistry.getSets(PublicPlantSetRegistry.SetType.CRYSTALLINE)){
                CrystallineSet crystallineSet = (CrystallineSet) set;
                float spawnChance = crystallineSet.setProperties.seedSpawnChanceFromOre();

                if(spawnChance < 0 || !(block == Block.getBlockFromItem(crystallineSet.getSourceOre().getItem())))
                    continue;

                //Spawn
                if(MathUtil.chance(spawnChance)){
                    world.spawnEntity(new EntityItem(
                            world.getWorld(), x, y, z, new ItemStack(crystallineSet.seedsItem, 1)
                    ));
                    event.setCanceled(true);
                    world.setBlockState(event.getPos(), Blocks.AIR.getDefaultState(), 2);
                }
            }
        }

        /**
         * Handles spawning seeds in the world
         * when shard produce items are left
         * to despawn in water.
         *
         * @param event forge event.
         */
        @SubscribeEvent
        @SuppressWarnings("unused")//Reflection
        public void onItemExpire(ItemExpireEvent event){
            //Checks and declarations
            Item i = event.getEntityItem().getItem().getItem();
            World world = event.getEntityItem().world;
            BlockPos pos = event.getEntityItem().getPosition();
            int count = event.getEntityItem().getItem().getCount();
            Block b = world.getBlockState(pos).getBlock();

            if(b != Blocks.WATER)
                return;

            //Sets
            for(PlantSet set : PublicPlantSetRegistry.getSets(PublicPlantSetRegistry.SetType.CRYSTALLINE)) {
                //Checks
                if (i != set.getProduceItemOrBlock().getItem()) {
                    continue;
                }

                float spawnChance = ((CrystallineSet) set).setProperties.seedSpawnChanceFromShard();

                if(spawnChance < 0)
                    continue;

                //Spawn
                for(int j = 0; j < count; j++){
                    if(MathUtil.chance(spawnChance)){
                        event.getEntityItem().world.spawnEntity(
                                new EntityItem(
                                        event.getEntityItem().world,
                                        event.getEntityItem().posX,
                                        event.getEntityItem().posY,
                                        event.getEntityItem().posZ,
                                        new ItemStack(set.getSeedsItem(), 1)
                                )
                        );
                    }
                }
                return;
            }
        }
    }
}
