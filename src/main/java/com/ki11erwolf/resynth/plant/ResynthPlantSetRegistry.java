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
package com.ki11erwolf.resynth.plant;

import com.ki11erwolf.resynth.plant.block.BlockPlantBase;
import com.ki11erwolf.resynth.plant.block.BlockOrganicPlantOre;
import com.ki11erwolf.resynth.plant.item.ItemPlantProduceBulb;
import com.ki11erwolf.resynth.plant.item.ItemPlantProduceShard;
import com.ki11erwolf.resynth.plant.item.ItemPlantSeeds;

import java.util.ArrayList;

/**
 * Holds a set of lists that contain the items
 * and blocks marked for forge registration
 * by the PlantSet APIs.
 *
 * <p><b>
 *     Note: this registry is for internal
 *     block and item registry only and does not
 *     provide blocks/item to other classes.
 * </b></p>
 */
final class ResynthPlantSetRegistry {

    //Static class.
    private ResynthPlantSetRegistry(){}

    /**
     * List of all metallic plant sets to register to the game.
     */
    private static final ArrayList<PlantSetMetallic> METALLIC_PLANT_SETS = new ArrayList<>();

    /**
     * List of all biochemical plant sets to register to the game.
     */
    private static final ArrayList<PlantSetBiochemical> BIOCHEMICAL_PLANT_SETS = new ArrayList<>();

    /**
     * List of all crystalline plant sets to register to the game.
     */
    private static final ArrayList<PlantSetCrystalline> CRYSTALLINE_PLANT_SETS = new ArrayList<>();

    /**
     * List of all plant blocks to register to the game.
     */
    private static final ArrayList<BlockPlantBase> PLANT_BLOCKS = new ArrayList<>();

    /**
     * List of all seed items to register to the game.
     */
    private static final ArrayList<ItemPlantSeeds> PLANT_SEEDS = new ArrayList<>();

    /**
     * List of all plant produce blocks (organic ore) to register to the game.
     */
    private static final ArrayList<BlockOrganicPlantOre> ORGANIC_ORE_BLOCKS = new ArrayList<>();

    /**
     * List of all plant produce items that extend ItemPlantProduceShard to register to the game.
     */
    private static final ArrayList<ItemPlantProduceShard> PLANT_SHARD_PRODUCE_ITEMS = new ArrayList<>();

    /**
     * List of all plant produce items that extend ItemPlantProduceBulb to register to the game.
     */
    private static final ArrayList<ItemPlantProduceBulb> PLANT_BULB_PRODUCES_ITEMS = new ArrayList<>();

    /**
     * Adds a metallic plant set instance to the registry.
     *
     * @param plantSet the metallic plant set instance.
     */
    protected static void addPlantSet(PlantSetMetallic plantSet){
        METALLIC_PLANT_SETS.add(plantSet);
    }

    /**
     * Adds a crystalline plant set instance to the registry.
     *
     * @param plantSet the crystalline plant set instance.
     */
    protected static void addPlantSet(PlantSetCrystalline plantSet){
        CRYSTALLINE_PLANT_SETS.add(plantSet);
    }

    /**
     * Adds a biochemical plant plant instance to the registry.
     *
     * @param plantSet the biochemical plant set instance.
     */
    protected static void addPlantSet(PlantSetBiochemical plantSet){
        BIOCHEMICAL_PLANT_SETS.add(plantSet);
    }

    /**
     * Adds a plant block instance to the registry.
     *
     * @param plant the plant block instance.
     */
    protected static void addPlantBlock(BlockPlantBase plant){
        PLANT_BLOCKS.add(plant);
    }

    /**
     * Adds a seed item instance to the registry.
     *
     * @param seeds the seed item.
     */
    protected static void addSeeds(ItemPlantSeeds seeds){
        PLANT_SEEDS.add(seeds);
    }

    /**
     * Adds an organic ore produce instance to the registry.
     *
     * @param ore the organic ore block instance.
     */
    protected static void addOrganicOreBlock(BlockOrganicPlantOre ore){
        ORGANIC_ORE_BLOCKS.add(ore);
    }

    /**
     * Adds a plant produce item to the registry that extends
     * ItemPlantProduceShard.
     *
     * @param produce the plant produce item instance.
     */
    protected static void addShardProduce(ItemPlantProduceShard produce){
        PLANT_SHARD_PRODUCE_ITEMS.add(produce);
    }

    /**
     * Adds a plant produce item to the registry that extends
     * ItemPlantProduceBulb.
     *
     * @param produce the plant produce item instance.
     */
    protected static void addBulbProduce(ItemPlantProduceBulb produce){
        PLANT_BULB_PRODUCES_ITEMS.add(produce);
    }

    /**
     * @return an array of the plant blocks to register.
     */
    protected static BlockPlantBase[] getPlantBlocks(){
        return PLANT_BLOCKS.toArray(new BlockPlantBase[0]);
    }

    /**
     * @return an array of the seed items to register.
     */
    protected static ItemPlantSeeds[] getSeedItems(){
        return PLANT_SEEDS.toArray(new ItemPlantSeeds[0]);
    }

    /**
     * @return an array of the organic plant ore blocks to register.
     */
    protected static BlockOrganicPlantOre[] getOrganicOreBlocks(){
        return ORGANIC_ORE_BLOCKS.toArray(new BlockOrganicPlantOre[0]);
    }

    /**
     * @return an array of the metallic plant sets to register.
     */
    protected static PlantSetMetallic[] getMetallicPlantSets() {
        return METALLIC_PLANT_SETS.toArray(new PlantSetMetallic[0]);
    }

    /**
     * @return an array of all the biochemical plant sets to register.
     */
    protected static PlantSetBiochemical[] getBiochemicalPlantSets(){
        return BIOCHEMICAL_PLANT_SETS.toArray(new PlantSetBiochemical[0]);
    }

    /**
     * @return an array of all crystalline plant sets to register.
     */
    protected static PlantSetCrystalline[] getCrystallinePlantSets(){
        return CRYSTALLINE_PLANT_SETS.toArray(new PlantSetCrystalline[0]);
    }

    /**
     * @return an array of all plant shard produce items to register.
     */
    protected static ItemPlantProduceShard[] getShardProduceItems(){
        return PLANT_SHARD_PRODUCE_ITEMS.toArray(new ItemPlantProduceShard[0]);
    }

    /**
     * @return an array of the plant bulb produce items to register.
     */
    protected static ItemPlantProduceBulb[] getBulbProduceItems(){
        return PLANT_BULB_PRODUCES_ITEMS.toArray(new ItemPlantProduceBulb[0]);
    }
}
