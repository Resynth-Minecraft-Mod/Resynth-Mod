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
package com.ki11erwolf.resynth.config;

import java.io.File;

/**
 * Holds references to the various configuration
 * files used by Resynth. As well as handles
 * initialization of config.
 *
 * This class (including all config) is loaded when it's
 * first used (i.e. lazily initialized). So it should be
 * ready to use at any point in the application/mod life-cycle.
 */
public class ResynthConfig {

    /**
     * The folder (within the run directory) where the configuration
     * files are kept.
     */
    private static final String CONFIG_FOLDER = "config/resynth/";

    /*
        Ensures all parent directories exist before
        continuing.
     */
    static {
        //Ensure parent folders exist.
        File configFolder = new File(CONFIG_FOLDER);
        if(!configFolder.exists())
            //noinspection ResultOfMethodCallIgnored
            configFolder.mkdirs();
    }

    // *******
    // Configs
    // *******

    /**
     * General config file used to store settings not specific to plants.
     */
    public static final ConfigFile GENERAL_CONFIG = new ConfigFile(newConfig("general"));

    /**
     * Config file used to store Vanilla plant set settings.
     */
    public static final ConfigFile VANILLA_PLANTS_CONFIG = new ConfigFile(newConfig("vanilla-plants"));

    /**
     * Config file used to store modded plant set settings.
     */
    public static final ConfigFile MODDED_PLANTS_CONFIG = new ConfigFile(newConfig("modded-plants"));

    /**
     * Private (non-instantiatable) constructor.
     */
    private ResynthConfig(){}

    /**
     * Turns a config name into an actual file name including
     * the path.
     *
     * @param name config name.
     * @return the config file name.
     */
    @SuppressWarnings("SameParameterValue")
    private static String newConfig(String name){
        return CONFIG_FOLDER + name + ".toml";
    }
}
