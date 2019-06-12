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
package com.ki11erwolf.resynth.plant.item;

import com.ki11erwolf.resynth.ResynthConfig;
import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.ResynthTabs;
import com.ki11erwolf.resynth.item.ResynthItem;
import com.ki11erwolf.resynth.plant.PlantSetBiochemical;
import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The produce item for biochemical plants.
 */
public abstract class ItemPlantProduceBulb extends ResynthItem {

    /**
     * Prefix for the item.
     */
    //TODO: Change in 1.13
    private static final String PREFIX = "mobproduce";

    /**
     * The seed item to drop when this item is thrown.
     */
    private final ItemPlantSeeds plantSeeds;

    /**
     * The biochemical plant set that created
     * this item.
     */
    private final PlantSetBiochemical plantSet;

    /**
     * Default item constructor.
     *
     * @param name the name of the item.
     * @param seeds the seeds item type this produce drops.
     * @param set the biochemical plant set this item belongs to.
     */
    public ItemPlantProduceBulb(String name, ItemPlantSeeds seeds, PlantSetBiochemical set) {
        super(new Properties().group(ResynthTabs.TAB_RESYNTH_PRODUCE), name, PREFIX);
        this.plantSet = set;
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn){
        tooltip.add(stringToTextComponent("Can be thrown for a chance at getting more seeds."));
        tooltip.add(stringToTextComponent("Can be smelted to obtain the resource."));

        tooltip.add(stringToTextComponent(""));

        tooltip.add(stringToTextComponent(
                TextFormatting.GOLD
                        + "Seed Drop Chance (Mob): " +
                        plantSet.getTextualMobSeedDropChance()
        ));

        tooltip.add(stringToTextComponent(
                TextFormatting.GREEN
                        + "Seed Drop Chance (Produce): "
                        + plantSet.getTextualProduceSeedDropChance()
        ));

        tooltip.add(stringToTextComponent(
                TextFormatting.AQUA
                        + "Seed Drop Chance (Mystical Seed Pod): "
                        + plantSet.getTextualPodSeedDropChance()
        ));

        tooltip.add(stringToTextComponent(
                TextFormatting.RED
                        + "Resource Count (Smelting): x"
                        + plantSet.getResult().getCount()
        ));

        tooltip.add(stringToTextComponent(
                TextFormatting.DARK_PURPLE
                        + "Plant Growth Chance: "
                        + plantSet.getTextualPlantGrowthChance()
        ));
    }

    /**
     * {@inheritDoc}
     *
     * Throws the item when the player uses it.
     *
     * @return Result Success
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
        ItemStack itemstack = player.getHeldItem(hand);

        if (!player.isCreative()){
            itemstack.shrink(1);
        }

        if(/*ResynthConfig.PLANTS_GENERAL.produceDropSeeds*/ true)//TODO: Config
            throwItem(world, player);

        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }

    /**
     * Creates a modified ender pearl entity that randomly spawns seeds
     * and throws it.
     *
     * @param worldIn -
     * @param playerIn -
     */
    protected void throwItem(World worldIn, EntityPlayer playerIn){
        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ,
                SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.PLAYERS,
                0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F)
        );

        if (!worldIn.isRemote){
            EntityEnderPearl ep = new EntityEnderPearl(worldIn, playerIn){

                @Override
                protected void onImpact(RayTraceResult result){
                    //Generic catch of all code as
                    //Minecraft likes to crash from this randomly
                    try{
                        if(MathUtil.chance(getSeedSpawnChance())) {
                            ItemPlantSeeds.addEffects(worldIn, result.getBlockPos());
                            worldIn.spawnEntity(new EntityItem(worldIn,
                                    result.getBlockPos().getX(),
                                    result.getBlockPos().getY(),
                                    result.getBlockPos().getZ(),
                                    new ItemStack(plantSeeds)));
                        }
                    } catch (Exception e){
                        ResynthMod.getLogger().error("Failed to spawn seeds from bulb entity", e);
                    } finally {
                        try{
                            this.remove();
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
     * <b>MUST OVERRIDE</b>. This is how the produce
     * item knows the seeds spawn chance from config.
     *
     * @return the chance seeds will spawn from throwing a mob
     * produce item (0.0F - 100.0F).
     */
    protected abstract float getSeedSpawnChance();
}