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
import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

/**
 * Base class for all Resynth items.
 *
 * Provides extra utility and item registration.
 * @param <T> the subclass to this class (e.i. the inheriting class).
 */
public class ResynthItem<T> extends Item {

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

    T queueRegistration(){
        if(isQueued)
            throw new IllegalStateException(
                    String.format("Item: %s already queued for registration.", this.getClass().getCanonicalName())
            );

        ResynthItems.queueItem(this);
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
}
