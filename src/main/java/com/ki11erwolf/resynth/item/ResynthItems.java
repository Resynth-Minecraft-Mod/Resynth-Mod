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
package com.ki11erwolf.resynth.item;

import com.ki11erwolf.resynth.ResynthMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Holds references to all the mod items.
 */
@GameRegistry.ObjectHolder(ResynthMod.MOD_ID)
public class ResynthItems {

    /**
     * Mods test item.
     */
    public static final ResynthItem ITEM_TEST = new ItemTest();

    /**
     * Mineral Rock. The item dropped from Mineral Rich Rock.
     */
    public static final ResynthItem ITEM_MINERAL_ROCK = new ItemMineralRock().register();

    /**
     * Dense Mineral Rock. x9 MineralRocks.
     */
    public static final ResynthItem ITEM_DENSE_MINERAL_ROCK = new ItemDenseMineralRock().register();

    /**
     * Mineral Crystal. Recipe component for Mineral Soil.
     */
    public static final ResynthItem ITEM_MINERAL_CRYSTAL = new ItemMineralCrystal().register();

    /**
     * @return an array of all the registered resynth items.
     */
    public static Item[] getItems(){
        return ResynthItemRegistry.getItems();
    }
}
