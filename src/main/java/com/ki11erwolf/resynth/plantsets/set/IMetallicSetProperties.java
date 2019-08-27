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
