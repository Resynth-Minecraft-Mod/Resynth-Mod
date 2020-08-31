/*
 * Copyright 2018-2020 Ki11er_wolf
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

import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.SeedPodConfig;
import com.ki11erwolf.resynth.plant.set.PlantSet;
import com.ki11erwolf.resynth.plant.set.PublicPlantSetRegistry;
import com.ki11erwolf.resynth.util.MathUtil;
import com.ki11erwolf.resynth.util.MinecraftUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;

//import net.minecraft.util.math.Vec3d;

/**
 * Mystical Seed Pod. Can be configured to drops Biochemical seeds when broken.
 * Useful for players in peaceful mode and skyblock.
 */
@SuppressWarnings("deprecation")
public class BlockSeedPod extends ResynthBlock<BlockSeedPod> implements IPlantable {

    /**
     * The configuration settings for this block.
     */
    private static final SeedPodConfig CONFIG = ResynthConfig.GENERAL_CONFIG.getCategory(SeedPodConfig.class);

    /**
     * The bounding/hit box (shape) for this block.
     */
    private static final VoxelShape SEED_POD_SHAPE = Block.makeCuboidShape(
             3.0D, 0.0D, 3.0D,
            14.0D, 15.0D, 14.0D
    );

    /**
     * Default constructor.
     */
    BlockSeedPod(String name) {
        super(Properties.create(Material.PLANTS).sound(SoundType.PLANT).notSolid(), name);
    }

    // **************
    // Visual & Shape
    // **************

    /**
     * {@inheritDoc}
     *
     * @return {@link Block.OffsetType#XZ}.
     */
    @Override
    public Block.OffsetType getOffsetType() {
        return Block.OffsetType.XZ;
    }

    /**
     * This does not affect the collision box of the plant as it's
     * set by {@link #getCollisionShape(BlockState, IBlockReader, BlockPos, ISelectionContext)}
     *
     * @return the bounding box/hit box of this plant. {@link #SEED_POD_SHAPE}
     * with the offset.
     */
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Vector3d vector3d = state.getOffset(worldIn, pos);
        return SEED_POD_SHAPE.withOffset(vector3d.x, vector3d.y, vector3d.z);
    }

    /**
     * @return {@code 0}.
     */
    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0;
    }

    /**
     * @return {@link VoxelShapes#empty()}.
     */
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }

    // ***************
    // Plant Interface
    // ***************

    /**
     * @return the default state of this block or it's
     * state in the world at the given position.
     */
    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() != this)
            return getDefaultState();

        return state;
    }

    // *********
    // Placement
    // *********

    /**
     * Used to check if the given block is valid ground
     * (i.e. if this plant block can be placed on it).
     *
     * @param state the block to check if it's valid ground.
     * @return {@code true} if the given block is valid ground
     * (this plant can be placed on it).
     */
    private boolean isValidGround(BlockState state) {
        Block block = state.getBlock();
        return  block == Blocks.GRASS_BLOCK || block == Blocks.DIRT
                || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code true} if the block underneath this block is valid ground
     * ({@link #isValidGround(BlockState)}).
     */
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.down();
        //Forge: This function is called during world gen and placement,
        //before this block is set, so if we are not 'here' then assume it's the pre-check.
        if (state.getBlock() == this)
            return worldIn.getBlockState(blockpos).canSustainPlant(worldIn, blockpos, Direction.UP, this);

        return this.isValidGround(worldIn.getBlockState(blockpos));
    }

    /**
     * {@inheritDoc}
     *
     * Checks if this plant block is still on {@link #isValidGround(BlockState)}
     * after a neighbour block changes.
     *
     * @return the updated state of this block. Can be air if the plant
     * is no longer on valid ground.
     */
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState,
                                           IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState()
                : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    // ***********
    // Plant Logic
    // ***********

    /**
     * {@inheritDoc}
     *
     * Handles what happens when the user breaks this block.
     * Will either drop the block itself or a the seeds item
     * of a random biochemical plant set depending on config.
     */
    @Override
    public void spawnAdditionalDrops(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack) {
        if (!CONFIG.areDropsEnabled()) {
            MinecraftUtil.spawnItemStackInWorld(new ItemStack(this), world, pos);
            return;
        }

        PlantSet<?>[] plantSets = PublicPlantSetRegistry.getSets(PublicPlantSetRegistry.SetType.BIOCHEMICAL);
        PlantSet<?> randomSet = plantSets[MathUtil.getRandomIntegerInRange(0, plantSets.length - 1)];
        MinecraftUtil.spawnItemInWorld(randomSet.getSeedsItem(), world, pos);
    }

    /**
     * {@inheritDoc}
     * Handles what happens when the users
     * picks this block (middle-click).
     *
     * @return the item to give the player
     * when the pick the block.
     */
    @Override
    @Nonnull
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(this);
    }
}
