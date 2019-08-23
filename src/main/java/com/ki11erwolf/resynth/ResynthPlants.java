package com.ki11erwolf.resynth;

import com.ki11erwolf.resynth.plantsets.set.CrystallineSetProperties;
import com.ki11erwolf.resynth.plantsets.set.PlantSet;
import com.ki11erwolf.resynth.plantsets.set.PlantSetFactory;
import net.minecraft.init.Blocks;

/**
 * Holds the definitions and references to every Resynth
 * plant (or plant set to be specific) for Vanilla (no mods)
 * Minecraft and Resynth.
 *
 * This class is responsible for defining the various plants,
 * their properties, as well everything else that makes them unique
 * (e.g. growth rates and drops).
 *
 * All plant sets in this class are referred to as Vanilla plant sets.
 */
@SuppressWarnings("WeakerAccess")
public class ResynthPlants {

    /**
     * The plant set for Vanilla Diamonds.
     */
    public static final PlantSet DIAMOND = PlantSetFactory.newVanillaCrystallineSet(
            "diamond",
            new CrystallineSetProperties(
                    false,
                    6.0F,
                    1,
                    5.0F,
                    2.5F
            ),
            Blocks.DIAMOND_ORE
    ).register();

    /**Private constructor*/
    private ResynthPlants(){}

    /**
     * Ensures all Vanilla Resynth plant sets are
     * initialized and queued for registration.
     */
    public static void initSets(){/*NO-OP*/}

}
