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
