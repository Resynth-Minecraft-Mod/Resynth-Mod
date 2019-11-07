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
import com.ki11erwolf.resynth.util.BlockInfoProvider;
import com.ki11erwolf.resynth.util.EffectsUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
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
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Mineral Hoe - Resynth's tool of choice.
 *
 * <p/>The Mineral Hoe has many uses, which include:
 * tilling dirt or grass into Mineral Soil, .......
 */
class ItemMineralHoe extends ResynthItem<ItemMineralHoe>{

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
     * Constructs the Mineral Hoe item class.
     *
     * @param name registry name of the item.
     */
    ItemMineralHoe(String name) {
        super(new Properties().maxStackSize(1), name);
    }

    // *******
    // Tooltip
    // *******

    /**
     * {@inheritDoc}.
     *
     * Displays a tooltip with the amount of charges left
     * and how to use the item.
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn,
                               List<ITextComponent> tooltip, ITooltipFlag flagIn){
        ensureChargesNBT(stack);

        if(stack.getTag() != null) {
            tooltip.add(new StringTextComponent(
                    getTooltip("mineral_hoe_charges", TextFormatting.GOLD).getFormattedText()
                            + TextFormatting.GREEN + getCharges(stack)
            ));

            tooltip.add(new StringTextComponent(""));
        }

        setDescriptiveTooltip(tooltip, this);
    }

    // ***********
    // Charges NBT
    // ***********

    /**
     * Ensures that the charges NBT tag and
     * value is set on the given ItemStack.
     *
     * <p/>This should ALWAYS be called before
     * interacting with the charges NBT.
     *
     * @param stack the given ItemStack.
     */
    private static void ensureChargesNBT(ItemStack stack){
        CompoundNBT nbt = stack.getTag();

        if(nbt == null){
            LOG.info("Setting NBT tag on a Mineral Hoe item stack...");
            nbt = new CompoundNBT();
            nbt.putInt(NBT_TAG_CHARGES, (Math.min(CONFIG.getInitialCharges(), 2)));
            stack.setTag(nbt);
        }
    }

    private static int getCharges(ItemStack stack){
        ensureChargesNBT(stack);

        CompoundNBT tag = stack.getTag();
        return tag == null ? -1 : tag.getInt(NBT_TAG_CHARGES);
    }

    private static boolean setCharges(ItemStack stack, int charges){
        ensureChargesNBT(stack);

        CompoundNBT tag = stack.getTag();
        if(tag == null)
            return false;

        tag.putInt(NBT_TAG_CHARGES, charges);
        stack.setTag(tag);
        return true;
    }

    private static boolean incrementCharges(ItemStack stack){
        int charges = getCharges(stack);

        if(charges >= CONFIG.getMaxCharges())
            return false;

        return setCharges(stack, charges + 1);
    }

    @SuppressWarnings("UnusedReturnValue")
    private static boolean decrementCharges(ItemStack stack){
        int charges = getCharges(stack);

        if(charges <= 0)
            return false;

        return setCharges(stack, charges - 1);
    }

    // ****************
    // MC Action Events
    // ****************

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        boolean success;

        if(context.getPlayer() == null) {
            success = false;
        } else if(context.getPlayer().isSneaking()) {
            success = onBlockShiftClick(
                    context.getWorld(), context.getPos(), context.getWorld().getBlockState(context.getPos()),
                    context.getItem(), context.getPlayer(), context.getFace()
            );
        } else {
            success = onBlockClick(
                    context.getWorld(), context.getPos(), context.getWorld().getBlockState(context.getPos()),
                    context.getItem(), context.getPlayer(), context.getFace()
            );
        }

        return (success) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        boolean success;

        if(player.isSneaking()){
            success = onItemShiftClick(world, player, stack);
        } else {
            success = onItemClick(world, player, stack);
        }

