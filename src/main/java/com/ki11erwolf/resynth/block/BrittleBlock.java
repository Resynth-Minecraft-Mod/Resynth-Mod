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

import com.ki11erwolf.resynth.ResynthRecipes;
import com.ki11erwolf.resynth.ResynthTabs;
import com.ki11erwolf.resynth.util.ExpandingTooltip;
import com.ki11erwolf.resynth.util.Tooltip;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.ki11erwolf.resynth.util.Tooltip.newBlankLine;

public class BrittleBlock<B extends Block> extends ResynthBlock<BrittleBlock<B>> {

    public static final float DEFAULT_EXPLOSION_RESISTANCE = 6.0F;

    public static final float MINIMUM_EXPLOSION_RESISTANCE = 1.0F;

    public static final float MAXIMUM_EXPLOSION_RESISTANCE = 12.0F;

    protected static final String PREFIX = "brittle";

    private final B block;

    public BrittleBlock(B of) {
        this(of, DEFAULT_EXPLOSION_RESISTANCE);
    }

    public BrittleBlock(B of, float resistance) {
        //noinspection ConstantConditions
        super(
                AbstractBlock.Properties.from(Objects.requireNonNull(of)).lootFrom(() -> of).hardnessAndResistance(
                        of.getDefaultState().getBlockHardness(null, null),
                        Math.max(MINIMUM_EXPLOSION_RESISTANCE, Math.min(resistance, MAXIMUM_EXPLOSION_RESISTANCE))
                ),
                new Item.Properties().group(
                        ResynthTabs.TAB_RESYNTH
                ),
                PREFIX + "_" +  Objects.requireNonNull(of.getRegistryName()).getPath()
        );

        this.block = of;
    }

    @Override
    @SuppressWarnings("deprecation")
    public float getExplosionResistance() {
        float resistance = super.blastResistance;
        return Math.max(MINIMUM_EXPLOSION_RESISTANCE, Math.min(resistance, MAXIMUM_EXPLOSION_RESISTANCE));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String originalName = TextFormatting.GRAY + block.getTranslatedName().getString() + TextFormatting.DARK_GRAY;

        new ExpandingTooltip().setCtrlForDescription(
                tooltips -> Tooltip.addBlankLine(tooltips).addAll(
                        Arrays.asList(Tooltip.formatLineFeeds(
                                getDescriptiveTooltip(PREFIX + "_block", originalName), TextFormatting.DARK_GRAY
                        ))
                )
        ).write(tooltip).add(newBlankLine());
    }

    @Override
    public ResynthBlock<BrittleBlock<B>> queueRegistration() {
        BrittleBlockRecipes.INSTANCE.addBrittleBlockRecipe(this);
        return super.queueRegistration();
    }

    public final B of() {
        return this.block;
    }

    public enum BrittleBlockRecipes implements ResynthRecipes.RecipeProvider {

        INSTANCE;

        private List<BrittleBlock<?>> brittleBlocks = new ArrayList<>();

        private List<IRecipe<?>> recipes;

        void addBrittleBlockRecipe(BrittleBlock<?> block) {
            brittleBlocks.add(block);
        }

        private void createRecipes() {
            recipes = new ArrayList<>();

            for(BrittleBlock<?> block : brittleBlocks) {
                recipes.add(ResynthRecipes.RecipeProvider.newShapelessRecipe(
                        Objects.requireNonNull(block.getRegistryName()).getPath()
                                + "_to_" + Objects.requireNonNull(block.of().getRegistryName()).getPath(),
                        "brittle_block_recipes", new ItemStack(block), block.of()
                ));

                recipes.add(ResynthRecipes.RecipeProvider.newShapelessRecipe(
                        Objects.requireNonNull(block.of().getRegistryName()).getPath()
                                + "_to_" + Objects.requireNonNull(block.getRegistryName()).getPath(),
                        "brittle_block_recipes", new ItemStack(block.of()), block
                ));
            }

            brittleBlocks = null;
        }

        @Override
        public IRecipe<?>[] get() {
            if(recipes == null)
                createRecipes();

            return recipes.toArray(new IRecipe[0]);
        }
    }
}
