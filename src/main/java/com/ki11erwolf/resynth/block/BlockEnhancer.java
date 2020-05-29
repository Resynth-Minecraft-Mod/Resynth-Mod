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
package com.ki11erwolf.resynth.block;

import com.ki11erwolf.resynth.util.ExpandingTooltip;
import com.ki11erwolf.resynth.util.Tooltip;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;

import static com.ki11erwolf.resynth.util.Tooltip.newBlankLine;

/**
 * An enhancer block that can be placed underneath Mineral
 * Soil to increase it's Mineral Content beyond 50%.
 */
public class BlockEnhancer extends ResynthBlock<BlockEnhancer>{

    /**
     * Name appended to all enhancer blocks.
     */
    private static final String BLOCK_NAME = "enhancer";

    /**
     * The amount to increase the Mineral Soil content
     * by.
     */
    private final float increase;

    /**
     * The number of stages to increase the
     * Mineral Soil block growth stage by.
     */
    private final int stageIncrease;

    /**
     * Creates a new enhancer block.
     *
     * @param name the name of the enhancer block.
     * @param increase The amount to increase the Mineral Soil content by
     * @param stageIncrease The number of stages to increase the Mineral Soil block growth stage by.
     */
    BlockEnhancer(String name, float increase, int stageIncrease) {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(2), name + "_" + BLOCK_NAME);

        this.increase = increase;
        this.stageIncrease = stageIncrease;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn) {
        new ExpandingTooltip().setCtrlForDescription(
                tooltips -> Tooltip.addBlankLine(tooltips).add(getDescriptiveTooltip(
                        this.getRegistryName() == null ? "" : this.getRegistryName().getPath(), increase
                ))
        ).write(tooltip).add(newBlankLine());
    }

    /**
     * {@inheritDoc}
     *
     * @return The tool needed to break this block: Pickaxe.
     */
    @Nullable
    @Override
    public ToolType getHarvestTool(BlockState state) {
        return ToolType.PICKAXE;
    }

    /**
     * @return the amount to increase the Mineral Soil content
     * by.
     */
    public float getIncrease() {
        return increase;
    }

    /**
     * @return the number of stages to increase the
     * Mineral Soil block growth stage by.
     */
    int getStageIncrease() {
        return stageIncrease;
    }
}
