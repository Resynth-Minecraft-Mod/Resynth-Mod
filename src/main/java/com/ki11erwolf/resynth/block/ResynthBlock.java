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
import com.ki11erwolf.resynth.ResynthTab;
import com.ki11erwolf.resynth.util.StringUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * Base block class for all mod blocks.
 */
public class ResynthBlock extends Block {

    /**
     * The prefix for all block names.
     */
    private static final String BLOCK_PREFIX = "block";

    /**
     * @param material the blocks material.
     * @param name the general name of the block (e.g. stone).
     */
    public ResynthBlock(Material material, String name) {
        this(material, SoundType.STONE, name, BLOCK_PREFIX);
    }

    /**
     * @param material the blocks material.
     * @param sound the sound the block makes when interacted with.
     * @param name the general name of the block (e.g. stone).
     */
    public ResynthBlock(Material material, SoundType sound, String name){
        this(material, sound, name, BLOCK_PREFIX);
    }

    /**
     * @param material the blocks material.
     * @param name the general name of the block (e.g. redstoneOre).
     * @param sound the sound the block makes when interacted with.
     * @param prefix the prefix to add before all names of the block.
     */
    public ResynthBlock(Material material, SoundType sound, String name, String prefix) {
        super(material);
        setUnlocalizedName(ResynthMod.MOD_ID + "." + prefix + StringUtil.capitalize(name));
        setRegistryName(prefix + "_" + StringUtil.toUnderscoreLowercase(name));
        setCreativeTab(ResynthTab.RESYNTH_TAB);
        setSoundType(sound);
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
