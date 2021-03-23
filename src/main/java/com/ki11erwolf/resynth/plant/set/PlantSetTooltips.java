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
package com.ki11erwolf.resynth.plant.set;

import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.GeneralConfig;
import com.ki11erwolf.resynth.plant.set.properties.AbstractBiochemicalProperties;
import com.ki11erwolf.resynth.plant.set.properties.AbstractCrystallineProperties;
import com.ki11erwolf.resynth.plant.set.properties.AbstractMetallicProperties;
import com.ki11erwolf.resynth.plant.set.properties.AbstractPlantSetProperties;
import com.ki11erwolf.resynth.util.CommonRKeys;
import com.ki11erwolf.resynth.util.ExpandingTooltip;
import com.ki11erwolf.resynth.util.Tooltip;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.List;

/**
 * A set of utilities to aid with Plant Sets.
 */
public class PlantSetTooltips {

    /**Private Constructor.*/
    private PlantSetTooltips(){}

    @SuppressWarnings("deprecation")
    public static void addWarningIfBroken(List<ITextComponent> tooltip, PlantSet<?, ?> plantSet) {
        if(!plantSet.isBroken())
            return;

        //Spacing
        new ExpandingTooltip().setCondition(CommonRKeys.SHIFT.rKey.query() || CommonRKeys.CONTROL.rKey.query())
                .setExpandedTooltip(Tooltip::addBlankLine).setCollapsedTooltip((collapsedTooltip) -> {})
                .write(tooltip);

        // Header
        tooltip.add(new StringTextComponent(TextFormatting.RED.toString() + TextFormatting.UNDERLINE
                + getFormattedTooltip("broken", TextFormatting.UNDERLINE).getUnformattedComponentText()
        ));

        // Additional information
        new ExpandingTooltip().setConditionToControlDown().setExpandedTooltip(expandedTooltip -> {
            expandedTooltip.addAll(
                    Arrays.asList(Tooltip.formatLineFeeds(new StringTextComponent(
                            WordUtils.wrap(
                                    getFormattedTooltip("broken.information", TextFormatting.DARK_RED)
                                            .getUnformattedComponentText(),
                                    ResynthConfig.GENERAL_CONFIG.getCategory(GeneralConfig.class).getTooltipCharacterLimit(),
                                    "\n", true
                            )
                    ), TextFormatting.DARK_RED))
            );
            Tooltip.addBlankLine(expandedTooltip);
        }).setCollapsedTooltip((x) -> {}).write(Tooltip.addBlankLine(tooltip));
    }

    /**
     * Adds tooltips to the given tooltip array containing
     * a readable description of the plantSetProperties object.
     * This works with all PlantSetProperties instances.
     *
     * @param tooltip the tooltip array to append the tooltip to.
     */
    public static void setPropertiesTooltip(List<ITextComponent> tooltip, PlantSet<?, ?> set){
        AbstractPlantSetProperties properties = set.serverPlantSetProperties().orElse(null);

        if(properties != null) {
            tooltip.add(getFormattedTooltip(
                    "synchronized", TextFormatting.DARK_GREEN,
                    TextFormatting.BOLD.toString() + TextFormatting.ITALIC.toString()
            ));
            tooltip.add(new StringTextComponent(""));
        } else properties = set.getPlantSetProperties();

        // Bonemeal
        boolean bonemeal = properties.bonemealGrowth();
        tooltip.add(getFormattedTooltip(bonemeal ? "bonemeal_effective" : "bonemeal_ineffective",
                TextFormatting.RED, bonemeal ? TextFormatting.DARK_GREEN : TextFormatting.DARK_RED
        ));

        // Growth Rate Multiplier
        tooltip.add(getFormattedTooltip("growth_rate",
                TextFormatting.BLUE, TextFormatting.GOLD, properties.growthProbability() + "%"
        ));

        //Seed Sources
        setSourcesForSeeds(tooltip, set);

        // Various set type properties
        if(properties instanceof AbstractCrystallineProperties)
            setCrystallinePropertiesTooltip(tooltip, (AbstractCrystallineProperties) properties);
        else if (properties instanceof AbstractMetallicProperties)
            setMetallicPropertiesTooltip(tooltip, (AbstractMetallicProperties) properties);
        else if (properties instanceof AbstractBiochemicalProperties)
            setBiochemicalPropertiesTooltip(tooltip, (AbstractBiochemicalProperties) properties);

        // Produce Yield
        int produceYield = set.serverPlantSetProduceProperties().orElse(set.getProduceProperties()).produceYield();
        tooltip.add(getFormattedTooltip(produceYield == 1 ? "produce_yield_singular" : "produce_yield",
                TextFormatting.DARK_PURPLE, TextFormatting.GOLD, produceYield, TextFormatting.DARK_PURPLE
        ));
    }

