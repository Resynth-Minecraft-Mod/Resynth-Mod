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
import com.ki11erwolf.resynth.plant.block.BlockPlantBiochemical;
import com.ki11erwolf.resynth.plant.item.ItemPlantMobProduce;
import com.ki11erwolf.resynth.plant.item.ItemPlantSeed;
import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * The class used to create new biochemical plants
 * with seeds item and produce item.
 */
@Mod.EventBusSubscriber
public abstract class PlantBiochemical {

    /**
     * The plant block.
     */
    private final BlockPlantBiochemical plant;

    /**
     * The produce item.
     */
    private final ItemPlantMobProduce produce;

    /**
     * The seeds item.
     */
    private final ItemPlantSeed seeds;

    /**
     * The entity that drops this plants seeds.
     */
    private final Class<? extends EntityLiving> entity;

    /**
     * Constructor.
     *
     * @param name the name of the plant, seeds and produce.
     * @param entity - the entity that drops this plants seeds.
     */
    public PlantBiochemical(String name, Class<? extends EntityLiving> entity){
        this.entity = entity;
        this.plant = new BlockPlantBiochemical(name) {

            @Override
            protected Item getSeed() {return seeds;}

            @Override
            protected Item getProduce() {return produce;}

            @Override
            protected float getGrowthPeriod() {return getFloweringPeriod();}

            @Override
            protected boolean canBonemeal() {return canBoneMeal();}
        };

        this.seeds = new ItemPlantSeed(plant, name, name);

        this.produce = new ItemPlantMobProduce(name, seeds) {
            @Override
            protected float getSeedSpawnChance() {
                return getProduceSeedDropChance();
            }
        };

        this.register();
    }

    /**
     * @return The plant block.
     */
    public BlockPlantBiochemical getPlant(){
        return this.plant;
    }

    /**
     * @return the seeds item.
     */
    public ItemPlantSeed getSeeds(){
        return this.seeds;
    }

    /**
     * @return the produce item.
     */
    public ItemPlantMobProduce getProduce(){
        return this.produce;
    }

    /**
     * Adds this plant to the game.
     */
    private void register(){
        ResynthPlantRegistry.addPlant(plant);
        ResynthPlantRegistry.addMobProduce(produce);
        ResynthPlantRegistry.addSeeds(seeds);
        ResynthPlantRegistry.addPlant(this);
    }

    /**
     * @return the item given when this plants produce item
     * is smelted.
     */
    public abstract ItemStack getResult();

    /**
     * @return true if the given minecraft ore block should drop seeds.
     */
    protected abstract boolean doesMobDropSeeds();

    /**
     * @return the chance the minecraft ore block will drop this plants seeds.
     */
    protected abstract float getMobSeedDropChance();

    /**
     * @return how long this plant takes to grow.
     */
    protected abstract float getFloweringPeriod();

    /**
     * @return true if bonemeal can be used on this plant.
     */
    protected abstract boolean canBoneMeal();

    /**
     * @return the chance this plants produce will turn into seeds in water.
     */
    protected abstract float getProduceSeedDropChance();

    /**
     * @return true if this plants produce turns into seeds in water.
     */
    protected abstract boolean doesProduceDropSeeds();

    /**
     * @return the chance a mystical seed pod will drop this plant
     * types seeds.
     */
    public abstract float getSeedPodDropPercentage();

    /**
     * Handles distributing seeds when a mob is killed by a player.
     *
     * @param deathEvent {@link net.minecraftforge.event.entity.living.LivingDeathEvent}
     */
    @SubscribeEvent
    public static void onMobDeath(net.minecraftforge.event.entity.living.LivingDeathEvent deathEvent){
        Entity causer = deathEvent.getSource().getTrueSource();
        Entity victim = deathEvent.getEntity();

        if(causer == null || causer.getClass() != EntityPlayerMP.class || !ResynthConfig.PLANTS_GENERAL.mobDropSeeds)
            return;

        for(PlantBiochemical plant : ResynthPlantRegistry.getBiochemicalPlants()){
            if(victim.getClass() == plant.entity) {
                if (plant.doesMobDropSeeds() && MathUtil.chance(plant.getMobSeedDropChance())) {
                    victim.getEntityWorld().spawnEntity(
                            new EntityItem(victim.getEntityWorld(),
                                    victim.posX, victim.posY, victim.posZ,
                                    new ItemStack(plant.getSeeds(), 1))
                    );
                }
            }
        }
    }
}
