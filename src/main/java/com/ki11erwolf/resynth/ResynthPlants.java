package com.ki11erwolf.resynth;

import com.ki11erwolf.resynth.block.ResynthBlocks;
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
     * The plant set for Resynth Mineral Rocks
     */
    public static final PlantSet MINERAL_ROCKS = PlantSetFactory.newVanillaCrystallineSet(
            "mineral_rock",
            new CrystallineSetProperties(
                    false,
                    10,
                    3,
                    10,
                    15
            ),
            ResynthBlocks.BLOCK_MINERAL_STONE
    ).register();

    /**
     * The plant set for Vanilla Diamonds.
     */
    public static final PlantSet DIAMOND = PlantSetFactory.newVanillaCrystallineSet(
            "diamond",
            new CrystallineSetProperties(
                    false,
                    6.0F,
                    1,
                    2.0F,
                    3.5F
            ),
            Blocks.DIAMOND_ORE
    ).register();

    /**
     * The plant set for Vanilla Redstone.
     */
    public static final PlantSet REDSTONE = PlantSetFactory.newVanillaCrystallineSet(
            "redstone",
            new CrystallineSetProperties(
                    false,
                    15.0F,
                    2,
                    1.0F,
                    3.5F
            ),
            Blocks.REDSTONE_ORE
    ).register();

    /**
     * The plant set for Vanilla Lapis Lazuli.
     */
    public static final PlantSet LAPIS_LAZULI = PlantSetFactory.newVanillaCrystallineSet(
            "lapis_lazuli",
            new CrystallineSetProperties(
                    false,
                    6,
                    2,
                    2,
                    3.5F
            ),
            Blocks.LAPIS_ORE
    ).register();

    /**Private constructor*/
    private ResynthPlants(){}

    /**
     * Ensures all Vanilla Resynth plant sets are
     * initialized and queued for registration.
     */
    public static void initSets(){/*NO-OP*/}

}
