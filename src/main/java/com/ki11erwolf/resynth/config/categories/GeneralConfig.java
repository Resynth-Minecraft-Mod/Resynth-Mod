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
package com.ki11erwolf.resynth.config.categories;

import com.ki11erwolf.resynth.config.BooleanConfigValue;
import com.ki11erwolf.resynth.config.ConfigCategory;
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
            "Allows auto-farming using hoppers when true, where plants will drop their " +
                      "produce in a hopper underneath the plant when fully grown, starting the " +
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
            40, 20, 70, this
    );

    /**
     * Config value that allows enabling or disabling light penalties in plant growth.
     */
    private final BooleanConfigValue enableLightPenalty = new BooleanConfigValue(
            "enable-light-penalty",
            "Allows turning on or off light penalties in plant growth." +
                     "\nWhen set to 'true', plants will grow slightly slower with less light.",
            true, this
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
    public boolean isHopperAutofarmingEnabled(){
        return enableHopperAutoFarming.getValue();
    }

    /**
     * @return the config defined value that specifies the maximum
     * number of characters that item/block descriptive tooltips can
     * have on one line.
     */
    public int getTooltipCharacterLimit() { return tooltipCharacterLimit.getValue(); }

    /**
     * @return the config defined value that specifies if light penalties to plant
     * growth is enabled or disabled.
     */
    public boolean enableLightPenalty() { return enableLightPenalty.getValue(); }

    /**
     * @return the config defined value that specifies if crafting recipes for
     * resources are enabled or disabled.
     */
    public boolean enableResourceRecipes() { return enableResourceRecipes.getValue(); }
}
