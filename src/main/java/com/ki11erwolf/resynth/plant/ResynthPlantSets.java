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
package com.ki11erwolf.resynth.plant;

import com.ki11erwolf.resynth.ResynthConfig;
import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.item.ResynthItems;
import com.ki11erwolf.resynth.plant.block.BlockPlantBase;
import com.ki11erwolf.resynth.plant.block.BlockOrganicPlantOre;
import com.ki11erwolf.resynth.plant.item.ItemPlantProduceBulb;
import com.ki11erwolf.resynth.plant.item.ItemPlantProduceShard;
import com.ki11erwolf.resynth.plant.item.ItemPlantSeeds;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Holds all the definitions and {@code public static}
 * instances of every available Resynth plant
 * set as well as helper methods to get arrays
 * of each plant set and registered block/item.
 */
@SuppressWarnings("unused")//Plant Sets register themselves when declared.
public final class ResynthPlantSets {

    /**
     * The iron plant set instance.
     */
    public static final PlantSetMetallic PLANT_SET_IRON
            = new PlantSetMetallic("iron", new ItemStack(Blocks.IRON_ORE)){
        @Override
        protected float getPlantGrowthChance() {return ResynthConfig.PLANT_IRON.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return ResynthConfig.PLANT_IRON.canBonemeal;}

        @Override
        protected float getSourceOreSeedDropChance() {return ResynthConfig.PLANT_IRON.oreSeedDropChance;}

        @Override
        protected float getProduceSeedDropChance() {return ResynthConfig.PLANT_IRON.organicOreSeedDropChance;}

        @Override
        protected boolean doesSourceOreDropSeeds() {return ResynthConfig.PLANT_IRON.oreDropSeeds;}

        @Override
        protected boolean doesPlantOreDropSeeds() {return ResynthConfig.PLANT_IRON.organicOreDropSeeds;}

        @Override
        public ItemStack getResult() {return new ItemStack(Blocks.IRON_ORE, ResynthConfig.PLANT_IRON.yield);}
    }.register();

    /**
     * The gold plant set instance.
     */
    public static final PlantSetMetallic PLANT_SET_GOLD
            = new PlantSetMetallic("gold", new ItemStack(Blocks.GOLD_ORE)) {
        @Override
        protected float getPlantGrowthChance() {return ResynthConfig.PLANT_GOLD.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return ResynthConfig.PLANT_GOLD.canBonemeal;}

        @Override
        protected float getSourceOreSeedDropChance() {return ResynthConfig.PLANT_GOLD.oreSeedDropChance;}

        @Override
        protected float getProduceSeedDropChance() {return ResynthConfig.PLANT_GOLD.organicOreSeedDropChance;}

        @Override
        protected boolean doesSourceOreDropSeeds() {return ResynthConfig.PLANT_GOLD.oreDropSeeds;}

        @Override
        protected boolean doesPlantOreDropSeeds() {return ResynthConfig.PLANT_GOLD.organicOreDropSeeds;}

        @Override
        public ItemStack getResult() {return new ItemStack(Blocks.GOLD_ORE, ResynthConfig.PLANT_GOLD.yield);}
    }.register();

    /**
     * The clay plant set instance.
     */
    public static final PlantSetMetallic PLANT_SET_CLAY
            = new PlantSetMetallic("clay", new ItemStack(Blocks.CLAY)) {
        @Override
        protected float getPlantGrowthChance() {return ResynthConfig.PLANT_CLAY.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return ResynthConfig.PLANT_CLAY.canBonemeal;}

        @Override
        protected float getSourceOreSeedDropChance() {return ResynthConfig.PLANT_CLAY.oreSeedDropChance;}

        @Override
        protected float getProduceSeedDropChance() {return ResynthConfig.PLANT_CLAY.organicOreSeedDropChance;}

        @Override
        protected boolean doesSourceOreDropSeeds() {return ResynthConfig.PLANT_CLAY.oreDropSeeds;}

        @Override
        protected boolean doesPlantOreDropSeeds() {return ResynthConfig.PLANT_CLAY.organicOreDropSeeds;}

        @Override
        public ItemStack getResult() {return new ItemStack(Blocks.CLAY, ResynthConfig.PLANT_CLAY.yield);}
    }.register();

    /**
     * The end stone plant set instance.
     */
    public static final PlantSetMetallic PLANT_SET_END
            = new PlantSetMetallic("end", new ItemStack(Blocks.END_STONE)) {
        @Override
        protected float getPlantGrowthChance() {return ResynthConfig.PLANT_END.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return ResynthConfig.PLANT_END.canBonemeal;}

        @Override
        protected float getSourceOreSeedDropChance() {return ResynthConfig.PLANT_END.oreSeedDropChance;}

        @Override
        protected float getProduceSeedDropChance() {return ResynthConfig.PLANT_END.organicOreSeedDropChance;}

        @Override
        protected boolean doesSourceOreDropSeeds() {return ResynthConfig.PLANT_END.oreDropSeeds;}

        @Override
        protected boolean doesPlantOreDropSeeds() {return ResynthConfig.PLANT_END.organicOreDropSeeds;}

        @Override
        public ItemStack getResult() {return new ItemStack(Blocks.END_STONE, ResynthConfig.PLANT_END.yield);}
    }.register();

