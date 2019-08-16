package com.ki11erwolf.resynth.features;

import com.ki11erwolf.resynth.block.BlockMineralStone;
import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.MineralStoneGenConfig;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.MinableConfig;
import net.minecraft.world.gen.feature.MinableFeature;
import net.minecraft.world.gen.placement.CountRangeConfig;

/**
 * Mineral Infused Stone ore generation feature. Handles
 * generating {@link BlockMineralStone} in the world
 * as ore veins.
 */
class MineralStoneFeature extends MinableFeature {

    /**
     * Configuration settings for Mineral Stone ore generation.
     */
    private static final MineralStoneGenConfig CONFIG
            = ResynthConfig.GENERAL_CONFIG.getCategory(MineralStoneGenConfig.class);

    /**
     * Registers the Mineral Stone ore generation feature
     * to every biome provided the config allows it.
     */
    MineralStoneFeature(){
        if(!CONFIG.shouldGenerate())
            return;

        Biome.BIOMES.forEach(this::add);
    }

    /**
     * Adds this feature to the given biome.
     *
     * @param biome the biome to add this feature to.
     */
    private void add(Biome biome){
        biome.addFeature(
                GenerationStage.Decoration.UNDERGROUND_ORES,
                Biome.createCompositeFeature(
                        this,
                        new MinableConfig(
                                MinableConfig.IS_ROCK,
                                ResynthBlocks.BLOCK_MINERAL_ORE.getDefaultState(),
                                CONFIG.getSize()
                        ),
                        Biome.COUNT_RANGE,
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
                CONFIG.getMinHeight(),
                CONFIG.getMaxBaseHeight(),
                CONFIG.getMaxHeight()
        );
    }
}
