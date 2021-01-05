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
package com.ki11erwolf.resynth.config.categories;

import com.ki11erwolf.resynth.config.BooleanConfigValue;
import com.ki11erwolf.resynth.config.ConfigCategory;

/**
 * Configuration settings relating to the use of the
 * Mineral Crystal item.
 */
public class MineralCrystalConfig extends ConfigCategory {

    /**
     * Toggle that allows enabling or disabling the Emergency Conversion
     * Mode on Mineral Crystal items.
     */
    private final BooleanConfigValue enableECM = new BooleanConfigValue(
            "enable-emergency-conversion-mode",
            "Enable this (set to true) to allow turning dirt or grass into\n" +
                      "Mineral Soil directly by using the item on the dirt or grass block\n" +
                      "without having to use the Mineral Hoe. Use this mode when the Mineral\n" +
                      "Hoe is broken or cannot be used.",
            false,
            this
    );

    /**
     * Creates a new, unique configuration category
     * grouping.
     */
    public MineralCrystalConfig() {
        super("mineral-crystal");
    }

    /**
     * @return {@code true} if the player has
     * enabled emergency conversion mode
     * through the config files.
     */
    public boolean isECMEnabled(){
        return enableECM.getValue();
    }
}
