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
package com.ki11erwolf.resynth.block;

import com.ki11erwolf.resynth.ResynthMod;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * List of block references and objects.
 */
@GameRegistry.ObjectHolder(ResynthMod.MOD_ID)
public class ResynthBlocks {

    /**
     * Mods test block.
     */
    public static final Block BLOCK_TEST = null;

    /**
     * Mineral Rich Stone. The mods ore.
     */
    public static final Block BLOCK_MINERAL_ORE = null;

    /**
     * Array of all the constructed mod blocks.
     */
    private static final Block[] BLOCKS = {
            new BlockTest(),
            new BlockMineralOre()
    };

    /**
     * @return an array of all the constructed mod blocks
     */
    public static Block[] getBlocks(){
        return BLOCKS;
    }
}
