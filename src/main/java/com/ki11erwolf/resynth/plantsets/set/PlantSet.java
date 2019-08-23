package com.ki11erwolf.resynth.plantsets.set;

import com.ki11erwolf.resynth.plantsets.block.BlockPlant;
import com.ki11erwolf.resynth.plantsets.item.ItemSeeds;
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
public class PlantSet<P extends BlockPlant> {

    /**
     * The name of the plant set instance (e.g. diamond).
     */
    private final String setName;

    /**
     * The name of the plant set type (e.g. crystalline).
     */
    private final String setTypeName;

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
    PlantSet(String setTypeName, String setName, PlantSetSeedHooks seedHooks){
        this.setTypeName = Objects.requireNonNull(setTypeName);
        this.setName = Objects.requireNonNull(setName);
        seedHooks.register();
    }

    /**
     * @return The name of the plant set type (e.g. crystalline).
     */
    @SuppressWarnings("unused")
    public String getSetTypeName(){
        return this.setTypeName;
    }

    /**
     * @return The name of the plant set instance (e.g. diamond).
     */
    @SuppressWarnings("WeakerAccess")
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
