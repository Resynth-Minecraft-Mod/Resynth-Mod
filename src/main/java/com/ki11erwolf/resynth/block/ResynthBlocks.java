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
package com.ki11erwolf.resynth.block;

/**
 * Static list of Resynth block instances as well as a getAsArray method.
 *
 * Note: This class does NOT hold any plant or plant produce blocks.
 */
public class ResynthBlocks {

    /**
     * Mineral Rich Stone. The mods ore.
     */
    public static final ResynthBlock BLOCK_MINERAL_ORE = new BlockMineralOre().register();

    /**
     * Mineral Enriched Soil. The farmland block for the mod.
     */
    public static final ResynthBlock BLOCK_MINERAL_SOIL = new BlockMineralSoil().register();

    /**
     * Mystical Seed Pos. The random biochemical seed dropper plant.
     */
    public static final ResynthBlock BLOCK_SEED_POD = new BlockSeedPod().register();

    /**
     * @return an array of all the TO BE registered resynth blocks (the returned instances
     * may not be registered yet!).
     */
    public static ResynthBlock[] getResynthBlocks(){
        return ResynthBlockRegistry.getBlocks();
    }
}
