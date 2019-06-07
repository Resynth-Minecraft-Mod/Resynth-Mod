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

import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.util.MinecraftUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
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
     * The registry name of the item.
     */
    public static final String ITEM_NAME = "mineral_hoe";

    /**
     * Default constructor.
     */
    public ItemMineralHoe() {
        super(new Properties().maxStackSize(1), ITEM_NAME);
    }

    /**
     * {@inheritDoc}
     *
     * Handles both charging the hoe with mineral crystals
     * and turning dirt/grass into mineral soil
     * using mineral crystal charges.
     *
     * @return the result of the action.
     */
    @Nonnull
    @Override
    @SuppressWarnings("ConstantConditions")
    public EnumActionResult onItemUse(ItemUseContext context) {
        if(false/*TODO: Config*/){
            return EnumActionResult.FAIL;
        }

        World world = context.getWorld();
        BlockPos blockPos = context.getPos();
        EntityPlayer player = context.getPlayer();

        Block type = world.getBlockState(blockPos).getBlock();
        ItemStack stack = player.getActiveItemStack();

        if(stack == null)
            return EnumActionResult.FAIL;

        NBTTagCompound nbt = stack.getTag();

        if(player.isSneaking()){
            new MinecraftUtil.SideSensitiveCode(world){
                @Override
                public void onServer() {
                    //Creative
                    if(player.isSneaking()){
                        if(stack.getTag().getInt(NBT_TAG_CHARGES)
                                >= 64/*TODO: Config*/){
                            world.playSound(
                                    null, blockPos,
                                    SoundEvents.ENTITY_HORSE_LAND,
                                    SoundCategory.NEUTRAL,
                                    0.7F, 1.0F);
                            return;
                        }

                        if(player.isCreative()){
                            stack.getTag().setInt(ItemMineralHoe.NBT_TAG_CHARGES,
                                    stack.getTag().getInt(ItemMineralHoe.NBT_TAG_CHARGES) + 1);

                            world.playSound(
                                    null, blockPos,
                                    SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                                    SoundCategory.NEUTRAL,
                                    0.7F, 1.0F);
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

                                    stack.getTag().setInt(ItemMineralHoe.NBT_TAG_CHARGES,
                                            stack.getTag().getInt(
                                                    ItemMineralHoe.NBT_TAG_CHARGES) + 1);

                                    world.playSound(
                                            null, blockPos,
                                            SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                                            SoundCategory.NEUTRAL,
                                            0.7F, 1.0F);
                                    break;
                                }

                                if(player.inventory.getStackInSlot(pos) == ItemStack.EMPTY){
                                    world.playSound(
                                            null, blockPos,
                                            SoundEvents.ENTITY_HORSE_LAND,
                                            SoundCategory.NEUTRAL,
                                            0.7F, 1.0F);
                                    break;
                                }


                                pos++;
                            } catch (Exception e){
                                break;
                            }
                        }
                    }
                }
            }.execute();
            return EnumActionResult.FAIL;
        }

        //***********************
        //Block replacement code.
        //***********************

        //Non-creative charges check.
        if(!player.isCreative() && nbt.getInt(NBT_TAG_CHARGES) <= 0){
            world.playSound(
                    player, blockPos,
                    SoundEvents.ENTITY_HORSE_LAND,
                    SoundCategory.NEUTRAL,
                    0.7F, 1.0F);
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
                nbt.setInt(NBT_TAG_CHARGES, nbt.getInt(NBT_TAG_CHARGES) - 1);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn){

        //NBT Initialization
        NBTTagCompound nbt = stack.getTag();

        if(nbt == null){
            nbt = new NBTTagCompound();
            stack.setTag(nbt);
            nbt.setInt(NBT_TAG_CHARGES, 2/*TODO: config*/);
        }

        //NBT tooltip
        tooltip.add(new TextComponentString(TextFormatting.GOLD + "Charges: " + nbt.getInt(NBT_TAG_CHARGES)));
        //Usage tooltip
        tooltip.add(new TextComponentString(
                "Sneak + Right Click on a block with Mineral Crystals in inventory to charge."));
    }

    /**
     * Spawns particles in the world when ever a dirt/grass
     * block is turned into mineral soil.
     *
     * @param worldIn the world in which to spawn the particles.
     * @param pos the location in the world.
     * @param amount amount of particles.
     */
    //@SideOnly(Side.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public static void spawnBlockChangeParticles(World worldIn, BlockPos pos, int amount){
        if (amount == 0){
            amount = 15;
        }

        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getMaterial() != Material.AIR) {
            for (int i = 0; i < amount; ++i){
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                worldIn.spawnParticle(Particles.FIREWORK,
                        (double)((float)pos.getX() + random.nextFloat()),
                        (double)pos.getY() + (double)random.nextFloat()
                                * iblockstate.getShape(worldIn, pos).getEnd(EnumFacing.Axis.Y),
                        (double)((float)pos.getZ() + random.nextFloat()), d0, d1, d2);
            }
        }
        else {
            for (int i1 = 0; i1 < amount; ++i1) {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                worldIn.spawnParticle(Particles.FIREWORK,
                        (double)((float)pos.getX() + random.nextFloat()),
                        (double)pos.getY() + (double)random.nextFloat() * 1.0f,
                        (double)((float)pos.getZ() + random.nextFloat()), d0, d1, d2);
            }
        }
    }
}
