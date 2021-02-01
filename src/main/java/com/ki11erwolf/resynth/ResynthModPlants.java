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
package com.ki11erwolf.resynth;

import com.ki11erwolf.resynth.plant.set.*;
import com.ki11erwolf.resynth.plant.set.properties.CrystallineProperties;
import com.ki11erwolf.resynth.plant.set.properties.MetallicProperties;
import com.ki11erwolf.resynth.plant.set.properties.ProduceProperties;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.function.Consumer;

import static com.ki11erwolf.resynth.ResynthPlants.DEFAULT_PRODUCE_PROPERTIES;

/**
 * Holds the definitions and references to every Resynth
 * plant (or plant set to be specific) for other mods
 * that may or may not be installed.
 *
 * The plant sets in this class CAN be {@code null}
 * if the mod it's for is not installed, or they can
 * be in a state of failure if they otherwise
 * fail to set themselves up correct due to some
 * mod error.
 *
 * This class is responsible for defining the various plants,
 * their properties, as well everything else that makes them unique
 * (e.g. growth rates and drops).
 *
 * All plant sets in this class are referred to as Modded plant sets.
 */
@SuppressWarnings("ConstantConditions")
public class ResynthModPlants {

    // ********
    //   Mods
    // ********

    /**
     * A queryable type of mini registry that holds a list of all the mods
     * (mod name and id) that Resynth adds plants for/supports.
     */
    public static class Mods {

        // *******
        // MOD IDS
        // *******

        /**
         * The mod id for the Simple Ores mod.
         */
        private static final Mod SIMPLE_ORES = new Mod("Simple Ores", "simpleores");

        /**
         * The mod id for the More Ores in ONE mod.
         */
        private static final Mod MORE_ORES_IN_ONE = new Mod("More Ores in ONE", "moreoresinone");

        /**
         * The mod id for the Just Another Ruby Mod.
         */
        private static final Mod JUST_ANOTHER_RUBY_MOD = new Mod("Just Another Ruby Mod", "ruby");

        /**
         * The mod id for the Blue Power mod.
         */
        private static final Mod BLUE_POWER = new Mod("Blue Power", "bluepower");

        /**
         * The mod id for the Basic Nether Ores Mod.
         */
        private static final Mod BASIC_NETHER_ORES = new Mod("Basic Nether Ores", "bno");

        /**
         * The mod id for the Mystical Agriculture mod.
         */
        private static final Mod MYSTICAL_AGRICULTURE = new Mod("Mystical Agriculture", "mysticalagriculture");

        /**
         * The mod id for the Mekanism mod.
         */
        private static final Mod MEKANISM = new Mod("Mekanism", "mekanism");

        /**
         * The mod id for the Botania mod.
         */
        private static final Mod BOTANIA = new Mod("Botania", "botania");

        /**
         * The mod id for "The Midnight" mod.
         */
        private static final Mod THE_MIDNIGHT = new Mod("The Midnight", "midnight");

        /**
         * The mod id for the mod 'Applied Energistics 2'.
         */
        private static final Mod APPLIED_ENERGISTICS_2 = new Mod("Applied Energistics 2", "appliedenergistics2");

        /**
         * The mod id for the mod 'Thermal Foundation', called 'Thermal, from the Thermal Series'.
         */
        private static final Mod THERMAL_FOUNDATION = new Mod("Thermal Foundation", "thermal");

        /**
         * The mod id for the mod 'Immersive Engineering'.
         */
        private static final Mod IMMERSIVE_ENGINEERING = new Mod("Immersive Engineering", "immersiveengineering");

        /**
         * The global map instance that stores the list of supported mods
         * for the class. Every supported mod is stored as a Mod object,
         * that holds the name & id of the mod, mapped to an internal
         * numeric ID.
         */
        private static final Map<Integer, Mod> MOD_LIST
                = new HashMap<Integer, Mod>() {{
                //Integer based Map allows negative indexing, used for weird mod integration
                putMod(-2, new Mod("The One Probe", "theoneprobe"));
                putMod(-1, new Mod("Hwyla", "waila"));
                //Normal mods
                putMod(SIMPLE_ORES);
                putMod(MORE_ORES_IN_ONE);
                putMod(JUST_ANOTHER_RUBY_MOD);
                putMod(BLUE_POWER);
                putMod(BASIC_NETHER_ORES);
                putMod(MYSTICAL_AGRICULTURE);
                putMod(MEKANISM);
                putMod(BOTANIA);
                putMod(THE_MIDNIGHT);
                putMod(APPLIED_ENERGISTICS_2);
                putMod(THERMAL_FOUNDATION);
                putMod(IMMERSIVE_ENGINEERING);
            }

            /**
             * Internal tracker for counting number of mods
             * and mod ids.
             */
            private int modCount = 1;

            /**
             * Adds a mod reference to the list of supported mods,
             * mapped to the given numerical ID. Also sets the
             * numerical ID in the mod reference object.
             *
             * @param id the unique numerical ID to store the mod
             *           reference under.
             * @param mod the mod representation object, containing
             *            the name and modid of the mod.
             */
            public void putMod(Integer id, Mod mod){
                this.put(id, mod);
                mod.numericID = id;
            }

            /**
             * Adds a mod reference to the list of supported mods,
             * mapped to the given numerical ID. Also sets the
             * numerical ID in the mod reference object. The mods
             * ID is set automatically.
             *
             * @param mod the mod representation object, containing
             *            the name and modid of the mod.
             */
            public void putMod(Mod mod) {
                this.putMod(modCount++, mod);
            }
        };

