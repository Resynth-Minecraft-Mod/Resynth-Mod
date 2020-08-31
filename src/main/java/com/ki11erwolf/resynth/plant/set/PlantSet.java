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

import com.ki11erwolf.resynth.analytics.PlantSetFailureEvent;
import com.ki11erwolf.resynth.analytics.ResynthAnalytics;
import com.ki11erwolf.resynth.plant.block.BlockPlant;
import com.ki11erwolf.resynth.plant.item.ItemSeeds;
import com.ki11erwolf.resynth.util.ItemOrBlock;

import java.util.Objects;

/**
 * Defines and prepares the basics needed to
 * create a Resynth plant set.
 * <p/>
 * A Resynth plant set is a set of items and blocks
 * that make up the minimum requirements to grow
 * a particular resource. This includes a PlantBlock,
 * a produce block/item and a seeds item. This class
 * also allows easily registering plant sets
 * ({@link #register()}).
 * <p/>
 * <b>WARNING: Plant Sets do NOT handle the smelting of
 * produce items into the final resource. This must be done
 * with json furnace (smelting) recipes.</b>
 *
 * @param <P> The plant block for the set.
 */
public class PlantSet<P extends BlockPlant<?>> {

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
    private final PlantSetProperties basicPlantSetProperties;

    /**
     * Will be {@code true} if this plant set has been
     * flagged as a failure.
     */
    private boolean isFailure;

    /**
     * The plant block instance in the set.
     */
    P plantBlock;

    /**
     * The seeds item instance in the set.
     */
    ItemSeeds seedsItem;

    /**
     * The produce item/block in the set.
     */
    ItemOrBlock produceItemOrBlock;

    /**
     * @param setTypeName The name of the plant set type (e.g. crystalline).
     * @param setName The name of the plant set instance (e.g. diamond).
     * @param seedHooks the specific plant sets {@code static} SeedHooks instance.
     */
    PlantSet(String setTypeName, String setName, PlantSetSeedHooks seedHooks, PlantSetProperties properties){
        this.setTypeName = Objects.requireNonNull(setTypeName);
        this.setName = Objects.requireNonNull(setName);
        this.basicPlantSetProperties = properties;
        seedHooks.register();
    }

    /**
     * Allows flagging this plant set as a failure -
     * that is, a plant set that will not work
     * for some or other reason.
     * This is useful when failure can only
     * be determined after the set has been
     * created and registered to the game.
     * This action cannot be undone.
     * <p/>
     * Will prevent seeds from planting as
     * well as notifies the user of a problem.
     */
    void flagAsFailure(){
        if(isFailure)
            return;

        this.getSeedsItem().flagAsFailure();
        ResynthAnalytics.send(new PlantSetFailureEvent(setName));
    }

    /**
     * @return {@code true} if this plant set
     * has been flagged as a failure.
     */
    boolean isFailure(){
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
    @SuppressWarnings("WeakerAccess")
    public P getPlantBlock(){
        return this.plantBlock;
    }

    /**
     * @return the specific produce item/block
     * in the set.
     */
    public ItemOrBlock getProduceItemOrBlock(){
        return this.produceItemOrBlock;
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
    public PlantSetProperties getPlantSetProperties(){
        return this.basicPlantSetProperties;
    }

    /**
     * Registers this plant set to the game
     * (including blocks, items and game
     * mechanics).
     *
     * @return {@code this} - this plant set.
     */
    public PlantSet<P> register(){
        PlantSetRegistry.registerPlantSet(this);
        return this;
    }
}
