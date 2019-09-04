/*
 * Copyright 2018-2019 Ki11er_wolf
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


import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.SeedPodConfig;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.DefaultFlowersFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;

/**
 * Mystical Seed Pod Feature. Handles generating the
 * {@link com.ki11erwolf.resynth.block.BlockSeedPod}
 * in the world as a flower. The Seed Pod will generate
 * in every biome.
 */
class SeedPodFeature extends DefaultFlowersFeature {

    /**
     * Configuration settings for Seed Pod generation.
     */
    private static final SeedPodConfig CONFIG = ResynthConfig.GENERAL_CONFIG.getCategory(SeedPodConfig.class);

    /**
     * Adds this feature to every biome provided
     * the config allows the plant to spawn.
     */
    SeedPodFeature(){
        super(NoFeatureConfig::deserialize);

        //Skip generation
        if(!CONFIG.generate())
            return;

        Biome.BIOMES.forEach(
                biome -> biome.addFeature(
                        GenerationStage.Decoration.VEGETAL_DECORATION,
                        Biome.createDecoratedFeature(
                                this,
                                NoFeatureConfig.NO_FEATURE_CONFIG,
                                Placement.COUNT_HEIGHTMAP_32,
                                new FrequencyConfig(CONFIG.getGenerationFrequency())
                        )
                )
        );
    }

    /**
     * @return {@link ResynthBlocks#BLOCK_SEED_POD} as this is the
     * only block we want to spawn.
     */
    public BlockState getRandomFlower(Random random, BlockPos pos) {
        return ResynthBlocks.BLOCK_SEED_POD.getDefaultState();
    }
}