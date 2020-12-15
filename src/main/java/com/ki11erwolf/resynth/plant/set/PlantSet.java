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

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.analytics.PlantSetFailureEvent;
import com.ki11erwolf.resynth.analytics.ResynthAnalytics;
import com.ki11erwolf.resynth.plant.block.BlockPlant;
import com.ki11erwolf.resynth.plant.item.ItemSeeds;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @param <P> Plant block class used by the set.
 * @param <S> Source of Seeds. The game entity/object that
 *           this plant sets seeds will drop from, such as
 *           Block or Entity.
 */
public abstract class PlantSet<P extends BlockPlant<?>, S extends IForgeRegistryEntry<?>> {

    //TODO: attempt to implement recipes related to plants in code.
    //TODO: look into programmatically generating plant set assets.
    //TODO: base class handling of seed spawner(s)?

    /**
     * The logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    /**
     * The name of the plant set instance (e.g. diamond).
     */
    private final String setName;

    /**
     * The name of the plant set type (e.g. crystalline).
     */
    private final String setTypeName;

    /**
     * The properties for this plant set.
     */
    private final IPlantSetProperties basicPlantSetProperties;

    private IPlantSetProduceProperties produceProperties;

    /**
     * Will be {@code true} if this plant set has been
     * flagged as a failure.
     */
    private boolean isFailure;

    private S[] cachedSeedSources;

    /**
     * The plant block instance in the set.
     */
    P plantBlock;

    /**
     * The seeds item instance in the set.
     */
    ItemSeeds seedsItem;

    /**
     * The produce item or block in the set.
     */
    IItemProvider produceItem;

    /**
     * @param setTypeName The name of the plant set type (e.g. crystalline).
     * @param setName The name of the plant set instance (e.g. diamond).
     * @param seedHooks the specific plant sets {@code static} SeedHooks instance.
     */
    PlantSet(String setTypeName, String setName, PlantSetSeedHooks seedHooks, IPlantSetProperties properties){
        this.setTypeName = Objects.requireNonNull(setTypeName);
        this.setName = Objects.requireNonNull(setName);
        this.basicPlantSetProperties = Objects.requireNonNull(properties);
        seedHooks.register();
    }

    // ############
    // Seed Sources
    // ############

    abstract S[] onSeedSourcesRequest() throws Exception;

    void initSeedResources() throws IllegalStateException {
        cacheSeedSources();
    }

    // Accessors

    S[] getSeedSources() {
        if(isBroken())
            return null;

        return cachedSeedSources;
    }

    <T extends IForgeRegistryEntry<?>> T[] getSeedSources(Class<T[]> t) {
        S[] seedSources;
        if((seedSources = getSeedSources()) == null)
            return t.cast(Array.newInstance(t.getComponentType(), 0));
        else return t.cast(seedSources);
    }

    // Internal Logic

    private void cacheSeedSources() throws IllegalStateException {
        // Check if cache already created
        if(cachedSeedSources != null) throw new IllegalStateException(
                "Cannot load seed sources because the cache has already been created."
        );

        // Check if already flagged as broken
        if(isBroken()) {
            LOG.error("Cannot load seed sources for the PlantSet '" + getSetName() + "' which has been flagged as broken!");
            cachedSeedSources = null;
            return;
        }

        // Store inside own variable for caching
        cachedSeedSources = loadSeedSources();
    }

    private S[] loadSeedSources() {
        S[] givenSeedSources = null;

        // Get from set implementation
        try {
            givenSeedSources = onSeedSourcesRequest();
        } catch (Exception e) {
            LOG.error(String.format("PlantSet '%s' throw exception when getting seed resources", this.getSetName()), e);
        }

        // Return for caching if okay
        if(checkSeedSources(givenSeedSources))
            return givenSeedSources;

        //Otherwise flag as broken
        this.flagAsBroken();
        return null;
    }

    private boolean checkSeedSources(S[] seedSources) {
        // Check if null or empty array, or null objects in array
        if(seedSources == null || seedSources.length == 0) {
            LOG.error("Got null or empty list of seed resources from PlantSet '" + getSetName() + "'.");
            return false;
        } else if(Arrays.stream(seedSources).anyMatch(Objects::isNull)) {
            LOG.error(String.format("PlantSet '%s' provided list of one or more null seed sources.", getSetName()));
            return false;
        }

        return true;
    }


    /**
     * Permanently flags this plant set as broken beyond repair - that is,
     * to declare it as unusable and problematic both to the player as
     * well as in software. Effectively disabling all use and functionality
     * of the plant, as well prominently displaying to the player the fact
     * that the PlantSet is broken.
     *
     * <p/> Any cause that would prevent the plant set from functioning
     * correctly should flag it with this method. The usual cause would
     * be a missing Block or Item expected to be found in the forge
     * registries, which this plant set uses directly and cannot function
     * without. Likely queried from the forge registries using its {@link
     * net.minecraft.util.ResourceLocation} ID, rather than an object
     * reference. Scenarios like this are bound to occur with additional
     * independent external mods, as items and objects are eventually
     * going to be renamed or removed.
     *
     * <p/> If {@link ResynthAnalytics} is not disabled, an anonymous
     * event will be sent that logs a PlantSet failure with the name
     * of this set. This makes handling broken plant sets a lot easier
     * on the developer side without reliance on bug reports as well.
     */
    void flagAsBroken() {
        if(isFailure)
            return;
        else isFailure = true;

        ResynthAnalytics.send(new PlantSetFailureEvent(setName));
    }

    void setProduceProperties(IPlantSetProduceProperties properties){
        this.produceProperties = Objects.requireNonNull(properties);
    }

    /**
     * @return {@code true} if this plant set has been
     * {@link #flagAsBroken() flagged} as broken, meaning
     * it will not work or function correctly and should
     * be disabled and avoided.
     */
    public boolean isBroken() {
        return this.isFailure;
    }

    /**
     * @return The name of the plant set type (e.g. crystalline).
     */
    public String getSetTypeName(){
        return this.setTypeName;
    }

    /**
     * @return The name of the plant set instance (e.g. diamond).
     */
    public String getSetName(){
        return this.setName;
    }

    /**
     * @return the specific plant block instance
     * in the set.
     */
    public P getPlantBlock(){
        return this.plantBlock;
    }

    /**
     * @return the specific produce item/block
     * in the set.
     */
    public IItemProvider getProduceItem(){
        return this.produceItem;
    }

    /**
     * @return the specific seeds item instance
     * in the set.
     */
    public ItemSeeds getSeedsItem(){
        return this.seedsItem;
    }

    /**
     * @return the properties for this plant set. May be
     * any of the plant set properties types.
     */
    public IPlantSetProperties getPlantSetProperties(){
        return this.basicPlantSetProperties;
    }

    public IPlantSetProduceProperties getProduceProperties() {
        return this.produceProperties;
    }

    /**
     * @return a simple plain text String containing the {@link
     * #getSetName() name} and {@link #getSetTypeName() type}
     * of the specific plant set.
     */
    @Override
    public String toString() {
        return String.format("PlantSet[type=%s, name=%s, failure=%s]",
                this.getSetTypeName(), this.getSetName(), isBroken()
        );
    }

    /**
     * Registers this plant set to the game
     * (including blocks, items and game
     * mechanics).
     *
     * @return {@code this} - this plant set.
     */
    public PlantSet<P, S> register(){
        PlantSetRegistry.registerPlantSet(this);
        return this;
    }
}