        /**
         * Gets a reference to a representation of a supported mod using the
         * internal numerical ID the mod representation was stored under.
         * The ID's can be found in the {@link #MOD_LIST} map.
         *
         * @param id the internal numerical ID the mod representation
         *           is stored under.
         * @return the mod object, containing the mod name and modid, that
         * represents the supported mod.
         */
        public static Mod getModByNumericID(int id){
            return MOD_LIST.getOrDefault(id, null);
        }

        /**
         * @return an array of all the mods Resynth adds support for,
         * as an array of Mod objects that each represent a supported mod.
         */
        public static Mod[] getAllMods(){
            List<Mod> mods = new ArrayList<>(MOD_LIST.size() + 1);
            mods.addAll(MOD_LIST.values());
            return mods.toArray(new Mod[0]);
        }

        /**
         * Allows iterating over all the Mod objects that
         * each represent a supported mod.
         *
         * @param action the action to execute for each Mod
         *               representation object.
         */
        public static void iterateAllMods(Consumer<Mod> action){
            MOD_LIST.values().iterator().forEachRemaining(action);
        }

        // Mod Instance

        /**
         * Represents an external mod Resynth adds plants/support for
         * that can be installed alongside Resynth. A Mod object holds
         * the display name, modID, and internal numeric id of the mod
         * the Mod object represents.
         */
        public static class Mod {

            /**
             * The textual display name of the mod this
             * object represents.
             */
            private final String name;

            /**
             * The unique ModID of the mod this object represents.
             */
            private final String id;

            /**
             * The internal numerical id this mod representation
             * is mapped under within the {@link Mod#MOD_LIST}.
             */
            private int numericID;

            /**
             * Creates a new Mod object that will represent
             * a supported mod.
             *
             * @param name the textual display name of the mod.
             * @param id the unique ModID of the mod this object represents.
             */
            private Mod(String name, String id){
                this.name = name;
                this.id = id;
            }

            /**
             * @return the textual display name of the mod this
             * object represents.
             */
            public String getName() {
                return name;
            }

            /**
             * @return the unique ModID of the mod this object represents.
             */
            public String getModID() {
                return id;
            }

            /**
             * @return the internal numerical id this mod representation is
             * mapped under within the {@link Mod#MOD_LIST}.
             */
            public int getNumericID(){
                return numericID;
            }

            /**
             * @return a new {@link ResourceLocation} which is configured
             * with this {@link #getModID()} as its parent mod, or namespace
             * as it's called. The resource path is an empty String, however.
             */
            public ResourceLocation getEmptyID() {
                return new ResourceLocation(getModID(), "");
            }

            /**
             * @return a new {@link ResourceLocation} which is configured
             * with this {@link #getModID()} as its parent mod, or namespace
             * as it's called. The resource name, called path, is set to
             * {@code name} - allowing easy creation of ID's that point to
             * objects from the parent mod.
             */
            public ResourceLocation getID(String name) {
                return new ResourceLocation(getModID(), Objects.requireNonNull(name));
            }
        }
    }

    // *******************
    // GENERAL DEFINITIONS
    // *******************

    // Crystalline

    /**
     * The set properties for general onyx gems and onyx ore.
     */
    private static final CrystallineProperties ONYX_PROPERTIES = new CrystallineProperties(
            false, 9, 1,
            7, 5
    );

    /**
     * The set properties for general ruby ore and gems.
     */
    private static final CrystallineProperties RUBY_PROPERTIES = new CrystallineProperties(
            false, 14, 1,
            1, 25
    );

    /**
     * The set properties for general sapphire ore and gems.
     */
    private static final CrystallineProperties SAPPHIRE_PROPERTIES = new CrystallineProperties(
            false, 14, 1,
            1, 25
    );