    /**
     * The sand plant set instance.
     */
    public static final PlantSetMetallic PLANT_SET_SAND
            = new PlantSetMetallic("sand", new ItemStack(Blocks.SAND)) {
        @Override
        protected float getPlantGrowthChance() {return ResynthConfig.PLANT_SAND.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return ResynthConfig.PLANT_SAND.canBonemeal;}

        @Override
        protected float getSourceOreSeedDropChance() {return ResynthConfig.PLANT_SAND.oreSeedDropChance;}

        @Override
        protected float getProduceSeedDropChance() {return ResynthConfig.PLANT_SAND.organicOreSeedDropChance;}

        @Override
        protected boolean doesSourceOreDropSeeds() {return ResynthConfig.PLANT_SAND.oreDropSeeds;}

        @Override
        protected boolean doesPlantOreDropSeeds() {return ResynthConfig.PLANT_SAND.organicOreDropSeeds;}

        @Override
        public ItemStack getResult() {return new ItemStack(Blocks.SAND, ResynthConfig.PLANT_SAND.yield);}
    }.register();

    /*
        CRYSTALLINE
     */

    /**
     * The mineral plant set instance.
     */
    public static final PlantSetCrystalline PLANT_SET_MINERAL
            = new PlantSetCrystalline("mineral", ResynthBlocks.BLOCK_MINERAL_ORE) {
        private final ResynthConfig.PlantMineral cfg = ResynthConfig.PLANT_MINERAL;

        @Override
        protected boolean doesOreDropSeeds() { return cfg.oreDropSeeds; }

        @Override
        protected float getOreSeedDropChance() { return cfg.oreSeedDropChance; }

        @Override
        protected float getPlantGrowthChance() { return cfg.floweringPeriod; }

        @Override
        protected boolean canBonemealPlant() { return cfg.canBonemeal; }

        @Override
        protected float getProduceSeedDropChance() { return cfg.produceSeedDropChance; }

        @Override
        protected boolean doesProduceDropSeeds() { return cfg.produceDropSeeds; }

        @Override
        public ItemStack getResult() { return new ItemStack(ResynthItems.ITEM_MINERAL_ROCK, cfg.yield); }
    }.register();

    /**
     * The diamond plant set instance.
     */
    public static final PlantSetCrystalline PLANT_SET_DIAMOND
            = new PlantSetCrystalline("diamond", Blocks.DIAMOND_ORE) {
        private final ResynthConfig.PlantDiamond cfg = ResynthConfig.PLANT_DIAMOND;

        @Override
        protected boolean doesOreDropSeeds() { return cfg.oreDropSeeds; }

        @Override
        protected float getOreSeedDropChance() { return cfg.oreSeedDropChance; }

        @Override
        protected float getPlantGrowthChance() { return cfg.floweringPeriod; }

        @Override
        protected boolean canBonemealPlant() { return cfg.canBonemeal; }

        @Override
        protected float getProduceSeedDropChance() { return cfg.produceSeedDropChance; }

        @Override
        protected boolean doesProduceDropSeeds() { return cfg.produceDropSeeds; }

        @Override
        public ItemStack getResult() { return new ItemStack(Items.DIAMOND, cfg.yield); }
    }.register();

    /**
     * The redstone plant set instance.
     */
    public static final PlantSetCrystalline PLANT_SET_REDSTONE
            = new PlantSetCrystalline("redstone", Blocks.REDSTONE_ORE) {
        private final ResynthConfig.PlantRedstone cfg = ResynthConfig.PLANT_REDSTONE;

        @Override
        protected boolean doesOreDropSeeds() { return cfg.oreDropSeeds; }

        @Override
        protected float getOreSeedDropChance() { return cfg.oreSeedDropChance; }

        @Override
        protected float getPlantGrowthChance() { return cfg.floweringPeriod; }

        @Override
        protected boolean canBonemealPlant() { return cfg.canBonemeal; }

        @Override
        protected float getProduceSeedDropChance() { return cfg.produceSeedDropChance; }

        @Override
        protected boolean doesProduceDropSeeds() { return cfg.produceDropSeeds; }

        @Override
        public ItemStack getResult() { return new ItemStack(Items.REDSTONE, cfg.yield); }
    }.register();

    /**
     * The lapis plant set instance.
     */
    public static final PlantSetCrystalline PLANT_SET_LAPIS
            = new PlantSetCrystalline("lapis", Blocks.LAPIS_ORE) {
        private final ResynthConfig.PlantLapis cfg = ResynthConfig.PLANT_LAPIS;

        @Override
        protected boolean doesOreDropSeeds() { return cfg.oreDropSeeds; }

        @Override
        protected float getOreSeedDropChance() { return cfg.oreSeedDropChance; }

        @Override
        protected float getPlantGrowthChance() { return cfg.floweringPeriod; }

        @Override
        protected boolean canBonemealPlant() { return cfg.canBonemeal; }

        @Override
        protected float getProduceSeedDropChance() { return cfg.produceSeedDropChance; }

        @Override
        protected boolean doesProduceDropSeeds() { return cfg.produceDropSeeds; }

        @Override
        public ItemStack getResult() { return new ItemStack(Items.DYE, cfg.yield, 4); }
    }.register();

