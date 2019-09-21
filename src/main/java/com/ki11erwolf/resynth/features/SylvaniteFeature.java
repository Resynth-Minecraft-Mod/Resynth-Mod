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
import com.ki11erwolf.resynth.config.categories.SylvaniteGenConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Sylvanite Infused End Stone ore generation feature. Handles generating
 * {@link ResynthBlocks#BLOCK_SYLVANITE_END_STONE}
 * in the world as ore veins.
 */
class SylvaniteFeature extends OreFeature {

    /**
     * Configuration settings for Sylvanite ore generation.
     */
    private static final SylvaniteGenConfig CONFIG
            = ResynthConfig.GENERAL_CONFIG.getCategory(SylvaniteGenConfig.class);

    /**
     * End Stone - The block to replace with the ore. Used instead of the config provided one.
     */
    private final Predicate<BlockState> filler = new BlockMatcher(Blocks.END_STONE);

    /**
     * Registers the Sylvanite ore generation feature
     * to the End biomes provided the config allows it.
     */
    SylvaniteFeature(){
        super(OreFeatureConfig::deserialize);

        if(!CONFIG.shouldGenerate())
            return;

        add(Biomes.THE_END);
        add(Biomes.END_BARRENS);
        add(Biomes.END_HIGHLANDS);
        add(Biomes.END_MIDLANDS);
        add(Biomes.SMALL_END_ISLANDS);
    }

    /**
     * Adds this feature to the given biome.
     *
     * @param biome the biome to add this feature to.
     */
    private void add(@SuppressWarnings("SameParameterValue") Biome biome){
        //noinspection ConstantConditions //We don't use the value
        biome.addFeature(
                GenerationStage.Decoration.UNDERGROUND_ORES,
                Biome.createDecoratedFeature(
                        this,
                        new OreFeatureConfig(
                                null,//NOT USED. Use #filler
                                ResynthBlocks.BLOCK_SYLVANITE_END_STONE.getDefaultState(),
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

    // *********************
    // MC CODE MODIFICATIONS
    // *********************

    /**
     * Modified to use the provided filler block (block to replace) instead of the config
     * provided one as the config provided one does not allow End Stone.
     */
    @Override
    protected boolean func_207803_a(IWorld worldIn, Random random, OreFeatureConfig config, double p_207803_4_,
                                    double p_207803_6_, double p_207803_8_, double p_207803_10_,
                                    double p_207803_12_, double p_207803_14_, int p_207803_16_,
                                    int p_207803_17_, int p_207803_18_, int p_207803_19_, int p_207803_20_) {
        int i = 0;
        BitSet bitset = new BitSet(p_207803_19_ * p_207803_20_ * p_207803_19_);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        double[] adouble = new double[config.size * 4];

        for(int j = 0; j < config.size; ++j) {
            float f = (float)j / (float)config.size;
            double d0 = MathHelper.lerp(f, p_207803_4_, p_207803_6_);
            double d2 = MathHelper.lerp(f, p_207803_12_, p_207803_14_);
            double d4 = MathHelper.lerp(f, p_207803_8_, p_207803_10_);
            double d6 = random.nextDouble() * (double)config.size / 16.0D;
            double d7 = ((double)(MathHelper.sin((float)Math.PI * f) + 1.0F) * d6 + 1.0D) / 2.0D;
            adouble[j * 4] = d0;
            adouble[j * 4 + 1] = d2;
            adouble[j * 4 + 2] = d4;
            adouble[j * 4 + 3] = d7;
        }

        for(int l2 = 0; l2 < config.size - 1; ++l2) {
            if (!(adouble[l2 * 4 + 3] <= 0.0D)) {
                for(int j3 = l2 + 1; j3 < config.size; ++j3) {
                    if (!(adouble[j3 * 4 + 3] <= 0.0D)) {
                        double d12 = adouble[(l2 << 2)] - adouble[j3 * 4];
                        double d13 = adouble[l2 * 4 + 1] - adouble[j3 * 4 + 1];
                        double d14 = adouble[l2 * 4 + 2] - adouble[j3 * 4 + 2];
                        double d15 = adouble[l2 * 4 + 3] - adouble[j3 * 4 + 3];
                        if (d15 * d15 > d12 * d12 + d13 * d13 + d14 * d14) {
                            if (d15 > 0.0D) {
                                adouble[j3 * 4 + 3] = -1.0D;
                            } else {
                                adouble[l2 * 4 + 3] = -1.0D;
                            }
                        }
                    }
                }
            }
        }

        for(int i3 = 0; i3 < config.size; ++i3) {
            double d11 = adouble[i3 * 4 + 3];
            if (!(d11 < 0.0D)) {
                double d1 = adouble[i3 * 4];
                double d3 = adouble[i3 * 4 + 1];
                double d5 = adouble[i3 * 4 + 2];
                int k = Math.max(MathHelper.floor(d1 - d11), p_207803_16_);
                int k3 = Math.max(MathHelper.floor(d3 - d11), p_207803_17_);
                int l = Math.max(MathHelper.floor(d5 - d11), p_207803_18_);
                int i1 = Math.max(MathHelper.floor(d1 + d11), k);
                int j1 = Math.max(MathHelper.floor(d3 + d11), k3);
                int k1 = Math.max(MathHelper.floor(d5 + d11), l);

                for(int l1 = k; l1 <= i1; ++l1) {
                    double d8 = ((double)l1 + 0.5D - d1) / d11;
                    if (d8 * d8 < 1.0D) {
                        for(int i2 = k3; i2 <= j1; ++i2) {
                            double d9 = ((double)i2 + 0.5D - d3) / d11;
                            if (d8 * d8 + d9 * d9 < 1.0D) {
                                for(int j2 = l; j2 <= k1; ++j2) {
                                    double d10 = ((double)j2 + 0.5D - d5) / d11;
                                    if (d8 * d8 + d9 * d9 + d10 * d10 < 1.0D) {
                                        int k2 = l1 - p_207803_16_ + (i2 - p_207803_17_)
                                                * p_207803_19_ + (j2 - p_207803_18_) * p_207803_19_ * p_207803_20_;
                                        if (!bitset.get(k2)) {
                                            bitset.set(k2);
                                            blockpos$mutableblockpos.setPos(l1, i2, j2);
                                            if (filler.test(worldIn.getBlockState(blockpos$mutableblockpos))) {
                                                worldIn.setBlockState(blockpos$mutableblockpos, config.state, 2);
                                                ++i;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return i > 0;
    }
}
