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
package com.ki11erwolf.resynth;

import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.GeneralConfig;
import com.ki11erwolf.resynth.item.ResynthItems;
import com.ki11erwolf.resynth.plant.set.PlantSet;
import com.ki11erwolf.resynth.plant.set.PlantSetAPI;
import com.ki11erwolf.resynth.util.MathUtil;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.Random;

/**
 * Holds references to the various ItemGroups(Creative
 * Tabs) used by Resynth.
 */
@MethodsReturnNonnullByDefault
public final class ResynthTabs {

    /**
     * Private constructor.
     */
    private ResynthTabs(){}

    /**
     * The mods main creative tab. Used for general blocks/items.
     */
    public static final ItemGroup TAB_RESYNTH = new ItemGroup("resynth") {
        @Override public ItemStack makeIcon() {
            return new ItemStack(ResynthItems.ITEM_MINERAL_HOE);
        }
    };

    /**
     * The mods seeds tab. Holds the seed items for all plants.
     */
    public static final ItemGroup TAB_RESYNTH_SEEDS = new ItemGroup("resynth_seeds") {
        @Override public ItemStack makeIcon() {
            if(ResynthConfig.GENERAL_CONFIG.getCategory(GeneralConfig.class).useRandomPlantTabItems()) {
                return new ItemStack(PlantSetAPI.getRandomSet().getSeedsItem());
            } else {
                return new ItemStack(ResynthPlants.GOLD.getSeedsItem().getItem());
            }
        }
    };

    /**
     * The mods produce tab. Holds the produce items for all plants.
     */
    public static final ItemGroup TAB_RESYNTH_PRODUCE = new ItemGroup("resynth_produce") {
        @Override public ItemStack makeIcon() {
            if(ResynthConfig.GENERAL_CONFIG.getCategory(GeneralConfig.class).useRandomPlantTabItems()) {
                return new ItemStack(PlantSetAPI.getRandomSet().getProduceItem());
            } else {
                return new ItemStack(ResynthPlants.SPIDER_EYE.getProduceItem().asItem());
            }
        }
    };
}
