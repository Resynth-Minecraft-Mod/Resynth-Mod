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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A publicly visible registry that provides
 * a solid API for obtaining plant sets.
 */
public class PublicPlantSetRegistry {

    /**Private Constructor.*/
    private PublicPlantSetRegistry(){}

    /**
     * Allows iterating over a list of plant sets specified by type.
     *
     * @param setType the type of plant sets to iterate over.
     * @param action the action to perform for each plant set.
     */
    @SuppressWarnings("unused")
    public static void foreach(SetType setType, Function<PlantSet, Void> action){
        for(PlantSet plantSet : PlantSetRegistry.getPlantSets()){
            if(setType.matches(plantSet))
                action.apply(plantSet);
        }
    }

    /**
     * Obtains a list of plant sets specified by type.
     *
     * @param setType the type of plant sets to obtain.
     * @return a modifiable list of the obtained plant sets.
     */
    public static PlantSet[] getSets(SetType setType){
        List<PlantSet> plantSets = new ArrayList<>();

        for(PlantSet plantSet : PlantSetRegistry.getPlantSets()){
            System.out.println("set class -> " + plantSet.getClass().getSuperclass());
            if(setType.matches(plantSet))
                plantSets.add(plantSet);
        }

        return plantSets.toArray(new PlantSet[0]);
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
            public boolean matches(PlantSet set){
                return set instanceof BiochemicalSet;
            }
        },

        /**
         * Represents the Crystalline plant set type.
         */
        CRYSTALLINE{
            @Override
            public boolean matches(PlantSet set){
                return set instanceof CrystallineSet;
            }
        },

        /**
         * Represents the Metallic plant set type.
         */
        METALLIC{
            @Override
            public boolean matches(PlantSet set){
                return set instanceof MetallicSet;
            }
        },

        /**
         * Represents all the plant set types (biochemical,
         * metallic and crystalline).
         */
        ALL{
            @Override
            public boolean matches(PlantSet set){
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
        public abstract boolean matches(PlantSet set);
    }
}
