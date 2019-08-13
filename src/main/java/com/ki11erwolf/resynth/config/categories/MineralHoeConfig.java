package com.ki11erwolf.resynth.config.categories;

import com.ki11erwolf.resynth.config.BooleanConfigValue;
import com.ki11erwolf.resynth.config.ConfigCategory;
import com.ki11erwolf.resynth.config.IntegerConfigValue;

/**
 * Holds the configuration settings for the
 * {@link com.ki11erwolf.resynth.item.ItemMineralHoe}.
 */
public class MineralHoeConfig extends ConfigCategory {

    /**
     * Config definition that allows enabling/disabling using a Mineral
     * Hoe to turn dirt/grass into Mineral Soil.
     */
    private final BooleanConfigValue enabled = new BooleanConfigValue(
            "enable-mineral-hoe",
            "Set to false to prevent the Mineral Hoe from being used\n" +
                      "to turn dirt or grass into Mineral Enriched Soil.",
            true,
            this
    );

    /**
     * Config definition that allows enabling/disabling particles spawning
     * when using the Mineral Hoe.
     */
    private final BooleanConfigValue showParticles = new BooleanConfigValue(
            "show-particles",
            "When enabled, the Mineral Hoe will spawn flame particles whenever\n" +
                      "tilling a block.",
            true,
            this
    );

    /**
     * Config definition that allows enabling/disabling using a Mineral
     * Hoe to turn dirt/grass into Mineral Soil.
     */
    private final BooleanConfigValue playChargeSound = new BooleanConfigValue(
            "play-charge-sound",
            "When enabled, the Mineral Hoe will play a sound whenever a charge (Mineral\n" +
                      "Crystal) is added to it.",
            true,
            this
    );

    /**
     * Config definition that allows enabling/disabling the sound that
     * plays when the Mineral Hoe is out of charges.
     */
    private final BooleanConfigValue playFailSound = new BooleanConfigValue(
            "play-failure-sound",
            "When enabled, the Mineral Hoe will play a sound when trying to\n" +
                      "till a block without enough charges.",
            true,
            this
    );

    /**
     * Config definition that allows setting the initial charges
     * on every new crafted Mineral Hoe.
     */
    private final IntegerConfigValue initialCharges = new IntegerConfigValue(
            "number-of-initial-charges",
            "The amount of charges a Mineral Hoe will have when first crafted.",
            2,
            0, 2,
            this
    );

    /**
     * Config definition that allows setting the maximum number of
     * charges a Mineral Hoe can hold.
     */
    private final IntegerConfigValue maxCharges = new IntegerConfigValue(
            "maximum-number-of-charges",
            "The maximum amount of charges a single Mineral Hoe can store.",
            64,
            1, 64*10,
            this
    );

    /**
     * Creates a new, unique configuration category
     * grouping.
     */
    public MineralHoeConfig() {
        super("mineral-hoe");
    }

    /**
     * @return {@code true} if the config allows using
     * the Mineral Hoe to till dirt/grass.
     */
    public boolean isEnabled(){
        return this.enabled.getValue();
    }

    /**
     * @return {@code true} if the config allows
     * spawning particles when tilling a block.
     */
    public boolean showParticles(){
        return this.showParticles.getValue();
    }

    /**
     * @return {@code true} if the config allows
     * playing a sound when the Mineral Hoe is out
     * of charges.
     */
    public boolean playFailSound(){
        return this.playFailSound.getValue();
    }

    /**
     * @return {@code true} if the config allows
     * playing a sound when charging the Mineral
     * Hoe.
     */
    public boolean playChargeSound(){
        return this.playChargeSound.getValue();
    }

    /**
     * @return the initial charges given to a
     * Mineral Hoe specified by the config.
     */
    public int getInitialCharges(){
        return this.initialCharges.getValue();
    }

    /**
     * @return the maximum number of charges
     * a Mineral Hoe can hold specified by
     * the config.
     */
    public int getMaxCharges(){
        return this.maxCharges.getValue();
    }
}
