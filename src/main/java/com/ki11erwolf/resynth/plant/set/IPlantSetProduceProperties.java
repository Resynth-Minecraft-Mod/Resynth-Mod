package com.ki11erwolf.resynth.plant.set;

/**
 * The represents and defines any properties implementation which provides a
 * set of changable configuration type values, or config settings, that defines
 * how a particular {@link PlantSet}'s {@link PlantSet#getProduceItem() produce
 * item} will behave in a furnace.
 */
public interface IPlantSetProduceProperties {

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
    int resourceCount();

    /**
     * Explicitly declares the amount time that it will take single item of {@link
     * PlantSet#getProduceItem() plant produce} to be smelted in a furnace. The
     * time is measured in Minecraft ticks, where one tick is one twentieth of
     * a second (0.05 seconds). The time given should always be a positive integer.
     *
     * @return the amount of time, in ticks, that a single item of {@link
     * PlantSet#getProduceItem() produce} takes to smelt in a furnace.
     */
    int smeltingTime();

    /**
     * Explicitly declares the amount of experience (xp) points a single item of
     * {@link PlantSet#getProduceItem() plant produce} will give the player when
     * smelted in a furnace.
     *
     * @return the amount of experience points that a single item of {@link
     * PlantSet#getProduceItem() produce} will give the player when smelted.
     */
    double experienceWorth();

}