    /**
     * The coal plant set instance.
     */
    public static final PlantSetCrystalline PLANT_SET_COAL
            = new PlantSetCrystalline("coal", Blocks.COAL_ORE) {
                private final ResynthConfig.PlantCoal cfg = ResynthConfig.PLANT_COAL;

        @Override
        protected boolean doesOreDropSeeds() { return cfg.oreDropSeeds; }

        @Override
        protected float getOreSeedDropChance() { return cfg.oreSeedDropChance; }

        @Override
        protected float getPlantGrowthChance() { return cfg.floweringPeriod; }

        @Override
        protected boolean canBonemealPlant() { return cfg.canBonemeal; }

        @Override
        protected float getProduceSeedDropChance() { return cfg.produceSeedDropChance; }

        @Override
        protected boolean doesProduceDropSeeds() { return cfg.produceDropSeeds; }

        @Override
        public ItemStack getResult() { return new ItemStack(Items.COAL, cfg.yield); }
    }.register();

    /**
     * The emerald plant set instance.
     */
    public static final PlantSetCrystalline PLANT_SET_EMERALD
            = new PlantSetCrystalline("emerald", Blocks.EMERALD_ORE) {
        private final ResynthConfig.PlantEmerald cfg = ResynthConfig.PLANT_EMERALD;

        @Override
        protected boolean doesOreDropSeeds() { return cfg.oreDropSeeds; }

        @Override
        protected float getOreSeedDropChance() { return cfg.oreSeedDropChance; }

        @Override
        protected float getPlantGrowthChance() { return cfg.floweringPeriod; }

        @Override
        protected boolean canBonemealPlant() { return cfg.canBonemeal; }

        @Override
        protected float getProduceSeedDropChance() { return cfg.produceSeedDropChance; }

        @Override
        protected boolean doesProduceDropSeeds() { return cfg.produceDropSeeds; }

        @Override
        public ItemStack getResult() { return new ItemStack(Items.EMERALD, cfg.yield); }
    }.register();

    /**
     * The quartz plant set instance.
     */
    public static final PlantSetCrystalline PLANT_SET_QUARTZ
            = new PlantSetCrystalline("quartz", Blocks.QUARTZ_ORE) {
        private final ResynthConfig.PlantQuartz cfg = ResynthConfig.PLANT_QUARTZ;

        @Override
        protected boolean doesOreDropSeeds() { return cfg.oreDropSeeds; }

        @Override
        protected float getOreSeedDropChance() { return cfg.oreSeedDropChance; }

        @Override
        protected float getPlantGrowthChance() { return cfg.floweringPeriod; }

        @Override
        protected boolean canBonemealPlant() { return cfg.canBonemeal; }

        @Override
        protected float getProduceSeedDropChance() { return cfg.produceSeedDropChance; }

        @Override
        protected boolean doesProduceDropSeeds() { return cfg.produceDropSeeds; }

        @Override
        public ItemStack getResult() { return new ItemStack(Items.QUARTZ, cfg.yield); }
    }.register();

    /**
     * The glowstone plant set instance.
     */
    public static final PlantSetCrystalline PLANT_SET_GLOWSTONE
            = new PlantSetCrystalline("glowstone", Blocks.GLOWSTONE) {
        private final ResynthConfig.PlantGlowstone cfg = ResynthConfig.PLANT_GLOWSTONE;

        @Override
        protected boolean doesOreDropSeeds() { return cfg.oreDropSeeds; }

        @Override
        protected float getOreSeedDropChance() { return cfg.oreSeedDropChance; }

        @Override
        protected float getPlantGrowthChance() { return cfg.floweringPeriod; }

        @Override
        protected boolean canBonemealPlant() { return cfg.canBonemeal; }

        @Override
        protected float getProduceSeedDropChance() { return cfg.produceSeedDropChance; }

        @Override
        protected boolean doesProduceDropSeeds() { return cfg.produceDropSeeds; }

        @Override
        public ItemStack getResult() { return new ItemStack(Items.GLOWSTONE_DUST, cfg.yield); }
    }.register();

    /*
        BIOCHEMICAL
     */

