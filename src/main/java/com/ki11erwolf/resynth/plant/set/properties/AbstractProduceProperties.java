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

import com.ki11erwolf.resynth.plant.set.PlantSet;
import com.ki11erwolf.resynth.util.JSerializer;

/**
 * The represents and defines any properties implementation which provides a
 * set of changable configuration type values, or config settings, that defines
 * how a particular {@link PlantSet}'s {@link PlantSet#getProduceItem() produce
 * item} will behave in a furnace.
 */
public interface AbstractProduceProperties extends JSerializer.JSerializable<AbstractProduceProperties> {

    JSerializer<AbstractProduceProperties> SERIALIZER = new ProduceProperties.ProducePropertiesSerializer();

    /**
     * Explicitly declares the amount of resource items that a single item of
     * {@link PlantSet#getProduceItem() plant produce} will yield when smelted
     * in a furnace. The amount given should be between one and sixty-four,
     * inclusive. Values outside of this range should be rounded to the closest
     * acceptable value without errors.
     *
     * @return the amount of resources a that a single item of {@link
     * PlantSet#getProduceItem() produce} will make when smelted.
     */
    int produceYield();

    /**
     * Explicitly declares the amount time that it will take single item of
     * {@link PlantSet#getProduceItem() plant produce} to be completely smelted
     * in a furnace to yield the desired final resource and amount. The time is
     * measured in Minecraft ticks, where one tick is one twentieth of a second
     * (0.05 seconds). The time given should always be a positive integer.
     *
     * @return the amount of time, in ticks, that a single item of {@link
     * PlantSet#getProduceItem() produce} takes to smelt in a furnace.
     */
    int timePerYield();

    /**
     * Explicitly declares the amount of experience (xp) points a single item of
     * {@link PlantSet#getProduceItem() plant produce} will give the player when
     * smelted in a furnace.
     *
     * @return the amount of experience points that a single item of {@link
     * PlantSet#getProduceItem() produce} will give the player when smelted.
     */
    double experiencePoints();

    /**
     * {@inheritDoc}
     */
    @Override
    default JSerializer<AbstractProduceProperties> getSerializer() {
        return SERIALIZER;
    }
}
