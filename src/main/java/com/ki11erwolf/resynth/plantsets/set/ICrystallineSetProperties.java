package com.ki11erwolf.resynth.plantsets.set;

/**
 * Defines the required "settings" (properties) needed for crystalline
 * plant sets specifically.
 */
public interface ICrystallineSetProperties extends PlantSetProperties {

    /**
     * @return the number of produce item the plant type
     * will drop when harvested. May be specified by config.
     */
    int numberOfProduceDrops();

    /**
     * @return the percentage (0.0 - 100.0) chance that seeds
     * will spawn when the product ore is broken. May be
     * specified by config.
     */
    float seedSpawnChanceFromOre();

    /**
     * @return the percentage (0.0 - 100.0) chance that seeds
     * will spawn when the plant set produce (shard) is left
     * in water to despawn. May be specified by config.
     */
    float seedSpawnChanceFromShard();
}
