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
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

            worldIn.playSound(null, pos, SoundEvents.BLOCK_GRASS_PLACE,
                    SoundCategory.BLOCKS, 0.4F, 1.2F / (worldIn.rand.nextFloat() * 0.2F + 0.9F));
            spawnBlockChangeParticles(worldIn, pos, 100);

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

    @SideOnly(Side.CLIENT)
    public static void spawnBlockChangeParticles(World worldIn, BlockPos pos, int amount){
        if (amount == 0){
            amount = 15;
        }

        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getMaterial() != Material.AIR) {
            for (int i = 0; i < amount; ++i){
                double d0 = itemRand.nextGaussian() * 0.02D;
                double d1 = itemRand.nextGaussian() * 0.02D;
                double d2 = itemRand.nextGaussian() * 0.02D;
                worldIn.spawnParticle(EnumParticleTypes.SPELL,
                        (double)((float)pos.getX() + itemRand.nextFloat()),
                        (double)pos.getY() + (double)itemRand.nextFloat()
                                * iblockstate.getBoundingBox(worldIn, pos).maxY,
                        (double)((float)pos.getZ() + itemRand.nextFloat()), d0, d1, d2);
            }
        }
        else {
            for (int i1 = 0; i1 < amount; ++i1) {
                double d0 = itemRand.nextGaussian() * 0.02D;
                double d1 = itemRand.nextGaussian() * 0.02D;
                double d2 = itemRand.nextGaussian() * 0.02D;
                worldIn.spawnParticle(EnumParticleTypes.SPELL,
                        (double)((float)pos.getX() + itemRand.nextFloat()),
                        (double)pos.getY() + (double)itemRand.nextFloat() * 1.0f,
                        (double)((float)pos.getZ() + itemRand.nextFloat()), d0, d1, d2);
            }
        }
    }
}
