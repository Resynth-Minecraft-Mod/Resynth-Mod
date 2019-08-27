package com.ki11erwolf.resynth.config.categories;

import com.ki11erwolf.resynth.config.BooleanConfigValue;
import com.ki11erwolf.resynth.config.ConfigCategory;
import com.ki11erwolf.resynth.config.DoubleConfigValue;
import com.ki11erwolf.resynth.plantsets.set.IMetallicSetProperties;
import com.ki11erwolf.resynth.plantsets.set.MetallicSetProperties;

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
                defaultProperties.canBonemeal(),
                this
        );

        this.chanceToGrow = new DoubleConfigValue(
                "chance-to-grow",
                "The chance (percentage) this plant type will grow on a random tick." +
                        "\nIncrease this number to increase the growth rate of the plant," +
                        "\ndecrease it to decrease the growth rate of the plant.",
                defaultProperties.chanceToGrow(),
                0.0, 100.0,
                this
        );

        this.seedSpawnChanceFromOre = new DoubleConfigValue(
                "seed-spawn-chance-from-ore",
                "The chance (percentage) that this plant types seed will spawn" +
                        "\nwhen the final products ore block is blown up with TNT." +
                        "\nSet this to 0 to prevent the seeds from spawning when blowing up the ore blocks.",
                defaultProperties.seedSpawnChanceFromOre(),
                0.0, 100.0,
                this
        );

        this.seedSpawnChanceFromOrganicOre = new DoubleConfigValue(
                "seed-spawn-chance-from-organic-ore",
                "The chance (percentage) that this plant types seed will spawn" +
                        "\nwhen the plant produce block is blown up with TNT." +
                        "\nSet this to 0 to prevent the seeds from spawning when blowing" +
                        "\nup the plant produce blocks.",
                defaultProperties.seedSpawnChanceFromOrganicOre(),
                0.0, 100.0,
                this
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canBonemeal() {
        return canUseBonemeal.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float chanceToGrow() {
        return (float) chanceToGrow.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float seedSpawnChanceFromOre() {
        return (float) seedSpawnChanceFromOre.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float seedSpawnChanceFromOrganicOre() {
        return (float) seedSpawnChanceFromOrganicOre.getValue();
    }
}
