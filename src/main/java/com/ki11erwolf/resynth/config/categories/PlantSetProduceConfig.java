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
import com.ki11erwolf.resynth.plant.set.IPlantSetProduceProperties;

import java.util.Objects;

/**
 * A type of {@link ConfigCategory} that stores and retrieves the configuration
 * values for a specific PlantSet's {@link IPlantSetProduceProperties}. Can be
 * instantated multiple times under different unique identifying names to create
 * a configuration instance for each plant set. Allows specifing default values.
 */
public class PlantSetProduceConfig extends ConfigCategory implements IPlantSetProduceProperties {

    /**
     * The prefix prepended to the unique name of this config setting group.
     */
    private static final String PREFIX = "plant-set-";

    /**
     * The suffix appended to the unique name of this config setting group.
     */
    private static final String SUFFIX = ".produce-smelting";

    /**
     * The configuration setting object that defines and stores the
     * config file value which determines the amount of resource
     * items smelted from a single item of plant produce.
     */
    private final IntegerConfigValue resourceCountConfig;

    /**
     * The configuration setting object that defines and stores the
     * configuration value which determines the amount of time, in
     * ticks, a single produce item takes to smelt.
     */
    private final IntegerConfigValue smeltingTimeConfig;

    /**
     * The configuration setting object that defines and stores the
     * configuration value which determines the amount of experience
     * a furnace will give the player after smelting a single item
     * of produce.
     */
    private final DoubleConfigValue experienceWorthConfig;

    /**
     * The configuration setting object that defines and stores the
     * configuration value which determines if the values in the
     * config file are used or not.
     */
    private final BooleanConfigValue useConfigValues;

    /**
     * @param uniqueName The unique name of this specific plant set
     *                   produce configuration settings. Should be the
     *                   same as the normal plant set configuration.
     * @param defaultProperties the configuration setting values that
     *                          will used by default.
     */
    public PlantSetProduceConfig(String uniqueName, IPlantSetProduceProperties defaultProperties) {
        super(PREFIX + Objects.requireNonNull(uniqueName) + SUFFIX);

        this.resourceCountConfig = new IntegerConfigValue(
                "number-of-resources-when-smelted",
                "The amount of resource items a player will get for smelting\n" +
                         "a single item of this plants produce. Change this number to\n" +
                         "increase or decrease the amount of resources a single item of\n" +
                         "produce gives when smelted in a furnace.",
                defaultProperties.produceYield(), 1, 64,
                this
        );

        this.smeltingTimeConfig = new IntegerConfigValue(
                "number-of-ticks-to-smelt",
                "The amount of time it takes a furnace to smelt a single item\n" +
                         "of this plants produce. The time is measured in ticks, where\n" +
                         "1 second is 20 ticks, so 1 ticks is 50 milliseconds. Most\n" +
                         "items take 200 ticks (10 seconds) to smelt.",
                defaultProperties.timePerYield(), 1, 200 * 10,
                this
        );

        this.experienceWorthConfig = new DoubleConfigValue(
                "amount-of-xp-when-smelted",
                "The amount of xp (experience) a player will get from a furnace\n" +
                         "after smelting a single item of this plants produce. It takes\n" +
                         "exactly 7.5 points to go from level 0 to level 1. Going from\n" +
                         "level 1 to level 2 will take slightly more than 7.5 points.",
                defaultProperties.experiencePoints(), 1, 1000,
                this
        );

        this.useConfigValues = new BooleanConfigValue(
                "use-config-values",
                "Enable this configuration option to use the other options in this category!\n" +
                         "Controls if the configuration values in this category are actually\n" +
                         "used instead of the default values. By default, changing the values of\n" +
                         "the other setting in this category will have no effect unless this value\n" +
                         "is set to 'true'. Note that enabling this will prevent changes to these\n" +
                         "setting in future Resynth updates from being applied.",
                false, this
        );
    }

    /**
     * {@inheritDoc}
     *
     * @return the exact value stored in the players configuration
     * file for this property. The value will be the default value
     * if the player hasn't enabled or modified it.
     */
    @Override
    public int produceYield() {
        if(useConfigValues.getValue())
            return resourceCountConfig.getValue();
        else return Integer.parseInt(resourceCountConfig.getDefaultValue().toString());
    }

    /**
     * {@inheritDoc}
     *
     * @return the exact value stored in the players configuration
     * file for this property. The value will be the default value
     * if the player hasn't enabled or modified it.
     */
    @Override
    public int timePerYield() {
        if(useConfigValues.getValue())
            return smeltingTimeConfig.getValue();
        else return Integer.parseInt(smeltingTimeConfig.getDefaultValue().toString());
    }

    /**
     * {@inheritDoc}
     *
     * @return the exact value stored in the players configuration
     * file for this property. The value will be the default value
     * if the player hasn't enabled or modified it.
     */
    @Override
    public double experiencePoints() {
        if(useConfigValues.getValue())
            return experienceWorthConfig.getValue();
        else return Double.parseDouble(experienceWorthConfig.getDefaultValue().toString());
    }
}
