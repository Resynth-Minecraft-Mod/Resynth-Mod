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
package com.ki11erwolf.resynth;

import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.plant.set.*;
import net.minecraft.block.Blocks;

import static net.minecraft.entity.EntityType.*;

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
//A thought: a json system that parses a json file containing the information in this class.
//TODO: Add Netherite plant
//TODO: I should really write a script to automate asset creation for new plants
@SuppressWarnings({"WeakerAccess", "unused", "RedundantSuppression"})//Reflection, Trickery & maybe even a bug.
public class ResynthPlants {

    // ****************
    // Crystalline Sets
    // ****************

    /**
     * The plant set for Resynth Mineral Rocks
     */
    public static final PlantSet<?> MINERAL_ROCKS = PlantSetFactory.newVanillaCrystallineSet(
            "mineral_rock",
            new CrystallineSetProperties(
                    false,
                    25,
                    3,
                    5,
                    30
            ),
            ResynthBlocks.BLOCK_MINERAL_STONE
    ).register();

    /**
     * The plant set for Resynth Calvinite Crystals
     */
    public static final PlantSet<?> CALVINITE_CRYSTAL = PlantSetFactory.newVanillaCrystallineSet(
            "calvinite_crystal",
            new CrystallineSetProperties(
                    false,
                    10F,
                    1,
                    2F,
                    10
            ),
            ResynthBlocks.BLOCK_CALVINITE_NETHERRACK
    ).register();

    /**
     * The plant set for Minecraft Netherite Scrap.
     */
    public static final PlantSet<?> NETHERITE_SCRAP = PlantSetFactory.newVanillaCrystallineSet(
            "netherite_scrap", ResynthModdedPlants.NETHERITE_SCRAP_PROPERTIES, Blocks.field_235398_nh_
    ).register();

    /**
     * The plant set for Vanilla Diamonds.
     */
    public static final PlantSet<?> DIAMOND = PlantSetFactory.newVanillaCrystallineSet(
            "diamond", ResynthModdedPlants.DIAMOND_PROPERTIES, Blocks.DIAMOND_ORE
    ).register();

    /**
     * The plant set for Vanilla Redstone.
     */
    public static final PlantSet<?> REDSTONE = PlantSetFactory.newVanillaCrystallineSet(
            "redstone", ResynthModdedPlants.REDSTONE_PROPERTIES, Blocks.REDSTONE_ORE
    ).register();

    /**
     * The plant set for Vanilla Lapis Lazuli.
     */
    public static final PlantSet<?> LAPIS_LAZULI = PlantSetFactory.newVanillaCrystallineSet(
            "lapis_lazuli", ResynthModdedPlants.LAPIS_LAZULI_PROPERTIES, Blocks.LAPIS_ORE
    ).register();

    /**
     * The plant set for Vanilla Coal.
     */
    public static final PlantSet<?> COAL = PlantSetFactory.newVanillaCrystallineSet(
            "coal", ResynthModdedPlants.COAL_PROPERTIES, Blocks.COAL_ORE
    ).register();

    /**
     * The plant set for Vanilla Emeralds.
     */
    public static final PlantSet<?> EMERALD = PlantSetFactory.newVanillaCrystallineSet(
            "emerald", ResynthModdedPlants.EMERALD_PROPERTIES, Blocks.EMERALD_ORE
    ).register();

    /**
     * The plant set for Vanilla Glowstone.
     */
    public static final PlantSet<?> GLOWSTONE = PlantSetFactory.newVanillaCrystallineSet(
            "glowstone",
            new CrystallineSetProperties(
                    false,
                    60,
                    3,
                    3.0F,
                    30.0F
            ),
            Blocks.GLOWSTONE
    ).register();

    /**
     * The plant set for Vanilla Nether Quartz.
     */
    public static final PlantSet<?> QUARTZ = PlantSetFactory.newVanillaCrystallineSet(
            "quartz",
            new CrystallineSetProperties(
                    false,
                    50,
                    2,
                    3,
                    25F
            ),
            Blocks.NETHER_QUARTZ_ORE
    ).register();

