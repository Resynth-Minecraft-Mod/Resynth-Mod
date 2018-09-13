/*
 * Copyright 2018 Ki11er_wolf
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
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Objects;
import java.util.Random;

/**
 * Handles what blocks to generate in which world
 * and how many.
 */
//The saddest moment in Minecraft modding comes
//when you find diamond ore before your own...
public class ResynthWorldGen implements IWorldGenerator {

    /**
     * Static instance of this class.
     */
    public static final ResynthWorldGen INSTANCE = new ResynthWorldGen();

    /**
     * The world gen object for Mineral Stone.
     */
    private final WorldGenMinable oreMineralStone;

    /**
     * Configuration settings for ore generation.
     */
    private final ResynthConfig.OreGen oreGenConfig;

    /**
     * Initializes everything.
     */
    private ResynthWorldGen(){
        this.oreGenConfig = ResynthConfig.oreGen;
        oreMineralStone = new WorldGenMinable(
                Objects.requireNonNull(ResynthBlocks.BLOCK_MINERAL_ORE).getDefaultState(),
                15,
                BlockMatcher.forBlock(Blocks.STONE)
        );
    }


    /**
     * {@inheritDoc}
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
        switch (world.provider.getDimensionType()) {
            case NETHER:
                break;
            case OVERWORLD:
                if(oreGenConfig.generate)
                    iGenerate(oreMineralStone, world, random, chunkX, chunkZ,
                        oreGenConfig.perChunk, oreGenConfig.minHeight, oreGenConfig.maxHeight);
                break;
            case THE_END:
                break;

        }
    }

    /**
     * Generates the given ore in the given boundaries.
     *
     * @param generator the ore generator.
     * @param world the world (end, nether).
     * @param random random object instance.
     * @param chunkX chunk x position.
     * @param chunkZ chunk z position.
     * @param spawnTries number of times to generate the ore.
     * @param minHeight minimum height to generate the ore.
     * @param maxHeight maximum height to generate the ore.
     */
    private void iGenerate(WorldGenerator generator, World world, Random random, int chunkX, int chunkZ,
                           int spawnTries, int minHeight, int maxHeight){
        if(minHeight < 0) minHeight = 0;
        if(maxHeight > 255) maxHeight = 255;

        if(maxHeight < minHeight) {
            int i = minHeight;
            minHeight = maxHeight;
            maxHeight = i;
        } else if(maxHeight == minHeight) {
            if(maxHeight < 255) {
                maxHeight++;
            } else minHeight--;
        }

        BlockPos chunkPos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
        int heightDiff = maxHeight - minHeight + 1;

        for (int i = 0; i < spawnTries; i++) {
            generator.generate(world, random, chunkPos.add(random.nextInt(16), minHeight
                    + random.nextInt(heightDiff), random.nextInt(16)));
        }
    }
}
