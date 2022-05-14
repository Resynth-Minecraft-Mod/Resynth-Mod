/*
 * Copyright 2018-2021 Ki11er_wolf
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
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

/**
 * The base class for all blocks that provide a tile entity.
 * This class extends the functionality of the base class (ResynthBlock)
 * for tile entities.
 *
 * @param <T> the tile entity class.
 */
public abstract class ResynthTileEntity <T extends TileEntity> extends ResynthBlock<ResynthTileEntity<?>> {

    /**
     * Basic constructor for all Resynth tile entities.
     *
     * @param properties the properties of this specific block.
     * @param name the name of the block (e.g. redstoneDust).
     */
    protected ResynthTileEntity(Properties properties, String name) {
        super(properties, name);
    }

    /**
     * Gets the tile entity at the given location
     * cast as tile entity with generic type.
     *
     * @param world the world the tile entity is in.
     * @param pos the location in the world.
     * @return the cast tile entity.
     */
    @SuppressWarnings({"unchecked"})
    protected T getBlockEntity(IBlockReader world, BlockPos pos) {
        return (T)world.getBlockEntity(pos);
    }

    /**
     * Always returns {@code true} to indicate
     * the block class implementing this class
     * has a tile entity class.
     *
     * @return {@code true}
     */
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    // ****************
    // Abstract Methods
    // ****************

    /**
     * @return this blocks tile entity class.
     */
    @SuppressWarnings({"unused", "RedundantSuppression"})
    public abstract Class<T> getBlockEntityClass();

    /**
     * Constructs and returns the tile entity object instance
     * for this block.
     *
     * @param world the world the block is in.
     * @param state the block state of the block.
     * @return the newly constructed tile entity.
     */
    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);
}
