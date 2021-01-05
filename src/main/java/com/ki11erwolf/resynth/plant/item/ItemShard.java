/*
 * Copyright 2018-2021 Ki11er_wolf
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
import com.ki11erwolf.resynth.plant.block.BlockCrystallinePlant;
import com.ki11erwolf.resynth.plant.set.PlantSet;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The produce item for Crystalline plant types (e.g. diamond shard).
 */
public class ItemShard extends ResynthItem<ItemShard> {

    /**
     * The prefix given to all shard produce items.
     */
    private static final String PREFIX = "shard";

    private final PlantSet<BlockCrystallinePlant, Block> parentSet;

    public ItemShard(PlantSet<BlockCrystallinePlant, Block> parentSet) {
        super(parentSet.getSetTypeName() + "_" + PREFIX + "_" + parentSet.getSetName(), ResynthTabs.TAB_RESYNTH_PRODUCE);
        this.parentSet = parentSet;
    }

    /**
     * Constructs the tooltip for the item.
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn){
        addPlantItemTooltips(tooltip, PREFIX, parentSet);
    }
}
