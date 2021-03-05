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
package com.ki11erwolf.resynth.block;

import com.ki11erwolf.resynth.ResynthTabs;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.GeneralConfig;
import com.ki11erwolf.resynth.item.ResynthItemBlock;
import com.ki11erwolf.resynth.plant.set.PlantSet;
import com.ki11erwolf.resynth.util.ExpandingTooltip;
import com.ki11erwolf.resynth.util.Tooltip;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import static com.ki11erwolf.resynth.util.Tooltip.*;

/**
 * Base block class for all Resynth blocks.
 *
 * Provides extra utility, registration and a linked ItemBlock.
 *
 * @param <T> the subclass to this class (e.i. the inheriting class).
 */
public class ResynthBlock<T extends ResynthBlock<?>> extends Block {

    /**
     * Flag to prevent queuing a block
     * more than once.
     */
    private boolean isQueued = false;

    /**
     * The ItemBlock instance for this block instance.
     */
    private final ResynthItemBlock itemBlock;

    /**
     * Creates a new Resynth Block with the given name
     * and properties.
     *
     * @param properties the properties that define the block.
     * @param name the name of the block.
     */
    public ResynthBlock(Properties properties, String name){
        this(properties, new Item.Properties().group(ResynthTabs.TAB_RESYNTH), name);
    }

    /**
     * Creates a new Resynth Block with the given name, ItemBlock
     * properties and block properties.
     *
     * @param properties the properties that define the block.
     * @param itemProperties the properties that defines the blocks item.
     * @param name the name of the block.
     */
    public ResynthBlock(Properties properties, Item.Properties itemProperties, String name) {
        super(properties);
        setRegistryName(name);

        itemBlock = new ResynthItemBlock(this, itemProperties);
        itemBlock.setRegistryName(name);
    }

    /**
     * @return the {@link net.minecraft.item.BlockItem} for this block instance.
     */
    public ResynthItemBlock getItemBlock(){
        return this.itemBlock;
    }

    /**
     * {@inheritDoc}
     *
     * Sets the tooltip for this block from the lang files.
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn) {
        new ExpandingTooltip().setCtrlForDescription(
                tooltips -> addBlankLine(tooltips).addAll(
                        Arrays.asList(Tooltip.formatLineFeeds(getDescriptiveTooltip(this), TextFormatting.DARK_GRAY)))
        ).write(tooltip).add(newBlankLine());
    }

    /**
     * Adds this block instance to ItemRegistrationQueue, which will then
     * be registered to the game from the queue. This will also register
     * the ItemBlock instance associated with this block instance.
     *
     * @return {@code this}.
     */
    @SuppressWarnings("WeakerAccess")//Lies
    protected ResynthBlock<T> queueRegistration() {
        if (isQueued)
            throw new IllegalStateException(
                    String.format("Block: %s already queued for registration.",
                            this.getClass().getCanonicalName())
            );

        ResynthBlocks.INSTANCE.queueForRegistration(this);
        itemBlock.queueRegistration();
        isQueued = true;

        return this;
    }

    // *******
    // Utility
    // *******

    /**
     * A utility method specifically made for block produce
     * from plants (e.g. organic ore).
     *
     * This method is used to set the blocks tooltip to contain
     * both the blocks description and statistics on the plant
     * set passed in.
     *
     * @param tooltip the tooltip we're modifying with the new
     *                new tooltips.
     * @param blockName the registry name (path only) of the
     *                  block we're getting the tooltip of.
     */
    @SuppressWarnings("DuplicatedCode")
    protected static void addPlantItemBlockTooltips(
            List<ITextComponent> tooltip,@SuppressWarnings("SameParameterValue") String blockName, PlantSet<?, ?> parentSet){
        Tooltip.addPlantItemOrBlockTooltips(tooltip, parentSet, getDescriptiveTooltip(blockName));
    }

    /**
     * Attempts to get the tooltip with the blocks decription
     * from the user selected language file.
     *
     * @param item the items who's description tooltip we want.
     */
    protected static ITextComponent getDescriptiveTooltip(ResynthBlock<?> item){
        return item == null || item.getRegistryName() == null                                   //Null check
                ? toTextComponent(TextFormatting.RED + "Error")                             //Is null
                : getDescriptiveTooltip(item.getRegistryName().getPath());                      //Is not null
    }

    /**
     * Attempts to get the tooltip with the given blocks description
     * from the user selected language file. The returned tooltip
     * will be formatted ({@link String#format(String, Object...)})
     * using the given parameters.
     *
     * @param params list of parameters for use in formatting.
     * @param item the name of the blocks who's tooltip we want and key appended.
     */
    @SuppressWarnings("deprecation")
    protected static ITextComponent getDescriptiveTooltip(String item, Object... params){
        if(item == null){
            return toTextComponent(TextFormatting.RED + "Error");
        }

        return toTextComponent(WordUtils.wrap(
                TextFormatting.DARK_GRAY + I18n.format("tooltip.block.resynth." + item, params),
                ResynthConfig.GENERAL_CONFIG.getCategory(GeneralConfig.class).getTooltipCharacterLimit(),
                "\n", true
        ));
    }
}
