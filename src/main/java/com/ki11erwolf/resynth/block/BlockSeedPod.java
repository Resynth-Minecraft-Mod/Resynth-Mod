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

import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.SeedPodConfig;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Mystic Seed Pod. Drops Biochemical seeds for players
 * in peaceful mode.
 */
@SuppressWarnings("deprecation")
public class BlockSeedPod extends ResynthBlock implements IPlantable {

    /**
     * The configuration settings for this block.
     */
    @SuppressWarnings("unused")
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
    BlockSeedPod() {
        super(Properties.create(Material.PLANTS).sound(SoundType.PLANT), "seed_pod");
    }

    // **************
    // Visual & Shape
    // **************

    /**
     * {@inheritDoc}
     *
     * @return {@code false}.
     */
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link BlockFaceShape#UNDEFINED}.
     */
    @Override
    @Nonnull
    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state,
                                            BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link Block.EnumOffsetType#XZ}.
     */
    @Override
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XZ;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code false}.
     */
    @Override
    public boolean isSolid(IBlockState state) {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link BlockRenderLayer#CUTOUT}.
     */
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * {@inheritDoc}.
     *
     * This does not affect the collision box of the plant as it's
     * set by {@link #getCollisionShape(IBlockState, IBlockReader, BlockPos)}.
     *
     * @return the bounding box/hit box of this plant. {@link #SEED_POD_SHAPE}
     * with the offset.
     */
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        Vec3d vec3d = state.getOffset(worldIn, pos);
        return SEED_POD_SHAPE.withOffset(vec3d.x, vec3d.y, vec3d.z);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code 0}.
     */
    @Override
    public int getOpacity(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link VoxelShapes#empty()}.
     */
    @Override
    public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return VoxelShapes.empty();
    }

    // ***************
    // Plant Interface
    // ***************

    /**
     * {@inheritDoc}
     *
     * @return the default state of this block or it's
     * state in the world at the given position.
     */
    @Override
    public IBlockState getPlant(IBlockReader world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() != this)
            return getDefaultState();

        return state;
    }

    // *******
    // Tooltip
    // *******

    /**
     * {@inheritDoc}.
     *
     * Sets the tooltip on what this block does/how to use it.
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn) {
        tooltip.add(new TextComponentString(
                "Can drop seeds normally dropped by mobs for players in peaceful mode."));
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
    private boolean isValidGround(IBlockState state) {
        Block block = state.getBlock();
        return  block == Blocks.GRASS_BLOCK
                || block == Blocks.DIRT
                || block == Blocks.COARSE_DIRT
                || block == Blocks.PODZOL;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code true} if the block underneath this block is valid ground
     * ({@link #isValidGround(IBlockState)}).
     */
    @Override
    public boolean isValidPosition(IBlockState state, IWorldReaderBase worldIn, BlockPos pos) {
        BlockPos blockpos = pos.down();
        //Forge: This function is called during world gen and placement,
        //before this block is set, so if we are not 'here' then assume it's the pre-check.
        if (state.getBlock() == this)
            return worldIn.getBlockState(blockpos).canSustainPlant(worldIn, blockpos, EnumFacing.UP, this);

        return this.isValidGround(worldIn.getBlockState(blockpos));
    }

    /**
     * {@inheritDoc}
     *
     * Checks if this plant block is still on {@link #isValidGround(IBlockState)}
     * after a neighbour block changes.
     *
     * @return the updated state of this block. Can be air if the plant
     * is no longer on valid ground.
     */
    @Override
    public IBlockState updatePostPlacement(IBlockState stateIn, EnumFacing facing, IBlockState facingState,
                                           IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !stateIn.isValidPosition(worldIn, currentPos)
                ? Blocks.AIR.getDefaultState()
                : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    // ***********
    // Plant Logic
    // ***********

    //TODO: Fix.
//    @Override
//    @Nonnull
//    public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune) {
//        if (true/*TODO: config*/) {
//            List<PlantSetBiochemical> plants = Arrays.asList(ResynthPlantSets.getBiochemicalPlantSets());
//
//            PlantSetBiochemical plant = ResynthPlantSets.getBiochemicalPlantSets()[0];
//
//            for (int i = 0; i <= 10/*TODO: config: MYSTICAL_SEED_POD.triesPerBreak*/; i++) {
//                plant = plants.get(MathUtil.getRandomIntegerInRange(0, plants.size() - 1));
//                if (MathUtil.chance(plant.getSeedPodDropPercentage())) {
//                    return plant.getSeeds();
//                }
//            }
//
//            if (true/*TODO: config: MYSTICAL_SEED_POD.alwaysDropSeeds*/) {
//                return plant.getSeeds();
//            } else {
//                return Item.getItemFromBlock(Blocks.AIR);
//            }
//        }
//
//        return this;
//    }
}
