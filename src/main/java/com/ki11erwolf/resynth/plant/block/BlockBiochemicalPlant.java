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
package com.ki11erwolf.resynth.plant.block;

import com.ki11erwolf.resynth.plant.set.IBiochemicalSetProperties;
import com.ki11erwolf.resynth.util.EffectsUtil;
import com.ki11erwolf.resynth.util.MinecraftUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

/**
 * The Biochemical set plant block - used to grow resources that
 * drop when a mob is killed. Specific plant blocks
 * are usually referred to as plant set types, while their instances
 * (diamond vs redstone) are referred to as the plant type.
 * <p/>
 * This class defines the look and feel (how the plant looks and behaves)
 * of the Biochemical plant block. This class DOES NOT handle any logic
 * other the way in which the plant type grows and is harvested.
 */
public abstract class BlockBiochemicalPlant extends BlockPlant<BlockBiochemicalPlant> {

    /**
     * @param plantTypeName the name of the plant set type (e.g. crystalline).
     * @param plantName the name of the plant type (e.g. diamond).
     * @param properties the properties (e.g. growth chances) of the
     *                   plant type.
     */
    @SuppressWarnings("WeakerAccess")//Lies
    public BlockBiochemicalPlant(String plantTypeName, String plantName, IBiochemicalSetProperties properties) {
        super(plantTypeName, plantName, properties);
    }

    // *************************
    // Abstract method overrides
    // *************************

    /**
     * {@inheritDoc}
     */
    @Override
    IntegerProperty getGrowthProperty() {
        return IntegerProperty.create("growth_stage", 0, 7);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    VoxelShape[] getShapeByAge() {
        return new VoxelShape[]{
                Block.makeCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 2.0D, 9.0D),   //1
                Block.makeCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 2.0D, 9.0D),   //2
                Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 3.0D, 11.0D), //3
                Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 5.0D, 11.0D), //4
                Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 7.0D, 13.0D), //5
                Block.makeCuboidShape(3.0D, 0.5D, 3.0D, 13.0D, 9.0D, 13.0D),//6
                Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 11.0D, 13.0D),//7
                Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D) //8
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxGrowthStage() {
        return 7;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean dropsProduceWhenGrown(){
        return true;
    }

    // *************
    // Harvest Logic
    // *************

    /**
     * {@inheritDoc}
     *
     * Handles what happens when the player tries
     * to harvest the plant by right-clicking.
     *
     * @return {@code true} if the plant was harvested.
     */
    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType func_225533_a_(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                                           BlockRayTraceResult hit){
        int growth = world.getBlockState(pos).get(getGrowthProperty());
        int postHarvestGrowth = growth - 4;

        //noinspection rawtypes
        if(growth >= ((BlockPlant)world.getBlockState(pos).getBlock()).getMaxGrowthStage()){
            if(world.setBlockState(pos, world.getBlockState(pos)
                    .with(getGrowthProperty(), postHarvestGrowth), 2)){
                if(!world.isRemote)
                    MinecraftUtil.spawnItemStackInWorld(
                            new ItemStack(
                                    getProduce().getItem(),
                                    ((IBiochemicalSetProperties) properties).numberOfProduceDrops()
                            ),
                            world, pos
                    );

                EffectsUtil.playNormalSoundWithRandomPitch(
                        world, player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS
                );

                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.FAIL;
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
