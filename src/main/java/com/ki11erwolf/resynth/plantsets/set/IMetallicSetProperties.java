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
 * Defines the required "settings" (properties) needed for Metallic
 * plant sets specifically.
 */
public interface IMetallicSetProperties extends PlantSetProperties {

    /**
     * @return the percentage (0.0 - 100.0) chance that seeds
     * will spawn when the final product ore is blown up. May be
     * specified by config.
     */
    float seedSpawnChanceFromOre();

    /**
     * @return the percentage (0.0 - 100.0) chance that seeds
     * will spawn when the plant set produce (Organic Ore) is
     * blown up. May be specified by config.
     */
    float seedSpawnChanceFromOrganicOre();
}
