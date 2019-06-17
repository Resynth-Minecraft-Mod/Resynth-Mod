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
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

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
     *
     * This isn't used.
     */
    protected static final AxisAlignedBB DEFAULT_AABB = new AxisAlignedBB(
            0.30000001192092896D, 0.0D, 0.30000001192092896D,
            0.699999988079071D, 0.6000000238418579D, 0.699999988079071D);

    private static final VoxelShape SHAPE = Block.makeCuboidShape(
            0.30000001192092896D, 0.0D, 0.30000001192092896D,
            0.699999988079071D, 0.6000000238418579D, 0.699999988079071D
    );

    /**
     * Default constructor.
     *
     * @param name the name the block is identified by.
     */
    protected BlockPlantBase(String name) {
        super(
                Properties.create(Material.PLANTS)
                        .sound(SoundType.PLANT).needsRandomTick().hardnessAndResistance(0.0F),
                name, PLANT_PREFIX
        );
        //this.setTickRandomly(true);
        //Don't make available in creative.
        //this.setCreativeTab(null);
        //this.setHardness(0.0F);
        //this.setSoundType(SoundType.PLANT);
    }

    /**
     * @param world -
     * @param pos -
     * @return {@link EnumPlantType#Crop}
     */
    @Override
    public EnumPlantType getPlantType(IBlockReader world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    /**
     * {@inheritDoc}
     * Checks if the plant can be grown with bonemeal by looking
     * at the config settings.
     *
     * @param worldIn
     * @param rand
     * @param pos
     * @param state
     * @return dependent on configuration.
     */
    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state){
        return /*ResynthConfig.PLANTS_GENERAL.canBonemeal TODO: Config && */ this.canBonemeal();
    }

    /**
     * {@inheritDoc}
     *
     * @param world
     * @param pos
     * @return the default state of this block.
     */
    @Override
    public IBlockState getPlant(IBlockReader world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() != this)
            return getDefaultState();

        return state;
    }

    /**
     * {@inheritDoc}
     *
     * @return CUTOUT
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer(){
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * {@inheritDoc}
     *
     * @param worldIn
     * @param state
     * @param pos
     * @param face
     * @return UNDEFINED
     */
    @Override
    @SuppressWarnings("deprecation")
    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state,
                                            BlockPos pos, EnumFacing face){
        return BlockFaceShape.UNDEFINED;
    }

    /**
     * {@inheritDoc}
     *
     * @param state
     * @return false
     */
    //@Override //TODO: Something
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param state
     * @return false
     */
    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state){
        return false;
    }

//    /**
//     * {@inheritDoc}
//     *
//     * @param blockState
//     * @param worldIn
//     * @param pos
//     * @return no collision box.
//     */
//    @Override
//    @Nullable
//    @SuppressWarnings("deprecation")
//    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos){
//        return NULL_AABB;
//    }

    @Override
    @Deprecated //No alternative yet
    public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return VoxelShapes.empty();//Can walk through.
    }

//    /**
//     * {@inheritDoc}
//     *
//     * @param state
//     * @param source
//     * @param pos
//     * @return default aligned axis for all plants. Not used.
//     */
//    @Override
//    @SuppressWarnings("deprecation")
//    public AxisAlignedBB getBoundingBox(){
//        return DEFAULT_AABB;
//    }

    @Override
    @Deprecated //No alternative yet
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return SHAPE;
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

    //TODO: Fix this later
//    /**
//     * Spawns particles around the plant.
//     */
//    //@SideOnly(Side.CLIENT)
//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
//        if(!ResynthConfig.PLANTS_GENERAL.enableSmokingPlants)
//            return;
//
//        IBlockState iblockstate = world.getBlockState(pos);
//        int amount = 3;
//
//        if(!MathUtil.chance(2.0F))
//            return;
//
//        if (iblockstate.getMaterial() != Material.AIR) {
//            for (int i = 0; i < amount; ++i){
//                double d0 = rand.nextGaussian() * 0.02D;
//                double d1 = rand.nextGaussian() * 0.02D;
//                double d2 = rand.nextGaussian() * 0.02D;
//                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
//                        (double)((float)pos.getX() + rand.nextFloat()),
//                        (double)pos.getY() + (double)rand.nextFloat()
//                                * iblockstate.getBoundingBox(world, pos).maxY,
//                        (double)((float)pos.getZ() + rand.nextFloat()), d0, d1, d2);
//            }
//        }
//        else {
//            for (int i1 = 0; i1 < amount; ++i1) {
//                double d0 = rand.nextGaussian() * 0.02D;
//                double d1 = rand.nextGaussian() * 0.02D;
//                double d2 = rand.nextGaussian() * 0.02D;
//                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
//                        (double)((float)pos.getX() + rand.nextFloat()),
//                        (double)pos.getY() + (double)rand.nextFloat() * 1.0f,
//                        (double)((float)pos.getZ() + rand.nextFloat()), d0, d1, d2);
//            }
//        }
//    }

