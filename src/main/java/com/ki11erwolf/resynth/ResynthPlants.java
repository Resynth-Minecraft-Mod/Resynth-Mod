/*
 * Copyright 2018-2021 Ki11er_wolf
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
import com.ki11erwolf.resynth.item.ResynthItems;
import com.ki11erwolf.resynth.plant.set.*;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

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
//TODO: I should really write a script to automate asset creation for new plants
public class ResynthPlants {

    // **************
    // Set Properties
    // **************

    static final PlantSetProduceProperties DEFAULT_PRODUCE_PROPERTIES = new PlantSetProduceProperties(
            1, 200, 2.0
    );

    // Crystalline

    /**
     * The set properties for general Diamond and Diamond Ore.
     */
    static final CrystallineSetProperties DIAMOND_PROPERTIES = new CrystallineSetProperties(
            false, 8.0F,1,
            1.30F, 25.0F
    );

    /**
     * The set properties for general Redstone and Redstone Ore.
     */
    static final CrystallineSetProperties REDSTONE_PROPERTIES = new CrystallineSetProperties(
            false, 65.0F, 3,
            1.0F, 75.0F
    );

    /**
     * The set properties for general Lapis Lazuli and Lapis Lazuli Ore.
     */
    static final CrystallineSetProperties LAPIS_LAZULI_PROPERTIES = new CrystallineSetProperties(
            false, 45, 3,
            2, 50F
    );

    /**
     * The set properties for general Coal and Coal Ore.
     */
    static final CrystallineSetProperties COAL_PROPERTIES = new CrystallineSetProperties(
            false, 75, 2,
            0.5F, 50F
    );

    /**
     * The set properties for general Emerald and Emerald Ore.
     */
    static final CrystallineSetProperties EMERALD_PROPERTIES = new CrystallineSetProperties(
            false, 7.0F, 1,
            1.5F, 25F
    );

    // Metallic

    /**
     * The set properties for general Netherite scrap and Ancient Debris Ore.
     */
    static final MetallicSetProperties ANCIENT_DEBRIS_PROPERTIES = new MetallicSetProperties(
            false, 10.0F, 4.0F, 8.0F
    );


    /**
     * The set properties for general Iron and Iron Ore.
     */
    static final MetallicSetProperties IRON_PROPERTIES = new MetallicSetProperties(
            false, 40, 6, 6
    );

    /**
     * The set properties for general Gold and Gold Ore.
     */
    static final MetallicSetProperties GOLD_PROPERTIES = new MetallicSetProperties(
            false, 25, 5, 5
    );

    // ****************
    // Crystalline Sets
    // ****************

    /**
     * The plant set for Resynth Mineral Rocks
     */
    public static final PlantSet<?, ?> MINERAL_ROCKS = PlantSetFactory.makeCrystallineSet(
            resynthResource("mineral_rock"),
            new CrystallineSetProperties(
                    false,
                    25,
                    3,
                    5,
                    30
            ), new PlantSetProduceProperties(
                    2, 200, 3.0
            ),
            ResynthItems.ITEM_MINERAL_ROCK, ResynthBlocks.BLOCK_MINERAL_STONE
    ).register();

    /**
     * The plant set for Resynth Calvinite Crystals
     */
    public static final PlantSet<?, ?> CALVINITE_CRYSTAL = PlantSetFactory.makeCrystallineSet(
            resynthResource("calvinite_crystal"),
            new CrystallineSetProperties(
                    false,
                    10F,
                    1,
                    2F,
                    10
            ), DEFAULT_PRODUCE_PROPERTIES, ResynthItems.ITEM_CALVINITE, ResynthBlocks.BLOCK_CALVINITE_NETHERRACK
    ).register();

    /**
     * The plant set for Vanilla Diamonds.
     */
    public static final PlantSet<?, ?> DIAMOND = PlantSetFactory.makeCrystallineSet(
            minecraftResource("diamond"), DIAMOND_PROPERTIES,
            DEFAULT_PRODUCE_PROPERTIES, Items.DIAMOND, Blocks.DIAMOND_ORE
    ).register();

    /**
     * The plant set for Vanilla Redstone.
     */
    public static final PlantSet<?, ?> REDSTONE = PlantSetFactory.makeCrystallineSet(
            minecraftResource("redstone"), REDSTONE_PROPERTIES,
            new PlantSetProduceProperties(
                    3, 200, 2
            ), Items.REDSTONE, Blocks.REDSTONE_ORE
    ).register();

    /**
     * The plant set for Vanilla Lapis Lazuli.
     */
    public static final PlantSet<?, ?> LAPIS_LAZULI = PlantSetFactory.makeCrystallineSet(
            minecraftResource("lapis_lazuli"), LAPIS_LAZULI_PROPERTIES,
            new PlantSetProduceProperties(
                    2, 200, 2
            ), Items.LAPIS_LAZULI, Blocks.LAPIS_ORE
    ).register();

    /**
     * The plant set for Vanilla Coal.
     */
    public static final PlantSet<?, ?> COAL = PlantSetFactory.makeCrystallineSet(
            minecraftResource("coal"), COAL_PROPERTIES,
            new PlantSetProduceProperties(
                    2, 200, 2
            ), Items.COAL, Blocks.COAL_ORE
    ).register();

    /**
     * The plant set for Vanilla Emeralds.
     */
    public static final PlantSet<?, ?> EMERALD = PlantSetFactory.makeCrystallineSet(
            minecraftResource("emerald"), EMERALD_PROPERTIES,
            DEFAULT_PRODUCE_PROPERTIES, Items.EMERALD, Blocks.EMERALD_ORE
    ).register();

    /**
     * The plant set for Vanilla Glowstone.
     */
    public static final PlantSet<?, ?> GLOWSTONE = PlantSetFactory.makeCrystallineSet(
            minecraftResource("glowstone"),
            new CrystallineSetProperties(
                    false,
                    60,
                    2,
                    3.0F,
                    30.0F
            ), new PlantSetProduceProperties(
                    2, 200, 2
            ), Items.GLOWSTONE_DUST, Blocks.GLOWSTONE
    ).register();

    /**
     * The plant set for Vanilla Nether Quartz.
     */
    public static final PlantSet<?, ?> QUARTZ = PlantSetFactory.makeCrystallineSet(
            minecraftResource("quartz"),
            new CrystallineSetProperties(
                    false,
                    50,
                    2,
                    3,
                    25F
            ), DEFAULT_PRODUCE_PROPERTIES, Items.QUARTZ, Blocks.NETHER_QUARTZ_ORE
    ).register();

    /**
     * The plant set for Vanilla Cookies (food).
     * Obtained from the cocoa crop.
     */
    public static final PlantSet<?, ?> COOKIE = PlantSetFactory.makeCrystallineSet(
            minecraftResource("cookie"),
            new CrystallineSetProperties(
                    false,
                    65,
                    2,
                    2,
                    4
            ), new PlantSetProduceProperties(
                    2, 200, 2
            ), Items.COOKIE, Blocks.COCOA
    ).register();

    // *************
    // Metallic Sets
    // *************

    /**
     * The plant set for Minecraft Ancient Debris.
     */
    public static final PlantSet<?, ?> ANCIENT_DEBRIS = PlantSetFactory.makeMetallicSet(
            minecraftResource("ancient_debris"), ANCIENT_DEBRIS_PROPERTIES,
            new PlantSetProduceProperties(
                    2, 200, 2
            ), Blocks.ANCIENT_DEBRIS, Blocks.ANCIENT_DEBRIS
    ).register();

    /**
     * The plant set for Vanilla Iron.
     */
    public static final PlantSet<?, ?> IRON = PlantSetFactory.makeMetallicSet(
            minecraftResource("iron"), IRON_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES, Blocks.IRON_ORE, Blocks.IRON_ORE
    ).register();

    /**
     * The plant set for Vanilla Gold.
     */
    public static final PlantSet<?, ?> GOLD = PlantSetFactory.makeMetallicSet(
            minecraftResource("gold"),  GOLD_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES, Blocks.GOLD_ORE, Blocks.GOLD_ORE
    ).register();

    /**
     * The plant set for Vanilla Clay.
     */
    public static final PlantSet<?, ?> CLAY = PlantSetFactory.makeMetallicSet(
            minecraftResource("clay"),
            new MetallicSetProperties(
                    false,
                    55,
                    10,
                    10
            ), new PlantSetProduceProperties(
                    2, 200, 2
            ), Blocks.CLAY, Blocks.CLAY
    ).register();

    /**
     * The plant set for Vanilla End Stone
     */
    public static final PlantSet<?, ?> END_STONE = PlantSetFactory.makeMetallicSet(
            minecraftResource("end_stone"),
            new MetallicSetProperties(
                    false,
                    45,
                    4,
                    4
            ), DEFAULT_PRODUCE_PROPERTIES, Blocks.END_STONE, Blocks.END_STONE
    ).register();

    /**
     * The plant set for Vanilla Sand.
     */
    public static final PlantSet<?, ?> SAND = PlantSetFactory.makeMetallicSet(
            minecraftResource("sand"),
            new MetallicSetProperties(
                    false,
                    60,
                    3,
                    3
            ), new PlantSetProduceProperties(
                    2, 200, 2
            ), Blocks.SAND, Blocks.SAND
    ).register();

    /**
     * The plant set for Vanilla Stone blocks.
     */
    public static final PlantSet<?, ?> STONE = PlantSetFactory.makeMetallicSet(
            minecraftResource("stone"),
            new MetallicSetProperties(
                    false,
                    60,
                    4,
                    4
            ), DEFAULT_PRODUCE_PROPERTIES, Blocks.STONE, Blocks.STONE
    ).register();

    /**
     * The plant set for Vanilla Granite blocks.
     */
    public static final PlantSet<?, ?> GRANITE = PlantSetFactory.makeMetallicSet(
            minecraftResource("granite"),
            new MetallicSetProperties(
                    false,
                    50,
                    3,
                    3
            ), new PlantSetProduceProperties(
                    2, 200, 2
            ), Blocks.GRANITE, Blocks.GRANITE
    ).register();

    /**
     * The plant set for Vanilla Diorite blocks.
     */
    public static final PlantSet<?, ?> DIORITE = PlantSetFactory.makeMetallicSet(
            minecraftResource("diorite"),
            new MetallicSetProperties(
                    false,
                    50,
                    3,
                    3
            ), new PlantSetProduceProperties(
                    2, 200, 2
            ), Blocks.DIORITE, Blocks.DIORITE
    ).register();

    /**
     * The plant set for Vanilla Andesite blocks.
     */
    public static final PlantSet<?, ?> ANDESITE = PlantSetFactory.makeMetallicSet(
            minecraftResource("andesite"),
            new MetallicSetProperties(
                    false,
                    50,
                    3,
                    3
            ), new PlantSetProduceProperties(
                    2, 200, 2
            ), Blocks.ANDESITE, Blocks.ANDESITE
    ).register();

    /**
     * The plant set for Vanilla Dirt blocks.
     */
    public static final PlantSet<?, ?> DIRT = PlantSetFactory.makeMetallicSet(
            minecraftResource("dirt"),
            new MetallicSetProperties(
                    false,
                    70,
                    2,
                    2
            ), new PlantSetProduceProperties(
                    4, 200, 2
            ), Blocks.DIRT, Blocks.DIRT
    ).register();

    /**
     * The plant set for Vanilla Cobblestone blocks.
     */
    public static final PlantSet<?, ?> COBBLESTONE = PlantSetFactory.makeMetallicSet(
            minecraftResource("cobblestone"),
            new MetallicSetProperties(
                    false,
                    75,
                    2,
                    2
            ), new PlantSetProduceProperties(
                    3, 200, 2
            ), Blocks.COBBLESTONE, Blocks.COBBLESTONE
    ).register();

    /**
     * The plant set for Vanilla Sponge blocks.
     */
    public static final PlantSet<?, ?> SPONGE = PlantSetFactory.makeMetallicSet(
            minecraftResource("sponge"),
            new MetallicSetProperties(
                    false,
                    10,
                    5,
                    5
            ), DEFAULT_PRODUCE_PROPERTIES, Blocks.SPONGE, Blocks.SPONGE
    ).register();
	
	/**
     * The plant set for Vanilla Gravel.
     */
    public static final PlantSet<?, ?> GRAVEL = PlantSetFactory.makeMetallicSet(
            minecraftResource("gravel"),
            new MetallicSetProperties(
                    false,
                    40,
                    3,
                    3
            ), new PlantSetProduceProperties(
                    2, 200, 2
            ), Blocks.GRAVEL, Blocks.GRAVEL
    ).register();

    /**
     * The plant set for Vanilla Netherrack.
     */
    public static final PlantSet<?, ?> NETHERRACK = PlantSetFactory.makeMetallicSet(
            minecraftResource("netherrack"),
            new MetallicSetProperties(
                    false,
                    35,
                    4,
                    3
            ), new PlantSetProduceProperties(
                    2, 200, 2
            ), Blocks.NETHERRACK, Blocks.NETHERRACK
    ).register();

    // ****************
    // Biochemical Sets
    // ****************

    /**
     * The plant set for Vanilla Ender Pearls.
     */
    public static final PlantSet<?, ?> ENDER_PEARL = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("ender_pearl"),
            new BiochemicalSetProperties(
                    false,
                    13,
                    1,
                    8,
                    6
            ), DEFAULT_PRODUCE_PROPERTIES, Items.ENDER_PEARL, ENDERMAN
    ).register();

    /**
     * The plant set for Vanilla Gunpowder.
     */
    public static final PlantSet<?, ?> GUNPOWDER = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("gunpowder"),
            new BiochemicalSetProperties(
                    false,
                    30,
                    1,
                    7,
                    5
            ), DEFAULT_PRODUCE_PROPERTIES, Items.GUNPOWDER, CREEPER
    ).register();

    /**
     * The plant set for Vanilla Blaze Rods.
     */
    public static final PlantSet<?, ?> BLAZE_ROD = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("blaze_rod"),
            new BiochemicalSetProperties(
                    false,
                    16,
                    1,
                    8,
                    6
            ), DEFAULT_PRODUCE_PROPERTIES, Items.BLAZE_ROD, BLAZE
    ).register();

    /**
     * The plant set for Vanilla Bones.
     */
    public static final PlantSet<?, ?> BONE = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("bone"),
            new BiochemicalSetProperties(
                    false,
                    30,
                    2,
                    5,
                    3
            ), new PlantSetProduceProperties(
                    2, 200, 2
            ), Items.BONE, SKELETON
    ).register();

    /**
     * The plant set for Vanilla String.
     */
    public static final PlantSet<?, ?> STRING = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("string"),
            new BiochemicalSetProperties(
                    false,
                    25,
                    2,
                    5,
                    3
            ), DEFAULT_PRODUCE_PROPERTIES, Items.STRING, SPIDER, CAVE_SPIDER
    ).register();

    /**
     * The plant set for Vanilla Feathers.
     */
    public static final PlantSet<?, ?> FEATHER = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("feather"),
            new BiochemicalSetProperties(
                    false,
                    35,
                    2,
                    4,
                    3
            ), DEFAULT_PRODUCE_PROPERTIES, Items.FEATHER, CHICKEN
    ).register();

    /**
     * The plant set for Vanilla Ghast Tears.
     */
    public static final PlantSet<?, ?> GHAST_TEAR = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("ghast_tear"),
            new BiochemicalSetProperties(
                    false,
                    5,
                    1,
                    10,
                    3
            ), DEFAULT_PRODUCE_PROPERTIES, Items.GHAST_TEAR, GHAST
    ).register();

    /**
     * The plant set for Vanilla Nether Stars.
     */
    public static final PlantSet<?, ?> NETHER_STAR = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("nether_star"),
            new BiochemicalSetProperties(
                    false,
                    2,
                    1,
                    12,
                    4
            ), DEFAULT_PRODUCE_PROPERTIES, Items.NETHER_STAR, WITHER
    ).register();

    /**
     * The plant set for Vanilla Spider Eyes.
     */
    public static final PlantSet<?, ?> SPIDER_EYE = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("spider_eye"),
            new BiochemicalSetProperties(
                    false,
                    20,
                    1,
                    5,
                    3
            ), DEFAULT_PRODUCE_PROPERTIES, Items.SPIDER_EYE, SPIDER, CAVE_SPIDER
    ).register();

    /**
     * The plant set for Vanilla Slime Balls.
     */
    public static final PlantSet<?, ?> SLIME_BALL = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("slime_ball"),
            new BiochemicalSetProperties(
                    false,
                    20,
                    2,
                    5,
                    3
            ), DEFAULT_PRODUCE_PROPERTIES, Items.SLIME_BALL, SLIME
    ).register();

    /**
     * The plant set for Vanilla Shulker Shells.
     */
    public static final PlantSet<?, ?> SHULKER_SHELL = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("shulker_shell"),
            new BiochemicalSetProperties(
                    false,
                    12,
                    1,
                    10,
                    5
            ), DEFAULT_PRODUCE_PROPERTIES, Items.SHULKER_SHELL, SHULKER
    ).register();

    /**
     * The plant set for Vanilla Ink Sacs.
     */
    public static final PlantSet<?, ?> INK_SAC = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("ink_sac"),
            new BiochemicalSetProperties(
                    false,
                    20,
                    2,
                    5,
                    2
            ), new PlantSetProduceProperties(
                    2, 200, 2
            ), Items.INK_SAC, SQUID
    ).register();

    /**
     * The plant set for Vanilla Leather.
     */
    public static final PlantSet<?, ?> LEATHER = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("leather"),
            new BiochemicalSetProperties(
                    false,
                    13,
                    1,
                    4,
                    4
            ), DEFAULT_PRODUCE_PROPERTIES, Items.LEATHER, COW, HORSE
    ).register();

    /**
     * The plant set for Vanilla Rotten Flesh
     */
    public static final PlantSet<?, ?> ROTTEN_FLESH = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("rotten_flesh"),
            new BiochemicalSetProperties(
                    false,
                    50,
                    2,
                    14,
                    12
            ), new PlantSetProduceProperties(
                    4, 200, 2
            ), Items.ROTTEN_FLESH, ZOMBIE
    ).register();

    /**
     * The plant set for Vanilla Prismarine Crystals.
     */
    public static final PlantSet<?, ?> PRISMARINE_CRYSTAL = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("prismarine_crystal"),
            new BiochemicalSetProperties(
                    false,
                    10,
                    1,
                    7,
                    5
            ), DEFAULT_PRODUCE_PROPERTIES, Items.PRISMARINE_CRYSTALS, GUARDIAN
    ).register();

    /**
     * The plant set for Vanilla Prismarine Shards.
     */
    public static final PlantSet<?, ?> PRISMARINE_SHARD = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("prismarine_shard"),
            new BiochemicalSetProperties(
                    false,
                    10,
                    1,
                    7,
                    5
            ), DEFAULT_PRODUCE_PROPERTIES, Items.PRISMARINE_SHARD, GUARDIAN
    ).register();

    /**
     * The plant set for Vanilla Rabbits Foot.
     */
    public static final PlantSet<?, ?> RABBIT_FOOT = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("rabbit_foot"),
            new BiochemicalSetProperties(
                    false,
                    25,
                    1,
                    10,
                    5
            ), DEFAULT_PRODUCE_PROPERTIES, Items.RABBIT_FOOT, RABBIT
    ).register();

    /**
     * The plant set for Vanilla Dragons Breath.
     */
    public static final PlantSet<?, ?> DRAGONS_BREATH = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("dragons_breath"),
            new BiochemicalSetProperties(
                    false,
                    15,
                    1,
                    100,
                    10
            ), DEFAULT_PRODUCE_PROPERTIES, Items.DRAGON_BREATH, ENDER_DRAGON
    ).register();

    /**
     * The plant set for Vanilla Experience Bottles.
     */
    public static final PlantSet<?, ?> EXPERIENCE_BOTTLE = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("experience_bottle"),
            new BiochemicalSetProperties(
                    false,
                    4,
                    2,
                    0.5F,
                    0.5F
            ), new PlantSetProduceProperties(2, 200, 2), Items.EXPERIENCE_BOTTLE,
            BAT, BLAZE, CAVE_SPIDER, CHICKEN, COD, COW, CREEPER, DONKEY, DOLPHIN, DROWNED, ELDER_GUARDIAN,
            ENDER_DRAGON, ENDERMAN, ENDERMITE, EVOKER, GHAST, GIANT, GUARDIAN, HORSE, HUSK, LLAMA, MAGMA_CUBE,
            MULE, MOOSHROOM, OCELOT, PARROT, PIG, PUFFERFISH, ZOMBIFIED_PIGLIN, POLAR_BEAR, RABBIT,
            SALMON, SHEEP, SHULKER, SILVERFISH, SKELETON, SKELETON_HORSE, SLIME, SPIDER, SQUID, TURTLE, TROPICAL_FISH,
            WITCH, WITHER, WITHER_SKELETON, WOLF, ZOMBIE, ZOMBIE_VILLAGER, ZOMBIE_HORSE, PHANTOM
    ).register();

    /**
     * The plant set for Vanilla Nautilus Shells.
     */
    public static final PlantSet<?, ?> NAUTILUS_SHELL = PlantSetFactory.makeBiochemicalSet(
            minecraftResource("nautilus_shell"),
            new BiochemicalSetProperties(
                    false,
                    6,
                    1,
                    2,
                    1
            ), DEFAULT_PRODUCE_PROPERTIES, Items.NAUTILUS_SHELL, DROWNED
    ).register();

    /**Private constructor*/
    private ResynthPlants(){}

    /**
     * Ensures all Vanilla Resynth plant sets are
     * initialized and queued for registration.
     */
    public static void initSets(){/*NO-OP*/}

    // Helpers

    /**
     * A utility method to obtain a {@link ResourceLocation} for use as a
     * PlantSet ID, specifically for PlantSets that grow Resynth resources.
     *
     * @param setName the identifying name of the plant set.
     * @return a new {@link ResourceLocation} that specifies the identifying
     * name of a PlantSet as well as designating Resynth as the mod the plant
     * set exists for.
     */
    private static ResourceLocation resynthResource(String setName) {
        if(Objects.requireNonNull(setName).isEmpty())
            throw new IllegalArgumentException("The sets name cannot be empty");

        return new ResourceLocation(ResynthMod.MODID, setName);
    }

    /**
     * A utility method to obtain a {@link ResourceLocation} for use as a
     * PlantSet ID, specifically for PlantSets that grow Minecraft resources.
     *
     * @param setName the identifying name of the plant set.
     * @return a new {@link ResourceLocation} that specifies the identifying
     * name of a PlantSet as well as designating Minecraft as the mod the plant
     * set exists for.
     */
    private static ResourceLocation minecraftResource(String setName) {
        if(Objects.requireNonNull(setName).isEmpty())
            throw new IllegalArgumentException("The sets name cannot be empty");

        return new ResourceLocation("minecraft", setName);
    }
}
