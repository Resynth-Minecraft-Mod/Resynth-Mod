/*
 * Copyright 2018-2021 Ki11er_wolf
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

import com.ki11erwolf.resynth.ResynthRecipes;
import com.ki11erwolf.resynth.plant.set.properties.AbstractPlantSetProperties;
import com.ki11erwolf.resynth.plant.set.properties.AbstractProduceProperties;

import java.util.Objects;
import java.util.function.Function;

/**
 * A publicly visible registry that provides
 * a solid API for obtaining plant sets.
 */
public class PlantSetAPI {

    /**Private Constructor.*/
    private PlantSetAPI(){}

    /**
     * Allows iterating over a list of plant sets specified by type.
     *
     * @param setType the type of plant sets to iterate over.
     * @param action the action to perform for each plant set.
     */
    public static void foreachSet(SetType setType, Function<PlantSet<?, ?>, Void> action){
        PlantSetRegistry.streamPlantSets().filter(setType::matches).forEach(action::apply);
    }

    /**
     * Obtains a list of plant sets specified by type.
     *
     * @param setType the type of plant sets to obtain.
     * @return a modifiable list of the obtained plant sets.
     */
    public static PlantSet<?, ?>[] getSetsByType(SetType setType){
        return PlantSetRegistry.streamPlantSets().filter(setType::matches).toArray(PlantSet<?, ?>[]::new);
    }

    public static PlantSet<?, ?> getSetByName(String name) {
        return PlantSetRegistry.streamPlantSets().filter(
                (set) -> set.getSetName().equals(Objects.requireNonNull(name))
        ) .findFirst().orElse(null);
    }

    /**
     * @return the {@link ResynthRecipes.RecipeProvider} instance
     * for PlantSets. <b>Only provided so that it can be registered
     * in {@link ResynthRecipes}.</b>
     */
    public static ResynthRecipes.RecipeProvider getPlantSetRecipes(){
        return PlantSetRecipes.INSTANCE;
    }

    public static void synchronizePlantSetProperties(String setName, AbstractPlantSetProperties properties,
                                                     AbstractProduceProperties produceProperties) {
        PropertiesSynchronizer.INSTANCE.handlePropertiesSynchronizing(
                Objects.requireNonNull(setName), Objects.requireNonNull(properties), Objects.requireNonNull(produceProperties)
        );
    }

    /**
     * Represents a plant set type.
     *
     * Allows obtaining plant sets based on type
     * without having access to the actual plant
     * set types.
     */
    public enum SetType {

        /**
         * Represents the Biochemical plant set type.
         */
        BIOCHEMICAL{
            @Override
            public boolean matches(PlantSet<?, ?> set){
                return set instanceof BiochemicalSet;
            }
        },

        /**
         * Represents the Crystalline plant set type.
         */
        CRYSTALLINE{
            @Override
            public boolean matches(PlantSet<?, ?> set){
                return set instanceof CrystallineSet;
            }
        },

        /**
         * Represents the Metallic plant set type.
         */
        METALLIC{
            @Override
            public boolean matches(PlantSet<?, ?> set){
                return set instanceof MetallicSet;
            }
        },

        /**
         * Represents all the plant set types (biochemical,
         * metallic and crystalline).
         */
        ALL{
            @Override
            public boolean matches(PlantSet<?, ?> set){
                return set != null;
            }
        };

        /**
         * Used to check if the given plant set matches
         * this type.
         *
         * @param set the given plant set.
         * @return {@code true} if and only if the given
         * plant set matches this enum representation.
         */
        public abstract boolean matches(PlantSet<?, ?> set);
    }
}