    /**
     * The set properties for general topaz ore and gems.
     */
    private static final CrystallineProperties TOPAZ_PROPERTIES = new CrystallineProperties(
            false, 14, 1,
            1, 25
    );

    /**
     * The set properties for general amethyst ore and gems.
     */
    private static final CrystallineProperties AMETHYST_PROPERTIES = new CrystallineProperties(
            false, 14, 1,
            1, 25
    );

    /**
     * The set properties for general opal ore and gems.
     */
    private static final CrystallineProperties OPAL_PROPERTIES = new CrystallineProperties(
            false, 14, 1,
            1, 25
    );

    /**
     * The set properties for general malachite ore and gems.
     */
    private static final CrystallineProperties MALACHITE_PROPERTIES = new CrystallineProperties(
            false, 14, 1,
            1, 25
    );

    /**
     * The set properties for general teslatite ore and gems.
     */
    private static final CrystallineProperties TESLATITE_PROPERTIES = new CrystallineProperties(
            false, 13, 1,
            1, 25
    );

    /**
     * The set properties for general Prosperity ore and gems/dusts.
     */
    private static final CrystallineProperties PROSPERITY_PROPERTIES = new CrystallineProperties(
            false, 18, 2,
            1, 10
    );

    /**
     * The set properties for general Inferium ore and gems/dusts.
     */
    private static final CrystallineProperties INFERIUM_PROPERTIES = new CrystallineProperties(
            false, 20, 2,
            1.5F, 10
    );

    /**
     * The set properties for general Soulium ore and gems/dusts.
     */
    private static final CrystallineProperties SOULIUM_PROPERTIES = new CrystallineProperties(
            false, 18, 2,
            1F, 10
    );

    /**
     * The plant set properties for every botania flower petal plant.
     */
    private static final CrystallineProperties BOTANIA_PETAL_PROPERTIES = new CrystallineProperties(
            false, 25, 2, 10, 20
    );


    /**
     * The plant set properties for all items from The Midnight Mod grown with Crystalline plants.
     */
    private static final CrystallineProperties MIDNIGHT_CRYSTALLINE = new CrystallineProperties(
            false, 35, 1, 3, 6
    );

    private static final CrystallineProperties CERTUS_QUARTZ_PROPERTIES = new CrystallineProperties(
            false, 20, 1, 3, 6
    );

    private static final CrystallineProperties CHARGED_CERTUS_QUARTZ_PROPERTIES = new CrystallineProperties(
            false, 13, 1, 4, 8
    );

    private static final CrystallineProperties APATITE_PROPERTIES = new CrystallineProperties(
            false, 40, 2, 3, 6
    );

    private static final CrystallineProperties CINNABAR_PROPERTIES = new CrystallineProperties(
            false, 25, 2, 3, 6
    );

    private static final CrystallineProperties SULFUR_PROPERTIES = new CrystallineProperties(
            false, 20, 2, 3, 6
    );

    // Metallic

    /**
     * The set properties for general copper and copper ore.
     */
    private static final MetallicProperties COPPER_PROPERTIES = new MetallicProperties(
            false, 35, 7, 7
    );

    /**
     * The set properties for general tin and tin ore.
     */
    private static final MetallicProperties TIN_PROPERTIES = new MetallicProperties(
            false, 35, 7, 7
    );

    /**
     * The set properties for general aluminium and aluminium ore.
     */
    private static final MetallicProperties ALUMINIUM_PROPERTIES = new MetallicProperties(
            false, 30, 6, 6
    );

    /**
     * The set properties for general lead and lead ore.
     */
    private static final MetallicProperties LEAD_PROPERTIES = new MetallicProperties(
            false, 25, 8, 8
    );

    /**
     * The set properties for general nickel and nickel ore.
     */
    private static final MetallicProperties NICKEL_PROPERTIES = new MetallicProperties(
            false, 25, 8, 8
    );

    /**
     * The set properties for general uranium and uranium ore.
     */
    private static final MetallicProperties URANIUM_PROPERTIES = new MetallicProperties(
            false, 15, 4, 4
    );

    /**
     * The set properties for general silver and silver ore.
     */
    private static final MetallicProperties SILVER_PROPERTIES = new MetallicProperties(
            false, 20, 8, 8
    );

    /**
     * The set properties for general zinc and zinc ore.
     */
    private static final MetallicProperties ZINC_PROPERTIES = new MetallicProperties(
            false, 25, 8, 8
    );

    private static final MetallicProperties PLATINUM_PROPERTIES = new MetallicProperties(
            false, 8, 4, 8
    );

    /**
     * The set properties for general tungsten and tungsten ore.
     */
    private static final MetallicProperties TUNGSTEN_PROPERTIES = new MetallicProperties(
            false, 25, 8, 8
    );

