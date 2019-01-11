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
package com.ki11erwolf.resynth;

import com.ki11erwolf.resynth.block.ResynthBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Random;

import static com.ki11erwolf.resynth.ResynthConfig.ORE_GENERATION;
import static com.ki11erwolf.resynth.ResynthConfig.MYSTICAL_SEED_POD;

/**
 * Handles all world generation for resynth.
 */
public class ResynthWorldGen{

    /**
     * An enum list of all registered world generators.
     */
    protected enum WorldGenerators implements IWorldGenerator {

        /**
         * The world generator for mineral stone.
         */
        MINERAL_STONE_MINABLE(
                ORE_GENERATION.generate,
                Blocks.STONE, ResynthBlocks.BLOCK_MINERAL_ORE.getDefaultState(), DimensionType.OVERWORLD,
                ORE_GENERATION.blockCount, ORE_GENERATION.perChunk, ORE_GENERATION.minHeight,
                ORE_GENERATION.maxHeight
        ),

        /**
         * The world generator for mystical seed pods.
         */
        MYSTICAL_SEED_POD_MINABLE(
                MYSTICAL_SEED_POD.generate,
                Blocks.TALLGRASS, ResynthBlocks.BLOCK_SEED_POD.getDefaultState(), DimensionType.OVERWORLD,
                MYSTICAL_SEED_POD.blockCount, MYSTICAL_SEED_POD.perChunk, MYSTICAL_SEED_POD.minHeight,
                MYSTICAL_SEED_POD.maxHeight
        );

        /**
         * Enable/disable world generation toggle.
         */
        private final boolean generate;

        /**
         * Minecraft block in world to replace.
         */
        private final Block target;

        /**
         * New block - the block that will replace the block in the world.
         */
        private final IBlockState replacement;

        /**
         * Average number of blocks per cluster.
         */
        private final int blocksPerCluster;

        /**
         * The dimension to generate the block in.
         */
        private final DimensionType dimension;

        /**
         * The number of clusters per chunk.
         */
        private final int clustersPerChunk;

        /**
         * Minimum height to generate clusters.
         */
        private final int minHeight;

        /**
         * Maximum height to generate clusters.
         */
        private final int maxHeight;

        /**
         * The backing forge world gen instance.
         */
        private WorldGenMinable worldGenInstance;

        /**
         * Creates a new world generator.
         *
         * @param generate true to generate.
         * @param target the block in the world to replace.
         * @param replacement the replacement block.
         * @param world the world to generate the block in.
         * @param blocksPerCluster the average number of blocks in a cluster.
         * @param clustersPerChunk the average number of clusters in a chunk.
         * @param minHeight the minimum height to generate clusters.
         * @param maxHeight the maximum height to generate clusters.
         */
        WorldGenerators(boolean generate, Block target, IBlockState replacement, DimensionType world,
                        int blocksPerCluster, int clustersPerChunk, int minHeight, int maxHeight){
            this.generate = generate;
            this.target = target;
            this.replacement = replacement;
            this.blocksPerCluster = blocksPerCluster;
            this.dimension = world;
            this.clustersPerChunk = clustersPerChunk;
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
        }

        /**
         * Creates the backing forge world generator instance.
         * This is called by the resynth world generator
         * when registering all generators to the game.
         */
        private void initMinable(){
            worldGenInstance = new WorldGenMinable(
                    replacement, blocksPerCluster, BlockMatcher.forBlock(target)
            );
        }

        /**
         * {@inheritDoc}
         * Determines what world to generate the blocks in.
         *
         * @param random
         * @param chunkX
         * @param chunkZ
         * @param world
         * @param chunkGenerator
         * @param chunkProvider
         */
        @Override
        public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
                             IChunkProvider chunkProvider) {
            if(!generate)
                return;

            switch (world.provider.getDimensionType()) {
                case NETHER:
                    if(dimension == DimensionType.NETHER)
                        internalGenerate(worldGenInstance, world, random, chunkX, chunkZ,
                                clustersPerChunk, minHeight, maxHeight);
                case OVERWORLD:
                    if(dimension == DimensionType.OVERWORLD)
                        internalGenerate(worldGenInstance, world, random, chunkX, chunkZ,
                                clustersPerChunk, minHeight, maxHeight);
                    break;
                case THE_END:
                    if(dimension == DimensionType.THE_END)
                        internalGenerate(worldGenInstance, world, random, chunkX, chunkZ,
                                clustersPerChunk, minHeight, maxHeight);
                    break;
            }
        }
    }

    //*************************
    //       END GEN LIST
    //*************************

    /**
     * Static world gen initialized flag.
     */
    private static boolean isInitialized = false;

    /**
     * The weight of all resynth world generators.
     */
    private final int weight;

    /**
     * Constructs a new resynth world generator manager.
     */
    ResynthWorldGen(){
        this(0);
    }

    /**
     * Constructs a new resynth world generator manager.
     *
     * @param weight the weight of all resynth world generators.
     */
    ResynthWorldGen(int weight){
        this.weight = weight;
    }

    /**
     * Single use only.
     *
     * Constructs, initializes and registers all resynth world generators.
     */
    protected void init(){
        if(isInitialized){
            ResynthMod.getLogger().warn("Attempt to initialized world generation twice. Skipping...");
            return;
        }

        for(WorldGenerators generator : WorldGenerators.values()){
            generator.initMinable();
            GameRegistry.registerWorldGenerator(generator, weight);
        }

        isInitialized = true;
    }

    /**
     * Logic for generating clusters in the world.
     *
     * @param generator forge world generator instance
     * @param world the world the generator is assigned to.
     * @param random minecraft random instance
     * @param chunkX chunk x coordinate
     * @param chunkZ chunk z coordinate
     * @param spawnTries average number of clusters
     * @param minHeight the minimum height to generate clusters.
     * @param maxHeight the maximum height to generate clusters.
     */
    private static void internalGenerate(WorldGenerator generator, World world, Random random,
                                  int chunkX, int chunkZ, int spawnTries,
                                  int minHeight, int maxHeight){
        if(minHeight < 0) minHeight = 0;
        if(maxHeight > 255) maxHeight = 255;

        if(maxHeight < minHeight) {
            int i = minHeight;
            minHeight = maxHeight;
            maxHeight = i;
        } else if(maxHeight == minHeight) {
            if(maxHeight < 255) maxHeight++;
            else                minHeight--;
        }

        BlockPos chunkPos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
        int heightDiff = maxHeight - minHeight + 1;

        for (int i = 0; i < spawnTries; i++) {
            generator.generate(world, random, chunkPos.add(random.nextInt(16),
                    minHeight + random.nextInt(heightDiff), random.nextInt(16)));
        }
    }
}
