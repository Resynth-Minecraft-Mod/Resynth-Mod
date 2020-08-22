///*
// * Copyright 2018-2020 Ki11er_wolf
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.ki11erwolf.resynth.features;
//
//import com.ki11erwolf.resynth.block.ResynthBlocks;
//import com.ki11erwolf.resynth.config.ResynthConfig;
//import com.ki11erwolf.resynth.config.categories.CalviniteGenConfig;
//import net.minecraft.world.biome.*;
//import net.minecraft.world.gen.GenerationStage;
//import net.minecraft.world.gen.feature.OreFeature;
//import net.minecraft.world.gen.feature.OreFeatureConfig;
////import net.minecraft.world.gen.placement.CountRangeConfig;
//import net.minecraft.world.gen.placement.NoiseDependant;
//import net.minecraft.world.gen.placement.Placement;
//import net.minecraft.world.gen.placement.TopSolidRangeConfig;
//
///**
// * Calvinite Infused Netherrack ore generation feature. Handles generating
// * {@link com.ki11erwolf.resynth.block.ResynthBlocks#BLOCK_CALVINITE_NETHERRACK}
// * in the world as ore veins.
// */
//class CalviniteFeature extends OreFeature {
//
//    /**
//     * Configuration settings for Calvinite ore generation.
//     */
//    private static final CalviniteGenConfig CONFIG
//            = ResynthConfig.GENERAL_CONFIG.getCategory(CalviniteGenConfig.class);
//
//    /**
//     * Registers the Calvinite ore generation feature
//     * to the Nether biome provided the config allows it.
//     */
//    CalviniteFeature(){
//        super(OreFeatureConfig.field_236566_a_);
//
//        if(!CONFIG.shouldGenerate())
//            return;
//
//        //Renaming nether biomes...
//        Biome waistland = Biomes.field_235254_j_;
//        Biome soulSandVally = Biomes.field_235252_ay_;
//        Biome crimsonForest = Biomes.field_235253_az_;
//        Biome warpedForest = Biomes.field_235250_aA_;
//        Biome basaltDelta = Biomes.field_235251_aB_;
//
//        add(waistland);
//        add(soulSandVally);
//        add(crimsonForest);
//        add(warpedForest);
//        add(basaltDelta);
//    }
//
//    /**
//     * Adds this feature to the given biome. As of 1.16,
//     * this now also spawns the ore in the different biomes
//     * of the nether.
//     *
//     * @param biome the biome (nether type) to add this feature to.
//     */
//    private void add(Biome biome){
//        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
//            this.withConfiguration(
//                new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241883_b,
//                    ResynthBlocks.BLOCK_CALVINITE_NETHERRACK.getDefaultState(),
//                    CONFIG.getSize()
//                )
//            ).withPlacement(Placement.field_242900_d.configure(
//                    new NoiseDependant(10, 10, 10)
//            ))
//        );
//    }
//
////
////    /**
////     * @return a {@link CountRangeConfig} object with values
////     * specified by the config.
////     */
////    private CountRangeConfig getRangeCount(){
////        return new CountRangeConfig(CONFIG.getCount(), CONFIG.getBottomOffset(),
////                CONFIG.getTopOffset(), CONFIG.getMaxHeight()
////        );
////    }
//}
