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
 * Seed pod event sent at startup that tells if the Seed Pod
 * is enabled or not.
 */
public class SeedPodEvent extends Event {

    /**
     * If the SeedPod is enabled or not.
     */
    private final boolean enabled;

    /**
     * @param isEnabled {@code true} if the SeedPod is enabled.
     */
    public SeedPodEvent(boolean isEnabled){
        this.enabled = isEnabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getName() {
        return "Seed-Pod";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getProperties(){
        Map<String, String> props = super.getProperties();
        props.put("enabled", String.valueOf(enabled));
        return props;
    }

}
