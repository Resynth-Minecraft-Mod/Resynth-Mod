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

import com.ki11erwolf.resynth.plant.set.ICrystallineSetProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

/**
 * The Crystalline set plant block - used to grow resources that
 * drop when ores are mined (e.g. diamond). Specific plant blocks
 * are usually referred to as plant set types, while their instances
 * (diamond vs redstone) are referred to as the plant type.
 * <p/>
 * This class defines the look and feel (how the plant looks and behaves)
 * of the Crystalline plant type. This class DOES NOT handle any logic
 * other the way in which the plant type grows.
 */
public abstract class BlockCrystallinePlant extends BlockPlant<BlockCrystallinePlant> {

    /**
     * @param plantTypeName the name of the plant set type (e.g. crystalline).
     * @param plantName the name of the plant type (e.g. diamond).
     * @param properties the properties (e.g. growth chances) of the
     *                   plant type.
     */
    @SuppressWarnings("WeakerAccess")//Lies
    public BlockCrystallinePlant(String plantTypeName, String plantName, ICrystallineSetProperties properties) {
        super(plantTypeName, plantName, properties);
    }

    // *************************
    // Abstract method overrides
    // *************************

    /**
     * {@inheritDoc}.
     *
     * @return the growth stages for Crystalline plant set types.
     */
    @Override
    IntegerProperty getGrowthProperty() {
        return IntegerProperty.create("growth_stage", 0, 7);
    }

    /**
     * {@inheritDoc}
     *
     * @return the bounding box shapes for the
     * various growth stages of Crystalline plant
     * set types.
     */
    @Override
    VoxelShape[] getShapeByAge() {
        return new VoxelShape[]{
                Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 2.0D, 10.0D), //1
                Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 3.5D, 11.0D), //2
                Block.makeCuboidShape(4.5D, 0.0D, 4.5D, 11.5D, 5.0D, 11.5D), //3
                Block.makeCuboidShape(2.5D, 0.0D, 2.5D, 13.5D, 7.5D, 13.5D), //4
                Block.makeCuboidShape(2.5D, 0.0D, 2.5D, 13.5D, 7.5D, 13.5D), //5
                Block.makeCuboidShape(0.5D, 0.5D, 0.5D, 15.5D, 11.0D, 15.5D),//6
                Block.makeCuboidShape(0.5D, 0.0D, 0.5D, 15.5D, 14.0D, 15.5D),//7
                Block.makeCuboidShape(0.5D, 0.0D, 0.5D, 15.5D, 15.0D, 15.5D) //8
        };
    }

    /**
     * {@inheritDoc}
     *
     * @return the number of growth stages
     * every Crystalline plant set type has.
     */
    @Override
    int getMaxGrowthStage() {
        return 7;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code true} - Crystalline plant set types
     * always drop their produce when fully grown and broken.
     */
    @Override
    boolean dropsProduceWhenGrown(){
        return true;
    }

    // ************
    // Growth Logic
    // ************

    /**
     * {@inheritDoc}
     * <p/>
     * Grows the plant in the world. Specifically, increases
     * the plant growth property by the given amount.
     */
    @Override
    void growPlant(World world, BlockState state, BlockPos pos, int increase) {
        int growth = increase + getGrowthStage(state);

        if(growth > getMaxGrowthStage())
            growth = getMaxGrowthStage();

        world.setBlockState(pos, this.getDefaultState().with(this.getGrowthProperty(), growth), 2);
    }
}
