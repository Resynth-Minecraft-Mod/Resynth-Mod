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
 * Config settings for the {@link com.ki11erwolf.resynth.block.BlockSeedPod}.
 */
public class SeedPodConfig extends ConfigCategory {

    // ************************
    // Config Value Definitions
    // ************************

    /**
     * Config value definition that allows enabling/disabling
     * drops from the Mystical Seed Pod.
     */
    private final BooleanConfigValue enabled = new BooleanConfigValue(
            "enable-drops-from-seed-pod",
            "Set to true to make the Mystical Seed Pod drop a random Biochemical (mob drop)" +
                      "\nseed when broken. This can be useful for players playing on peaceful mode who can" +
                      "\notherwise not get mob drops",
            false,
            this
    );

    /**
     * Config value definition that allows enabling/disabling Mystical Seed Pod
     * world generation.
     */
    private final BooleanConfigValue generate = new BooleanConfigValue(
            "generate-in-world",
            "Allows enabling or disabling the generating of Mystical Seed Pods in the world." +
                      "\nSet this to false to prevent Mystical Seed Pods from generating in the world.",
            true,
            this
    );

    /**
     * Config value definition that allows setting the spawn frequency
     * of Mystical Seed Pods.
     */
    private final IntegerConfigValue rarity = new IntegerConfigValue(
            "flower-patch-rarity",
            "The frequency (or the number of times) that patches of Mystical Seed Pods will generate in a chunk." +
                      "\nSetting this to a higher value will generate more patches of Mystical Seed Pods.",
            5,
            1, 64,
            this
    );

    private final IntegerConfigValue size = new IntegerConfigValue(
            "flower-patch-size",
            "The average amount of Mystical Seed Pods that will generate in a flower patch.\n" +
                    "Setting this to a higher value will generate more Mystical Seed Pods per patch.",
            4, 64,
            1, this
    );

    /**
     * Creates a new, unique configuration category
     * grouping.
     */
    public SeedPodConfig() {
        super("mystical-seed-pod");
    }

    // ****************
    // Public Accessors
    // ****************

    /**
     * @return {@code true} if the config has enabled
     * drops from the SeedPod.
     */
    public boolean areDropsEnabled(){
        return enabled.getValue();
    }

    /**
     * @return {@code true} if the config allows
     * generating Mystical Seed Pods in the world.
     */
    public boolean generate(){
        return this.generate.getValue();
    }

    /**
     * @return the frequency of Mystical Seed
     * Pod generation as specified by the config.
     */
    public int getRarity(){
        return this.rarity.getValue();
    }

    public int getSize() {
        return this.size.getValue();
    }
}
