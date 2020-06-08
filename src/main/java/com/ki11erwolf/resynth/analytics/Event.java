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
import dmurph.tracking.AnalyticsRequestData;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for analytic events.
 */
public abstract class Event {

    /**
     * This Resynth builds version.
     */
    private static final String RESYNTH_VERSION = ResynthMod.MOD_VERSION;

    /**
     * The Minecraft version for this build.
     */
    private static final String MC_VERSION =
            ResynthMod.MC_VERSION.replace("[", "").replace("]", "");

    /**
     * The analytics data object that will be used to send
     * this event and its data.
     */
    private final AnalyticsRequestData backingEventData;

    /**
     * Constructs a new event.
     */
    Event(){
        backingEventData = new AnalyticsRequestData();
        ResynthAnalytics.setEventHost(backingEventData);
    }

    /**
     * Will return the unique identifying name of this event.
     * Should be overriden by inheriting classes to provide the name.
     */
    protected abstract String getName();

    /**
     * Will return a map of event data mapped to the datas name.
     * Inheriting classes should override this to provide additional
     * information.
     */
    protected Map<String, String> getProperties(){
        return new HashMap<>();
    }

    /**
     * The events primary title. Holds the events name
     * and any data supplied using ({@link #getProperties()}).
     *
     * @return the event name and any additional data.
     */
    protected String getAction(){
        return getName() + propertiesToString(getProperties());
    }

    /**
     * The events secondary title. Holds the events name,
     * supplied data and the version number of Resynth
     * and Minecraft.
     *
     * @return the event name, data/properties, and
     * version numbers.
     */
    protected String getCategory(){
        Map<String, String> properties = getProperties();
        properties.put("version", RESYNTH_VERSION);
        properties.put("minecraft_version", MC_VERSION);
        return getName() + propertiesToString(properties);
    }

    /**
     * The events tertiary title. Holds only the
     * version number of Resynth and Minecraft.
     *
     * @return the version number of Resynth and Minecraft.
     */
    protected String getLabel(){
        return String.format(
                "Event[resynth=%s, minecraft=%s]",
                RESYNTH_VERSION, MC_VERSION
        );
    }

    /**
     * Turns a map of names to data as human readable String.
     *
     * @param properties a map names to data.
     * @return the human readable string representation of
     * the {@code properties} map.
     */
    protected static String propertiesToString(Map<String, String> properties){
        if(properties.size() == 0)
            return "";

        StringBuilder props = new StringBuilder("[");
        for(Map.Entry<String, String> propEntry : properties.entrySet()){
            props.append(propEntry.getKey()).append("=").append(propEntry.getValue()).append(",");
        }

        return props.substring(0, props.length() - 1) + "]";
    }

    /**
     * @return a new {@link AnalyticsRequestData} object with the
     * events data bundled within, that can be used to send
     * the actual event.
     */
    protected AnalyticsRequestData getEventData(){
        backingEventData.setEventAction(getAction());
        backingEventData.setEventCategory(getCategory());
        backingEventData.setEventLabel(getLabel());
        return backingEventData;
    }

    /**
     * {@inheritDoc}
     *
     * @return a valid String representation of this object
     * containing all unique event data.
     */
    @Override
    public String toString() {
        return String.format("Resynth.Event[name=%s, properties=%s, action=%s, category=%s, label=%s]",
                getName(), propertiesToString(getProperties()), getAction(), getCategory(), getLabel()
        );
    }
}
