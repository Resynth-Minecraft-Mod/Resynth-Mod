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

    private final int veinMinY;

    private final int veinMaxY;

    protected ResynthOreFeature(ResourceLocation id, Biome.Category[] biomes, Block ore,
                                RuleTest target, int rarity, int size, int minY, int maxY) {
        super(id, biomes);

        this.ore = Objects.requireNonNull(ore);
        this.target = Objects.requireNonNull(target);

        this.veinRarity = MathUtil.within(rarity, 1, 64);
        this.veinSize = MathUtil.within(size, 1, 64);
        this.veinMinY = MathUtil.within(minY, 1, 254);
        this.veinMaxY = MathUtil.within(maxY, minY + 1, 255);
    }

    @Override
    protected void onConfigure(BiomeGenerationSettingsBuilder builder) throws Exception {
        if(ore.getRegistryName() == null) throw new Exception("Ore registry name is null");

        Registry.register(
                WorldGenRegistries.field_243653_e, ore.getRegistryName(), Feature.ORE.withConfiguration(
                        new OreFeatureConfig(target, ore.getDefaultState(), veinSize)
                ).withPlacement(Placement.field_242907_l.configure(
                        new TopSolidRangeConfig(veinMinY, veinMinY, veinMaxY)
                )).func_242728_a().func_242731_b(veinRarity)
        );

        ConfiguredFeature<?,?> feature = WorldGenRegistries.field_243653_e.getOrDefault(ore.getRegistryName());

        if(feature == null) throw new Exception("Feature configuration is null");
        else builder.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, feature);
    }
}
