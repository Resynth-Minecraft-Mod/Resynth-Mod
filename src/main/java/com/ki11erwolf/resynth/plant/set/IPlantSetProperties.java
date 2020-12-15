/*
 * Copyright 2018-2020 Ki11er_wolf
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
 * Defines the required properties (configurable settings) common
 * to all plants and PlantSets.
 */
public interface IPlantSetProperties {

    /**
     * Defines and returns if the plant block from the set can be
     * grown using bonemeal.
     */
    boolean bonemealGrowth();

    /**
     * Defines and returns the probability, as a percentage ranging
     * from 0% ({@code 0.0F}) to 100% ({@code 100.0F}) - inclusive,
     * that the plant block from the set will grow or yield produce
     * on any given update tick. Determines how fast the plants will
     * grow naturally.
     */
    float growthProbability();
}
