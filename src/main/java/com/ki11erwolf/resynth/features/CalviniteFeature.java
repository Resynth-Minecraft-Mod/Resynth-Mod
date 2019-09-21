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
import com.ki11erwolf.resynth.config.categories.CalviniteGenConfig;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;

/**
 * Calvinite Infused Netherrack ore generation feature. Handles generating
 * {@link com.ki11erwolf.resynth.block.ResynthBlocks#BLOCK_CALVINITE_NETHERRACK}
 * in the world as ore veins.
 */
class CalviniteFeature extends OreFeature {

    /**
     * Configuration settings for Calvinite ore generation.
     */
    private static final CalviniteGenConfig CONFIG
            = ResynthConfig.GENERAL_CONFIG.getCategory(CalviniteGenConfig.class);

    /**
     * Registers the Calvinite ore generation feature
     * to the Nether biome provided the config allows it.
     */
    CalviniteFeature(){
        super(OreFeatureConfig::deserialize);

        if(!CONFIG.shouldGenerate())
            return;

        add(Biomes.NETHER);
    }

    /**
     * Adds this feature to the given biome.
     *
     * @param biome the biome to add this feature to.
     */
    private void add(@SuppressWarnings("SameParameterValue") Biome biome){
        biome.addFeature(
                GenerationStage.Decoration.UNDERGROUND_ORES,
                Biome.createDecoratedFeature(
                        this,
                        new OreFeatureConfig(
                                OreFeatureConfig.FillerBlockType.NETHERRACK,
                                ResynthBlocks.BLOCK_CALVINITE_NETHERRACK.getDefaultState(),
                                CONFIG.getSize()
                        ),
                        Placement.COUNT_RANGE,
                        getRangeCount()
                )
        );
    }

    /**
     * @return a {@link CountRangeConfig} object with values
     * specified by the config.
     */
    private CountRangeConfig getRangeCount(){
        return new CountRangeConfig(
                CONFIG.getCount(),
                CONFIG.getBottomOffset(),
                CONFIG.getTopOffset(),
                CONFIG.getMaxHeight()
        );
    }
}
