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
 * A basic implementation of ICrystallineSetProperties. Used
 * to specify the default properties of a specific plant set
 * instance (before config is taken into account).
 */
public class CrystallineProperties implements AbstractCrystallineProperties {

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
     * fully grown and broken.
     */
    private final int numberOfProduceDrops;

    /**
     * The default chance that seeds will spawn
     * when the sets ore block is mined.
     */
    private final float seedSpawnChanceFromOre;

    /**
     * The default chance that seeds will spawn
     * when the sets produce is left in water
     * to despawn.
     */
    private final float seedSpawnChanceFromShard;

    /**
     * The amount of the plant sets output resource item to
     * give the player for crafting the plant sets seed item.
     */
    private final int resourcesPerSeeds;

    /**
     * @param canBonemeal {@code true} if the plant set instance can be grown with bonemeal by default.
     * @param chanceToGrow the default growth chance of the plant set instance.
     * @param numberOfProduceDrops the default number of produce item the plant block drops
     *                             when fully grown and broken.
     * @param seedSpawnChanceFromOre the default chance seeds will spawn when the sets
     *                               ore block is mined.
     * @param seedSpawnChanceFromShard the default chance seeds will spawn when the sets
     *                                 produce item is left to despawn in water.
     */
    public CrystallineProperties(boolean canBonemeal, float chanceToGrow, int numberOfProduceDrops,
                                 float seedSpawnChanceFromOre, float seedSpawnChanceFromShard){
        this(canBonemeal, chanceToGrow, numberOfProduceDrops, seedSpawnChanceFromOre, seedSpawnChanceFromShard, 2);
    }

    /**
     * @param canBonemeal {@code true} if the plant set instance can be grown with bonemeal by default.
     * @param chanceToGrow the default growth chance of the plant set instance.
     * @param numberOfProduceDrops the default number of produce item the plant block drops
     *                             when fully grown and broken.
     * @param seedSpawnChanceFromOre the default chance seeds will spawn when the sets
     *                               ore block is mined.
     * @param seedSpawnChanceFromShard the default chance seeds will spawn when the sets
     *                                 produce item is left to despawn in water.
     * @param resourcesPerSeeds The amount of the plant sets output resource item to give
     *                          the player for crafting the plant sets seed item.
     */
    public CrystallineProperties(boolean canBonemeal, float chanceToGrow, int numberOfProduceDrops, float seedSpawnChanceFromOre,
                                 float seedSpawnChanceFromShard, int resourcesPerSeeds){
        this.canBonemeal = canBonemeal;
        this.chanceToGrow = chanceToGrow;
        this.numberOfProduceDrops = numberOfProduceDrops;
        this.seedSpawnChanceFromOre = seedSpawnChanceFromOre;
        this.seedSpawnChanceFromShard = seedSpawnChanceFromShard;
        this.resourcesPerSeeds = Math.min(resourcesPerSeeds, 64);
    }


    /**
     * @return {@code true} if plant set instance
     * can have bonemeal applied to it by default.
     */
    @Override
    public boolean bonemealGrowth() {
        return canBonemeal;
    }

    /**
     * @return The default growth chance of the plant
     * set instance.
     */
    @Override
    public float growthProbability() {
        return chanceToGrow;
    }

    /**
     * @return The default number of produce items
     * the plant set instance drops when
     * fully grown and broken.
     */
    @Override
    public int plantYield() {
        return numberOfProduceDrops;
    }

    /**
     * @return The default chance that seeds will spawn
     * when the sets ore block is mined.
     */
    @Override
    public float seedSpawnChanceFromOre() {
        return seedSpawnChanceFromOre;
    }

    /**
     * @return The default chance that seeds will spawn
     * when the sets produce is left in water
     * to despawn.
     */
    @Override
    public float seedSpawnChanceFromShard() {
        return seedSpawnChanceFromShard;
    }

    /**
     * {@inheritDoc}
     *
     * @return the {@code resourcesPerSeeds} value given to this
     * objects constructor.
     */
    @Override
    public int seedCraftingYield() {
        return resourcesPerSeeds;
    }

    // ##############
    // # Serializer #
    // ##############

    static class CrystallinePropertiesSerializer extends JSerializer<AbstractCrystallineProperties> {

        CrystallinePropertiesSerializer() {
            super("crystalline-plant-set-properties");
        }

        @Override
        protected void objectToData(AbstractCrystallineProperties object, JSerialDataIO dataIO) {
            dataIO.add(SerializedPropertyValue.TYPE_OF_PROPERTIES.key, "crystalline");
            dataIO.add(SerializedPropertyValue.CRYSTALLINE_PRODUCE_SEED_YIELD.key, object.seedCraftingYield());
            dataIO.add(SerializedPropertyValue.PROBABILITY_OF_GROWING.key, object.growthProbability());
            dataIO.add(SerializedPropertyValue.FERTILIZED_BY_BONEMEAL.key, object.bonemealGrowth());
            dataIO.add(SerializedPropertyValue.PLANT_HARVEST_YIELD.key, object.plantYield());
            dataIO.add(SerializedPropertyValue.SEED_SPAWN_PROBABILITY_FROM_SOURCE.key, object.seedSpawnChanceFromOre());
            dataIO.add(SerializedPropertyValue.SEED_SPAWN_PROBABILITY_FROM_PRODUCE.key, object.seedSpawnChanceFromShard());
        }

        @Override
        protected AbstractCrystallineProperties dataToObject(AbstractCrystallineProperties suggestedObject,
                                                             JSerialDataIO dataIO) throws Exception {
            if(!"crystalline".equals(dataIO.getString(SerializedPropertyValue.TYPE_OF_PROPERTIES.key)))
                throw new Exception("Not of type Crystalline!");

            boolean bonemealGrowth = dataIO.getBoolean(SerializedPropertyValue.FERTILIZED_BY_BONEMEAL.key);
            float growthProbability = dataIO.get(SerializedPropertyValue.PROBABILITY_OF_GROWING.key).getAsFloat();
            float seedSpawnChanceFromMob = dataIO.get(SerializedPropertyValue.SEED_SPAWN_PROBABILITY_FROM_SOURCE.key).getAsFloat();
            float seedSpawnChanceFromBulb = dataIO.get(SerializedPropertyValue.SEED_SPAWN_PROBABILITY_FROM_PRODUCE.key).getAsFloat();
            int plantYield = dataIO.get(SerializedPropertyValue.PLANT_HARVEST_YIELD.key).getAsInt();
            int craftingYield = dataIO.getInteger(SerializedPropertyValue.CRYSTALLINE_PRODUCE_SEED_YIELD.key);

            return new CrystallineProperties(
                    bonemealGrowth, growthProbability, plantYield, seedSpawnChanceFromMob, seedSpawnChanceFromBulb, craftingYield
            );
        }

        @Override
        protected AbstractCrystallineProperties createInstance() {
            return new CrystallineProperties(
                    false, 0, 0, 0, 0
            );
        }
    }
}
