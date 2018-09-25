/*
 * Copyright 2018 Ki11er_wolf
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

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.ResynthTab;
import com.ki11erwolf.resynth.util.StringUtil;
import net.minecraft.item.Item;

/**
 * Base for all mod item classes.
 */
public class ResynthItem extends Item {

    /**
     * The prefix for all item names.
     */
    private static final String ITEM_PREFIX = "item";

    /**
     * Sets the creative tab, unlocalized name prefix
     * and registry name.
     *
     * @param name the general name of the item (e.g. redstoneDust)
     */
    public ResynthItem(String name) {
        this(name, ITEM_PREFIX);
    }

    /**
     * @param name the general name of the item (e.g. redstoneDust)
     * @param prefix the prefix to add before all names of the item.
     */
    public ResynthItem(String name, String prefix){
        this.setUnlocalizedName(ResynthMod.MOD_ID + "." + prefix + StringUtil.capitalize(name));
        this.setRegistryName(prefix + "_" + StringUtil.toUnderscoreLowercase(name));
        this.setCreativeTab(ResynthTab.RESYNTH_TAB);
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
}
