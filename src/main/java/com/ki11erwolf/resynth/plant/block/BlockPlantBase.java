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

import java.util.Random;

public abstract class BlockPlantBase extends ResynthBlock implements IGrowable, IPlantable {

    protected static final String PLANT_PREFIX = "plant_";

    private static final VoxelShape SHAPE = Block.makeCuboidShape(
            0.30000001192092896D, 0.0D, 0.30000001192092896D,
            0.699999988079071D, 0.6000000238418579D, 0.699999988079071D
    );

    protected BlockPlantBase(String name) {
        super(
                Properties.create(Material.PLANTS)
                        .sound(SoundType.PLANT)
                        .needsRandomTick()
                        .hardnessAndResistance(0.0F),
                PLANT_PREFIX + name
        );
        //this.setTickRandomly(true);
        //Don't make available in creative.
        //this.setCreativeTab(null);
        //this.setHardness(0.0F);
        //this.setSoundType(SoundType.PLANT);
    }

    @Override
    public EnumPlantType getPlantType(IBlockReader world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state){
        return /*ResynthConfig.PLANTS_GENERAL.canBonemeal TODO: Config && */ this.canBonemeal();
    }

    @Override
    public IBlockState getPlant(IBlockReader world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() != this)
            return getDefaultState();

        return state;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer(){
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state,
                                            BlockPos pos, EnumFacing face){
        return BlockFaceShape.UNDEFINED;
    }

    //@Override //TODO: Something
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state){
        return false;
    }

    @Override
    @Deprecated //No alternative yet
    public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return VoxelShapes.empty();//Can walk through.
    }

    @Override
    @Deprecated //No alternative yet
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return SHAPE;
    }

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
    @Deprecated
    public boolean isValidPosition(IBlockState state, IWorldReaderBase worldIn, BlockPos pos) {
        return (worldIn.getLightSubtracted(pos, 0) >= 8 || worldIn.canSeeSky(pos))
                && (worldIn.getBlockState(pos.down()).getBlock() == getSoilBlock());
    }

    protected boolean canSustainPlant(IBlockState state){
        return state.getBlock() == getSoilBlock();
    }

    protected Block getSoilBlock(){
        return ResynthBlocks.BLOCK_MINERAL_SOIL;
    }

    protected void dropIfShould(World worldIn, BlockPos pos, IBlockState state){
        if(!canStay(worldIn, pos)){
            this.dropBlockAsItemWithChance(state, worldIn, pos, 1.0F, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    protected boolean canStay(World worldIn, BlockPos pos){
        return worldIn.getBlockState(pos.down()).getBlock() == getSoilBlock();
    }

    protected boolean shouldGrow(World world, BlockPos pos){
        float randomChance = MathUtil.getRandomIntegerInRange(0, 100);
        float chance = getSoilMineralContent(world, pos);

        if(!(chance > randomChance))
            return false;

        return MathUtil.chance(_getGrowthChance());
    }

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

    protected abstract void onGrowApproved(World world, BlockPos pos, IBlockState state, Random random);

    protected abstract float _getGrowthChance();

    protected abstract boolean canBonemeal();
}
