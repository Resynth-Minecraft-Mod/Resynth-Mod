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
package com.ki11erwolf.resynth.features;

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.CalviniteGenConfig;
import com.ki11erwolf.resynth.config.categories.MineralStoneGenConfig;
import com.ki11erwolf.resynth.config.categories.SeedPodConfig;
import com.ki11erwolf.resynth.config.categories.SylvaniteGenConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Feature construction and registration class for Resynth.
 * Contains declared and registered features created by Resynth.
 */
@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ResynthMod.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public final class ResynthFeatures {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    /**
     * List of Resynth's {@link Feature} implementations to registered to the game.
     */
    private static final List<Feature<?>> FEATURE_LIST = new ArrayList<>();

    // ****************
    //  Feature Config
    // ****************

    /**
     * World generation user config for Mystical Seed Pods.
     */
    private static final SeedPodConfig MYSTICAL_SEED_POD_CONFIG = ResynthConfig.GENERAL_CONFIG.getCategory(SeedPodConfig.class);

    /**
     * World generation user config for Mineral Stone.
     */
    private static final MineralStoneGenConfig MINERAL_STONE_GEN_CONFIG
            = ResynthConfig.GENERAL_CONFIG.getCategory(MineralStoneGenConfig.class);

    /**
     * World generation user config for Calvinite.
     */
    private static final CalviniteGenConfig CALVINITE_GEN_CONFIG
            = ResynthConfig.GENERAL_CONFIG.getCategory(CalviniteGenConfig.class);

    /**
     * World generation user config for Sylvanite.
     */
    private static final SylvaniteGenConfig SYLVANITE_GEN_CONFIG
            = ResynthConfig.GENERAL_CONFIG.getCategory(SylvaniteGenConfig.class);

    // ********
    //  Biomes
    // ********

    /**
     * An array containing all the {@link Biome Biomes} present in the Overworld.
     */
    private static final Biome.Category[] BIOMES = new Biome.Category[]{
            Biome.Category.TAIGA, Biome.Category.EXTREME_HILLS, Biome.Category.JUNGLE, Biome.Category.MESA,
            Biome.Category.PLAINS, Biome.Category.SAVANNA, Biome.Category.ICY, Biome.Category.BEACH, Biome.Category.FOREST,
            Biome.Category.OCEAN, Biome.Category.DESERT, Biome.Category.RIVER, Biome.Category.SWAMP, Biome.Category.MUSHROOM
    };

    /**
     * An array containing all the {@link Biome Biomes} present in the Nether.
     */
    private static final Biome.Category[] NETHER_BIOMES = new Biome.Category[] {
            Biome.Category.NETHER
    };

    /**
     * An array containing all the {@link Biome Biomes} present in the End.
     */
    private static final Biome.Category[] END_BIOMES = new Biome.Category[] {
            Biome.Category.THEEND
    };

    // **********
    //  Features
    // **********

    /**
     * The {@link FlowerFeature Flower} type {@link Feature} used to generate
     *{@link ResynthBlocks#BLOCK_SEED_POD Mystical Seed Pods} in the world,
     * using user modifiable config settings to determine generation values.
     */
    private static final Feature<FlowerFeature> MYSTICAL_SEED_POD = new FlowerFeature(
            new ResourceLocation(ResynthMod.MODID, "mystical_seed_pod_flower"), BIOMES,
            ResynthBlocks.BLOCK_SEED_POD, MYSTICAL_SEED_POD_CONFIG.getRarity(), MYSTICAL_SEED_POD_CONFIG.getSize()
    ).register();

    /**
     * The {@link OreFeature Ore} type {@link Feature} used to generate
     *{@link ResynthBlocks#BLOCK_MINERAL_STONE Mineral Stone} in the world,
     * using user modifiable config settings to determine generation values.
     */
    private static final Feature<OreFeature> MINERAL_STONE = new OreFeature(
            new ResourceLocation(ResynthMod.MODID,  "mineral_stone_ore"), BIOMES,
            ResynthBlocks.BLOCK_MINERAL_STONE, BlockListMatcher.MATCH_OVERWORLD_ROCK,
            MINERAL_STONE_GEN_CONFIG.getRarity(), MINERAL_STONE_GEN_CONFIG.getSize(),
            MINERAL_STONE_GEN_CONFIG.getMinimumHeight(), MINERAL_STONE_GEN_CONFIG.getMaximumHeight()
    ).register();

    /**
     * The {@link OreFeature Ore} type {@link Feature} used to generate,
     *{@link ResynthBlocks#BLOCK_CALVINITE_NETHERRACK Calvinite} in the nether,
     * using user modifiable config settings to determine generation values.
     */
    private static final Feature<OreFeature> CALVINITE = new OreFeature(
            new ResourceLocation(ResynthMod.MODID,  "calvinite_ore"), NETHER_BIOMES,
            ResynthBlocks.BLOCK_CALVINITE_NETHERRACK, BlockListMatcher.MATCH_NETHERWORLD_ROCK,
            CALVINITE_GEN_CONFIG.getRarity(), CALVINITE_GEN_CONFIG.getSize(),
            CALVINITE_GEN_CONFIG.getMinimumHeight(), CALVINITE_GEN_CONFIG.getMaximumHeight()
    ).register();

    /**
     * The {@link OreFeature Ore} type {@link Feature} used to generate,
     *{@link ResynthBlocks#BLOCK_SYLVANITE_END_STONE Sylvanite} in the end world,
     * using user modifiable config settings to determine generation values.
     */
    private static final Feature<OreFeature> SYLVANITE = new OreFeature(
            new ResourceLocation(ResynthMod.MODID,  "sylvanite_ore"), END_BIOMES,
            ResynthBlocks.BLOCK_SYLVANITE_END_STONE, BlockListMatcher.MATCH_ENDWORLD_ROCK,
            SYLVANITE_GEN_CONFIG.getRarity(), SYLVANITE_GEN_CONFIG.getSize(),
            SYLVANITE_GEN_CONFIG.getMinimumHeight(), SYLVANITE_GEN_CONFIG.getMaximumHeight()
    ).register();

    // **************
    //  Registration
    // **************

    /** Private constructor preventing inheritance and object creation. */
    private ResynthFeatures() { }

    /* Registers Forge EventBus listener for: biome loading events. */
    static {
        MinecraftForge.EVENT_BUS.addListener(ResynthFeatures::onBiomeLoading);
    }

    /**
     * Public initialize method to create and register features.
     */
    public static void init() { }

    /**
     * Adds a {@link Feature} implementation to the list of Features
     * created by Resynth and registered to the game.
     *
     * @return the Feature passed in ({@code T feature}).
     */
    protected static <T extends Feature<?>> T addFeature(T feature) {
        FEATURE_LIST.add(Objects.requireNonNull(feature));
        return feature;
    }

    /**
     * Forge EventBus callback fired when a {@link BiomeLoadingEvent
     * Biome Loading Event} is triggered.
     *
     * Handles generating {@link #FEATURE_LIST registered Features}
     * in the {@link Biome.Category Biome} being created.
     */
    private static void onBiomeLoading(BiomeLoadingEvent event) {
        FEATURE_LIST.forEach((feature -> feature.configure(event.getGeneration(), event.getCategory())));
    }

    /**
     * Callback fired when Forge wants to register {@link Feature Features} to the game.
     * Used to create and register Resynth's Features.
     */
    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<net.minecraft.world.gen.feature.Feature<?>> event) {
        IForgeRegistry<net.minecraft.world.gen.feature.Feature<?>> featureRegistry = event.getRegistry();
        FEATURE_LIST.stream().peek(
                feature -> LOG.info("Registering ResynthFeature {}.", feature.getID().toString())
        ).filter(
                feature -> feature.getFeature() != null
        ).forEach(
                feature -> Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, feature.getID(), feature.getFeature())
        );
    }
}
