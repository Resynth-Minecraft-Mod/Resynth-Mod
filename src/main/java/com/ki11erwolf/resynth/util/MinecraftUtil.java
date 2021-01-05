/*
 * Copyright 2018-2021 Ki11er_wolf
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
package com.ki11erwolf.resynth.util;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A set of utilities that help with
 * general Minecraft Modding.
 */
public class MinecraftUtil {

    /**Private Constructor.*/
    private MinecraftUtil() { }

    /**
     * Spawns an item in the world as an entity.
     *
     * @return {@code true} if the item was spawned.
     */
    public static boolean spawnItemInWorld(Item item, World world, BlockPos pos){
        return spawnItemStackInWorld(new ItemStack(item), world, pos);
    }

    /**
     * Spawns an ItemStack in the world as an entity.
     *
     * @return {@code true} if the ItemStack was spawned.
     */
    public static boolean spawnItemStackInWorld(ItemStack stack, World world, BlockPos pos){
        return world.addEntity(new ItemEntity(world,pos.getX(), pos.getY(), pos.getZ(), stack));
    }
}
