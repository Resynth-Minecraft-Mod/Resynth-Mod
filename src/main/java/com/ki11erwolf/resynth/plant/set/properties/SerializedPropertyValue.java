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

import java.util.Objects;

enum SerializedPropertyValue {

    TYPE_OF_PROPERTIES("type"),

    PROBABILITY_OF_GROWING("growth-probability"),

    FERTILIZED_BY_BONEMEAL("fertiliser"),

    SMELTED_PRODUCE_YIELD("produce-yield"),

    PLANT_HARVEST_YIELD("plant-yield"),

    SEED_SPAWN_PROBABILITY_FROM_PRODUCE("produce-drop-probability"),

    SEED_SPAWN_PROBABILITY_FROM_SOURCE("source-drop-probability"),

    CRYSTALLINE_PRODUCE_SEED_YIELD("crystalline-produce-yield");

    public final String key;

    SerializedPropertyValue(String key) {
        this.key = Objects.requireNonNull(key);
    }
}
