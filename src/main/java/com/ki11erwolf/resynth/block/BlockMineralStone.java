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
package com.ki11erwolf.resynth.block;

import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.MineralStoneConfig;
import com.ki11erwolf.resynth.item.ResynthItems;
import com.ki11erwolf.resynth.util.MathUtil;
import com.ki11erwolf.resynth.util.MinecraftUtil;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Mineral Rich Stone. The mods ore block.
 */
public class BlockMineralStone extends BlockOre{

    /**
     * The configuration settings for this block.
     */
    private static final MineralStoneConfig CONFIG = ResynthConfig.GENERAL_CONFIG.getCategory(MineralStoneConfig.class);

    /**
     * Sets the basic properties of the block.
     */
    BlockMineralStone(String name) {
        super(name, CONFIG.getMinExpDropped(), CONFIG.getMaxExpDropped());
    }

    /**
     * Handles spawning a random number Mineral Rocks in the world
     * when the player mines this block.
     */
    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void spawnAfterBreak(BlockState state, ServerWorld worldIn, BlockPos pos, ItemStack stack) {
        MinecraftUtil.spawnItemStackInWorld(
                new ItemStack(
                        ResynthItems.ITEM_MINERAL_ROCK, CONFIG.getBaseDrops() +
                        (MathUtil.Probability.newPercentageProbability(CONFIG.getExtraDropsChance())
                                .randomResult().isTrue() ? CONFIG.getExtraDrops() : 0)
                ),
                worldIn, pos
        );
    }
}
