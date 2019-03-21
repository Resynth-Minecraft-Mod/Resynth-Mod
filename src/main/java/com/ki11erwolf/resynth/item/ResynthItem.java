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

/**
 * Base class for all Resynth items.
 */
public class ResynthItem extends Item {

    /**
     * The prefix for all item names.
     */
    private static final String ITEM_PREFIX = "item";

//    Old 1.12.2 constructor.
//    /**
//     * Sets the creative tab, unlocalized name prefix
//     * and registry name.
//     *
//     * @param name the identifying name of the item (e.g. redstoneDust)
//     */
//    public ResynthItem(String name) {
//        this(name, ITEM_PREFIX);
//    }

//    Old 1.12.2 constructor.
//    /**
//     * @param name the general name of the item (e.g. redstoneDust)
//     * @param prefix the prefix to add before all names of the item.
//     */
//    public ResynthItem(String name, String prefix){
//        this.setUnlocalizedName(ResynthMod.MOD_ID + "." + prefix + StringUtil.capitalize(name));
//        this.setRegistryName(prefix + "_" + StringUtil.toUnderscoreLowercase(name));
//        this.setCreativeTab(ResynthTabs.RESYNTH_TAB);
//    }

    /**
     * Constructor for all Resynth items that takes
     * a properties object.
     *
     * @param properties the items properties. The item
     *                   group will be set by this class.
     */
    public ResynthItem(Properties properties){
        super(setItemGroup(properties));
    }

    /**
     * Default constructor for all Resynth items
     * with a default properties object with the
     * item group set to the default Resynth
     * item group {@link ResynthTabs#TAB_RESYNTH}.
     */
    public ResynthItem(){
        this(new Properties());
    }

    /**
     * Adds this item to the list of items to be
     * registered to the game though forge.
     *
     * @return {@code this} item.
     */
    protected ResynthItem register(){
        ResynthItemRegistry.addItem(this);
        return this;
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
}
