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

import com.ki11erwolf.resynth.util.StringUtil;
import net.minecraft.block.Block;

/**
 * Base block class for all Resynth blocks.
 */
public class ResynthBlock extends Block {

    /**
     * The prefix for all block names (excluding plant/seed/produce items).
     */
    private static final String BLOCK_PREFIX = "block";

    /**
     * Default constructor for all mod blocks.
     *
     * @param properties the characteristics of the block.
     * @param name the identifying name of the block (e.g. stone or woodenDoor).
     */
    public ResynthBlock(Properties properties, String name) {
        this(properties, name, BLOCK_PREFIX);
    }

    /**
     * @param properties the characteristics of the block.
     * @param name the general name of the block (e.g. redstoneOre).
     * @param prefix the prefix to add before all names of the block.
     */
    public ResynthBlock(Properties properties, String name, String prefix) {
        super(properties);
        //setUnlocalizedName(ResynthMod.MOD_ID + "." + prefix + StringUtil.capitalize(name));
        setRegistryName(prefix + "_" + StringUtil.toUnderscoreLowercase(name));
        //setCreativeTab(ResynthTabs.RESYNTH_TAB);
        //setSoundType(sound);
    }

    /**
     * Adds this block to the list of blocks to be
     * registered to the game though forge.
     *
     * @return {@code this} block.
     */
    protected ResynthBlock register(){
        ResynthBlockRegistry.addBlock(this);
        return this;
    }
}
