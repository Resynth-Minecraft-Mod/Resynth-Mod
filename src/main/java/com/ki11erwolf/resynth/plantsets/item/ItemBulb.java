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
package com.ki11erwolf.resynth.plantsets.item;

import com.ki11erwolf.resynth.ResynthTabs;
import com.ki11erwolf.resynth.item.ResynthItem;
import com.ki11erwolf.resynth.plantsets.set.IBiochemicalSetProperties;
import com.ki11erwolf.resynth.plantsets.set.PlantSetUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * The produce item for Biochemical plants.
 * <p/>
 * Can be used (right-click) to fire a
 * {@link ForgeEventFactory#onPlayerDestroyItem(EntityPlayer, ItemStack, EnumHand)}
 * event which is handled in the plant set seed hooks
 * when distributing seeds. This is done for consistency
 * with seed hooks and plant sets.
 */
public class ItemBulb extends ResynthItem<ItemBulb> {

    /**
     * Prefix for all bulb items.
     */
    private static final String PREFIX = "bulb";

    /**
     * The name of the set this produce item
     * type is for (e.g. string).
     */
    private final String setName;

    /**
     * The properties specific to the plant set
     * this produce item is registered to.
     */
    private final IBiochemicalSetProperties properties;

    /**
     * @param setTypeName the name of the plant set type (e.g. crystalline).
     * @param setName the name of the plant set (e.g. diamond).
     * @param properties the properties specific to the plant set.
     */
    public ItemBulb(String setTypeName, String setName, IBiochemicalSetProperties properties) {
        super(setTypeName + "_" + PREFIX + "_" + setName, ResynthTabs.TAB_RESYNTH_PRODUCE);
        this.setName = setName;
        this.properties = properties;
    }

    /**
     * Constructs the tooltip for the item.
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn){
        PlantSetUtil.PlantSetTooltipUtil.setPropertiesTooltip(tooltip, properties);
        setDescriptiveTooltip(tooltip, PREFIX, setName);
    }

    // ************
    // Active (Use)
    // ************

    /**
     * {@inheritDoc}
     * <p/>
     * Handles what happens when the player finishes
     * using the item - fires a
     * {@link ForgeEventFactory#onPlayerDestroyItem(EntityPlayer, ItemStack, EnumHand)} event.
     */
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        playSmashSound(world, entityLiving);

        if(!(entityLiving instanceof EntityPlayer))
            return stack;

        if(!((EntityPlayer)entityLiving).isCreative())
            stack.shrink(1);

        //Fire event so we can handle it in seed hooks.
        ForgeEventFactory.onPlayerDestroyItem((EntityPlayer) entityLiving, stack, EnumHand.MAIN_HAND);
        return stack;
    }

    /**
     * Plays the smashing sound when a bulb
     * is smashed in the world.
     */
    @OnlyIn(Dist.CLIENT)
    private static void playSmashSound(World world, EntityLivingBase player){
        world.playSound(null, player.posX, player.posY, player.posZ,
                SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS,
                0.4F, 0.4F / (new Random().nextFloat() * 0.4F + 0.8F)
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
     * @return {@link EnumAction#SPEAR}.
     */
    public EnumAction getUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    /**
     * {@inheritDoc}.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }
}
