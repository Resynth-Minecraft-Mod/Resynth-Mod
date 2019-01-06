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
package com.ki11erwolf.resynth;

import com.ki11erwolf.resynth.item.ResynthItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * The mods creative tab. Singleton instance.
 */
public final class ResynthTab extends CreativeTabs {

    /**
     * Creative tab instance.
     */
    public static final CreativeTabs RESYNTH_TAB = new ResynthTab("tabResynth");

    /**
     * Private constructor.
     *
     * @param label tab name.
     */
    private ResynthTab(String label) {
        super(ResynthMod.MOD_ID + "." + label);
    }

    /**
     * @return the item/block to use as tabs icon.
     */
    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ResynthItems.ITEM_MINERAL_HOE);
    }
}
