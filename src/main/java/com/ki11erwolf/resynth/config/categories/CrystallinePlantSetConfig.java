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
import com.ki11erwolf.resynth.config.DoubleConfigValue;
import com.ki11erwolf.resynth.config.IntegerConfigValue;
import com.ki11erwolf.resynth.plant.set.CrystallineSetProperties;
import com.ki11erwolf.resynth.plant.set.ICrystallineSetProperties;

/**
 * Defines the configuration settings used by Crystalline plant sets.
 * Each Crystalline plant set contains a reference their own instance
 * of {@link CrystallinePlantSetConfig}.
 * <p/>
 * Unlike normal configuration classes, this one simply lays out the
 * configuration settings used by Crystalline plant sets, it does
 * not specify any default values and can be instantiated multiple
 * times under different names.
 * <p/>
 * Default values are provided to the constructor using a
 * {@link CrystallineSetProperties} instance, rather than
 * defined in the class.
 */
//A thought: toggle that enables/disables using the config categories... for future updates that change config values.
public class CrystallinePlantSetConfig extends ConfigCategory implements ICrystallineSetProperties {

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
     * Config value that specifies the number of
     * produce items the plant type drops when broken.
     */
    private final IntegerConfigValue numberOfProduceDrops;

    /**
     * Config value that specifies the percentage chance that
     * seeds will spawn from the resources ore block when mined.
     */
    private final DoubleConfigValue seedSpawnChanceFromOre;

    /**
     * Config value that specifies the percentage chance that
     * seeds will spawn from the plant produce when left in
     * water.
     */
    private final DoubleConfigValue seedSpawnChanceFromShard;

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
     * Config value that stores the amount of resource items to
     * be crafted when placing seeds in a crafting table.
     */
    private final IntegerConfigValue resourcesPerSeedsConfig;

    /**
     * Creates a new Crystalline plant set config category for the
     * given plant set with the given default values.
     *
     * @param plantSetName the name of the plant set the this config
     *                     category instance if for.
     * @param defaultProperties default config values.
     */
    public CrystallinePlantSetConfig(String plantSetName, ICrystallineSetProperties defaultProperties) {
        super(PREFIX + plantSetName);

        this.canUseBonemeal = new BooleanConfigValue(
                "enable-bonemeal",
                "Set this to true to allow using bonemeal on this specific plant type.",
                defaultProperties.canBonemeal(),
                this
        );

        this.chanceToGrow = new DoubleConfigValue(
                "chance-to-grow",
                "The chance (percentage) this plant type will grow on a random tick." +
                          "\nIncrease this number to increase the growth rate of the plant," +
                          "\ndecrease it to decrease the growth rate of the plant." +
                          "\nYOU MUST SET 'use-configuration-values-for-growth' TO 'true' TO USE THIS.",
                defaultProperties.chanceToGrow(),
                0.0, 100.0,
                this
        );

        this.numberOfProduceDrops = new IntegerConfigValue(
                "number-of-produce-drops",
                "The number of produce item drops this plant type will drop when fully grown" +
                          "\nand broken.",
                defaultProperties.numberOfProduceDrops(),
                1, 64,
                this
        );

        this.seedSpawnChanceFromOre = new DoubleConfigValue(
                "seed-spawn-chance-from-ore",
                "The chance (percentage) that this plant types seed will spawn" +
                        "\nwhen mining the growable resources ore block. Set this to 0 to" +
                        "\nprevent the seeds from spawning when mining the ore blocks." +
                        "\nYOU MUST SET 'use-configuration-values-for-seed-drops' TO 'true' TO USE THIS.",
                defaultProperties.seedSpawnChanceFromOre(),
                0.0, 100.0,
                this
        );

        this.seedSpawnChanceFromShard = new DoubleConfigValue(
                "seed-spawn-chance-from-shard",
                "The chance (percentage) that this plant types seed will spawn" +
                        "\nwhen the plants produce (shard) is left in water to despawn." +
                        "\nSet this to 0 to prevent seed spawning from shards." +
                        "\nYOU MUST SET 'use-configuration-values-for-seed-drops' TO 'true' TO USE THIS.",
                defaultProperties.seedSpawnChanceFromShard(),
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

        this.resourcesPerSeedsConfig = new IntegerConfigValue(
                "number-of-resources-crafted-from-seeds",
                "The amount of resource items, that this plant set grows, that will be crafted when\n" +
                         "placing this plants seeds in a crafting table. A value of 0 (zero) will disable this\n" +
                         "feature for the particular plant and plant set. ",
                defaultProperties.resourcesPerSeeds(), 0, 64,
                this
        );
    }

    /**
     * {@inheritDoc}
     * @return the value specified by config.
     */
    @Override
    public boolean canBonemeal() {
        return this.canUseBonemeal.getValue();
    }

    /**
     * {@inheritDoc}.
     * Specified by config.
     */
    @Override
    public float chanceToGrow() {
        if(!this.useConfigGrowthChanceValue.getValue())
            return Float.parseFloat(chanceToGrow.getDefaultValue().toString());
        return (float) this.chanceToGrow.getValue();
    }

    /**
     * {@inheritDoc}.
     * Specified by config.
     */
    @Override
    public int numberOfProduceDrops() {
        return this.numberOfProduceDrops.getValue();
    }

    /**
     * {@inheritDoc}.
     * Specified by config.
     */
    @Override
    public float seedSpawnChanceFromOre() {
        if(!this.useConfigSeedChanceValues.getValue())
            return Float.parseFloat(seedSpawnChanceFromOre.getDefaultValue().toString());
        return (float) this.seedSpawnChanceFromOre.getValue();
    }

    /**
     * {@inheritDoc}.
     * Specified by config.
     */
    @Override
    public float seedSpawnChanceFromShard() {
        if(!this.useConfigSeedChanceValues.getValue())
            return Float.parseFloat(seedSpawnChanceFromShard.getDefaultValue().toString());
        return (float) this.seedSpawnChanceFromShard.getValue();
    }

    /**
     * {@inheritDoc}
     *
     * @return the value given to this configuration option through
     * the players configuration file.
     */
    @Override
    public int resourcesPerSeeds() {
        return resourcesPerSeedsConfig.getValue();
    }
}
