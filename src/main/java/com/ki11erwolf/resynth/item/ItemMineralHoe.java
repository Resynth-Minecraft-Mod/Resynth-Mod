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
package com.ki11erwolf.resynth.item;

import com.ki11erwolf.resynth.ResynthConfig;
import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.util.MinecraftUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Item Mineral Hoe.
 *
 * The tool of Resynth. It's used to turn dirt
 * into mineral soil by using mineral crystals.
 *
 * It was put in place to replace the hold way of
 * obtaining mineral soil.
 */
public class ItemMineralHoe extends ResynthItem {

    /**
     * Change NBT value key.
     */
    public static final String NBT_TAG_CHARGES = "charges";

    /**
     * Default constructor.
     */
    public ItemMineralHoe() {
        super("mineralHoe");
        this.setMaxStackSize(1);
    }

    /**
     * {@inheritDoc}
     *
     * Handles turning dirt/grass into mineral soil
     * using mineral crystal charges.
     *
     * @param player
     * @param world
     * @param blockPos
     * @param hand
     * @param blockDirection
     * @param x
     * @param y
     * @param z
     * @return
     */
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos blockPos, EnumHand hand,
                                      EnumFacing blockDirection, float x, float y, float z) {

        Block type = world.getBlockState(blockPos).getBlock();
        ItemStack stack = player.getHeldItem(hand);
        NBTTagCompound nbt = stack.getTagCompound();

        if(player.isSneaking() || !ResynthConfig.MINERAL_HOE.canUse){
            return EnumActionResult.FAIL;
        }

        //Non-creative charges check.
        if(!player.isCreative() && nbt.getInteger(NBT_TAG_CHARGES) <= 0){
            world.playSound(
                    player, blockPos,
                    SoundEvents.ENTITY_HORSE_LAND,
                    SoundCategory.NEUTRAL,
                    1.0F, 1.0F);
            return EnumActionResult.FAIL;
        }

        //Block change action
        if(type == Blocks.DIRT || type == Blocks.GRASS){
            world.playSound(null, blockPos, SoundEvents.BLOCK_GRASS_PLACE,
                    SoundCategory.BLOCKS, 0.4F, 1.2F
                            / (world.rand.nextFloat() * 0.2F + 0.9F));
            spawnBlockChangeParticles(world, blockPos, 100);

            world.setBlockState(blockPos, ResynthBlocks.BLOCK_MINERAL_SOIL.getDefaultState());
            if(!player.isCreative())
                nbt.setInteger(NBT_TAG_CHARGES, nbt.getInteger(NBT_TAG_CHARGES) - 1);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

    /**
     * {@inheritDoc}
     *
     * Handles charging the hoe with mineral crystals.
     *
     * @param world
     * @param player
     * @param hand
     * @return
     */
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player,
                                                    EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        new MinecraftUtil.SideSensitiveCode(world){
            @Override
            public void onServer() {
                //Creative
                if(player.isSneaking()){
                    if(player.isCreative()){
                        stack.getTagCompound().setInteger(ItemMineralHoe.NBT_TAG_CHARGES,
                                stack.getTagCompound()
                                        .getInteger(ItemMineralHoe.NBT_TAG_CHARGES) + 1);
                        return;
                    }

                    //Not creative
                    int pos = 0;

                    for(;;){
                        try{
                            if(player.inventory.getStackInSlot(pos).getItem()
                                    == ResynthItems.ITEM_MINERAL_CRYSTAL){

                                player.inventory.getStackInSlot(pos).setCount(
                                        player.inventory.getStackInSlot(pos).getCount()
                                                - 1
                                );

                                stack.getTagCompound().setInteger(ItemMineralHoe.NBT_TAG_CHARGES,
                                        stack.getTagCompound()
                                                .getInteger(ItemMineralHoe.NBT_TAG_CHARGES) + 1);
                                break;
                            }

                            if(player.inventory.getStackInSlot(pos) == ItemStack.EMPTY)
                                break;

                            pos++;
                        } catch (Exception e){
                            break;
                        }
                    }
                }
            }
        }.execute();

        return super.onItemRightClick(world, player, hand);
    }

    /**
     * {@inheritDoc}
     *
     * Displays a tooltip with the amount of charges left
     * and how to use the item.
     *
     * @param stack
     * @param worldIn
     * @param tooltip
     * @param flagIn
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip,
                               ITooltipFlag flagIn){

        //NBT Initialization
        NBTTagCompound nbt = stack.getTagCompound();

        if(nbt == null){
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
            nbt.setInteger(NBT_TAG_CHARGES, ResynthConfig.MINERAL_HOE.initialCharges);
        }

        //NBT tooltip
        tooltip.add(TextFormatting.GOLD + "Charges: " + nbt.getInteger(NBT_TAG_CHARGES));
        //Usage tooltip
        tooltip.add(TextFormatting.GRAY
                + "Sneak + Right Click with Mineral Crystals in inventory to charge.");
    }

    /**
     * Spawns particles in the world when ever a dirt/grass
     * block is turned into mineral soil.
     *
     * @param worldIn the world in which to spawn the particles.
     * @param pos the location in the world.
     * @param amount amount of particles.
     */
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
                worldIn.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK,
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
                worldIn.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK,
                        (double)((float)pos.getX() + itemRand.nextFloat()),
                        (double)pos.getY() + (double)itemRand.nextFloat() * 1.0f,
                        (double)((float)pos.getZ() + itemRand.nextFloat()), d0, d1, d2);
            }
        }
    }
}
