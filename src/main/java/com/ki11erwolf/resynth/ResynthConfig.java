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
        @Comment("The value of one Mineral Rock. One Mineral Rock item will increase the mineral " +
                "content percentage value of Mineral Enriched Soil by this amount.")
        @RangeDouble(min = 0.1, max = 49.0)
        public float mineralValue = 1.0F;
    }

    //*********************************
    //              PLANTS
    //*********************************

    /**
     * Settings for all plants and plant types.
     */
    @Name("General Plant Settings")
    @Comment("Settings for plants as a whole.")
    public static final PlantGeneral PLANTS_GENERAL = new PlantGeneral();

    /**
     * Settings for all plants and plant types.
     */
    public static class PlantGeneral{

        /**
         * Can bonemeal be used on plants.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to false to disable bonemeal on all plants.")
        public boolean canBonemeal = false;

        /**
         * Do minecraft ore blocks drop seeds.
         */
        @Name("Enable Seed Drops From Ore")
        @Comment("Set to false to disable ores dropping seeds when blown up.")
        public boolean oreDropSeeds = true;

        /**
         * Do plant ore blocks drop seeds.
         */
        @Name("Enable Seed Drops From Organic Ore")
        @Comment("Set to false to disable organic ores dropping seeds when blown up.")
        public boolean organicOreDropSeeds = true;

        /**
         * Can plant produce turn into seeds.
         */
        @Name("Enable Seed Drops From Produce (straw)")
        @Comment("Set to false to disable plant produce (straw) turning into seeds when left in water")
        public boolean produceDropSeeds = true;

        /**
         * Should plants grow.
         */
        @Name("Enable Growth")
        @Comment("Set to false to disable growth of all plants. This effectively disables the mod")
        public boolean enableGrowth = true;
    }

    /**
     * Settings for the iron plant.
     */
    @Name("Plant: Iron")
    @Comment("Settings for the Iron Plant.")
    public static final PlantIron PLANT_IRON = new PlantIron();

    /**
     * Settings for the iron plant.
     */
    public static class PlantIron{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Flowering Period")
        @Comment("How long it takes this species of plant to grow. " +
                "The higher the number, the longer the plant takes to grow. " +
                "Each increment doubles the time the plant takes to grow. " +
                "E.g. two is twice as long as one.")
        @RangeInt(min = 1, max = 100_000)
        public int floweringPeriod = 25;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant.")
        public boolean canBonemeal = true;

        /**
         * Does the minecraft ore block this plant
         * represents drop seeds.
         */
        @Name("Does Ore Drop Seeds")
        @Comment("If set to true, iron ore will occasionally drop iron seeds when blown up.")
        public boolean oreDropSeeds = true;

        /**
         * Does the ore block this plant
         * produces drop seeds.
         */
        @Name("Does Organic Ore Drop Seeds")
        @Comment("If set to true, organic iron ore will occasionally drop iron seeds when blown up.")
        public boolean organicOreDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from iron ore.
         */
        @Name("Ore Seed Drop Chance")
        @Comment("The chance iron ore will drop iron seeds. The chance of dropping a seed " +
                "is one in Ore Seed Drop Chance + 1.")
        @RangeInt(min = 1, max = 100_000)
        public int oreSeedDropChance = 32;

        /**
         * The chance of seeds dropping from
         * organic iron ore.
         */
        @Name("Organic Ore Seed Drop Chance")
        @Comment("The chance organic iron ore will drop iron seeds. The chance of dropping a seed " +
                "is one in Organic Ore Seed Drop Chance + 1.")
        @RangeInt(min = 1, max = 100_000)
        public int organicOreSeedDropChance = 64;

        /**
         * The number of ingots the plants ore
         * block gives.
         */
        @Name("Yield")
        @Comment("The amount of iron ingots an Organic Iron Ore block will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;
    }

    /**
     * Settings for the gold plant.
     */
    @Name("Plant: Gold")
    @Comment("Settings for the Gold Plant.")
    public static final PlantGold PLANT_GOLD = new PlantGold();

    /**
     * Settings for the gold plant.
     */
    public static class PlantGold{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Flowering Period")
        @Comment("How long it takes this species of plant to grow. " +
                "The higher the number, the longer the plant takes to grow. " +
                "Each increment doubles the time the plant takes to grow. " +
                "E.g. two is twice as long as one.")
        @RangeInt(min = 1, max = 100_000)
        public int floweringPeriod = 45;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant.")
        public boolean canBonemeal = true;

        /**
         * Does the minecraft ore block this plant
         * represents drop seeds.
         */
        @Name("Does Ore Drop Seeds")
        @Comment("If set to true, gold ore will occasionally drop gold seeds when blown up.")
        public boolean oreDropSeeds = true;

        /**
         * Does the ore block this plant
         * produces drop seeds.
         */
        @Name("Does Organic Ore Drop Seeds")
        @Comment("If set to true, organic gold ore will occasionally drop gold seeds when blown up.")
        public boolean organicOreDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from gold ore.
         */
        @Name("Ore Seed Drop Chance")
        @Comment("The chance gold ore will drop gold seeds. The chance of dropping a seed " +
                "is one in Ore Seed Drop Chance + 1.")
        @RangeInt(min = 1, max = 100_000)
        public int oreSeedDropChance = 64;

        /**
         * The chance of seeds dropping from
         * organic gold ore.
         */
        @Name("Organic Ore Seed Drop Chance")
        @Comment("The chance organic gold ore will drop gold seeds. The chance of dropping a seed " +
                "is one in Organic Ore Seed Drop Chance + 1.")
        @RangeInt(min = 1, max = 100_000)
        public int organicOreSeedDropChance = 96;

        /**
         * The number of ingots the plants ore
         * block gives.
         */
        @Name("Yield")
        @Comment("The amount of gold ingots an Organic Gold Ore block will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;
    }

    /**
     * Settings for the diamond plant.
     */
    @Name("Plant: Diamond")
    @Comment("Settings for the Diamond Plant.")
    public static final PlantDiamond PLANT_DIAMOND = new PlantDiamond();

    /**
     * Settings for the diamond plant.
     */
    public static class PlantDiamond{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Flowering Period")
        @Comment("How long it takes this species of plant to grow. " +
                "The higher the number, the longer the plant takes to grow. " +
                "Each increment doubles the time the plant takes to grow. " +
                "E.g. two is twice as long as one.")
        @RangeInt(min = 1, max = 100_000)
        public int floweringPeriod = 45;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft ore block this plant
         * represents drop seeds.
         */
        @Name("Does Ore Drop Seeds")
        @Comment("If set to true, diamond ore will occasionally drop diamond seeds when mined.")
        public boolean oreDropSeeds = true;

        /**
         * Does the ore block this plant
         * produces drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, diamond straw will occasionally turn into diamond seeds when left in water.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from diamond ore.
         */
        @Name("Ore Seed Drop Chance")
        @Comment("The chance diamond ore will drop diamond seeds. The chance of dropping a seed " +
                "is one in Ore Seed Drop Chance + 1.")
        @RangeInt(min = 1, max = 100_000)
        public int oreSeedDropChance = 64;

        /**
         * The chance of seeds dropping from
         * diamond straw.
         */
        @Name("Produce Seed Drop Chance")
        @Comment("The chance diamond straw will turn into diamond seeds when left in water. " +
                "The chance of dropping a seed " +
                "is one in Produce Seed Drop Chance + 1.")
        @RangeInt(min = 1, max = 100_000)
        public int produceSeedDropChance = 96;

        /**
         * The number of diamonds the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of diamonds diamond straw will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;
    }

    /**
     * Settings for the diamond plant.
     */
    @Name("Plant: Redstone")
    @Comment("Settings for the Redstone Plant.")
    public static final PlantRedstone PLANT_REDSTONE = new PlantRedstone();

    /**
     * Settings for the diamond plant.
     */
    public static class PlantRedstone{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Flowering Period")
        @Comment("How long it takes this species of plant to grow. " +
                "The higher the number, the longer the plant takes to grow. " +
                "Each increment doubles the time the plant takes to grow. " +
                "E.g. two is twice as long as one.")
        @RangeInt(min = 1, max = 100_000)
        public int floweringPeriod = 35;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft ore block this plant
         * represents drop seeds.
         */
        @Name("Does Ore Drop Seeds")
        @Comment("If set to true, diamond ore will occasionally drop diamond seeds when mined.")
        public boolean oreDropSeeds = true;

        /**
         * Does the ore block this plant
         * produces drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, redstone straw will occasionally turn into redstone seeds when left in water.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from redstone ore.
         */
        @Name("Ore Seed Drop Chance")
        @Comment("The chance redstone ore will drop redstone seeds. The chance of dropping a seed " +
                "is one in Ore Seed Drop Chance + 1.")
        @RangeInt(min = 1, max = 100_000)
        public int oreSeedDropChance = 48;

        /**
         * The chance of seeds dropping from
         * redstone straw.
         */
        @Name("Produce Seed Drop Chance")
        @Comment("The chance redstone straw will turn into redstone seeds when left in water. " +
                "The chance of dropping a seed " +
                "is one in Produce Seed Drop Chance + 1.")
        @RangeInt(min = 1, max = 100_000)
        public int produceSeedDropChance = 64;

        /**
         * The number of redstone the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of redstone dust redstone straw will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;
    }

    /**
     * Settings for the lapis plant.
     */
    @Name("Plant: Lapis")
    @Comment("Settings for the Lapis Plant.")
    public static final PlantLapis PLANT_LAPIS = new PlantLapis();

    /**
     * Settings for the lapis plant.
     */
    public static class PlantLapis{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Flowering Period")
        @Comment("How long it takes this species of plant to grow. " +
                "The higher the number, the longer the plant takes to grow. " +
                "Each increment doubles the time the plant takes to grow. " +
                "E.g. two is twice as long as one.")
        @RangeInt(min = 1, max = 100_000)
        public int floweringPeriod = 40;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft ore block this plant
         * represents drop seeds.
         */
        @Name("Does Ore Drop Seeds")
        @Comment("If set to true, lapis ore will occasionally drop lapis seeds when mined.")
        public boolean oreDropSeeds = true;

        /**
         * Does the ore block this plant
         * produces drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, lapis straw will occasionally turn into lapis seeds when left in water.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from lapis ore.
         */
        @Name("Ore Seed Drop Chance")
        @Comment("The chance lapis ore will drop lapis seeds. The chance of dropping a seed " +
                "is one in Ore Seed Drop Chance + 1.")
        @RangeInt(min = 1, max = 100_000)
        public int oreSeedDropChance = 48;

        /**
         * The chance of seeds dropping from
         * lapis straw.
         */
        @Name("Produce Seed Drop Chance")
        @Comment("The chance lapis straw will turn into lapis seeds when left in water. " +
                "The chance of dropping a seed " +
                "is one in Produce Seed Drop Chance + 1.")
        @RangeInt(min = 1, max = 100_000)
        public int produceSeedDropChance = 48*2;

        /**
         * The number of lapis items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of lapis lazuli lapis straw will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;
    }

    /**
     * Settings for the coal plant.
     */
    @Name("Plant: coal")
    @Comment("Settings for the coal Plant.")
    public static final PlantCoal PLANT_COAL = new PlantCoal();

    /**
     * Settings for the coal plant.
     */
    public static class PlantCoal{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Flowering Period")
        @Comment("How long it takes this species of plant to grow. " +
                "The higher the number, the longer the plant takes to grow. " +
                "Each increment doubles the time the plant takes to grow. " +
                "E.g. two is twice as long as one.")
        @RangeInt(min = 1, max = 100_000)
        public int floweringPeriod = 15;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft ore block this plant
         * represents drop seeds.
         */
        @Name("Does Ore Drop Seeds")
        @Comment("If set to true, coal ore will occasionally drop coal seeds when mined.")
        public boolean oreDropSeeds = true;

        /**
         * Does the ore block this plant
         * produces drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, coal straw will occasionally turn into coal seeds when left in water.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from coal ore.
         */
        @Name("Ore Seed Drop Chance")
        @Comment("The chance coal ore will drop coal seeds. The chance of dropping a seed " +
                "is one in Ore Seed Drop Chance + 1.")
        @RangeInt(min = 1, max = 100_000)
        public int oreSeedDropChance = 32;

        /**
         * The chance of seeds dropping from
         * coal straw.
         */
        @Name("Produce Seed Drop Chance")
        @Comment("The chance coal straw will turn into coal seeds when left in water. " +
                "The chance of dropping a seed " +
                "is one in Produce Seed Drop Chance + 1.")
        @RangeInt(min = 1, max = 100_000)
        public int produceSeedDropChance = 64;

        /**
         * The number of coal items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of coal lazuli coal straw will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;
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
