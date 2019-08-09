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
package com.ki11erwolf.resynth.plant.block;

import com.ki11erwolf.resynth.igtooltip.HwylaDataProvider;

import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.block.Block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

/**
 * The block class for plants that
 * ores that need to be smelted (e.g.
 * gold and iron).
 */
public abstract class BlockPlantMetallic extends BlockPlantBase implements HwylaDataProvider {

    /**
     * Growth stage (age) property of the plant type.
     */
    public static final IntegerProperty GROWTH_STAGE = IntegerProperty.create(
            "age", 0, 7);

    /**
     * The direction the block is facing. Used when it's connected
     * to its produce (i.e. flowering).
     */
    public static final DirectionProperty FACING
            = DirectionProperty.create("facing", EnumFacing.Plane.HORIZONTAL);

    /**
     * List of bounding boxes for the plants growth stages.
     */
    protected static final VoxelShape[] METALLIC_STEM_AABB = new VoxelShape[] {
            Block.makeCuboidShape(0.375D, 0.0D, 0.375D, 0.625D, 0.125D, 0.625D), //Age=0
            Block.makeCuboidShape(0.375D, 0.0D, 0.375D, 0.625D, 0.25D, 0.625D),  //Age=1
            Block.makeCuboidShape(0.375D, 0.0D, 0.375D, 0.625D, 0.375D, 0.625D), //Age=2
            Block.makeCuboidShape(0.375D, 0.0D, 0.375D, 0.625D, 0.5D, 0.625D),   //Age=3
            Block.makeCuboidShape(0.375D, 0.0D, 0.375D, 0.625D, 0.625D, 0.625D), //Age=4
            Block.makeCuboidShape(0.375D, 0.0D, 0.375D, 0.625D, 0.75D, 0.625D),  //Age=5
            Block.makeCuboidShape(0.375D, 0.0D, 0.375D, 0.625D, 0.875D, 0.625D), //Age=6
            Block.makeCuboidShape(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D)    //Age=7
    };

    /**
     * The block the plant places as its produce.
     */
    private final Block produce;

    /**
     * Constructs a new metallic plant block type.
     *
     * @param produce the block the plant places.
     * @param name the name of the block.
     */
    public BlockPlantMetallic(Block produce, String name) {
        super(name);
        this.setDefaultState(
                this.getStateContainer().getBaseState().with(GROWTH_STAGE, 0)
                        .with(FACING, EnumFacing.UP)
        );
        this.produce = produce;
    }

    /**
     * Checks if the block hasn't reached its max growth stage (7).
     *
     * @param worldIn -
     * @param pos -
     * @param state -
     * @param isClient -
     * @return true if the block isn't at its max growth stage.
     */
    @Override
    public boolean canGrow(IBlockReader worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return state.get(GROWTH_STAGE) != 7;
    }

    /**
     * Grows the plant by one growth stage. This does
     * not place produce.
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

//    /**
//     * The correct bounding box for the plants growth stage.
//     *
//     * @param state -
//     * @param source -
//     * @param pos -
//     * @return the bounding box for the plant
//     * with the given growth stage.
//     */
//    @Override
//    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
//        return METALLIC_STEM_AABB[state.getValue(GROWTH_STAGE)];
//    }

//    /**
//     * Gets the meta value from the blocks state.
//     *
//     * @param state -
//     * @return the associated meta value.
//     */
//    @Override
//    public int getMetaFromState(IBlockState state){
//        return state.get(GROWTH_STAGE);
//    }
//
//    /**
//     * @return a newly constructed a new block state container
//     * with the given block properties (growth stage and facing).
//     */
//    @Override
//    protected BlockStateContainer createBlockState(){
//        return new BlockStateContainer(this, GROWTH_STAGE, FACING);
//    }
//
//    /**
//     * Gets the block state with the given meta value.
//     *
//     * @param meta -
//     * @return the block state from the given meta value.
//     */
//    @Override
//    @SuppressWarnings("deprecation")
//    public IBlockState getStateFromMeta(int meta){
//        return this.getDefaultState().withProperty(GROWTH_STAGE, meta);
//    }

