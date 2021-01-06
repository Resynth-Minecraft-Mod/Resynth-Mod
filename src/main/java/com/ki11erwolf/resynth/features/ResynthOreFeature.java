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
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;

import java.util.Objects;

public class ResynthOreFeature extends ResynthFeature<ResynthOreFeature> {

    private final Block ore;

    private final RuleTest target;

    private final int veinRarity;

    private final int veinSize;

    private final int veinMinHeight;

    private final int veinMaxHeight;

    protected ResynthOreFeature(ResourceLocation id, Biome.Category[] biomes, Block ore, RuleTest target,
                                int rarity, int size, int minHeight, int maxHeight) {
        super(id, biomes);

        this.ore = Objects.requireNonNull(ore);
        this.target = Objects.requireNonNull(target);

        this.veinRarity = MathUtil.within(rarity, 1, 64);
        this.veinSize = MathUtil.within(size, 1, 64);
        this.veinMinHeight = MathUtil.within(minHeight, 1, 254);
        this.veinMaxHeight = MathUtil.within(maxHeight, minHeight + 1, 255);
    }

    @Override
    protected void onConfigure(BiomeGenerationSettingsBuilder builder) throws Exception {
        if(ore.getRegistryName() == null) throw new Exception("Ore registry name is null");

        Registry.register(
                WorldGenRegistries.CONFIGURED_FEATURE, ore.getRegistryName(), Feature.ORE.withConfiguration(
                        new OreFeatureConfig(target, ore.getDefaultState(), veinSize)
                ).withPlacement(Placement.RANGE.configure(
                        new TopSolidRangeConfig(veinMinHeight, veinMinHeight, veinMaxHeight)
                )).square().func_242731_b(veinRarity)
        );

        ConfiguredFeature<?,?> feature = WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(ore.getRegistryName());

        if(feature == null) throw new Exception("Ore Feature configuration is null");
        else builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, feature);
    }
}
