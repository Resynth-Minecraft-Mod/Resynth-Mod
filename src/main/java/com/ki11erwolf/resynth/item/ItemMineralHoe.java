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
import com.ki11erwolf.resynth.util.EffectsUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Item Mineral Hoe.
 *
 * The tool of Resynth. It's used to turn dirt
 * into mineral soil using mineral crystals as fuel.
 *
 * It was put in place to replace the hold way of
 * obtaining mineral soil.
 */
//TODO: This could be better written...
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
    ItemMineralHoe(String name) {
        super(new Properties().maxStackSize(1), name);
    }

    /**
     * Ensures that the NBT tag and required
     * NBT values are set on the given ItemStack.
     *
     * @param stack the given ItemStack.
     */
    private void ensureNBT(ItemStack stack){
        //NBT Initialization
        CompoundNBT nbt = stack.getTag();

        if(nbt == null){
            nbt = new CompoundNBT();
            nbt.putInt(NBT_TAG_CHARGES, (Math.min(CONFIG.getInitialCharges(), 2)));
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
     * @return {@link ActionResultType#SUCCESS} if the Mineral Hoe
     * was charged.
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        ensureNBT(stack);
        //noinspection ConstantConditions //Already taken care of.
        int charges = stack.getTag().getInt(NBT_TAG_CHARGES);

        //At capacity
        if(charges >= CONFIG.getMaxCharges()){
            return ActionResult.newResult(ActionResultType.FAIL, stack);
        }

        //Can charge
        if(player.isCreative() || takeCharge(player)){
            stack.getTag().putInt(NBT_TAG_CHARGES, charges + 1);
            EffectsUtil.playSound(
                    world, player, player.getPosition(),
                    SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.BLOCKS
            );
            return ActionResult.newResult(ActionResultType.SUCCESS, stack);
        }

        return ActionResult.newResult(ActionResultType.FAIL, stack);
    }

    /**
     * Attempts to take a charge (Mineral Crystal) from the
     * players inventory.
     *
     * @param player the player who we want to take the charge from.
     * @return {@code true} if a charge was taken from the players
     * inventory.
     */
    private boolean takeCharge(PlayerEntity player){
        for(ItemStack itemStack : player.container.getInventory()){
            if(itemStack.getItem() == ResynthItems.ITEM_MINERAL_CRYSTAL){
                itemStack.shrink(1);
                return true;
            }
        }

        return false;
    }

    // *******
    // Actions
    // *******

    /**
     * {@inheritDoc}
     *
     * Handles what happens when the hoe is used on a block.
     * This can be many things, from charging the hoe,
     * tilling soil, getting block info or growing a plant.
     *
     * @return {@link ActionResultType#SUCCESS} if the block
     * the player is looking at was tilted or the hoe was charged.
     */
    @Nonnull
    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        //Pre Checks
        if(!CONFIG.isEnabled()){
            return ActionResultType.FAIL;
        }

        ensureNBT(context.getItem());
        if(context.getPlayer() == null){
            LOG.warn("Invalid player using the Mineral Hoe...");
            return ActionResultType.FAIL;
        }

        //Begin
        Block block = context.getWorld().getBlockState(context.getPos()).getBlock();

        //Grow plant
        if(context.getPlayer().isCreative() && context.getPlayer().isSneaking()){
            if(tryGrowPlant(block, context))
                return ActionResultType.SUCCESS;
        }

        //Info Provider
        if(block instanceof InfoProvider){
            getInfo(block, context);
            return ActionResultType.SUCCESS;
        }

        //Charge
        if(context.getPlayer().isSneaking()){
            return onItemRightClick(context.getWorld(), context.getPlayer(), Hand.MAIN_HAND).getType();
        }

        //Tilling
        //Creative mode
        if(context.getPlayer().isCreative())
            return replace(context);

        //Not creative
        if(context.getItem().getTag() == null)
            return ActionResultType.FAIL;

        int charges = context.getItem().getTag().getInt(NBT_TAG_CHARGES);
        if(charges < 1){
            if(CONFIG.playFailSound())
                EffectsUtil.playSound(
                        context.getWorld(), context.getPlayer(), context.getPos(),
                        SoundEvents.BLOCK_NOTE_BLOCK_HAT, SoundCategory.BLOCKS
                );

            return ActionResultType.FAIL;
        }

        //Remove a charge.
        ActionResultType result = replace(context);
        if(result == ActionResultType.SUCCESS)
            context.getItem().getTag().putInt(NBT_TAG_CHARGES, charges - 1);

        return result;
    }

    /**
     * Attempts to grow a plant similar to bonemeal,
     * however it ignores {@link IGrowable#canGrow(IBlockReader, BlockPos, BlockState, boolean)}
     * and {@link IGrowable#canUseBonemeal(World, Random, BlockPos, BlockState)}.
     *
     * @return {@code true} if the plant could be grown.
     */
    private boolean tryGrowPlant(Block block, ItemUseContext context){
        if(block instanceof IGrowable){
            if(!context.getWorld().isRemote)
                ((IGrowable)block).grow(
                        context.getWorld(),
                        random,
                        context.getPos(),
                        context.getWorld().getBlockState(context.getPos())
                );
            return true;
        }

        return false;
    }

    /**
     * Shows the provided info from an info provider
     * block in chat.
     */
    private void getInfo(Block block, ItemUseContext context){
        if(!context.getWorld().isRemote)
            if(context.getPlayer() != null)
            context.getPlayer().sendMessage(
                    new StringTextComponent(((InfoProvider)block).getInfo(context.getWorld(), context.getPos()))
            );
    }

    /**
     * Replaces the block the player is looking at
     * to Mineral Soil if possible.
     *
     * @param context the ItemUseContext instance given by
     *                {@link #onItemUse(ItemUseContext)}.
     * @return {@link ActionResultType#SUCCESS} if the block
     * was replaced.
     */
    private ActionResultType replace(ItemUseContext context){
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState source = world.getBlockState(pos);

        if(source.getBlock() == Blocks.DIRT || source.getBlock() == Blocks.GRASS_BLOCK){
            if (context.getFace() != Direction.DOWN && world.isAirBlock(pos.up())) {
                //Replacement
                world.setBlockState(pos, ResynthBlocks.BLOCK_MINERAL_SOIL.getDefaultState());

                if(CONFIG.showParticles() && world.isRemote)
                    spawnParticles(world, pos.up());

                EffectsUtil.playSound(world, context.getPlayer(), pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS);

                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.FAIL;
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

        BlockState blockstate = worldIn.getBlockState(pos);

        if (blockstate.getMaterial() != Material.AIR) {
            for (int i = 0; i < amount; ++i){
                worldIn.addParticle(ParticleTypes.FLAME,
                        (float)pos.getX() + random.nextFloat(),
                        (double)pos.getY() + (double)random.nextFloat()
                                * blockstate.getShape(worldIn, pos).getEnd(Direction.Axis.Y),
                        (float)pos.getZ() + random.nextFloat(), d, d, d);
            }
        }
        else {
            for (int i1 = 0; i1 < amount; ++i1) {
                worldIn.addParticle(ParticleTypes.FLAME,
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
