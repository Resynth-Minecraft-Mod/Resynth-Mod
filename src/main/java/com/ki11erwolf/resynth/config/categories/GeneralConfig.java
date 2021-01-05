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
package com.ki11erwolf.resynth.config.categories;

import com.ki11erwolf.resynth.config.BooleanConfigValue;
import com.ki11erwolf.resynth.config.ConfigCategory;
import com.ki11erwolf.resynth.config.DoubleConfigValue;
import com.ki11erwolf.resynth.config.IntegerConfigValue;

/**
 * General configuration settings used by Resynth.
 */
public class GeneralConfig extends ConfigCategory {

    // *************
    // Config Values
    // *************

    /**
     * Analytics enable/disable switch.
     */
    private final BooleanConfigValue enableAnalytics = new BooleanConfigValue(
            "enable analytics",
            "Enables or disables all the analytical features of the mod.\n" +
                      "Including usage and error reports (all sent reports are anonymous).\n" +
                      "False to disable, true to enable.",
            true,
            this
    );

    /**
     * Debug/Development features enable/disable flag.
     */
    private final BooleanConfigValue enableDebugHelp = new BooleanConfigValue(
            "enable debug help",
            "Enables features related to debugging and development.\n" +
                      "It's unlikely you'd want to enable this.",
            false,
            this
    );

    /**
     * Helpful tooltips enable/disable flag.
     */
    private final BooleanConfigValue enableHelpTooltips = new BooleanConfigValue(
            "enable descriptive tooltips",
            "Set to true to show detailed and descriptive tooltips on all Resynth blocks & items.",
            true,
            this
    );

    /**
     * Config properties that allows enabling/disabling auto-farming using hoppers.
     */
    private final BooleanConfigValue enableHopperAutoFarming = new BooleanConfigValue(
            "allow hopper auto-farming",
            "Allows auto-farming using hoppers when true, where plants will drop their\n" +
                      "produce in a hopper underneath the plant when fully grown, starting the\n" +
                      "growth cycle over.",
            true, this
    );

    /**
     * Config value that allows setting the maximum number
     * of characters that item/block descriptive tooltips
     * can have on one line.
     */
    private final IntegerConfigValue tooltipCharacterLimit = new IntegerConfigValue(
            "tooltip-character-limit",
            "The maximum amount of characters allowed on a single line in an " +
                    "item or block tooltip.",
            60, 20, 100, this
    );

    /**
     * Config value that allows enabling or disabling light penalties in plant growth.
     */
    private final BooleanConfigValue enableBrightnessBasedGrowth = new BooleanConfigValue(
            "is-growth-affected-by-light",
            "Determines if the lighting in the world affects the growth rates and chances of planted crops.\n" +
                     "When 'true', the growth rates of plants change slightly based on the amount of ambient light\n" +
                     "and brightness on the plant. Low to moderate amounts of light negatively affect growth, while\n" +
                     "very bright light  positively affects growth.",
            true, this
    );

    private final IntegerConfigValue lightLevelZeroPoint = new IntegerConfigValue(
            "light-level-zero-point",
            "The light level at which the growth multiplier is 0 (zero), making any lower light levels decrease the\n" +
                     "multiplier while any higher light levels increase the multiplier. Said in layman's terms: sets the point\n" +
                     "where light levels change from decreasing the growth multiplier to increasing the growth multiplier.",
            11, 9, 13,
            this
    );

    private final DoubleConfigValue lightLevelWorth = new DoubleConfigValue(
            "worth-of-light-level-as-growth-multiplier",
            "The percentage (in decimal form: 0.04 = 4%) each level of light (above or below the zero point) is worth.\n" +
                     "Each level of light will change the multiplier by this value - minimum. So, at a worth of 0.04, 3 levels\n" +
                     "above the zero point will add +0.12 to the multiplier, increasing growth by 12%. This works the same way in\n" +
                     "the reverse direction.",
            0.04, 0.001, 0.1,
            this
    );

    private final DoubleConfigValue lightLevelInterest = new DoubleConfigValue(
            "interest-increase-on-worth-per-level",
            "The amount of interest added to the worth of a light level, for each light level above or below the zero\n" +
                     "point. Put more simply, it's the amount each additional light level adds to the worth of a light level. \n" +
                     "This value controls the exponential growth of the cost per light level.",
            0.01, -1, 1,
            this
    );

    /**
     * Config value that allows enabling or disabling crafting recipes for resources.
     */
    private final BooleanConfigValue enableResourceRecipes = new BooleanConfigValue(
            "enable-core-resource-recipes",
            "Allows enabling (or disabling) the addition of various crafting " +
                    "recipes that can be used to craft Resynth's core resources:\nMineral Rocks, " +
                    "Calvinite Crystals, Sylvanite Crystals, ect. These resources are NOT normally " +
                    "craftable, instead, they need\nto mined or otherwise obtained from the world." +
                    "\nEnable this config option if the ores/resources are not generating naturally, " +
                    "or if the resources cannot otherwise be obtained.", false, this
    );

    /**
     * The config value that stores the value enabling or disabling crafting Crystalline seeds into resources.
     */
    private final BooleanConfigValue enableCraftingCrystallineSeeds = new BooleanConfigValue(
            "enable-crafting-crystalline-seeds",
            "All Crystalline type plants and plant sets offer the ability to directly craft\n" +
                     "the resource grown by this plant by placing the plants seeds in a crafting table.\n" +
                     "This option is available if you wish to be able turn seeds mined from ores into the\n" +
                     "resource that would normally drop, without having to grow it, or for any other reason.\n" +
                     "Set this option to 'true' to enable the functionality.",
            false, this
    );

    /**
     * Constructor.
     */
    public GeneralConfig() {
        super("general-resynth-config");
    }

    // ****************
    // Public Accessors
    // ****************

    /**
     * @return {@code true} if and only if the config
     * allows enabling analytics.
     */
    public boolean isAnalyticsEnabled(){
        return enableAnalytics.getValue();
    }

    /**
     * @return {@code true} if dev features
     * are enabled.
     */
    public boolean isDevHelpEnabled(){
        return enableDebugHelp.getValue();
    }

    /**
     * @return {@code true} if the helpful/explanatory/descriptive
     * tooltips are enabled for blocks and items.
     */
    public boolean areTooltipsEnabled(){
        return this.enableHelpTooltips.getValue();
    }

    /**
     * @return the config defined value that specifies if auto-farming
     * using hoppers is enabled or disabled.
     */
    public boolean isHopperAutoFarmingEnabled(){
        return enableHopperAutoFarming.getValue();
    }

    /**
     * @return the config defined value that specifies the maximum
     * number of characters that item/block descriptive tooltips can
     * have on one line.
     */
    public int getTooltipCharacterLimit() { return tooltipCharacterLimit.getValue(); }

    /**
     * @return the config defined value that specifies if the ambient lighting and
     * brightness on the plant affects its growth rate.
     */
    public boolean isGrowthLightDependent() { return enableBrightnessBasedGrowth.getValue(); }

    public int getLightLevelZeroPoint() {
        return lightLevelZeroPoint.getValue();
    }

    public double getWorthPerLightLevel() {
        return lightLevelWorth.getValue();
    }

    public double getInterestPerLightLevel() {
        return lightLevelInterest.getValue();
    }

    /**
     * @return the config defined value that specifies if crafting recipes for
     * resources are enabled or disabled.
     */
    public boolean enableResourceRecipes() { return enableResourceRecipes.getValue(); }
}
