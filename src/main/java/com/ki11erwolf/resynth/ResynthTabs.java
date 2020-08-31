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
package com.ki11erwolf.resynth;

import com.ki11erwolf.resynth.item.ResynthItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * Holds references to the various ItemGroups(Creative
 * Tabs) used by Resynth.
 */
public final class ResynthTabs {

    /**
     * Private constructor.
     */
    private ResynthTabs(){}

    /**
     * The mods main creative tab. Used for general blocks/items.
     */
    public static final ItemGroup TAB_RESYNTH = new ItemGroup("resynth") {
        @Override public ItemStack createIcon() { return new ItemStack(ResynthItems.ITEM_MINERAL_HOE); }
    };

    /**
     * The mods seeds tab. Holds the seed items for all plants.
     */
    public static final ItemGroup TAB_RESYNTH_SEEDS = new ItemGroup("resynth_seeds") {
        @Override public ItemStack createIcon() { return new ItemStack(ResynthPlants.DIAMOND.getSeedsItem().getItem()); }
    };

    /**
     * The mods produce tab. Holds the produce items for all plants.
     */
    public static final ItemGroup TAB_RESYNTH_PRODUCE = new ItemGroup("resynth_produce") {
        @Override public ItemStack createIcon() { return new ItemStack(ResynthPlants.ENDER_PEARL.getProduceItemOrBlock().getItem()); }
    };
}
