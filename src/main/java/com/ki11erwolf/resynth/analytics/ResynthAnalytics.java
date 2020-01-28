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
package com.ki11erwolf.resynth.analytics;

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.ResynthSSL;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.GeneralConfig;
import dmurph.tracking.AnalyticsConfigData;
import dmurph.tracking.JGoogleAnalyticsTracker;
import org.apache.logging.log4j.Logger;

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
        ResynthSSL.disableSSL();
    }

    /**
     * The Google analytics API.
     */
    private static final JGoogleAnalyticsTracker ANALYTICS = new JGoogleAnalyticsTracker(
            new AnalyticsConfigData(CODE),
            JGoogleAnalyticsTracker.GoogleAnalyticsVersion.V_4_7_2
    );

    /**
     * Static instance object.
     */

    //Static class.
    private ResynthAnalytics(){}

    /**
     * Sends the event to the analytics api.
     *
     * @param e the event to send.
     */
    public static void send(Event e){
        //Never send an event if analytics is disabled.
        if(!ENABLED){
            LOG.info("Analytics disabled! Event prevented from sending.");
            return;
        }


        //Disable it again before sending the event.
        ResynthSSL.disableSSL();
        LOG.info("Analytics enabled! Sending analytics event: " + e.getClass().getName());
        ANALYTICS.makeCustomRequest(e);
        //Re-enable it once we're done
        ResynthSSL.enableSSL();
    }

    /**
     * Sets the address (host name/page) of the event.
     *
     * @param event the event
     */
    static void setEventHost(Event event){
        event.setHostName(RESYNTH_DOMAIN);
        event.setPageURL(IDENTIFIER_PAGE);
        event.setPageTitle(TITLE);
    }
}
