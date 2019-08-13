package com.ki11erwolf.resynth.config;

import java.util.Objects;

/**
 * Represents a string configuration value within a {@link
 * ConfigCategory} with an identifiable name, comment and
 * default value.
 */
public class StringConfigValue implements ConfigValue {

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
    private final String defaultValue;

    /**
     * The current value (set by config file) of this
     * configuration value.
     */
    private String value;

    /**
     * Creates a new string config value with the given
     * unique name, comment and default value.
     *
     * @param uniqueName the unique name of the value (not enforced).
     * @param comment the comment attached to the config value.
     * @param defaultValue the default value of the config value.
     * @param category the config category this value belongs to.
     */
    public StringConfigValue(String uniqueName, String comment, String defaultValue, ConfigCategory category){
        this.uniqueName = Objects.requireNonNull(uniqueName).replace(' ', '-');
        this.comment = Objects.requireNonNull(comment);
        this.defaultValue = Objects.requireNonNull(defaultValue);

        category.registerConfigValue(this);
    }

    /**
     * @return the string value stored in this
     * config value.
     */
    public String getValue(){
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
        return comment + "\n(type=string, default=" + defaultValue + ")";
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
        this.value = String.valueOf(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get() {
        return getValue();
    }
}
