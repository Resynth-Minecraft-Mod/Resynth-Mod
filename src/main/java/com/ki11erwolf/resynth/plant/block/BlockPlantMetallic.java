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
package com.ki11erwolf.resynth.plant.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * The block class for plants that
 * produce metallic (gold and iron) ore drops.
 */
public abstract class BlockPlantMetallic extends BlockPlantBase {

    /**
     * The age property of the plant. I.e. how big it is.
     */
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);

    /**
     * The direction the block is facing. Used when it's connected
     * to another block.
     */
    public static final PropertyDirection FACING = BlockTorch.FACING;

    /**
     * The block the plant places.
     */
    private final Block produce;

    /**
     * List of aligned axises the plant
     * can have.
     */
    protected static final AxisAlignedBB[] MC_STEM_AABB = new AxisAlignedBB[] {
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.125D, 0.625D), //Age=0
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.25D, 0.625D),  //Age=1
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.375D, 0.625D), //Age=2
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.5D, 0.625D),   //Age=3
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.625D, 0.625D), //Age=4
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.75D, 0.625D),  //Age=5
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.875D, 0.625D), //Age=6
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D)    //Age=7
    };

    /**
     * Constructs a new metallic plant block.
     *
     * @param produce the block the plant places.
     * @param name the name of the block.
     */
    public BlockPlantMetallic(Block produce, String name) {
        super(name);
        this.setDefaultState(
                this.blockState.getBaseState().withProperty(AGE, 0).withProperty(FACING, EnumFacing.UP)
        );
        this.produce = produce;
    }

    /**
     * Allows the block to grow
     * if it hasn't reached its max age (height)
     *
     * @param worldIn -
     * @param pos -
     * @param state -
     * @param isClient -
     * @return true if the block can grow more.
     */
    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return state.getValue(AGE) != 7;
    }

    /**
     * Grows the block by one.
     *
     * @param worldIn -
     * @param rand -
     * @param pos -
     * @param state -
     */
    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        this.growStem(worldIn, pos, state);
    }

    /**
     * The correct bounding box for the plant
     * in its current state.
     *
     * @param state -
     * @param source -
     * @param pos -
     * @return the bounding box for the plant
     * with the given age(height)
     */
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        return MC_STEM_AABB[state.getValue(AGE)];
    }

    /**
     * Ges the meta value from the blocks state.
     *
     * @param state -
     * @return the associated meta value.
     */
    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(AGE);
    }

    /**
     * @return a newly constructed a new block state container
     * with the given block properties (age and facing).
     */
    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, AGE, FACING);
    }

    /**
     * Gets the block state with the given meta value.
     *
     * @param meta -
     * @return the block state with the given meta value.
     */
    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta){
        return this.getDefaultState().withProperty(AGE, meta);
    }

    /**
     * {@inheritDoc}
     *
     * @param worldIn
     * @param pos
     * @param state
     * @return the seed item for this plant.
     */
    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state){
        Item item = this.getSeedItem();
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }

    /**
     * {@inheritDoc}
     *
     * @param state
     * @param rand
     * @param fortune
     * @return nothing.
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return Items.AIR;
    }

    /**
     * {@inheritDoc}
     * <p>
     *     Adds the seed item to the list.
     * </p>
     *
     * @param drops
     * @param world
     * @param pos
     * @param state
     * @param fortune
     */
    @Override
    public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops,
                         IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
        Item item = this.getSeedItem();

        if (item != null){
            drops.add(new ItemStack(item));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param worldIn
     * @param pos
     * @param state
     * @param chance
     * @param fortune
     */
    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
    }

    /**
     * {@inheritDoc}
     *
     * @param state
     * @param worldIn
     * @param pos
     * @return
     */
    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos){
        int i =  state.getValue(AGE);
        state = state.withProperty(FACING, EnumFacing.UP);

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL){
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this.produce && i == 7){
                state = state.withProperty(FACING, enumfacing);
                break;
            }
        }

        return state;
    }

    /**
     * {@inheritDoc}
     * <p>
     *     Grows the plant or places the produce block
     *     if a growth tick is approved.
     * </p>
     *
     * @param worldIn -
     * @param pos -
     * @param state -
     * @param rand -
     */
    @Override
    protected void onGrowApproved(World worldIn, BlockPos pos, IBlockState state, Random rand){
        int i = state.getValue(AGE);

        if (i < 7) {
            //Grow
            IBlockState newState = state.withProperty(AGE, i + 1);
            worldIn.setBlockState(pos, newState, 2);
        } else {
            //Place ore block
            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL){
                if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this.produce){
                    return;
                }
            }

            pos = pos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));
            IBlockState soil = worldIn.getBlockState(pos.down());
            Block block = soil.getBlock();

            if (worldIn.isAirBlock(pos) && (block.canSustainPlant(soil, worldIn, pos.down(), EnumFacing.UP,
                    this) || block == Blocks.DIRT || block == Blocks.GRASS)){
                worldIn.setBlockState(pos, this.produce.getDefaultState());
            }
        }
        net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
    }

    /**
     * Grows the plant. This increases it's age
     * by one and updates the texture and bounding box.
     *
     * @param worldIn -
     * @param pos -
     * @param state -
     */
    private void growStem(World worldIn, BlockPos pos, IBlockState state){
        int i = state.getValue(AGE) + MathHelper.getInt(worldIn.rand, 2, 5);
        worldIn.setBlockState(pos, state.withProperty(AGE, Math.min(7, i)), 2);
    }

    /**
     * @return the seed item used to plant this block.
     */
    protected abstract Item getSeedItem();
}
