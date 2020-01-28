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
package com.ki11erwolf.resynth.proxy;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

/**
 * Resynth mod proxy interface.
 */
public interface Proxy {

    /**
     * First mod registration event. Called to
     * initialize and register the Resynth mod.
     *
     * @param event forge provided event.
     */
    void setup(final FMLCommonSetupEvent event);

    /**
     * Client side mod registration event.
     *
     * @param event forge provided event.
     */
    default void doClientStuff(final FMLClientSetupEvent event){}

    /**
     * InterModCommunication message send event.
     *
     * @param event forge provided event.
     */
    default void enqueueIMC(@SuppressWarnings("unused") final InterModEnqueueEvent event){}

    /**
     * InterModCommunication process event.
     *
     * @param event forge provided event.
     */
    default void processIMC(@SuppressWarnings("unused") final InterModProcessEvent event){}

    /**
     * Server side registration event.
     *
     * @param event forge provided event.
     */
    default void onServerStarting(@SuppressWarnings("unused") FMLServerStartingEvent event){}

    /**
     * Called when the game is stopping.
     *
     * @param event Forge event.
     */
    default void onServerStopped(@SuppressWarnings("unused") FMLServerStoppedEvent event){}
}
