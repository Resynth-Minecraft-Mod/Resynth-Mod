/*
 * Copyright 2018-2019 Ki11er_wolf
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

import com.ki11erwolf.resynth.analytics.ConnectEvent;
import com.ki11erwolf.resynth.analytics.ErrorEvent;
import com.ki11erwolf.resynth.analytics.NewUserEvent;
import com.ki11erwolf.resynth.analytics.ResynthAnalytics;
import com.ki11erwolf.resynth.proxy.ClientProxy;
import com.ki11erwolf.resynth.proxy.IProxy;
import com.ki11erwolf.resynth.versioning.ModVersionManager;
import com.ki11erwolf.resynth.versioning.VersionManagerBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistry;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

/**
 * Resynth mod class.
 */
@Mod(
        modid = ResynthMod.MOD_ID,
        name = ResynthMod.MOD_NAME,
        version = ResynthMod.MOD_VERSION,
        acceptedMinecraftVersions = ResynthMod.MC_VERSION,
        dependencies =
                        "after:appliedenergistics2; " +
                        "after:tconstruct; " +
                        "after:forestry; " +
                        "after:thermalfoundation;" +
                        //"after:actuallyadditions;" +
                        "after:bigreactors;" +
                        //"after:deepresonance;"
                        "after:waila;" +
                        "after:draconicevolution"
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
    public static final String MOD_VERSION = "1.3.7";

    /**
     * Minecraft version.
     */
    public static final String MC_VERSION = "[1.12.2]";

    /**
     * The package declaration for the mod.
     */
    public static final String MOD_PACKAGE = "com.ki11erwolf.resynth";

    /**
     * The URL to versions.json file.
     */
    public static final String UPDATE_URL = "https://resynth-minecraft-mod.github.io/mod/versions.json";

    /**
     * FML initialized proxy. Can be server side proxy, client side proxy or both.
     */
    @SidedProxy(clientSide = ResynthMod.CLIENT_PROXY, serverSide = ResynthMod.SERVER_PROXY)
    public static IProxy proxy;

    /**
     * Modid for Applied energistics.
     */
    public static final String MODID_AE2 = "appliedenergistics2";

    /**
     * Modid for Forestry.
     */
    public static final String MODID_FORESTRY = "forestry";

    /**
     * Modid for Tinkers' Construct.
     */
    public static final String MODID_TINKERS_CONSTRUCT = "tconstruct";

    /**
     * Thermal Foundation/Thermal Expansion modid.
     */
    public static final String MODID_THERMAL_FOUNDATION = "thermalfoundation";

//    Failed attempt at adding black quartz ore.
//    /**
//     * Actually Additions modid
//     */
//    public static final String MODID_ACTUALLY_ADDITIONS = "actuallyadditions";

    /**
     * Extreme Reactors (port of Big Reactors) modid.
     */
    public static final String MODID_EXTREME_REACTORS = "bigreactors";

    /**
     * Draconic Evolution modid.
     */
    public static final String MODID_DRACONIC_EVOLUTION = "draconicevolution";

//    Failed attempt at adding resonating ore.
//    /**
//     * Deep Resonance modid.
//     */
//    public static final String MODID_DEEP_RESONANCE = "deepresonance";

    /**
     * Path (including pack) to the server proxy class.
     */
    static final String SERVER_PROXY = MOD_PACKAGE + ".proxy.ServerProxy";

    /**
     * Path (including package) to the client proxy class.
     */
    static final String CLIENT_PROXY = MOD_PACKAGE + ".proxy.ClientProxy";

    /**
     * Resynth's new/returning user identification file.
     * This servers to separate new Resynth users
     * from existing ones.
     */
    private static final String RESYNTH_NU_FILE = "/resynth.id";

    /**
     * Logger for the mod.
     */
    private static Logger logger;

    /**
     * The resynth world generator instance.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private static ResynthWorldGen worldGen;

    /**
     * Stage 1 of 3 in the initialization event.
     *
     * @param event pre init event.
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        wrapError((object) -> {
            logger = event.getModLog();
            logger.info("Entering pre-init phase...");
            proxy.preInit(event);

            logger.info("Resynth proxy: " + proxy.getClass().getName());
            if(proxy instanceof ClientProxy){
                ClientCommandHandler.instance.registerCommand(new ResynthCommand());
                logger.info("Attempting Resynth version check...");
                VersionManagerBuilder resynthVMBuilder
                        = new VersionManagerBuilder(MOD_ID)
                        .setEnabled(!ResynthConfig.RESYNTH.disableVersionChecks)
                        .setOutOfDateConsoleWarningEnabled(!ResynthConfig.RESYNTH.disableVersionMessage)
                        .addVersionJsonFileURL(UPDATE_URL);
                ModVersionManager resynthVersionManager = new ModVersionManager(resynthVMBuilder);
                resynthVersionManager.preInit();
            } else {
                logger.info("Resynth is running server side - skipping version check...");
            }

            ResynthAnalytics.send(new ConnectEvent());

            File resynthFile
                    = new File( event.getModConfigurationDirectory().getAbsolutePath()
                            + RESYNTH_NU_FILE);

            if(!resynthFile.exists()){
                ResynthAnalytics.send(new NewUserEvent());
                try {
                    boolean create = resynthFile.createNewFile();
                    if(!create)
                        getLogger().error("Failed to create Resynth NU file");
                } catch (IOException e) {
                    getLogger().error("Failed to create Resynth NU file", e);
                }
            }

            return null;
        });
    }

    /**
     * Stage 2 of 3 in the initialization event.
     *
     * @param event init event.
     */
    @EventHandler
    public void init(FMLInitializationEvent event){
        wrapError((object -> {
            logger.info("Entering init phase...");
            proxy.init(event);

            worldGen = new ResynthWorldGen();
            worldGen.init();

            ResynthFurnaceRecipes.registerFurnaceRecipes();

            //Finally fixed this...
            if(!ResynthConfig.RESYNTH.disableDevelopmentHelp){
                //Prints all registered blocks/items to console.
                //Helps with adding other mods ore plants
                for(Map.Entry<ResourceLocation, Item> entry : ForgeRegistries.ITEMS.getEntries()){
                    ResynthMod.getLogger().info("<Resynth-Development-Help> | Found item: "
                            + entry.getKey().getResourceDomain() + ":"
                            + entry.getKey().getResourcePath());
                }

                for(Map.Entry<ResourceLocation, Block> entry : ForgeRegistries.BLOCKS.getEntries()){
                    ResynthMod.getLogger().info("<Resynth-Development-Help> | Found block: "
                            + entry.getKey().getResourceDomain() + ":"
                            + entry.getKey().getResourcePath());
                }
            }

            return null;
        }));
    }

    /**
     * Stage 3 of 3 in the initialization event.
     *
     * @param event post init event.
     */
    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        wrapError(object -> {
            logger.info("Entering post-init phase...");
            proxy.postInit(event);
            return null;
        });
    }

    /**
     * @return the logger for the mod.
     */
    public static Logger getLogger(){
        return logger;
    }

    /**
     * Wraps a block of code in a try/catch
     * that reports and logs any exceptions.
     *
     * @param t lambda block of code.
     */
    public static void wrapError(Function<Void, Void> t){
        try{
            t.apply(null);
        } catch (Exception e){
            getLogger().fatal("Error in Resynth init()", e);
            ResynthAnalytics.send(new ErrorEvent());
            throw e;
        }
    }

}
