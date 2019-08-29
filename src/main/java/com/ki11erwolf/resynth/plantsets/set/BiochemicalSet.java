package com.ki11erwolf.resynth.plantsets.set;

import com.ki11erwolf.resynth.plantsets.block.BlockBiochemicalPlant;
import com.ki11erwolf.resynth.plantsets.item.ItemBulb;
import com.ki11erwolf.resynth.plantsets.item.ItemSeeds;
import com.ki11erwolf.resynth.util.ItemOrBlock;
import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Used to create Biochemical plant sets. These are plant sets
 * that grow resources normally dropped when a mob is killed.
 * <p/>
 * This class merely handles defining what blocks/items go
 * into a set and how they interact, as well as handling
 * how seeds are obtained (through events).
 */
abstract class BiochemicalSet extends PlantSet<BlockBiochemicalPlant> {

    /**
     * The name of the plant set type.
     */
    private static final String SET_TYPE_NAME = "biochemical";

    /**
     * The static seed hooks instance.
     */
    private static final SeedHooks SEED_HOOKS = new SeedHooks();

    /**
     * The properties specific to this plant set instance.
     */
    private final IBiochemicalSetProperties setProperties;

    /**
     * @param setName the name of the specific plant set instance (e.g. diamond).
     * @param properties the properties for the specific plant set instance.
     */
    BiochemicalSet(String setName, IBiochemicalSetProperties properties) {
        super(SET_TYPE_NAME, setName, SEED_HOOKS);
        this.setProperties = properties;

        //Plant
        this.produceItemOrBlock = new ItemOrBlock(new ItemBulb(SET_TYPE_NAME, setName, properties));
        this.plantBlock = new BlockBiochemicalPlant(SET_TYPE_NAME, setName, setProperties) {
            @Override
            protected ItemSeeds getSeedsItem() {
                return seedsItem;
            }

            @Override
            protected ItemStack getProduce() {
                return new ItemStack(produceItemOrBlock.getItem(), properties.numberOfProduceDrops());
            }
        };
        this.seedsItem = new ItemSeeds(SET_TYPE_NAME, setName, plantBlock, properties);
    }

    // ****************
    // Abstract Getters
    // ****************

    /**
     * Allows the PlantSetFactory to provide the
     * source mobs that seeds are obtained from
     * only when they're needed. This allows
     * providing mobs that are not yet registered
     * to the game. This in turn allows adding mobs
     * from Resynth and other mods.
     *
     * @return the list of mobs this plant sets seeds
     * are obtainable from.
     */
    abstract EntityType[] getSourceMobs();

    // **********
    // Seed Hooks
    // **********

    /**
     * Registers game hooks that allow spawning seeds
     * in the world based on user actions.
     */
    private static class SeedHooks extends PlantSetSeedHooks {

        /**
         * Handles spawning seeds in the world
         * when a player kills a mob that is
         * part of a plant set.
         *
         * @param event forge event.
         */
        @SubscribeEvent
        @SuppressWarnings("unused")
        public void onEntityKilled(LivingDeathEvent event){
            //For each plant set
            for(PlantSet set : PlantSetRegistry.getPlantSets()){
                if(!(set instanceof BiochemicalSet))
                    continue;

                BiochemicalSet plantSet = (BiochemicalSet) set;

                //For each mob type
                for(EntityType entity : plantSet.getSourceMobs()){
                    if(entity.getEntityClass() == event.getEntity().getClass()) {
                        if (MathUtil.chance(plantSet.setProperties.seedSpawnChanceFromMob())) {
                            //Spawn seeds if lucky
                            if(!event.getEntity().getEntityWorld().isRemote)
                                event.getEntity().getEntityWorld().spawnEntity(
                                        new EntityItem(
                                                event.getEntity().getEntityWorld(),
                                                event.getEntity().posX,
                                                event.getEntity().posY,
                                                event.getEntity().posZ,
                                                new ItemStack(set.getSeedsItem())
                                        )
                                );
                        }
                    }
                }
            }
        }

        /**
         * Handles spawning seeds in the world
         * when the player smashes a bulb.
         *
         * @param event forge event.
         */
        @SubscribeEvent
        @SuppressWarnings("unused")
        public void onItemDestroyed(PlayerDestroyItemEvent event){
            //For each plant set
            for(PlantSet set : PlantSetRegistry.getPlantSets()) {
                if (!(set instanceof BiochemicalSet))
                    continue;

                BiochemicalSet plantSet = (BiochemicalSet) set;

                if(event.getOriginal().getItem() == plantSet.getProduceItemOrBlock().getItem()){
                    if(MathUtil.chance(plantSet.setProperties.seedSpawnChanceFromBulb())) {
                        if(!event.getEntity().getEntityWorld().isRemote)
                            event.getEntity().getEntityWorld().spawnEntity(
                                    //Spawn seeds if lucky
                                    new EntityItem(
                                            event.getEntity().getEntityWorld(),
                                            event.getEntity().posX + (MathUtil.chance(50) ?
                                                    MathUtil.getRandomIntegerInRange(1, 2)
                                                    : -MathUtil.getRandomIntegerInRange(1, 2)),
                                            event.getEntity().posY + 2,
                                            event.getEntity().posZ + (MathUtil.chance(50) ?
                                                    MathUtil.getRandomIntegerInRange(1, 2)
                                                    : -MathUtil.getRandomIntegerInRange(1, 2)),
                                            new ItemStack(plantSet.getSeedsItem())
                                    )
                            );
                    }
                }
            }
        }
    }
}