    /**
     * {@inheritDoc}
     * Returns the item stack the player should get
     * when harvesting/picking the block.
     *
     * @param worldIn
     * @param pos
     * @param state
     * @return the plants seed type.
     */
    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, IBlockState state){
        Item item = this.getSeedItem();
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }

    /**
     * {@inheritDoc}
     *
     * @param state
     * @param worldIn
     * @param fortune
     * @return nothing (air).
     */
    @Override
    public Item getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune){
        return Items.AIR;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     *     Adds the seed item to the list of drops.
     * </p>
     *
     * @param drops
     * @param world
     * @param pos
     * @param state
     * @param fortune
     */
    @Override
    public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune){
        Item item = this.getSeedItem();

        if (item != null){
            drops.add(new ItemStack(item));
        }
    }

    /**
     * {@inheritDoc}
     *
     * Gets the full state of block with meta, growth stage
     * and facing property.
     *
     * @param state
     * @param worldIn
     * @param pos
     *
     * @return the full state of the block.
     */
    //@Override//TODO: Something
    public IBlockState getActualState(IBlockState state, IBlockReader worldIn, BlockPos pos){
        int i =  state.get(GROWTH_STAGE);
        state = state.with(FACING, EnumFacing.UP);

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL){
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this.produce && i == 7){
                state = state.with(FACING, enumfacing);
                break;
            }
        }

        return state;
    }

    /**
     * {@inheritDoc}
     *
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
        int i = state.get(GROWTH_STAGE);

        if (i < 7) {
            //Grow
            IBlockState newState = state.with(GROWTH_STAGE, i + 1);
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
        net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
    }

    /**
     * Grows the plant stem. This increases it's growth stage
     * by one and updates the block state and bounding box.
     *
     * @param worldIn -
     * @param pos -
     * @param state -
     */
    private void growStem(World worldIn, BlockPos pos, IBlockState state){
        int i = state.get(GROWTH_STAGE) + MathUtil.getRandomIntegerInRange(5, 5);
        worldIn.setBlockState(pos, state.with(GROWTH_STAGE, Math.min(7, i)), 2);
    }

    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return METALLIC_STEM_AABB[state.get(GROWTH_STAGE)];
    }

//    /**
//     * {@inheritDoc}
//     *
//     * Provides the plants growth stage to the Hwyla tooltip.
//     *
//     * @param itemStack the item stack returned by the block.
//     * @param tooltip list of strings to pass to Hwyla to show in the tooltip.
//     * @param accessor accessor used to get data from the block such as NBT.
//     * @param config current Hwyla configuration
//     */
//    @Override
//    public void onHwylaBodyRequest(ItemStack itemStack, List<String> tooltip,
//                                   IWailaDataAccessor accessor, IWailaConfigHandler config){
//
//        tooltip.add(
//                TextFormatting.GREEN
//                + "Growth Stage: "
//                + getStageFromBlockMeta(accessor.getMetadata())
//        );
//    }
//
//    /**
//     * {@inheritDoc}
//     *
//     * Changes the plant name in the Hwyla tooltip.
//     *
//     * @param itemStack the block ItemStack.
//     * @param tooltip the head tooltip array (containing the block name).
//     * @param accessor Hwyla data provider.
//     * @param config current Hwyla config settings.
//     */
//    @Override
//    public void onHwylaHeadRequest(ItemStack itemStack, List<String> tooltip,
//                                   IWailaDataAccessor accessor, IWailaConfigHandler config){
//        String blockTitle = tooltip.get(0);
//        if(blockTitle.contains("Seeds")){
//            tooltip.clear();
//            tooltip.add(0, TextFormatting.GOLD
//                    + blockTitle.replace("Seeds", "Plant"));
//            tooltip.add(TextFormatting.RED + "Drops: " + blockTitle);
//        }
//    }

    /**
     * Used to get the seed item this plant instance
     * will actually drop. This is specified
     * in the class creating this one.
     *
     * @return the seed item used to plant this block.
     */
    protected abstract Item getSeedItem();
}
