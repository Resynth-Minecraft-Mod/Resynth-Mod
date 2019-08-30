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
import com.ki11erwolf.resynth.config.IntegerConfigValue;

/**
 * Configuration settings for Mineral Stone world gen.
 */
public class MineralStoneGenConfig extends ConfigCategory {

    /**
     * Config value that allows enabling/disabling Mineral Stone
     * world generation.
     */
    private final BooleanConfigValue generate = new BooleanConfigValue(
            "generate",
            "Allows enabling or disabling generating Mineral Stone in the world." +
                      "\nSet this to false to prevent Mineral Stone from spawning in the world",
            true,
            this
    );

    /**
     * Config value that allows setting the size of Mineral Stone
     * veins.
     */
    private final IntegerConfigValue size = new IntegerConfigValue(
            "vein-size",
            "The average size of Mineral Stone veins. Set this number" +
                      "\nhigher to increase the amount of Mineral Stone blocks" +
                      "\nin an ore vein.",
            10,
            1, 100,
            this
    );

    /**
     * Config value that allows setting the frequency of Mineral Stone
     * veins.
     */
    private final IntegerConfigValue count = new IntegerConfigValue(
            "vein-count",
            "The average count/frequency of Mineral Stone veins in a chunk." +
                      "\nSet this number higher to increase the number of Mineral Stone ore veins.",
            8,
            1, 100,
            this
    );

    /**
     * Config value that allows setting the min gen height of Mineral Stone
     * veins.
     */
    private final IntegerConfigValue minHeight = new IntegerConfigValue(
            "minimum-height",
            "The minimum height (Y-level) that Mineral Stone blocks will generate.",
            3,
            1, 255,
            this
    );

    /**
     * Config value that allows setting the max gen height of Mineral Stone
     * veins.
     */
    private final IntegerConfigValue maxHeight = new IntegerConfigValue(
            "maximum-height",
            "The maximum height (Y-level) that Mineral Stone blocks will generate",
            10,
            1, 255,
            this
    );

    /**
     * Config value that allows setting the max base height of Mineral Stone
     * veins.
     */
    private final IntegerConfigValue maxBaseHeight = new IntegerConfigValue(
            "maximum-base-height",
            "The maximum height (Y-level) that Mineral Stone ore veins will generate",
            7,
            1, 255,
            this
    );

    /**
     * Creates a new, unique configuration category
     * grouping.
     */
    public MineralStoneGenConfig() {
        super("mineral-stone.ore-generation");
    }

    /**
     * @return {@code true} if the config
     * allows generation Mineral Stone veins.
     */
    public boolean shouldGenerate(){
        return this.generate.getValue();
    }

    /**
     * @return the size of Mineral Stone veins
     * as specified by the config.
     */
    public int getSize(){
        return this.size.getValue();
    }

    /**
     * @return the frequency of Mineral Stone veins
     * as specified by the config.
     */
    public int getCount(){
        return this.count.getValue();
    }

    /**
     * @return the min height of Mineral Stone veins
     * as specified by the config.
     */
    public int getMinHeight(){
        return this.minHeight.getValue();
    }

    /**
     * @return the max height of Mineral Stone veins
     * as specified by the config.
     */
    public int getMaxHeight(){
        return this.maxHeight.getValue();
    }

    /**
     * @return the max base height of Mineral Stone veins
     * as specified by the config.
     */
    public int getMaxBaseHeight(){
        return this.maxBaseHeight.getValue();
    }
}
