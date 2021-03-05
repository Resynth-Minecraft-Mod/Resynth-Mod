/*
 * Copyright (c) 2018 - 2021 Ki11er_wolf.
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

import com.ki11erwolf.resynth.ResynthTabs;
import com.ki11erwolf.resynth.util.ExpandingTooltip;
import com.ki11erwolf.resynth.util.Tooltip;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.ki11erwolf.resynth.util.Tooltip.newBlankLine;

/**
 *
 *
 * <p/> Created as a workaround for Blocks like {@link Blocks#OBSIDIAN}
 * and {@link Blocks#ANCIENT_DEBRIS} which cannot otherwise cannot be
 * made into Metallic PlantSets because TNT does not affect them.
 */
public class DamagedBlock<B extends Block> extends ResynthBlock<DamagedBlock<B>> {

    /**
     * The String prefix appended to the beginning of the blocks registry name.
     */
    protected static final String PREFIX = "damaged";

    /**
     * A low explosion resistance (same as stone) passed
     * to {@link #getExplosionResistance()}.
     */
    public static final float EXPLOSION_RESISTANCE = 6.0F;

    /**
     * The original {@link Block} that this imitation {@link Block} is crafted from and into.
     */
    public final B original;

    /**
     * Creates a new {@link DamagedBlock} that will mimic
     * the given {@code impersonation} block.
     *
     * @param impersonate the block to impersonate and mimic.
     */
    public DamagedBlock(B impersonate) {
        this(impersonate, 0);
    }

    /**
     * Creates a new {@link DamagedBlock} that will mimic
     * the given {@code impersonation} block.
     *
     * @param impersonate the block to impersonate and mimic.
     */
    public DamagedBlock(B impersonate, float hardness) {
        super(
                AbstractBlock.Properties.create( // Material
                        Objects.requireNonNull(impersonate).getDefaultState().getMaterial()
                ).sound( // Sound
                        impersonate.getDefaultState().getSoundType()
                ).hardnessAndResistance( // Hardness & Resistance
                        Math.max(hardness, 40), EXPLOSION_RESISTANCE
                ).harvestTool( // Tool
                        ToolType.PICKAXE
                ).harvestLevel( // Tool Level
                        impersonate.getDefaultState().getHarvestLevel()
                ).setRequiresTool(),
                new Item.Properties().group( // Item Group
                        ResynthTabs.TAB_RESYNTH
                ), // Name
                PREFIX + "_" + Objects.requireNonNull(impersonate.getRegistryName()).getPath()
        );

        this.original = impersonate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String originalName = TextFormatting.GRAY + original.getTranslatedName().getString() + TextFormatting.DARK_GRAY;

        new ExpandingTooltip().setCtrlForDescription(
                tooltips -> Tooltip.addBlankLine(tooltips).addAll(
                        Arrays.asList(Tooltip.formatLineFeeds(
                                getDescriptiveTooltip(PREFIX, originalName), TextFormatting.DARK_GRAY
                        ))
                )
        ).write(tooltip).add(newBlankLine());
    }

    /**
     * Always returns a low explosion resistance, making the block
     * easily destroyed by TNT.
     *
     * @return {@link #EXPLOSION_RESISTANCE a constant weak explosion
     * resistance}.
     */
    @Override
    @SuppressWarnings("deprecation")
    public float getExplosionResistance() {
        return EXPLOSION_RESISTANCE;
    }

}
