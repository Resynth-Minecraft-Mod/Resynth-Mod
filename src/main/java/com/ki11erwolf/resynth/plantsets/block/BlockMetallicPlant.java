package com.ki11erwolf.resynth.plantsets.block;

import com.ki11erwolf.resynth.plantsets.set.PlantSetProperties;
import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * The Metallic plant set type - used to grow resources that
 * are obtained by smelting ores. Specific plant blocks
 * are usually referred to as plant set types, while their instances
 * (iron vs gold) are referred to as the plant type.
 * <p/>
 * This class defines the look and feel (how the plant looks and behaves)
 * of the Metallic plant type. This class handles little to no logic
 * other than the way in which the plant type grows.
 */
public abstract class BlockMetallicPlant extends BlockPlant<BlockMetallicPlant> {

    /**
     * The direction the plant is facing when its grown
     * produce.
     */
    private static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;

    /**
     * @param plantTypeName the name of the plant set type (e.g. crystalline).
     * @param plantName the name of the plant type (e.g. diamond).
     * @param properties the properties (e.g. growth chances) of the
     *                   plant type.
     */
    @SuppressWarnings("WeakerAccess")//Lies
    public BlockMetallicPlant(String plantTypeName, String plantName, PlantSetProperties properties) {
        super(plantTypeName, plantName, properties);
    }

    // *************************
    // Abstract Method Overrides
    // *************************

    /**
     * {@inheritDoc}
     */
    @Override
    IntegerProperty getGrowthProperty() {
        return IntegerProperty.create("growth_stage", 0, 8);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    VoxelShape[] getShapeByAge() {
        return new VoxelShape[]{
                Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 2.0D, 10.0D),  //1
                Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 4.0D, 10.0D),  //2
                Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 6.0D, 10.0D),  //3
                Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 8.0D, 10.0D),  //4
                Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D), //5
                Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 12.0D, 10.0D), //6
                Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 14.0D, 10.0D), //7
                Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D), //8
                Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 11.0D, 10.0D)  //9
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    int getMaxAge() {
        return 8;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean dropsProduceWhenGrown() {
        return false;
    }

    // ****************************
    // Base Implementation Override
    // ****************************

    /**
     * {@inheritDoc}.
     *
     * Registers the {@link #FACING} property.
     */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    /**
     * {@inheritDoc}.
     *
     * Handles updating the plant growth stage
     * when its produce is broken.
     */
    @Deprecated
    @SuppressWarnings("deprecation")
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        if(blockIn == Block.getBlockFromItem(getProduce().getItem())){
            if(worldIn.getBlockState(pos).get(getGrowthProperty()) == this.getMaxAge())
                //Reset the plant if the player breaks its produce.
                worldIn.setBlockState(
                        pos, this.getDefaultState().with(this.getGrowthProperty(), this.getMaxAge() - 1)
                );
        }
    }

    // ************
    // Growth Logic
    // ************

    /**
     * {@inheritDoc}
     * <p/>
     * Handles growing the plant growth property and placing
     * its produce when it's already fully grown.
     */
    @Override
    void growPlant(World world, IBlockState state, BlockPos pos, int increase) {
        int growth = increase + getGrowthStage(state);

        if(growth >= getMaxAge()){
            //Produce
            growth = getMaxAge();
            EnumFacing facing = placeProduce(world, pos);
            if(facing != null)
                world.setBlockState(pos, this.getDefaultState()
                        .with(this.getGrowthProperty(), growth)
                        .with(BlockHorizontal.HORIZONTAL_FACING, facing), 2);
        } else {
            //Grow
            world.setBlockState(pos, this.getDefaultState().with(this.getGrowthProperty(), growth), 2);
        }
    }

    /**
     * Attempts to place the plants produce in the world
     * on either of the 4 sides.
     *
     * @return the direction the produce was placed, {@code null}
     * if the produce could not be placed.
     */
    @Nullable
    private EnumFacing placeProduce(World world, BlockPos pos){
        //Random direction.
        int random = MathUtil.getRandomIntegerInRange(0, 4);

        for(int i = 0; i < 4; i++){
            //For each side
            BlockPos pos1 = null;
            IBlockState block = null;
            EnumFacing facing = null;

            if(random > 3)
                random = 0;

            switch (random){
                case 0:
                    pos1 = pos.north();
                    facing = EnumFacing.NORTH;
                    block = world.getBlockState(pos1);
                    break;
                case 1:
                    pos1 = pos.south();
                    facing = EnumFacing.SOUTH;
                    block = world.getBlockState(pos1);
                    break;
                case 2:
                    pos1 = pos.east();
                    facing = EnumFacing.EAST;
                    block = world.getBlockState(pos1);
                    break;
                case 3:
                    pos1 = pos.west();
                    facing = EnumFacing.WEST;
                    block = world.getBlockState(pos1);
                    break;
            }

            if(block == null)
                return null;

            //Place produce block.
            if(block.getBlock() == Blocks.AIR && world.getBlockState(pos1.down()).isFullCube()){
                if(world.setBlockState(pos1, Block.getBlockFromItem(getProduce().getItem()).getDefaultState()))
                    return facing;
                else return null;
            }

            random++;
        }

        //Could not place on any side.
        return null;
    }
}
