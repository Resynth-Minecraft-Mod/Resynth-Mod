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
     * Configuration settings for the ore the mod uses.
     */
    public static final Ore ORE = new Ore();

    /**
     * Configuration settings for the ore the mod uses.
     */
    public static class Ore{

        /**
         * The block hardness of mods ore block.
         */
        @Config.Comment("Mineral Rich Stone hardness")
        public float oreHardness = 3.0F;

        /**
         * The minimum number of items the ore block will drop.
         */
        @Config.Comment("Minimum base number of items dropped by Mineral Rich Stone")
        public int oreBaseDrops = 1;

        /**
         * The chance of the ore blocking dropping extra items.
         * Where {@code n} is {@code oreChanceOfExtraDropIn}
         * and {@code c} is {@code chance}.
         * <p>
         * {@code c = 1/n}
         */
        @Config.Comment("Chance of dropping an extra item. 1 in n")
        public int oreChanceOfExtraDropIn = 6;

        /**
         * The number of extra items to drop.
         */
        @Config.Comment("Number of extra drops.")
        public int oreExtraDrops = 1;

        /**
         * The maximum amount of experience orbs to drop.
         */
        @Config.Comment("Maximum amount of XP dropped")
        public int maxXP = 6;

        /**
         * The multiplier used when calculating fortune
         * influenced drops. fortune(I) = 1 and fortune(III) = 3.
         * <p>
         * Actual value used is {@code fortune*multiplier}.
         */
        @Config.Comment("Fortune drop multiplier.")
        public int multiplier = 1;
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