        return ActionResult.newResult((success ? ActionResultType.SUCCESS : ActionResultType.FAIL), stack);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        return true;
    }

    // **********************
    // Specific Action Events
    // **********************

    private boolean onBlockClick(World world, BlockPos pos, BlockState block, ItemStack item, PlayerEntity player,
                                 Direction face){
        boolean tilled = tryTillBlock(world, pos, block, item, player, face);

        if(!tilled)
            return tryGetInfo(world, pos, block, player);

        return true;
    }

    @SuppressWarnings("unused")
    private boolean onBlockShiftClick(World world, BlockPos pos, BlockState block, ItemStack item, PlayerEntity player,
                                      Direction face){
        if(player.isCreative())
            if(tryGrowPlant(world, block, pos))
                return true;

        return tryCharge(world, player, item);
    }

    private boolean onItemClick(World world, PlayerEntity player, ItemStack item){
        return tryCharge(world, player, item);
    }

    private boolean onItemShiftClick(World world, PlayerEntity player, ItemStack item){
        boolean charged = tryCharge(world, player, item);

        if(!charged)
            displayCharges(world, player, item);

        return true;
    }

    // ********
    // Charging
    // ********

    private boolean tryCharge(World world, PlayerEntity player, ItemStack item){
        int charges = getCharges(item);

        if(charges >= CONFIG.getMaxCharges()){
            return false;
        }

        if(player.isCreative() || tryTakeCrystal(player)){
            if(incrementCharges(item)){
                EffectsUtil.playNormalSound(
                        world, player, player.getPosition(),
                        SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.BLOCKS
                );

                displayCharges(world, player, item);
                return true;
            }
        }

        return false;
    }

    private boolean tryTakeCrystal(PlayerEntity player){
        for(ItemStack itemStack : player.container.getInventory()){
            if(itemStack.getItem() == ResynthItems.ITEM_MINERAL_CRYSTAL){
                itemStack.shrink(1);
                return true;
            }
        }

        return false;
    }

    private void displayCharges(World world, PlayerEntity player, ItemStack item){
        if(!world.isRemote)
            return;

        player.sendMessage(new StringTextComponent(
                getTooltip("mineral_hoe_charges", TextFormatting.GOLD).getFormattedText()
                        + TextFormatting.GREEN + getCharges(item)
        ));
    }

    // *******
    // Tilling
    // *******

    private boolean tryTillBlock(World world, BlockPos pos, BlockState state, ItemStack item,
                                 PlayerEntity player, Direction face){

        //Check for dirt and grass.
        if(!(state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.GRASS_BLOCK))
            return false;

        if (!(face != Direction.DOWN && world.isAirBlock(pos.up()))) {
            return false;
        }

        //Check for creative and charges.
        if(!(player.isCreative() || getCharges(item) > 0)){
            if(CONFIG.playFailSound())
                EffectsUtil.playNormalSound(
                        world, player, pos, SoundEvents.BLOCK_NOTE_BLOCK_HAT, SoundCategory.BLOCKS
                );
            return false;
        }

        //If all checks pass

        //Replace
        boolean replaced = tillBlock(world, pos, player);

        //Finally remove charge if replaced.
        if(replaced && !player.isCreative()){
            decrementCharges(item);
        }

        return replaced;
    }

    private boolean tillBlock(World world, BlockPos pos, PlayerEntity player){
        boolean replaced = world.setBlockState(pos, ResynthBlocks.BLOCK_MINERAL_SOIL.getDefaultState());

        if(replaced){
            //Particles
            if(CONFIG.showParticles())
                EffectsUtil.displayStandardEffects(world, pos.up(), 5, ParticleTypes.FLAME);

            //Sound
            EffectsUtil.playNormalSound(
                    world, player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS
            );
        }

        return replaced;
    }

    // ********************
    // Information Provider
    // ********************
    //TODO: Locale for this

    private boolean tryGetInfo(World world, BlockPos pos, BlockState block, PlayerEntity player){
        if(!(block.getBlock() instanceof BlockInfoProvider))
            return false;

        //We want this done on the server side to get correct information.
        if(world.isRemote)
            return false;

        BlockInfoProvider infoProvider = (BlockInfoProvider)block.getBlock();
        player.sendMessage(new StringTextComponent(
                getInfoFromProvider(infoProvider, world, pos, block)
        ));

        return true;
    }

    private String getInfoFromProvider(BlockInfoProvider provider, World world, BlockPos pos, BlockState block){
        List<String> informationList = new ArrayList<>();
        provider.appendBlockInformation(informationList, world, pos, block);

        StringBuilder informationText = new StringBuilder();

        for(String informationLine : informationList){
            informationText.append(informationLine).append("\n");
        }

        return informationText.toString();
    }

    // ************
    // Plant Growth
    // ************

    /**
     * Attempts to grow a plant similar to bonemeal,
     * however it ignores {@link IGrowable#canGrow(IBlockReader, BlockPos, BlockState, boolean)}
     * and {@link IGrowable#canUseBonemeal(World, Random, BlockPos, BlockState)}.
     *
     * @return {@code true} if the plant could be grown.
     */
    private boolean tryGrowPlant(World world, BlockState block, BlockPos pos){
        if(block.getBlock() instanceof IGrowable){
            if(!world.isRemote)
                ((IGrowable)block.getBlock()).grow(world, random, pos, world.getBlockState(pos));
            return true;
        }

        return false;
    }
}
