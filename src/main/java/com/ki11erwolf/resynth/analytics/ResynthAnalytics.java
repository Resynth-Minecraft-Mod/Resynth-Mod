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
package com.ki11erwolf.resynth.analytics;

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.config.categories.SeedPodConfig;
import com.ki11erwolf.resynth.integration.SupportedMods;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.GeneralConfig;
import dmurph.tracking.AnalyticsConfigData;
import dmurph.tracking.AnalyticsRequestData;
import dmurph.tracking.JGoogleAnalyticsTracker;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Provides an API for more easily sending
 * Google Analytics events.
 */
public final class ResynthAnalytics {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    /**
     * Analytics enable/disable flag.
     */
    private static final boolean ENABLED
            = ResynthConfig.GENERAL_CONFIG.getCategory(GeneralConfig.class).isAnalyticsEnabled();

    /**
     * The file on disk, created whenever Resynth is run, which is looked
     * for by Resynth to determine if this run is by a new Resynth user.
     */
    private static final File RESYNTH_USER_FILE = new File(
            System.getProperty("user.home") + "/AppData/Roaming/.minecraft/resynth-user"
    );

    /**
     * The Google Analytics ID code for resynth.
     */
    private static final String CODE = "UA-127648959-1";

    /**
     * The domain name for the Resynth website
     */
    private static final String RESYNTH_DOMAIN = "https://resynth-minecraft-mod.github.io";

    /**
     * Identifier page that all events are sent to. This
     * allows us to see the mod events as "page hits".
     */
    private static final String IDENTIFIER_PAGE = "/jar";

    /**
     * The dummy page title.
     */
    private static final String TITLE = "Resynth - Jar";

    /*
     * Disables SSL Verification before initializing
     * Google Analytics. If this isn't here the event
     * messages will be block by java for some reason.
     */
    static{
        //noinspection ResultOfMethodCallIgnored
        RESYNTH_USER_FILE.getParentFile().mkdir();
        //noinspection ResultOfMethodCallIgnored
        RESYNTH_USER_FILE.getParentFile().mkdirs();

        SSLHelper.disableSSL();
    }

    /**
     * The Google analytics API.
     */
    private static final JGoogleAnalyticsTracker ANALYTICS;

    /*
        Only initializes the tracker if analytics is enabled.
     */
    static {
        if(ENABLED) {
            LOG.info("Analytics has been enabled!");
            ANALYTICS = new JGoogleAnalyticsTracker(
                    new AnalyticsConfigData(CODE),
                    JGoogleAnalyticsTracker.GoogleAnalyticsVersion.V_4_7_2
            );
        } else {
            LOG.info("Analytics has been enabled!");
            ANALYTICS = null;
        }
    }

    //Static class.
    private ResynthAnalytics(){}

    /**
     * Initializes Resynth's Analytics - by sending the basic startup events.
     * This method is called (once) when Resynth is being initialized.
     *
     * <p/> Sends the basic analytics events for all MC runs: {@link ConnectEvent},
     * {@link SeedPodEvent}, {@link ModIntegrationEvent}'s, and {@link NewUserEvent},
     * <b>if and only if analytics are enabled.</b>
     */
    public static void init() {
        //Connect Event - send first
        ResynthAnalytics.send(new ConnectEvent());

        // Miscellaneous Events
        ResynthAnalytics.send(new SeedPodEvent(
                ResynthConfig.GENERAL_CONFIG.getCategory(SeedPodConfig.class).areDropsEnabled()
        ));

        SupportedMods.sendModIntegrationEvents();

        // New User Event
        if(!RESYNTH_USER_FILE.exists()){
            ResynthAnalytics.send(new NewUserEvent());
            try {
                boolean create = RESYNTH_USER_FILE.createNewFile();
                if(!create)
                    LOG.error("Failed to create Resynth NU file");
            } catch (IOException e) {
                LOG.error("Failed to create Resynth NU file", e);
            }
        }
    }

    /**
     * Sends the event to the analytics api.
     *
     * @param e the event to send.
     */
    public static void send(Event e){
        //Never send an event if analytics is disabled.
        if(!ENABLED || ANALYTICS == null){
            LOG.info(String.format("Analytics disabled! Event (%s) prevented from sending.", e.toString()));
            SSLHelper.enableSSL();
            return;
        }

        //Disable it again before sending the event.
        SSLHelper.disableSSL();
        LOG.info("Sending analytics event: " + e.toString());
        ANALYTICS.makeCustomRequest(e.getEventData());
        //Re-enable it once we're done
        SSLHelper.enableSSL();
    }

    /**
     * Sets the address (host name/page) of the event.
     *
     * @param eventData the event
     */
    static void setEventHost(AnalyticsRequestData eventData){
        eventData.setHostName(RESYNTH_DOMAIN);
        eventData.setPageURL(IDENTIFIER_PAGE);
        eventData.setPageTitle(TITLE);
    }
}
