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
