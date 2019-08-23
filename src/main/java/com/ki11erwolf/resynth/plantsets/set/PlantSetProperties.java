package com.ki11erwolf.resynth.plantsets.set;

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
