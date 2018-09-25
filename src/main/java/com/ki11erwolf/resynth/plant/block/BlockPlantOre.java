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
package com.ki11erwolf.resynth.plant.block;

import com.ki11erwolf.resynth.block.ResynthBlock;
import com.ki11erwolf.resynth.util.BlockUtil;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * The base class for all blocks produced
 * by metallic plants.
 */
public class BlockPlantOre extends ResynthBlock {

    /**
     * Prefix for the block.
     */
    protected static final String ORE_PREFIX = "ore";

    /**
     * Constructs a new plant ore block.
     *
     * @param name the name of the block.
     */
    public BlockPlantOre(String name) {
        super(Material.ROCK, SoundType.PLANT, name, ORE_PREFIX);
        this.setHardness(2.0F);
        BlockUtil.setHarvestLevel(this, BlockUtil.HarvestTools.AXE, 2);
    }
}
