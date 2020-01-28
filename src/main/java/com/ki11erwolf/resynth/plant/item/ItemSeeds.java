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
import com.ki11erwolf.resynth.plant.set.PlantSetProperties;
import com.ki11erwolf.resynth.plant.set.PlantSetUtil;
import com.ki11erwolf.resynth.util.EffectsUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The seeds item for every Growable resynth plant type (plant block).
 * Handles placement of the plant type as well as displays a tooltip
 * with the properties (e.g. growth chance) of the plant type.
 */
public class ItemSeeds extends ResynthItem<ItemSeeds> implements IPlantable {

    /**
     * The prefix for all seeds items.
     */
    private static final String PREFIX = "seeds";

    /**
     * The specific plant type (plant block)
     * this seeds item type will spawn.
     */
    private final BlockState plant;

    /**
     * The name of the set this seeds item
     * type is for (e.g. diamond).
     */
    private final String setName;

    /**
     * The name of the plant set type
     * this seeds item is for.
     */
    private final String setTypeName;

    /**
     * The properties specific to the plant set
     * this seeds item type is registered to.
     */
    private final PlantSetProperties setProperties;

    /**
     * {@code true} if the plant set this seeds
     * item belongs to was flagged as a failure.
     */
    private boolean isFailure = false;

    /**
     * @param setType the name of the plant set type (e.g. crystalline).
     * @param setName the name of the plant set (e.g. diamond).
     * @param plant the specific plant type (plant block instance) to place.
     * @param setProperties the properties specific to the plant set.
     */
    public ItemSeeds(String setType, String setName, Block plant, PlantSetProperties setProperties){
        super(setType + "_" + PREFIX + "_" + setName, ResynthTabs.TAB_RESYNTH_SEEDS);
        this.setTypeName = setType;
        this.setProperties = setProperties;
        this.setName = setName;
        this.plant = plant.getDefaultState();
    }

    /**
     * Handles placing the plant type (plant block
     * instance) in the world.
     *
     * @return {@link ActionResultType#SUCCESS} if the plant
     * was placed, {@link ActionResultType#FAIL} otherwise.
     */
    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if(isFailure)
            return ActionResultType.FAIL;

        IWorld world = context.getWorld();
        BlockPos blockpos = context.getPos().up();

        if (context.getFace() == Direction.UP && world.isAirBlock(blockpos)
                && this.plant.isValidPosition(world, blockpos)) {

            world.setBlockState(blockpos, this.plant, 11);
            ItemStack itemstack = context.getItem();
            PlayerEntity playerEntity = context.getPlayer();

            if (playerEntity instanceof ServerPlayerEntity) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockpos, itemstack);
            }

            itemstack.shrink(1);

            EffectsUtil.playNormalSound(
                    world.getWorld(), playerEntity, blockpos, SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS
            );

            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.FAIL;
        }
    }

    /**
     * Constructs the tooltip for the item.
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn){
        PlantSetUtil.PlantSetTooltipUtil.setPropertiesTooltip(tooltip, setProperties);
        setDescriptiveTooltip(tooltip, setTypeName + "_" + PREFIX, setName);

        if(isFailure)
            tooltip.add(getFormattedTooltip(PREFIX + ".failure", TextFormatting.RED));
    }

    /**
     * {@inheritDoc}
     *
     * @return the plant type (plant block) this seeds item type (instance) spawns.
     */
    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        return plant;
    }

    /**
     * Flags this seeds item instance as a failure.
     * When declared as a failure, the seeds will
     * no longer be able to plant a block and will
     * will display the plant set failure message
     * in the tooltip.
     */
    public void flagAsFailure(){
        isFailure = true;
    }
}
