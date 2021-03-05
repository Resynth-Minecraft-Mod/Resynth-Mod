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

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.ki11erwolf.resynth.util.Tooltip.newBlankLine;

/**
 * A special type of {@link Block} used to obtain Seeds from Blocks
 * which otherwise don't allow it.
 *
 * <p> Created as a workaround for Blocks like {@link Blocks#OBSIDIAN}
 * and {@link Blocks#ANCIENT_DEBRIS} which cannot otherwise cannot be
 * made into Metallic PlantSets because TNT does not affect them.
 */
public class DamagedBlock<B extends Block> extends ResynthBlock<DamagedBlock<B>> {

    /**
     * The default resistance of {@link DamagedBlock}s.
     * Similar to ores and rocks.
     */
    public static final float DEFAULT_EXPLOSION_RESISTANCE = 6.0F;

    /**
     * The minimum resistance a {@link DamagedBlock} will
     * ever go.
     */
    public static final float MINIMUM_EXPLOSION_RESISTANCE = 1.0F;

    /**
     * The maximum resistance a {@link DamagedBlock} will
     * ever go.
     */
    public static final float MAXIMUM_EXPLOSION_RESISTANCE = 12.0F;

    /**
     * The String prefix appended to the beginning of the blocks registry name.
     */
    protected static final String PREFIX = "damaged";

    /**
     * The wrapped {@link Block} and basis to this Block,
     * which defines how this Block looks and acts on
     * a most basic level.
     */
    private final B basis;

    /**
     * @param of the {@link Block} to create a damaged version of.
     */
    public DamagedBlock(B of) {
        this(of, DEFAULT_EXPLOSION_RESISTANCE);
    }

    /**
     * @param of the {@link Block} to create a damaged version of.
     */
    public DamagedBlock(B of, float resistance) {
        //noinspection ConstantConditions
        super(
                AbstractBlock.Properties.from(Objects.requireNonNull(of)).hardnessAndResistance(
                        of.getDefaultState().getBlockHardness(null, null),
                        Math.max(MINIMUM_EXPLOSION_RESISTANCE, Math.min(resistance, MAXIMUM_EXPLOSION_RESISTANCE))
                ),
                new Item.Properties().group(
                        ResynthTabs.TAB_RESYNTH
                ),
                String.join(
                        "_", PREFIX, Objects.requireNonNull(of.getRegistryName()).getPath()
                )
        );

        this.basis = of;
    }

    /**
     * @return the {@link Block} this damaged version is based off of.
     */
    public final B of() {
        return this.basis;
    }

    /**
     * Specifies how resistant this {@link Block} is to {@link net.minecraft.block.TNTBlock}
     * and other kinds of explosions.
     *
     * <p> The resistance is guaranteed to be low, allowing this Block to be used as a
     * resource for obtaining Metallic Seeds.
     *
     * @return a resistance to explosions that is guaranteed to be relatively low and in the
     * range of {@link #MINIMUM_EXPLOSION_RESISTANCE} and {@link #MAXIMUM_EXPLOSION_RESISTANCE}.
     */
    @Override
    @SuppressWarnings("deprecation")
    public float getExplosionResistance() {
        float resistance = super.getExplosionResistance();
        return Math.max(MINIMUM_EXPLOSION_RESISTANCE, Math.min(resistance, MAXIMUM_EXPLOSION_RESISTANCE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String originalName = TextFormatting.GRAY + basis.getTranslatedName().getString() + TextFormatting.DARK_GRAY;

        new ExpandingTooltip().setCtrlForDescription(
                tooltips -> Tooltip.addBlankLine(tooltips).addAll(
                        Arrays.asList(Tooltip.formatLineFeeds(
                                getDescriptiveTooltip(PREFIX, originalName), TextFormatting.DARK_GRAY
                        ))
                )
        ).write(tooltip).add(newBlankLine());
    }
}
