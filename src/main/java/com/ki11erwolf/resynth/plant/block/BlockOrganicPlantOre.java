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
package com.ki11erwolf.resynth.plant.block;

import com.ki11erwolf.resynth.ResynthTabs;
import com.ki11erwolf.resynth.block.ResynthBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The base class for all blocks produced
 * by metallic plants.
 */
public class BlockOrganicPlantOre extends ResynthBlock {

    /**
     * Prefix for the ore block.
     */
    protected static final String ORE_PREFIX = "ore";

    /**
     * The plant set that created this block.
     */
    //private final PlantSetMetallic plantSet;

    /**
     * Constructs a new plant ore block instance.
     *
     * @param name the name of the block.
     * @param set the metallic plant set this block belongs to.
     */
    public BlockOrganicPlantOre(String name) {
        super(
                Properties.create(Material.GROUND).sound(SoundType.STONE).hardnessAndResistance(2.0F),
                name, ORE_PREFIX);
        //this.setHardness(2.0F);
        //this.setCreativeTab(ResynthTabProduce.RESYNTH_TAB_PRODUCE);
        //this.plantSet = set;
        //BlockUtil.setHarvestLevel(this, BlockUtil.HarvestTools.AXE, 2);
    }

    /**
     * {@inheritDoc}
     * Adds a tooltip on how to use the block.
     *
     * @param stack
     * @param worldIn
     * @param tooltip
     * @param flagIn
     */
    //@Override//TODO: Move to itemblock class.
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn) {
//        tooltip.add(stringToTextComponent("Can be blown up to for a chance to obtain more seeds."));
//        tooltip.add(stringToTextComponent("Can be smelted to obtain the original ore block."));
//
//        tooltip.add(stringToTextComponent(""));
//
//        tooltip.add(stringToTextComponent(
//                TextFormatting.GOLD
//                        + "Seed Drop Chance (Ore): " +
//                        plantSet.getTextualOreSeedDropChance()
//        ));
//
//        tooltip.add(stringToTextComponent(
//                TextFormatting.GREEN
//                        + "Seed Drop Chance (Produce): "
//                        + plantSet.getTextualProduceSeedDropChance()
//        ));
//
//
//        tooltip.add(stringToTextComponent(
//                TextFormatting.RED
//                        + "Resource Count (Smelting): x"
//                        + plantSet.getResult().getCount()
//        ));
//
//        tooltip.add(stringToTextComponent(
//                TextFormatting.DARK_PURPLE
//                        + "Plant Growth Chance: "
//                        + plantSet.getTextualPlantGrowthChance()
//        ));
    }

}
