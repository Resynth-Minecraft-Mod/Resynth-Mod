package com.ki11erwolf.resynth.features;

import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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

public class ResynthFlowerFeature extends ResynthFeature<ResynthFlowerFeature> {

    /*
        Uses a modified ore generation feature to generate flowers.
     */

    private static final RuleTest TARGET_FLOWERS = new MatchBlockListRuleTest(
            Blocks.GRASS, Blocks.FERN, Blocks.DEAD_BUSH, Blocks.POPPY, Blocks.DANDELION, Blocks.BLUE_ORCHID,
            Blocks.SUNFLOWER, Blocks.OXEYE_DAISY, Blocks.PEONY
    );

    private static final TopSolidRangeConfig FLOWER_HEIGHT_RANGE = new TopSolidRangeConfig(
            50, 50, 255
    );

    private final Block flower;

    private final int patchRarity;

    private final int patchSize;

    protected ResynthFlowerFeature(ResourceLocation id, Biome.Category[] biomes, Block flower, int patchRarity, int patchSize) {
        super(id, biomes);

        this.flower = Objects.requireNonNull(flower);
        this.patchRarity = MathUtil.within(patchRarity, 1, 64);
        this.patchSize = MathUtil.within(patchSize, 1, 64);
    }

    @Override
    protected void onConfigure(BiomeGenerationSettingsBuilder builder) throws Exception {
        if(flower.getRegistryName() == null) throw new Exception("Flower registry name is null");

        Registry.register(
                WorldGenRegistries.field_243653_e, flower.getRegistryName(), Feature.ORE.withConfiguration(
                        new OreFeatureConfig(TARGET_FLOWERS, flower.getDefaultState(), patchSize)
                ).withPlacement(Placement.field_242907_l.configure(FLOWER_HEIGHT_RANGE)).func_242728_a().func_242731_b(patchRarity)
        );

        ConfiguredFeature<?,?> feature = WorldGenRegistries.field_243653_e.getOrDefault(flower.getRegistryName());

        if(feature == null) throw new Exception("Flower Feature configuration is null");
        else builder.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, feature);
    }

}
