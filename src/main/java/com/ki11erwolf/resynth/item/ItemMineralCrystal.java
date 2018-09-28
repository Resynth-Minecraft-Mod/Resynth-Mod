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
package com.ki11erwolf.resynth.item;

import com.ki11erwolf.resynth.block.ResynthBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Mineral Crystal. Recipe component for Mineral Soil.
 */
public class ItemMineralCrystal extends ResynthItem {

    /**
     * Sets the creative tab, unlocalized name prefix
     * and registry name.
     */
    public ItemMineralCrystal() {
        super("mineralCrystal");
    }

    /**
     * {@inheritDoc}
     * Turns grass/dirt into mineral soil
     * at the cost of this item.
     *
     * @param player
     * @param worldIn
     * @param pos
     * @param hand
     * @param facing
     * @param hitX
     * @param hitY
     * @param hitZ
     * @return
     */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ){
        Block type = worldIn.getBlockState(pos).getBlock();

        if(type == Blocks.DIRT || type == Blocks.GRASS){
            worldIn.setBlockState(pos, ResynthBlocks.BLOCK_MINERAL_SOIL.getDefaultState());
            player.getHeldItem(hand).shrink(1);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

    /**
     * {@inheritDoc}
     * Adds a tooltip on how to use the item.
     *
     * @param stack
     * @param worldIn
     * @param tooltip
     * @param flagIn
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip,
                               ITooltipFlag flagIn){
        tooltip.add("Right click on dirt or grass to turn it into mineral soil.");
    }
}