//    /**
//     * {@inheritDoc}
//     *
//     * @param worldIn
//     * @param pos
//     * @return true if the block underneath is mineral soil.
//     */
//    @Override
//    public boolean canPlaceBlockAt(World worldIn, BlockPos pos){
//        //Does NOT seem to work right...
//        return (worldIn.getBlockState(pos.down()).getBlock() == getSoilBlock());
//    }

    @Override
    @Deprecated //
    public boolean isValidPosition(IBlockState state, IWorldReaderBase worldIn, BlockPos pos) {
        return (worldIn.getLightSubtracted(pos, 0) >= 8 || worldIn.canSeeSky(pos))
                && (worldIn.getBlockState(pos.down()).getBlock() == getSoilBlock());
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
     * Removes this block from the world if the block below cannot
     * sustain this plant.
     *
     * @param worldIn -
     * @param pos -
     * @param state -
     */
    protected void dropIfShould(World worldIn, BlockPos pos, IBlockState state){
        if(!canStay(worldIn, pos)){
            this.dropBlockAsItemWithChance(state, worldIn, pos, 1.0F, 0);
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
     * taking into account the given {@link #_getGrowthChance()}
     * and mineral content percentage of the
     * soil block below.
     *
     * This method handles growth chances.
     *
     * @param world -
     * @param pos -
     * @return true if the block should grow.
     */
    protected boolean shouldGrow(World world, BlockPos pos){
        float randomChance = MathUtil.getRandomIntegerInRange(0, 100);
        float chance = getSoilMineralContent(world, pos);

        if(!(chance > randomChance))
            return false;

        return MathUtil.chance(_getGrowthChance());
    }

    /**
     * Gets the mineral content percentage
     * of the soil block below.
     *
     * @param world -
     * @param pos -
     * @return the mineral content percentage of the block below.
     */
    protected float getSoilMineralContent(World world, BlockPos pos){
        if(world.getTileEntity(pos.down()).getBlockState().getBlock() != ResynthBlocks.BLOCK_MINERAL_SOIL)
            return 0F;

        TileEntityMineralSoil tEntity = (TileEntityMineralSoil)world.getTileEntity(pos.down());
        return tEntity.getMineralPercentage();
    }

//    /**
//     * Calls abstract grow method
//     * and ensures the block can stay.
//     *
//     * This method handles growth rules.
//     *
//     * <p>
//     *     The basic growth rules are light levels
//     *     above 9 and if the area loaded.
//     *
//     *     The rest of the growth is determined by
//     *     the specific plant instance and if the
//     *     {@link #shouldGrow(World, BlockPos)}
//     *     method gives the "go ahead".
//     * </p>
//     *
//     * @param worldIn -
//     * @param pos -
//     * @param state -
//     * @param rand -
//     */
//    @Override
//    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
//        this.dropIfShould(worldIn, pos, state);
//
//        if (!worldIn.isAreaLoaded(pos, 1)) return;
//        if (worldIn.getLightFromNeighbors(pos.up()) < 9)return;
//
//        boolean grow = this.shouldGrow(worldIn, pos);
//        if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, grow)) {
//            if(ResynthConfig.PLANTS_GENERAL.enableGrowth)
//                onGrowApproved(worldIn, pos, state, rand);
//        }
//    }

    @Override
    @Deprecated //Why even?
    public void tick(IBlockState state, World worldIn, BlockPos pos, Random random) {
        this.dropIfShould(worldIn, pos, state);

        if (!worldIn.isAreaLoaded(pos, 1)) return;
        if (worldIn.getLight(pos.up()) < 9)return;

        boolean grow = this.shouldGrow(worldIn, pos);
        if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, grow)) {
            if(/*ResynthConfig.PLANTS_GENERAL.enableGrowth TODO: Config*/ true)
                onGrowApproved(worldIn, pos, state, random);
        }
    }

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

    /**
     * Called when a random update tick decides the
     * block can grow.
     *
     * This is the method that will
     * actually handle growing the plant. This
     * method does not handle growth rules and
     * chances.
     *
     * @param world -
     * @param pos -
     * @param state -
     * @param random -
     */
    protected abstract void onGrowApproved(World world, BlockPos pos, IBlockState state, Random random);

    /**
     * This is the internal growth chance method
     * used by the plants Block classes. The
     * "getGrowthChance()" method without
     * the underscore is the one used by the
     * actual plant set creator classes.
     *
     * @return how long the plant takes to grow
     * in general.
     */
    protected abstract float _getGrowthChance();

    /**
     * @return true if bonemeal can be used
     * on the plant when bonemeal on plants is enabled.
     */
    protected abstract boolean canBonemeal();
}
