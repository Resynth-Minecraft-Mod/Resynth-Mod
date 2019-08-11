package com.ki11erwolf.resynth.config.categories;

import com.ki11erwolf.resynth.config.ConfigCategory;
import com.ki11erwolf.resynth.config.ConfigDoubleValue;
import com.ki11erwolf.resynth.config.ConfigIntegerValue;

/**
 * Config settings for the {@link com.ki11erwolf.resynth.block.BlockMineralOre}
 * block.
 */
public class MineralOreConfig extends ConfigCategory {

    // *************
    // Config Values
    // *************

    /**
     * The config value definition for the base (minimum) number of drops
     * Mineral Stone will give.
     */
    private final ConfigIntegerValue baseDrops = new ConfigIntegerValue(
            "number of base drops",
            "The minimum number of Mineral Rocks dropped by Mineral Stone when it's mined.",
            1,
            0, 64,
            this
    );

    /**
     * The config value definition for the amount of extra drops
     * Mineral Stone will give.
     */
    private final ConfigIntegerValue extraDrops = new ConfigIntegerValue(
            "number of extra drops",
            "The amount of extra Mineral Rocks Mineral Stone will give" +
                      "\nwhen mined (based on [chance-of-extra-drops]).",
            2,
            0, 64,
            this
    );

    /**
     * The config value definition for the percentage chance of Mineral
     * Stone dropping extra drops.
     */
    private final ConfigDoubleValue extraDropsChance = new ConfigDoubleValue(
            "chance of extra drops",
            "The chance of Mineral Stone giving extra Mineral Rocks when mined (0% to 100%).",
            20,
            0, 100,
            this
    );

    /**
     * The config value definition for the minimum number of experience orbs
     * Mineral Stone will drop when mined.
     */
    private final ConfigIntegerValue minimumExperience = new ConfigIntegerValue(
            "minimum experience dropped",
            "The minimum number of experience orbs Mineral Stone will drop when mined.",
            3,
            0, 100,
            this
    );

    /**
     * The config value definition for the maximum number of experience orbs
     * Mineral Stone will drop when mined
     */
    private final ConfigIntegerValue maximumExperience = new ConfigIntegerValue(
            "maximum experience dropped",
            "The maximum number of experience orbs Mineral Stone will drop when mined.",
            7,
            0, 100,
            this
    );

    /**
     * Creates a new, unique configuration category
     * grouping.
     */
    public MineralOreConfig() {
        super("mineral ore");
    }

    // ****************
    // Public Accessors
    // ****************

    /**
     * @return The base (minimum) number of drops Mineral Stone should
     * give as determined by config.
     */
    public int getBaseDrops(){
        return this.baseDrops.getValue();
    }

    /**
     * @return the percentage chance of Mineral Stone dropping
     * extra drops as determined by config.
     */
    public double getExtraDropsChance(){
        return this.extraDropsChance.getValue();
    }

    /**
     * @return the number of extra drops Mineral Stone will
     * drop as determined by config.
     */
    public int getExtraDrops(){
        return this.extraDrops.getValue();
    }

    /**
     * @return the minimum number of experience orbs Mineral
     * Stone will drop as determined by config.
     */
    public int getMinExpDropped(){
        return this.minimumExperience.getValue();
    }

    /**
     * @return the maximum number of experience orbs Mineral
     * Stone will drop as determined by config.
     */
    public int getMaxExpDropped(){
        return this.maximumExperience.getValue();
    }
}
