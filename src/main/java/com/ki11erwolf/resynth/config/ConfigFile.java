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

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.ki11erwolf.resynth.ResynthMod;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a TOML config file built out of config categories (or groups)
 * and config values.
 */
public class ConfigFile {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    /**
     * List of already parsed and loaded config categories.
     */
    private final Map<Class, ? super ConfigCategory> loadedCategories = new HashMap<>();

    /**
     * The toml config file.
     */
    private final CommentedFileConfig config;

    /**
     * Constructs a new configuration file instance.
     *
     * @param file the file on disk.
     */
    ConfigFile(String file){
        LOG.info("Loading config file: " + file);
        this.config = CommentedFileConfig.builder(Objects.requireNonNull(file)).autosave().build();

        try{
            config.load();
            LOG.info("File loaded without errors.");
        } catch (Exception e){//We want a broad catch.
            LOG.fatal("Config load failure", e);
            throw e;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Saving config file: " + file + "...");

            config.save();
            config.close();

            LOG.info("Config file saved!");
        }));
    }

    /**
     * Reads a config category from file by its class. Used to
     * obtain references to config categories with only one instance
     * (e.g. GeneralConfig). Only one config category can be registered by class.
     *
     * @param catClass the config category class.
     * @param <T> the config category class type.
     * @return the constructed and loaded config category.
     */
    @SuppressWarnings("unused")
    public <T extends ConfigCategory> T getCategory(Class<T> catClass){
        //Already loaded.
        if(loadedCategories.containsKey(catClass))
            //noinspection unchecked //Should not happen.
            return (T)loadedCategories.get(catClass);

        //Read from file.
        T category;

        try {
            category = catClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOG.error("Failed to instantiate config category class", e);
            throw new IllegalArgumentException("Category class not instantiatable");
        }

        category.initValues(config);

        //Cache
        loadedCategories.put(catClass, category);
        return category;
    }

    /**
     * Reads a config category from file and attempts to store
     * the values in the category provided. This method
     * allows multiple config categories per class, unlike
     * {@link #getCategory(Class)}, however, it requires
     * an instance to be provided.
     *
     * @param category the provided category that will
     *                 represent the config on file.
     * @param <T> category class.
     * @return the provided category with the config
     * values loaded from file.
     */
    public <T extends ConfigCategory> T loadCategory(T category){
        Objects.requireNonNull(category).initValues(config);
        return category;
    }
}
