/*
 * Copyright 2018-2019 Ki11er_wolf
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
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * Base class for all Resynth items.
 *
 * Provides extra utility and item registration.
 *
 * @param <T> the subclass to this class (e.i. the inheriting class).
 */
public class ResynthItem<T extends ResynthItem> extends Item {

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
     * Constructor that allows a custom prefix.
     *
     * @param name the name of the item.
     * @param prefix prefix to the name of the item.
     * @param properties item properties.
     */
    public ResynthItem(Properties properties, String name, String prefix){
        super(setItemGroup(properties));
        this.setRegistryName(prefix + name);
    }

    /**
     * Adds this item instance to ItemRegistrationQueue, which will then
     * be registered to the game from the queue.
     *
     * @return {@code this}.
     */
    T queueRegistration(){
        if(isQueued)
            throw new IllegalStateException(
                    String.format("Item: %s already queued for registration.",
                            this.getClass().getCanonicalName())
            );

        ResynthItems.INSTANCE.queueForRegistration(this);
        isQueued = true;

        //noinspection unchecked //Should NOT be possible.
        return (T)this;
    }

    // ***************
    // Utility Methods
    // ***************

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
     * Turns a given string into a {@link TextComponentString}
     * containing the given string.
     *
     * @param text the given string text.
     * @return a new TextComponentString containing the given string.
     */
    protected static ITextComponent stringToTextComponent(String text){
        return new TextComponentString(text);
    }

    /**
     * Allows getting an items tooltip from the language file. This method
     * ignores config settings.
     *
     * @param key the tooltip key.
     * @return the text as an {@link ITextComponent} given by the
     * language file.
     */
    @SuppressWarnings("WeakerAccess")
    static ITextComponent getTooltip(String key){
        return stringToTextComponent(TextFormatting.GRAY + I18n.format("tooltip.item.resynth." + key));
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
        return stringToTextComponent(color + I18n.format("tooltip.item.resynth." + key, params));
    }

    /**
     * Allows getting an items tooltip from the language file. This method
     * ignores config settings.
     *
     * @param key the tooltip key.
     * @param color the color of the tooltip.
     * @return the text as an {@link ITextComponent} given by the
     * language file.
     */
    @SuppressWarnings("unused")
    static ITextComponent getTooltip(String key, TextFormatting color){
        return stringToTextComponent(color + I18n.format("tooltip.item.resynth." + key));
    }

    /**
     * Allows getting an items tooltip from the language file. This method
     * ignores config settings.
     *
     * @param item the item who's tooltip we want.
     * @return the text as an {@link ITextComponent} given by the
     * language file.
     */
    @SuppressWarnings("unused")
    static ITextComponent getTooltip(ResynthItem item){
        if(item.getRegistryName() == null){
            return stringToTextComponent(TextFormatting.RED + "Error");
        }

        return getTooltip(item.getRegistryName().toString().replace(":", "."));
    }

    /**
     * Will add an items tooltip (from lang file) to the given
     * tooltip array, provided the config allows it.
     *
     * @param tooltip the tooltip array object.
     * @param item the items who's tooltip we want.
     */
    static void setDescriptiveTooltip(List<ITextComponent> tooltip, ResynthItem item){
        if(!ResynthConfig.GENERAL_CONFIG.getCategory(GeneralConfig.class).areTooltipsEnabled())
            return;

        if(item.getRegistryName() == null){
            tooltip.add(stringToTextComponent(TextFormatting.RED + "Error"));
        }

        tooltip.add(stringToTextComponent(TextFormatting.GRAY + I18n.format(
                "tooltip.item." + item.getRegistryName().toString().replace(":", ".")
        )));
    }

    /**
     * Will add an items tooltip (from lang file) to the given
     * tooltip array, provided the config allows it.
     *
     * @param tooltip the tooltip array object.
     * @param item the name of the items who's tooltip we want and key appended.
     */
    protected static void setDescriptiveTooltip(List<ITextComponent> tooltip, String item, Object... params){
        if(!ResynthConfig.GENERAL_CONFIG.getCategory(GeneralConfig.class).areTooltipsEnabled())
            return;

        if(item == null){
            tooltip.add(stringToTextComponent(TextFormatting.RED + "Error"));
        }

        tooltip.add(stringToTextComponent(TextFormatting.GRAY + I18n.format(
                "tooltip.item.resynth." + item, params
        )));
    }
}
