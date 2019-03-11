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

import com.ki11erwolf.resynth.analytics.ErrorEvent;
import com.ki11erwolf.resynth.analytics.ResynthAnalytics;
import com.ki11erwolf.resynth.proxy.Client;
import com.ki11erwolf.resynth.proxy.Proxy;
import com.ki11erwolf.resynth.proxy.Common;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

/**
 * Resynth mod class.
 */
//@Mod(
//        modid = ResynthMod.MOD_ID,
//        name = ResynthMod.MOD_NAME,
//        version = ResynthMod.MOD_VERSION,
//        acceptedMinecraftVersions = ResynthMod.MC_VERSION,
//        dependencies =
//                        "after:appliedenergistics2; " +
//                        "after:tconstruct; " +
//                        "after:forestry; " +
//                        "after:thermalfoundation;" +
//                        //"after:actuallyadditions;" +
//                        "after:bigreactors;" +
//                        //"after:deepresonance;"
//                        "after:waila;"
//)
@Mod(ResynthMod.MOD_ID)
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
    public static final String MOD_VERSION = "2.0.0";

    /**
     * Minecraft version.
     */
    public static final String MC_VERSION = "[1.13.2]";

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
    public static final Proxy proxy = DistExecutor.runForDist(() -> Client::new, () -> Common::new);

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

    /**
     * Extreme Reactors (port of Big Reactors) modid.
     */
    public static final String MODID_EXTREME_REACTORS = "bigreactors";

    /**
     * Path (including pack) to the server proxy class.
     */
    static final String SERVER_PROXY = MOD_PACKAGE + ".proxy.Common";

    /**
     * Path (including package) to the client proxy class.
     */
    static final String CLIENT_PROXY = MOD_PACKAGE + ".proxy.Client";

    /**
     * Resynth's new/returning user identification file.
     * This servers to separate new Resynth users
     * from existing ones.
     */
    public static final String RESYNTH_NU_FILE = "/resynth.id";

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
     * Mod constructor.
     *
     * Calls the construct method of the selected proxy class.
     */
    public ResynthMod(){
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        wrapError((object) -> {
            proxy.construct();
            return null;
        }, "construct");
    }

    /**
     * Forge FMLCommonSetupEvent handler.
     *
     * @param setupEvent forge common setup event.
     */
    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent setupEvent){
        wrapError((object) -> {
            proxy.commonSetup(setupEvent);
            return null;
        }, "commonSetup");
    }

    /**
     * Forge FMLClientSetupEvent handler.
     *
     * @param setupEvent forge client setup event.
     */
    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent setupEvent){
        wrapError((object) -> {
            proxy.clientSetup(setupEvent);
            return null;
        }, "clientSetup");
    }

    /**
     * Forge FMLLoadCompleteEvent handler.
     *
     * @param completeEvent forge complete event.
     */
    @SubscribeEvent
    public void complete(FMLLoadCompleteEvent completeEvent){
        wrapError((object) -> {
            proxy.complete(completeEvent);
            return null;
        }, "complete");
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
    private static void wrapError(Function<Void, Void> t, String initEvent){
        try{
            t.apply(null);
        } catch (Exception e){
            getLogger().fatal("Error in Resynth " + initEvent + "()", e);
            ResynthAnalytics.send(new ErrorEvent());
            throw e;
        }
    }
}
