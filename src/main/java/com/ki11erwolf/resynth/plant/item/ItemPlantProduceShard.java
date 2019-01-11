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
package com.ki11erwolf.resynth.plant.item;

import com.ki11erwolf.resynth.ResynthTabProduce;
import com.ki11erwolf.resynth.item.ResynthItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The produce item for crystalline plants.
 */
public class ItemPlantProduceShard extends ResynthItem {

    /**
     * Prefix for the item.
     */
    //TODO: Change in 1.13
    private static final String PREFIX = "produce";

    /**
     * Default item constructor.
     *
     * @param name the name of the item.
     */
    public ItemPlantProduceShard(String name) {
        super(name, PREFIX);
        this.setCreativeTab(ResynthTabProduce.RESYNTH_TAB_PRODUCE);
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip,
                               ITooltipFlag flagIn){
        tooltip.add("Can be left in water for a chance to obtain more seeds.");
        tooltip.add("Can be smelted to obtain the resource.");
    }
}
