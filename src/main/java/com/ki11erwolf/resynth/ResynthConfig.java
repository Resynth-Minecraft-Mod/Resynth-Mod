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
import net.minecraftforge.fml.common.Loader;
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
     * Settings related to core of resynth.
     */
    @Name("Resynth")
    @Comment("Settings for the Resynth mod itself.")
    public static final Resynth RESYNTH = new Resynth();

    /**
     * Settings related to the core of resynth.
     */
    public static class Resynth {

        /**
         * Disables the google analytics event from firing when true.
         */
        @Name("Disable Analytics (Not in use)")
        @Comment("Set to true to disable the mods analytics functionality (not in use).")
        @RequiresMcRestart
        public boolean disableAnalytics = true;

        /**
         * Disables all version checks when true.
         */
        @Name("Disable Version Checks")
        @Comment("Set to true to disable everything related to version checking")
        @RequiresMcRestart
        public boolean disableVersionChecks = false;

        /**
         * Disables out of date messages from appearing in chat.
         */
        @Name("Disable Version Chat Message")
        @Comment("Set to true to disable the chat message that" +
                " notifies you when your version of resynth is out of date")
        @RequiresMcRestart
        public boolean disableVersionMessage = false;
    }

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
         * Chance is percentage based.
         */
        @Name("Extra Mineral Rock Drop Chance")
        @Comment("The chance of extra Mineral Rocks dropping. Chance is percentage based")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float extraChance = 50.0F;

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
        public int perChunk = 5;

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

        /**
         * The average number of blocks per vein.
         */
        @Name("Block Count")
        @Comment("The average number of blocks per ore vein")
        @RangeInt(min = 1, max = 10_000)
        @RequiresMcRestart
        public int blockCount = 10;
    }

    /**
     * All settings relating to the mystical seed pod.
     */
    @Name("Mystical Seed Pod")
    @Comment("Settings for the Mystical Seed Pod and its world generation")
    @RequiresMcRestart
    public static final MysticalSeedPod MYSTICAL_SEED_POD = new MysticalSeedPod();

    /**
     * All settings relating to the mystical seed pod.
     */
    public static class MysticalSeedPod {
        /**
         * Mystical seed pod world gen toggle.
         */
        @Name("Generate Mystical Seed Pods")
        @Comment("If true, Mystical Seed Pods will generate in the world.")
        @RequiresMcRestart
        public boolean generate = true;

        /**
         * Seed pod drop seeds toggle.
         */
        @Name("Should Drop Seeds")
        @Comment("If true, Mystical Seed Pods will drop a random Biochemical seed." +
                "If false, the plant block will be dropped instead. This offers a way to obtain" +
                " biochemical seeds in peaceful mode.")
        public boolean dropSeeds = false;

        /**
         * If false, has the chance to drop no item.
         */
        @Name("Should Always Drop Seeds")
        @Comment("If false, Mystical Seed Pods have a chance to drop no seeds.")
        public boolean alwaysDropSeeds = false;

        /**
         * Number of attempts at finding a suitable seed type to
         * drop before giving up.
         */
        @Name("Seed Attempts Per Break")
        @Comment("The number of times a Mystical Seed Pod will calculate the chance of the randomly" +
                " selected seeds" +
                " dropping before giving up. Making this number too high may affect performance. " +
                " Making this number too low will affect seed drop chance calculations")
        @RangeInt(min = 10, max = 1_000)
        public int triesPerBreak = 30;

        /**
         * Number of flower fields (clusters) to generate in a given chunk.
         */
        @Name("Fields Per Chunk")
        @Comment("The average number of flower fields (clusters) in a chunk. ")
        @RangeInt(min = 1, max = 32)
        @RequiresMcRestart
        public int perChunk = 25;

        /**
         * Minimum height to generate the fields.
         */
        @Name("Minimum Height")
        @Comment("The minimum block height the flower fields will spawn at.")
        @RangeInt(min = 2, max = 252)
        @RequiresMcRestart
        public int minHeight = 35;

        /**
         * Maximum height to generate the fields.
         */
        @Name("Maximum Height")
        @Comment("The maximum block height the flower fields will spawn at.")
        @RangeInt(min = 3, max = 254)
        @RequiresMcRestart
        public int maxHeight = 110;

        /**
         * The average number of blocks per field.
         */
        @Name("Block Count")
        @Comment("The average number of plants per field")
        @RangeInt(min = 1, max = 10_000)
        @RequiresMcRestart
        public int blockCount = 6;
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
         * Do mobs drop seeds.
         */
        @Name("Enable Seed Drops From Mobs")
        @Comment("Set to false to disable mobs dropping seeds when killed.")
        public boolean mobDropSeeds = true;

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
    @Comment("Settings for the iron plant.")
    public static final PlantIron PLANT_IRON = new PlantIron();

    /**
     * Settings for the iron plant.
     */
    public static class PlantIron{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 6.0F;

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
         * from this species of plants ore block.
         */
        @Name("Seed Drop Chance From Ore")
        @Comment("The chance of the ore block dropping seeds for this species of plant when blown up." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float oreSeedDropChance = 3.0F;

        /**
         * The chance of seeds dropping from
         * this plant species' produce.
         */
        @Name("Seed Drop Chance From Organic Ore")
        @Comment("The chance of this plants produce (organic ore) dropping seeds " +
                "when blown up by TNT.  This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float organicOreSeedDropChance = 1.5F;

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
    @Comment("Settings for the gold plant.")
    public static final PlantGold PLANT_GOLD = new PlantGold();

    /**
     * Settings for the gold plant.
     */
    public static class PlantGold{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance.  This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 4.0F;

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
         * from this species of plants ore block.
         */
        @Name("Seed Drop Chance From Ore")
        @Comment("The chance of the ore block dropping seeds for this species of plant when blown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float oreSeedDropChance = 6.0F;

        /**
         * The chance of seeds dropping from
         * this plant species' produce.
         */
        @Name("Seed Drop Chance From Organic Ore")
        @Comment("The chance of this plants produce (organic ore) dropping seeds " +
                "when blown up by TNT.  This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float organicOreSeedDropChance = 3.0F;

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
     * Settings for the mineral plant.
     */
    @Name("Plant: Mineral")
    @Comment("Settings for the mineral plant.")
    public static final PlantMineral PLANT_MINERAL = new PlantMineral();

    /**
     * Settings for the mineral plant.
     */
    public static class PlantMineral{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 12.0F;

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
        @Comment("If set to true, mineral rock ore will occasionally drop mineral seeds when mined.")
        public boolean oreDropSeeds = true;

        /**
         * Does the ore block this plant
         * produces drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, mineral straw will occasionally turn into mineral seeds when left in water.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants ore block.
         */
        @Name("Seed Drop Chance From Ore")
        @Comment("The chance this plants ore will drop seeds when mined." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float oreSeedDropChance = 25.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (straw) from this plant type will drop seeds." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 15.0F;

        /**
         * The number of diamonds the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of diamonds diamond straw will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 2;
    }

    /**
     * Settings for the diamond plant.
     */
    @Name("Plant: Diamond")
    @Comment("Settings for the diamond plant.")
    public static final PlantDiamond PLANT_DIAMOND = new PlantDiamond();

    /**
     * Settings for the diamond plant.
     */
    public static class PlantDiamond{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 6.0F;

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
         * from this plants minecraft ore block.
         */
        @Name("Seed Drop Chance From Ore")
        @Comment("The chance this plants ore block will drop seeds when mined." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float oreSeedDropChance = 5.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (straw) from this plant type will drop seeds." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 2.5F;

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
     * Settings for the redstone plant.
     */
    @Name("Plant: Redstone")
    @Comment("Settings for the redstone plant.")
    public static final PlantRedstone PLANT_REDSTONE = new PlantRedstone();

    /**
     * Settings for the redstone plant.
     */
    public static class PlantRedstone{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 10.0F;

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
        @Comment("If set to true, redstone ore will occasionally drop redstibe seeds when mined.")
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
         * from this plants minecraft ore block.
         */
        @Name("Seed Drop Chance From Ore")
        @Comment("The chance this plants ore will drop seeds when mined." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float oreSeedDropChance = 1.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (straw) from this plant type will drop seeds." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 0.5F;

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
    @Comment("Settings for the lapis lazuli plant.")
    public static final PlantLapis PLANT_LAPIS = new PlantLapis();

    /**
     * Settings for the lapis plant.
     */
    public static class PlantLapis{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 4.5F;

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
         * from this plants minecraft ore block.
         */
        @Name("Seed Drop Chance From Ore")
        @Comment("The chance this plants ore block will drop seeds when mined." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float oreSeedDropChance = 4.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (straw) from this plant type will drop seeds." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 2.0F;

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
    @Name("Plant: Coal")
    @Comment("Settings for the coal plant.")
    public static final PlantCoal PLANT_COAL = new PlantCoal();

    /**
     * Settings for the coal plant.
     */
    public static class PlantCoal{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 30.0F;

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
         * from this plants minecraft ore block.
         */
        @Name("Seed Drop Chance From Ore")
        @Comment("The chance this plants ore block will drop seeds when mined." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float oreSeedDropChance = 1.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (straw) from this plant type will drop seeds." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 0.5F;

        /**
         * The number of coal items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of coal items coal straw will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;
    }

    /**
     * Settings for the emerald plant.
     */
    @Name("Plant: Emerald")
    @Comment("Settings for the emerald plant.")
    public static final PlantEmerald PLANT_EMERALD = new PlantEmerald();

    /**
     * Settings for the emerald plant.
     */
    public static class PlantEmerald{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 2.0F;

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
        @Comment("If set to true, emerald ore will occasionally drop emerald seeds when mined.")
        public boolean oreDropSeeds = true;

        /**
         * Does the ore block this plant
         * produces drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, emerald straw will occasionally turn into emerald seeds when left in water.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft ore block.
         */
        @Name("Seed Drop Chance From Ore")
        @Comment("The chance this plants ore block will drop seeds when mined." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float oreSeedDropChance = 10.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (straw) from this plant type will drop seeds." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 5.0F;

        /**
         * The number of emerald items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of emeralds emerald straw will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;
    }

    /**
     * Settings for the glowstone plant.
     */
    @Name("Plant: Glowstone")
    @Comment("Settings for the glowstone plant.")
    public static final PlantGlowstone PLANT_GLOWSTONE = new PlantGlowstone();

    /**
     * Settings for the glowstone plant.
     */
    public static class PlantGlowstone{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 25.0F;

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
        @Comment("If set to true, glowstone will occasionally drop glowstone seeds when mined.")
        public boolean oreDropSeeds = true;

        /**
         * Does the ore block this plant
         * produces drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, glowstone straw will occasionally turn into glowstone seeds when left in water.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft ore block.
         */
        @Name("Seed Drop Chance From Ore")
        @Comment("The chance this plants ore block will drop seeds when mined." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float oreSeedDropChance = 2.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (straw) from this plant type will drop seeds." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 1.0F;

        /**
         * The number of glowstone items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of glowstone glowstone straw will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;
    }

    /**
     * Settings for the quartz plant.
     */
    @Name("Plant: Quartz")
    @Comment("Settings for the quartz plant.")
    public static final PlantQuartz PLANT_QUARTZ = new PlantQuartz();

    /**
     * Settings for the quartz plant.
     */
    public static class PlantQuartz{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 25.0F;

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
        @Comment("If set to true, quartz ore will occasionally drop quartz seeds when mined.")
        public boolean oreDropSeeds = true;

        /**
         * Does the ore block this plant
         * produces drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, quartz straw will occasionally turn into quartz seeds when left in water.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft ore block.
         */
        @Name("Seed Drop Chance From Ore")
        @Comment("The chance this plants ore block will drop seeds when mined." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float oreSeedDropChance = 2.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (straw) from this plant type will drop seeds." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 1.0F;

        /**
         * The number of quartz items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of quartz quartz straw will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;
    }

    /**
     * Settings for the ender pearl plant.
     */
    @Name("Plant: Ender Pearl")
    @Comment("Settings for the Ender Pearl plant.")
    public static final PlantEnderpearl PLANT_ENDERPEARL = new PlantEnderpearl();

    /**
     * Settings for the ender pearl plant.
     */
    public static class PlantEnderpearl{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 9.0F;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft mob that represents
         * this plant drop seeds?
         */
        @Name("Do Mobs Drop Seeds")
        @Comment("If set to true, endermen will sometimes drop seeds when killed.")
        public boolean mobDropSeeds = true;

        /**
         * Does the plants produce drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, enderpearl bulbs will occasionally turn into seeds when thrown.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft mob.
         */
        @Name("Seed Drop Chance From Mob")
        @Comment("The chance an enderman will drop seeds when killed. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float mobSeedDropChance = 8.5F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (bulb) from this plant type will drop seeds when thrown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 5.0F;

        /**
         * The number of ender pearls the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of ender pearls enderpearl straw will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;

        /**
         * The chance this plants seeds will drop from a mystical seed pod.
         */
        @Name("Mystical Seed Pod Drop Chance")
        @Comment("The chance this plant types seeds will be chosen to drop when a" +
                " mystical seed pod is broken.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float seedPodDropChance = 20F;
    }

    /**
     * Settings for the ender pearl plant.
     */
    @Name("Plant: Gunpowder")
    @Comment("Settings for the Ender Pearl plant.")
    public static final PlantGunpowder PLANT_GUNPOWDER = new PlantGunpowder();

    /**
     * Settings for the gunpowder plant.
     */
    public static class PlantGunpowder{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 30.0F;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft mob that represents
         * this plant drop seeds?
         */
        @Name("Do Mobs Drop Seeds")
        @Comment("If set to true, creepers will sometimes drop seeds when killed.")
        public boolean mobDropSeeds = true;

        /**
         * Does the plants produce drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, gunpowder bulbs will occasionally turn into seeds when thrown.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft mob.
         */
        @Name("Seed Drop Chance From Mob")
        @Comment("The chance a creeper will drop seeds when killed. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float mobSeedDropChance = 10.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (bulb) from this plant type will drop seeds when thrown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 15.0F;

        /**
         * The number of gunpowder items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of gunpowder gunpowder bulbs will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;

        /**
         * The chance this plants seeds will drop from a mystical seed pod.
         */
        @Name("Mystical Seed Pod Drop Chance")
        @Comment("The chance this plant types seeds will be chosen to drop when a" +
                " mystical seed pod is broken.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float seedPodDropChance = 30F;
    }

    /**
     * Settings for the blaze plant.
     */
    @Name("Plant: Blaze")
    @Comment("Settings for the Blaze plant.")
    public static final PlantBlaze PLANT_BLAZE = new PlantBlaze();

    /**
     * Settings for the blaze plant.
     */
    public static class PlantBlaze{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 10.0F;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft mob that represents
         * this plant drop seeds?
         */
        @Name("Do Mobs Drop Seeds")
        @Comment("If set to true, blaze will sometimes drop seeds when killed.")
        public boolean mobDropSeeds = true;

        /**
         * Does the plants produce drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, blaze bulbs will occasionally turn into seeds when thrown.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft mob.
         */
        @Name("Seed Drop Chance From Mob")
        @Comment("The chance a blaze will drop seeds when killed. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float mobSeedDropChance = 8.5F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (bulb) from this plant type will drop seeds when thrown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 5.0F;

        /**
         * The number of blaze rod items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of blaze rods blaze bulbs will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;

        /**
         * The chance this plants seeds will drop from a mystical seed pod.
         */
        @Name("Mystical Seed Pod Drop Chance")
        @Comment("The chance this plant types seeds will be chosen to drop when a" +
                " mystical seed pod is broken.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float seedPodDropChance = 15F;
    }

    /**
     * Settings for the bone plant.
     */
    @Name("Plant: Bone")
    @Comment("Settings for the bone plant.")
    public static final PlantBone PLANT_BONE = new PlantBone();

    /**
     * Settings for the bone plant.
     */
    public static class PlantBone{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 35.0F;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft mob that represents
         * this plant drop seeds?
         */
        @Name("Do Mobs Drop Seeds")
        @Comment("If set to true, skeletons will sometimes drop seeds when killed.")
        public boolean mobDropSeeds = true;

        /**
         * Does the plants produce drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, bone bulbs will occasionally turn into seeds when thrown.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft mob.
         */
        @Name("Seed Drop Chance From Mob")
        @Comment("The chance a skeleton will drop seeds when killed. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float mobSeedDropChance = 10.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (bulb) from this plant type will drop seeds when thrown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 15.0F;

        /**
         * The number of bone items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of bones bone bulbs will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 2;

        /**
         * The chance this plants seeds will drop from a mystical seed pod.
         */
        @Name("Mystical Seed Pod Drop Chance")
        @Comment("The chance this plant types seeds will be chosen to drop when a" +
                " mystical seed pod is broken.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float seedPodDropChance = 30F;
    }

    /**
     * Settings for the string plant.
     */
    @Name("Plant: String")
    @Comment("Settings for the string plant.")
    public static final PlantString PLANT_STRING = new PlantString();

    /**
     * Settings for the string plant.
     */
    public static class PlantString{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 35.0F;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft mob that represents
         * this plant drop seeds?
         */
        @Name("Do Mobs Drop Seeds")
        @Comment("If set to true, spiders will sometimes drop seeds when killed.")
        public boolean mobDropSeeds = true;

        /**
         * Does the plants produce drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, string bulbs will occasionally turn into seeds when thrown.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft mob.
         */
        @Name("Seed Drop Chance From Mob")
        @Comment("The chance a spider will drop seeds when killed. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float mobSeedDropChance = 10.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (bulb) from this plant type will drop seeds when thrown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 15.0F;

        /**
         * The number of bone items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of string items string bulbs will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;

        /**
         * The chance this plants seeds will drop from a mystical seed pod.
         */
        @Name("Mystical Seed Pod Drop Chance")
        @Comment("The chance this plant types seeds will be chosen to drop when a" +
                " mystical seed pod is broken.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float seedPodDropChance = 30F;
    }

    /**
     * Settings for the feather plant.
     */
    @Name("Plant: Feather")
    @Comment("Settings for the feather plant.")
    public static final PlantFeather PLANT_FEATHER = new PlantFeather();

    /**
     * Settings for the feather plant.
     */
    public static class PlantFeather{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 35.0F;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft mob that represents
         * this plant drop seeds?
         */
        @Name("Do Mobs Drop Seeds")
        @Comment("If set to true, chickens will sometimes drop seeds when killed.")
        public boolean mobDropSeeds = true;

        /**
         * Does the plants produce drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, feather bulbs will occasionally turn into seeds when thrown.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft mob.
         */
        @Name("Seed Drop Chance From Mob")
        @Comment("The chance a chicken will drop seeds when killed. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float mobSeedDropChance = 10.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (bulb) from this plant type will drop seeds when thrown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 20.0F;

        /**
         * The number of feather items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of feather items feather bulbs will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 2;

        /**
         * The chance this plants seeds will drop from a mystical seed pod.
         */
        @Name("Mystical Seed Pod Drop Chance")
        @Comment("The chance this plant types seeds will be chosen to drop when a" +
                " mystical seed pod is broken.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float seedPodDropChance = 35F;
    }

    /**
     * Settings for the ghast plant.
     */
    @Name("Plant: Ghast")
    @Comment("Settings for the ghast plant.")
    public static final PlantGhast PLANT_GHAST = new PlantGhast();

    /**
     * Settings for the ghast plant.
     */
    public static class PlantGhast{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 20.0F;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft mob that represents
         * this plant drop seeds?
         */
        @Name("Do Mobs Drop Seeds")
        @Comment("If set to true, ghasts will sometimes drop seeds when killed.")
        public boolean mobDropSeeds = true;

        /**
         * Does the plants produce drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, ghast bulbs will occasionally turn into seeds when thrown.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft mob.
         */
        @Name("Seed Drop Chance From Mob")
        @Comment("The chance a ghast will drop seeds when killed. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float mobSeedDropChance = 10.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (bulb) from this plant type will drop seeds when thrown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 10.0F;

        /**
         * The number of ghast tear items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of ghast tear items ghast bulbs will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;

        /**
         * The chance this plants seeds will drop from a mystical seed pod.
         */
        @Name("Mystical Seed Pod Drop Chance")
        @Comment("The chance this plant types seeds will be chosen to drop when a" +
                " mystical seed pod is broken.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float seedPodDropChance = 10F;
    }

    /**
     * Settings for the netherstar plant.
     */
    @Name("Plant: Nether Star")
    @Comment("Settings for the nether star plant.")
    public static final PlantNetherstar PLANT_NETHERSTAR = new PlantNetherstar();

    /**
     * Settings for the netherstar plant.
     */
    public static class PlantNetherstar{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 5.0F;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft mob that represents
         * this plant drop seeds?
         */
        @Name("Do Mobs Drop Seeds")
        @Comment("If set to true, withers will sometimes drop seeds when killed.")
        public boolean mobDropSeeds = true;

        /**
         * Does the plants produce drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, netherstar bulbs will occasionally turn into seeds when thrown.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft mob.
         */
        @Name("Seed Drop Chance From Mob")
        @Comment("The chance a wither will drop seeds when killed. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float mobSeedDropChance = 10.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (bulb) from this plant type will drop seeds when thrown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 2.0F;

        /**
         * The number of nether star items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of nether star items nether star bulbs will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;

        /**
         * The chance this plants seeds will drop from a mystical seed pod.
         */
        @Name("Mystical Seed Pod Drop Chance")
        @Comment("The chance this plant types seeds will be chosen to drop when a" +
                " mystical seed pod is broken.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float seedPodDropChance = 5F;
    }

    /**
     * Settings for the spider eye plant.
     */
    @Name("Plant: Spider Eye")
    @Comment("Settings for the spider eye plant.")
    public static final PlantSpidereye PLANT_SPIDEREYE = new PlantSpidereye();

    /**
     * Settings for the spidereye plant.
     */
    public static class PlantSpidereye{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 20.0F;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft mob that represents
         * this plant drop seeds?
         */
        @Name("Do Mobs Drop Seeds")
        @Comment("If set to true, spider will sometimes drop spider eye seeds when killed.")
        public boolean mobDropSeeds = true;

        /**
         * Does the plants produce drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, spider eye bulbs will occasionally turn into seeds when thrown.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft mob.
         */
        @Name("Seed Drop Chance From Mob")
        @Comment("The chance a spider will drop spider eye seeds when killed. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float mobSeedDropChance = 6.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (bulb) from this plant type will drop seeds when thrown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 15.0F;

        /**
         * The number of spider eye items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of spider eye items spider eye bulbs will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;

        /**
         * The chance this plants seeds will drop from a mystical seed pod.
         */
        @Name("Mystical Seed Pod Drop Chance")
        @Comment("The chance this plant types seeds will be chosen to drop when a" +
                " mystical seed pod is broken.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float seedPodDropChance = 25F;
    }

    /**
     * Settings for the slime plant.
     */
    @Name("Plant: Slime")
    @Comment("Settings for the slime plant.")
    public static final PlantSlime PLANT_SLIME = new PlantSlime();

    /**
     * Settings for the slime plant.
     */
    public static class PlantSlime{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 35.0F;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft mob that represents
         * this plant drop seeds?
         */
        @Name("Do Mobs Drop Seeds")
        @Comment("If set to true, slimes will sometimes drop seeds when killed.")
        public boolean mobDropSeeds = true;

        /**
         * Does the plants produce drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, slime bulbs will occasionally turn into seeds when thrown.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft mob.
         */
        @Name("Seed Drop Chance From Mob")
        @Comment("The chance a slime will drop slime seeds when killed. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float mobSeedDropChance = 3.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (bulb) from this plant type will drop seeds when thrown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 9.0F;

        /**
         * The number of slime ball items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of slime ball items slime bulbs will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 3;

        /**
         * The chance this plants seeds will drop from a mystical seed pod.
         */
        @Name("Mystical Seed Pod Drop Chance")
        @Comment("The chance this plant types seeds will be chosen to drop when a" +
                " mystical seed pod is broken.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float seedPodDropChance = 20F;
    }

    /**
     * Settings for the shulker plant.
     */
    @Name("Plant: Shulker")
    @Comment("Settings for the shulker plant.")
    public static final PlantShulker PLANT_SHULKER = new PlantShulker();

    /**
     * Settings for the shulker plant.
     */
    public static class PlantShulker{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 15.0F;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft mob that represents
         * this plant drop seeds?
         */
        @Name("Do Mobs Drop Seeds")
        @Comment("If set to true, shulkers will sometimes drop seeds when killed.")
        public boolean mobDropSeeds = true;

        /**
         * Does the plants produce drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, shulker bulbs will occasionally turn into seeds when thrown.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft mob.
         */
        @Name("Seed Drop Chance From Mob")
        @Comment("The chance a shulker will drop seeds when killed. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float mobSeedDropChance = 5.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (bulb) from this plant type will drop seeds when thrown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 5.0F;

        /**
         * The number of shulker shell items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of shulker shell items shulker bulbs will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;

        /**
         * The chance this plants seeds will drop from a mystical seed pod.
         */
        @Name("Mystical Seed Pod Drop Chance")
        @Comment("The chance this plant types seeds will be chosen to drop when a" +
                " mystical seed pod is broken.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float seedPodDropChance = 10F;
    }

    /**
     * Settings for the ink plant.
     */
    @Name("Plant: Ink")
    @Comment("Settings for the ink plant.")
    public static final PlantInk PLANT_INK = new PlantInk();

    /**
     * Settings for the ink plant.
     */
    public static class PlantInk{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 25.0F;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft mob that represents
         * this plant drop seeds?
         */
        @Name("Do Mobs Drop Seeds")
        @Comment("If set to true, squids will sometimes drop seeds when killed.")
        public boolean mobDropSeeds = true;

        /**
         * Does the plants produce drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, ink bulbs will occasionally turn into seeds when thrown.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft mob.
         */
        @Name("Seed Drop Chance From Mob")
        @Comment("The chance a squid will drop seeds when killed. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float mobSeedDropChance = 10.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (bulb) from this plant type will drop seeds when thrown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 20.0F;

        /**
         * The number of ink sac items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of ink sac items ink bulbs will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;

        /**
         * The chance this plants seeds will drop from a mystical seed pod.
         */
        @Name("Mystical Seed Pod Drop Chance")
        @Comment("The chance this plant types seeds will be chosen to drop when a" +
                " mystical seed pod is broken.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float seedPodDropChance = 30F;
    }

    /**
     * Settings for the leather plant.
     */
    @Name("Plant: Leather")
    @Comment("Settings for the leather plant.")
    public static final PlantLeather PLANT_LEATHER = new PlantLeather();

    /**
     * Settings for the leather plant.
     */
    public static class PlantLeather{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 35.0F;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft mob that represents
         * this plant drop seeds?
         */
        @Name("Do Mobs Drop Seeds")
        @Comment("If set to true, cows will sometimes drop seeds when killed.")
        public boolean mobDropSeeds = true;

        /**
         * Does the plants produce drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, leather bulbs will occasionally turn into seeds when thrown.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft mob.
         */
        @Name("Seed Drop Chance From Mob")
        @Comment("The chance a cow will drop seeds when killed. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float mobSeedDropChance = 5.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (bulb) from this plant type will drop seeds when thrown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 8.0F;

        /**
         * The number of leather items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of leather items leather bulbs will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;

        /**
         * The chance this plants seeds will drop from a mystical seed pod.
         */
        @Name("Mystical Seed Pod Drop Chance")
        @Comment("The chance this plant types seeds will be chosen to drop when a" +
                " mystical seed pod is broken.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float seedPodDropChance = 25F;
    }

    /**
     * Settings for the flesh plant.
     */
    @Name("Plant: Flesh")
    @Comment("Settings for the flesh plant.")
    public static final PlantFlesh PLANT_FLESH = new PlantFlesh();

    /**
     * Settings for the flesh plant.
     */
    public static class PlantFlesh{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 35.0F;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft mob that represents
         * this plant drop seeds?
         */
        @Name("Do Mobs Drop Seeds")
        @Comment("If set to true, zombies will sometimes drop seeds when killed.")
        public boolean mobDropSeeds = true;

        /**
         * Does the plants produce drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, flesh bulbs will occasionally turn into seeds when thrown.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft mob.
         */
        @Name("Seed Drop Chance From Mob")
        @Comment("The chance a zombie will drop seeds when killed. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float mobSeedDropChance = 25.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (bulb) from this plant type will drop seeds when thrown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 30.0F;

        /**
         * The number of rotten flesh items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of rotten flesh items flesh bulbs will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;

        /**
         * The chance this plants seeds will drop from a mystical seed pod.
         */
        @Name("Mystical Seed Pod Drop Chance")
        @Comment("The chance this plant types seeds will be chosen to drop when a" +
                " mystical seed pod is broken.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float seedPodDropChance = 40F;
    }

    /**
     * Settings for the prismarine crystal plant.
     */
    @Name("Plant: Prismarine Crystal")
    @Comment("Settings for the Prismarine Crystal plant.")
    public static final PlantPrismarineCrystal PLANT_PRISMARINE_CRYSTAL = new PlantPrismarineCrystal();

    /**
     * Settings for the prismarine crystal plant.
     */
    public static class PlantPrismarineCrystal{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 10.0F;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft mob that represents
         * this plant drop seeds?
         */
        @Name("Do Mobs Drop Seeds")
        @Comment("If set to true, guardians will sometimes drop prismarine crystal seeds when killed.")
        public boolean mobDropSeeds = true;

        /**
         * Does the plants produce drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, prismarine crystal bulbs will occasionally turn into seeds when thrown.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft mob.
         */
        @Name("Seed Drop Chance From Mob")
        @Comment("The chance a guardian will drop prismarine crystal seeds when killed." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float mobSeedDropChance = 7.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (bulb) from this plant type will drop seeds when thrown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 14.0F;

        /**
         * The number of prismarine crystal items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of prismarine crystal items prismarine crystal bulbs will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 2;

        /**
         * The chance this plants seeds will drop from a mystical seed pod.
         */
        @Name("Mystical Seed Pod Drop Chance")
        @Comment("The chance this plant types seeds will be chosen to drop when a" +
                " mystical seed pod is broken.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float seedPodDropChance = 20F;
    }

    /**
     * Settings for the prismarine shard plant.
     */
    @Name("Plant: Prismarine Shard")
    @Comment("Settings for the Prismarine Shard plant.")
    public static final PlantPrismarineShard PLANT_PRISMARINE_SHARD = new PlantPrismarineShard();

    /**
     * Settings for the prismarine shard plant.
     */
    public static class PlantPrismarineShard{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 10.0F;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft mob that represents
         * this plant drop seeds?
         */
        @Name("Do Mobs Drop Seeds")
        @Comment("If set to true, guardians will sometimes drop prismarine shard seeds when killed.")
        public boolean mobDropSeeds = true;

        /**
         * Does the plants produce drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, prismarine shard bulbs will occasionally turn into seeds when thrown.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft mob.
         */
        @Name("Seed Drop Chance From Mob")
        @Comment("The chance a guardian will drop prismarine shard seeds when killed." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float mobSeedDropChance = 7.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (bulb) from this plant type will drop seeds when thrown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 14.0F;

        /**
         * The number of prismarine shard items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of prismarine shard items prismarine shard bulbs will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 2;

        /**
         * The chance this plants seeds will drop from a mystical seed pod.
         */
        @Name("Mystical Seed Pod Drop Chance")
        @Comment("The chance this plant types seeds will be chosen to drop when a" +
                " mystical seed pod is broken.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float seedPodDropChance = 20F;
    }

    /**
     * Settings for the rabbit foot plant.
     */
    @Name("Plant: Rabbit Foot")
    @Comment("Settings for the Rabbit Foot plant.")
    public static final PlantRabbitFoot PLANT_RABBIT_FOOT = new PlantRabbitFoot();

    /**
     * Settings for the rabbit foot plant.
     */
    public static class PlantRabbitFoot{

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod = 25.0F;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal = false;

        /**
         * Does the minecraft mob that represents
         * this plant drop seeds?
         */
        @Name("Do Mobs Drop Seeds")
        @Comment("If set to true, rabbits will sometimes drop seeds when killed.")
        public boolean mobDropSeeds = true;

        /**
         * Does the plants produce drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, rabbit foot bulbs will occasionally turn into seeds when thrown.")
        public boolean produceDropSeeds = true;

        /**
         * The chance of seeds dropping
         * from this plants minecraft mob.
         */
        @Name("Seed Drop Chance From Mob")
        @Comment("The chance a rabbit will drop seeds when killed." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float mobSeedDropChance = 10.0F;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (bulb) from this plant type will drop seeds when thrown." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance = 5.0F;

        /**
         * The number of rabbit foot items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of rabbit foot items rabbit foot bulbs will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield = 1;

        /**
         * The chance this plants seeds will drop from a mystical seed pod.
         */
        @Name("Mystical Seed Pod Drop Chance")
        @Comment("The chance this plant types seeds will be chosen to drop when a" +
                " mystical seed pod is broken.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float seedPodDropChance = 15F;
    }

    /*
        OTHER MOD PLANTS CONFIG.
     */

    /*
     * Handles loading config classes for modded crystalline plants.
     */
    static {
        if(Loader.isModLoaded(ResynthMod.MODID_AE2))
            PLANT_CERTUS_QUARTZ = new ModPlantCrystallineCfg(
                    15.0F,
                    false,
                    true,
                    true,
                    05.0F,
                    02.5F,
                    1
            );
        else PLANT_CERTUS_QUARTZ = null;

        if(Loader.isModLoaded(ResynthMod.MODID_FORESTRY))
            PLANT_APATITE = new ModPlantCrystallineCfg(
                    30.0F,
                    false,
                    true,
                    true,
                    10.0F,
                    06.0F,
                    2
            );
        else PLANT_APATITE = null;
    }

    /**
     * Settings for the certus quartz crystal plant.
     */
    @Name("Plant: Certus Quartz Crystal (Applied Energistics 2)")
    @Comment("Settings for the certus quartz crystal plant from Applied Energistics 2.")
    public static final ModPlantCrystallineCfg PLANT_CERTUS_QUARTZ;

    /**
     * Settings for the apatite plant.
     */
    @Name("Plant: Apatite (Forestry)")
    @Comment("Settings for the apatite plant from Forestry.")
    public static final ModPlantCrystallineCfg PLANT_APATITE;

    /**
     * Configuration class for all mod crystalline plants.
     */
    public static class ModPlantCrystallineCfg {

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal;

        /**
         * Does the modded ore block this plant
         * represents drop seeds.
         */
        @Name("Does Ore Drop Seeds")
        @Comment("If set to true, the ore block for this plant will occasionally drop seeds when mined.")
        public boolean oreDropSeeds;

        /**
         * Does the ore block this plant
         * produces drop seeds.
         */
        @Name("Does Produce Drop Seeds")
        @Comment("If set to true, this plants produce will occasionally turn into seeds when left in water.")
        public boolean produceDropSeeds;

        /**
         * The chance of seeds dropping
         * from this plants modded ore block.
         */
        @Name("Seed Drop Chance From Ore")
        @Comment("The chance this plants ore block will drop seeds when mined." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float oreSeedDropChance;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Produce")
        @Comment("The chance the produce (straw) from this plant type will drop seeds." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance;

        /**
         * The number of resource items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of resources the plants produce (straw) will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield;

        private ModPlantCrystallineCfg (
                float floweringPeriod, boolean canBonemeal,
                boolean doesOreDropSeeds, boolean doesProduceDropSeeds,
                float oreSeedDropChance, float produceSeedDropChance, int yield
        ){
            this.floweringPeriod = floweringPeriod; this.canBonemeal = canBonemeal;
            this.oreDropSeeds = doesOreDropSeeds; this.produceDropSeeds = doesProduceDropSeeds;
            this.oreSeedDropChance = oreSeedDropChance; this.produceSeedDropChance = produceSeedDropChance;
            this.yield = yield;
        }
    }

    /*
     * Handles loading config classes for modded metallic plants.
     */
    static{
        if(Loader.isModLoaded(ResynthMod.MODID_TINKERS_CONSTRUCT))
            PLANT_COBALT = new ModPlantMetallicCfg(
                    10.0F,
                    false,
                    true,
                    true,
                    5.0F,
                    9.0F,
                    1
            );
        else PLANT_COBALT = null;

        if(Loader.isModLoaded(ResynthMod.MODID_TINKERS_CONSTRUCT))
            PLANT_ARDITE = new ModPlantMetallicCfg(
                    10.0F,
                    false,
                    true,
                    true,
                    5.0F,
                    9.0F,
                    1
            );
        else PLANT_ARDITE = null;
    }

    /**
     * Settings for the cobalt plant.
     */
    @Name("Plant: Cobalt (Tinkers' Construct)")
    @Comment("Settings for the Cobalt plant from Tinkers' Construct.")
    public static final ModPlantMetallicCfg PLANT_COBALT;

    /**
     * Settings for the cobalt plant.
     */
    @Name("Plant: Ardite (Tinkers' Construct)")
    @Comment("Settings for the Ardite plant from Tinkers' Construct.")
    public static final ModPlantMetallicCfg PLANT_ARDITE;

    /**
     * Configuration class for all mod metallic plants.
     */
    public static class ModPlantMetallicCfg {

        /**
         * How long it takes this plant type
         * to grow.
         */
        @Name("Chance To Grow")
        @Comment("The chance the plant species will grow when a random tick occurs. This" +
                " is calculated after the mineral soil growth chance. This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float floweringPeriod;

        /**
         * Can bonemeal be used on this plant type.
         */
        @Name("Enable Bonemeal")
        @Comment("Set to true to allow bonemeal to be used on this species of plant. " +
                "WARNING: Breaks game mechanics when set to true")
        public boolean canBonemeal;

        /**
         * Does the modded ore block this plant
         * represents drop seeds.
         */
        @Name("Does Ore Drop Seeds")
        @Comment("If set to true, the ore block for this plant will occasionally drop seeds when mined.")
        public boolean oreDropSeeds;

        /**
         * Does the ore block this plant
         * produces drop seeds.
         */
        @Name("Does Organic Ore Drop Seeds")
        @Comment("If set to true, this plants produce (organic ore) will occasionally" +
                "turn into seeds when blown up by TNT")
        public boolean produceDropSeeds;

        /**
         * The chance of seeds dropping
         * from this plants modded ore block.
         */
        @Name("Seed Drop Chance From Ore")
        @Comment("The chance this plants ore block will drop seeds when mined." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float oreSeedDropChance;

        /**
         * The chance of seeds dropping from
         * this plant types produce.
         */
        @Name("Seed Drop Chance From Organic Ore")
        @Comment("The chance the produce (organic ore) from this plant type will drop seeds." +
                " This chance is percentage based.")
        @RangeDouble(min = 0.0F, max = 100.0F)
        public float produceSeedDropChance;

        /**
         * The number of resource items the plants produce
         * item gives.
         */
        @Name("Yield")
        @Comment("The amount of resources the plants produce (organic ore) will give when smelted.")
        @RangeInt(min = 1, max = 64)
        @RequiresMcRestart
        public int yield;

        private ModPlantMetallicCfg (
                float floweringPeriod, boolean canBonemeal,
                boolean doesOreDropSeeds, boolean doesProduceDropSeeds,
                float oreSeedDropChance, float produceSeedDropChance, int yield
        ){
            this.floweringPeriod = floweringPeriod; this.canBonemeal = canBonemeal;
            this.oreDropSeeds = doesOreDropSeeds; this.produceDropSeeds = doesProduceDropSeeds;
            this.oreSeedDropChance = oreSeedDropChance; this.produceSeedDropChance = produceSeedDropChance;
            this.yield = yield;
        }
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
