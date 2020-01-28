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
package com.ki11erwolf.resynth.item;

import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.MineralCrystalConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The item class for the Mineral Crystal item.
 *
 * <p/>This class exists mainly to provide the
 * "emergency" dirt/grass to Mineral Soil
 * conversion functionality.
 */
class ItemMineralCrystal extends ResynthItem<ItemMineralCrystal> {

    /**
     * Configuration settings for this item class.
     */
    private static final MineralCrystalConfig CONFIG
            = ResynthConfig.GENERAL_CONFIG.getCategory(MineralCrystalConfig.class);

    /**
     * The name of the item given to its constructor.
     */
    private final String name;

    /**
     * Constructs a new Mineral Crystal item.
     *
     * @param name the registry name of the item.
     */
    ItemMineralCrystal(String name) {
        super(name);
        this.name = name;
    }

    /**
     * {@inheritDoc}.
     *
     * Sets the tooltip for this item from the lang files.
     * Will also display a message if emergency conversion
     * mode is enabled from the config.
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn) {
        if(CONFIG.isECMEnabled())
            tooltip.add(getFormattedTooltip(name + ".ecm", TextFormatting.RED));

        super.addInformation(stack, world, tooltip, flagIn);
    }

    /**
     * Called when the item is used (right-clicked) while
     * targeting a block.
     *
     * <p/>This is used, when enabled, to turn dirt/grass
     * DIRECTLY into Mineral Soil WITHOUT ANYTHING ELSE.
     * That is, this method will JUST do the conversion,
     * without any extras that are not required to complete
     * the task.
     *
     * <p/>This method is used in cases where the Mineral Hoe
     * is broken and Mineral Soil cannot otherwise be obtained.
     *
     * <p/>Which is why it is VITAL that this method simply performs
     * the task at hand and does not attempt anything else.
     *
     * @param context the context in which the item was used.
     * @return the result of using the item (pass/fail).
     */
    public ActionResultType onItemUse(ItemUseContext context) {
        ActionResultType result;

        //Do change if can
        if(CONFIG.isECMEnabled()){
            World world = context.getWorld();
            BlockPos pos = context.getPos();
            BlockState source = world.getBlockState(pos);

            //Do change
            result = doChange(world, pos, source);
        } else result = ActionResultType.FAIL;

        //If the block was replaced and the player is in survival,
        //shrink the item stack used to do the replace
        if(result == ActionResultType.SUCCESS)
            if(context.getPlayer() != null && !context.getPlayer().isCreative())
                context.getItem().shrink(1);

        return result;
    }

    /**
     * An extremely minimal way of turning dirt or grass
     * into Mineral Soil. This simply does the block change
     * if it can, otherwise it returns.
     *
     * @param world the world.
     * @param pos the block position of the block to replace.
     * @param source the block we're replacing.
     * @return {@link ActionResultType#SUCCESS} if the block
     * was replaced, {@link ActionResultType#FAIL} if the block
     * was NOT replaced.
     */
    private ActionResultType doChange(World world, BlockPos pos, BlockState source){
        //If block is grass or dirt.
        if(source.getBlock() == Blocks.DIRT || source.getBlock() == Blocks.GRASS_BLOCK){
            //Change block to mineral soil.
            world.setBlockState(pos, ResynthBlocks.BLOCK_MINERAL_SOIL.getDefaultState());
            return ActionResultType.SUCCESS;
        }

        //Else do nothing
        return ActionResultType.FAIL;
    }
}
