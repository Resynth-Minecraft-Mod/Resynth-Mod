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
package com.ki11erwolf.resynth.features;

import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;

import java.util.Objects;

/**
 * An underground ore and resource {@link Feature World Generation Feature}.
 */
public class OreFeature extends Feature<OreFeature> {

    /**
     * The exact {@link Block} type to generate.
     */
    private final Block ore;

    /**
     * The {@link Block Block/Blocks} in the worlds landscape
     * to generate the ore blocks in.
     */
    private final RuleTest target;

    /**
     * The number of veins of the ore to generate in any given Chunk.
     */
    private final int rarity;

    /**
     * The number of ore blocks to generate in any given vein of ore.
     */
    private final int size;

    /**
     * The minimum height in the world where veins of the ore can generate.
     */
    private final int minimumHeight;

    /**
     * The maximum height in the world where veins of the ore can generate.
     */
    private final int maximumHeight;

    /**
     * Creates a new OreFeature that generates a specific ore Block in a list of
     * biomes.
     *
     * @param id a unique identifier for the constructed OreFeature.
     * @param biomes list of {@link Biome.Category Biomes} to generate the ore in.
     * @param ore the exact {@link Block} type to generate.
     * @param target the {@link Block Block/Blocks} from the worlds landscape to
     *               generate the ore blocks in.
     * @param rarity the number of veins of the ore to generate in any given Chunk.
     * @param size the number of ore blocks to generate in any given vein of ore.
     * @param minHeight the minimum height in the world where veins of the ore can generate.
     * @param maxHeight the maximum height in the world where veins of the ore can generate.
     */
    protected OreFeature(ResourceLocation id, Biome.Category[] biomes, Block ore, RuleTest target,
                         int rarity, int size, int minHeight, int maxHeight) {
        super(id, biomes);

        this.ore = Objects.requireNonNull(ore);
        this.target = Objects.requireNonNull(target);

        this.rarity = MathUtil.within(rarity, 1, 64);
        this.size = MathUtil.within(size, 1, 64);
        this.minimumHeight = MathUtil.within(minHeight, 1, 254);
        this.maximumHeight = MathUtil.within(maxHeight, minHeight + 1, 255);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ConfiguredFeature<?, ?> constructFeature() {
        return net.minecraft.world.gen.feature.Feature.ORE.withConfiguration(
                new OreFeatureConfig(target, ore.getDefaultState(), size)
        ).withPlacement(Placement.RANGE.configure(
                new TopSolidRangeConfig(minimumHeight, minimumHeight, maximumHeight)
        )).square().func_242731_b(rarity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configureFeature(BiomeGenerationSettingsBuilder builder) throws Exception {
        if(ore.getRegistryName() == null) throw new Exception("Ore registry name is null");

        if(getFeature() == null) throw new Exception("Ore Feature was not constructed correctly!");
        else builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, getFeature());
    }
}
