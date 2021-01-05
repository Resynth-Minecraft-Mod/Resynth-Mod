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
 * An event fired whenever a supported mod, that Resynth adds plants for,
 * is found in the Forge mod list (installed mods). An event instance is
 * created & fired for ever mod found. Each event represents one of the
 * mods as a unique plain-text name.
 */
public class ModIntegrationEvent extends Event {

    /**
     * A unique plain-text name generated to identify a specific
     * mod. Generated using both the mod name & ID.
     */
    private final String identifingName;

    /**
     * Creates a new ModIntegrationEvent representing a given
     * mod that can then be sent.
     *
     * @param modID the MODID of the mod.
     * @param modName the display name of the mod.
     */
    public ModIntegrationEvent(String modID, String modName){
        this.identifingName = String.format("%s[%s]", modName, modID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getName() {
        return "Mod-Integration";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getProperties(){
        Map<String, String> props = super.getProperties();
        props.put("mod", String.valueOf(identifingName));
        return props;
    }

}
