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
    private final IntegerConfigValue count;

    /**
     * Config value that allows setting the min gen height of the ore
     * veins.
     */
    private final IntegerConfigValue bottomOffset;

    /**
     * Config value that allows setting the max gen height of the ore
     * veins.
     */
    private final IntegerConfigValue maxHeight;

    /**
     * Config value that allows setting the max base height of the ore
     * veins.
     */
    private final IntegerConfigValue topOffset;

    /**
     * Creates a new, unique configuration category
     * grouping with settings for ore generation.
     *
     * @param configName the name of the config grouping.
     *                   Has {@code .ore-generation} appended.
     */
    GenConfig(String configName, boolean generate, int veinSize, int veinCount, int bottomOffset, int maxHeight,
              int topOffset) {
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
                veinSize,
                1, 100,
                this
        );

        this.count = new IntegerConfigValue(
                "vein-count",
                "The average count/frequency of ore veins in a chunk." +
                        "\nSet this number higher to increase the number of ore veins.",
                veinCount,
                1, 100,
                this
        );

        this.bottomOffset = new IntegerConfigValue(
                "bottom-offset",
                "The minimum height (Y-level) that the ore blocks will generate.",
                bottomOffset,
                0, 255,
                this
        );

        this.maxHeight = new IntegerConfigValue(
                "maximum-height",
                "The maximum height (Y-level) that the ore blocks will generate",
                maxHeight,
                1, 255,
                this
        );

        this.topOffset = new IntegerConfigValue(
                "top-offset",
                "The amount of blocks the ore block generation will be offset by on the Y-Axis.",
                topOffset,
                0, 255,
                this
        );
    }

    /**
     * @return {@code true} if the config
     * allows generating the ore veins.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
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
    public int getCount(){
        return this.count.getValue();
    }

    /**
     * @return the min height of the ore veins
     * as specified by the config.
     */
    public int getBottomOffset(){
        return this.bottomOffset.getValue();
    }

    /**
     * @return the max height of the ore veins
     * as specified by the config.
     */
    public int getMaxHeight(){
        return this.maxHeight.getValue();
    }

    /**
     * @return the max base height of the ore veins
     * as specified by the config.
     */
    public int getTopOffset(){
        return this.topOffset.getValue();
    }
}
