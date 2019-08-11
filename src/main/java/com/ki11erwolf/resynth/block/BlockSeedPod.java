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

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;


@SuppressWarnings("deprecation")
public class BlockSeedPod extends ResynthBlock implements IPlantable {

    protected static final VoxelShape FLOWER_AABB = Block.makeCuboidShape(
             3.0D, 0.0D, 3.0D,
            14.0D, 15.0D, 14.0D
    );

    public BlockSeedPod() {
        super(Properties.create(Material.PLANTS), "seed_pod");
    }

    //@Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        IBlockState soil = worldIn.getBlockState(pos.down());
        return soil.getBlock().getClass() == Blocks.GRASS.getClass()
                || soil.getBlock().getClass() == Blocks.DIRT.getClass();
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumPlantType getPlantType(IBlockReader world, BlockPos pos) {
        return EnumPlantType.Plains;
    }

    @Override
    @Nonnull
    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state,
                                            BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    //TODO: Redo this.
//    /**
//     * {@inheritDoc}
//     * Determines what seeds to drop (random percentage chance)
//     * if seeds should be dropped.
//     *
//     * @param state
//     * @param worldIn
//     * @param fortune
//     * @param pos
//     * @return the seeds item to drop, the plant itself or nothing (config dependent).
//     */
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

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos,
                                Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        //this.checkAndDropBlock(worldIn, pos, state);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn) {
        tooltip.add(new TextComponentString(
                "Can drop seeds normally dropped by mobs for players in peaceful mode."));
    }

    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.canPlaceBlockAt(worldIn, pos)) {
            //this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    @Override
    public IBlockState getPlant(IBlockReader world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() != this)
            return getDefaultState();

        return state;
    }

    @Override
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XZ;
    }

    @Override
    public boolean isSolid(IBlockState state) {
        return false;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        Vec3d vec3d = state.getOffset(worldIn, pos);
        return FLOWER_AABB.withOffset(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
    public int getOpacity(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0;
    }
}
