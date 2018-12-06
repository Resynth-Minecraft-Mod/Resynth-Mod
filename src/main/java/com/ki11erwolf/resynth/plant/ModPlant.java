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

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Map;

/**
 * Base class for all mod plant types.
 *
 * @param <T>
 */
public class ModPlant<T> {

    /**
     * The resynth plant created for the mod resource.
     */
    protected T backingPlant;

    /**
     * @return true if the backing plant was able to be initialized.
     */
    public boolean isLoaded(){
        return backingPlant != null;
    }

    /**
     * Gets a block from the forge registry matching the given
     * modid and name.
     *
     * @param modid the blocks mods modid.
     * @param blockName the blocks name.
     * @return the block if found, {@code null} if not found.
     */
    static Block getModBlock(String modid, String blockName){
        Block block = null;

        for(Map.Entry<ResourceLocation, Block> entry : ForgeRegistries.BLOCKS.getEntries()){
            if(entry.getKey().getResourceDomain().equals(modid)
                    && entry.getKey().getResourcePath().equals(blockName))
                block = entry.getValue();
        }

        return block;
    }

    /**
     * Gets an item from the forge registry matching the given
     * modid and name.
     *
     * @param modid the items mods modid.
     * @param itemName the items name.
     * @return the item if found, {@code null} if not found.
     */
    static Item getModItem(String modid, String itemName){
        Item item = null;

        for(Map.Entry<ResourceLocation, Item> entry : ForgeRegistries.ITEMS.getEntries()){
            if(entry.getKey().getResourceDomain().equals(modid)
                    && entry.getKey().getResourcePath().equals(itemName))
                item = entry.getValue();
        }

        return item;
    }

    /**
     * Attempts to register the plant to the game.
     *
     * @return this if the registration was successfully,
     * null if not.
     */
    public ModPlant<T> register(){
        if(backingPlant == null){
            return null;
        }

        if(backingPlant instanceof PlantCrystalline)
            ((PlantCrystalline) backingPlant).register();

        if(backingPlant instanceof PlantMetallic)
            ((PlantMetallic) backingPlant).register();

        return this;
    }
}
