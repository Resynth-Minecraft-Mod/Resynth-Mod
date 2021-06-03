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
import net.minecraft.block.Blocks;
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
 * A plant decoration {@link Feature World Generation Feature}.
 */
public class FlowerFeature extends Feature<FlowerFeature> {

    /**
     * A list of vanilla flowers generated in the world by Minecraft.
     */
    private static final RuleTest BASE_FLOWERS = new BlockListMatcher(
            Blocks.GRASS, Blocks.FERN, Blocks.DEAD_BUSH, Blocks.POPPY, Blocks.DANDELION, Blocks.BLUE_ORCHID,
            Blocks.SUNFLOWER, Blocks.OXEYE_DAISY, Blocks.PEONY
    );

    /**
     * The range of heights that flowers can be generated at.
     */
    private static final TopSolidRangeConfig HEIGHT_RANGE = new TopSolidRangeConfig(
            40, 40, 255
    );

    /**
     * The specific flower Block to generate.
     */
    private final Block flower;

    /**
     * The average amount of flower patches to generate in any given chunk.
     */
    private final int rarity;

    /**
     * The average amount of flowers to generate in a patch of flowers.
     */
    private final int size;

    /**
     * Creates a new FlowerFeature that generates a specific flower Block in a list of
     * biomes.
     *
     * @param id a unique identifier for the constructed OreFeature.
     * @param biomes list of {@link Biome.Category Biomes} to generate the ore in.
     * @param flower the exact {@link Block} type to generate.
     * @param rarity the average amount of flower patches to generate in any given chunk.
     * @param size the average amount of flowers to generate in a patch of flowers.
     */
    protected FlowerFeature(ResourceLocation id, Biome.Category[] biomes, Block flower, int rarity, int size) {
        super(id, biomes);

        this.flower = Objects.requireNonNull(flower);
        this.rarity = MathUtil.within(rarity, 1, 64);
        this.size = MathUtil.within(size, 1, 64);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ConfiguredFeature<?, ?> constructFeature() {
        return net.minecraft.world.gen.feature.Feature.ORE.withConfiguration(
                new OreFeatureConfig(BASE_FLOWERS, flower.getDefaultState(), size)
        ).withPlacement(Placement.RANGE.configure(HEIGHT_RANGE)).func_242731_b(rarity);
    }

    /**
     * {@inheritDoc}
     *
     * <p> <b>NOTE:</b> FlowerFeatures use underground ore generation behind the scenes. </p>
     */
    @Override
    protected void configureFeature(BiomeGenerationSettingsBuilder builder) throws Exception {
        if(flower.getRegistryName() == null) throw new Exception("Flower registry name is null");

        if(getFeature() == null) throw new Exception("Flower Feature was not constructed correctly!");
        else builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, getFeature());
    }
}
