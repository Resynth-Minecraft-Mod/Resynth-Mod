package com.ki11erwolf.resynth;

import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.plantsets.set.*;
import net.minecraft.entity.EntityType;
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
@SuppressWarnings({"WeakerAccess", "unused"})
public class ResynthPlants {

    // ****************
    // Crystalline Sets
    // ****************

    /**
     * The plant set for Resynth Mineral Rocks
     */
    public static final PlantSet MINERAL_ROCKS = PlantSetFactory.newVanillaCrystallineSet(
            "mineral_rock",
            new CrystallineSetProperties(
                    false,
                    10,
                    3,
                    6,
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
                    3.0F,
                    1.0F
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

    /**
     * The plant set for Vanilla Coal.
     */
    public static final PlantSet COAL = PlantSetFactory.newVanillaCrystallineSet(
            "coal",
            new CrystallineSetProperties(
                    false,
                    30,
                    2,
                    1.0F,
                    3.0F
            ),
            Blocks.COAL_ORE
    ).register();

    /**
     * The plant set for Vanilla Emeralds.
     */
    public static final PlantSet EMERALD = PlantSetFactory.newVanillaCrystallineSet(
            "emerald",
            new CrystallineSetProperties(
                    false,
                    2.0F,
                    1,
                    4.0F,
                    0.5F
            ),
            Blocks.EMERALD_ORE
    ).register();

    /**
     * The plant set for Vanilla Glowstone.
     */
    public static final PlantSet GLOWSTONE = PlantSetFactory.newVanillaCrystallineSet(
            "glowstone",
            new CrystallineSetProperties(
                    false,
                    40,
                    1,
                    3.0F,
                    3.0F
            ),
            Blocks.GLOWSTONE
    ).register();

    /**
     * The plant set for Vanilla Nether Quartz.
     */
    public static final PlantSet QUARTZ = PlantSetFactory.newVanillaCrystallineSet(
            "quartz",
            new CrystallineSetProperties(
                    false,
                    25,
                    2,
                    5,
                    2.5F
            ),
            Blocks.NETHER_QUARTZ_ORE
    ).register();

    // *************
    // Metallic Sets
    // *************

    /**
     * The plant set for Vanilla Iron.
     */
    public static final PlantSet IRON = PlantSetFactory.newVanillaMetallicPlantSet(
            "iron",
            new MetallicSetProperties(
                    false,
                    10,
                    6,
                    6
            ),
            Blocks.IRON_ORE
    ).register();

    /**
     * The plant set for Vanilla Gold.
     */
    public static final PlantSet GOLD = PlantSetFactory.newVanillaMetallicPlantSet(
            "gold",
            new MetallicSetProperties(
                    false,
                    8,
                    5,
                    5
            ),
            Blocks.GOLD_ORE
    ).register();

    /**
     * The plant set for Vanilla Clay.
     */
    public static final PlantSet CLAY = PlantSetFactory.newVanillaMetallicPlantSet(
            "clay",
            new MetallicSetProperties(
                    false,
                    20,
                    10,
                    10
            ),
            Blocks.CLAY
    ).register();

    /**
     * The plant set for Vanilla End Stone
     */
    public static final PlantSet END_STONE = PlantSetFactory.newVanillaMetallicPlantSet(
            "end_stone",
            new MetallicSetProperties(
                    false,
                    9,
                    4,
                    4
            ),
            Blocks.END_STONE
    ).register();

    /**
     * The plant set for Vanilla Sand.
     */
    public static final PlantSet SAND = PlantSetFactory.newVanillaMetallicPlantSet(
            "sand",
            new MetallicSetProperties(
                false,
                    50,
                    3,
                    3
            ),
            Blocks.SAND
    ).register();

    // ****************
    // Biochemical Sets
    // ****************

    /**
     * The plant set for Vanilla Ender Pearls.
     */
    public static final PlantSet ENDER_PEARL = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "ender_pearl",
            new BiochemicalSetProperties(
                    false,
                    10,
                    1,
                    6,
                    5
            ),
            EntityType.ENDERMAN
    ).register();

    /**Private constructor*/
    private ResynthPlants(){}

    /**
     * Ensures all Vanilla Resynth plant sets are
     * initialized and queued for registration.
     */
    public static void initSets(){/*NO-OP*/}

}
