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

import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.MineralOreConfig;
import com.ki11erwolf.resynth.item.ResynthItems;
import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Mineral Rich Stone. The mods ore block.
 */
//TODO: Maybe rename to BlockMineralStone or just MineralStone
public class BlockMineralOre extends ResynthBlock{

    /**
     * The configuration settings for this block.
     */
    private static final MineralOreConfig CONFIG = ResynthConfig.GENERAL_CONFIG.getCategory(MineralOreConfig.class);

    /**
     * Sets the basic properties of the block.
     */
    BlockMineralOre() {
        super(
                Block.Properties.create(Material.ROCK)
                        .hardnessAndResistance(3.0F, 3.0F)
                        .lightValue(1),
                "mineral_ore"
        );
    }

    /**
     * {@inheritDoc}.
     *
     * Sets the tooltip on what this block does/how to use it.
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn) {
        setDescriptiveTooltip(tooltip, this);
    }

    /**
     * {@inheritDoc}
     *
     * @return The item dropped when the block is broken:
     * {@link ResynthItems#ITEM_MINERAL_ROCK}.
     */
    @Nonnull
    @Override
    public Item getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune) {
        return ResynthItems.ITEM_MINERAL_ROCK;
    }

    /**
     * {@inheritDoc}
     *
     * @return the number of items to drop when this block is broken.
     * Determined by config settings.
     */
    @Override
    @SuppressWarnings("deprecation")
    public int quantityDropped(IBlockState state, Random random) {
        return CONFIG.getBaseDrops()
                + (MathUtil.chance((float)CONFIG.getExtraDropsChance()) ? CONFIG.getExtraDrops() : 0);
    }

    /**
     * {@inheritDoc}
     *
     * @return the amount of experience points to give the player
     * when they break the block. Determined by config.
     */
    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IWorldReader reader, BlockPos pos, int fortune) {
        //noinspection deprecation
        if (this.getItemDropped(null, null, null, 0) != Item.getItemFromBlock(this)){
            int min = CONFIG.getMinExpDropped(); int max = CONFIG.getMaxExpDropped();

            if(min > max)
                min = max;

            return MathUtil.getRandomIntegerInRange(min, max);
        }

        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @return The tool needed to break this block: Pickaxe.
     */
    @Nullable
    @Override
    public ToolType getHarvestTool(IBlockState state) {
        return ToolType.PICKAXE;
    }

    /**
     * {@inheritDoc}
     *
     * @return the required tool level to break this block: 2 (iron).
     */
    @Override
    public int getHarvestLevel(IBlockState state) {
        return 2;
    }

    /**
     * {@inheritDoc}.
     *
     * Overrides silk touch - it's not wanted.
     *
     * @param state
     * @return the item to drop when the block is broken with a
     * silk touch pickaxe: {@link ResynthItems#ITEM_MINERAL_ROCK}.
     */
    @Nonnull
    @Override
    protected ItemStack getSilkTouchDrop(@Nonnull IBlockState state){
        return new ItemStack(ResynthItems.ITEM_MINERAL_ROCK, quantityDropped(null, null));
    }
}
