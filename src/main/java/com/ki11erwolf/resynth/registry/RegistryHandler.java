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
package com.ki11erwolf.resynth.registry;

import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.block.tileEntity.ResynthTileEntity;
import com.ki11erwolf.resynth.item.ResynthItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Handles the registering of all mod items.
 */
@Mod.EventBusSubscriber
public class RegistryHandler {

    /**
     * Registers all items when the event triggers.
     *
     * @param event item register event.
     */
    @SubscribeEvent
    @SuppressWarnings({"ConstantConditions", "SingleStatementInBlock", "unchecked", "deprecation"})
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ResynthItems.getItems());

        for(Block b : ResynthBlocks.getBlocks()){
            event.getRegistry().register(new ItemBlock(b).setRegistryName(b.getRegistryName()));

            if(b instanceof ResynthTileEntity){
                GameRegistry.registerTileEntity(((ResynthTileEntity)b).getTileEntityClass(), b.getRegistryName().toString());
            }
        }
    }

    /**
     * Registers all blocks when the event triggers.
     *
     * @param event block register event.
     */
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ResynthBlocks.getBlocks());
    }

}