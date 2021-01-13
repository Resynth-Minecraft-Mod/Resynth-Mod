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
package com.ki11erwolf.resynth.plant.set.properties;

import com.ki11erwolf.resynth.util.JSerializer;

/**
 * A basic implementation of IMetallicSetProperties. Used
 * to specify the default properties of a specific plant set
 * instance (before config is taken into account).
 */
public class MetallicProperties implements AbstractMetallicProperties {

    /**
     * {@code true} if bonemeal can be used on
     * the plant to grow it.
     */
    private final boolean canBonemeal;

    /**
     * The percentage chance of the plant
     * growing on a random tick.
     */
    private final float chanceToGrow;

    /**
     * The percentage chance that seeds will
     * spawn when the final product ore block
     * is blown up.
     */
    private final float seedSpawnChanceFromOre;

    /**
     * The percentage chance that seeds will
     * spawn when the plant produce is blown up.
     */
    private final float seedSpawnChanceFromOrganicOre;

    /**
     * @param canBonemeal {@code true} if the plant set instance can be grown with bonemeal by default.
     * @param chanceToGrow the default growth chance of the plant set instance.
     * @param seedSpawnChanceFromOre the chance seeds will spawn when the ore block is blown up.
     * @param  seedSpawnChanceFromOrganicOre the chance seeds will spawn when the organic ore block is blown up.
     */
    public MetallicProperties(boolean canBonemeal, float chanceToGrow, float seedSpawnChanceFromOre,
                              float seedSpawnChanceFromOrganicOre){
        this.canBonemeal = canBonemeal;
        this.chanceToGrow = chanceToGrow;
        this.seedSpawnChanceFromOre = seedSpawnChanceFromOre;
        this.seedSpawnChanceFromOrganicOre = seedSpawnChanceFromOrganicOre;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean bonemealGrowth() {
        return canBonemeal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float growthProbability() {
        return chanceToGrow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float seedSpawnChanceFromOre() {
        return seedSpawnChanceFromOre;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float seedSpawnChanceFromOrganicOre() {
        return seedSpawnChanceFromOrganicOre;
    }

    // ##############
    // # Serializer #
    // ##############

    static class MetallicPropertiesSerializer extends JSerializer<AbstractMetallicProperties> {

        MetallicPropertiesSerializer() {
            super("metallic-plant-set-properties");
        }

        @Override
        protected void objectToData(AbstractMetallicProperties object, JSerialDataIO dataIO) {
            dataIO.add(SerializedPropertyValue.TYPE_OF_PROPERTIES.key, "metallic");
            dataIO.add(SerializedPropertyValue.PROBABILITY_OF_GROWING.key, object.growthProbability());
            dataIO.add(SerializedPropertyValue.FERTILIZED_BY_BONEMEAL.key, object.bonemealGrowth());
            dataIO.add(SerializedPropertyValue.SEED_SPAWN_PROBABILITY_FROM_SOURCE.key, object.seedSpawnChanceFromOre());
            dataIO.add(SerializedPropertyValue.SEED_SPAWN_PROBABILITY_FROM_PRODUCE.key, object.seedSpawnChanceFromOrganicOre());
        }

        @Override
        protected AbstractMetallicProperties dataToObject(AbstractMetallicProperties suggestedObject,
                                                          JSerialDataIO dataIO) throws Exception {
            if(!"metallic".equals(dataIO.getString(SerializedPropertyValue.TYPE_OF_PROPERTIES.key)))
                throw new Exception("Not of type Metallic!");

            boolean bonemealGrowth = dataIO.getBoolean(SerializedPropertyValue.FERTILIZED_BY_BONEMEAL.key);
            float growthProbability = dataIO.get(SerializedPropertyValue.PROBABILITY_OF_GROWING.key).getAsFloat();
            float seedSpawnChanceFromMob = dataIO.get(SerializedPropertyValue.SEED_SPAWN_PROBABILITY_FROM_SOURCE.key).getAsFloat();
            float seedSpawnChanceFromBulb = dataIO.get(SerializedPropertyValue.SEED_SPAWN_PROBABILITY_FROM_PRODUCE.key).getAsFloat();

            return new MetallicProperties(bonemealGrowth, growthProbability, seedSpawnChanceFromMob, seedSpawnChanceFromBulb);
        }

        @Override
        protected AbstractMetallicProperties createInstance() {
            return new MetallicProperties(false, 0, 0, 0);
        }
    }
}
