/*
 * Copyright 2018-2021 Ki11er_wolf
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
import com.ki11erwolf.resynth.plant.set.properties.AbstractCrystallineProperties;
import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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
abstract class CrystallineSet extends PlantSet<BlockCrystallinePlant, Block> {

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
    private final AbstractCrystallineProperties setProperties;

    /**
     * @param setName the name of the specific plant set instance (e.g. diamond).
     * @param properties the properties for the specific plant set instance.
     */
    CrystallineSet(String setName, AbstractCrystallineProperties properties) {
        super(SET_TYPE_NAME, setName, SEED_HOOKS, properties);
        this.setProperties = properties;

        this.produceItem = new ItemShard(this);
        this.plantBlock = new BlockCrystallinePlant(this){
            @Override
            protected ItemSeeds getSeedsItem(){
                return seedsItem;
            }

            @Override
            protected ItemStack getProduce(){
                return new ItemStack(produceItem.asItem(), setProperties.plantYield());
            }
        };
        this.seedsItem = new ItemSeeds(this);
    }

    // **********
    // Seed Hooks
    // **********

    /**
     * Registers game hooks that allow spawning seeds
     * in the world based on user actions.
     */
    private static class SeedHooks extends PlantSetSeedHooks {

        /**
         * Handles spawning seeds in the world when the player breaks
         * a specific ore block.
         *
         * @param event forge event.
         */
        @SubscribeEvent //Reflection
        public void onBlockBroken(BlockEvent.BreakEvent event){
            //Checks and declarations
            if(event.getPlayer() == null || event.getPlayer().isCreative())
                return;

            Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
            IWorld world = event.getWorld();

            //Sets
            for(PlantSet<?, ?> set : PublicPlantSetRegistry.getSets(PublicPlantSetRegistry.SetType.CRYSTALLINE)){
                if(set.isBroken() || set.getSeedSources(Block[].class).length == 0)
                    continue;

                CrystallineSet crystallineSet = (CrystallineSet) set;
                float spawnChance = crystallineSet.setProperties.seedSpawnChanceFromOre();

                if(spawnChance < 0 || !(block == Block.getBlockFromItem(crystallineSet.getSeedSources(Block[].class)[0].asItem())))
                    continue;

                //Spawn
                if(MathUtil.Probability.newPercentageProbability(spawnChance).randomResult().isTrue()){
                    dropSeeds(crystallineSet.seedsItem, (World) world, event.getPos());
                    event.setCanceled(true);
                    world.setBlockState(event.getPos(), Blocks.AIR.getDefaultState(), 2);
                }
            }
        }

        /**
         * Handles spawning seeds in the world when shard produce items
         * are left to despawn in water.
         *
         * @param event forge event.
         */
        @SubscribeEvent //Reflection
        public void onItemExpire(ItemExpireEvent event){
            //Checks and declarations
            Item i = event.getEntityItem().getItem().getItem();
            World world = event.getEntityItem().world;
            BlockPos pos = new BlockPos(event.getEntityItem().getPositionVec());
            int count = event.getEntityItem().getItem().getCount();
            Block b = world.getBlockState(pos).getBlock();

            if(b != Blocks.WATER)
                return;

            //Sets
            for(PlantSet<?, ?> set : PublicPlantSetRegistry.getSets(PublicPlantSetRegistry.SetType.CRYSTALLINE)) {
                //Checks
                if(set.isBroken() || set.getSeedSources(Block[].class).length == 0)
                    continue;

                if (i != set.getProduceItem().asItem()) {
                    continue;
                }

                float spawnChance = ((CrystallineSet) set).setProperties.seedSpawnChanceFromShard();
                if(spawnChance < 0)
                    continue;

                //Spawn
                for(int j = 0; j < count; j++){
                    if(MathUtil.Probability.newPercentageProbability(spawnChance).randomResult().isTrue()){
                        dropSeeds(set.getSeedsItem(), world, pos);
                    }
                }
                return;
            }
        }
    }
}