    /**
     * The plant set for Vanilla Cookies (food).
     * Obtained from the cocoa crop.
     */
    public static final PlantSet<?> COOKIE = PlantSetFactory.newVanillaCrystallineSet(
            "cookie",
            new CrystallineSetProperties(
                    false,
                    65,
                    3,
                    2,
                    4
            ),
            Blocks.COCOA
    ).register();

    // *************
    // Metallic Sets
    // *************

    /**
     * The plant set for Vanilla Iron.
     */
    public static final PlantSet<?> IRON = PlantSetFactory.newVanillaMetallicPlantSet(
            "iron",
            ResynthModdedPlants.IRON_PROPERTIES,
            Blocks.IRON_ORE
    ).register();

    /**
     * The plant set for Vanilla Gold.
     */
    public static final PlantSet<?> GOLD = PlantSetFactory.newVanillaMetallicPlantSet(
            "gold",
            ResynthModdedPlants.GOLD_PROPERTIES,
            Blocks.GOLD_ORE
    ).register();

    /**
     * The plant set for Vanilla Clay.
     */
    public static final PlantSet<?> CLAY = PlantSetFactory.newVanillaMetallicPlantSet(
            "clay",
            new MetallicSetProperties(
                    false,
                    55,
                    10,
                    10
            ),
            Blocks.CLAY
    ).register();

    /**
     * The plant set for Vanilla End Stone
     */
    public static final PlantSet<?> END_STONE = PlantSetFactory.newVanillaMetallicPlantSet(
            "end_stone",
            new MetallicSetProperties(
                    false,
                    45,
                    4,
                    4
            ),
            Blocks.END_STONE
    ).register();

    /**
     * The plant set for Vanilla Sand.
     */
    public static final PlantSet<?> SAND = PlantSetFactory.newVanillaMetallicPlantSet(
            "sand",
            new MetallicSetProperties(
                    false,
                    60,
                    3,
                    3
            ),
            Blocks.SAND
    ).register();

    /**
     * The plant set for Vanilla Stone blocks.
     */
    public static final PlantSet<?> STONE = PlantSetFactory.newVanillaMetallicPlantSet(
            "stone",
            new MetallicSetProperties(
                    false,
                    60,
                    4,
                    4
            ),
            Blocks.STONE
    ).register();

    /**
     * The plant set for Vanilla Granite blocks.
     */
    public static final PlantSet<?> GRANITE = PlantSetFactory.newVanillaMetallicPlantSet(
            "granite",
            new MetallicSetProperties(
                    false,
                    50,
                    3,
                    3
            ),
            Blocks.GRANITE
    ).register();

    /**
     * The plant set for Vanilla Diorite blocks.
     */
    public static final PlantSet<?> DIORITE = PlantSetFactory.newVanillaMetallicPlantSet(
            "diorite",
            new MetallicSetProperties(
                    false,
                    50,
                    3,
                    3
            ),
            Blocks.DIORITE
    ).register();

    /**
     * The plant set for Vanilla Andesite blocks.
     */
    public static final PlantSet<?> ANDESITE = PlantSetFactory.newVanillaMetallicPlantSet(
            "andesite",
            new MetallicSetProperties(
                    false,
                    50,
                    3,
                    3
            ),
            Blocks.ANDESITE
    ).register();

    /**
     * The plant set for Vanilla Dirt blocks.
     */
    public static final PlantSet<?> DIRT = PlantSetFactory.newVanillaMetallicPlantSet(
            "dirt",
            new MetallicSetProperties(
                    false,
                    70,
                    2,
                    2
            ),
            Blocks.DIRT
    ).register();

    /**
     * The plant set for Vanilla Cobblestone blocks.
     */
    public static final PlantSet<?> COBBLESTONE = PlantSetFactory.newVanillaMetallicPlantSet(
            "cobblestone",
            new MetallicSetProperties(
                    false,
                    75,
                    2,
                    2
            ),
            Blocks.COBBLESTONE
    ).register();

