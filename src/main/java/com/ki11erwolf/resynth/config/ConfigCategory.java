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
package com.ki11erwolf.resynth.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a category (or group) of config options
 * within a configuration file.
 */
public abstract class ConfigCategory {

    /**
     * The unique name (not enforced) of this
     * category/group.
     */
    private final String uniqueName;

    /**
     * List of config values registered to this category.
     */
    private final List<ConfigValue> values = new ArrayList<>();

    /**
     * Creates a new, unique configuration category
     * grouping.
     *
     * @param uniqueName The unique name (not enforced) of this
     * category/group. All spaces are replaced with hyphens (-).
     */
    public ConfigCategory(String uniqueName){
        this.uniqueName = Objects.requireNonNull(uniqueName.replace(' ', '-'));
    }

    /**
     * Initializes all {@link ConfigValue}s within this config
     * category from file (or uses the default).
     *
     * <p/>
     *
     * This method will always update the stored value if it's
     * incorrect as well as the comments.
     *
     * @param config the config file.
     */
    void initValues(CommentedFileConfig config){
        for(ConfigValue configValue : values){
            String key = getValueKey(uniqueName, configValue.getUniqueName());

            if(config.contains(key))
                configValue.setValue(config.get(key));
            else {
                configValue.setValue(configValue.getDefaultValue());
            }

            config.set(key, configValue.get());
            config.setComment(key, configValue.getComment());
        }
    }

    /**
     * Adds the config value to the category.
     *
     * @param value the config value.
     */
    void registerConfigValue(ConfigValue value){
        values.add(value);
    }

    /**
     * Used to turn a config category and a config value
     * into a single key.
     *
     * @param category the category name.
     * @param name the value name.
     * @return the single key used to obtain
     * the specific value within the category.
     */
    private String getValueKey(String category, String name){
        return category + "." + name;
    }
}
