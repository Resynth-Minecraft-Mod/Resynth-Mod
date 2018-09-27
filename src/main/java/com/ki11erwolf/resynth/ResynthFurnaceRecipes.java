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
package com.ki11erwolf.resynth;

import com.ki11erwolf.resynth.item.ResynthItems;
import com.ki11erwolf.resynth.plant.PlantCrystalline;
import com.ki11erwolf.resynth.plant.PlantMetallic;
import com.ki11erwolf.resynth.plant.ResynthPlants;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Provides a method to register all furnace recipes.
 */
final class ResynthFurnaceRecipes {

    /**
     * Static util class.
     */
    private ResynthFurnaceRecipes(){}

    /**
     * Adds all furnace recipes to the game.
     */
    static void registerFurnaceRecipes(){
        GameRegistry.addSmelting(
                ResynthItems.ITEM_DENSE_MINERAL_ROCK,
                new ItemStack(ResynthItems.ITEM_MINERAL_CRYSTAL, 1), 0F
        );

        for(PlantMetallic plant : ResynthPlants.getMetallicPlants())
            GameRegistry.addSmelting(plant.getOre(), plant.getResult(), 1.0F);
        for(PlantCrystalline plant : ResynthPlants.getCrystallinePlants())
            GameRegistry.addSmelting(plant.getProduce(), plant.getResult(), 2.0F);
    }
}
