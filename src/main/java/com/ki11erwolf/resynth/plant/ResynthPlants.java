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

import com.ki11erwolf.resynth.ResynthConfig;
import com.ki11erwolf.resynth.plant.block.BlockPlantBase;
import com.ki11erwolf.resynth.plant.block.BlockPlantOre;
import com.ki11erwolf.resynth.plant.item.ItemPlantSeed;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * List of all plants.
 */
public final class ResynthPlants {

    /**
     * The iron plant type.
     */
    public static final PlantMetallic PLANT_IRON = new PlantMetallic("iron", Blocks.IRON_ORE){
        @Override
        protected int getFloweringPeriod() {return ResynthConfig.PLANT_IRON.floweringPeriod;}

        @Override
        protected boolean canBoneMeal() {return ResynthConfig.PLANT_IRON.canBonemeal;}

        @Override
        protected int getOreSeedDropChance() {return ResynthConfig.PLANT_IRON.oreSeedDropChance;}

        @Override
        protected int getOrganicOreSeedDropChance() {return ResynthConfig.PLANT_IRON.organicOreSeedDropChance;}

        @Override
        protected boolean doesOreDropSeeds() {return ResynthConfig.PLANT_IRON.oreDropSeeds;}

        @Override
        protected boolean doesOrganicOreDropSeeds() {return ResynthConfig.PLANT_IRON.organicOreDropSeeds;}

        @Override
        public ItemStack getResult() {return new ItemStack(Items.IRON_INGOT, ResynthConfig.PLANT_IRON.yield);}
    };

    //Static class.
    private ResynthPlants(){}

    /**
     * @return an array of the metallic plants to register.
     */
    public static PlantMetallic[] getMetallicPlants(){
        return ResynthPlantRegistry.getMetallicPlants();
    }

    /**
     * @return an array of the plant blocks to register.
     */
    public static BlockPlantBase[] getPlantBlocks(){
        return ResynthPlantRegistry.getPlantBlocks();
    }

    /**
     * @return an array of the seed items to register.
     */
    public static ItemPlantSeed[] getSeedItems(){
        return ResynthPlantRegistry.getSeedItems();
    }

    /**
     * @return an array of the plant ore blocks to register.
     */
    public static BlockPlantOre[] getOreBlocks(){
        return ResynthPlantRegistry.getOreBlocks();
    }

}
