package com.ki11erwolf.resynth.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

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
     * Creates a new, unique configuration category
     * grouping.
     *
     * @param uniqueName The unique name (not enforced) of this
     * category/group.
     */
    public ConfigCategory(String uniqueName){
        this.uniqueName = Objects.requireNonNull(uniqueName);
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
        for(ConfigValue configValue : getValues()){
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
     * USED BY IMPLEMENTING CLASSES.
     *
     * <p/>
     *
     * Should return an array of all the ConfigValues
     * used by the implementing class.
     *
     * @return an array of every used ConfigValue by this
     * config category.
     */
    protected abstract ConfigValue[] getValues();

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
