/*
 * Copyright 2018-2019 Ki11er_wolf
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

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * A set of utilities to aid with Plant Sets.
 */
public class PlantSetUtil {
    /**Private Constructor.*/
    private PlantSetUtil(){}

    /**
     * A set of utilities for tooltips placed on
     * plant set items/blocks.
     */
    public static class PlantSetTooltipUtil {
        /**Private Constructor.*/
        private PlantSetTooltipUtil(){}

        /**
         * Adds tooltips to the given tooltip array containing
         * a readable description of the plantSetProperties object.
         * This works with all PlantSetProperties instances.
         *
         * @param tooltip the tooltip array to append the tooltip to.
         * @param plantSetProperties the properties object.
         */
        public static void setPropertiesTooltip(List<ITextComponent> tooltip, PlantSetProperties plantSetProperties){
            tooltip.add(getFormattedTooltip(
                    "growth_chance", TextFormatting.GOLD, plantSetProperties.chanceToGrow())
            );

            tooltip.add(getFormattedTooltip(
                    "bonemeal_enabled", TextFormatting.AQUA, plantSetProperties.canBonemeal())
            );

            if(plantSetProperties instanceof ICrystallineSetProperties)
                setPropertiesTooltip(tooltip, (ICrystallineSetProperties)plantSetProperties);
            else if (plantSetProperties instanceof IMetallicSetProperties)
                setPropertiesTooltip(tooltip, (IMetallicSetProperties)plantSetProperties);
            else if (plantSetProperties instanceof IBiochemicalSetProperties)
                setPropertiesTooltip(tooltip, (IBiochemicalSetProperties)plantSetProperties);
        }

        /**
         * Handles Crystalline set properties and tooltips.
         */
        private static void setPropertiesTooltip(List<ITextComponent> tooltip, ICrystallineSetProperties properties){
            tooltip.add(getFormattedTooltip(
                    "plant_yield", TextFormatting.DARK_PURPLE,
                    properties.numberOfProduceDrops())
            );

            tooltip.add(getFormattedTooltip(
                    "seed_spawn_chance_from_ore", TextFormatting.GREEN,
                    properties.seedSpawnChanceFromOre())
            );

            tooltip.add(getFormattedTooltip(
                    "seed_spawn_chance_from_shard", TextFormatting.RED,
                    properties.seedSpawnChanceFromShard())
            );
        }

        /**
         * Handles Metallic set properties and tooltips.
         */
        private static void setPropertiesTooltip(List<ITextComponent> tooltip, IMetallicSetProperties properties){
            tooltip.add(getFormattedTooltip(
                    "seed_spawn_chance_from_ore", TextFormatting.GREEN,
                    properties.seedSpawnChanceFromOre())
            );

            tooltip.add(getFormattedTooltip(
                    "seed_spawn_chance_from_organic_ore", TextFormatting.BLUE,
                    properties.seedSpawnChanceFromOrganicOre())
            );
        }

        /**
         * Handles Biochemical set properties and tooltips.
         */
        private static void setPropertiesTooltip(List<ITextComponent> tooltip, IBiochemicalSetProperties properties){
            tooltip.add(getFormattedTooltip(
                    "plant_yield", TextFormatting.DARK_PURPLE,
                    properties.numberOfProduceDrops())
            );

            tooltip.add(getFormattedTooltip(
                    "seed_spawn_chance_from_mob", TextFormatting.DARK_GREEN,
                    properties.seedSpawnChanceFromMob())
            );

            tooltip.add(getFormattedTooltip(
                    "seed_spawn_chance_from_bulb", TextFormatting.YELLOW,
                    properties.seedSpawnChanceFromBulb())
            );
        }
    }

    /**
     * Gets a formatted tooltip from the active
     * language file by key.
     *
     * @param key the tooltip key.
     * @param color the color of the tooltip.
     * @param params the formatting parameters.
     * @return the formatted tooltip from the language file.
     */
    private static ITextComponent getFormattedTooltip(String key, TextFormatting color, Object... params){
        return new TextComponentString(color + I18n.format("tooltip.property.resynth." + key, params));
    }
}
