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

import java.util.ArrayList;

/**
 * Stores instances of Resynth blocks that should be registered to forge.
 *
 * Note: This registry does NOT hold any plant or plant produce blocks.
 */
public class ResynthBlockRegistry {

    /**
     * Array list containing all the block instances.
     */
    private static final ArrayList<ResynthBlock> BLOCKS = new ArrayList<>();

    /**
     * Adds a block to the registry.
     *
     * @param block the block to add.
     */
    protected static void addBlock(ResynthBlock block){
        BLOCKS.add(block);
    }

    /**
     * @return an array of all Resynth block instances that should be registered.
     */
    protected static ResynthBlock[] getBlocks(){
        return BLOCKS.toArray(new ResynthBlock[0]);
    }
}
