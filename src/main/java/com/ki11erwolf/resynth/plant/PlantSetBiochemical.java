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
import com.ki11erwolf.resynth.plant.block.BlockPlantBiochemical;
import com.ki11erwolf.resynth.plant.item.ItemPlantProduceBulb;
import com.ki11erwolf.resynth.plant.item.ItemPlantSeeds;
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
 * API used to create new plant sets (this includes the:
 * plant, seeds, produce and seed spawning logic) for
 * vanilla mobs.
 */
@Mod.EventBusSubscriber
public abstract class PlantSetBiochemical {

    /**
     * The plant block.
     */
    private final BlockPlantBiochemical plant;

    /**
     * The produce item.
     */
    private final ItemPlantProduceBulb produce;

    /**
     * The seeds item.
     */
    private final ItemPlantSeeds seeds;

    /**
     * The entity that drops this plants seeds.
     */
    private final Class<? extends EntityLiving> entity;

    /**
     * Default constructor.
     *
     * @param name the name of the plant, seeds and produce.
     * @param entity - the entity that drops this plants seeds.
     */
    public PlantSetBiochemical(String name, Class<? extends EntityLiving> entity){
        this.entity = entity;
        this.plant = new BlockPlantBiochemical(name) {

            @Override
            protected Item getSeed() {return seeds;}

            @Override
            protected Item getProduce() {return produce;}

            @Override
            protected float _getGrowthChance() {return getPlantGrowthChance();}

            @Override
            protected boolean canBonemeal() {return canBonemealPlant();}
        };

        this.seeds = new ItemPlantSeeds(plant, name);

        this.produce = new ItemPlantProduceBulb(name, seeds) {
            @Override
            protected float getSeedSpawnChance() {
                return getProduceSeedDropChance();
            }
        };

        this.register();
    }

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

        for(PlantSetBiochemical plant : ResynthPlantSetRegistry.getBiochemicalPlantSets()){
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

    /**
     * @return The plant block.
     */
    public BlockPlantBiochemical getPlant(){
        return this.plant;
    }

    /**
     * @return the seeds item.
     */
    public ItemPlantSeeds getSeeds(){
        return this.seeds;
    }

    /**
     * @return the produce item.
     */
    public ItemPlantProduceBulb getProduce(){
        return this.produce;
    }

    /**
     * Adds this plant set to the forge registry.
     */
    private void register(){
        ResynthPlantSetRegistry.addPlantBlock(plant);
        ResynthPlantSetRegistry.addBulbProduce(produce);
        ResynthPlantSetRegistry.addSeeds(seeds);
        ResynthPlantSetRegistry.addPlantSet(this);
    }

    /**
     * @return the item given when this plants produce item
     * is smelted.
     */
    public abstract ItemStack getResult();

    /**
     * @return true if the given minecraft mob should drop this
     * plant types seeds.
     */
    protected abstract boolean doesMobDropSeeds();

    /**
     * @return the chance the minecraft mob will drop this plants seeds.
     */
    protected abstract float getMobSeedDropChance();

    /**
     * @return the chance this plant will grow
     * on an approved growth tick.
     */
    protected abstract float getPlantGrowthChance();

    /**
     * @return true if bonemeal can be used on this plant.
     */
    protected abstract boolean canBonemealPlant();

    /**
     * @return the chance this plants produce will turn into seeds when thrown
     */
    protected abstract float getProduceSeedDropChance();

    /**
     * @return true if this plants produce turns into seeds when thrown.
     */
    protected abstract boolean doesProduceDropSeeds();

    /**
     * @return the chance a mystical seed pod will drop this plant
     * types seeds.
     */
    public abstract float getSeedPodDropPercentage();
}
