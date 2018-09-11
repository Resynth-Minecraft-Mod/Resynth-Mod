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

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.item.ResynthItems;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Handles registering of item and block models.
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ModelRegistryHandler {

    /**
     * Registers all mod item models.
     *
     * @param event model register event.
     */
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for(Item i : ResynthItems.getItems())
            registerModel(i);
        for(Block b : ResynthBlocks.getBlocks())
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