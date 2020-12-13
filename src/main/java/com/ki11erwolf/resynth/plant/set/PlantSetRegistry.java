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

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.ResynthModPlants;
import com.ki11erwolf.resynth.ResynthPlants;
import com.ki11erwolf.resynth.block.ResynthBlock;
import com.ki11erwolf.resynth.integration.Hwyla;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//import com.ki11erwolf.resynth.integration.Hwyla;

/**
 * Handles registering plant set items and blocks
 * to the game. Also allows obtaining all registered
 * plant sets as a list.
 */
class PlantSetRegistry {

    /**
     * The logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    /**
     * List of all created and registered plant sets.
     */
    private static final List<PlantSet<?, ?>> PLANT_SETS = new ArrayList<>(45);

    /**
     * Queues the given plant set for registration.
     *
     * @param set the given plant set.
     */
    static void registerPlantSet(PlantSet<?, ?> set){
        if(PLANT_SETS.contains(Objects.requireNonNull(set))){
            LOG.warn("Attempt to register plant set: " + set.getSetName() + " more than once!");
            return;
        }

        LOG.info("Queuing plant set for registration: " + set.getSetName());
        PLANT_SETS.add(set);
    }

    /**
     * @return an array of every created and registered
     * plant set (regardless of set type).
     */
    static PlantSet<?, ?>[] getPlantSets(){
        return PLANT_SETS.toArray(new PlantSet[0]);
    }

    // ***************
    // Game Registerer
    // ***************

    /**
     * Helper class used to actually register blocks and items.
     */
    //Reflection
    @SuppressWarnings({"unused", "RedundantSuppression"})
    @Mod.EventBusSubscriber(modid = ResynthMod.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
    private static class Registerer {

        /*
         * Ensures plant sets are created and queued for
         * registration before we try and actually register them.
         */
        static {
            LOG.info("Queuing plant set registration...");

            ResynthPlants.initSets();
            ResynthModPlants.initSets();

            MinecraftForge.EVENT_BUS.addListener(Registerer::initializeRegistryReferences);
        }

        /**
         * Handles registering every plant sets blocks
         * to the game.
         *
         * @param event forge event.
         */
        @SubscribeEvent
        @SuppressWarnings("unused")//Reflection
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            PLANT_SETS.forEach(set -> {
                registerPlantBlock(set, event.getRegistry());
                registerProduceItemOrBlock(set, event.getRegistry(), null, false);
            });
        }

        /**
         * Handles registering every plant sets items
         * to the game.
         *
         * @param event forge event.
         */
        @SubscribeEvent
        @SuppressWarnings("unused")//Reflection
        public static void registerItems(RegistryEvent.Register<Item> event) {
            PLANT_SETS.forEach(set -> {
                registerSeedsItem(set, event.getRegistry());
                registerProduceItemOrBlock(set, null, event.getRegistry(), true);
            });
        }

        public static void initializeRegistryReferences(FMLServerAboutToStartEvent event) {
            PLANT_SETS.forEach(PlantSet::initSeedResources);
        }

        /**
         * Registers a given plant sets plant block to the game.
         */
        private static void registerPlantBlock(PlantSet<?, ?> set, IForgeRegistry<Block> registry){
            LOG.debug("Registering plant block: " + set.getPlantBlock().getRegistryName());
            registry.register(set.getPlantBlock());

            //Hwyla
            Hwyla.addIfProvider(set.getPlantBlock());
        }

        /**
         * Registers a given plant sets seeds item to the game.
         */
        private static void registerSeedsItem(PlantSet<?, ?> set, IForgeRegistry<Item> registry){
            LOG.debug("Registering plant seeds item: " + set.getSeedsItem().getRegistryName());
            registry.register(set.getSeedsItem());
        }

        /**
         * Registers a given plant sets produce block(and itemblock)/item to the game.
         */
        private static void registerProduceItemOrBlock(PlantSet<?, ?> set, IForgeRegistry<Block> blockRegistry,
                                                       IForgeRegistry<Item> itemRegistry, boolean forItems){
            IItemProvider itemProvider = set.getProduceItem();

            boolean isItem = itemProvider instanceof Item;
            boolean isBlock = itemProvider instanceof Block;

            Block block = isBlock ? (Block) itemProvider : null;
            Item item = (!isBlock && isItem) ? (Item) itemProvider : null;

            ResourceLocation registryName;

            if(block != null)
                registryName = block.getRegistryName();
            else if (item != null)
                registryName = item.getRegistryName();
            else registryName = new ResourceLocation(ResynthMod.MODID, "registery-error");

            //Block
            if(isBlock && !forItems){
                LOG.debug("Registering plant produce block: " + registryName);
                blockRegistry.register(block);

                //Hwyla
                Hwyla.addIfProvider(block);
            }

            //ItemBlock
            if(isBlock && forItems){
                LOG.debug("Registering plant produce ItemBlock: " + registryName);

                if(!(block instanceof ResynthBlock<?>))
                    LOG.warn("Cannot register an ItemBlock as it doesn't inherit from ResynthBlock: " + registryName);
                else itemRegistry.register(((ResynthBlock<?>) block).getItemBlock());
            }

            //Item
            if(isItem && forItems){
                LOG.debug("Registering plant produce item: " + registryName);
                itemRegistry.register((Item) itemProvider);
            }
        }
    }
}
