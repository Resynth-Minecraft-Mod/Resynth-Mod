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

    private static final String BLOCK_PREFIX = "block";

    /**
     * @param material the blocks material.
     * @param name the general name of the block (e.g. stone).
     */
    public ResynthBlock(Material material, String name) {
        this(material, SoundType.STONE, name);
    }

    /**
     * @param material the blocks material.
     * @param name the general name of the block (e.g. redstoneOre).
     * @param sound the sound the block makes when interacted with.
     */
    public ResynthBlock(Material material, SoundType sound, String name) {
        super(material);
        setUnlocalizedName(ResynthMod.MOD_ID + "." + BLOCK_PREFIX + StringUtil.capitalize(name));
        setRegistryName(BLOCK_PREFIX + "_" + StringUtil.toUnderscoreLowercase(name));
        setCreativeTab(ResynthTab.RESYNTH_TAB);
        setSoundType(sound);
    }


}
