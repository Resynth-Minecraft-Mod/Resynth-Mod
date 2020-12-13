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

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
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
        public static void setPropertiesTooltip(List<ITextComponent> tooltip, IPlantSetProperties plantSetProperties,
                                                IPlantSetProduceProperties produceProperties){
            if(!Minecraft.getInstance().isSingleplayer()) {
                tooltip.add(getFormattedTooltip("stats_disabled", TextFormatting.RED));
                return;
            }

            tooltip.add(getFormattedTooltip(
                    "growth_chance", TextFormatting.GOLD,
                    TextFormatting.YELLOW, plantSetProperties.chanceToGrow(), TextFormatting.GOLD
            ));

            boolean bonemeal = plantSetProperties.canBonemeal();

            tooltip.add(getFormattedTooltip(
                    bonemeal ? "bonemeal_enabled" : "bonemeal_disabled",
                    bonemeal ? TextFormatting.GREEN : TextFormatting.RED,
                    TextFormatting.YELLOW, plantSetProperties.canBonemeal(),
                    bonemeal ? TextFormatting.GREEN : TextFormatting.RED
            ));

            if(plantSetProperties instanceof ICrystallineSetProperties)
                setPropertiesTooltip(tooltip, (ICrystallineSetProperties)plantSetProperties);
            else if (plantSetProperties instanceof IMetallicSetProperties)
                setPropertiesTooltip(tooltip, (IMetallicSetProperties)plantSetProperties);
            else if (plantSetProperties instanceof IBiochemicalSetProperties)
                setPropertiesTooltip(tooltip, (IBiochemicalSetProperties)plantSetProperties);

            if(produceProperties == null)
                return;

            tooltip.add(getFormattedTooltip("produce_yield",
                    TextFormatting.DARK_AQUA, TextFormatting.YELLOW, produceProperties.resourceCount(), TextFormatting.DARK_AQUA));
        }

        /**
         * Handles Crystalline set properties and tooltips.
         */
        private static void setPropertiesTooltip(List<ITextComponent> tooltip, ICrystallineSetProperties properties){
            tooltip.add(getFormattedTooltip(
                    "seed_spawn_chance_from_ore", TextFormatting.AQUA,
                    TextFormatting.YELLOW, properties.seedSpawnChanceFromOre(), TextFormatting.AQUA
            ));

            tooltip.add(getFormattedTooltip(
                    "seed_spawn_chance_from_shard", TextFormatting.DARK_BLUE,
                    TextFormatting.YELLOW, properties.seedSpawnChanceFromShard(), TextFormatting.DARK_BLUE
            ));

            tooltip.add(getFormattedTooltip(
                    properties.numberOfProduceDrops() > 1 ? "plant_yields" : "plant_yield", TextFormatting.DARK_PURPLE,
                    TextFormatting.YELLOW, properties.numberOfProduceDrops(), TextFormatting.DARK_PURPLE
            ));
        }

        /**
         * Handles Metallic set properties and tooltips.
         */
        private static void setPropertiesTooltip(List<ITextComponent> tooltip, IMetallicSetProperties properties){
            tooltip.add(getFormattedTooltip(
                    "seed_spawn_chance_from_ore", TextFormatting.AQUA,
                    TextFormatting.YELLOW, properties.seedSpawnChanceFromOre(), TextFormatting.AQUA
            ));

            tooltip.add(getFormattedTooltip(
                    "seed_spawn_chance_from_organic_ore", TextFormatting.BLUE,
                    TextFormatting.YELLOW, properties.seedSpawnChanceFromOrganicOre(), TextFormatting.BLUE
            ));
        }

        /**
         * Handles Biochemical set properties and tooltips.
         */
        private static void setPropertiesTooltip(List<ITextComponent> tooltip, IBiochemicalSetProperties properties){
            tooltip.add(getFormattedTooltip(
                    "seed_spawn_chance_from_mob", TextFormatting.DARK_GREEN,
                    TextFormatting.YELLOW, properties.seedSpawnChanceFromMob(), TextFormatting.DARK_GREEN
            ));

            tooltip.add(getFormattedTooltip(
                    "seed_spawn_chance_from_bulb", TextFormatting.DARK_RED,
                    TextFormatting.YELLOW, properties.seedSpawnChanceFromBulb(), TextFormatting.DARK_RED
            ));

            tooltip.add(getFormattedTooltip(
                    properties.numberOfProduceDrops() > 1 ? "plant_yields" : "plant_yield", TextFormatting.DARK_PURPLE,
                    TextFormatting.YELLOW, properties.numberOfProduceDrops(), TextFormatting.DARK_PURPLE
            ));
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
        return new StringTextComponent(color + I18n.format("tooltip.property.resynth." + key, params));
    }
}
