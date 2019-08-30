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
package com.ki11erwolf.resynth.plantsets.set;

/**
 * A basic implementation of IBiochemicalSetProperties. Used
 * to specify the default properties of a specific plant set
 * instance (before config is taken into account).
 */
public class BiochemicalSetProperties implements IBiochemicalSetProperties {

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
    public BiochemicalSetProperties(boolean canBonemeal, float chanceToGrow, int numberOfProduceDrops,
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
    public boolean canBonemeal() {
        return canBonemeal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float chanceToGrow() {
        return chanceToGrow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int numberOfProduceDrops() {
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
}
