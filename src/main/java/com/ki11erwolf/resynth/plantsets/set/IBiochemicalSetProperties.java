package com.ki11erwolf.resynth.plantsets.set;

public interface IBiochemicalSetProperties extends PlantSetProperties {

    /**
     * @return the number of produce item the plant type
     * will drop when harvested. May be specified by config.
     */
    int numberOfProduceDrops();

    /**
     * @return the percentage (0.0 - 100.0) chance that seeds
     * will spawn when the one of the plant sets mobs
     * are killed.
     */
    float seedSpawnChanceFromMob();

    /**
     * @return the percentage (0.0 - 100.0) chance that seeds
     * will spawn when the plant sets produce is smashed.
     */
    float seedSpawnChanceFromBulb();
}
