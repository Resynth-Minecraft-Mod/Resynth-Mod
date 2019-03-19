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
package com.ki11erwolf.resynth.block;

import com.ki11erwolf.resynth.ResynthConfig;
import com.ki11erwolf.resynth.item.ResynthItems;
import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Mineral Rich Stone.
 *
 * The mods ore block.
 */
public class BlockMineralOre extends ResynthBlock{

    /**
     * Ore configuration settings.
     */
    public ResynthConfig.Ore oreCfg;

    /**
     * Sets the basic properties of the block.
     */
    protected BlockMineralOre() {
        super(Material.ROCK, "mineralOre");
        oreCfg = ResynthConfig.ORE;
        this.setHardness(oreCfg.hardness);
        this.setLightLevel(0.25F);
        BlockUtil.setHarvestLevel(this, BlockUtil.HarvestTools.PICKAXE, 2);
    }

    /**
     * {@inheritDoc}
     *
     * @param state
     * @param rand
     * @param fortune
     * @return {@link com.ki11erwolf.resynth.item.ItemMineralRock} as the item dropped.
     */
    @Nonnull
    @Override
    @SuppressWarnings("ConstantConditions")
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ResynthItems.ITEM_MINERAL_ROCK;
    }

    /**
     * The number of items dropped (with fortune)
     *
     * @param fortune fortune level.
     * @param random random object instance.
     * @return the number of items dropped with fortune.
     * Determined by config settings.
     */
    @Override
    public int quantityDroppedWithBonus(int fortune, @Nonnull Random random) {
        if(fortune == 0)
            return this.quantityDropped(random);

        if(fortune == 3)
            return this.quantityDropped(random)
                    + MathUtil.getRandomIntegerInRange(1, (fortune * oreCfg.multiplier) - 1);

        return this.quantityDropped(random) + MathUtil.getRandomIntegerInRange(0, fortune * oreCfg.multiplier);
    }

    /**
     * The number of items dropped.
     *
     * @param random random object instance.
     * @return the number of items dropped without fortune.
     * Determined by config settings.
     */
    @Override
    public int quantityDropped(Random random) {
        return oreCfg.baseDrops + (MathUtil.chance(oreCfg.extraChance) ? oreCfg.extraDrops : 0);
    }

    /**
     * The amount of experience dropped by the block.
     *
     * @param state -
     * @param world -
     * @param pos -
     * @param fortune -
     * @return Experience amount to drop. Determined by config. Minimum is 2.
     */
    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune){
        if (this.getItemDropped(state, RANDOM, fortune) != Item.getItemFromBlock(this)){
            return 2 + RANDOM.nextInt(oreCfg.maxXP);
        }

        return 0;
    }

    /**
     * Drops the ore's item instead of the block when
     * silk touch is used. This block is useless.
     *
     * @param state -
     * @return {@link com.ki11erwolf.resynth.item.ItemMineralRock}
     */
    @Nonnull
    @Override
    @SuppressWarnings("ConstantConditions")
    protected ItemStack getSilkTouchDrop(@Nonnull IBlockState state){
        return new ItemStack(ResynthItems.ITEM_MINERAL_ROCK, quantityDropped(null));
    }
}
