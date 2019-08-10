package com.ki11erwolf.resynth.proxy;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

/**
 * Resynth mod proxy interface.
 */
public interface Proxy {

    /**
     * First mod registration event. Called to
     * initialize and register the Resynth mod.
     *
     * @param event forge provided event.
     */
    void setup(final FMLCommonSetupEvent event);

    /**
     * Client side mod registration event.
     *
     * @param event forge provided event.
     */
    default void doClientStuff(final FMLClientSetupEvent event){}

    /**
     * InterModCommunication message send event.
     *
     * @param event forge provided event.
     */
    default void enqueueIMC(final InterModEnqueueEvent event){}

    /**
     * InterModCommunication process event.
     *
     * @param event forge provided event.
     */
    default void processIMC(final InterModProcessEvent event){}

    /**
     * Server side registration event.
     *
     * @param event forge provided event.
     */
    default void onServerStarting(FMLServerStartingEvent event){}

    /**
     * Called when the game is stopping.
     *
     * @param event Forge event.
     */
    default void onServerStopped(FMLServerStoppedEvent event){}
}
