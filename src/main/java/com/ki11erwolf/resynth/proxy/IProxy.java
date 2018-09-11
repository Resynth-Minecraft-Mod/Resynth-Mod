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
package com.ki11erwolf.resynth.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Client/Server proxy interface.
 */
public interface IProxy {

    /**
     * On FML pre initialization.
     *
     * @param event pre init event.
     */
    void preInit(FMLPreInitializationEvent event);

    /**
     * On FML initialization.
     *
     * @param event init event.
     */
    void init(FMLInitializationEvent event);

    /**
     * On FML post initialization.
     *
     * @param event post init event.
     */
    void postInit(FMLPostInitializationEvent event);

}