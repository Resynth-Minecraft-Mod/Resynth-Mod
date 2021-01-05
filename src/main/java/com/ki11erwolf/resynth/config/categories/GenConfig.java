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
import com.ki11erwolf.resynth.config.IntegerConfigValue;

/**
 * Configuration settings for ore block generation.
 */
public class GenConfig extends ConfigCategory {

    /**
     * Config value that allows enabling/disabling the ore block world generation.
     */
    private final BooleanConfigValue generate;

    /**
     * Config value that allows setting the size of the ore veins.
     */
    private final IntegerConfigValue size;

    /**
     * Config value that allows setting the frequency of the ore
     * veins.
     */
    private final IntegerConfigValue rarity;

    /**
     * Config value that allows setting the min gen height of the ore
     * veins.
     */
    private final IntegerConfigValue minimumHeight;

    /**
     * Config value that allows setting the max gen height of the ore
     * veins.
     */
    private final IntegerConfigValue maximumHeight;

    /**
     * Creates a new, unique configuration category
     * grouping with settings for ore generation.
     *
     * @param configName the name of the config grouping.
     *                   Has {@code .ore-generation} appended.
     */
    GenConfig(String configName, boolean generate, int size, int rarity, int minimumHeight, int maximumHeight) {
        super(configName + ".ore-generation");

        this.generate = new BooleanConfigValue(
                "generate",
                "Allows enabling or disabling generating the ore block in the world." +
                        "\nSet this to false to prevent the ore block from spawning in the world",
                generate,
                this
        );

        this.size = new IntegerConfigValue(
                "vein-size",
                "The average size of the ore veins. Set this number" +
                        "\nhigher to increase the amount of ore blocks in an ore vein.",
                size,
                1, 64,
                this
        );

        this.rarity = new IntegerConfigValue(
                "vein-rarity",
                "The average number/frequency of ore veins in a chunk." +
                        "\nSet this number higher to increase the number of ore veins.",
                rarity,
                1, 64,
                this
        );

        this.minimumHeight = new IntegerConfigValue(
                "minimum-vein-height",
                "The minimum height (Y-level) that the ore blocks will generate.",
                minimumHeight,
                1, 254,
                this
        );

        this.maximumHeight = new IntegerConfigValue(
                "maximum-vein-height",
                "The maximum height (Y-level) that the ore blocks will generate",
                maximumHeight,
                1, 255,
                this
        );
    }

    /**
     * @return {@code true} if the config
     * allows generating the ore veins.
     */
    public boolean shouldGenerate(){
        return this.generate.getValue();
    }

    /**
     * @return the size of the ore veins
     * as specified by the config.
     */
    public int getSize(){
        return this.size.getValue();
    }

    /**
     * @return the frequency of the ore veins
     * as specified by the config.
     */
    public int getRarity(){
        return this.rarity.getValue();
    }

    /**
     * @return the min height of the ore veins
     * as specified by the config.
     */
    public int getMinimumHeight(){
        return this.minimumHeight.getValue();
    }

    /**
     * @return the max height of the ore veins
     * as specified by the config.
     */
    public int getMaximumHeight(){
        return this.maximumHeight.getValue();
    }

}
