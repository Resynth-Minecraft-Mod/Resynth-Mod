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
package com.ki11erwolf.resynth.plant;

import com.ki11erwolf.resynth.ResynthConfig;
import com.ki11erwolf.resynth.plant.block.BlockPlantBase;
import com.ki11erwolf.resynth.plant.block.BlockPlantOre;
import com.ki11erwolf.resynth.plant.item.ItemPlantMobProduce;
import com.ki11erwolf.resynth.plant.item.ItemPlantOreProduce;
import com.ki11erwolf.resynth.plant.item.ItemPlantSeed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * List of all plants.
 */
public final class ResynthPlants {

    /**
     * The iron plant instance.
     */
    public static final PlantMetallic PLANT_IRON = new PlantMetallic("iron", Blocks.IRON_ORE){
        @Override
        protected float getFloweringPeriod() {return ResynthConfig.PLANT_IRON.floweringPeriod;}

        @Override
        protected boolean canBoneMeal() {return ResynthConfig.PLANT_IRON.canBonemeal;}

        @Override
        protected float getOreSeedDropChance() {return ResynthConfig.PLANT_IRON.oreSeedDropChance;}

        @Override
        protected float getOrganicOreSeedDropChance() {return ResynthConfig.PLANT_IRON.organicOreSeedDropChance;}

        @Override
        protected boolean doesOreDropSeeds() {return ResynthConfig.PLANT_IRON.oreDropSeeds;}

        @Override
        protected boolean doesOrganicOreDropSeeds() {return ResynthConfig.PLANT_IRON.organicOreDropSeeds;}

        @Override
        public ItemStack getResult() {return new ItemStack(Items.IRON_INGOT, ResynthConfig.PLANT_IRON.yield);}
    };

    /**
     * The gold plant instance.
     */
    public static final PlantMetallic PLANT_GOLD = new PlantMetallic("gold", Blocks.GOLD_ORE) {
        @Override
        protected float getFloweringPeriod() {return ResynthConfig.PLANT_GOLD.floweringPeriod;}

        @Override
        protected boolean canBoneMeal() {return ResynthConfig.PLANT_GOLD.canBonemeal;}

        @Override
        protected float getOreSeedDropChance() {return ResynthConfig.PLANT_GOLD.oreSeedDropChance;}

        @Override
        protected float getOrganicOreSeedDropChance() {return ResynthConfig.PLANT_GOLD.organicOreSeedDropChance;}

        @Override
        protected boolean doesOreDropSeeds() {return ResynthConfig.PLANT_GOLD.oreDropSeeds;}

        @Override
        protected boolean doesOrganicOreDropSeeds() {return ResynthConfig.PLANT_GOLD.organicOreDropSeeds;}

        @Override
        public ItemStack getResult() {return new ItemStack(Items.GOLD_INGOT, ResynthConfig.PLANT_GOLD.yield);}
    };

    /*
        CRYSTALLINE
     */

    /**
     * The diamond plant instance.
     */
    public static final PlantCrystalline PLANT_DIAMOND = new PlantCrystalline("diamond", Blocks.DIAMOND_ORE) {
        private final ResynthConfig.PlantDiamond cfg = ResynthConfig.PLANT_DIAMOND;

        @Override
        protected boolean doesOreDropSeeds() { return cfg.oreDropSeeds; }

        @Override
        protected float getOreSeedDropChance() { return cfg.oreSeedDropChance; }

        @Override
        protected float getFloweringPeriod() { return cfg.floweringPeriod; }

        @Override
        protected boolean canBoneMeal() { return cfg.canBonemeal; }

        @Override
        protected float getProduceSeedDropChance() { return cfg.produceSeedDropChance; }

        @Override
        protected boolean doesProduceDropSeeds() { return cfg.produceDropSeeds; }

        @Override
        public ItemStack getResult() { return new ItemStack(Items.DIAMOND, cfg.yield); }
    };

