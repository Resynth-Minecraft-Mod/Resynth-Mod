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
package com.ki11erwolf.resynth.plant.item;

import com.ki11erwolf.resynth.ResynthConfig;
import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.ResynthTabProduce;
import com.ki11erwolf.resynth.item.ResynthItem;
import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The produce item for biochemical plants.
 */
public abstract class ItemPlantMobProduce extends ResynthItem {

    /**
     * Prefix for the item.
     */
    private static final String PREFIX = "mobproduce";

    /**
     * The seed item for the plant that produces this item.
     */
    private final ItemPlantSeed plantSeeds;

    /**
     * Constructor.
     *
     * @param name the name of the item.
     */
    public ItemPlantMobProduce(String name, ItemPlantSeed seeds) {
        super(name, PREFIX);
        this.setCreativeTab(ResynthTabProduce.RESYNTH_TAB_PRODUCE);
        this.plantSeeds = seeds;
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
                               ITooltipFlag flagIn) {
        tooltip.add("Throw for a chance at getting more seeds.");
        tooltip.add("Smelt to get the item.");
    }

    /**
     * Throws the item (modified ender pearl) when right clicked.
     *
     * @param worldIn -
     * @param playerIn -
     * @param handIn -
     * @return Result Success
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!playerIn.capabilities.isCreativeMode){
            itemstack.shrink(1);
        }

        if(ResynthConfig.PLANTS_GENERAL.produceDropSeeds)
            throwItem(worldIn, playerIn);

        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }

    /**
     * Creates a modified ender pearl entity that randomly spawns seeds
     * and spawns it.
     *
     * @param worldIn -
     * @param playerIn -
     */
    protected void throwItem(World worldIn, EntityPlayer playerIn){
        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ,
                SoundEvents.ENTITY_ENDERPEARL_THROW, SoundCategory.PLAYERS,
                0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F)
        );

        if (!worldIn.isRemote){
            EntityEnderPearl ep = new EntityEnderPearl(worldIn, playerIn){

                @Override
                protected void onImpact(RayTraceResult result){
                    //Generic catch of all code as
                    //Minecraft likes to crash from this randomly
                    try{
                        if(!worldIn.isRemote && MathUtil.chance(getSeedSpawnChance())) {
                            worldIn.spawnEntity(new EntityItem(worldIn,
                                    result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ(),
                                    new ItemStack(plantSeeds)));
                        }
                    } catch (Exception e){
                        ResynthMod.getLogger().error("Failed to spawn seeds from bulb entity", e);
                    } finally {
                        try{
                            this.setDead();
                        } catch (Exception e){
                            ResynthMod.getLogger().error("Failed to kill off bulb entity", e);
                        }
                    }
                }
            };

            ep.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw,
                    0.0F, 1.5F, 1.0F);

            worldIn.spawnEntity(ep);
        }
    }

    /**
     * <b>MUST OVERRIDE</b>
     *
     * @return the chance seeds will spawn from throwing a mob
     * produce item (0.0F - 100.0F).
     */
    protected abstract float getSeedSpawnChance();
}