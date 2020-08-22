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
package com.ki11erwolf.resynth.features;

import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.SylvaniteGenConfig;
import net.minecraft.block.Block;
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
//TODO: I worry about the class... CHECK ORE GEN!11
class SylvaniteFeature extends OreFeature {

    /**
     * Configuration settings for Sylvanite ore generation.
     */
    private static final SylvaniteGenConfig CONFIG = ResynthConfig.GENERAL_CONFIG.getCategory(SylvaniteGenConfig.class);

    /**
     * Registers the Sylvanite ore generation feature
     * to the End biomes provided the config allows it.
     */
    SylvaniteFeature(){
        super(OreFeatureConfig.field_236566_a_);

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
        biome.addFeature(
                GenerationStage.Decoration.UNDERGROUND_ORES,
                this.withConfiguration(
                        new OreFeatureConfig(
                                OreFeatureConfig.FillerBlockType.NETHERRACK,//NOT USED. Use #filler
                                ResynthBlocks.BLOCK_SYLVANITE_END_STONE.getDefaultState(),
                                CONFIG.getSize()
                        )
                ).withPlacement(Placement.COUNT_RANGE.configure(getRangeCount()))
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
     *
     * <b>In 1.16 port,</b> refractored variable names to be shorter and restrucred code.
     * Functionality was intentionally kept identical as before.
     */
    @Override
    protected boolean func_207803_a(IWorld wrld, Random rand, OreFeatureConfig cfg, double d01, double d02, double d03,
                                    double d04, double d05, double d06, int i01, int i02, int i03, int i04, int i05) {
        BitSet bitSet = new BitSet(i04 * i05 * i04);
        BlockPos.Mutable pm = new BlockPos.Mutable();
        double[] d0001 = new double[cfg.size * 4];
        Predicate<BlockState> filler = new BlockMatcher(Blocks.END_STONE);
        int i1; double d1; double d2; double d3; double d4; int i0 = 0;

        //For 0
        for(i1 = 0; i1 < cfg.size; ++i1) {
            float lvt_26_1_ = (float)i1 / (float)cfg.size;
            d1 = MathHelper.lerp(lvt_26_1_, d01, d02);
            d2 = MathHelper.lerp(lvt_26_1_, d05, d06);
            d3 = MathHelper.lerp(lvt_26_1_, d03, d04);
            d4 = rand.nextDouble() * (double)cfg.size / 16.0D;
            double lvt_35_1_
                    = ((double)(MathHelper.sin(3.1415927F * lvt_26_1_) + 1.0F) * d4 + 1.0D) / 2.0D;
            d0001[i1 * 4] = d1;
            d0001[i1 * 4 + 1] = d2;
            d0001[i1 * 4 + 2] = d3;
            d0001[i1 * 4 + 3] = lvt_35_1_;
        }

        //For 1
        for(i1 = 0; i1 < cfg.size - 1; ++i1) {
            if (d0001[i1 * 4 + 3] > 0.0D) {
                for(int lvt_26_2_ = i1 + 1; lvt_26_2_ < cfg.size; ++lvt_26_2_) {
                    if (d0001[lvt_26_2_ * 4 + 3] > 0.0D) {
                        d1 = d0001[i1 * 4] - d0001[lvt_26_2_ * 4];
                        d2 = d0001[i1 * 4 + 1] - d0001[lvt_26_2_ * 4 + 1];
                        d3 = d0001[i1 * 4 + 2] - d0001[lvt_26_2_ * 4 + 2];
                        d4 = d0001[i1 * 4 + 3] - d0001[lvt_26_2_ * 4 + 3];
                        if (d4 * d4 > d1 * d1 + d2
                                * d2 + d3 * d3) {
                            if (d4 > 0.0D) {
                                d0001[lvt_26_2_ * 4 + 3] = -1.0D;
                            } else {
                                d0001[i1 * 4 + 3] = -1.0D;
                            }
                        }
                    }
                }
            }
        }

        //For 2
        for(i1 = 0; i1 < cfg.size; ++i1) {
            double lvt_26_3_ = d0001[i1 * 4 + 3];
            if (lvt_26_3_ >= 0.0D) {
                double dd1 = d0001[i1 * 4];
                double dd2 = d0001[i1 * 4 + 1];
                double dd3 = d0001[i1 * 4 + 2];
                int lvt_34_1_ = Math.max(MathHelper.floor(dd1 - lvt_26_3_), i01);
                int lvt_35_2_ = Math.max(MathHelper.floor(dd2 - lvt_26_3_), i02);
                int lvt_36_1_ = Math.max(MathHelper.floor(dd3 - lvt_26_3_), i03);
                int lvt_37_1_ = Math.max(MathHelper.floor(dd1 + lvt_26_3_), lvt_34_1_);
                int lvt_38_1_ = Math.max(MathHelper.floor(dd2 + lvt_26_3_), lvt_35_2_);
                int lvt_39_1_ = Math.max(MathHelper.floor(dd3 + lvt_26_3_), lvt_36_1_);

                //For 2 -> For 1
                for(int lvt_40_1_ = lvt_34_1_; lvt_40_1_ <= lvt_37_1_; ++lvt_40_1_) {
                    double d001 = ((double)lvt_40_1_ + 0.5D - dd1) / lvt_26_3_;
                    if (d001 * d001 < 1.0D) {
                        for(int lvt_43_1_ = lvt_35_2_; lvt_43_1_ <= lvt_38_1_; ++lvt_43_1_) {
                            double d002 = ((double)lvt_43_1_ + 0.5D - dd2) / lvt_26_3_;
                            if (d001 * d001 + d002 * d002 < 1.0D) {
                                for(int lvt_46_1_ = lvt_36_1_; lvt_46_1_ <= lvt_39_1_; ++lvt_46_1_) {
                                    double d003 = ((double)lvt_46_1_ + 0.5D - dd3) / lvt_26_3_;
                                    if (d001 * d001 + d002 * d002 + d003 * d003 < 1.0D) {
                                        int lvt_49_1_ = lvt_40_1_ - i01 + (lvt_43_1_ - i02)
                                                * i04 + (lvt_46_1_ - i03) * i04 * i05;
                                        if (!bitSet.get(lvt_49_1_)) {
                                            bitSet.set(lvt_49_1_);
                                            pm.setPos(lvt_40_1_, lvt_43_1_, lvt_46_1_);
                                            if (filler.test(wrld.getBlockState(pm))) {
                                                Block bses = ResynthBlocks.BLOCK_SYLVANITE_END_STONE;
                                                BlockState bsses = bses.getDefaultState();
                                                wrld.setBlockState(pm, bsses,2);
                                                ++i0;
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

        return i0 > 0;
    }
}
