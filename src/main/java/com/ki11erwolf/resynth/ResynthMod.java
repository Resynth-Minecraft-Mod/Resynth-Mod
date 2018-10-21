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

import com.ki11erwolf.resynth.proxy.IProxy;
import com.ki11erwolf.resynth.versioning.ModVersionManager;
import com.ki11erwolf.resynth.versioning.VersionManagerBuilder;
import dmurph.tracking.AnalyticsConfigData;
import dmurph.tracking.AnalyticsRequestData;
import dmurph.tracking.JGoogleAnalyticsTracker;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;

/**
 * Resynth mod class.
 */
@Mod(
        modid = ResynthMod.MOD_ID,
        name = ResynthMod.MOD_NAME,
        version = ResynthMod.MOD_VERSION,
        acceptedMinecraftVersions = ResynthMod.MC_VERSION
)
public class ResynthMod {

    /**
     * Resynth mod ID.
     */
    public static final String MOD_ID = "resynth";

    /**
     * Resynth mod name.
     */
    public static final String MOD_NAME = "Resynth";

    /**
     * Resynth version.
     */
    public static final String MOD_VERSION = "1.1.1";

    /**
     * Minecraft version.
     */
    public static final String MC_VERSION = "[1.12.2]";

    /**
     * The package declaration for the mod.
     */
    public static final String MOD_PACKAGE = "com.ki11erwolf.resynth";

    /**
     * The URL to versions.json file.
     */
    public static final String UPDATE_URL = "https://resynth-minecraft-mod.github.io/mod/versions.json";

    /**
     * FML initialized proxy. Can be server side proxy, client side proxy or both.
     */
    @SidedProxy(clientSide = ResynthMod.CLIENT_PROXY, serverSide = ResynthMod.SERVER_PROXY)
    public static IProxy proxy;

    /**
     * Path (including pack) to the server proxy class.
     */
    static final String SERVER_PROXY = MOD_PACKAGE + ".proxy.ServerProxy";

    /**
     * Path (including package) to the client proxy class.
     */
    static final String CLIENT_PROXY = MOD_PACKAGE + ".proxy.ClientProxy";

    /**
     * Logger for the mod.
     */
    private static Logger logger;

    /**
     * The resynth world generator instance.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private static ResynthWorldGen worldGen;

    /**
     * Stage 1 of 3 in the initialization event.
     *
     * @param event pre init event.
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        logger.info("Entering pre-init phase...");
        proxy.preInit(event);

        VersionManagerBuilder resynthVMBuilder
                = new VersionManagerBuilder(MOD_ID)
                .setEnabled(!ResynthConfig.RESYNTH.disableVersionChecks)
                .setOutOfDateConsoleWarningEnabled(!ResynthConfig.RESYNTH.disableVersionMessage)
                .addVersionJsonFileURL(UPDATE_URL);
        ModVersionManager resynthVersionManager = new ModVersionManager(resynthVMBuilder);
        resynthVersionManager.preInit();

        if(!ResynthConfig.RESYNTH.disableAnalytics)
            try{sendAnalyticsEvent();}
            catch (Exception e){getLogger().error("Failed to send analytics event", e);}
    }

    /**
     * Stage 2 of 3 in the initialization event.
     *
     * @param event init event.
     */
    @EventHandler
    public void init(FMLInitializationEvent event){
        logger.info("Entering init phase...");
        proxy.init(event);
        worldGen = new ResynthWorldGen();
        worldGen.init();
        ResynthFurnaceRecipes.registerFurnaceRecipes();
    }

    /**
     * Stage 3 of 3 in the initialization event.
     *
     * @param event post init event.
     */
    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        logger.info("Entering post-init phase...");
        proxy.postInit(event);
    }

    /**
     * @return the logger for the mod.
     */
    public static Logger getLogger(){
        return logger;
    }

    //***************************
    //     Google Analytics
    //***************************

    /**
     * The Google Analytics tracking code for resynth.
     */
    private static final String TRACKING_CODE = "UA-127648959-1";

    /**
     * The mods beacon page - used as an identifier (page does not exist).
     */
    private static final String BEACON_PAGE = "/mod/mod-client-connect";

    /**
     * The pages title.
     */
    private static final String TITLE = "Resynth - Mod Client Connect";

    /**
     * The resynth host name.
     */
    private static final String HOST_NAME = "https://resynth-minecraft-mod.github.io";

    /**
     * The events category, action and label identifiers.
     */
    private static final String
            EVENT_CATEGORY = "Resynth-Mod-Jar",
            EVENT_ACTION = "Mod-Client-Connect-"
                    + MOD_VERSION + MC_VERSION,
            EVENT_LABEL = "IDENTIFIER-" + getID();

    /**
     * Sends the connected event to google analytics.
     */
    private static void sendAnalyticsEvent(){
        ModVersionManager.disableSSLVerification();
        AnalyticsConfigData data = new AnalyticsConfigData(TRACKING_CODE);
        JGoogleAnalyticsTracker tracker = new JGoogleAnalyticsTracker(data,
                JGoogleAnalyticsTracker.GoogleAnalyticsVersion.V_4_7_2);

        AnalyticsRequestData resynthModEvent = new AnalyticsRequestData();
        resynthModEvent.setPageURL(BEACON_PAGE);
        resynthModEvent.setPageTitle(TITLE);
        resynthModEvent.setHostName(HOST_NAME);

        resynthModEvent.setEventCategory(EVENT_CATEGORY);
        resynthModEvent.setEventAction(EVENT_ACTION);
        resynthModEvent.setEventLabel(EVENT_LABEL);

        tracker.makeCustomRequest(resynthModEvent);
        ModVersionManager.enableSSLVerification();
    }

    /**
     * Attempts to get the first host address of the running
     * machine.
     *
     * This servers to identify returning users
     * from new users. This will fail at times
     * and isn't reliable but does server its
     * purpose of successfully identifying a
     * returning user at least one or twice.
     *
     * This only gets the host address - no personal
     * information such as user names. The host address
     * can chance quite easily and doesn't tie the user
     * down to the value.
     *
     * @return the first found host address found or
     * {@code "Unknown"}.
     */
    private static String getID() {
        String hostAddress = "Unknown";

        try {
            //Use first identifier found.
            //This isn't perfect and will fail to points but
            //will serve its purpose of identifying returning users.
            for (InetAddress aMac : InetAddress.getAllByName(InetAddress.getLocalHost().getHostName())) {
                if(aMac.getHostAddress() != null){
                    hostAddress = aMac.getHostAddress();
                    return  hostAddress;
                }
            }
        } catch (Exception e) {
            getLogger().error("Failed to get identifier.", e);
        }

        return hostAddress;
    }
}