    /**
     * The ender pearl plant set instance.
     */
    public static final PlantSetBiochemical PLANT_SET_ENDER_PEARL
            = new PlantSetBiochemical("enderpearl", EntityEnderman.class){
        ResynthConfig.PlantEnderpearl cfg = ResynthConfig.PLANT_ENDERPEARL;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.ENDER_PEARL, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /**
     * The gunpowder plant set instance.
     */
    public static final PlantSetBiochemical PLANT_SET_GUNPOWDER
            = new PlantSetBiochemical("gunpowder", EntityCreeper.class){
        ResynthConfig.PlantGunpowder cfg = ResynthConfig.PLANT_GUNPOWDER;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.GUNPOWDER, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /**
     * The gunpowder plant set instance.
     */
    public static final PlantSetBiochemical PLANT_SET_BLAZE
            = new PlantSetBiochemical("blaze", EntityBlaze.class){
        ResynthConfig.PlantBlaze cfg = ResynthConfig.PLANT_BLAZE;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.BLAZE_ROD, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /**
     * The bone plant set instance.
     */
    public static final PlantSetBiochemical PLANT_SET_BONE
            = new PlantSetBiochemical("bone", EntitySkeleton.class){
        ResynthConfig.PlantBone cfg = ResynthConfig.PLANT_BONE;
        @Override
        public ItemStack getResult() {return new ItemStack(Items.BONE, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /**
     * The string plant set instance.
     */
    public static final PlantSetBiochemical PLANT_SET_STRING
            = new PlantSetBiochemical("string", EntitySpider.class){
        ResynthConfig.PlantString cfg = ResynthConfig.PLANT_STRING;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.STRING, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /**
     * The feather plant set instance.
     */
    public static final PlantSetBiochemical PLANT_SET_FEATHER
            = new PlantSetBiochemical("feather", EntityChicken.class){
        ResynthConfig.PlantFeather cfg = ResynthConfig.PLANT_FEATHER;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.FEATHER, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /**
     * The ghast plant set instance.
     */
    public static final PlantSetBiochemical PLANT_SET_GHAST
            = new PlantSetBiochemical("ghast", EntityGhast.class){
        ResynthConfig.PlantGhast cfg = ResynthConfig.PLANT_GHAST;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.GHAST_TEAR, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /**
     * The nether star plant set instance.
     */
    public static final PlantSetBiochemical PLANT_SET_NETHERSTAR
            = new PlantSetBiochemical("netherstar", EntityWither.class){
        ResynthConfig.PlantNetherstar cfg = ResynthConfig.PLANT_NETHERSTAR;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.NETHER_STAR, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /**
     * The spider eye plant set instance.
     */
    public static final PlantSetBiochemical PLANT_SET_SPIDEREYE
            = new PlantSetBiochemical("spidereye", EntitySpider.class){
        ResynthConfig.PlantSpidereye cfg = ResynthConfig.PLANT_SPIDEREYE;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.SPIDER_EYE, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /**
     * The slime plant set instance.
     */
    public static final PlantSetBiochemical PLANT_SET_SLIME
            = new PlantSetBiochemical("slime", EntitySlime.class){
        ResynthConfig.PlantSlime cfg = ResynthConfig.PLANT_SLIME;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.SLIME_BALL, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /**
     * The shulker plant set instance.
     */
    public static final PlantSetBiochemical PLANT_SET_SHULKER
            = new PlantSetBiochemical("shulker", EntityShulker.class){
        ResynthConfig.PlantShulker cfg = ResynthConfig.PLANT_SHULKER;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.SHULKER_SHELL, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /**
     * The ink plant set instance.
     */
    public static final PlantSetBiochemical PLANT_SET_INK
            = new PlantSetBiochemical("ink", EntitySquid.class){
        ResynthConfig.PlantInk cfg = ResynthConfig.PLANT_INK;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.DYE, cfg.yield, 0);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /**
     * The leather plant set instance.
     */
    public static final PlantSetBiochemical PLANT_SET_LEATHER
            = new PlantSetBiochemical("leather", EntityCow.class){
        ResynthConfig.PlantLeather cfg = ResynthConfig.PLANT_LEATHER;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.LEATHER, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /**
     * The flesh plant set instance.
     */
    public static final PlantSetBiochemical PLANT_SET_FLESH
            = new PlantSetBiochemical("flesh", EntityZombie.class){
        ResynthConfig.PlantFlesh cfg = ResynthConfig.PLANT_FLESH;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.ROTTEN_FLESH, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /**
     * The prismarine crystal plant set instance.
     */
    public static final PlantSetBiochemical PLANT_SET_PRISMARINE_CRYSTAL
            = new PlantSetBiochemical("prismarineCrystal", EntityGuardian.class){
        ResynthConfig.PlantPrismarineCrystal cfg = ResynthConfig.PLANT_PRISMARINE_CRYSTAL;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.PRISMARINE_CRYSTALS, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /**
     * The prismarine shard plant set instance.
     */
    public static final PlantSetBiochemical PLANT_SET_PRISMARINE_SHARD
            = new PlantSetBiochemical("prismarineShard", EntityGuardian.class){
        ResynthConfig.PlantPrismarineShard cfg = ResynthConfig.PLANT_PRISMARINE_SHARD;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.PRISMARINE_SHARD, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /**
     * The rabbit foot plant set instance.
     */
    public static final PlantSetBiochemical PLANT_SET_RABBIT_FOOT
            = new PlantSetBiochemical("rabbitFoot", EntityRabbit.class){
        ResynthConfig.PlantRabbitFoot cfg = ResynthConfig.PLANT_RABBIT_FOOT;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.RABBIT_FOOT, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /**
     * Plant set for dragon's breath from the ender dragon.
     */
    public static final PlantSetBiochemical PLANT_SET_DRAGONS_BREATH
            = new PlantSetBiochemical("dragonsBreath", EntityDragon.class) {
        ResynthConfig.PlantDragonsBreath cfg = ResynthConfig.PLANT_DRAGONS_BREATH;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.DRAGON_BREATH, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealPlant() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}

        @Override
        public float getSeedPodDropPercentage() {return cfg.seedPodDropChance;}
    };

    /*
        External Mod Plants

        Plants for other supported mods.
     */

    /**
     * The certus quartz crystal plant set for Applied Energistics 2.
     */
    public static final ModPlantSetCrystalline MOD_PLANT_SET_AE2_QUARTZ
            = new ModPlantSetCrystalline("ae2Quartz", ResynthMod.MODID_AE2,
            "quartz_ore", "material", 0) {
        private final ResynthConfig.ModPlantCrystallineCfg cfg = ResynthConfig.PLANT_CERTUS_QUARTZ;

        @Override
        protected int getResultCount() {return cfg.yield;}

        @Override
        protected boolean doesModOreDropSeeds() {return cfg.oreDropSeeds;}

        @Override
        protected float getModOreSeedDropChance() {return cfg.oreSeedDropChance;}

        @Override
        protected float getModPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealModPlant() {return cfg.canBonemeal;}

        @Override
        protected float getModProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesModProduceDropSeeds() {return cfg.produceDropSeeds;}
    }.register();

    /**
     * The apatite plant set for Forestry.
     */
    public static final ModPlantSetCrystalline MOD_PLANT_SET_FORESTRY_APATITE
            = new ModPlantSetCrystalline("forestryApatite", ResynthMod.MODID_FORESTRY,
            "resources", "apatite", 0) {
        private final ResynthConfig.ModPlantCrystallineCfg cfg = ResynthConfig.PLANT_APATITE;

        @Override
        protected int getResultCount() {return cfg.yield;}

        @Override
        protected boolean doesModOreDropSeeds() {return cfg.oreDropSeeds;}

        @Override
        protected float getModOreSeedDropChance() {return cfg.oreSeedDropChance;}

        @Override
        protected float getModPlantGrowthChance() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBonemealModPlant() {return cfg.canBonemeal;}

        @Override
        protected float getModProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesModProduceDropSeeds() {return cfg.produceDropSeeds;}
    }.register();

    /**
     * Cobalt plant set for tinkers' construct.
     */
    public static final ModPlantSetMetallic MOD_PLANT_SET_TC_COBALT
            = new ModPlantSetMetallic("cobalt", ResynthMod.MODID_TINKERS_CONSTRUCT,
            "ore", 0, "ore", 0) {
        ResynthConfig.ModPlantMetallicCfg cfg = ResynthConfig.PLANT_COBALT;

        @Override
        protected int getResultCount() {
            return cfg.yield;
        }

        @Override
        protected boolean doesModOreDropSeeds() {
            return cfg.oreDropSeeds;
        }

        @Override
        protected float getModOreSeedDropChance() {
            return cfg.oreSeedDropChance;
        }

        @Override
        protected float getModPlantGrowthChance() {
            return cfg.floweringPeriod;
        }

        @Override
        protected boolean canBonemealModPlant() {
            return cfg.canBonemeal;
        }

        @Override
        protected float getModProduceSeedDropChance() {
            return cfg.produceSeedDropChance;
        }

        @Override
        protected boolean doesModProduceDropSeeds() {
            return cfg.produceDropSeeds;
        }
    }.register();

    /**
     * Ardite plant set for tinkers' construct.
     */
    public static final ModPlantSetMetallic MOD_PLANT_SET_TC_ARDITE
            = new ModPlantSetMetallic("ardite", ResynthMod.MODID_TINKERS_CONSTRUCT
            , "ore", 1, "ore", 1) {
        ResynthConfig.ModPlantMetallicCfg cfg = ResynthConfig.PLANT_ARDITE;

        @Override
        protected int getResultCount() {
            return cfg.yield;
        }

        @Override
        protected boolean doesModOreDropSeeds() {
            return cfg.oreDropSeeds;
        }

        @Override
        protected float getModOreSeedDropChance() {
            return cfg.oreSeedDropChance;
        }

        @Override
        protected float getModPlantGrowthChance() {
            return cfg.floweringPeriod;
        }

        @Override
        protected boolean canBonemealModPlant() {
            return cfg.canBonemeal;
        }

        @Override
        protected float getModProduceSeedDropChance() {
            return cfg.produceSeedDropChance;
        }

        @Override
        protected boolean doesModProduceDropSeeds() {
            return cfg.produceDropSeeds;
        }
    }.register();

    /**
     * Copper plant set for Thermal Foundation.
     */
    public static final ModPlantSetMetallic MOD_PLANT_SET_TF_COPPER
            = new ModPlantSetMetallic("copper", ResynthMod.MODID_THERMAL_FOUNDATION
            , "ore", 0, "ore", 0) {
        ResynthConfig.ModPlantMetallicCfg cfg = ResynthConfig.PLANT_COPPER;

        @Override
        protected int getResultCount() {
            return cfg.yield;
        }

        @Override
        protected boolean doesModOreDropSeeds() {
            return cfg.oreDropSeeds;
        }

        @Override
        protected float getModOreSeedDropChance() {
            return cfg.oreSeedDropChance;
        }

        @Override
        protected float getModPlantGrowthChance() {
            return cfg.floweringPeriod;
        }

        @Override
        protected boolean canBonemealModPlant() {
            return cfg.canBonemeal;
        }

        @Override
        protected float getModProduceSeedDropChance() {
            return cfg.produceSeedDropChance;
        }

        @Override
        protected boolean doesModProduceDropSeeds() {
            return cfg.produceDropSeeds;
        }
    }.register();

    /**
     * Tin plant set for Thermal Foundation.
     */
    public static final ModPlantSetMetallic MOD_PLANT_SET_TF_TIN
            = new ModPlantSetMetallic("tin", ResynthMod.MODID_THERMAL_FOUNDATION
            , "ore", 1, "ore", 1) {
        ResynthConfig.ModPlantMetallicCfg cfg = ResynthConfig.PLANT_TIN;

        @Override
        protected int getResultCount() {
            return cfg.yield;
        }

        @Override
        protected boolean doesModOreDropSeeds() {
            return cfg.oreDropSeeds;
        }

        @Override
        protected float getModOreSeedDropChance() {
            return cfg.oreSeedDropChance;
        }

        @Override
        protected float getModPlantGrowthChance() {
            return cfg.floweringPeriod;
        }

        @Override
        protected boolean canBonemealModPlant() {
            return cfg.canBonemeal;
        }

        @Override
        protected float getModProduceSeedDropChance() {
            return cfg.produceSeedDropChance;
        }

        @Override
        protected boolean doesModProduceDropSeeds() {
            return cfg.produceDropSeeds;
        }
    }.register();

    /**
     * Silver plant set for Thermal Foundation.
     */
    public static final ModPlantSetMetallic MOD_PLANT_SET_TF_SILVER
            = new ModPlantSetMetallic("silver", ResynthMod.MODID_THERMAL_FOUNDATION
            , "ore", 2, "ore", 2) {
        ResynthConfig.ModPlantMetallicCfg cfg = ResynthConfig.PLANT_SILVER;

        @Override
        protected int getResultCount() {
            return cfg.yield;
        }

        @Override
        protected boolean doesModOreDropSeeds() {
            return cfg.oreDropSeeds;
        }

        @Override
        protected float getModOreSeedDropChance() {
            return cfg.oreSeedDropChance;
        }

        @Override
        protected float getModPlantGrowthChance() {
            return cfg.floweringPeriod;
        }

        @Override
        protected boolean canBonemealModPlant() {
            return cfg.canBonemeal;
        }

        @Override
        protected float getModProduceSeedDropChance() {
            return cfg.produceSeedDropChance;
        }

        @Override
        protected boolean doesModProduceDropSeeds() {
            return cfg.produceDropSeeds;
        }
    }.register();

    /**
     * Lead plant set for Thermal Foundation.
     */
    public static final ModPlantSetMetallic MOD_PLANT_SET_TF_LEAD
            = new ModPlantSetMetallic("lead", ResynthMod.MODID_THERMAL_FOUNDATION
            , "ore", 3, "ore", 3) {
        ResynthConfig.ModPlantMetallicCfg cfg = ResynthConfig.PLANT_LEAD;

        @Override
        protected int getResultCount() {
            return cfg.yield;
        }

        @Override
        protected boolean doesModOreDropSeeds() {
            return cfg.oreDropSeeds;
        }

        @Override
        protected float getModOreSeedDropChance() {
            return cfg.oreSeedDropChance;
        }

        @Override
        protected float getModPlantGrowthChance() {
            return cfg.floweringPeriod;
        }

        @Override
        protected boolean canBonemealModPlant() {
            return cfg.canBonemeal;
        }

        @Override
        protected float getModProduceSeedDropChance() {
            return cfg.produceSeedDropChance;
        }

        @Override
        protected boolean doesModProduceDropSeeds() {
            return cfg.produceDropSeeds;
        }
    }.register();

    /**
     * Aluminum plant set for Thermal Foundation.
     */
    public static final ModPlantSetMetallic MOD_PLANT_SET_TF_ALUMINUM
            = new ModPlantSetMetallic("aluminum", ResynthMod.MODID_THERMAL_FOUNDATION
            , "ore", 4, "ore", 4) {
        ResynthConfig.ModPlantMetallicCfg cfg = ResynthConfig.PLANT_ALUMINUM;

        @Override
        protected int getResultCount() {
            return cfg.yield;
        }

        @Override
        protected boolean doesModOreDropSeeds() {
            return cfg.oreDropSeeds;
        }

        @Override
        protected float getModOreSeedDropChance() {
            return cfg.oreSeedDropChance;
        }

        @Override
        protected float getModPlantGrowthChance() {
            return cfg.floweringPeriod;
        }

        @Override
        protected boolean canBonemealModPlant() {
            return cfg.canBonemeal;
        }

        @Override
        protected float getModProduceSeedDropChance() {
            return cfg.produceSeedDropChance;
        }

        @Override
        protected boolean doesModProduceDropSeeds() {
            return cfg.produceDropSeeds;
        }
    }.register();

    /**
     * Nickel plant set for Thermal Foundation.
     */
    public static final ModPlantSetMetallic MOD_PLANT_SET_TF_NICKEL
            = new ModPlantSetMetallic("nickel", ResynthMod.MODID_THERMAL_FOUNDATION
            , "ore", 5, "ore", 5) {
        ResynthConfig.ModPlantMetallicCfg cfg = ResynthConfig.PLANT_NICKEL;

        @Override
        protected int getResultCount() {
            return cfg.yield;
        }

        @Override
        protected boolean doesModOreDropSeeds() {
            return cfg.oreDropSeeds;
        }

        @Override
        protected float getModOreSeedDropChance() {
            return cfg.oreSeedDropChance;
        }

        @Override
        protected float getModPlantGrowthChance() {
            return cfg.floweringPeriod;
        }

        @Override
        protected boolean canBonemealModPlant() {
            return cfg.canBonemeal;
        }

        @Override
        protected float getModProduceSeedDropChance() {
            return cfg.produceSeedDropChance;
        }

        @Override
        protected boolean doesModProduceDropSeeds() {
            return cfg.produceDropSeeds;
        }
    }.register();

    /**
     * Platinum plant set for Thermal Foundation.
     */
    public static final ModPlantSetMetallic MOD_PLANT_SET_TF_PLATINUM
            = new ModPlantSetMetallic("platinum", ResynthMod.MODID_THERMAL_FOUNDATION
            , "ore", 6, "ore", 6) {
        ResynthConfig.ModPlantMetallicCfg cfg = ResynthConfig.PLANT_PLATINUM;

        @Override
        protected int getResultCount() {
            return cfg.yield;
        }

        @Override
        protected boolean doesModOreDropSeeds() {
            return cfg.oreDropSeeds;
        }

        @Override
        protected float getModOreSeedDropChance() {
            return cfg.oreSeedDropChance;
        }

        @Override
        protected float getModPlantGrowthChance() {
            return cfg.floweringPeriod;
        }

        @Override
        protected boolean canBonemealModPlant() {
            return cfg.canBonemeal;
        }

        @Override
        protected float getModProduceSeedDropChance() {
            return cfg.produceSeedDropChance;
        }

        @Override
        protected boolean doesModProduceDropSeeds() {
            return cfg.produceDropSeeds;
        }
    }.register();

    /**
     * Iridium plant set for Thermal Foundation.
     */
    public static final ModPlantSetMetallic MOD_PLANT_SET_TF_IRIDIUM
            = new ModPlantSetMetallic("iridium", ResynthMod.MODID_THERMAL_FOUNDATION
            , "ore", 7, "ore", 7) {
        ResynthConfig.ModPlantMetallicCfg cfg = ResynthConfig.PLANT_IRIDIUM;

        @Override
        protected int getResultCount() {
            return cfg.yield;
        }

        @Override
        protected boolean doesModOreDropSeeds() {
            return cfg.oreDropSeeds;
        }

        @Override
        protected float getModOreSeedDropChance() {
            return cfg.oreSeedDropChance;
        }

        @Override
        protected float getModPlantGrowthChance() {
            return cfg.floweringPeriod;
        }

        @Override
        protected boolean canBonemealModPlant() {
            return cfg.canBonemeal;
        }

        @Override
        protected float getModProduceSeedDropChance() {
            return cfg.produceSeedDropChance;
        }

        @Override
        protected boolean doesModProduceDropSeeds() {
            return cfg.produceDropSeeds;
        }
    }.register();

    /**
     * Mana plant set for Thermal Foundation.
     */
    public static final ModPlantSetMetallic MOD_PLANT_SET_TF_MANA
            = new ModPlantSetMetallic("mana", ResynthMod.MODID_THERMAL_FOUNDATION
            , "ore", 8, "ore", 8) {
        ResynthConfig.ModPlantMetallicCfg cfg = ResynthConfig.PLANT_MANA;

        @Override
        protected int getResultCount() {
            return cfg.yield;
        }

        @Override
        protected boolean doesModOreDropSeeds() {
            return cfg.oreDropSeeds;
        }

        @Override
        protected float getModOreSeedDropChance() {
            return cfg.oreSeedDropChance;
        }

        @Override
        protected float getModPlantGrowthChance() {
            return cfg.floweringPeriod;
        }

        @Override
        protected boolean canBonemealModPlant() {
            return cfg.canBonemeal;
        }

        @Override
        protected float getModProduceSeedDropChance() {
            return cfg.produceSeedDropChance;
        }

        @Override
        protected boolean doesModProduceDropSeeds() {
            return cfg.produceDropSeeds;
        }
    }.register();


//    //Will not work... it just won't
//    /*
//     * Plant for Black Quartz Ore from Actually Additions
//     */
//    public static final ModPlantSetMetallic MOD_PLANT_BLACK_QUARTZ
//            = new ModPlantSetMetallic("blackQuartz", ResynthMod.MODID_ACTUALLY_ADDITIONS,
//            "block_misc", 3, "item_misc", 5) {
//        @Override
//        protected int getResultCount() {
//            return 1;
//        }
//
//        @Override
//        protected boolean doesModOreDropSeeds() {
//            return true;
//        }
//
//        @Override
//        protected float getModOreSeedDropChance() {
//            return 100;
//        }
//
//        @Override
//        protected float getModPlantGrowthChance() {
//            return 100;
//        }
//
//        @Override
//        protected boolean canBonemealModPlant() {
//            return true;
//        }
//
//        @Override
//        protected float getModProduceSeedDropChance() {
//            return 100;
//        }
//
//        @Override
//        protected boolean doesModProduceDropSeeds() {
//            return true;
//        }
//    }.register();


    /**
     * Yellorite plant set for Extreme Reactors (port of Big Reactors)
     */
    public static final ModPlantSetMetallic MOD_PLANT_SET_ER_YELLORITE
            = new ModPlantSetMetallic("yellorite", ResynthMod.MODID_EXTREME_REACTORS,
            "brore", 0, "brore", 0) {
        ResynthConfig.ModPlantMetallicCfg cfg = ResynthConfig.PLANT_YELLORITE;
        @Override
        protected int getResultCount() {
            return cfg.yield;
        }

        @Override
        protected boolean doesModOreDropSeeds() {
            return cfg.oreDropSeeds;
        }

        @Override
        protected float getModOreSeedDropChance() {
            return cfg.oreSeedDropChance;
        }

        @Override
        protected float getModPlantGrowthChance() {
            return cfg.floweringPeriod;
        }

        @Override
        protected boolean canBonemealModPlant() {
            return cfg.canBonemeal;
        }

        @Override
        protected float getModProduceSeedDropChance() {
            return cfg.produceSeedDropChance;
        }

        @Override
        protected boolean doesModProduceDropSeeds() {
            return cfg.produceDropSeeds;
        }
    }.register();

//    //Failed attempt at adding resonating ore.
//    /**
//     * Resonating Ore plant for Deep Resonance.
//     */
//    public static final ModPlantSetMetallic MOD_PLANT_RESONATING_ORE
//            = new ModPlantSetMetallic("resonatingOre", ResynthMod.MODID_DEEP_RESONANCE,
//            "resonating_ore", 0, "resonating_ore", 0) {
//        ResynthConfig.ModPlantMetallicCfg cfg = ResynthConfig.PLANT_YELLORITE;
//        @Override
//        protected int getResultCount() {
//            return 1;//return cfg.yield;
//        }
//
//        @Override
//        protected boolean doesModOreDropSeeds() {
//            return true;//return cfg.oreDropSeeds;
//        }
//
//        @Override
//        protected float getModOreSeedDropChance() {
//            return 100;//return cfg.oreSeedDropChance;
//        }
//
//        @Override
//        protected float getModPlantGrowthChance() {
//            return 100;//return cfg.floweringPeriod;
//        }
//
//        @Override
//        protected boolean canBonemealModPlant() {
//            return true;//return cfg.canBonemealModPlant;
//        }
//
//        @Override
//        protected float getModProduceSeedDropChance() {
//            return 100;//return cfg.produceSeedDropChance;
//        }
//
//        @Override
//        protected boolean doesModProduceDropSeeds() {
//            return true;//return cfg.produceDropSeeds;
//        }
//    }.register();

    /**
     * Draconium plant set for Draconic Evolution.
     */
    public static final ModPlantSetCrystalline MOD_PLANT_SET_DRACONIUM
            = new ModPlantSetCrystalline("draconium", ResynthMod.MODID_DRACONIC_EVOLUTION,
            "draconium_ore", "draconium_dust", 0) {
        ResynthConfig.ModPlantCrystallineCfg cfg = ResynthConfig.PLANT_DRACONIUM;

        @Override
        protected int getResultCount() {
            return cfg.yield;
        }

        @Override
        protected boolean doesModOreDropSeeds() {
            return cfg.oreDropSeeds;
        }

        @Override
        protected float getModOreSeedDropChance() {
            return cfg.oreSeedDropChance;
        }

        @Override
        protected float getModPlantGrowthChance() {
            return cfg.floweringPeriod;
        }

        @Override
        protected boolean canBonemealModPlant() {
            return cfg.canBonemeal;
        }

        @Override
        protected float getModProduceSeedDropChance() {
            return cfg.produceSeedDropChance;
        }

        @Override
        protected boolean doesModProduceDropSeeds() {
            return cfg.produceDropSeeds;
        }
    }.register();

    //Static class.
    private ResynthPlantSets(){}


    /**
     * @return an array of the registered plant blocks.
     */
    public static BlockPlantBase[] getPlantBlocks(){
        return ResynthPlantSetRegistry.getPlantBlocks();
    }

    /**
     * @return an array of the registered seed items.
     */
    public static ItemPlantSeeds[] getSeedItems(){
        return ResynthPlantSetRegistry.getSeedItems();
    }

    /**
     * @return an array of the registered organic ore blocks.
     */
    public static BlockOrganicPlantOre[] getOrganicOreBlocks(){
        return ResynthPlantSetRegistry.getOrganicOreBlocks();
    }

    /**
     * @return an array of the registered plant shard produce items.
     */
    public static ItemPlantProduceShard[] getShardProduceItems(){
        return ResynthPlantSetRegistry.getShardProduceItems();
    }

    /**
     * @return an array of the registered bulb produce items.
     */
    public static ItemPlantProduceBulb[] getBulbProduceItems(){
        return ResynthPlantSetRegistry.getBulbProduceItems();
    }

    /**
     * @return an array of the registered metallic plant sets.
     */
    public static PlantSetMetallic[] getMetallicPlantSets(){
        return ResynthPlantSetRegistry.getMetallicPlantSets();
    }

    /**
     * @return an array of the registered crystalline plant sets.
     */
    public static PlantSetCrystalline[] getCrystallinePlantSets(){
        return ResynthPlantSetRegistry.getCrystallinePlantSets();
    }

    /**
     * @return an array of the registered biochemical plant sets.
     */
    public static PlantSetBiochemical[] getBiochemicalPlantSets(){
        return ResynthPlantSetRegistry.getBiochemicalPlantSets();
    }
}