    private static void setSourcesForSeeds(List<ITextComponent> tooltip, PlantSet<?, ?> set) {
        if(set.isBroken() || set.getSeedSources() == null)
            return;

        int limit = 5;
        int seedSourcesCount = set.getSeedSources().length;
        String s = (seedSourcesCount > limit) ? " +" + (seedSourcesCount - limit) : "";

        if(set instanceof CrystallineSet) {
            // Crystalline Seed Sources
            tooltip.add(getFormattedTooltip("seed_sources", TextFormatting.GREEN,
                    getFormattedTooltip("seed_sources.crystalline", TextFormatting.DARK_GREEN).getString(),
                    TextFormatting.WHITE, Tooltip.toTextComponent(Arrays.toString(
                            Arrays.stream(set.getSeedSources(Block[].class)).limit(limit).map(
                                    in -> TextFormatting.GOLD + in.getTranslatedName().getString() + TextFormatting.WHITE
                            ).toArray()
                    ) + s).getString()
            ));
        } else if (set instanceof MetallicSet) {
            // Metallic Seed Sources
            tooltip.add(getFormattedTooltip("seed_sources", TextFormatting.GREEN,
                    getFormattedTooltip("seed_sources.metallic", TextFormatting.DARK_GREEN).getString(),
                    TextFormatting.WHITE, Tooltip.toTextComponent(
                            Arrays.toString(
                                    Arrays.stream(set.getSeedSources(Block[].class)).limit(limit).map(
                                            in -> TextFormatting.GOLD + in.getTranslatedName().getString() + TextFormatting.WHITE
                                    ).toArray()
                            ) + s).getString()
            ));
        } else if (set instanceof BiochemicalSet) {
            // Biochemical Seed Sources
            tooltip.add(getFormattedTooltip("seed_sources", TextFormatting.GREEN,
                    getFormattedTooltip("seed_sources.biochemical", TextFormatting.DARK_GREEN).getString(),
                    TextFormatting.WHITE, Tooltip.toTextComponent(Arrays.toString(
                            Arrays.stream(set.getSeedSources(EntityType[].class)).limit(limit).map(
                                    in -> TextFormatting.GOLD + in.getName().getString() + TextFormatting.WHITE
                            ).toArray()
                    ) + s).getString()
            ));
        }
    }

    /**
     * Handles Crystalline set properties and tooltips.
     */
    private static void setCrystallinePropertiesTooltip(List<ITextComponent> tooltip, AbstractCrystallineProperties properties){
        tooltip.add(getFormattedTooltip(properties.plantYield() == 1 ? "plant_yield_singular": "plant_yield",
                TextFormatting.DARK_AQUA, TextFormatting.GOLD, properties.plantYield(), TextFormatting.DARK_AQUA
        ));

        tooltip.add(getFormattedTooltip("ore_seed_drop_rate", TextFormatting.AQUA,
                TextFormatting.GOLD, properties.seedSpawnChanceFromOre() + "%", TextFormatting.AQUA
        ));

        tooltip.add(getFormattedTooltip("produce_seed_drop_rate", TextFormatting.LIGHT_PURPLE,
                TextFormatting.GOLD, properties.seedSpawnChanceFromShard() + "%", TextFormatting.LIGHT_PURPLE
        ));
    }

    /**
     * Handles Metallic set properties and tooltips.
     */
    private static void setMetallicPropertiesTooltip(List<ITextComponent> tooltip, AbstractMetallicProperties properties){
        tooltip.add(getFormattedTooltip("ore_seed_drop_rate", TextFormatting.AQUA,
                TextFormatting.GOLD, properties.seedSpawnChanceFromOre() + "%", TextFormatting.AQUA
        ));

        tooltip.add(getFormattedTooltip("produce_seed_drop_rate", TextFormatting.LIGHT_PURPLE,
                TextFormatting.GOLD, properties.seedSpawnChanceFromOrganicOre() + "%", TextFormatting.LIGHT_PURPLE
        ));
    }

    /**
     * Handles Biochemical set properties and tooltips.
     */
    private static void setBiochemicalPropertiesTooltip(List<ITextComponent> tooltip, AbstractBiochemicalProperties properties){
        tooltip.add(getFormattedTooltip(properties.plantYield() == 1 ? "plant_yield_singular": "plant_yield",
                TextFormatting.DARK_AQUA, TextFormatting.GOLD, properties.plantYield(), TextFormatting.DARK_AQUA
        ));

        tooltip.add(getFormattedTooltip("mob_seed_drop_rate", TextFormatting.AQUA,
                TextFormatting.GOLD, properties.seedSpawnChanceFromMob() + "%", TextFormatting.AQUA
        ));

        tooltip.add(getFormattedTooltip("produce_seed_drop_rate", TextFormatting.LIGHT_PURPLE,
                TextFormatting.GOLD, properties.seedSpawnChanceFromBulb() + "%", TextFormatting.LIGHT_PURPLE
        ));
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
        return new StringTextComponent(color + I18n.format("tooltip.resynth.plantset.property." + key, params));
    }
}

