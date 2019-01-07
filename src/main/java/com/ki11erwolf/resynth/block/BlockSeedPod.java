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
package com.ki11erwolf.resynth.block;

import com.ki11erwolf.resynth.plant.PlantBiochemical;
import com.ki11erwolf.resynth.plant.ResynthPlants;
import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.ki11erwolf.resynth.ResynthConfig.MYSTICAL_SEED_POD;

/**
 * Mystical Seed Pod. A flower that spawns randomly in the
 * world and drops biochemical seeds.
 */
@SuppressWarnings("deprecation")
public class BlockSeedPod extends ResynthBlock implements IPlantable {

    /**
     * Hit box for the plant.
     */
    protected static final AxisAlignedBB FLOWER_AABB
            = new AxisAlignedBB(
            0.30000001192092896D,
            0.0D,
            0.30000001192092896D,
            0.699999988079071D,
            0.875D,
            0.699999988079071D
    );

    /**
     * Default constructor.
     */
    public BlockSeedPod() {
        super(Material.PLANTS, SoundType.PLANT, "seedPod");
    }

    /**
     * {@inheritDoc}
     *
     * @param worldIn
     * @param pos
     * @return true if the block below is dirt or grass.
     */
    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        IBlockState soil = worldIn.getBlockState(pos.down());
        return soil.getBlock().getClass() == Blocks.GRASS.getClass()
                || soil.getBlock().getClass() == Blocks.DIRT.getClass();
    }

    /**
     * {@inheritDoc}
     *
     * @param state
     * @param source
     * @param pos
     * @return flower hit box
     */
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FLOWER_AABB;
    }

    /**
     * {@inheritDoc}
     *
     * @param state
     * @return false
     */
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param state
     * @return false
     */
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param world
     * @param pos
     * @return plains plant type
     */
    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Plains;
    }

    /**
     * {@inheritDoc}
     *
     * @param world
     * @param pos
     * @return this plants default state
     */
    @Override
    public IBlockState getPlant(net.minecraft.world.IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() != this)
            return getDefaultState();

        return state;
    }

    /**
     * {@inheritDoc}
     *
     * @return cutout
     */
    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * {@inheritDoc}
     *
     * @param worldIn
     * @param state
     * @param pos
     * @param face
     * @return undefined
     */
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    /**
     * {@inheritDoc}
     *
     * @param blockState
     * @param worldIn
     * @param pos
     * @return none
     */
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    /**
     * {@inheritDoc}
     * Determines what seeds to drop (random percentage chance)
     * if seeds should be dropped.
     *
     * @param state
     * @param rand
     * @param fortune
     * @return the seeds item to drop, the plant itself or nothing (config dependent).
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (MYSTICAL_SEED_POD.dropSeeds) {
            List<PlantBiochemical> plants = Arrays.asList(ResynthPlants.getBiochemicalPlants());

            PlantBiochemical plant = ResynthPlants.getBiochemicalPlants()[0];

            for (int i = 0; i <= MYSTICAL_SEED_POD.triesPerBreak; i++) {
                plant = plants.get(MathUtil.getRandomIntegerInRange(0, plants.size() - 1));
                if (MathUtil.chance(plant.getSeedPodDropPercentage())) {
                    return plant.getSeeds();
                }
            }

            if (MYSTICAL_SEED_POD.alwaysDropSeeds) {
                return plant.getSeeds();
            } else {
                return Item.getItemFromBlock(Blocks.AIR);
            }
        }

        return super.getItemDropped(state, rand, fortune);
    }

    /**
     * {@inheritDoc}
     * Updates and drops (if on invalid block) the block when a neighbour
     * changes.
     *
     * @param state
     * @param worldIn
     * @param pos
     * @param blockIn
     * @param fromPos
     */
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        this.checkAndDropBlock(worldIn, pos, state);
    }

    /**
     * {@inheritDoc}
     *
     * Adds information to the tooltip about what the block does.
     *
     * @param stack
     * @param worldIn
     * @param tooltip
     * @param flagIn
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip,
                               ITooltipFlag flagIn) {
        tooltip.add("Can drop seeds normally dropped by mobs for players in peaceful mode.");
    }

    /**
     * Drops the plant block if it isn't
     * place on dirt or grass.
     *
     * @param worldIn -
     * @param pos     -
     * @param state   -
     */
    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.canPlaceBlockAt(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }



}
