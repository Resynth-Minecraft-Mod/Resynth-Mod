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
//
//import com.ki11erwolf.resynth.block.ResynthBlocks;
//import com.ki11erwolf.resynth.config.ResynthConfig;
//import com.ki11erwolf.resynth.config.categories.SeedPodConfig;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.ISeedReader;
//import net.minecraft.world.World;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.gen.ChunkGenerator;
//import net.minecraft.world.gen.GenerationStage;
//import net.minecraft.world.gen.feature.Feature;
//import net.minecraft.world.gen.feature.IFeatureConfig;
//import net.minecraft.world.gen.feature.NoFeatureConfig;
//import net.minecraft.world.gen.feature.structure.StructureManager;
//import net.minecraft.world.gen.placement.CountRangeConfig;
//import net.minecraft.world.gen.placement.Placement;
//
//import java.util.Random;
//
///**
// * Mystical Seed Pod Feature. Handles generating the
// * {@link com.ki11erwolf.resynth.block.BlockSeedPod}
// * in the world as a flower. The Seed Pod will generate
// * in every biome.
// */
//class SeedPodFeature extends Feature<NoFeatureConfig> {
//
//    /**
//     * Configuration settings for Seed Pod generation.
//     */
//    private static final SeedPodConfig CONFIG = ResynthConfig.GENERAL_CONFIG.getCategory(SeedPodConfig.class);
//
//    /**
//     * Adds this feature to every biome provided
//     * the config allows the plant to spawn.
//     */
//    SeedPodFeature(){
//        super(NoFeatureConfig.field_236558_a_);
//
//        //Skip generation
//        if(!CONFIG.generate())
//            return;
//
//        Biome.BIOMES.forEach(biome -> biome.addFeature(
//                GenerationStage.Decoration.UNDERGROUND_ORES,
//                this.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(
//                        Placement.COUNT_RANGE.configure(
//                                new CountRangeConfig(CONFIG.getGenerationFrequency(),
//                                        0,0,255
//                                )
//                        )
//                )
//        ));
//    }
//
//    /**
//     * {@inheritDoc}
//     * Called from {@link #func_230362_a_(ISeedReader, StructureManager,
//     * ChunkGenerator, Random, BlockPos, NoFeatureConfig)}. Use this method
//     * instead of the obfuscated one Minecraft provided.
//     *
//     * Handles placing the Mystical Seed Pod block in the world, as flowers atop dirt, grass,
//     * corse dirt, and podzol. Only places flowers within air blocks.
//     *
//     * @param seedReader object that provides a reference to the world object instance.
//     * @param random Minecrafts global Random object instance.
//     * @param pos placement block position.
//     * @return {@code true} if an ore/block/resource was actually placed (I think).
//     */
//    private boolean place(ISeedReader seedReader, Random random, BlockPos pos/*, ChunkGenerator gen,
//                          StructureManager structureManager, NoFeatureConfig nfc*/) {
//        BlockState blockstate = ResynthBlocks.BLOCK_SEED_POD.getDefaultState();
//        World world = seedReader.getWorld();
//        int tries = CONFIG.getGenerationFrequency();
//        int i = 0;
//
//        for(int j = 0; j < tries; ++j) {
//            BlockPos blockpos = pos.add(
//                    random.nextInt(8) - random.nextInt(8),
//                    random.nextInt(4) - random.nextInt(4),
//                    random.nextInt(8) - random.nextInt(8)
//            );
//
//            if (world.isAirBlock(blockpos) && blockpos.getY() < 255
//                    && (world.getBlockState(blockpos.down()).getBlock() == Blocks.GRASS_BLOCK
//                    || world.getBlockState(blockpos.down()).getBlock() == Blocks.DIRT
//                    || world.getBlockState(blockpos.down()).getBlock() == Blocks.COARSE_DIRT
//                    || world.getBlockState(blockpos.down()).getBlock() == Blocks.PODZOL)) {
//                world.setBlockState(blockpos, blockstate, 2);
//                ++i;
//            }
//        }
//
//        return i > 0;
//    }
//
//    /**
//     * Minecraft Feature <code>place()</code> method. The name is obfuscated, so, we just call the method
//     * {@link #place(ISeedReader, Random, BlockPos)} and return its value; pretending
//     * this method doesn't exist.
//     *
//     *
//     * @param seedReader object that provides a reference to the world object instance.
//     * @param structureManager structure generation. Unused.
//     * @param gen chunk generation object. Unused.
//     * @param random Minecrafts global Random object instance.
//     * @param pos placement block position.
//     * @param nfc config object representing a feature with no config. Similar to <code>void</code>. Unused.
//     * @return {@code true} if an ore/block/resource was actually placed (I think).
//     */
//    @Override
//    public boolean func_230362_a_(ISeedReader seedReader, StructureManager structureManager, ChunkGenerator gen,
//                                  Random random, BlockPos pos, NoFeatureConfig nfc) {
//        return place(seedReader, random, pos/*, gen, structureManager, nfc*/);
//    }
//}