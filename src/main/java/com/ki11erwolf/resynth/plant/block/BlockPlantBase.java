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

import com.ki11erwolf.resynth.ResynthConfig;
import com.ki11erwolf.resynth.block.ResynthBlock;
import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.block.tileEntity.TileEntityMineralSoil;
import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Base class for all plants.
 *
 * Provides the base implementation for plants
 * as well as the interaction with mineral soil.
 */
public abstract class BlockPlantBase extends ResynthBlock implements IGrowable, IPlantable {

    /**
     * Prefix for all plant blocks.
     */
    protected static final String PLANT_PREFIX = "plant";

    /**
     * The default bounding box for plant blocks.
     */
    protected static final AxisAlignedBB DEFAULT_AABB = new AxisAlignedBB(
            0.30000001192092896D, 0.0D, 0.30000001192092896D,
            0.699999988079071D, 0.6000000238418579D, 0.699999988079071D);

    /**
     * Default constructor.
     *
     * @param name the name the block is identified by.
     */
    protected BlockPlantBase(String name) {
        super(Material.PLANTS, SoundType.PLANT, name, PLANT_PREFIX);
        this.setTickRandomly(true);
        //Don't make available in creative.
        //noinspection RedundantCast
        this.setCreativeTab((CreativeTabs)null);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
    }

    /**
     * @param world -
     * @param pos -
     * @return {@link EnumPlantType#Crop}
     */
    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    /**
     * {@inheritDoc}
     *
     * @param worldIn
     * @param rand
     * @param pos
     * @param state
     * @return dependent on configuration.
     */
    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state){
        return ResynthConfig.PLANTS_GENERAL.canBonemeal && this.canBonemeal();
    }

    /**
     * {@inheritDoc}
     *
     * @param world
     * @param pos
     * @return the default state of this block.
     */
    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() != this)
            return getDefaultState();

        return state;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer(){
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * {@inheritDoc}
     *
     * @param worldIn
     * @param state
     * @param pos
     * @param face
     * @return
     */
    @Override
    @SuppressWarnings("deprecation")
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face){
        return BlockFaceShape.UNDEFINED;
    }

    /**
     * {@inheritDoc}
     *
     * @param state
     * @return
     */
    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param state
     * @return
     */
    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state){
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param blockState
     * @param worldIn
     * @param pos
     * @return no collision box.
     */
    @Override
    @Nullable
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos){
        return NULL_AABB;
    }

    /**
     * {@inheritDoc}
     *
     * @param state
     * @param source
     * @param pos
     * @return default aligned axis for all plants,
     */
    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        return DEFAULT_AABB;
    }

    /**
     * {@inheritDoc}
     * <p>
     *     Makes sure the block drops if it is not on the correct block.
     * </p>
     *
     * @param state
     * @param worldIn
     * @param pos
     * @param blockIn
     * @param fromPos
     */
    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos){
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        this.dropIfShould(worldIn, pos, state);
    }

    /**
     * {@inheritDoc}
     *
     * @param worldIn
     * @param pos
     * @return true if the block underneath is mineral soil.
     */
    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos){
        //Does NOT seem to work right...
        return (worldIn.getBlockState(pos.down()).getBlock() == getSoilBlock());
    }

    /**
     * Checks if the given block can sustain this plant.
     *
     * @param state the instance of the given block
     * @return true if the given block state is mineral soil.
     */
    protected boolean canSustainPlant(IBlockState state){
        return state.getBlock() == getSoilBlock();
    }

    /**
     * @return {@link com.ki11erwolf.resynth.block.BlockMineralSoil}
     */
    protected Block getSoilBlock(){
        return ResynthBlocks.BLOCK_MINERAL_SOIL;
    }

    /**
     * Removes this block from the world is the block below cannot
     * sustain this plant.
     *
     * @param worldIn -
     * @param pos -
     * @param state -
     */
    protected void dropIfShould(World worldIn, BlockPos pos, IBlockState state){
        if(!canStay(worldIn, pos)){
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    /**
     * @param worldIn -
     * @param pos -
     * @return false if the block below cannot sustain this plant.
     */
    protected boolean canStay(World worldIn, BlockPos pos){
        return worldIn.getBlockState(pos.down()).getBlock() == getSoilBlock();
    }

    /**
     * Randomly decides if the block should grow,
     * taking into account the given {@link #getGrowthPeriod()}
     * and mineral content percentage of the
     * soil block below.
     *
     * @param world -
     * @param pos -
     * @return true if the block should grow.
     */
    protected boolean shouldGrow(World world, BlockPos pos){
        float randomChance = MathUtil.getRandomIntegerInRange(0, 100);
        float chance = getGrowthChance(world, pos);

        if(!(chance > randomChance))
            return false;

        return MathUtil.chance(getGrowthPeriod());
    }

    /**
     * Gets the mineral content percentage
     * of the soil block below.
     *
     * @param world -
     * @param pos -
     * @return the mineral content percentage of the block below.
     */
    protected float getGrowthChance(World world, BlockPos pos){
        if(world.getTileEntity(pos.down()).getBlockType() != ResynthBlocks.BLOCK_MINERAL_SOIL)
            return 0F;

        TileEntityMineralSoil tEntity = (TileEntityMineralSoil)world.getTileEntity(pos.down());
        return tEntity.getPercentage();
    }

    /**
     * Calls abstract grow method
     * and ensured the block can stay.
     *
     * @param worldIn -
     * @param pos -
     * @param state -
     * @param rand -
     */
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        this.dropIfShould(worldIn, pos, state);

        if (!worldIn.isAreaLoaded(pos, 1)) return;
        if (worldIn.getLightFromNeighbors(pos.up()) < 9)return;

        boolean grow = this.shouldGrow(worldIn, pos);
        if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, grow)) {
            if(ResynthConfig.PLANTS_GENERAL.enableGrowth)
                onGrowApproved(worldIn, pos, state, rand);
        }
    }

    /**
     * Called when a random tick decides the
     * block can grow.
     *
     * @param world -
     * @param pos -
     * @param state -
     * @param random -
     */
    protected abstract void onGrowApproved(World world, BlockPos pos, IBlockState state, Random random);

    /**
     * @return how long the plant takes to grow
     * in general.
     */
    protected abstract float getGrowthPeriod();

    /**
     * @return true if bonemeal can be used
     * on the plant.
     */
    protected abstract boolean canBonemeal();

    /**
     * Generates a human readable string that gives a
     * plants growth stage from the plant blocks metadata.
     *
     * <b>Only works for plants with 8 growth stages (0-7).</b>
     *
     * @param meta plant block metadata.
     * @return the plants growth stage as a human readable String.
     */
    static String getStageFromBlockMeta(int meta){
        if(meta == 0)
            return "1 of 8 (0%)";

        if(meta == 1)
            return "2 of 8 (14%)";

        if(meta == 2)
            return "3 of 8 (29%)";

        if(meta == 3)
            return "4 of 8 (43%)";

        if(meta == 4)
            return "5 of 8 (57%)";

        if(meta == 5)
            return "6 of 8 (71%)";

        if(meta == 6)
            return "7 of 8 (85%)";

        if(meta == 7)
            return "8 of 8 (100%)";

        return "Unknown";
    }
}
