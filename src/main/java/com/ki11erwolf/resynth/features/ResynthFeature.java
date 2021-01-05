package com.ki11erwolf.resynth.features;

import com.ki11erwolf.resynth.ResynthMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class ResynthFeature<T extends ResynthFeature<T>> {

    private static final Logger LOG = ResynthMod.getNewLogger();

    private final ResourceLocation id;

    private final List<Biome.Category> biomes;

    protected ResynthFeature(ResourceLocation id, Biome.Category[] biomes) {
        this.id = Objects.requireNonNull(id);
        this.biomes = Arrays.asList(biomes);
    }

    protected abstract void onConfigure(BiomeGenerationSettingsBuilder builder) throws Exception;

    protected final void configure(BiomeGenerationSettingsBuilder builder, Biome.Category biomeCategory) {
        if(!biomes.contains(biomeCategory))
            return;

        try{
            LOG.debug("Configuring Resynth feature: '" + id.toString() + "' for biome: '" + biomeCategory.getName() + "'...");
            onConfigure(builder);
        } catch (Exception e) {
            LOG.error("Failed to configure feature: " + id.toString(), e);
        }
    }

    public ResynthFeature<T> register() {
        return ResynthFeatures.addFeature(this);
    }
}