    /**
     * The plant set for Vanilla Sponge blocks.
     */
    public static final PlantSet<?> SPONGE = PlantSetFactory.newVanillaMetallicPlantSet(
            "sponge",
            new MetallicSetProperties(
                    false,
                    10,
                    5,
                    5
            ),
            Blocks.SPONGE
    ).register();
	
	/**
     * The plant set for Vanilla Gravel.
     */
    public static final PlantSet<?> GRAVEL = PlantSetFactory.newVanillaMetallicPlantSet(
            "gravel",
            new MetallicSetProperties(
                    false,
                    40,
                    3,
                    3
            ),
            Blocks.GRAVEL
    ).register();

    /**
     * The plant set for Vanilla Netherrack.
     */
    public static final PlantSet<?> NETHERRACK = PlantSetFactory.newVanillaMetallicPlantSet(
            "netherrack",
            new MetallicSetProperties(
                    false,
                    35,
                    4,
                    3
            ),
            Blocks.NETHERRACK
    ).register();

    // ****************
    // Biochemical Sets
    // ****************

    /**
     * The plant set for Vanilla Ender Pearls.
     */
    public static final PlantSet<?> ENDER_PEARL = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "ender_pearl",
            new BiochemicalSetProperties(
                    false,
                    13,
                    1,
                    8,
                    6
            ),
            ENDERMAN
    ).register();

    /**
     * The plant set for Vanilla Gunpowder.
     */
    public static final PlantSet<?> GUNPOWDER = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "gunpowder",
            new BiochemicalSetProperties(
                    false,
                    30,
                    1,
                    7,
                    5
            ),
            CREEPER
    ).register();

    /**
     * The plant set for Vanilla Blaze Rods.
     */
    public static final PlantSet<?> BLAZE_ROD = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "blaze_rod",
            new BiochemicalSetProperties(
                    false,
                    16,
                    1,
                    8,
                    6
            ),
            BLAZE
    ).register();

    /**
     * The plant set for Vanilla Bones.
     */
    public static final PlantSet<?> BONE = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "bone",
            new BiochemicalSetProperties(
                    false,
                    30,
                    2,
                    5,
                    3
            ),
            SKELETON
    ).register();

    /**
     * The plant set for Vanilla String.
     */
    public static final PlantSet<?> STRING = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "string",
            new BiochemicalSetProperties(
                    false,
                    25,
                    2,
                    5,
                    3
            ),
            SPIDER, CAVE_SPIDER
    ).register();

    /**
     * The plant set for Vanilla Feathers.
     */
    public static final PlantSet<?> FEATHER = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "feather",
            new BiochemicalSetProperties(
                    false,
                    35,
                    2,
                    4,
                    3
            ),
            CHICKEN
    ).register();

    /**
     * The plant set for Vanilla Ghast Tears.
     */
    public static final PlantSet<?> GHAST_TEAR = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "ghast_tear",
            new BiochemicalSetProperties(
                    false,
                    5,
                    1,
                    10,
                    3
            ),
            GHAST
    ).register();

    /**
     * The plant set for Vanilla Nether Stars.
     */
    public static final PlantSet<?> NETHER_STAR = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "nether_star",
            new BiochemicalSetProperties(
                    false,
                    2,
                    1,
                    12,
                    4
            ),
            WITHER
    ).register();

    /**
     * The plant set for Vanilla Spider Eyes.
     */
    public static final PlantSet<?> SPIDER_EYE = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "spider_eye",
            new BiochemicalSetProperties(
                    false,
                    20,
                    1,
                    5,
                    3
            ),
            SPIDER, CAVE_SPIDER
    ).register();

    /**
     * The plant set for Vanilla Slime Balls.
     */
    public static final PlantSet<?> SLIME_BALL = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "slime_ball",
            new BiochemicalSetProperties(
                    false,
                    20,
                    2,
                    5,
                    3
            ),
            SLIME
    ).register();

    /**
     * The plant set for Vanilla Shulker Shells.
     */
    public static final PlantSet<?> SHULKER_SHELL = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "shulker_shell",
            new BiochemicalSetProperties(
                    false,
                    12,
                    1,
                    10,
                    5
            ),
            SHULKER
    ).register();

    /**
     * The plant set for Vanilla Ink Sacs.
     */
    public static final PlantSet<?> INK_SAC = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "ink_sac",
            new BiochemicalSetProperties(
                    false,
                    20,
                    2,
                    5,
                    2
            ),
            SQUID
    ).register();

    /**
     * The plant set for Vanilla Leather.
     */
    public static final PlantSet<?> LEATHER = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "leather",
            new BiochemicalSetProperties(
                    false,
                    13,
                    1,
                    4,
                    4
            ),
            COW, HORSE
    ).register();

    /**
     * The plant set for Vanilla Rotten Flesh
     */
    public static final PlantSet<?> ROTTEN_FLESH = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "rotten_flesh",
            new BiochemicalSetProperties(
                    false,
                    50,
                    2,
                    14,
                    12
            ),
            ZOMBIE
    ).register();

    /**
     * The plant set for Vanilla Prismarine Crystals.
     */
    public static final PlantSet<?> PRISMARINE_CRYSTAL = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "prismarine_crystal",
            new BiochemicalSetProperties(
                    false,
                    10,
                    1,
                    7,
                    5
            ),
            GUARDIAN
    ).register();

    /**
     * The plant set for Vanilla Prismarine Shards.
     */
    public static final PlantSet<?> PRISMARINE_SHARD = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "prismarine_shard",
            new BiochemicalSetProperties(
                    false,
                    10,
                    1,
                    7,
                    5
            ),
            GUARDIAN
    ).register();

    /**
     * The plant set for Vanilla Rabbits Foot.
     */
    public static final PlantSet<?> RABBIT_FOOT = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "rabbit_foot",
            new BiochemicalSetProperties(
                    false,
                    25,
                    1,
                    10,
                    5
            ),
            RABBIT
    ).register();

    /**
     * The plant set for Vanilla Dragons Breath.
     */
    public static final PlantSet<?> DRAGONS_BREATH = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "dragons_breath",
            new BiochemicalSetProperties(
                    false,
                    15,
                    1,
                    100,
                    10
            ),
            ENDER_DRAGON
    ).register();

    /**
     * The plant set for Vanilla Experience Bottles.
     */
    public static final PlantSet<?> EXPERIENCE_BOTTLE = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "experience_bottle",
            new BiochemicalSetProperties(
                    false,
                    4,
                    2,
                    0.5F,
                    0.5F
            ),
            BAT, BLAZE, CAVE_SPIDER, CHICKEN, COD, COW, CREEPER, DONKEY, DOLPHIN, DROWNED, ELDER_GUARDIAN,
            ENDER_DRAGON, ENDERMAN, ENDERMITE, EVOKER, GHAST, GIANT, GUARDIAN, HORSE, HUSK, LLAMA, MAGMA_CUBE,
            MULE, MOOSHROOM, OCELOT, PARROT, PIG, PUFFERFISH, field_233592_ba_ /*zombie piglin*/, POLAR_BEAR, RABBIT,
            SALMON, SHEEP, SHULKER, SILVERFISH, SKELETON, SKELETON_HORSE, SLIME, SPIDER, SQUID, TURTLE, TROPICAL_FISH,
            VILLAGER, IRON_GOLEM, WITCH, WITHER, WITHER_SKELETON, WOLF, ZOMBIE, ZOMBIE_VILLAGER, ZOMBIE_HORSE, PHANTOM
    ).register();

    /**
     * The plant set for Vanilla Nautilus Shells.
     */
    public static final PlantSet<?> NAUTILUS_SHELL = PlantSetFactory.newVanillaBiochemicalPlantSet(
            "nautilus_shell",
            new BiochemicalSetProperties(
                    false,
                    6,
                    1,
                    2,
                    1
            ),
            DROWNED
    ).register();

    /**Private constructor*/
    private ResynthPlants(){}

    /**
     * Ensures all Vanilla Resynth plant sets are
     * initialized and queued for registration.
     */
    public static void initSets(){/*NO-OP*/}

}
