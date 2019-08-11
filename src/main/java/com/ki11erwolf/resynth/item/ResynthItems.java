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
package com.ki11erwolf.resynth.item;

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.util.QueueRegisterer;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Holds static references to all the mods base items (not including
 * seeds and produce and plants).
 *
 * This class also handles the registering of items.
 */
@Mod.EventBusSubscriber(modid = ResynthMod.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ResynthItems extends QueueRegisterer<Item> {

    /**
     * Static singleton instance.
     */
    static final ResynthItems INSTANCE = new ResynthItems();

    /**
     * Private constructor.
     */
    private ResynthItems() {}

    // **************
    // Item Instances
    // **************

    /**
     * Mineral Rock. The item dropped from Mineral Rich Rock.
     */
    public static final ResynthItem ITEM_MINERAL_ROCK = new ItemMineralRock().queueRegistration();

    /**
     * Dense Mineral Rock. x9 MineralRocks.
     */
    public static final ResynthItem ITEM_DENSE_MINERAL_ROCK = new ItemDenseMineralRock().queueRegistration();

    /**
     * Mineral Crystal. Recipe component for the Mineral Hoe.
     */
    public static final ResynthItem ITEM_MINERAL_CRYSTAL = new ItemMineralCrystal().queueRegistration();

    /**
     * Mineral Hoe. Tool used to turn dirt/grass into mineral soil.
     */
    public static final ResynthItem ITEM_MINERAL_HOE = new ItemMineralHoe().queueRegistration();

    // *****
    // Logic
    // *****

    /**
     * Registers all the queued items to the game.
     *
     * @param event forge event.
     */
    @SubscribeEvent
    @SuppressWarnings("unused")//Reflection
    public static void registerItems(RegistryEvent.Register<Item> event) {
        INSTANCE.iterateQueue(item -> event.getRegistry().register(item));
    }
}
