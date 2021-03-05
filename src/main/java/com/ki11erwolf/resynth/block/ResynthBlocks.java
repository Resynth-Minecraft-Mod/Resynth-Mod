/*
 * Copyright 2018-2021 Ki11er_wolf
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
package com.ki11erwolf.resynth.block;

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.EnhancersConfig;
import com.ki11erwolf.resynth.integration.Hwyla;
import com.ki11erwolf.resynth.util.QueueRegisterer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Holds static references to all the mods base blocks (not including
 * plant blocks/produce).
 *
 * This class also handles the registering of blocks(and their ItemBlock).
 */
//Block fields register themselves to the game.
@Mod.EventBusSubscriber(modid = ResynthMod.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ResynthBlocks extends QueueRegisterer<Block> {

    // ***************
    // Config Settings
    // ***************

    /**
     * Configuration settings for the Enhancer blocks.
     */
    private static final EnhancersConfig ENHANCERS_CONFIG
            = ResynthConfig.GENERAL_CONFIG.getCategory(EnhancersConfig.class);

    // ********
    // Instance
    // ********

    /**
     * Singleton instance of this class. NOT EXPOSED.
     */
    static final ResynthBlocks INSTANCE = new ResynthBlocks();

    // ***************
    // Block Instances
    // ***************

    /**
     * Mineral Rich Stone. The mods ore.
     */
    public static final ResynthBlock<?> BLOCK_MINERAL_STONE
            = new BlockMineralStone("mineral_stone").queueRegistration();

    /**
     * Mineral Enriched Soil. The farmland block for the mod.
     */
    public static final ResynthBlock<?> BLOCK_MINERAL_SOIL
            = new BlockMineralSoil("mineral_soil").queueRegistration();

    /**
     * Mystical Seed Pod. The random biochemical seed dropper plant.
     */
    public static final ResynthBlock<?> BLOCK_SEED_POD
            = new BlockSeedPod("seed_pod").queueRegistration();

    /**
     * Calaverite ore. Spawns in the nether.
     */
    public static final ResynthBlock<?> BLOCK_CALVINITE_NETHERRACK
            = new BlockOre("calvinite_netherrack").queueRegistration();

    /**
     * Sylvanite ore. Spawns in the end.
     */
    public static final ResynthBlock<?> BLOCK_SYLVANITE_END_STONE
            = new BlockOre("sylvanite_end_stone").queueRegistration();

    /**
     * Calvinite Enhancer. Allows creating tier 2 Mineral Soil.
     */
    @SuppressWarnings({"unused", "RedundantSuppression"})
    public static final ResynthBlock<?> BLOCK_CALVINITE_ENHANCER = new BlockEnhancer(
            "calvinite", ENHANCERS_CONFIG.getCalviniteMineralConcentrationIncrease(), 1
    ).queueRegistration();

    /**
     * Sylvanite Enhancer. Allows creating tier 3 Mineral Soil.
     */
    @SuppressWarnings({"unused", "RedundantSuppression"})
    public static final ResynthBlock<?> BLOCK_SYLVANITE_ENHANCER = new BlockEnhancer(
            "sylvanite", ENHANCERS_CONFIG.getSylvaniteMineralConcentrationIncrease(), 2
    ).queueRegistration();

    /**
     * The weakened form of Ancient Debris which is used to get Seeds by blowing it up.
     */
    public static final ResynthBlock<DamagedBlock<Block>> WEAK_ANCIENT_DEBRIS = new DamagedBlock<>(
            Blocks.ANCIENT_DEBRIS, 0
    ).queueRegistration();

    // *****
    // Logic
    // *****

    /**
     * Private (non-instantiatable) constructor.
     */
    private ResynthBlocks(){}

    /**
     * Registers all the queued blocks to the game.
     *
     * @param event forge event.
     */
    @SubscribeEvent//Reflection
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        INSTANCE.iterateQueue(block -> {
            ResynthMod.getNewLogger().debug("Registering Resynth Block: " + block);
            event.getRegistry().register(block);

            //Hwyla
            Hwyla.addIfProvider(block);
        });
    }
}
