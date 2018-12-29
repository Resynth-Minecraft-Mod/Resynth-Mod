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

import com.ki11erwolf.resynth.ResynthMod;
import dmurph.tracking.AnalyticsRequestData;

/**
 * Base class for analytic events.
 */
public abstract class Event extends AnalyticsRequestData {

    /**
     * Resynth version number.
     */
    private static final String VERSION = "[version=" + ResynthMod.MOD_VERSION + "]";

    /**
     * Constructs a new event.
     */
    public Event(){
        ResynthAnalytics.setEventHost(this);
        this.setEventAction(getAction() + VERSION);
        this.setEventCategory(getCategory());
        this.setEventLabel(getLabel());
    }

    /**
     * @return the event name/ID and version number.
     */
    public abstract String getAction();

    /**
     * @return copy of event name/ID.
     * Alternatively, extra information.
     */
    public String getCategory(){
        return getAction() + VERSION;
    }

    /**
     * @return Minecraft version.
     */
    public String getLabel(){
        return "Minecraft - " + ResynthMod.MC_VERSION;
    }
}
