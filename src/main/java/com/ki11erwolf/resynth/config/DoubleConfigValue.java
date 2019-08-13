package com.ki11erwolf.resynth.config;

import org.apache.logging.log4j.util.StackLocatorUtil;

import java.util.Objects;

/**
 * Represents a double configuration value within a {@link
 * ConfigCategory} with an identifiable name, comment,
 * default value, minimum and maximum value.
 */
public class DoubleConfigValue implements ConfigValue {

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
    private final double defaultValue;

    /**
     * The minimum value the true value can be.
     */
    private final double min;

    /**
     * The maximum value the true value can be.
     */
    private final double max;

    /**
     * The actual value stored (within the range of min & max).
     */
    private double value;

    /**
     * Creates a new double config value with the given
     * unique name, comment, default value, minimum and maximum values.
     *
     * @param uniqueName the unique name of the value (not enforced).
     * @param comment the comment attached to the config value.
     * @param defaultValue the default value of the config value.
     * @param min the minimum value the actual value can be.
     * @param max the maximum value the actual value can be.
     * @param category the config category this value belongs to.
     */
    public DoubleConfigValue(String uniqueName, String comment, double defaultValue, double min, double max,
                             ConfigCategory category){
        this.uniqueName = Objects.requireNonNull(uniqueName).replace(' ', '-');
        this.comment = Objects.requireNonNull(comment);
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;

        category.registerConfigValue(this);
    }

    /**
     * Creates a new double config value with the given
     * unique name, comment and default value.
     *
     * @param uniqueName the unique name of the value (not enforced).
     * @param comment the comment attached to the config value.
     * @param defaultValue the default value of the config value.
     * @param category the config category this value belongs to.
     */
    @SuppressWarnings("unused")
    public DoubleConfigValue(String uniqueName, String comment, double defaultValue, ConfigCategory category){
        this(uniqueName, comment, defaultValue, 0, 0, category);
    }

    /**
     * @return the double value stored in this
     * config value.
     */
    public double getValue(){
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
        return comment + ((min == 0 && max == 0)
                ? "\n(type=double, min=" + Double.MIN_VALUE + ", max="
                    + Double.MAX_VALUE + ", default=" + defaultValue + ")"
                : "\n(type=double, min=" + min + ", max=" + max + ", default=" + defaultValue + ")");
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
        try{
            this.value = Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException e){
            this.value = defaultValue;
        }

        //No min/max.
        if(this.max == 0 && this.min == 0)
            return;

        if(this.value > max)
            this.value = max;

        if(this.value < min)
            this.value = min;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get() {
        return getValue();
    }
}
