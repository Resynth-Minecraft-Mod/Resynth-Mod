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

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Dense Mineral Rock. x9 MineralRocks.
 */
public class ItemDenseMineralRock extends ResynthItem{

    /**
     * The registry name of the item.
     */
    public static final String ITEM_NAME = "dense_mineral_rock";

    /**
     * Default item constructor.
     */
    public ItemDenseMineralRock() {
        super(ITEM_NAME);
    }

    /**
     * {@inheritDoc}
     * Adds a tooltip on how to use the item.
     *
     * @param stack
     * @param worldIn
     * @param tooltip
     * @param flagIn
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn){
        tooltip.add(new TextComponentString("9 Mineral Rocks compressed into 1 item."));
        tooltip.add(new TextComponentString("Can also be added to Mineral Soil."));
    }
}
