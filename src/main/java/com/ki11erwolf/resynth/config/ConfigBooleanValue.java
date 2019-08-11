package com.ki11erwolf.resynth.config;

import java.util.Objects;

/**
 * Represents a boolean configuration value within a {@link
 * ConfigCategory} with an identifiable name, comment and
 * default value.
 */
public class ConfigBooleanValue implements ConfigValue {

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
    public ConfigBooleanValue(String uniqueName, String comment, boolean defaultValue, ConfigCategory category){
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
