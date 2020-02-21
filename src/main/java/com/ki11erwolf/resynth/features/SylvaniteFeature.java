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
@SuppressWarnings("unused")
class SylvaniteFeature extends OreFeature {

    /**
     * Configuration settings for Sylvanite ore generation.
     */
    private static final SylvaniteGenConfig CONFIG
            = ResynthConfig.GENERAL_CONFIG.getCategory(SylvaniteGenConfig.class);

    /**
     * Registers the Sylvanite ore generation feature
     * to the End biomes provided the config allows it.
     */
    @SuppressWarnings("unused")
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
        biome.addFeature(
                GenerationStage.Decoration.UNDERGROUND_ORES,
                this.func_225566_b_(
                        new OreFeatureConfig(
                                OreFeatureConfig.FillerBlockType.NETHERRACK,//NOT USED. Use #filler
                                ResynthBlocks.BLOCK_SYLVANITE_END_STONE.getDefaultState(),
                                CONFIG.getSize()
                        )
                ).func_227228_a_(Placement.COUNT_RANGE.func_227446_a_(getRangeCount()))
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
    protected boolean func_207803_a(IWorld p_207803_1_, Random p_207803_2_, OreFeatureConfig p_207803_3_,
                                    double p_207803_4_, double p_207803_6_, double p_207803_8_,
                                    double p_207803_10_, double p_207803_12_, double p_207803_14_,
                                    int p_207803_16_, int p_207803_17_, int p_207803_18_,
                                    int p_207803_19_, int p_207803_20_) {
        int lvt_21_1_ = 0;
        BitSet lvt_22_1_ = new BitSet(p_207803_19_ * p_207803_20_ * p_207803_19_);
        BlockPos.Mutable lvt_23_1_ = new BlockPos.Mutable();
        double[] lvt_24_1_ = new double[p_207803_3_.size * 4];
        Predicate<BlockState> filler = new BlockMatcher(Blocks.END_STONE);

        int lvt_25_2_;
        double lvt_27_2_;
        double lvt_29_2_;
        double lvt_31_2_;
        double lvt_33_2_;
        for(lvt_25_2_ = 0; lvt_25_2_ < p_207803_3_.size; ++lvt_25_2_) {
            float lvt_26_1_ = (float)lvt_25_2_ / (float)p_207803_3_.size;
            lvt_27_2_ = MathHelper.lerp(lvt_26_1_, p_207803_4_, p_207803_6_);
            lvt_29_2_ = MathHelper.lerp(lvt_26_1_, p_207803_12_, p_207803_14_);
            lvt_31_2_ = MathHelper.lerp(lvt_26_1_, p_207803_8_, p_207803_10_);
            lvt_33_2_ = p_207803_2_.nextDouble() * (double)p_207803_3_.size / 16.0D;
            double lvt_35_1_ = ((double)(MathHelper.sin(3.1415927F * lvt_26_1_) + 1.0F) * lvt_33_2_ + 1.0D) / 2.0D;
            lvt_24_1_[lvt_25_2_ * 4] = lvt_27_2_;
            lvt_24_1_[lvt_25_2_ * 4 + 1] = lvt_29_2_;
            lvt_24_1_[lvt_25_2_ * 4 + 2] = lvt_31_2_;
            lvt_24_1_[lvt_25_2_ * 4 + 3] = lvt_35_1_;
        }

        for(lvt_25_2_ = 0; lvt_25_2_ < p_207803_3_.size - 1; ++lvt_25_2_) {
            if (lvt_24_1_[lvt_25_2_ * 4 + 3] > 0.0D) {
                for(int lvt_26_2_ = lvt_25_2_ + 1; lvt_26_2_ < p_207803_3_.size; ++lvt_26_2_) {
                    if (lvt_24_1_[lvt_26_2_ * 4 + 3] > 0.0D) {
                        lvt_27_2_ = lvt_24_1_[lvt_25_2_ * 4] - lvt_24_1_[lvt_26_2_ * 4];
                        lvt_29_2_ = lvt_24_1_[lvt_25_2_ * 4 + 1] - lvt_24_1_[lvt_26_2_ * 4 + 1];
                        lvt_31_2_ = lvt_24_1_[lvt_25_2_ * 4 + 2] - lvt_24_1_[lvt_26_2_ * 4 + 2];
                        lvt_33_2_ = lvt_24_1_[lvt_25_2_ * 4 + 3] - lvt_24_1_[lvt_26_2_ * 4 + 3];
                        if (lvt_33_2_ * lvt_33_2_ > lvt_27_2_ * lvt_27_2_ + lvt_29_2_ * lvt_29_2_ + lvt_31_2_ * lvt_31_2_) {
                            if (lvt_33_2_ > 0.0D) {
                                lvt_24_1_[lvt_26_2_ * 4 + 3] = -1.0D;
                            } else {
                                lvt_24_1_[lvt_25_2_ * 4 + 3] = -1.0D;
                            }
                        }
                    }
                }
            }
        }

        for(lvt_25_2_ = 0; lvt_25_2_ < p_207803_3_.size; ++lvt_25_2_) {
            double lvt_26_3_ = lvt_24_1_[lvt_25_2_ * 4 + 3];
            if (lvt_26_3_ >= 0.0D) {
                double lvt_28_1_ = lvt_24_1_[lvt_25_2_ * 4];
                double lvt_30_1_ = lvt_24_1_[lvt_25_2_ * 4 + 1];
                double lvt_32_1_ = lvt_24_1_[lvt_25_2_ * 4 + 2];
                int lvt_34_1_ = Math.max(MathHelper.floor(lvt_28_1_ - lvt_26_3_), p_207803_16_);
                int lvt_35_2_ = Math.max(MathHelper.floor(lvt_30_1_ - lvt_26_3_), p_207803_17_);
                int lvt_36_1_ = Math.max(MathHelper.floor(lvt_32_1_ - lvt_26_3_), p_207803_18_);
                int lvt_37_1_ = Math.max(MathHelper.floor(lvt_28_1_ + lvt_26_3_), lvt_34_1_);
                int lvt_38_1_ = Math.max(MathHelper.floor(lvt_30_1_ + lvt_26_3_), lvt_35_2_);
                int lvt_39_1_ = Math.max(MathHelper.floor(lvt_32_1_ + lvt_26_3_), lvt_36_1_);

                for(int lvt_40_1_ = lvt_34_1_; lvt_40_1_ <= lvt_37_1_; ++lvt_40_1_) {
                    double lvt_41_1_ = ((double)lvt_40_1_ + 0.5D - lvt_28_1_) / lvt_26_3_;
                    if (lvt_41_1_ * lvt_41_1_ < 1.0D) {
                        for(int lvt_43_1_ = lvt_35_2_; lvt_43_1_ <= lvt_38_1_; ++lvt_43_1_) {
                            double lvt_44_1_ = ((double)lvt_43_1_ + 0.5D - lvt_30_1_) / lvt_26_3_;
                            if (lvt_41_1_ * lvt_41_1_ + lvt_44_1_ * lvt_44_1_ < 1.0D) {
                                for(int lvt_46_1_ = lvt_36_1_; lvt_46_1_ <= lvt_39_1_; ++lvt_46_1_) {
                                    double lvt_47_1_ = ((double)lvt_46_1_ + 0.5D - lvt_32_1_) / lvt_26_3_;
                                    if (lvt_41_1_ * lvt_41_1_ + lvt_44_1_ * lvt_44_1_ + lvt_47_1_ * lvt_47_1_ < 1.0D) {
                                        int lvt_49_1_ = lvt_40_1_ - p_207803_16_ + (lvt_43_1_ - p_207803_17_) * p_207803_19_ + (lvt_46_1_ - p_207803_18_) * p_207803_19_ * p_207803_20_;
                                        if (!lvt_22_1_.get(lvt_49_1_)) {
                                            lvt_22_1_.set(lvt_49_1_);
                                            lvt_23_1_.setPos(lvt_40_1_, lvt_43_1_, lvt_46_1_);
                                            if (filler.test(p_207803_1_.getBlockState(lvt_23_1_))) {
                                                p_207803_1_.setBlockState(
                                                        lvt_23_1_,
                                                        ResynthBlocks.BLOCK_SYLVANITE_END_STONE.getDefaultState(),
                                                        2
                                                );
                                                ++lvt_21_1_;
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

        return lvt_21_1_ > 0;
    }
}
