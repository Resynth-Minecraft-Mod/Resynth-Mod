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
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.AbstractFlowersFeature;
import net.minecraft.world.gen.placement.FrequencyConfig;

import java.util.Random;

import static net.minecraft.world.biome.Biome.createCompositeFlowerFeature;

/**
 * Mystical Seed Pod Feature. Handles generating the
 * {@link com.ki11erwolf.resynth.block.BlockSeedPod}
 * in the world as a flower. The Seed Pod will generate
 * in every biome.
 */
class SeedPodFeature extends AbstractFlowersFeature {

    /**
     * Configuration settings for Seed Pod generation.
     */
    private static final SeedPodConfig CONFIG = ResynthConfig.GENERAL_CONFIG.getCategory(SeedPodConfig.class);

    /**
     * Adds this feature to every biome.
     */
    SeedPodFeature(){
        //Skip generation
        if(!CONFIG.generate())
            return;

        Biome.BIOMES.forEach(
                biome -> biome.addFeature(
                        GenerationStage.Decoration.VEGETAL_DECORATION,
                        createCompositeFlowerFeature(
                                this,
                                Biome.SURFACE_PLUS_32,
                                new FrequencyConfig(CONFIG.getGenerationFrequency())
                        )
                )
        );
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link ResynthBlocks#BLOCK_SEED_POD}.
     */
    @Override
    public IBlockState getRandomFlower(Random random, BlockPos pos) {
        return ResynthBlocks.BLOCK_SEED_POD.getDefaultState();
    }
}
