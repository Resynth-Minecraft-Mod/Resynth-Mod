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
package com.ki11erwolf.resynth;

import com.ki11erwolf.resynth.proxy.ClientProxy;
import com.ki11erwolf.resynth.proxy.Proxy;
import com.ki11erwolf.resynth.proxy.ServerProxy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.StackLocatorUtil;

/**
 * Resynth mod class.
 */
@Mod(ResynthMod.MOD_ID)
public class ResynthMod {

    //In honor of Minecraft, for the countless hours of fun
    //and entertainment it has given me and millions of other
    //players, even today, as well as for helping me find
    //my passion for programming. Thank you!

    /**
     * The logger for this class.
     */
    private static final Logger LOG = getNewLogger();

    // *************
    // Global Values
    // *************

    /**
     * Resynth mod ID.
     */
    public static final String MOD_ID = "resynth";

    /**
     * Resynth version.
     */
    public static final String MOD_VERSION = "5.1.0";

    /**
     * Minecraft version.
     */
    public static final String MC_VERSION = "[1.16.2]";

    /**
     * Resynth's new/returning user identification file.
     * This servers to separate new Resynth users
     * from existing ones.
     */
    public static final String RESYNTH_NU_FILE = "/resynth.id";

    /**
     * FML initialized proxy. Will be ServerProxy on dedicated server, ClientProxy otherwise.
     */
    private static final Proxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    /**
     * Mod constructor.
     *
     * Calls the construct method of the selected proxy class.
     */
    public ResynthMod(){
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onEnqueueModComs);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onProcessModComs);
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    /**
     * @return a new apache log4j logging instance.
     * Equivalent to {@link LogManager#getLogger()}.
     */
    public static Logger getNewLogger(){
        return LogManager.getLogger(StackLocatorUtil.getCallerClass(2));
    }

    // ************
    // Proxy Events
    // ************

    /**
     * First mod registration event. Called to
     * initialize and register the Resynth mod.
     *
     * @param event forge provided event.
     */
    private void onSetup(final FMLCommonSetupEvent event){
        LOG.info(String.format("Resynth v%s setup has been initiated...", MOD_VERSION));
        proxy.onSetup(event);
    }

    /**
     * Client side mod registration event.
     *
     * @param event forge provided event.
     */
    private void onClientSetup(final FMLClientSetupEvent event) {
        if(proxy instanceof ClientProxy) proxy.onClientSetup(event);
    }

    /**
     * InterModCommunication message send event.
     *
     * @param event forge provided event.
     */
    private void onEnqueueModComs(final InterModEnqueueEvent event){
        proxy.onEnqueueModComs(event);
    }

    /**
     * InterModCommunication process event.
     *
     * @param event forge provided event.
     */
    private void onProcessModComs(final InterModProcessEvent event){
        proxy.onProcessModComs(event);
    }

    /**
     * Server side registration event.
     *
     * @param event forge provided event.
     */
    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        proxy.onServerStarting(event);
    }

    /**
     * Called when the game is stopping.
     *
     * @param event Forge event.
     */
    @SubscribeEvent
    public static void onServerStopped(FMLServerStoppedEvent event){
        if(!(proxy instanceof ClientProxy)) proxy.onServerStopped(event);
    }
}
