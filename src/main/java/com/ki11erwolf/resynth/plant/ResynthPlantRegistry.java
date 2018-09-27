/*
 * Copyright 2018 Ki11er_wolf
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
import com.ki11erwolf.resynth.plant.block.BlockPlantOre;
import com.ki11erwolf.resynth.plant.item.ItemPlantOreProduce;
import com.ki11erwolf.resynth.plant.item.ItemPlantSeed;

import java.util.ArrayList;

/**
 * Holds a list of all the plants (including seeds and ores) to
 * register to the game.
 */
final class ResynthPlantRegistry {

    //Static class.
    private ResynthPlantRegistry(){}

    /**
     * List of all metallic plants to register to the game.
     */
    private static final ArrayList<PlantMetallic> PLANTS_METALLIC = new ArrayList<>();

    /**
     * List of all crystalline plants to register to the game.
     */
    private static final ArrayList<PlantCrystalline> PLANTS_CRYSTALLINE = new ArrayList<>();

    /**
     * List of plant blocks to register to the game.
     */
    private static final ArrayList<BlockPlantBase> PLANT_BLOCKS = new ArrayList<>();

    /**
     * List of all seed items to register to the game.
     */
    private static final ArrayList<ItemPlantSeed> PLANT_SEEDS = new ArrayList<>();

    /**
     * List of all plant ore blocks to register to the game.
     */
    private static final ArrayList<BlockPlantOre> PLANT_ORE_BLOCKS = new ArrayList<>();

    /**
     * List of all plant produce items to register to the game.
     */
    private static final ArrayList<ItemPlantOreProduce> PLANT_PRODUCE_ITEMS = new ArrayList<>();

    /**
     * Adds a metallic plant instance to the list.
     *
     * @param plantMetallic the metallic plant instance.
     */
    protected static void addPlant(PlantMetallic plantMetallic){
        PLANTS_METALLIC.add(plantMetallic);
    }

    /**
     * Adds a crystalline plant instance to the list.
     *
     * @param plantCrystalline the crystalline plant instance.
     */
    protected static void addPlant(PlantCrystalline plantCrystalline){
        PLANTS_CRYSTALLINE.add(plantCrystalline);
    }

    /**
     * Adds a plant block instance to the list.
     *
     * @param plant the plant block instance.
     */
    protected static void addPlant(BlockPlantBase plant){
        PLANT_BLOCKS.add(plant);
    }

    /**
     * Adds a seed item instance to the list.
     *
     * @param seeds the seed item.
     */
    protected static void addSeeds(ItemPlantSeed seeds){
        PLANT_SEEDS.add(seeds);
    }

    /**
     * Adds a plant ore block instance to the list.
     *
     * @param ore the plant ore block.
     */
    protected static void addOre(BlockPlantOre ore){
        PLANT_ORE_BLOCKS.add(ore);
    }

    /**
     * Adds a plants produce item to the list.
     *
     * @param produce the produce item.
     */
    protected static void addProduce(ItemPlantOreProduce produce){
        PLANT_PRODUCE_ITEMS.add(produce);
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
    protected static ItemPlantSeed[] getSeedItems(){
        return PLANT_SEEDS.toArray(new ItemPlantSeed[0]);
    }

    /**
     * @return an array of the plant ore blocks to register.
     */
    protected static BlockPlantOre[] getOreBlocks(){
        return PLANT_ORE_BLOCKS.toArray(new BlockPlantOre[0]);
    }

    /**
     * @return an array of the metallic plants to register.
     */
    protected static PlantMetallic[] getMetallicPlants(){
        return PLANTS_METALLIC.toArray(new PlantMetallic[0]);
    }

    /**
     * @return an array of all crystalline plants to register.
     */
    protected static PlantCrystalline[] getCrystallinePlants(){
        return PLANTS_CRYSTALLINE.toArray(new PlantCrystalline[0]);
    }

    /**
     * @return an array of all plant produce items to register.
     */
    protected static ItemPlantOreProduce[] getProduce(){
        return PLANT_PRODUCE_ITEMS.toArray(new ItemPlantOreProduce[0]);
    }
}
