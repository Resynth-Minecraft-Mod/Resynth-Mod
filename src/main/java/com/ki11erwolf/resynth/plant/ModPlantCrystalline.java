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

import com.ki11erwolf.resynth.ResynthMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.Logger;

/**
 * Allows adding resynth crystalline plant to the game
 * for mods instead of vanilla minecraft.
 */
public abstract class ModPlantCrystalline extends ModPlant<PlantCrystalline>{

    /**
     * The logger for this class.
     */
    private static final Logger LOG = ResynthMod.getLogger();

    /**
     * The final produce mod item.
     */
    private Item modResourceItem;

    /**
     * @param name plant name.
     * @param modid mods modid.
     * @param oreBlockName the mod ore blocks name.
     * @param resourceName the mod resource item name.
     * @param metaData the metadata for the resource item stack.
     */
    public ModPlantCrystalline(String name, String modid,
                               String oreBlockName, String resourceName, int metaData){
        if(Loader.isModLoaded(modid)){
            LOG.info("Loading mod plant: " + name + " | mod: " + modid);
        } else {
            LOG.info("Mod: " + modid + " not loaded. Skipping plant: " + name + "...");
            return;
        }

        Block modOreBlock = getModBlock(modid, oreBlockName);

        if(modOreBlock == null){
            LOG.warn("Could not find mod ore block: " + modid + ":" + oreBlockName + ". Skipping...");
            return;
        }

        backingPlant = new PlantCrystalline(name, modOreBlock) {

            @Override
            public ItemStack getResult() {
                if(modResourceItem == null){
                    modResourceItem = getModItem(modid, resourceName);

                    if(modResourceItem == null){
                        LOG.error("Failed to get mod resource item: " + modid + ":" + resourceName);
                        modResourceItem = getModItem("minecraft", "dirt");
                    }
                }

                return new ItemStack(modResourceItem, getResultCount(), metaData);
            }

            @Override
            protected boolean doesOreDropSeeds() {return doesModOreDropSeeds();}

            @Override
            protected float getOreSeedDropChance() {return getModOreSeedDropChance();}

            @Override
            protected float getFloweringPeriod() {return getModPlantGrowthChance();}

            @Override
            protected boolean canBoneMeal() {return canBonemeal();}

            @Override
            protected float getProduceSeedDropChance() {return getModProduceSeedDropChance();}

            @Override
            protected boolean doesProduceDropSeeds() {return doesModProduceDropSeeds();}
        };
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public ModPlantCrystalline register(){
        return (ModPlantCrystalline)super.register();
    }

    /**
     * @return the number of drops the produce item gives when smelted.
     */
    protected abstract int getResultCount();

    /**
     * @return true if the mod ore block should drop seeds.
     */
    protected abstract boolean doesModOreDropSeeds();

    /**
     * @return the chance the mod ore will drop seeds when mined.
     */
    protected abstract float getModOreSeedDropChance();

    /**
     * @return the chance the plant will grow on a random tick.
     */
    protected abstract float getModPlantGrowthChance();

    /**
     * @return true if bonemeal can be used on the plant.
     */
    protected abstract boolean canBonemeal();

    /**
     * @return the chance the plant produce will drop seeds.
     */
    protected abstract float getModProduceSeedDropChance();

    /**
     * @return true if the plant produce will drop seeds when left in water.
     */
    protected abstract boolean doesModProduceDropSeeds();
}
