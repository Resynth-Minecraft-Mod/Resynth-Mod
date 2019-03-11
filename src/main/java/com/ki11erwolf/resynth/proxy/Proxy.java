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
package com.ki11erwolf.resynth.proxy;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

/**
 * Client/Server proxy interface.
 */
public interface Proxy {

    /**
     * Called on Resynth mod class construction.
     *
     * Constructs the mod depending on what side it's running from.
     */
    void construct();

    /**
     * Proxy handler for Forges common setup event.
     *
     * @param setupEvent FMLCommonSetupEvent
     */
    default void commonSetup(FMLCommonSetupEvent setupEvent){
        //NO-OP
    }

    /**
     * Proxy handler for Forges common setup event.
     *
     * @param setupEvent FMLClientSetupEvent
     */
    default void clientSetup(FMLClientSetupEvent setupEvent){
        //NO-OP
    }

    /**
     * Proxy handler for Forges complete event.
     *
     * @param completeEvent FMLLoadCompleteEvent
     */
    void complete(FMLLoadCompleteEvent completeEvent);
}