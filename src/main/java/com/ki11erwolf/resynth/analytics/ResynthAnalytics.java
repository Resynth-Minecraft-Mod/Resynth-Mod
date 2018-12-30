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
package com.ki11erwolf.resynth.analytics;

import com.ki11erwolf.resynth.ResynthConfig;
import com.ki11erwolf.resynth.versioning.ModVersionManager;
import dmurph.tracking.AnalyticsConfigData;
import dmurph.tracking.JGoogleAnalyticsTracker;

/**
 * Provides an API for more easily sending
 * Google Analytics events.
 */
public final class ResynthAnalytics {

    /*
     * Disables SSL Verification before initializing
     * Google Analytics.
     */
    static{
        ModVersionManager.disableSSLVerification();
    }

    /**
     * The Google Analytics code for resynth.
     */
    private static final String CODE = "UA-127648959-1";

    /**
     * The domain name for the Resynth website
     */
    private static final String RESYNTH_DOMAIN = "https://resynth-minecraft-mod.github.io";

    /**
     * Dummy page for identifying the mod client.
     */
    private static final String IDENTIFIER_PAGE = "/jar";

    /**
     * The dummy page title.
     */
    private static final String TITLE = "Resynth - Jar";

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
        if(ResynthConfig.RESYNTH.disableGAnalytics)
            return;

        ModVersionManager.disableSSLVerification();
        ANALYTICS.makeCustomRequest(e);
        ModVersionManager.enableSSLVerification();
    }

    /**
     * Sets the address (host name/page) of the event.
     *
     * @param event the event
     */
    protected static void setEventHost(Event event){
        event.setHostName(RESYNTH_DOMAIN);
        event.setPageURL(IDENTIFIER_PAGE);
        event.setPageTitle(TITLE);
    }
}
