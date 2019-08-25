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

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.MineralHoeConfig;
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
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Item Mineral Hoe.
 *
 * The tool of Resynth. It's used to turn dirt
 * into mineral soil using mineral crystals as fuel.
 *
 * It was put in place to replace the hold way of
 * obtaining mineral soil.
 */
public class ItemMineralHoe extends ResynthItem<ItemMineralHoe> {

    /**
     * The configuration settings for this item.
     */
    private static final MineralHoeConfig CONFIG = ResynthConfig.GENERAL_CONFIG.getCategory(MineralHoeConfig.class);

    /**
     * Logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    /**
     * NBT key used to store the number of charges in the hoe.
     */
    private static final String NBT_TAG_CHARGES = "charges";

    /**
     * Default item constructor.
     *
     * Sets the name and properties of the item.
     */
    ItemMineralHoe() {
        super(new Properties().maxStackSize(1), "mineral_hoe");
    }

    /**
     * Ensures that the NBT tag and required
     * NBT values are set on the given ItemStack.
     *
     * @param stack the given ItemStack.
     */
    private void ensureNBT(ItemStack stack){
        //NBT Initialization
        NBTTagCompound nbt = stack.getTag();

        if(nbt == null){
            nbt = new NBTTagCompound();
            nbt.setInt(NBT_TAG_CHARGES, (Math.min(CONFIG.getInitialCharges(), 2)));
            stack.setTag(nbt);
        }
    }

    /**
     * {@inheritDoc}.
     *
     * Displays a tooltip with the amount of charges left
     * and how to use the item.
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn,
                               List<ITextComponent> tooltip, ITooltipFlag flagIn){
        ensureNBT(stack);

        if(stack.getTag() != null)
            tooltip.add(getFormattedTooltip(
                "mineral_hoe_charges", TextFormatting.GOLD, stack.getTag().getInt(NBT_TAG_CHARGES))
            );

        setDescriptiveTooltip(tooltip, this);
    }

    // ********
    // Charging
    // ********

    /**
     * {@inheritDoc}
     *
     * Handles charging the Mineral Hoe with Mineral Crystals.
     *
     * @return {@link EnumActionResult#SUCCESS} if the Mineral Hoe
     * was charged.
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        ensureNBT(stack);
        //noinspection ConstantConditions //Already taken care of.
        int charges = stack.getTag().getInt(NBT_TAG_CHARGES);

        //At capacity
        if(charges >= CONFIG.getMaxCharges()){
            return ActionResult.newResult(EnumActionResult.FAIL, stack);
        }

        //Can charge
        if(player.isCreative() || takeCharge(player)){
            stack.getTag().setInt(NBT_TAG_CHARGES, charges + 1);
            world.playSound(
                    player, player.getPosition(), SoundEvents.BLOCK_NOTE_BLOCK_CHIME,
                    SoundCategory.BLOCKS, 1.0F, 1.0F
            );
            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
        }

        return ActionResult.newResult(EnumActionResult.FAIL, stack);
    }

    /**
     * Attempts to take a charge (Mineral Crystal) from the
     * players inventory.
     *
     * @param player the player who we want to take the charge from.
     * @return {@code true} if a charge was taken from the players
     * inventory.
     */
    private boolean takeCharge(EntityPlayer player){
        for(ItemStack itemStack : player.inventoryContainer.getInventory()){
            if(itemStack.getItem() == ResynthItems.ITEM_MINERAL_CRYSTAL){
                itemStack.shrink(1);
                return true;
            }
        }

        return false;
    }

    // *******
    // Tilling
    // *******

