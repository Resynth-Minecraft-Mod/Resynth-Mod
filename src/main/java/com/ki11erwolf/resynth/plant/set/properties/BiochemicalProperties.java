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
 * A basic implementation of IBiochemicalSetProperties. Used
 * to specify the default properties of a specific plant set
 * instance (before config is taken into account).
 */
public class BiochemicalProperties implements AbstractBiochemicalProperties {

    /**
     * Flag whether or not the plant set instance
     * can have bonemeal applied to it by default.
     */
    private final boolean canBonemeal;

    /**
     * The default growth chance of the plant
     * set instance.
     */
    private final float chanceToGrow;

    /**
     * The default number of produce items
     * the plant set instance drops when
     * fully grown and broken/harvested.
     */
    private final int numberOfProduceDrops;

    /**
     * The default chance that seeds will spawn
     * when one of the sets mobs are killed.
     */
    private final float seedSpawnChanceFromMob;

    /**
     * The default chance that seeds will spawn
     * when the sets produce is smashed.
     */
    private final float seedSpawnChanceFromBulb;

    /**
     * @param canBonemeal {@code true} if the plant set instance can be grown with bonemeal by default.
     * @param chanceToGrow the default growth chance of the plant set instance.
     * @param numberOfProduceDrops the default number of produce item the plant block drops
     *                             when fully grown and broken/harvested.
     * @param seedSpawnChanceFromMob the default chance seeds will spawn when one of the sets
     *                               mobs are killed.
     * @param seedSpawnChanceFromBulb the default chance seeds will spawn when the sets
     *                                 produce item is smashed.
     */
    public BiochemicalProperties(boolean canBonemeal, float chanceToGrow, int numberOfProduceDrops,
                                 float seedSpawnChanceFromMob, float seedSpawnChanceFromBulb){
        this.canBonemeal = canBonemeal;
        this.chanceToGrow = chanceToGrow;
        this.numberOfProduceDrops = numberOfProduceDrops;
        this.seedSpawnChanceFromMob = seedSpawnChanceFromMob;
        this.seedSpawnChanceFromBulb = seedSpawnChanceFromBulb;
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
    public int plantYield() {
        return numberOfProduceDrops;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float seedSpawnChanceFromMob() {
        return seedSpawnChanceFromMob;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float seedSpawnChanceFromBulb() {
        return seedSpawnChanceFromBulb;
    }

    // ##############
    // # Serializer #
    // ##############

    static class BiochemicalPropertiesSerializer extends JSerializer<AbstractBiochemicalProperties> {

        BiochemicalPropertiesSerializer() {
            super("biochemical-plant-set-properties");
        }

        @Override
        protected void objectToData(AbstractBiochemicalProperties object, JSerialDataIO dataIO) {
            dataIO.add(SerializedPropertyValue.TYPE_OF_PROPERTIES.key, "biochemical");
            dataIO.add(SerializedPropertyValue.PROBABILITY_OF_GROWING.key, object.growthProbability());
            dataIO.add(SerializedPropertyValue.FERTILIZED_BY_BONEMEAL.key, object.bonemealGrowth());
            dataIO.add(SerializedPropertyValue.PLANT_HARVEST_YIELD.key, object.plantYield());
            dataIO.add(SerializedPropertyValue.SEED_SPAWN_PROBABILITY_FROM_SOURCE.key, object.seedSpawnChanceFromMob());
            dataIO.add(SerializedPropertyValue.SEED_SPAWN_PROBABILITY_FROM_PRODUCE.key, object.seedSpawnChanceFromBulb());
        }

        @Override
        protected AbstractBiochemicalProperties dataToObject(AbstractBiochemicalProperties suggestedObject,
                                                             JSerialDataIO dataIO) throws Exception {
            if(!"biochemical".equals(dataIO.getString(SerializedPropertyValue.TYPE_OF_PROPERTIES.key)))
                throw new Exception("Not of type Biochemical!");

            boolean bonemealGrowth = dataIO.getBoolean(SerializedPropertyValue.FERTILIZED_BY_BONEMEAL.key);
            int plantYield = dataIO.get(SerializedPropertyValue.PLANT_HARVEST_YIELD.key).getAsInt();
            float growthProbability = dataIO.get(SerializedPropertyValue.PROBABILITY_OF_GROWING.key).getAsFloat();
            float seedSpawnChanceFromMob = dataIO.get(SerializedPropertyValue.SEED_SPAWN_PROBABILITY_FROM_SOURCE.key).getAsFloat();
            float seedSpawnChanceFromBulb = dataIO.get(SerializedPropertyValue.SEED_SPAWN_PROBABILITY_FROM_PRODUCE.key).getAsFloat();

            return new BiochemicalProperties(
                    bonemealGrowth, growthProbability, plantYield, seedSpawnChanceFromMob, seedSpawnChanceFromBulb
            );
        }

        @Override
        protected AbstractBiochemicalProperties createInstance() {
            return new BiochemicalProperties(
                    false, 0, 0, 0, 0
            );
        }
    }
}
