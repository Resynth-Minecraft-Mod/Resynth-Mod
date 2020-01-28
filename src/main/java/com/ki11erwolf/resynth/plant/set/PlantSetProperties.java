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
 * Defines the required "settings" (properties) needed for all
 * plant sets.
 */
public interface PlantSetProperties {

    /**
     * @return {@code true} if the plant block
     * in the plant set can be grown with bonemeal.
     */
    boolean canBonemeal();

    /**
     * @return the percentage chance the plant
     * in the plant set will grow on a random tick.
     */
    float chanceToGrow();
}
