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

/**
 * A simple block that can be destroyed by explosions which
 * is meant to wrap and mimic other blocks which cannot be,
 * such as Obsidian. The block wrapper and the original block
 * can also be converted to one another through a recipe.
 *
 * @param <B> the class of the block being wrapped.
 */
public class BrittleBlock<B extends Block> extends ResynthBlock<BrittleBlock<B>> {

    /**
     * The default resistance a BrittleBlock has to explosions.
     */
    public static final float DEFAULT_EXPLOSION_RESISTANCE = 6.0F;

    /**
     * The minimum explosion resistance allowed for BrittleBlocks.
     */
    public static final float MINIMUM_EXPLOSION_RESISTANCE = 1.0F;

    /**
     * The maximum explosion resistance allowed for BrittleBlocks.
     */
    public static final float MAXIMUM_EXPLOSION_RESISTANCE = 12.0F;

    /**
     * The prefix to all BrittleBlock registry names.
     */
    protected static final String PREFIX = "brittle";

    /**
     * A reference to the block being wrapped.
     */
    private final B block;

    /**
     * Creates a new BrittleBlock which wraps and mimics the given block.
     *
     * @param of the block to mimic.
     */
    public BrittleBlock(B of) {
        this(of, DEFAULT_EXPLOSION_RESISTANCE);
    }

    /**
     * Creates a new BrittleBlock which wraps and mimics the given block.
     *
     * @param resistance the explosion resistance of the created BrittleBlock.
     * @param of the block to mimic.
     */
    public BrittleBlock(B of, float resistance) {
        //noinspection ConstantConditions
        super(
                AbstractBlock.Properties.copy(Objects.requireNonNull(of)).lootFrom(() -> of).strength(
                        of.defaultBlockState().getDestroySpeed(null, null),
                        Math.max(MINIMUM_EXPLOSION_RESISTANCE, Math.min(resistance, MAXIMUM_EXPLOSION_RESISTANCE))
                ),
                new Item.Properties().tab(
                        ResynthTabs.TAB_RESYNTH
                ),
                PREFIX + "_" +  Objects.requireNonNull(of.getRegistryName()).getPath()
        );

        this.block = of;
    }

    /**
     * @return the resistance this particular block has to explosions.
     */
    @Override
    @SuppressWarnings("deprecation")
    public float getExplosionResistance() {
        float resistance = super.explosionResistance;
        return Math.max(MINIMUM_EXPLOSION_RESISTANCE, Math.min(resistance, MAXIMUM_EXPLOSION_RESISTANCE));
    }

    /**
     * Creates a tooltip for the specific block.
     */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String originalName = TextFormatting.GRAY + block.getName().getString() + TextFormatting.DARK_GRAY;

        new ExpandingTooltip().setCtrlForDescription(
                tooltips -> Tooltip.addBlankLine(tooltips).addAll(
                        Arrays.asList(Tooltip.formatLineFeeds(
                                getDescriptiveTooltip(PREFIX + "_block", originalName), TextFormatting.DARK_GRAY
                        ))
                )
        ).write(tooltip).add(newBlankLine());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResynthBlock<BrittleBlock<B>> queueRegistration() {
        BrittleBlockRecipes.INSTANCE.addBrittleBlockRecipe(this);
        return super.queueRegistration();
    }

    /**
     * @return the block instance this specific BrittleBlock mimics.
     */
    public final B of() {
        return this.block;
    }

    /**
     * The class responsible for creating and registering the recipes
     * which convert BrittleBlocks to and from the block they mimic.
     */
    public enum BrittleBlockRecipes implements ResynthRecipes.RecipeProvider {

        /**
         * Singleton instance of the class.
         */
        INSTANCE;

        /**
         * A list of all the BrittleBlocks created.
         */
        private List<BrittleBlock<?>> brittleBlocks = new ArrayList<>();

        /**
         * The list of all recipes created for BrittleBlocks.
         */
        private List<IRecipe<?>> recipes;
                /**
         * Adds a new recipe for the given BrittleBlock.
         */
        void addBrittleBlockRecipe(BrittleBlock<?> block) {
            brittleBlocks.add(block);
        }

        /**
         * Creates recipes for every registered BrittleBlock and adds them to
         * the array.
         */
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

        /**
         * {@inheritDoc}
         *
         * @return the list of all created recipes for converting to and from BrittleBlocks.
         */
        @Override
        public IRecipe<?>[] get() {
            if(recipes == null)
                createRecipes();

            return recipes.toArray(new IRecipe[0]);
        }
    }
}
