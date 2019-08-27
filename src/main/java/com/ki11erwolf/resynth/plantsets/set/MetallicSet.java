package com.ki11erwolf.resynth.plantsets.set;

import com.ki11erwolf.resynth.plantsets.block.BlockMetallicPlant;
import com.ki11erwolf.resynth.plantsets.block.BlockOrganicOre;
import com.ki11erwolf.resynth.plantsets.item.ItemSeeds;
import com.ki11erwolf.resynth.util.ItemOrBlock;
import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Used to create Metallic plant sets. These are plant sets
 * that grow resources normally obtained by smelting an ore.
 * <p/>
 * This class merely handles defining what blocks/items go
 * into a set and how they interact, as well as handling
 * how seeds are obtained (through events).
 */
abstract class MetallicSet extends PlantSet<BlockMetallicPlant> {

    /**
     * The name of the plant set type.
     */
    private static final String SET_TYPE_NAME = "metallic";

    /**
     * Static seed hooks instance.
     */
    private static final SeedHooks SEED_HOOKS = new SeedHooks();

    /**
     * The properties for this specific plant set.
     */
    private final IMetallicSetProperties properties;

    /**
     * @param setName the name of the specific plant set instance (e.g. iron).
     * @param properties the properties for the specific plant set instance.
     */
    MetallicSet(String setName, IMetallicSetProperties properties) {
        super(SET_TYPE_NAME, setName, SEED_HOOKS);
        this.properties = properties;

        this.produceItemOrBlock = new ItemOrBlock(new BlockOrganicOre(SET_TYPE_NAME, setName, properties));
        this.plantBlock = new BlockMetallicPlant(SET_TYPE_NAME, setName, properties) {
            @Override
            protected ItemSeeds getSeedsItem() {
                return seedsItem;
            }

            @Override
            protected ItemStack getProduce() {
                return new ItemStack(produceItemOrBlock.getBlock(), 1);
            }
        };
        this.seedsItem = new ItemSeeds(SET_TYPE_NAME, setName, plantBlock, properties);
    }

    // ****************
    // Abstract Methods
    // ****************

    /**
     * Allows the plant set factory to provide the source ore block
     * only when it's needed (when a block is blown up in game). This
     * allows providing source ore blocks that are not yet registered
     * to the game. This in turn allows adding source ore blocks from
     * Resynth and other mods.
     *
     * @return the ore block seeds from this set are obtained from.
     */
    abstract ItemStack getSourceOre();

    // **********
    // Seed Hooks
    // **********

    /**
     * Registers game hooks that allow spawning seeds
     * in the world based on player actions.
     */
    private static class SeedHooks extends PlantSetSeedHooks {

        /**
         * Handles distributing plant set seeds in the world
         * when a specific ore/organic ore block is destroyed
         * by TNT.
         *
         * @param detonateEvent forge event.
         */
        @SubscribeEvent
        @SuppressWarnings("unused")//Reflection
        public void onExplosion(ExplosionEvent.Detonate detonateEvent){
            World world = detonateEvent.getWorld();

            //For each block
            for(BlockPos pos : detonateEvent.getAffectedBlocks()){
                IBlockState block = world.getBlockState(pos);

                //For each set
                for(PlantSet set : PlantSetRegistry.getPlantSets()) {
                    //Checks
                    if (!(set instanceof MetallicSet))
                        continue;

                    //Chance of spawning item.
                    float chance = 0.0F;
                    if(block.getBlock() == Block.getBlockFromItem(((MetallicSet) set).getSourceOre().getItem())){
                        chance = ((MetallicSet) set).properties.seedSpawnChanceFromOre();
                    } else if(block.getBlock() == set.getProduceItemOrBlock().getBlock()){
                        chance = ((MetallicSet) set).properties.seedSpawnChanceFromOrganicOre();
                    }

                    //Spawn item.
                    if(MathUtil.chance(chance)){
                        world.spawnEntity(new EntityItem(
                                world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(set.seedsItem)
                        ));
                    }
                }
            }
        }

    }
}
