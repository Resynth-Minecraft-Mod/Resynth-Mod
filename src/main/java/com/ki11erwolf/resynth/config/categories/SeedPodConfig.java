package com.ki11erwolf.resynth.config.categories;

import com.ki11erwolf.resynth.config.BooleanConfigValue;
import com.ki11erwolf.resynth.config.ConfigCategory;
import com.ki11erwolf.resynth.config.IntegerConfigValue;

/**
 * Config settings for the {@link com.ki11erwolf.resynth.block.BlockSeedPod}.
 */
public class SeedPodConfig extends ConfigCategory {

    // ************************
    // Config Value Definitions
    // ************************

    /**
     * Config value definition that allows enabling/disabling
     * drops from the Mystical Seed Pod.
     */
    private final BooleanConfigValue enabled = new BooleanConfigValue(
            "enable-drops-from-seed-pod",
            "Set to true to make the Mystical Seed Pod drop a random Biochemical (mob drop)" +
                      "\nseed when broken. This can be useful for players playing on peaceful mode who can" +
                      "\notherwise not get mob drops",
            false,
            this
    );

    /**
     * Config value definition that allows enabling/disabling
     * the "always drop seeds" functionality of the Seed Pod.
     */
    private final BooleanConfigValue alwaysDropSeeds = new BooleanConfigValue(
            "always-drop-seeds",
            "Set to true to make sure the Mystical Seed Pod always drops seeds." +
                      "\nThis does NOT override [enable-drops-from-seed-pod], it only makes" +
                      "\nsure the plant will always drop seeds instead of occasionally dropping nothing.",
            false,
            this
    );

    /**
     * Config value definition that allows setting the
     * number of tries before giving up.
     */
    private final IntegerConfigValue tries = new IntegerConfigValue(
            "number-of-tries",
            "The amount of times the Mystical Seed Pod will try find a Biochemical" +
                      "\nseed before giving up and dropping nothing. Disable this with [always-drop-seeds].",
            10,
            5, 150,
            this
    );

    /**
     * Config value definition that allows enabling/disabling Mystical Seed Pod
     * world generation.
     */
    private final BooleanConfigValue generate = new BooleanConfigValue(
            "generate-in-world",
            "Allows enabling or disabling the generating of Mystical Seed Pods in the world." +
                      "\nSet this to false to prevent Mystical Seed Pods from generating in the world.",
            true,
            this
    );

    /**
     * Config value definition that allows setting the spawn frequency
     * of Mystical Seed Pods.
     */
    private final IntegerConfigValue generationFrequency = new IntegerConfigValue(
            "frequency-of-generation",
            "The frequency (or the number of times) that Mystical Seed Pods will generate in the world." +
                      "\nSetting this to a higher value will generate more Mystical Seed Pods",
            1,
            1, 50,
            this
    );

    /**
     * Creates a new, unique configuration category
     * grouping.
     */
    public SeedPodConfig() {
        super("mystical-seed-pod");
    }

    // ****************
    // Public Accessors
    // ****************

    /**
     * @return {@code true} if the config has enabled
     * drops from the SeedPod.
     */
    public boolean areDropsEnabled(){
        return enabled.getValue();
    }

    /**
     * @return {@code true} if the config has
     * enabled the "always drop seeds" functionality.
     */
    public boolean alwaysDropSeeds(){
        return this.alwaysDropSeeds.getValue();
    }

    /**
     * @return the amount of times to try and
     * find valid seeds before giving up.
     */
    public int getTries(){
        return this.tries.getValue();
    }

    /**
     * @return {@code true} if the config allows
     * generating Mystical Seed Pods in the world.
     */
    public boolean generate(){
        return this.generate.getValue();
    }

    /**
     * @return the frequency of Mystical Seed
     * Pod generation as specified by the config.
     */
    public int getGenerationFrequency(){
        return this.generationFrequency.getValue();
    }
}
