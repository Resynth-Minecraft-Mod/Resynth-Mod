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
package com.ki11erwolf.resynth.plant.set;

import net.minecraftforge.common.MinecraftForge;

/**
 * Allows implementing plant sets (e.g. {@link CrystallineSet})
 * to easily register game hooks that allow spawning
 * plant seeds in the world from player actions.
 * <p/>
 * Provides implementing classes with an easy way
 * of registration and some utilities.
 */
//TODO: Add utility
class PlantSetSeedHooks {

    /**
     * {@code true} if this SeedHooks instance
     * has already been registered, {@code false}
     * otherwise.
     */
    private boolean registered = false;

    /**
     * Registers this SeedHooks instance if it
     * has not yet been registered.
     */
    void register(){
        if(registered)
            return;

        MinecraftForge.EVENT_BUS.register(this);
        registered = true;
    }
}