    /**
     * {@inheritDoc}
     *
     * Handles tilling dirt/grass into Mineral Soil.
     * Also handles charging the hoe if the player
     * is sneaking.
     *
     * @return {@link EnumActionResult#SUCCESS} if the block
     * the player is looking at was tilted or the hoe was charged.
     */
    @Nonnull
    @Override
    public EnumActionResult onItemUse(ItemUseContext context) {
        //Pre Checks
        if(!CONFIG.isEnabled()){
            return EnumActionResult.FAIL;
        }

        ensureNBT(context.getItem());
        if(context.getPlayer() == null){
            LOG.warn("Invalid player using the Mineral Hoe...");
            return EnumActionResult.FAIL;
        }

        Block block = context.getWorld().getBlockState(context.getPos()).getBlock();
        if(block instanceof InfoProvider){
            if(!context.getWorld().isRemote)
                context.getPlayer().sendMessage(
                        new TextComponentString(((InfoProvider)block).getInfo(context.getWorld(), context.getPos()))
                );
            return EnumActionResult.SUCCESS;
        }

        if(context.getPlayer().isSneaking()){
            return onItemRightClick(context.getWorld(), context.getPlayer(), EnumHand.MAIN_HAND).getType();
        }

        //Creative mode
        if(context.getPlayer().isCreative())
            return replace(context);

        //Not creative
        //noinspection ConstantConditions //Should be taken care of.
        int charges = context.getItem().getTag().getInt(NBT_TAG_CHARGES);
        if(charges < 1){
            if(CONFIG.playFailSound())
                context.getWorld().playSound(
                        context.getPlayer(), context.getPos(), SoundEvents.BLOCK_NOTE_BLOCK_HAT,
                        SoundCategory.BLOCKS, 1.0F, 1.0F
                );

            return EnumActionResult.FAIL;
        }

        //Remove a charge.
        EnumActionResult result = replace(context);
        if(result == EnumActionResult.SUCCESS)
            context.getItem().getTag().setInt(NBT_TAG_CHARGES, charges - 1);

        return result;
    }

    /**
     * Replaces the block the player is looking at
     * to Mineral Soil if possible.
     *
     * @param context the ItemUseContext instance given by
     *                {@link #onItemUse(ItemUseContext)}.
     * @return {@link EnumActionResult#SUCCESS} if the block
     * was replaced.
     */
    private EnumActionResult replace(ItemUseContext context){
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        IBlockState source = world.getBlockState(pos);

        if(source.getBlock() == Blocks.DIRT || source.getBlock() == Blocks.GRASS_BLOCK){
            if (context.getFace() != EnumFacing.DOWN && world.isAirBlock(pos.up())) {
                //Replacement
                world.setBlockState(pos, ResynthBlocks.BLOCK_MINERAL_SOIL.getDefaultState());

                if(CONFIG.showParticles())
                    spawnParticles(world, pos.up());

                world.playSound(
                        context.getPlayer(), pos, SoundEvents.ITEM_HOE_TILL,
                        SoundCategory.BLOCKS, 1.0F, 1.0F
                );

                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.FAIL;
    }

    /**
     * Spawns fire particles in the world.
     *
     * @param worldIn the world to spawn the particles in.
     * @param pos the position in the world to spawn the particles in.
     */
    @OnlyIn(Dist.CLIENT)
    private static void spawnParticles(World worldIn, BlockPos pos){
        double d = random.nextGaussian() * 0.02D;
        int amount = 5;

        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getMaterial() != Material.AIR) {
            for (int i = 0; i < amount; ++i){
                worldIn.spawnParticle(Particles.FLAME,
                        (float)pos.getX() + random.nextFloat(),
                        (double)pos.getY() + (double)random.nextFloat()
                                * iblockstate.getShape(worldIn, pos).getEnd(EnumFacing.Axis.Y),
                        (float)pos.getZ() + random.nextFloat(), d, d, d);
            }
        }
        else {
            for (int i1 = 0; i1 < amount; ++i1) {
                worldIn.spawnParticle(Particles.FLAME,
                        (float)pos.getX() + random.nextFloat(),
                        (double)pos.getY() + (double)random.nextFloat() * 1.0f,
                        (float)pos.getZ() + random.nextFloat(), d, d, d);
            }
        }
    }

    /**
     * Allows blocks to provide information
     * to the user in a chat message when
     * they right-click the block with a
     * Mineral Hoe.
     */
    public interface InfoProvider {

        /**
         * Called by the Mineral Hoe to obtain
         * the message it should display to
         * the player.
         *
         * @return the message that should
         * be displayed to the player.
         */
        String getInfo(World world, BlockPos pos);
    }
}
