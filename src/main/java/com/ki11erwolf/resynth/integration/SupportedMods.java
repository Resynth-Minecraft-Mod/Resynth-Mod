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
package com.ki11erwolf.resynth.integration;

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.ResynthModdedPlants;
import com.ki11erwolf.resynth.analytics.ModIntegrationEvent;
import com.ki11erwolf.resynth.analytics.ResynthAnalytics;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.Logger;

/**
 * Handles sending {@link ModIntegrationEvent} analytics
 * events; where a ModIntegrationEvent is sent for each
 * mod Resynth adds support for that is also found in the
 * {@link ModList} list of installed mods.
 */
public class SupportedMods {

    /**
     * The static logging instance for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    /**
     * Flag that tells us if the events have been sent yet.
     * Flag is {@code false} until the events have been sent;
     * The flag is then set to {@code true} immediately
     * afterwards. This flag is used to prevent sending the
     * events multiple times.
     */
    private static boolean EVENTS_SENT = false;

    /**
     * Called to send a analytics ModIntegrationEvent for every supported mod that's
     * installed. This method is called from the
     * {@link com.ki11erwolf.resynth.proxy.ServerProxy#onSetup(FMLCommonSetupEvent)}.
     * This method can only be executed once; any additional calls after the first
     * will print a warning to the log and ignore the request.
     *
     * @return {@code true} if the events were sent, {@code false} if the events
     * have already been sent, and this call was ignored.
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean sendModIntegrationEvents(){
        if(EVENTS_SENT) {
            LOG.warn("Multiple calls to 'sendModIntegrationEvents()'! Skipping...");
            return false;
        }

        iterateModsAndSendEvents();

        return EVENTS_SENT = true;
    }

    /**
     * Iterates over every Resynth supported mod representation,
     * and sends a ModIntegrationEvent for each mod representation
     * that represents a mod currently installed.
     */
    private static void iterateModsAndSendEvents(){
        LOG.info("Sending mod integration events...");

        ResynthModdedPlants.Mods.iterateAllMods((mod -> {
            if(ModList.get().isLoaded(mod.getID())){
                ResynthAnalytics.send(
                        new ModIntegrationEvent(mod.getID(), mod.getName())
                );
            }
        }));
    }
}
