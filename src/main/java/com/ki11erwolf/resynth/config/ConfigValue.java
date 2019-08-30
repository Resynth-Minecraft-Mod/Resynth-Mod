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

/**
 * Represents a configuration value object that
 * holds a single raw value (e.g. string or int).
 * Identified by a unique name, with an attached comment
 * and a default value.
 */
public interface ConfigValue {

    /**
     * @return the unique name (not enforced)
     * of this config value used to identify it.
     */
    String getUniqueName();

    /**
     * @return the string comment attached to this
     * config value. The comment should include the raw
     * type, default value and min/max values.
     */
    String getComment();

    /**
     * @return the default value given to this
     * config value when no other value could be
     * found.
     */
    Object getDefaultValue();

    /**
     * USED BY INTERNAL CLASSES! TREAT AS PRIVATE!
     *
     * <p/>
     * Sets the value of the object instance
     * from the config file. This will not change
     * the value.
     *
     * @param value the value gotten from the
     *              config file.
     */
    void setValue(Object value);

    /**
     * USED BY INTERNAL CLASSES! TREAT AS PRIVATE!
     *
     * @return the value stored in this config value
     * cast as an object.
     */
    Object get();
}
