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
            //TODO: Handle this error better.
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
     * Reads a config category from file by its class.
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
}
