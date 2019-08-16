package com.ki11erwolf.resynth.features;

/**
 * Object holder class that holds references to every
 * created Resynth {@link net.minecraft.world.gen.feature.Feature}.
 * Also provides a base class for registering the created features.
 */
public class ResynthFeatures {

    /**
     * Reference to the {@link SeedPodFeature}.
     */
    private static final SeedPodFeature SEED_POD_FEATURE = new SeedPodFeature();

    /**
     * Reference to the {@link MineralStoneFeature}.
     */
    private static final MineralStoneFeature MINERAL_ROCK_FEATURE = new MineralStoneFeature();

    /**Private Constructor.*/
    private ResynthFeatures(){}

    /**
     * Static initialization method. Ensures
     * all Resynth Features are registered
     * to the game.
     */
    public static void init(){
        /*NO-OP - just serves to initialize the static references.*/
    }
}
