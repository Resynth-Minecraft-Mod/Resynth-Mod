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
@SuppressWarnings("unused")//Fields register themselves.
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
    public static final ResynthItem ITEM_MINERAL_ROCK
            = new ResynthItem("mineral_rock").queueRegistration();

    /**
     * Dense Mineral Rock. x9 MineralRocks.
     */
    public static final ResynthItem ITEM_DENSE_MINERAL_ROCK
            = new ResynthItem("dense_mineral_rock").queueRegistration();

    /**
     * Mineral Crystal. Recipe component for the Mineral Hoe.
     */
    public static final ResynthItem ITEM_MINERAL_CRYSTAL
            = new ItemMineralCrystal("mineral_crystal").queueRegistration();

    /**
     * Mineral Hoe. Tool used to turn dirt/grass into mineral soil.
     */
    public static final ResynthItem ITEM_MINERAL_HOE_OLD
            = new ItemMineralHoeOld("mineral_hoe");

    /**
     * Mineral Hoe. Resynth's tool of choice.
     */
    public static final ResynthItem ITEM_MINERAL_HOE
            = new ItemMineralHoe("mineral_hoe").queueRegistration();

    /**
     * Calvinite crystal. Dropped when Calvinite Infused Netherrack is mined.
     */
    public static final ResynthItem ITEM_CALVINITE
            = new ResynthItem("calvinite_crystal").queueRegistration();

    /**
     * Sylvanite crystal. Dropped when Sylvanite Infused End Stone is mined.
     */
    public static final ResynthItem ITEM_SYLVANITE
            = new ResynthItem("sylvanite_crystal").queueRegistration();

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
        INSTANCE.iterateQueue(item -> {
            ResynthMod.getNewLogger().debug("Registering Resynth Item: " + item.getClass().getName());
            event.getRegistry().register(item);
        });
    }
}
