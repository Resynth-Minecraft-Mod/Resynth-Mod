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
import com.ki11erwolf.resynth.plant.set.PlantSet;
import com.ki11erwolf.resynth.util.EffectsUtil;
import net.minecraft.advancements.CriteriaTriggers;
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
import net.minecraft.world.IBlockReader;
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

    private final PlantSet<?, ?> parentSet;

    public ItemSeeds(PlantSet<?, ?> plantSet){
        super(plantSet.getSetTypeName() + "_" + PREFIX + "_" + plantSet.getSetName(), ResynthTabs.TAB_RESYNTH_SEEDS);
        this.parentSet = plantSet;
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
        //Don't plant broken plants
        if(parentSet.isBroken())
            return ActionResultType.FAIL;

        //Get vars
        World world = context.getWorld();
        BlockPos blockpos = context.getPos().up();

        //If valid position to plant
        if (context.getFace() == Direction.UP && world.isAirBlock(blockpos)
                && this.parentSet.getPlantBlock().getDefaultState().isValidPosition(world, blockpos)) {

            //Set block in world
            world.setBlockState(blockpos, this.parentSet.getPlantBlock().getDefaultState(), 11);
            ItemStack itemstack = context.getItem();
            PlayerEntity playerEntity = context.getPlayer();

            //Trigger block placed event
            if (playerEntity instanceof ServerPlayerEntity) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockpos, itemstack);
            }

            //Shrink seeds count
            itemstack.shrink(1);

            //Play plant sound.
            EffectsUtil.playNormalSound(
                    world, playerEntity, blockpos, SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS
            );

            return ActionResultType.SUCCESS;
        } else return ActionResultType.FAIL;
    }

    /**
     * Constructs the tooltip for the item.
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
        addPlantItemTooltips(tooltip, String.format("%s_%s", parentSet.getSetTypeName(), PREFIX), parentSet);
    }

    /**
     * @return the plant type (plant block) this seeds item type (instance) spawns.
     */
    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        return parentSet.getPlantBlock().getDefaultState();
    }
}
