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

import java.util.Map;

/**
 * Event fired when a plant set is flagged as a failure.
 * Provides info on the plant set that failed.
 */
public class PlantSetFailureEvent extends Event {

    /**
     * The name of the plant set that failed -
     * include the modid.
     */
    private final String plantSet;

    /**
     * @param plantSet The name of the plant set that failed.
     */
    public PlantSetFailureEvent(String plantSet){
        this.plantSet = plantSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Plant-Set-Failure";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getProperties(){
        Map<String, String> props = super.getProperties();
        props.put("plant_set", plantSet);
        return props;
    }
}
