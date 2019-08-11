package com.ki11erwolf.resynth.config.categories;

import com.ki11erwolf.resynth.config.*;

/**
 * General configuration settings used by Resynth.
 */
public class General extends ConfigCategory {

    // *************
    // Config Values
    // *************

    /**
     * Analytics enable/disable switch.
     */
    private final ConfigBooleanValue enableAnalytics = new ConfigBooleanValue(
            "enable analytics",
            "Enables or disables all the analytical features of the mod.\n" +
                      "Including usage and error reports (all sent reports are anonymous).\n" +
                      "False to disable, true to enable.",
            true,
            this
    );

    /**
     * Debug/Development features enable/disable flag.
     */
    private final ConfigBooleanValue enableDebugHelp = new ConfigBooleanValue(
            "enable debug help",
            "Enables features related to debugging and development.\n" +
                      "It's unlikely you'd want to enable this.",
            false,
            this
    );

    /**
     * Constructor.
     */
    public General() {
        super("general-resynth-config");
    }

    // ****************
    // Public Accessors
    // ****************

    /**
     * @return {@code true} if and only if the config
     * allows enabling analytics.
     */
    public boolean isAnalyticsEnabled(){
        return enableAnalytics.getValue();
    }

    /**
     * @return {@code true} if dev features
     * are enabled.
     */
    public boolean isDevHelpEnabled(){
        return enableDebugHelp.getValue();
    }
}