    /**
     * The redstone plant instance.
     */
    public static final PlantCrystalline PLANT_REDSTONE
            = new PlantCrystalline("redstone", Blocks.REDSTONE_ORE) {
        private final ResynthConfig.PlantRedstone cfg = ResynthConfig.PLANT_REDSTONE;

        @Override
        protected boolean doesOreDropSeeds() { return cfg.oreDropSeeds; }

        @Override
        protected float getOreSeedDropChance() { return cfg.oreSeedDropChance; }

        @Override
        protected float getFloweringPeriod() { return cfg.floweringPeriod; }

        @Override
        protected boolean canBoneMeal() { return cfg.canBonemeal; }

        @Override
        protected float getProduceSeedDropChance() { return cfg.produceSeedDropChance; }

        @Override
        protected boolean doesProduceDropSeeds() { return cfg.produceDropSeeds; }

        @Override
        public ItemStack getResult() { return new ItemStack(Items.REDSTONE, cfg.yield); }
    };

    /**
     * The lapis plant instance.
     */
    public static final PlantCrystalline PLANT_LAPIS
            = new PlantCrystalline("lapis", Blocks.LAPIS_ORE) {
        private final ResynthConfig.PlantLapis cfg = ResynthConfig.PLANT_LAPIS;

        @Override
        protected boolean doesOreDropSeeds() { return cfg.oreDropSeeds; }

        @Override
        protected float getOreSeedDropChance() { return cfg.oreSeedDropChance; }

        @Override
        protected float getFloweringPeriod() { return cfg.floweringPeriod; }

        @Override
        protected boolean canBoneMeal() { return cfg.canBonemeal; }

        @Override
        protected float getProduceSeedDropChance() { return cfg.produceSeedDropChance; }

        @Override
        protected boolean doesProduceDropSeeds() { return cfg.produceDropSeeds; }

        @Override
        public ItemStack getResult() { return new ItemStack(Items.DYE, cfg.yield, 4); }
    };

    /**
     * The coal plant instance.
     */
    public static final PlantCrystalline PLANT_COAL
            = new PlantCrystalline("coal", Blocks.COAL_ORE) {
                private final ResynthConfig.PlantCoal cfg = ResynthConfig.PLANT_COAL;

        @Override
        protected boolean doesOreDropSeeds() { return cfg.oreDropSeeds; }

        @Override
        protected float getOreSeedDropChance() { return cfg.oreSeedDropChance; }

        @Override
        protected float getFloweringPeriod() { return cfg.floweringPeriod; }

        @Override
        protected boolean canBoneMeal() { return cfg.canBonemeal; }

        @Override
        protected float getProduceSeedDropChance() { return cfg.produceSeedDropChance; }

        @Override
        protected boolean doesProduceDropSeeds() { return cfg.produceDropSeeds; }

        @Override
        public ItemStack getResult() { return new ItemStack(Items.COAL, cfg.yield); }
    };

    /**
     * The emerald plant instance.
     */
    public static final PlantCrystalline PLANT_EMERALD
            = new PlantCrystalline("emerald", Blocks.EMERALD_ORE) {
        private final ResynthConfig.PlantEmerald cfg = ResynthConfig.PLANT_EMERALD;

        @Override
        protected boolean doesOreDropSeeds() { return cfg.oreDropSeeds; }

        @Override
        protected float getOreSeedDropChance() { return cfg.oreSeedDropChance; }

        @Override
        protected float getFloweringPeriod() { return cfg.floweringPeriod; }

        @Override
        protected boolean canBoneMeal() { return cfg.canBonemeal; }

        @Override
        protected float getProduceSeedDropChance() { return cfg.produceSeedDropChance; }

        @Override
        protected boolean doesProduceDropSeeds() { return cfg.produceDropSeeds; }

        @Override
        public ItemStack getResult() { return new ItemStack(Items.EMERALD, cfg.yield); }
    };

    /**
     * The quartz plant instance.
     */
    public static final PlantCrystalline PLANT_QUARTZ
            = new PlantCrystalline("quartz", Blocks.QUARTZ_ORE) {
        private final ResynthConfig.PlantQuartz cfg = ResynthConfig.PLANT_QUARTZ;

        @Override
        protected boolean doesOreDropSeeds() { return cfg.oreDropSeeds; }

        @Override
        protected float getOreSeedDropChance() { return cfg.oreSeedDropChance; }

        @Override
        protected float getFloweringPeriod() { return cfg.floweringPeriod; }

        @Override
        protected boolean canBoneMeal() { return cfg.canBonemeal; }

        @Override
        protected float getProduceSeedDropChance() { return cfg.produceSeedDropChance; }

        @Override
        protected boolean doesProduceDropSeeds() { return cfg.produceDropSeeds; }

        @Override
        public ItemStack getResult() { return new ItemStack(Items.QUARTZ, cfg.yield); }
    };

