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
 * Defines the required "settings" (properties) needed for Crystalline
 * plant sets specifically.
 */
public interface ICrystallineSetProperties extends IPlantSetProperties {

    /**
     * Explicitly declares the amount of {@link PlantSet#getProduceItem() plant
     * produce} Items the plant block from the set will yield when harvested.
     * The amount given should be between one and sixty-four, inclusive. Values
     * outside of this range should be rounded to the closest acceptable value
     * without errors.
     *
     * @return the amount of {@link PlantSet#getProduceItem() produce} Items
     * dropped by the plant block when harvested.
     */
    int plantYield();

    /**
     * Returns the percentage (0.0 - 100.0) chance that seeds
     * will spawn when the product ore is broken. May be
     * specified by config.
     */
    float seedSpawnChanceFromOre();

    /**
     * Returns the percentage (0.0 - 100.0) chance that seeds
     * will spawn when the plant set produce (shard) is left
     * in water to despawn. May be specified by config.
     */
    float seedSpawnChanceFromShard();

    /**
     * Specifies the amount of resource items to give the player
     * when they craft a single item of seeds from this plant set.
     * Allows fine control over the ability to turn seeds into
     * resources directly through crafting. Can be disabled with
     * a value of {@code 0} or less.
     *
     * @return the specific amount of the plant sets output
     * resource item to give the player for crafting the plant sets
     * seed item. The value may also be {@code 0} (zero) or negative,
     * in which case this functionality is disabled.
     */
    int seedCraftingYield();
}
