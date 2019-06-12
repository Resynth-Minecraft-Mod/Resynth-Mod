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

import com.ki11erwolf.resynth.ResynthTabs;
import com.ki11erwolf.resynth.item.ResynthItem;
import com.ki11erwolf.resynth.plant.PlantSetCrystalline;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
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
     * The crystalline plant set that created
     * this item.
     */
    private final PlantSetCrystalline plantSet;

    /**
     * Default item constructor.
     *
     * @param name the name of the item.
     * @param set the plant set this item belongs to.
     */
    public ItemPlantProduceShard(String name, PlantSetCrystalline set) {
        super(new Properties().group(ResynthTabs.TAB_RESYNTH_PRODUCE), name, PREFIX);
        this.plantSet = set;
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
        tooltip.add(stringToTextComponent("Can be left in water for a chance to obtain more seeds."));
        tooltip.add(stringToTextComponent("Can be smelted to obtain the resource."));

        tooltip.add(stringToTextComponent(""));

        tooltip.add(stringToTextComponent(
                TextFormatting.GOLD
                        + "Seed Drop Chance (Ore): " +
                        plantSet.getTextualOreSeedDropChance()
        ));

        tooltip.add(stringToTextComponent(
                TextFormatting.GREEN
                        + "Seed Drop Chance (Produce): "
                        + plantSet.getTextualProduceSeedDropChance()
        ));


        tooltip.add(stringToTextComponent(
                TextFormatting.RED
                        + "Resource Count (Smelting): x"
                        + plantSet.getResult().getCount()
        ));

        tooltip.add(stringToTextComponent(
                TextFormatting.DARK_PURPLE
                        + "Plant Growth Chance: "
                        + plantSet.getTextualPlantGrowthChance()
        ));
    }
}
