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
import com.ki11erwolf.resynth.plant.set.IMetallicSetProperties;
import com.ki11erwolf.resynth.plant.set.MetallicSetProperties;

/**
 * Defines the configuration settings used by Metallic plant sets.
 * Each Metallic plant set contains a reference their own instance
 * of {@link MetallicPlantSetConfig}.
 * <p/>
 * Unlike normal configuration classes, this one simply lays out the
 * configuration settings used by Metallic plant sets, it does
 * not specify any default values and can be instantiated multiple
 * times under different names.
 * <p/>
 * Default values are provided to the constructor using a
 * {@link MetallicSetProperties} instance, rather than
 * defined in the class, in order to specify the default values.
 */
public class MetallicPlantSetConfig extends ConfigCategory implements IMetallicSetProperties {

    /**
     * The prefix to the name of the config setting group.
     */
    private static final String PREFIX = "plant-set-";

    /**
     * Config value that specifies whether or not the plant
     * type can be grown with bonemeal.
     */
    private final BooleanConfigValue canUseBonemeal;

    /**
     * Config value that specifies the percentage
     * growth chance of the plant type.
     */
    private final DoubleConfigValue chanceToGrow;

    /**
     * Config value that specifies the percentage
     * chance seeds will spawn when the ore block
     * is blown up.
     */
    private final DoubleConfigValue seedSpawnChanceFromOre;

    /**
     * Config value that specifies the percentage
     * chance seeds will spawn when the plant produce
     * block is blown up.
     */
    private final DoubleConfigValue seedSpawnChanceFromOrganicOre;

    /**
     * Config value that determines if the growth chance config
     * value is used or not - true when being used. Allows
     * updating plant properties without the config file overriding
     * them.
     */
    private final BooleanConfigValue useConfigGrowthChanceValue;

    /**
     * Config value that determines if the seed spawn chance config
     * values are used or not - true when being used. Allows
     * updating plant properties without the config file overriding
     * them.
     */
    private final BooleanConfigValue useConfigSeedChanceValues;

    /**
     * Creates a new Metallic plant set config category for the
     * given plant set with the given default values.
     *
     * @param plantSetName the name of the plant set the this config
     *                     category instance if for.
     * @param defaultProperties default config values.
     */
    public MetallicPlantSetConfig(String plantSetName, MetallicSetProperties defaultProperties) {
        super(PREFIX + plantSetName);

        this.canUseBonemeal = new BooleanConfigValue(
                "enable-bonemeal",
                "Set this to true to allow using bonemeal on this specific plant type.",
                defaultProperties.bonemealGrowth(),
                this
        );

        this.chanceToGrow = new DoubleConfigValue(
                "chance-to-grow",
                "The chance (percentage) this plant type will grow on a random tick." +
                        "\nIncrease this number to increase the growth rate of the plant," +
                        "\ndecrease it to decrease the growth rate of the plant." +
                        "\nYOU MUST SET 'use-configuration-values-for-growth' TO 'true' TO USE THIS.",
                defaultProperties.growthProbability(),
                0.0, 100.0,
                this
        );

        this.seedSpawnChanceFromOre = new DoubleConfigValue(
                "seed-spawn-chance-from-ore",
                "The chance (percentage) that this plant types seed will spawn" +
                        "\nwhen the final products ore block is blown up with TNT." +
                        "\nSet this to 0 to prevent the seeds from spawning when blowing up the ore blocks." +
                        "\nYOU MUST SET 'use-configuration-values-for-seed-drops' TO 'true' TO USE THIS.",
                defaultProperties.seedSpawnChanceFromOre(),
                0.0, 100.0,
                this
        );

        this.seedSpawnChanceFromOrganicOre = new DoubleConfigValue(
                "seed-spawn-chance-from-organic-ore",
                "The chance (percentage) that this plant types seed will spawn" +
                        "\nwhen the plant produce block is blown up with TNT." +
                        "\nSet this to 0 to prevent the seeds from spawning when blowing" +
                        "\nup the plant produce blocks." +
                        "\nYOU MUST SET 'use-configuration-values-for-seed-drops' TO 'true' TO USE THIS.",
                defaultProperties.seedSpawnChanceFromOrganicOre(),
                0.0, 100.0,
                this
        );

        this.useConfigGrowthChanceValue = new BooleanConfigValue(
                "use-configuration-values-for-growth",
                "Prevents the configuration value for 'chance-to-grow' from being used" +
                        "\nforcing the default value to be used instead, when this is 'false'." +
                        "\nYou must set this to 'true' before the 'chance-to-grow' config value will work!",
                false, this
        );

        this.useConfigSeedChanceValues = new BooleanConfigValue(
                "use-configuration-values-for-seed-drops",
                "Prevents the configuration values for 'seed-spawn-chance-*' from being used" +
                        "\nforcing the default value to be used instead, when this is 'false'." +
                        "\nYou must set this to 'true' before the 'seed-spawn-chance-*' config values will work!",
                false, this
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean bonemealGrowth() {
        return canUseBonemeal.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float growthProbability() {
        if(!this.useConfigGrowthChanceValue.getValue())
            return Float.parseFloat(chanceToGrow.getDefaultValue().toString());
        return (float) chanceToGrow.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float seedSpawnChanceFromOre() {
        if(!this.useConfigSeedChanceValues.getValue())
            return Float.parseFloat(seedSpawnChanceFromOre.getDefaultValue().toString());
        return (float) seedSpawnChanceFromOre.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float seedSpawnChanceFromOrganicOre() {
        if(!this.useConfigSeedChanceValues.getValue())
            return Float.parseFloat(seedSpawnChanceFromOrganicOre.getDefaultValue().toString());
        return (float) seedSpawnChanceFromOrganicOre.getValue();
    }
}
