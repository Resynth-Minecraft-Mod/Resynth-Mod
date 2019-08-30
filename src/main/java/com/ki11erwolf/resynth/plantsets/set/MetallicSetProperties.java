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
 * A basic implementation of IMetallicSetProperties. Used
 * to specify the default properties of a specific plant set
 * instance (before config is taken into account).
 */
public class MetallicSetProperties implements IMetallicSetProperties {

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
    public MetallicSetProperties(boolean canBonemeal, float chanceToGrow, float seedSpawnChanceFromOre,
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
}
