/*
 * Copyright 2018-2019 Ki11er_wolf
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
import com.ki11erwolf.resynth.config.DoubleConfigValue;

/**
 * Configuration settings for the {@link com.ki11erwolf.resynth.block.BlockMineralSoil}.
 */
public class MineralSoilConfig extends ConfigCategory {

    /**
     * Config value definition for the enable chat message flag.
     */
    private final BooleanConfigValue enableChatMessage = new BooleanConfigValue(
            "enable-chat-message",
            "When enabled, a chat message containing the Mineral Content of" +
                      "\nthe Mineral Soil block will be displayed, when adding Mineral Rocks" +
                      "\nto the block",
            false,
            this
    );

    /**
     * Config value definition for the initial mineral content of mineral soil blocks.
     */
    private final DoubleConfigValue startingMineralContent = new DoubleConfigValue(
            "initial-mineral-content",
            "The initial/starting Mineral Content of every Mineral Soil block when it's" +
                      "\nfirst placed/created.",
            1.0D,
            0.1D, 50.0D,
            this
    );

    /**
     * Config value definition for the amount of Mineral Content a Mineral Rock
     * is worth.
     */
    private final DoubleConfigValue mineralRockWorth = new DoubleConfigValue(
            "mineral-rock-worth",
            "The Mineral Content a single Mineral Rock is worth. WARNING: Setting this value to one that" +
                      "\ndoes not eventually add up to 50 (taking into account the starting mineral content), will" +
                      "\ncause Mineral Rocks to be lost when breaking Mineral Soil blocks with a high/full Mineral" +
                      "\nContent.",
            1.0D,
            0.1D, 50D,
            this
    );

    /**
     * Creates a new, unique configuration category
     * grouping.
     */
    public MineralSoilConfig() {
        super("mineral-soil");
    }

    /**
     * @return {@code true} when the config allows
     * sending a chat message containing the Mineral
     * Content of a Mineral Soil block when a Mineral
     * Rock is added to it.
     */
    public boolean isChatMessageEnabled(){
        return enableChatMessage.getValue();
    }

    /**
     * @return the initial Mineral Content of Mineral
     * Soil blocks as specified by the config.
     */
    public double getStartingMineralContent(){
        return startingMineralContent.getValue();
    }

    /**
     * @return the amount of Mineral Content a
     * single Mineral Rock is worth.
     */
    public double getMineralRockWorth(){
        return mineralRockWorth.getValue();
    }
}
