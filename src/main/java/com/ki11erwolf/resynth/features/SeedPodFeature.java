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
public class SeedPodFeature extends AbstractFlowersFeature {

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
