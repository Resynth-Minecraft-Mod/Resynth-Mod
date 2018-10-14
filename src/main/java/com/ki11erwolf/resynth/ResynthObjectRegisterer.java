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
package com.ki11erwolf.resynth;

import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.block.tileEntity.ResynthTileEntity;
import com.ki11erwolf.resynth.item.ResynthItems;
import com.ki11erwolf.resynth.plant.ResynthPlants;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Handles the registering of all mod items and blocks.
 */
@Mod.EventBusSubscriber
public class ResynthObjectRegisterer {

    /**
     * Registers all items when the event triggers.
     *
     * @param event item register event.
     */
    @SubscribeEvent
    @SuppressWarnings({"ConstantConditions", "SingleStatementInBlock", "unchecked", "deprecation"})
    public static void registerItems(RegistryEvent.Register<Item> event) {
        //Normal
        event.getRegistry().registerAll(ResynthItems.getItems());

        for(Block b : ResynthBlocks.getBlocks()) {
            event.getRegistry().register(new ItemBlock(b).setRegistryName(b.getRegistryName()));

            if (b instanceof ResynthTileEntity) {
                GameRegistry.registerTileEntity(
                        ((ResynthTileEntity) b).getTileEntityClass(), b.getRegistryName().toString());
            }
        }


        //Plants
        event.getRegistry().registerAll(ResynthPlants.getSeedItems());
        event.getRegistry().registerAll(ResynthPlants.getProduceItems());
        event.getRegistry().registerAll(ResynthPlants.getMobProduceItems());

        for(Block b : ResynthPlants.getPlantBlocks()) {
            event.getRegistry().register(new ItemBlock(b).setRegistryName(b.getRegistryName()));
        }

        for(Block b : ResynthPlants.getOreBlocks()) {
            event.getRegistry().register(new ItemBlock(b).setRegistryName(b.getRegistryName()));
        }
    }

    /**
     * Registers all blocks when the event triggers.
     *
     * @param event block register event.
     */
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        //Normal Items
        event.getRegistry().registerAll(ResynthBlocks.getBlocks());

        //Plants
        event.getRegistry().registerAll(ResynthPlants.getPlantBlocks());
        event.getRegistry().registerAll(ResynthPlants.getOreBlocks());
    }

    @Mod.EventBusSubscriber(Side.CLIENT)
    public static class ObjectModelRegisterer {

        /**
         * Registers all mod item models.
         *
         * @param event model register event.
         */
        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            //Normal
            for(Item i : ResynthItems.getItems())
                registerModel(i);
            for(Block b : ResynthBlocks.getBlocks())
                registerModel(Item.getItemFromBlock(b));

            //Plants
            for(Item i : ResynthPlants.getSeedItems())
                registerModel(i);
            for(Item i : ResynthPlants.getProduceItems())
                registerModel(i);
            for(Item i : ResynthPlants.getMobProduceItems())
                registerModel(i);
            for(Block b : ResynthPlants.getPlantBlocks())
                registerModel(Item.getItemFromBlock(b));
            for(Block b : ResynthPlants.getOreBlocks())
                registerModel(Item.getItemFromBlock(b));
        }

        /**
         * Registers an item model.
         *
         * @param i the item who's model we are going to register.
         */
        @SuppressWarnings("ConstantConditions")
        private static void registerModel(Item i) {
            if(i == null) {
                ResynthMod.getLogger().warn("Null item passed to registerModel(item)");
                return;
            }

            ModelLoader.setCustomModelResourceLocation(i,
                    0, new ModelResourceLocation(i.getRegistryName(),"inventory"));
        }
    }
}