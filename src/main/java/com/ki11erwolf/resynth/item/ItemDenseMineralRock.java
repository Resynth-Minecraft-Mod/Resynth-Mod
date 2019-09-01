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
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Dense Mineral Rock.
 *
 * Compressed form of Mineral Rocks. Worth 9 Mineral Rocks.
 */
public class ItemDenseMineralRock extends ResynthItem<ItemDenseMineralRock> {

    /**
     * Default item constructor.
     *
     * Sets the name of the item.
     */
    ItemDenseMineralRock() {
        super("dense_mineral_rock");
    }

    /**
     * {@inheritDoc}.
     *
     * Constructs the tooltip for the item.
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn){
        setDescriptiveTooltip(tooltip, this);
    }
}
