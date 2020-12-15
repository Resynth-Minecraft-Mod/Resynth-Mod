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

import com.ki11erwolf.resynth.ResynthTabs;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.GeneralConfig;
import com.ki11erwolf.resynth.plant.set.PlantSet;
import com.ki11erwolf.resynth.util.ExpandingTooltip;
import com.ki11erwolf.resynth.util.Tooltip;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.List;

import static com.ki11erwolf.resynth.util.Tooltip.newBlankLine;
import static com.ki11erwolf.resynth.util.Tooltip.toTextComponent;

/**
 * Base class for all Resynth items.
 *
 * Provides extra utility and item registration.
 *
 * @param <T> the subclass to this class (e.i. the inheriting class).
 */
public class ResynthItem<T extends ResynthItem<?>> extends Item {

    /**
     * Flag to prevent queuing an item
     * more than once.
     */
    private boolean isQueued = false;

    /**
     * Constructor for all Resynth items that takes
     * a properties object.
     *
     * @param properties the items properties. The item
     *                   group will be set by this class.
     */
    ResynthItem(Properties properties, String name){
        super(setItemGroup(properties));
        this.setRegistryName(name);
    }

    /**
     * Default constructor for all Resynth items
     * with a default properties object with the
     * item group set to the default Resynth
     * item group {@link ResynthTabs#TAB_RESYNTH}.
     */
    @SuppressWarnings("WeakerAccess")
    public ResynthItem(String name){
        this(new Properties(), name);
    }

    /**
     * Constructor that allows providing a custom
     * ItemGroup.
     *
     * @param name the item name.
     * @param group the custom ItemGroup.
     */
    public ResynthItem(String name, ItemGroup group){
        super(new Properties().group(group));
        this.setRegistryName(name);
    }

    /**
     * {@inheritDoc}.
     *
     * Sets the tooltip for this item from the lang files.
     */
    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        new ExpandingTooltip().setCtrlForDescription(
                tooltips -> Tooltip.addBlankLine(tooltips).addAll(
                        Arrays.asList(Tooltip.formatLineFeeds(getDescriptiveTooltip(this), TextFormatting.DARK_GRAY))
                )
        ).write(tooltip).add(newBlankLine());
    }

    /**
     * Adds this item instance to ItemRegistrationQueue, which will then
     * be registered to the game from the queue.
     *
     * @return {@code this}.
     */
    ResynthItem<T> queueRegistration(){
        if(isQueued) throw new IllegalStateException(
                String.format("Item: %s already queued for registration.", this.getClass().getCanonicalName())
        );

        ResynthItems.INSTANCE.queueForRegistration(this);
        isQueued = true;

        return this;
    }

    // ***************
    // Utility Methods
    // ***************

    /**
     * A utility method specifically made for items of plants
     * (e.g. seeds).
     *
     * This method is used to set the items tooltip to contain
     * both the items description and statistics on the plant
     * set passed in.
     *
     * @param tooltip the tooltip we're modifying with the new
     *                new tooltips.
     * @param itemName the registry name (path only) of the
     *                 item we're getting the tooltip of.
     */
    protected static void addPlantItemTooltips(List<ITextComponent> tooltip, String itemName, PlantSet<?, ?> parentSet){
        Tooltip.addPlantItemOrBlockTooltips(tooltip, parentSet, getDescriptiveTooltip(itemName));
    }

    /**
     * Sets the item group in the properties object
     * to the default Resynth creative tab/item group.
     *
     * @param properties the items properties object.
     * @return properties, the items properties object.
     */
    private static Properties setItemGroup(Properties properties){
        properties.group(ResynthTabs.TAB_RESYNTH);
        return properties;
    }

    /**
     * Allows getting an items tooltip from the language file. This method
     * ignores config settings.
     *
     * @param key the tooltip key.
     * @param color the color of the tooltip.
     * @param params list of formatting parameters.
     * @return the text as an {@link ITextComponent} given by the
     * language file.
     */
    @SuppressWarnings("SameParameterValue")
    protected static ITextComponent getFormattedTooltip(String key, TextFormatting color, Object... params){
        return toTextComponent(color + I18n.format("tooltip.item.resynth." + key, params));
    }

    /**
     * Attempts to get the tooltip with the items decription
     * from the user selected language file.
     *
     * @param item the items who's description tooltip we want.
     */
    protected static ITextComponent getDescriptiveTooltip(ResynthItem<?> item){
        return item == null || item.getRegistryName() == null                                   //Null check
                ? toTextComponent(TextFormatting.RED + "Error")                             //Is null
                : getDescriptiveTooltip(item.getRegistryName().getPath());                      //Is not null
    }

    /**
     * Attempts to get the localized item tooltip stored under the
     * given key from the region decided language file.
     *
     * @param key the last part (e.g. <code>.item.resynth.[key]</code>)
     *            of the key where the tooltip is stored under.
     * @param params formatting parameters written to the tooltip
     *               in the marked places.
     * @return the item tooltip in the a decided language under the
     * given key, already formatted, or an error tooltip if an error
     * occured.
     */
    protected static ITextComponent getDescriptiveTooltip(String key, Object... params){
        if(key == null){ return toTextComponent(TextFormatting.RED + "Error"); }

        //noinspection deprecation
        return toTextComponent(WordUtils.wrap(
                TextFormatting.DARK_GRAY + I18n.format("tooltip.item.resynth." + key, params),
                ResynthConfig.GENERAL_CONFIG.getCategory(GeneralConfig.class).getTooltipCharacterLimit(),
                "\n", true
        ));
    }
}