    /**
     * The set properties for general mythril and mythril ore.
     */
    private static final MetallicProperties MYTHRIL_PROPERTIES = new MetallicProperties(
            false, 25, 5, 5
    );

    /**
     * The set properties for general adamantium and adamantium ore.
     */
    private static final MetallicProperties ADAMANTIUM_PROPERTIES = new MetallicProperties(
            false, 20, 5, 5
    );

    /**
     * The set properties for general Osmium and Osmium Ore.
     */
    static final MetallicProperties OSMIUM_PROPERTIES = new MetallicProperties(
            false, 30F, 6, 6
    );

    /**
     * The plant set properties for all items from The Midnight Mod grown with Metallic plants.
     */
    private static final MetallicProperties MIDNIGHT_METALLIC = new MetallicProperties(
            false, 35, 3, 6
    );


    // **********
    // PLANT SETS
    // **********

    //Simple Ores

    /**
     * The plant set instance for onyx from Simple Ores.
     */
    public static final PlantSet<?, ?> SIMPLE_ORES_ONYX = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.SIMPLE_ORES.getID("onyx"), ONYX_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "onyx_gem", "onyx_ore"
    ));

    /**
     * The plant set instance for copper ore from Simple Ores.
     */
    public static final PlantSet<?, ?> SIMPLE_ORES_COPPER = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.SIMPLE_ORES.getID("copper"), COPPER_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "copper_ore", "copper_ore"
    ));

    /**
     * The plant set instance for tin ore from Simple Ores.
     */
    public static final PlantSet<?, ?> SIMPLE_ORES_TIN = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.SIMPLE_ORES.getID("tin"), TIN_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "tin_ore", "tin_ore"
    ));

    /**
     * The plant set instance for mythril ore from Simple Ores.
     */
    public static final PlantSet<?, ?> SIMPLE_ORES_MYTHRIL = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.SIMPLE_ORES.getID("mythril"), MYTHRIL_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "mythril_ore", "mythril_ore"
    ));

    /**
     * The plant set instance for adamantium ore from Simple Ores.
     */
    public static final PlantSet<?, ?> SIMPLE_ORES_ADAMANTIUM = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.SIMPLE_ORES.getID("adamantium"), ADAMANTIUM_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "adamantium_ore", "adamantium_ore"
    ));

    //More Ores in ONE

    /**
     * The plant set for ruby ore from More Ores in ONE.
     */
    public static final PlantSet<?, ?> MORE_ORES_RUBY = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.MORE_ORES_IN_ONE.getID("ruby"), RUBY_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "ruby", "ruby_ore"
    ));

    /**
     * The plant set for sapphire ore from More Ores in ONE.
     */
    public static final PlantSet<?, ?> MORE_ORES_SAPPHIRE = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.MORE_ORES_IN_ONE.getID("sapphire"), SAPPHIRE_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "sapphire", "sapphire_ore"
    ));

    /**
     * The plant set for topaz ore from More Ores in ONE.
     */
    public static final PlantSet<?, ?> MORE_ORES_TOPAZ = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.MORE_ORES_IN_ONE.getID("topaz"), TOPAZ_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "topaz", "topaz_ore"
    ));

    /**
     * The plant set for amethyst ore from More Ores in ONE.
     */
    public static final PlantSet<?, ?> MORE_ORES_AMETHYST = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.MORE_ORES_IN_ONE.getID("amethyst"), AMETHYST_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "amethyst", "amethyst_ore"
    ));

    //Just Another Ruby Mod

    /**
     * The plant set for ruby from the Just Another Ruby Mod.
     */
    public static final PlantSet<?, ?> JUST_ANOTHER_RUBY_MOD_RUBY
            = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.JUST_ANOTHER_RUBY_MOD.getID("ruby"), RUBY_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "ruby_gem", "ruby_ore"
    ));

    /**
     * The plant set for opal from the Just Another Ruby Mod.
     */
    public static final PlantSet<?, ?> JUST_ANOTHER_RUBY_MOD_OPAL
            = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.JUST_ANOTHER_RUBY_MOD.getID("opal"), OPAL_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "opal_gem", "opal_ore"
    ));

    //Blue Power

    /**
     * The plant set for amethyst from the Blue Power mod.
     */
    public static final PlantSet<?, ?> BLUE_POWER_AMETHYST = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.BLUE_POWER.getID("amethyst"), AMETHYST_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "amethyst_gem", "amethyst_ore"
    ));

    /**
     * The plant set for ruby from the Blue Power mod.
     */
    public static final PlantSet<?, ?> BLUE_POWER_RUBY = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.BLUE_POWER.getID("ruby"), RUBY_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "ruby_gem", "ruby_ore"
    ));

    /**
     * The plant set for sapphire from the Blue Power mod.
     */
    public static final PlantSet<?, ?> BLUE_POWER_SAPPHIRE = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.BLUE_POWER.getID("sapphire"), SAPPHIRE_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "sapphire_gem", "sapphire_ore"
    ));

    /**
     * The plant set for malachite from the Blue Power mod.
     */
    public static final PlantSet<?, ?> BLUE_POWER_MALACHITE = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.BLUE_POWER.getID("malachite"), MALACHITE_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "malachite_gem", "malachite_ore"
    ));

    /**
     * The plant set for teslatite from the Blue Power mod.
     */
    public static final PlantSet<?, ?> BLUE_POWER_TESLATITE = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.BLUE_POWER.getID("teslatite"), TESLATITE_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "teslatite_dust", "teslatite_ore"
    ));

    /**
     * The plant set for copper from the Blue Power mod.
     */
    public static final PlantSet<?, ?> BLUE_POWER_COPPER = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.BLUE_POWER.getID("copper"), COPPER_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "copper_ore", "copper_ore"
    ));

    /**
     * The plant set for silver from the Blue Power mod.
     */
    public static final PlantSet<?, ?> BLUE_POWER_SILVER = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.BLUE_POWER.getID("silver"), SILVER_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "silver_ore", "silver_ore"
    ));

    /**
     * The plant set for zinc from the Blue Power mod.
     */
    public static final PlantSet<?, ?> BLUE_POWER_ZINC = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.BLUE_POWER.getID("zinc"), ZINC_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "zinc_ore", "zinc_ore"
    ));

    /**
     * The plant set for tungsten from the Blue Power mod.
     */
    public static final PlantSet<?, ?> BLUE_POWER_TUNGSTEN = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.BLUE_POWER.getID("tungsten"), TUNGSTEN_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "tungsten_ore", "tungsten_ore"
    ));

    //Basic Nether Ores

    /**
     * The plant set for nether iron from Basic Nether Ores.
     */
    public static final PlantSet<?, ?> BASIC_NETHER_ORES_IRON = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.BASIC_NETHER_ORES.getID("iron"), ResynthPlants.IRON_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            new ResourceLocation("minecraft", "iron_ore"), Mods.BASIC_NETHER_ORES.getID("netheriron_ore")
    ));

    /**
     * The plant set for nether coal from Basic Nether Ores.
     */
    public static final PlantSet<?, ?> BASIC_NETHER_ORES_COAL = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.BASIC_NETHER_ORES.getID("coal"), ResynthPlants.COAL_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            new ResourceLocation("minecraft", "coal"), Mods.BASIC_NETHER_ORES.getID("nethercoal_ore")
    ));

    /**
     * The plant set for nether lapis lazuli from Basic Nether Ores.
     */
    public static final PlantSet<?, ?> BASIC_NETHER_ORES_LAPIS_LAZULI
            = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.BASIC_NETHER_ORES.getID("lapis_lazuli"), ResynthPlants.LAPIS_LAZULI_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            new ResourceLocation("minecraft", "lapis_lazuli"), Mods.BASIC_NETHER_ORES.getID("netherlapis_ore")
    ));

    /**
     * The plant set for nether redstone from Basic Nether Ores.
     */
    public static final PlantSet<?, ?> BASIC_NETHER_ORES_REDSTONE
            = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.BASIC_NETHER_ORES.getID("redstone"), ResynthPlants.REDSTONE_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            new ResourceLocation("minecraft", "redstone"), Mods.BASIC_NETHER_ORES.getID("netherredstone_ore")
    ));

    /**
     * The plant set for nether diamond from Basic Nether Ores.
     */
    public static final PlantSet<?, ?> BASIC_NETHER_ORES_DIAMOND
            = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.BASIC_NETHER_ORES.getID("diamond"), ResynthPlants.DIAMOND_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            new ResourceLocation("minecraft", "diamond"), Mods.BASIC_NETHER_ORES.getID("netherdiamond_ore")
    ));

    /**
     * The plant set for nether emerald from Basic Nether Ores.
     */
    public static final PlantSet<?, ?> BASIC_NETHER_ORES_EMERALD
            = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.BASIC_NETHER_ORES.getID("emerald"), ResynthPlants.EMERALD_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            new ResourceLocation("minecraft", "emerald"), Mods.BASIC_NETHER_ORES.getID("netheremerald_ore")
    ));

    /**
     * The plant set for nether uranium for Basic Nether Ores.
     */
    public static final PlantSet<?, ?> BASIC_NETHER_ORES_URANIUM = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.BASIC_NETHER_ORES.getID("uranium"), URANIUM_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "netheruranium_ore", "netheruranium_ore"
    ));

    /**
     * The plant set for nether tin for Basic Nether Ores.
     */
    public static final PlantSet<?, ?> BASIC_NETHER_ORES_TIN = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.BASIC_NETHER_ORES.getID("tin"), TIN_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "nethertin_ore", "nethertin_ore"
    ));

    /**
     * The plant set for nether silver for Basic Nether Ores.
     */
    public static final PlantSet<?, ?> BASIC_NETHER_ORES_SILVER = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.BASIC_NETHER_ORES.getID("silver"), SILVER_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "nethersilver_ore", "nethersilver_ore"
    ));

    /**
     * The plant set for nether nickel for Basic Nether Ores.
     */
    public static final PlantSet<?, ?> BASIC_NETHER_ORES_NICKEL = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.BASIC_NETHER_ORES.getID("nickel"), NICKEL_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "nethernickel_ore", "nethernickel_ore"
    ));

    /**
     * The plant set for nether lead for Basic Nether Ores.
     */
    public static final PlantSet<?, ?> BASIC_NETHER_ORES_LEAD = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.BASIC_NETHER_ORES.getID("lead"), LEAD_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "netherlead_ore", "netherlead_ore"
    ));

    /**
     * The plant set for nether copper for Basic Nether Ores.
     */
    public static final PlantSet<?, ?> BASIC_NETHER_ORES_COPPER = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.BASIC_NETHER_ORES.getID("copper"), COPPER_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "nethercopper_ore", "nethercopper_ore"
    ));

    /**
     * The plant set for nether aluminium for Basic Nether Ores.
     */
    public static final PlantSet<?, ?> BASIC_NETHER_ORES_ALUMINIUM
            = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.BASIC_NETHER_ORES.getID("aluminium"), ALUMINIUM_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "netheraluminum_ore", "netheraluminum_ore"
    ));

    //Mekanism

    /**
     * The plant set for Osmium from Mekanism.
     */
    public static final PlantSet<?, ?> MEKANISM_OSMIUM
            = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.MEKANISM.getID("osmium"), OSMIUM_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "osmium_ore", "osmium_ore"
    ));

    /**
     * The plant set for Copper from Mekanism.
     */
    public static final PlantSet<?, ?> MEKANISM_COPPER
            = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.MEKANISM.getID("copper"), COPPER_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "copper_ore", "copper_ore"
    ));

    /**
     * The plant set for Tin from Mekanism.
     */
    public static final PlantSet<?, ?> MEKANISM_TIN
            = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.MEKANISM.getID("tin"), TIN_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "tin_ore", "tin_ore"
    ));

    //Mystical Agriculture

    /**
     * The plant set for Prosperity from Mystical Agriculture.
     */
    public static final PlantSet<?, ?> MYSTICAL_AGRICULTURE_PROSPERITY
            = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.MYSTICAL_AGRICULTURE.getID("prosperity"), PROSPERITY_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "prosperity_shard", "prosperity_ore"
    ));

    /**
     * The plant set for Inferium from Mystical Agriculture.
     */
    public static final PlantSet<?, ?> MYSTICAL_AGRICULTURE_INFERIUM
            = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.MYSTICAL_AGRICULTURE.getID("inferium"), INFERIUM_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "inferium_essence", "inferium_ore"
    ));

    /**
     * The plant set for Soulium from Mystical Agriculture.
     */
    public static final PlantSet<?, ?> MYSTICAL_AGRICULTURE_SOULIUM
            = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.MYSTICAL_AGRICULTURE.getID("soulium"), SOULIUM_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "soulium_dust", "soulium_ore"
    ));

    // The Midnight

    /**
     * The plant set for Tenebrum from The Midnight Mod.
     */
    public static final PlantSet<?, ?> MIDNIGHT_TENEBRUM = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.THE_MIDNIGHT.getID("tenebrum"), MIDNIGHT_METALLIC, DEFAULT_PRODUCE_PROPERTIES,
            "tenebrum_ore", "tenebrum_ore"
    ));

    /**
     * The plant set for Nagrilite from The Midnight Mod.
     */
    public static final PlantSet<?, ?> MIDNIGHT_NAGRILITE = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.THE_MIDNIGHT.getID("nagrilite"), MIDNIGHT_METALLIC, DEFAULT_PRODUCE_PROPERTIES,
            "nagrilite_ore", "nagrilite_ore"
    ));

    /**
     * The plant set for Dark Pearls from The Midnight Mod.
     */
    public static final PlantSet<?, ?> MIDNIGHT_DARK_PEARL = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.THE_MIDNIGHT.getID("dark_pearl"), MIDNIGHT_CRYSTALLINE, DEFAULT_PRODUCE_PROPERTIES,
            "dark_pearl", "dark_pearl_ore"
    ));

    /**
     * The plant set for Ebonite from The Midnight Mod.
     */
    public static final PlantSet<?, ?> MIDNIGHT_EBONITE = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.THE_MIDNIGHT.getID("ebonite"), MIDNIGHT_CRYSTALLINE, DEFAULT_PRODUCE_PROPERTIES,
            "ebonite", "ebonite_ore"
    ));

    /**
     * The plant set for Archaic from The Midnight Mod.
     */
    public static final PlantSet<?, ?> MIDNIGHT_ARCHAIC = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.THE_MIDNIGHT.getID("archaic"), MIDNIGHT_CRYSTALLINE, DEFAULT_PRODUCE_PROPERTIES,
            "archaic_shard", "archaic_ore"
    ));

    // Applied Energistics 2

    public static final PlantSet<?, ?> APPLIED_ENERGISTICS_2_CERTUS_QUARTZ = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.APPLIED_ENERGISTICS_2.getID("certus_quartz"), CERTUS_QUARTZ_PROPERTIES,
            new ProduceProperties(3, 200, 2.0),
            "certus_quartz_crystal", "quartz_ore"
    ));

    public static final PlantSet<?, ?> APPLIED_ENERGISTICS_2_CHARGED_CERTUS_QUARTZ = registerIfNotNull(
            PlantSetFactory.makeCrystallineSet(Mods.APPLIED_ENERGISTICS_2.getID("charged_certus_quartz"),
                    CHARGED_CERTUS_QUARTZ_PROPERTIES, new ProduceProperties(2, 200, 2.0),
                    "charged_certus_quartz_crystal", "charged_quartz_ore"
    ));

    // Immersive Engineering

    public static final PlantSet<?, ?> IMMERSIVE_ENGINEERING_ALUMINIUM = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.IMMERSIVE_ENGINEERING.getID("aluminium"), ALUMINIUM_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "ore_aluminum", "ore_aluminum"
    ));

    public static final PlantSet<?, ?> IMMERSIVE_ENGINEERING_COPPER = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.IMMERSIVE_ENGINEERING.getID("copper"), COPPER_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "ore_copper", "ore_copper"
    ));

    public static final PlantSet<?, ?> IMMERSIVE_ENGINEERING_LEAD = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.IMMERSIVE_ENGINEERING.getID("lead"), LEAD_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "ore_lead", "ore_lead"
    ));

    public static final PlantSet<?, ?> IMMERSIVE_ENGINEERING_NICKEL = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.IMMERSIVE_ENGINEERING.getID("nickel"), NICKEL_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "ore_nickel", "ore_nickel"
    ));

    public static final PlantSet<?, ?> IMMERSIVE_ENGINEERING_SILVER = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.IMMERSIVE_ENGINEERING.getID("silver"), SILVER_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "ore_silver", "ore_silver"
    ));

    public static final PlantSet<?, ?> IMMERSIVE_ENGINEERING_URANIUM = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.IMMERSIVE_ENGINEERING.getID("uranium"), URANIUM_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "ore_uranium", "ore_uranium"
    ));

    // Thermal Foundation

    public static final PlantSet<?, ?> THERMAL_FOUNDATION_APATITE = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.THERMAL_FOUNDATION.getID("apatite"), APATITE_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "apatite", "apatite_ore"
    ));

    public static final PlantSet<?, ?> THERMAL_FOUNDATION_CINNABAR = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.THERMAL_FOUNDATION.getID("cinnabar"), CINNABAR_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "cinnabar", "cinnabar_ore"
    ));

    public static final PlantSet<?, ?> THERMAL_FOUNDATION_RUBY = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.THERMAL_FOUNDATION.getID("ruby"), RUBY_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "ruby", "ruby_ore"
    ));

    public static final PlantSet<?, ?> THERMAL_FOUNDATION_SAPPHIRE = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.THERMAL_FOUNDATION.getID("sapphire"), SAPPHIRE_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "sapphire", "sapphire_ore"
    ));

    public static final PlantSet<?, ?> THERMAL_FOUNDATION_SULFUR = registerIfNotNull(PlantSetFactory.makeCrystallineSet(
            Mods.THERMAL_FOUNDATION.getID("sulfur"), SULFUR_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "sulfur", "sulfur_ore"
    ));

    public static final PlantSet<?, ?> THERMAL_FOUNDATION_COPPER = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.THERMAL_FOUNDATION.getID("copper"), COPPER_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "copper_ore", "copper_ore"
    ));

    public static final PlantSet<?, ?> THERMAL_FOUNDATION_LEAD = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.THERMAL_FOUNDATION.getID("lead"), LEAD_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "lead_ore", "lead_ore"
    ));

    public static final PlantSet<?, ?> THERMAL_FOUNDATION_NICKEL = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.THERMAL_FOUNDATION.getID("nickel"), NICKEL_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "nickel_ore", "nickel_ore"
    ));

    // thermal:platinum_ore is not registered as tested on: thermal_foundation-1.16.3-1.1.6
    /* public static final PlantSet<?, ?> THERMAL_FOUNDATION_PLATINUM = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.THERMAL_FOUNDATION.getID("platinum"), PLATINUM_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "platinum_ore", "platinum_ore"
    )); */

    public static final PlantSet<?, ?> THERMAL_FOUNDATION_SILVER = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.THERMAL_FOUNDATION.getID("silver"), SILVER_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "silver_ore", "silver_ore"
    ));

    public static final PlantSet<?, ?> THERMAL_FOUNDATION_TIN = registerIfNotNull(PlantSetFactory.makeMetallicSet(
            Mods.THERMAL_FOUNDATION.getID("tin"), TIN_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
            "tin_ore", "tin_ore"
    ));

    // Botania

    /**
     * A short hand method used to register a crystalline
     * plant set specifically for a type of Botania Flower
     * Petal.
     *
     * @param setIdentifier the flower petal identifier
     *                      (e.g. white).
     * @return the already registered plant set for the
     * requested Botania Flower Petal type.
     */
    private static PlantSet<?, ?> newBotaniaPetalSet(String setIdentifier){
        return registerIfNotNull(PlantSetFactory.makeCrystallineSet(
                Mods.BOTANIA.getID(setIdentifier + "_petal"), BOTANIA_PETAL_PROPERTIES, DEFAULT_PRODUCE_PROPERTIES,
                setIdentifier + "_petal", setIdentifier + "_mystical_flower"
        ));
    }

    /* All 16 plant sets for the 16 different Botania Flower Petals */

    public static final PlantSet<?, ?> BOTANIA_WHITE_PETAL         =       newBotaniaPetalSet("white");
    public static final PlantSet<?, ?> BOTANIA_RED_PETAL           =       newBotaniaPetalSet("red");
    public static final PlantSet<?, ?> BOTANIA_ORANGE_PETAL        =       newBotaniaPetalSet("orange");
    public static final PlantSet<?, ?> BOTANIA_PINK_PETAL          =       newBotaniaPetalSet("pink");
    public static final PlantSet<?, ?> BOTANIA_YELLOW_PETAL        =       newBotaniaPetalSet("yellow");
    public static final PlantSet<?, ?> BOTANIA_LIME_PETAL          =       newBotaniaPetalSet("lime");
    public static final PlantSet<?, ?> BOTANIA_GREEN_PETAL         =       newBotaniaPetalSet("green");
    public static final PlantSet<?, ?> BOTANIA_LIGHT_BLUE_PETAL    =       newBotaniaPetalSet("light_blue");
    public static final PlantSet<?, ?> BOTANIA_CYAN_PETAL          =       newBotaniaPetalSet("cyan");
    public static final PlantSet<?, ?> BOTANIA_BLUE_PETAL          =       newBotaniaPetalSet("blue");
    public static final PlantSet<?, ?> BOTANIA_MAGENTA_PETAL       =       newBotaniaPetalSet("magenta");
    public static final PlantSet<?, ?> BOTANIA_PURPLE_PETAL        =       newBotaniaPetalSet("purple");
    public static final PlantSet<?, ?> BOTANIA_BROWN_PETAL         =       newBotaniaPetalSet("brown");
    public static final PlantSet<?, ?> BOTANIA_GRAY_PETAL          =       newBotaniaPetalSet("gray");
    public static final PlantSet<?, ?> BOTANIA_LIGHT_GRAY_PETAL    =       newBotaniaPetalSet("light_gray");
    public static final PlantSet<?, ?> BOTANIA_BLACK_PETAL         =       newBotaniaPetalSet("black");

    /**Private constructor.*/
    private ResynthModPlants(){}

    /**
     * Ensures all Modded Resynth plant sets are
     * initialized and queued for registration.
     */
    public static void initSets(){/*NO-OP*/}

    /**
     * Will register the given plant set provided
     * it is not {@code null}. This is useful as
     * some modded plant sets may be null if the
     * mod is not present.
     *
     * @param set the plant set to register.
     * @return the registered plant set {@code set}
     * or {@code null} if the plant set was null;
     */
    private static PlantSet<?, ?> registerIfNotNull(PlantSet<?, ?> set){
        if(set == null)
            return null;

        return set.register();
    }
}
