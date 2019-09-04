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
package com.ki11erwolf.resynth.block;

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.integration.RHwylaIntegration;
import com.ki11erwolf.resynth.util.QueueRegisterer;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Holds static references to all the mods base blocks (not including
 * plant blocks/produce).
 *
 * This class also handles the registering of blocks(and their ItemBlock).
 */
@Mod.EventBusSubscriber(modid = ResynthMod.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ResynthBlocks extends QueueRegisterer<Block> {

    /**
     * Singleton instance of this class. NOT EXPOSED.
     */
    static final ResynthBlocks INSTANCE = new ResynthBlocks();

    /**
     * Private (non-instantiatable) constructor.
     */
    private ResynthBlocks(){}

    // ***************
    // Block Instances
    // ***************

    /**
     * Mineral Rich Stone. The mods ore.
     */
    public static final ResynthBlock BLOCK_MINERAL_STONE = new BlockMineralStone().queueRegistration();

    /**
     * Mineral Enriched Soil. The farmland block for the mod.
     */
    public static final ResynthBlock BLOCK_MINERAL_SOIL = new BlockMineralSoil().queueRegistration();

    /**
     * Mystical Seed Pos. The random biochemical seed dropper plant.
     */
    public static final ResynthBlock BLOCK_SEED_POD = new BlockSeedPod().queueRegistration();

    // *****
    // Logic
    // *****

    /**
     * Registers all the queued blocks to the game.
     *
     * @param event forge event.
     */
    @SubscribeEvent
    @SuppressWarnings("unused")//Reflection
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        INSTANCE.iterateQueue(block -> {
            ResynthMod.getNewLogger().debug("Registering Resynth Block: " + block);
            event.getRegistry().register(block);

            //Hwyla
            RHwylaIntegration.addIfProvider(block);
        });
    }
}
