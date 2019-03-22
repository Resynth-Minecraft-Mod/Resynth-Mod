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
package com.ki11erwolf.resynth.block.tileEntity;

import com.ki11erwolf.resynth.block.ResynthBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * The base class for all blocks that provide a tile entity.
 * This class extends the functionality of the base class (ResynthBlock)
 * for tile entities.
 *
 * @param <TE> the tile entity class.
 */
public abstract class ResynthTileEntity <TE extends TileEntity> extends ResynthBlock {

    /**
     * Basic constructor for all Resynth tile entities.
     *
     * @param material the material of the block.
     * @param name the name of the block (e.g. redstoneDust).
     */
    protected ResynthTileEntity(Material material, String name) {
        super(material, name);
    }

    /**
     * Extended constructor for all Resynth tile entities.
     *
     * @param material the material of the block.
     * @param sound the sound the block makes when broken or walked on.
     * @param name the name of the block (e.g. redstoneDust).
     */
    protected ResynthTileEntity(Material material, SoundType sound, String name) {
        super(material, sound, name);
    }

    /**
     * Gets the tile entity at the given location
     * cast as tile entity with generic type.
     *
     * @param world the world the tile entity is in.
     * @param pos the location in the world.
     * @return the cast tile entity.
     */
    @SuppressWarnings("unchecked")
    public TE getTileEntity(IBlockReader world, BlockPos pos) {
        return (TE)world.getTileEntity(pos);
    }

    /**
     * Always returns {@code true} to indicate
     * the block class implementing this class
     * has a tile entity class.
     *
     * @param state the blocks state in the world.
     * @return {@code true}
     */
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    /**
     * @return this blocks tile entity class.
     */
    public abstract Class<TE> getTileEntityClass();

    /**
     * Constructs and returns the tile entity object instance
     * for this block.
     *
     * @param world the world the block is in.
     * @param state the block state of the block.
     * @return the newly constructed tile entity.
     */
    //@Override
    public abstract TE createTileEntity(World world, IBlockState state);
}
