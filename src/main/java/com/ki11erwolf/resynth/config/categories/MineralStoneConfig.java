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

import com.ki11erwolf.resynth.block.BlockMineralStone;
import com.ki11erwolf.resynth.config.ConfigCategory;
import com.ki11erwolf.resynth.config.DoubleConfigValue;
import com.ki11erwolf.resynth.config.IntegerConfigValue;

/**
 * Config settings for the {@link BlockMineralStone}
 * block.
 */
public class MineralStoneConfig extends ConfigCategory {

    // *************
    // Config Values
    // *************

    /**
     * The config value definition for the base (minimum) number of drops
     * Mineral Stone will give.
     */
    private final IntegerConfigValue baseDrops = new IntegerConfigValue(
            "number of base drops",
            "The minimum number of Mineral Rocks dropped by Mineral Stone when it's mined.",
            1,
            0, 64,
            this
    );

    /**
     * The config value definition for the amount of extra drops
     * Mineral Stone will give.
     */
    private final IntegerConfigValue extraDrops = new IntegerConfigValue(
            "number of extra drops",
            "The amount of extra Mineral Rocks Mineral Stone will give" +
                      "\nwhen mined (based on [chance-of-extra-drops]).",
            2,
            0, 64,
            this
    );

    /**
     * The config value definition for the percentage chance of Mineral
     * Stone dropping extra drops.
     */
    private final DoubleConfigValue extraDropsChance = new DoubleConfigValue(
            "chance of extra drops",
            "The chance of Mineral Stone giving extra Mineral Rocks when mined (0% to 100%).",
            20,
            0, 100,
            this
    );

    /**
     * The config value definition for the minimum number of experience orbs
     * Mineral Stone will drop when mined.
     */
    private final IntegerConfigValue minimumExperience = new IntegerConfigValue(
            "minimum experience dropped",
            "The minimum number of experience orbs Mineral Stone will drop when mined.",
            3,
            0, 100,
            this
    );

    /**
     * The config value definition for the maximum number of experience orbs
     * Mineral Stone will drop when mined
     */
    private final IntegerConfigValue maximumExperience = new IntegerConfigValue(
            "maximum experience dropped",
            "The maximum number of experience orbs Mineral Stone will drop when mined.",
            7,
            0, 100,
            this
    );

    /**
     * Creates a new, unique configuration category
     * grouping.
     */
    public MineralStoneConfig() {
        super("mineral-stone");
    }

    // ****************
    // Public Accessors
    // ****************

    /**
     * @return The base (minimum) number of drops Mineral Stone should
     * give as determined by config.
     */
    public int getBaseDrops(){
        return this.baseDrops.getValue();
    }

    /**
     * @return the percentage chance of Mineral Stone dropping
     * extra drops as determined by config.
     */
    public double getExtraDropsChance(){
        return this.extraDropsChance.getValue();
    }

    /**
     * @return the number of extra drops Mineral Stone will
     * drop as determined by config.
     */
    public int getExtraDrops(){
        return this.extraDrops.getValue();
    }

    /**
     * @return the minimum number of experience orbs Mineral
     * Stone will drop as determined by config.
     */
    public int getMinExpDropped(){
        return this.minimumExperience.getValue();
    }

    /**
     * @return the maximum number of experience orbs Mineral
     * Stone will drop as determined by config.
     */
    public int getMaxExpDropped(){
        return this.maximumExperience.getValue();
    }
}
