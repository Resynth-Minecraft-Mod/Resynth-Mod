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

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Holds all the config values for the mod.
 */
@Config(modid = ResynthMod.MOD_ID)
@Config.LangKey(ResynthMod.MOD_ID + ".config.title")
public class ResynthConfig {

    /**
     * Private constructor.
     */
    private ResynthConfig(){}

    /**
     * A boolean config value for testing.
     */
    @Config.Comment("A test boolean")
    public static boolean testBoolean = false;

    /**
     * A test config category.
     */
    public static final Test TEST = new Test();

    /**
     * A test class that represents a config category.
     */
    public static class Test{

        /**
         * Test integer config property.
         */
        @Config.Comment("Test Integer A")
        public int TestIntegerA = 20;

        /**
         * Test integer config property.
         */
        @Config.Comment("Test Integer B")
        public int TestIntegerB = 30;
    }

    /**
     * Handles the syncing of config values
     * when they change.
     */
    @Mod.EventBusSubscriber(modid = ResynthMod.MOD_ID)
    private static class EventHandler {

        /**
         * Inject the new values and save to the config file when the config has been changed from the GUI.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(ResynthMod.MOD_ID)) {
                ConfigManager.sync(ResynthMod.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
