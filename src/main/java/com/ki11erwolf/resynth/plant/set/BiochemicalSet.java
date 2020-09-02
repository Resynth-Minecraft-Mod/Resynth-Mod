/*
 * Copyright 2018-2020 Ki11er_wolf
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

import com.ki11erwolf.resynth.plant.block.BlockBiochemicalPlant;
import com.ki11erwolf.resynth.plant.item.ItemBulb;
import com.ki11erwolf.resynth.plant.item.ItemSeeds;
import com.ki11erwolf.resynth.util.ItemOrBlock;
import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Used to create Biochemical plant sets. These are plant sets
 * that grow resources normally dropped when a mob is killed.
 * <p/>
 * This class merely handles defining what blocks/items go
 * into a set and how they interact, as well as handling
 * how seeds are obtained (through events).
 */
abstract class BiochemicalSet extends PlantSet<BlockBiochemicalPlant> {

    /**
     * The name of the plant set type.
     */
    private static final String SET_TYPE_NAME = "biochemical";

    /**
     * The static seed hooks instance.
     */
    private static final SeedHooks SEED_HOOKS = new SeedHooks();

    /**
     * The properties specific to this plant set instance.
     */
    private final IBiochemicalSetProperties setProperties;

    /**
     * @param setName the name of the specific plant set instance (e.g. diamond).
     * @param properties the properties for the specific plant set instance.
     */
    BiochemicalSet(String setName, IBiochemicalSetProperties properties) {
        super(SET_TYPE_NAME, setName, SEED_HOOKS, properties);
        this.setProperties = properties;

        //Plant
        this.produceItemOrBlock = new ItemOrBlock(new ItemBulb(SET_TYPE_NAME, setName, properties));
        this.plantBlock = new BlockBiochemicalPlant(SET_TYPE_NAME, setName, setProperties) {
            @Override
            protected ItemSeeds getSeedsItem() {
                return seedsItem;
            }

            @Override
            protected ItemStack getProduce() {
                return new ItemStack(produceItemOrBlock.getItem(), properties.numberOfProduceDrops());
            }
        };
        this.seedsItem = new ItemSeeds(SET_TYPE_NAME, setName, plantBlock, properties);
    }

    // ****************
    // Abstract Getters
    // ****************

    /**
     * Allows the PlantSetFactory to provide the
     * source mobs that seeds are obtained from
     * only when they're needed. This allows
     * providing mobs that are not yet registered
     * to the game. This in turn allows adding mobs
     * from Resynth and other mods.
     *
     * @return the list of mobs this plant sets seeds
     * are obtainable from.
     */
    abstract EntityType<?>[] getSourceMobs();

    // **********
    // Seed Hooks
    // **********

    /**
     * Registers game hooks that allow spawning seeds
     * in the world based on user actions.
     */
    private static class SeedHooks extends PlantSetSeedHooks {

        /**
         * Handles spawning seeds in the world
         * when a player kills a mob that is
         * part of a plant set.
         *
         * @param event forge event.
         */
        @SubscribeEvent
        public void onEntityKilled(LivingDeathEvent event){
            //For each plant set
            for(PlantSet<?> set : PublicPlantSetRegistry.getSets(PublicPlantSetRegistry.SetType.BIOCHEMICAL)){
                ResourceLocation deadEntity = event.getEntity().getType().getRegistryName();
                BiochemicalSet plantSet = (BiochemicalSet) set;

                if(set.isFailure()) continue;
                if(plantSet.getSourceMobs() == null) continue;

                //and each seed type
                for(EntityType<?> entity : plantSet.getSourceMobs()){
                    ResourceLocation loopedEntity = entity.getRegistryName();

                    //Do a lot of checks
                    if(loopedEntity == null) continue;
                    if(!loopedEntity.equals(deadEntity)) continue;
                    if(event.getEntity().getEntityWorld().isRemote) continue;
                    if(!MathUtil.chance(plantSet.setProperties.seedSpawnChanceFromMob())) continue;

                    //and spawn seeds if lucky
                    spawnSeeds(set.getSeedsItem(), event.getEntity().getEntityWorld(),
                            new BlockPos(event.getEntity().getPositionVec())
                    );
                }
            }
        }

        /**
         * Handles spawning seeds in the world
         * when the player smashes a bulb.
         *
         * @param event forge event.
         */
        @SubscribeEvent
        public void onItemDestroyed(PlayerDestroyItemEvent event){
            //noinspection ConstantConditions // Apparently it's not, and it can
            if(event.getOriginal() == null) return;

            //For each plant set
            for(PlantSet<?> set : PublicPlantSetRegistry.getSets(PublicPlantSetRegistry.SetType.BIOCHEMICAL)) {
                if(set.isFailure()) continue;

                BiochemicalSet plantSet = (BiochemicalSet) set;

                if(event.getOriginal().getItem() == plantSet.getProduceItemOrBlock().getItem()){
                    if(MathUtil.chance(plantSet.setProperties.seedSpawnChanceFromBulb())) {
                        if (!event.getEntity().getEntityWorld().isRemote) {
                            //Spawn seeds if lucky
                            spawnSeeds(
                                    plantSet.getSeedsItem(), event.getEntity().getEntityWorld(),
                                    new BlockPos(event.getEntity().getPositionVec())
                            );
                        }
                    }
                }
            }
        }

    }
}
