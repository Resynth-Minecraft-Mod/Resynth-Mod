/*
 * Copyright 2018-2019 Ki11er_wolf
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
package com.ki11erwolf.resynth.plant.set;

/**
 * A basic implementation of ICrystallineSetProperties. Used
 * to specify the default properties of a specific plant set
 * instance (before config is taken into account).
 */
public class CrystallineSetProperties implements ICrystallineSetProperties {

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
     * @param canBonemeal {@code true} if the plant set instance can be grown with bonemeal by default.
     * @param chanceToGrow the default growth chance of the plant set instance.
     * @param numberOfProduceDrops the default number of produce item the plant block drops
     *                             when fully grown and broken.
     * @param seedSpawnChanceFromOre the default chance seeds will spawn when the sets
     *                               ore block is mined.
     * @param seedSpawnChanceFromShard the default chance seeds will spawn when the sets
     *                                 produce item is left to despawn in water.
     */
    public CrystallineSetProperties(boolean canBonemeal, float chanceToGrow, int numberOfProduceDrops,
                                    float seedSpawnChanceFromOre, float seedSpawnChanceFromShard){
        this.canBonemeal = canBonemeal;
        this.chanceToGrow = chanceToGrow;
        this.numberOfProduceDrops = numberOfProduceDrops;
        this.seedSpawnChanceFromOre = seedSpawnChanceFromOre;
        this.seedSpawnChanceFromShard = seedSpawnChanceFromShard;
    }

    /**
     * @return {@code true} if plant set instance
     * can have bonemeal applied to it by default.
     */
    @Override
    public boolean canBonemeal() {
        return canBonemeal;
    }

    /**
     * @return The default growth chance of the plant
     * set instance.
     */
    @Override
    public float chanceToGrow() {
        return chanceToGrow;
    }

    /**
     * @return The default number of produce items
     * the plant set instance drops when
     * fully grown and broken.
     */
    @Override
    public int numberOfProduceDrops() {
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
}
