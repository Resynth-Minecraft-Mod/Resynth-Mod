/*
 * Copyright 2018 Ki11er_wolf
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

package com.ki11erwolf.resynth;

import com.ki11erwolf.resynth.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

/**
 * Resynth mod class.
 */
@Mod(
        modid = ResynthMod.MOD_ID,
        name = ResynthMod.MOD_NAME,
        version = ResynthMod.MOD_VERSION,
        acceptedMinecraftVersions = ResynthMod.MC_VERSION
)
public class ResynthMod {

    /**
     * Resynth mod ID.
     */
    public static final String MOD_ID = "resynth";

    /**
     * Resynth mod name.
     */
    public static final String MOD_NAME = "Resynth";

    /**
     * Resynth version.
     */
    public static final String MOD_VERSION = "1.0.1-Beta";

    /**
     * Minecraft version.
     */
    public static final String MC_VERSION = "[1.12.2]";

    /**
     * The package declaration for the mod.
     */
    public static final String MOD_PACKAGE = "com.ki11erwolf.resynth";

    /**
     * FML initialized proxy. Can be server side proxy, client side proxy or both.
     */
    @SidedProxy(clientSide = ResynthMod.CLIENT_PROXY, serverSide = ResynthMod.SERVER_PROXY)
    public static IProxy proxy;

    /**
     * Path (including pack) to the server proxy class.
     */
    static final String SERVER_PROXY = MOD_PACKAGE + ".proxy.ServerProxy";

    /**
     * Path (including package) to the client proxy class.
     */
    static final String CLIENT_PROXY = MOD_PACKAGE + ".proxy.ClientProxy";

    /**
     * Logger for the mod.
     */
    private static Logger logger;

    /**
     * Stage 1 of 3 in the initialization event.
     *
     * @param event pre init event.
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        logger.info("Entering pre-init phase...");
        proxy.preInit(event);
    }

    /**
     * Stage 2 of 3 in the initialization event.
     *
     * @param event init event.
     */
    @EventHandler
    public void init(FMLInitializationEvent event){
        logger.info("Entering init phase...");
        proxy.init(event);
        GameRegistry.registerWorldGenerator(ResynthWorldGen.INSTANCE, 0);
        ResynthFurnaceRecipes.registerFurnaceRecipes();
    }

    /**
     * Stage 3 of 3 in the initialization event.
     *
     * @param event post init event.
     */
    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        logger.info("Entering post-init phase...");
        proxy.postInit(event);
    }

    /**
     * @return the logger for the mod.
     */
    public static Logger getLogger(){
        return logger;
    }
}
