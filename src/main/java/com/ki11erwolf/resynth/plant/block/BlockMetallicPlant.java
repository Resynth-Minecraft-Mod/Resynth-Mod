/*
 * Copyright 2018-2022 Ki11er_wolf
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

import com.ki11erwolf.resynth.plant.set.PlantSet;
import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

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
    private static final DirectionProperty FACING = HorizontalBlock.FACING;

    @SuppressWarnings("WeakerAccess")//Lies
    public BlockMetallicPlant(PlantSet<BlockMetallicPlant, Block> parentSet) {
        super(parentSet);
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
                Block.box(6.0D, 0.0D, 6.0D, 10.0D, 2.0D, 10.0D),  //1
                Block.box(6.0D, 0.0D, 6.0D, 10.0D, 4.0D, 10.0D),  //2
                Block.box(6.0D, 0.0D, 6.0D, 10.0D, 6.0D, 10.0D),  //3
                Block.box(6.0D, 0.0D, 6.0D, 10.0D, 8.0D, 10.0D),  //4
                Block.box(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D), //5
                Block.box(6.0D, 0.0D, 6.0D, 10.0D, 12.0D, 10.0D), //6
                Block.box(6.0D, 0.0D, 6.0D, 10.0D, 14.0D, 10.0D), //7
                Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D), //8
                Block.box(6.0D, 0.0D, 6.0D, 10.0D, 11.0D, 10.0D)  //9
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxGrowthStage() {
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
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    /**
     * {@inheritDoc}.
     *
     * Handles updating the plant growth stage
     * when its produce is broken.
     */
    @Deprecated
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if(blockIn == Block.byItem(getProduce().getItem())){
            if(worldIn.getBlockState(pos).getValue(getGrowthProperty()) == this.getMaxGrowthStage())
                //Reset the plant if the player breaks its produce.
                worldIn.setBlockAndUpdate(pos,
                        this.defaultBlockState().setValue(this.getGrowthProperty(), this.getMaxGrowthStage() - 1)
                );
        }
    }

    // ************
    // Growth Logic
    // ************

    /**
     * Attempts to place the plants produce in the world
     * on either of the 4 sides.
     *
     * @return the direction the produce was placed, {@code null}
     * if the produce could not be placed.
     */
    @Nullable
    private Direction placeProduce(World world, BlockPos pos){
        int random = MathUtil.getRandomIntegerInRange(0, 4);//Random direction.

        for(int i = 0; i < 4; i++){ //For each side
            BlockPos pos1 = null;
            BlockState block = null;
            Direction facing = null;

            if(random > 3) random = 0;

            switch (random){
                case 0:
                    pos1 = pos.north(); facing = Direction.NORTH; block = world.getBlockState(pos1);
                    break;
                case 1:
                    pos1 = pos.south(); facing = Direction.SOUTH; block = world.getBlockState(pos1);
                    break;
                case 2:
                    pos1 = pos.east(); facing = Direction.EAST; block = world.getBlockState(pos1);
                    break;
                case 3:
                    pos1 = pos.west(); facing = Direction.WEST; block = world.getBlockState(pos1);
                    break;
            }

            if(block == null) return null;

            if(block.getBlock() == Blocks.AIR && world.getBlockState(pos1.below()).canOcclude()){ //Place produce block.
                if(world.setBlockAndUpdate(pos1, Block.byItem(getProduce().getItem()).defaultBlockState()))
                    return facing;
                else return null;
            }

            random++;
        }

        //Could not place on any side.
        return null;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Handles growing the plant growth property and placing
     * its produce when it's already fully grown.
     */
    @Override
    void onGrowPlantBlock(World world, BlockState state, BlockPos pos, int increase) {
        int growth = increase + getGrowthStage(state);

        if(growth >= getMaxGrowthStage()){
            //Plance Produce
            if(attemptAutoHarvest(growth, world, pos)){
                setGrowthStage(world, pos, getPostHarvestGrowthStage());
                return;
            }

            growth = getMaxGrowthStage();
            Direction facing = placeProduce(world, pos);

            if(facing == null) return;

            world.setBlock(pos, this.defaultBlockState().setValue(this.getGrowthProperty(), growth)
                    .setValue(HorizontalBlock.FACING, facing), 2);
        } else {//Grow Stem
            setGrowthStage(world, pos, growth);
        }
    }

    // *****
    // Other
    // *****

    /**
     * While this plant type does not allow right-click harvesting, this method is implemented to provide
     * the base class with the growth stage this plant type is normally reset to.
     *
     * <p/>This is related to {@code BlockPlant#tryRightclickHarvest(World, BlockPos, PlayerEntity)}
     *
     * @return {@code 6} - the growth stage this plant is normally reset to.
     */
    @Override
    protected int getPostHarvestGrowthStage(){
        return 5;
    }
}
