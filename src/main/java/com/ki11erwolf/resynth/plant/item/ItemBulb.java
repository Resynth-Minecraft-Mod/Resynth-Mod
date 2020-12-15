/*
 * Copyright 2018-2020 Ki11er_wolf
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
package com.ki11erwolf.resynth.plant.item;

import com.ki11erwolf.resynth.ResynthTabs;
import com.ki11erwolf.resynth.item.ResynthItem;
import com.ki11erwolf.resynth.plant.block.BlockBiochemicalPlant;
import com.ki11erwolf.resynth.plant.set.PlantSet;
import com.ki11erwolf.resynth.util.EffectsUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;

/**
 * The produce item for Biochemical plants.
 * <p/>
 * Can be used (right-click) to fire a
 * {@link ForgeEventFactory#onPlayerDestroyItem(PlayerEntity, ItemStack, Hand)}
 * event which is handled in the plant set seed hooks
 * when distributing seeds. This is done for consistency
 * with seed hooks and plant sets.
 */
public class ItemBulb extends ResynthItem<ItemBulb> {

    /**
     * Prefix for all bulb items.
     */
    private static final String PREFIX = "bulb";

    private final PlantSet<BlockBiochemicalPlant, EntityType<?>> parentSet;

    public ItemBulb(PlantSet<BlockBiochemicalPlant, EntityType<?>> parentSet) {
        super(parentSet.getSetTypeName() + "_" + PREFIX + "_" + parentSet.getSetName(), ResynthTabs.TAB_RESYNTH_PRODUCE);
        this.parentSet = parentSet;
    }

    // ************
    // Active (Use)
    // ************

    /**
     * {@inheritDoc}
     * <p/>
     * Handles what happens when the player finishes
     * using the item - fires a
     * {@link ForgeEventFactory#onPlayerDestroyItem(PlayerEntity, ItemStack, Hand)} event.
     */
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entityLiving) {
        if(world.isRemote)
            playSmashSound(world, entityLiving);

        if(!(entityLiving instanceof ServerPlayerEntity))
            return stack;

        if(!((PlayerEntity)entityLiving).isCreative())
            stack.shrink(1);

        //Fire event so we can handle it in seed hooks.
        ForgeEventFactory.onPlayerDestroyItem((PlayerEntity) entityLiving, stack, Hand.MAIN_HAND);
        return stack;
    }

    /**
     * Plays the smashing sound when a bulb
     * is smashed in the world.
     */
    private static void playSmashSound(World world, LivingEntity player){
        if(player instanceof PlayerEntity)
            EffectsUtil.playNormalSoundWithPitchInRage(
                    world, (PlayerEntity)player, SoundEvents.BLOCK_GLASS_BREAK,
                    SoundCategory.PLAYERS, 0.8F, 0.4F
            );
    }

    /**
     * {@inheritDoc}
     * @return {@code 10}
     */
    public int getUseDuration(ItemStack stack) {
        return 10;
    }

    /**
     * {@inheritDoc}
     * @return {@link UseAction#BOW}.
     */
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }

    /**
     * Constructs the tooltip for the item.
     */
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn){
        addPlantItemTooltips(tooltip, PREFIX, parentSet);
    }
}
