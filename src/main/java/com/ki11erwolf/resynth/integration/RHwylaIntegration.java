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
package com.ki11erwolf.resynth.integration;

import com.ki11erwolf.resynth.ResynthMod;
import mcp.mobius.waila.api.*;
import net.minecraft.block.Block;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows Resynth to integrate with the Hwyla (Fork of Waila) mod.
 *
 * Provides a basic API that allows queuing Blocks that implement
 * a Hwyla provider interface for Hwyla registration. This
 * class also handles registering the queued providers.
 */
@WailaPlugin
@SuppressWarnings("unused")//Reflection
public class RHwylaIntegration implements mcp.mobius.waila.api.IWailaPlugin{

    /**
     * The logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    // ************
    // INTERNAL API
    // ************

    /**
     * A list of all registered blocks that implement a Hwyla data provider
     * interface that are queued for registration.
     */
    //Generic object array as providers can be many things.
    private static final List<Block> PROVIDERS = new ArrayList<>(4);

    /**
     * Will queue the given block for registration as a Hwyla provider,
     * provided they implement a Hwyla provider interface (IComponentProvider,
     * IServerDataProvider). This check is done by the API so the calling
     * method doesn't need to check for this.
     *
     * @param provider the block to register provided it's a Hwyla
     *                 provider.
     */
    public static void addIfProvider(Block provider){
        if(provider instanceof IComponentProvider || provider instanceof IServerDataProvider)
            PROVIDERS.add(provider);
    }

    // *********
    // Internals
    // *********

    /**
     * Handles registering all queued provider to Hwyla.
     *
     * @param registrar Hwyla provided provider registrar.
     */
    @Override
    public void register(IRegistrar registrar) {
        LOG.info("Setting up hwyla...");

        for(Object provider : PROVIDERS){
            LOG.info("Registering provider: " + provider.getClass());

            if(provider instanceof IComponentProvider){
                registrar.registerComponentProvider(
                        (IComponentProvider)provider, TooltipPosition.HEAD, provider.getClass()
                );
                registrar.registerComponentProvider(
                        (IComponentProvider)provider, TooltipPosition.BODY, provider.getClass()
                );
                registrar.registerComponentProvider(
                        (IComponentProvider)provider, TooltipPosition.TAIL, provider.getClass()
                );
            }

            if(provider instanceof IServerDataProvider){
                //noinspection unchecked //Should be fine
                registrar.registerBlockDataProvider((IServerDataProvider)provider, provider.getClass());
            }
        }
    }
}
