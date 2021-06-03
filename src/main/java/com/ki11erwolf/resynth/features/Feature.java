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

import com.ki11erwolf.resynth.ResynthMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The base class for creating custom Resynth world generation
 * features, such as ore generators.
 *
 * @param <T> the specific type of {@link Feature} implementation.
 *           Typically, this is the implementing class type.
 */
public abstract class Feature<T extends Feature<T>> {

    /**
     * Private class logger.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    /**
     * The unique identifier specific to the exact {@link Feature}
     * implementation.
     */
    private final ResourceLocation id;

    /**
     * A list of {@link Biome Biomes} this Feature is active in.
     */
    private final List<Biome.Category> biomes;

    /**
     * The Feature implementation with configuration.
     */
    private ConfiguredFeature<?, ?> feature;

    /**
     * Protected constructor for Feature implementations.
     *
     * @param id the unique identifier for the implementation.
     * @param biomes the list of {@link Biome Biomes} the
     * implementation should generate features in.
     */
    protected Feature(ResourceLocation id, Biome.Category[] biomes) {
        this.id = Objects.requireNonNull(id);
        this.biomes = Arrays.asList(biomes);
    }

    /**
     * <b>Abstract method for {@link Feature} implementations.</b>
     *
     * <br/> Allows the implementation to construct a {@link
     * ConfiguredFeature} from the {@link Feature}, which
     * determines exactly how the Feature generates in the world.
     */
    protected abstract ConfiguredFeature<?, ?> constructFeature();

    /**
     * <b>Abstract method for {@link Feature} implementations.</b>
     *
     * <br/>Allows implementations to respond to world generation,
     * and integrate the feature into the world.
     */
    protected abstract void configureFeature(BiomeGenerationSettingsBuilder builder) throws Exception;

    /**
     * Configures the Feature implementation instance to generate in the given {@link Biome.Category Biome},
     * provided the Feature is configured to generate in the specific Biome.
     */
    protected final void configure(BiomeGenerationSettingsBuilder builder, Biome.Category biomeCategory) {
        if(!biomes.contains(biomeCategory))
            return;

        try{
            LOG.debug("Configuring Resynth feature: '" + id.toString() + "' for biome: '" + biomeCategory.getName() + "'...");
            configureFeature(builder);
        } catch (Exception e) {
            LOG.error("Failed to configure feature: " + id.toString(), e);
        }
    }

    /**
     * @return the unique identifier specific to the exact
     * {@link Feature} implementation.
     */
    public ResourceLocation getID() {
        return this.id;
    }

    /**
     * @return the Feature implementation with configuration.
     * If the Feature is not constructed when called, the Feature
     * is first constructed.
     */
    public ConfiguredFeature<?, ?> getFeature() {
        return feature == null ? feature = constructFeature() : feature;
    }

    /**
     * Register this specific {@link Feature} implementation
     * to the game. Required for the Feature to work.
     *
     * @return the {@link Feature} implementation as it is
     * registered in the game.
     */
    public Feature<T> register() {
        return ResynthFeatures.addFeature(this);
    }
}
