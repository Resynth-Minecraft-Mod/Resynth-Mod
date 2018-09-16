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

import static net.minecraftforge.common.config.Config.*;

/**
 * Holds all the config values for the mod.
 */
@Config(modid = ResynthMod.MOD_ID)
@LangKey(ResynthMod.MOD_ID + ".config.title")
public class ResynthConfig {

    /**
     * Private constructor.
     */
    private ResynthConfig(){}

    /**
     * Configuration settings for the ore the mod uses.
     */
    @Name("Mineral Stone and Mineral Rock")
    @Comment("Configuration settings for the mods ore blocks and items.")
    public static final Ore ORE = new Ore();

    /**
     * Configuration settings for the ore the mod uses.
     */
    public static class Ore{

        /**
         * The block hardness of mods ore block.
         */
        @Name("Mineral Stone Hardness")
        @Comment("The amount of time it takes to break the Mineral Rich Stone block.")
        @RangeDouble(min = 1.0, max = 100.0)
        @RequiresMcRestart
        public float hardness = 10.0F;

        /**
         * The minimum number of items the ore block will drop.
         */
        @Name("Mineral Rock Drop Count")
        @Comment("The amount of Mineral Rocks Mineral Stone drops as a minimum.")
        @RangeInt(min = 0, max = 256)
        public int baseDrops = 1;

        /**
         * The chance of the ore blocking dropping extra items.
         * Where {@code n} is {@code extraChance}
         * and {@code c} is {@code chance}.
         * <p>
         * {@code c = 1/n}
         */
        @Name("Mineral Rock Drop Chance")
        @Comment("The chance of extra Mineral Rocks dropping. Chance is 1 in n.")
        @RangeInt(min = 2, max = 100)
        public int extraChance = 6;

        /**
         * The number of extra items to drop.
         */
        @Name("Mineral Rock Extra Drop Count")
        @Comment("The number of extra Mineral Rocks will drop.")
        @RangeInt(min = 0, max = 256)
        public int extraDrops = 1;

        /**
         * The maximum amount of experience orbs to drop.
         */
        @Name("Mineral Stone XP Count")
        @Comment("Maximum amount of XP orbs dropped by Mineral Rock.")
        @RangeInt(min = 3, max = 150)
        public int maxXP = 6;

        /**
         * The multiplier used when calculating fortune
         * influenced drops. fortune(I) = 1 and fortune(III) = 3.
         * <p>
         * Actual value used is {@code fortune*multiplier}.
         */
        @Name("Fortune Multiplier")
        @Comment("The number of extra Mineral Rocks to drop based on fortune. 1 is 1 extra at fortune 1.")
        @RangeInt(min = 1, max = 20)
        public int multiplier = 1;
    }

    /**
     * Ore generation settings.
     */
    @Name("Mineral Stone World Generation")
    @Comment("Configuration settings for the mods ore world generation.")
    @RequiresMcRestart
    public static final OreGen ORE_GENERATION = new OreGen();

    /**
     * Ore generation settings.
     */
    public static class OreGen{

        /**
         * True if Mineral Stone should be generated in the overworld.
         */
        @Name("Generate Mineral Stone")
        @Comment("Determines if Mineral Stone is generated in the world or not. True to generate.")
        @RequiresMcRestart
        public boolean generate = true;

        /**
         * Number of ore veins (clusters) to generate in a given chunk.
         */
        @Name("Veins Per Chunk")
        @Comment("The number of ore veins (clusters) in a chunk. ")
        @RangeInt(min = 1, max = 32)
        @RequiresMcRestart
        public int perChunk = 2;

        /**
         * Minimum height to generate the ore veins.
         */
        @Name("Minimum Height")
        @Comment("The minimum block height the ore veins will spawn at.")
        @RangeInt(min = 2, max = 252)
        @RequiresMcRestart
        public int minHeight = 3;

        /**
         * Maximum height to generate the ore veins.
         */
        @Name("Maximum Height")
        @Comment("The maximum block height the ore veins will spawn at.")
        @RangeInt(min = 3, max = 254)
        @RequiresMcRestart
        public int maxHeight = 4;
    }

    /**
     * Mineral Enriched Soil Settings.
     */
    @Name("Mineral Enriched Soil")
    @Comment("Configuration settings for Mineral Enriched Soil and other related features.")
    public static final MineralSoil MINERAL_SOIL = new MineralSoil();

    /**
     * Mineral Enriched Soil Settings.
     */
    public static class MineralSoil{

        /**
         * The percentage one Mineral Rock item
         * will increase the soils mineral content.
         */
        @Name("Mineral Rock Value")
        @Comment("The value of one Mineral Rock. One Mineral Rock item will increase the mineral content percentage value of Mineral Enriched Soil by this amount.")
        @RangeDouble(min = 0.1, max = 49.0)
        public float mineralValue = 1.0F;
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