    /**
     * The glowstone plant instance.
     */
    public static final PlantCrystalline PLANT_GLOWSTONE
            = new PlantCrystalline("glowstone", Blocks.GLOWSTONE) {
        private final ResynthConfig.PlantGlowstone cfg = ResynthConfig.PLANT_GLOWSTONE;

        @Override
        protected boolean doesOreDropSeeds() { return cfg.oreDropSeeds; }

        @Override
        protected float getOreSeedDropChance() { return cfg.oreSeedDropChance; }

        @Override
        protected float getFloweringPeriod() { return cfg.floweringPeriod; }

        @Override
        protected boolean canBoneMeal() { return cfg.canBonemeal; }

        @Override
        protected float getProduceSeedDropChance() { return cfg.produceSeedDropChance; }

        @Override
        protected boolean doesProduceDropSeeds() { return cfg.produceDropSeeds; }

        @Override
        public ItemStack getResult() { return new ItemStack(Items.GLOWSTONE_DUST, cfg.yield); }
    };

    /*
        BIOCHEMICAL
     */

    /**
     * The ender pearl plant instance.
     */
    public static final PlantBiochemical PLANT_ENDER_PEARL
            = new PlantBiochemical("enderpearl", EntityEnderman.class){
        ResynthConfig.PlantEnderpearl cfg = ResynthConfig.PLANT_ENDERPEARL;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.ENDER_PEARL, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getFloweringPeriod() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBoneMeal() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}
    };

    /**
     * The gunpowder plant instance.
     */
    public static final PlantBiochemical PLANT_GUNPOWDER
            = new PlantBiochemical("gunpowder", EntityCreeper.class){
        ResynthConfig.PlantGunpowder cfg = ResynthConfig.PLANT_GUNPOWDER;

        @Override
        public ItemStack getResult() {return new ItemStack(Items.GUNPOWDER, cfg.yield);}

        @Override
        protected boolean doesMobDropSeeds() {return cfg.mobDropSeeds;}

        @Override
        protected float getMobSeedDropChance() {return cfg.mobSeedDropChance;}

        @Override
        protected float getFloweringPeriod() {return cfg.floweringPeriod;}

        @Override
        protected boolean canBoneMeal() {return cfg.canBonemeal;}

        @Override
        protected float getProduceSeedDropChance() {return cfg.produceSeedDropChance;}

        @Override
        protected boolean doesProduceDropSeeds() {return cfg.produceDropSeeds;}
    };

    //Static class.
    private ResynthPlants(){}

    /**
     * @return an array of the metallic plants to register.
     */
    public static PlantMetallic[] getMetallicPlants(){
        return ResynthPlantRegistry.getMetallicPlants();
    }

    /**
     * @return an array of the plant blocks to register.
     */
    public static BlockPlantBase[] getPlantBlocks(){
        return ResynthPlantRegistry.getPlantBlocks();
    }

    /**
     * @return an array of the seed items to register.
     */
    public static ItemPlantSeed[] getSeedItems(){
        return ResynthPlantRegistry.getSeedItems();
    }

    /**
     * @return an array of the plant ore blocks to register.
     */
    public static BlockPlantOre[] getOreBlocks(){
        return ResynthPlantRegistry.getOreBlocks();
    }

    /**
     * @return an array of the plant produce items to register.
     */
    public static ItemPlantOreProduce[] getProduceItems(){
        return ResynthPlantRegistry.getProduce();
    }

    /**
     * @return an array of the mob produce items to register.
     */
    public static ItemPlantMobProduce[] getMobProduceItems(){
        return ResynthPlantRegistry.getMobProduce();
    }

    /**
     * @return an array of the crystalline plants to register.
     */
    public static PlantCrystalline[] getCrystallinePlants(){
        return ResynthPlantRegistry.getCrystallinePlants();
    }

    /**
     * @return an array of the biochemical plants to register.
     */
    public static PlantBiochemical[] getBiochemicalPlants(){
        return ResynthPlantRegistry.getBiochemicalPlants();
    }
}
