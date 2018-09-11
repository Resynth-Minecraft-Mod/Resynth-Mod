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
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * Base block class for all mod blocks.
 */
public class ResynthBlock extends Block {

    /**
     * @param material the blocks material.
     * @param unlocalizedName the unlocalized name of the block.
     * @param registryName the registry name of the block.
     */
    public ResynthBlock(Material material, String unlocalizedName, String registryName) {
        this(material, SoundType.STONE, unlocalizedName, registryName);
    }

    /**
     * @param material the blocks material.
     * @param unlocalizedName the unlocalized name of the block.
     * @param registryName the registry name of the block.
     * @param sound the sound the block makes when interacted with.
     */
    public ResynthBlock(Material material, SoundType sound, String unlocalizedName, String registryName) {
        super(material);
        setUnlocalizedName(ResynthMod.MOD_ID + "." + unlocalizedName);
        setRegistryName(registryName);
        setCreativeTab(ResynthTab.RESYNTH_TAB);
        setSoundType(sound);
    }

}
