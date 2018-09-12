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
package com.ki11erwolf.resynth.util;

import net.minecraft.block.Block;

/**
 * A bunch of utilities for blocks.
 */
public final class BlockUtil {

    //Static class
    private BlockUtil(){}

    /**
     * Sets the harvest level of a block
     * using a set of enums rather than
     * a string. Makes life easier.
     *
     * @param b the block to set the harvest level of.
     * @param tool the tool the block is harvested with.
     * @param level the level of the tool
     *     Wood:    0
     *     Stone:   1
     *     Iron:    2
     *     Diamond: 3
     *     Gold:    0
     */
    public static void setHarvestLevel(Block b, HarvestTools tool, int level){
        b.setHarvestLevel(tool.getToolName(), level);
    }

    /**
     * An enum list of tools used to break blocks.
     * Used with {@link Block#setHarvestLevel(String, int)}.
     */
    public enum HarvestTools{

        /**
         * The pickaxe tool.
         */
        PICKAXE("pickaxe"),

        /**
         * The shovel/spade tool.
         */
        SHOVEL("shovel"),

        /**
         * The axe tool.
         */
        AXE("axe");

        /**
         * Lowercase string name of the tool. Defined by
         * force/minecraft source.
         */
        private final String toolName;

        /**
         * @param toolName Lowercase string name of the tool. Defined by
         * force/minecraft source.
         */
        HarvestTools(String toolName) {
            this.toolName = toolName;
        }

        /**
         * @return Lowercase string name of the tool. Defined by
         * force/minecraft source.
         */
        public String getToolName(){
            return this.toolName;
        }
    }
}
