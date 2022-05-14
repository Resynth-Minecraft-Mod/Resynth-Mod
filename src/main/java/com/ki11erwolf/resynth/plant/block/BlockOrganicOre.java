/*
 * Copyright 2018-2021 Ki11er_wolf
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
package com.ki11erwolf.resynth.plant.block;

import com.ki11erwolf.resynth.ResynthTabs;
import com.ki11erwolf.resynth.block.ResynthBlock;
import com.ki11erwolf.resynth.plant.set.PlantSet;
import com.ki11erwolf.resynth.util.MinecraftUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * The produce type for Metallic plants - a simple
 * block used as a furnace recipe, info provider,
 * and method of obtaining seeds.
 */
public class BlockOrganicOre extends ResynthBlock<BlockOrganicOre> {

    /**
     * The prefix for all blocks of this type.
     */
    private static final String PREFIX = "organic_ore";

    private final PlantSet<BlockMetallicPlant, Block> parentSet;

    public BlockOrganicOre(PlantSet<BlockMetallicPlant, Block> parentSet) {
        super(
                Block.Properties.of(Material.DIRT).harvestTool(ToolType.AXE)
                .strength(2),
                new Item.Properties().tab(ResynthTabs.TAB_RESYNTH_PRODUCE),
                parentSet.getSetTypeName() + "_" + PREFIX + "_" + parentSet.getSetName()
        );
        this.parentSet = parentSet;
    }

    /**
     * Constructs the tooltip for the block.
     */
    @Override
    public void appendHoverText(ItemStack stack, IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag){
        BlockPlant.addPlantItemBlockTooltips(tooltip, PREFIX, parentSet);
    }

    /**
     * {@inheritDoc}
     *
     * Handles dropping the block in the world
     * when it is broken.
     */
    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public void spawnAfterBreak(BlockState state, ServerWorld worldIn, BlockPos pos, ItemStack stack) {
        MinecraftUtil.spawnItemStackInWorld(new ItemStack(state.getBlock()), worldIn, pos);
    }
}
