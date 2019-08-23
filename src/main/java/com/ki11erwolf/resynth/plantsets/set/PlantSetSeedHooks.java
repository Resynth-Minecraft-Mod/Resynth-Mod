package com.ki11erwolf.resynth.plantsets.set;

import net.minecraftforge.common.MinecraftForge;

/**
 * Allows implementing plant sets (e.g. {@link CrystallineSet})
 * to easily register game hooks that allow spawning
 * plant seeds in the world from player actions.
 * <p/>
 * Provides implementing classes with an easy way
 * of registration and some utilities.
 */
//TODO: Add utility
class PlantSetSeedHooks {

    /**
     * {@code true} if this SeedHooks instance
     * has already been registered, {@code false}
     * otherwise.
     */
    private boolean registered = false;

    /**
     * Registers this SeedHooks instance if it
     * has not yet been registered.
     */
    void register(){
        if(registered)
            return;

        MinecraftForge.EVENT_BUS.register(this);
        registered = true;
    }
}
