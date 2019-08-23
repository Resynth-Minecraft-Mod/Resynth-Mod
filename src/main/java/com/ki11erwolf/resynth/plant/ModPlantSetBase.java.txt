///*
// * Copyright 2018-2019 Ki11er_wolf
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.ki11erwolf.resynth.plant;
//
//import net.minecraft.block.Block;
//import net.minecraft.item.Item;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fml.common.registry.ForgeRegistries;
//
//import java.util.Map;
//
///**
// * Base class for all apis that provide a plant set
// * for external mods.
// *
// * @param <T> plant set (e.g. metallic) this api wraps.
// */
//public class ModPlantSetBase<T> {
//
//    /**
//     * The plant set this mod api will wrap.
//     */
//    protected T backingPlantSet;
//
//    /**
//     * @return true if the plant set for the external
//     * mod was able to be loaded.
//     */
//    public boolean isLoaded(){
//        return backingPlantSet != null;
//    }
//
//    /**
//     * Attempts to register the plant set to the game.
//     *
//     * @return this if the registration was successful,
//     * null if not.
//     */
//    public ModPlantSetBase<T> register(){
//        if(backingPlantSet == null){
//            return null;
//        }
//
//        if(backingPlantSet instanceof PlantSetCrystalline)
//            ((PlantSetCrystalline) backingPlantSet).register();
//
//        if(backingPlantSet instanceof PlantSetMetallic)
//            ((PlantSetMetallic) backingPlantSet).register();
//
//        return this;
//    }
//
//    /**
//     * @return the original plant set created
//     * by this wrapper API.
//     */
//    public T getBackingPlantSet(){
//        return backingPlantSet;
//    }
//
//    /**
//     * Gets a block from the forge registry matching the given
//     * modid and name.
//     *
//     * @param modid the blocks mods modid.
//     * @param blockName the blocks name.
//     * @return the block if found, {@code null} if not found.
//     */
//    static Block getModBlock(String modid, String blockName){
//        Block block = null;
//
//        for(Map.Entry<ResourceLocation, Block> entry : ForgeRegistries.BLOCKS.getEntries()){
//            if(entry.getKey().getResourceDomain().equals(modid)
//                    && entry.getKey().getResourcePath().equals(blockName))
//                block = entry.getValue();
//        }
//
//        return block;
//    }
//
//    /**
//     * Gets an item from the forge registry matching the given
//     * modid and name.
//     *
//     * @param modid the items mods modid.
//     * @param itemName the items name.
//     * @return the item if found, {@code null} if not found.
//     */
//    static Item getModItem(String modid, String itemName){
//        Item item = null;
//
//        for(Map.Entry<ResourceLocation, Item> entry : ForgeRegistries.ITEMS.getEntries()){
//            if(entry.getKey().getResourceDomain().equals(modid)
//                    && entry.getKey().getResourcePath().equals(itemName))
//                item = entry.getValue();
//        }
//
//        return item;
//    }
//}
