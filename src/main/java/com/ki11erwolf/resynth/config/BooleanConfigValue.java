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
package com.ki11erwolf.resynth.config;

import java.util.Objects;

/**
 * Represents a boolean configuration value within a {@link
 * ConfigCategory} with an identifiable name, comment and
 * default value.
 */
public class BooleanConfigValue implements ConfigValue {

    /**
     * The unique name of the value (not enforced).
     */
    private final String uniqueName;

    /**
     * The comment for this value.
     */
    private final String comment;

    /**
     * The default value for this configuration value.
     */
    private final boolean defaultValue;

    /**
     * The current value (set by config file) of this
     * configuration value.
     */
    private boolean value;

    /**
     * Creates a new boolean config value with the given
     * unique name, comment and default value.
     *
     * @param uniqueName the unique name of the value (not enforced).
     * @param comment the comment attached to the config value.
     * @param defaultValue the default value of the config value.
     * @param category the config category this value belongs to.
     */
    public BooleanConfigValue(String uniqueName, String comment, boolean defaultValue, ConfigCategory category){
        this.uniqueName = Objects.requireNonNull(uniqueName).replace(' ', '-');
        this.comment = Objects.requireNonNull(comment);
        this.defaultValue = defaultValue;

        category.registerConfigValue(this);
    }

    /**
     * @return the boolean value stored in this
     * config value.
     */
    public boolean getValue(){
        return this.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUniqueName() {
        return uniqueName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getComment() {
        return comment + "\n(type=boolean, default=" + defaultValue +")";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(Object value) {
        this.value = Boolean.parseBoolean(String.valueOf(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get() {
        return getValue();
    }
}
