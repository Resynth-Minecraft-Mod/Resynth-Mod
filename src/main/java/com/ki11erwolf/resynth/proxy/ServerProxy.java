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

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.analytics.ConnectEvent;
import com.ki11erwolf.resynth.analytics.NewUserEvent;
import com.ki11erwolf.resynth.analytics.ResynthAnalytics;
import com.ki11erwolf.resynth.analytics.SeedPodEvent;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.GeneralConfig;
import com.ki11erwolf.resynth.config.categories.SeedPodConfig;
//import com.ki11erwolf.resynth.features.ResynthFeatures;
import com.ki11erwolf.resynth.integration.SupportedMods;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Server(common) side initialization class.
 */
public class ServerProxy implements Proxy {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    /**
     * {@inheritDoc}
     *
     * <p/>
     *
     * Handles the bulk of the mod initialization.
     *
     * @param event forge provided event.
     */
    @Override
    public void onSetup(FMLCommonSetupEvent event) {
        sendAnalyticsEvents();
        printItemAndBlockRegisters();
        //ResynthFeatures.init();
    }

    /**
     * Sends the analytics connect event and
     * new user event if enabled and appropriate.
     */
    private void sendAnalyticsEvents(){
        //Connect Event - send first
        ResynthAnalytics.send(new ConnectEvent());

        // Miscellaneous Events

        ResynthAnalytics.send(
                new SeedPodEvent(ResynthConfig.GENERAL_CONFIG.getCategory(SeedPodConfig.class).areDropsEnabled())
        );

        SupportedMods.sendModIntegrationEvents();

        // New User Event
        File resynthFile = new File(System.getProperty("user.home") + "/" + ResynthMod.RESYNTH_NU_FILE);

        if(!resynthFile.exists()){
            ResynthAnalytics.send(new NewUserEvent());
            try {
                boolean create = resynthFile.createNewFile();
                if(!create)
                    LOG.error("Failed to create Resynth NU file");
            } catch (IOException e) {
                LOG.error("Failed to create Resynth NU file", e);
            }
        }
    }

    /**
     * Prints every registered block and item to the console
     * if the debug setting is enabled. This helps with adding
     * new modded plants.
     */
    private void printItemAndBlockRegisters(){
        if(ResynthConfig.GENERAL_CONFIG.getCategory(GeneralConfig.class).isDevHelpEnabled()){
            for(Map.Entry<ResourceLocation, Item> entry : ForgeRegistries.ITEMS.getEntries()){
                LOG.info("<Resynth-Development-Help> | Found item: "
                        + entry.getKey().getNamespace()+ ":"
                        + entry.getKey().toString());
            }

            for(Map.Entry<ResourceLocation, Block> entry : ForgeRegistries.BLOCKS.getEntries()){
                LOG.info("<Resynth-Development-Help> | Found block: "
                        + entry.getKey().getNamespace() + ":"
                        + entry.getKey().toString());
            }
        }
    }
}
