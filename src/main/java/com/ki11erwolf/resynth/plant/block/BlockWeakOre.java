/*
 * Copyright (c) 2018 - 2021 Ki11er_wolf.
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

import com.ki11erwolf.resynth.block.ResynthBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;

import java.util.Objects;

/**
 * A very simple {@link net.minecraft.block.Block} with a low
 * {@link #getExplosionResistance()} that can mimic and replace
 * other blocks in the world, which normally cannot be destroyed
 * with {@link net.minecraft.block.TNTBlock} and making them
 * destructible. This makes it possible for players to obtain
 * Metallic seeds from the block and grow more.
 *
 * <p/> Created as a workaround for Blocks like {@link Blocks#OBSIDIAN}
 * and {@link Blocks#ANCIENT_DEBRIS} which cannot otherwise cannot be
 * made into Metallic PlantSets.
 */
public class BlockWeakOre extends ResynthBlock<BlockWeakOre> {

    /**
     * The String prefix appended to the beginning of the blocks registry name.
     */
    public static final String NAME_PREFIX = "weak_";

    /**
     * A low explosion resistance (same as stone) passed
     * to {@link #getExplosionResistance()}.
     */
    @SuppressWarnings("deprecation")
    public static final float EXPLOSION_RESISTANCE = Blocks.STONE.getExplosionResistance(); //6.0F

    /**
     * Creates a new {@link BlockWeakOre} that will mimic
     * the given {@code impersonation} block.
     *
     * @param impersonation the block to impersonate and mimic.
     */
    public BlockWeakOre(Block impersonation, float hardness) {
        super(
                AbstractBlock.Properties.create( // Material
                        impersonation.getDefaultState().getMaterial()
                ).sound( // Sound
                        impersonation.getDefaultState().getSoundType()
                ).hardnessAndResistance( // Hardness & Resistance
                        Math.max(hardness, 50), EXPLOSION_RESISTANCE
                ).harvestTool( // Tool
                        ToolType.PICKAXE
                ).harvestLevel( // Tool Level
                        impersonation.getDefaultState().getHarvestLevel()
                ).setRequiresTool(), // Name
                new Item.Properties(), NAME_PREFIX + Objects.requireNonNull(impersonation.getRegistryName()).getPath()
        );
    }

    /**
     * Always returns a low explosion resistance, making the block
     * easily destroyed by TNT.
     *
     * @return {@link #EXPLOSION_RESISTANCE a constant weak explosion
     * resistance}.
     */
    @SuppressWarnings("deprecation")
    @Override
    public float getExplosionResistance() {
        return EXPLOSION_RESISTANCE;
    }
}
