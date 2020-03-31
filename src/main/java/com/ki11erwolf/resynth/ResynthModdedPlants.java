/*
 * Copyright 2018-2020 Ki11er_wolf
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

import com.ki11erwolf.resynth.plant.set.CrystallineSetProperties;
import com.ki11erwolf.resynth.plant.set.MetallicSetProperties;
import com.ki11erwolf.resynth.plant.set.PlantSet;
import com.ki11erwolf.resynth.plant.set.PlantSetFactory;

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
@SuppressWarnings("unused")//Fields register themselves.
public class ResynthModdedPlants {

    // *******
    // MOD IDS
    // *******

    /**
     * The mod id for the Simple Ores mod.
     */
    private static final String SIMPLE_ORES = "simpleores";

    /**
     * The mod id for the More Ores in ONE mod.
     */
    private static final String MORE_ORES_IN_ONE = "moreoresinone";

    /**
     * The mod id for the Just Another Ruby Mod.
     */
    private static final String JUST_ANOTHER_RUBY_MOD = "ruby";

    /**
     * The mod id for the Blue Power mod.
     */
    private static final String BLUE_POWER = "bluepower";

    /**
     * The mod id for the Basic Nether Ores Mod.
     */
    private static final String BASIC_NETHER_ORES = "bno";

    /**
     * The mod id for the Mystical Agriculture mod.
     */
    private static final String MYSTICAL_AGRICULTURE = "mysticalagriculture";

    /**
     * The mod id for the Mekanism Mod.
     */
    private static final String MEKANISM = "mekanism";

    // *******************
    // GENERAL DEFINITIONS
    // *******************

    // Crystalline

    /**
     * The set properties for general Diamond and Diamond Ore.
     */
    static final CrystallineSetProperties DIAMOND_PROPERTIES = new CrystallineSetProperties(
            false, 8.0F,1,
            1.5F, 25.0F
    );

    /**
     * The set properties for general Redstone and Redstone Ore.
     */
    static final CrystallineSetProperties REDSTONE_PROPERTIES = new CrystallineSetProperties(
            false, 65.0F, 4,
            1.0F, 75.0F
    );

    /**
     * The set properties for general Lapis Lazuli and Lapis Lazuli Ore.
     */
    static final CrystallineSetProperties LAPIS_LAZULI_PROPERTIES = new CrystallineSetProperties(
            false, 45, 3,
            2, 50F
    );

    /**
     * The set properties for general Coal and Coal Ore.
     */
    static final CrystallineSetProperties COAL_PROPERTIES = new CrystallineSetProperties(
            false, 75, 2,
            0.5F, 50F
    );

    /**
     * The set properties for general Emerald and Emerald Ore.
     */
    static final CrystallineSetProperties EMERALD_PROPERTIES = new CrystallineSetProperties(
            false, 7.0F, 1,
            1.5F, 25F
    );

    /**
     * The set properties for general onyx gems and onyx ore.
     */
    private static final CrystallineSetProperties ONYX_PROPERTIES = new CrystallineSetProperties(
            false, 9, 1,
            7, 5
    );

    /**
     * The set properties for general ruby ore and gems.
     */
    private static final CrystallineSetProperties RUBY_PROPERTIES = new CrystallineSetProperties(
            false, 14, 1,
            1, 25
    );

    /**
     * The set properties for general sapphire ore and gems.
     */
    private static final CrystallineSetProperties SAPPHIRE_PROPERTIES = new CrystallineSetProperties(
            false, 14, 1,
            1, 25
    );

    /**
     * The set properties for general topaz ore and gems.
     */
    private static final CrystallineSetProperties TOPAZ_PROPERTIES = new CrystallineSetProperties(
            false, 14, 1,
            1, 25
    );

    /**
     * The set properties for general amethyst ore and gems.
     */
    private static final CrystallineSetProperties AMETHYST_PROPERTIES = new CrystallineSetProperties(
            false, 14, 1,
            1, 25
    );

    /**
     * The set properties for general opal ore and gems.
     */
    private static final CrystallineSetProperties OPAL_PROPERTIES = new CrystallineSetProperties(
            false, 14, 1,
            1, 25
    );

    /**
     * The set properties for general malachite ore and gems.
     */
    private static final CrystallineSetProperties MALACHITE_PROPERTIES = new CrystallineSetProperties(
            false, 14, 1,
            1, 25
    );

    /**
     * The set properties for general teslatite ore and gems.
     */
    private static final CrystallineSetProperties TESLATITE_PROPERTIES = new CrystallineSetProperties(
            false, 13, 1,
            1, 25
    );

    /**
     * The set properties for general Prosperity ore and gems/dusts.
     */
    private static final CrystallineSetProperties PROSPERITY_PROPERTIES = new CrystallineSetProperties(
            false, 18, 2,
            1, 10
    );

    /**
     * The set properties for general Inferium ore and gems/dusts.
     */
    private static final CrystallineSetProperties INFERIUM_PROPERTIES = new CrystallineSetProperties(
            false, 20, 2,
            1.5F, 10
    );

    /**
     * The set properties for general Soulium ore and gems/dusts.
     */
    private static final CrystallineSetProperties SOULIUM_PROPERTIES = new CrystallineSetProperties(
            false, 18, 2,
            1F, 10
    );

    // Metallic

    /**
     * The set properties for general Iron and Iron Ore.
     */
    static final MetallicSetProperties IRON_PROPERTIES = new MetallicSetProperties(
            false, 40, 6, 6
    );

    /**
     * The set properties for general Gold and Gold Ore.
     */
    static final MetallicSetProperties GOLD_PROPERTIES = new MetallicSetProperties(
            false, 25, 5, 5
    );

    /**
     * The set properties for general copper and copper ore.
     */
    private static final MetallicSetProperties COPPER_PROPERTIES = new MetallicSetProperties(
            false, 35, 7, 7
    );

    /**
     * The set properties for general tin and tin ore.
     */
    private static final MetallicSetProperties TIN_PROPERTIES = new MetallicSetProperties(
            false, 35, 7, 7
    );

    /**
     * The set properties for general aluminium and aluminium ore.
     */
    private static final MetallicSetProperties ALUMINIUM_PROPERTIES = new MetallicSetProperties(
            false, 30, 6, 6
    );

    /**
     * The set properties for general lead and lead ore.
     */
    private static final MetallicSetProperties LEAD_PROPERTIES = new MetallicSetProperties(
            false, 25, 8, 8
    );

    /**
     * The set properties for general nickel and nickel ore.
     */
    private static final MetallicSetProperties NICKEL_PROPERTIES = new MetallicSetProperties(
            false, 25, 8, 8
    );

    /**
     * The set properties for general uranium and uranium ore.
     */
    private static final MetallicSetProperties URANIUM_PROPERTIES = new MetallicSetProperties(
            false, 15, 4, 4
    );

    /**
     * The set properties for general silver and silver ore.
     */
    private static final MetallicSetProperties SILVER_PROPERTIES = new MetallicSetProperties(
            false, 20, 8, 8
    );

    /**
     * The set properties for general zinc and zinc ore.
     */
    private static final MetallicSetProperties ZINC_PROPERTIES = new MetallicSetProperties(
            false, 25, 8, 8
    );

    /**
     * The set properties for general tungsten and tungsten ore.
     */
    private static final MetallicSetProperties TUNGSTEN_PROPERTIES = new MetallicSetProperties(
            false, 25, 8, 8
    );

    /**
     * The set properties for general mythril and mythril ore.
     */
    private static final MetallicSetProperties MYTHRIL_PROPERTIES = new MetallicSetProperties(
            false, 25, 5, 5
    );

    /**
     * The set properties for general adamantium and adamantium ore.
     */
    private static final MetallicSetProperties ADAMANTIUM_PROPERTIES = new MetallicSetProperties(
            false, 20, 5, 5
    );

    /**
     * The set properties for general Osmium and Osmium Ore.
     */
    static final MetallicSetProperties OSMIUM_PROPERTIES = new MetallicSetProperties(
            false, 30F, 6, 6
    );

    // **********
    // PLANT SETS
    // **********

    //Simple Ores

    /**
     * The plant set instance for onyx from Simple Ores.
     */
    public static final PlantSet<?> SIMPLE_ORES_ONYX = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            SIMPLE_ORES, "onyx", ONYX_PROPERTIES, "onyx_ore"
    ));

    /**
     * The plant set instance for copper ore from Simple Ores.
     */
    public static final PlantSet<?> SIMPLE_ORES_COPPER = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            SIMPLE_ORES, "copper", COPPER_PROPERTIES, "copper_ore"
    ));

    /**
     * The plant set instance for tin ore from Simple Ores.
     */
    public static final PlantSet<?> SIMPLE_ORES_TIN = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            SIMPLE_ORES, "tin", TIN_PROPERTIES, "tin_ore"
    ));

    /**
     * The plant set instance for mythril ore from Simple Ores.
     */
    public static final PlantSet<?> SIMPLE_ORES_MYTHRIL = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            SIMPLE_ORES, "mythril", MYTHRIL_PROPERTIES, "mythril_ore"
    ));

    /**
     * The plant set instance for adamantium ore from Simple Ores.
     */
    public static final PlantSet<?> SIMPLE_ORES_ADAMANTIUM = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            SIMPLE_ORES, "adamantium", ADAMANTIUM_PROPERTIES, "adamantium_ore"
    ));

    //More Ores in ONE

    /**
     * The plant set for ruby ore from More Ores in ONE.
     */
    public static final PlantSet<?> MORE_ORES_RUBY = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            MORE_ORES_IN_ONE, "ruby", RUBY_PROPERTIES, "ruby_ore"
    ));

    /**
     * The plant set for sapphire ore from More Ores in ONE.
     */
    public static final PlantSet<?> MORE_ORES_SAPPHIRE = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            MORE_ORES_IN_ONE, "sapphire", SAPPHIRE_PROPERTIES, "sapphire_ore"
    ));

    /**
     * The plant set for topaz ore from More Ores in ONE.
     */
    public static final PlantSet<?> MORE_ORES_TOPAZ = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            MORE_ORES_IN_ONE, "topaz", TOPAZ_PROPERTIES, "topaz_ore"
    ));

    /**
     * The plant set for amethyst ore from More Ores in ONE.
     */
    public static final PlantSet<?> MORE_ORES_AMETHYST = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            MORE_ORES_IN_ONE, "amethyst", AMETHYST_PROPERTIES, "amethyst_ore"
    ));

    //Just Another Ruby Mod

    /**
     * The plant set for ruby from the Just Another Ruby Mod.
     */
    public static final PlantSet<?> JUST_ANOTHER_RUBY_MOD_RUBY
            = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            JUST_ANOTHER_RUBY_MOD, "ruby", RUBY_PROPERTIES, "ruby_ore"
    ));

    /**
     * The plant set for opal from the Just Another Ruby Mod.
     */
    public static final PlantSet<?> JUST_ANOTHER_RUBY_MOD_OPAL
            = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            JUST_ANOTHER_RUBY_MOD, "opal", OPAL_PROPERTIES, "opal_ore"
    ));

    //Blue Power

    /**
     * The plant set for amethyst from the Blue Power mod.
     */
    public static final PlantSet<?> BLUE_POWER_AMETHYST = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            BLUE_POWER, "amethyst", AMETHYST_PROPERTIES, "amethyst_ore"
    ));

    /**
     * The plant set for ruby from the Blue Power mod.
     */
    public static final PlantSet<?> BLUE_POWER_RUBY = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            BLUE_POWER, "ruby", RUBY_PROPERTIES, "ruby_ore"
    ));

    /**
     * The plant set for sapphire from the Blue Power mod.
     */
    public static final PlantSet<?> BLUE_POWER_SAPPHIRE = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            BLUE_POWER, "sapphire", SAPPHIRE_PROPERTIES, "sapphire_ore"
    ));

    /**
     * The plant set for malachite from the Blue Power mod.
     */
    public static final PlantSet<?> BLUE_POWER_MALACHITE = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            BLUE_POWER, "malachite", MALACHITE_PROPERTIES, "malachite_ore"
    ));

    /**
     * The plant set for teslatite from the Blue Power mod.
     */
    public static final PlantSet<?> BLUE_POWER_TESLATITE = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            BLUE_POWER, "teslatite", TESLATITE_PROPERTIES, "teslatite_ore"
    ));

    /**
     * The plant set for copper from the Blue Power mod.
     */
    public static final PlantSet<?> BLUE_POWER_COPPER = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            BLUE_POWER, "copper", COPPER_PROPERTIES, "copper_ore"
    ));

    /**
     * The plant set for silver from the Blue Power mod.
     */
    public static final PlantSet<?> BLUE_POWER_SILVER = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            BLUE_POWER, "silver", SILVER_PROPERTIES, "silver_ore"
    ));

    /**
     * The plant set for zinc from the Blue Power mod.
     */
    public static final PlantSet<?> BLUE_POWER_ZINC = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            BLUE_POWER, "zinc", ZINC_PROPERTIES, "zinc_ore"
    ));

    /**
     * The plant set for tungsten from the Blue Power mod.
     */
    public static final PlantSet<?> BLUE_POWER_TUNGSTEN = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            BLUE_POWER, "tungsten", TUNGSTEN_PROPERTIES, "tungsten_ore"
    ));

    //Basic Nether Ores

    /**
     * The plant set for nether iron from Basic Nether Ores.
     */
    public static final PlantSet<?> BASIC_NETHER_ORES_IRON = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            BASIC_NETHER_ORES, "iron", IRON_PROPERTIES, "netheriron_ore"
    ));

    /**
     * The plant set for nether gold from Basic Nether Ores.
     */
    public static final PlantSet<?> BASIC_NETHER_ORES_GOLD = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            BASIC_NETHER_ORES, "gold", GOLD_PROPERTIES, "nethergold_ore"
    ));

    /**
     * The plant set for nether coal from Basic Nether Ores.
     */
    public static final PlantSet<?> BASIC_NETHER_ORES_COAL = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            BASIC_NETHER_ORES, "coal", COAL_PROPERTIES, "nethercoal_ore"
    ));

    /**
     * The plant set for nether lapis lazuli from Basic Nether Ores.
     */
    public static final PlantSet<?> BASIC_NETHER_ORES_LAPIS_LAZULI
            = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            BASIC_NETHER_ORES, "lapis_lazuli", LAPIS_LAZULI_PROPERTIES, "netherlapis_ore"
    ));

    /**
     * The plant set for nether redstone from Basic Nether Ores.
     */
    public static final PlantSet<?> BASIC_NETHER_ORES_REDSTONE
            = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            BASIC_NETHER_ORES, "redstone", REDSTONE_PROPERTIES, "netherredstone_ore"
    ));

    /**
     * The plant set for nether diamond from Basic Nether Ores.
     */
    public static final PlantSet<?> BASIC_NETHER_ORES_DIAMOND
            = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            BASIC_NETHER_ORES, "diamond", DIAMOND_PROPERTIES, "netherdiamond_ore"
    ));

    /**
     * The plant set for nether emerald from Basic Nether Ores.
     */
    public static final PlantSet<?> BASIC_NETHER_ORES_EMERALD
            = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            BASIC_NETHER_ORES, "emerald", EMERALD_PROPERTIES, "netheremerald_ore"
    ));

    /**
     * The plant set for nether uranium for Basic Nether Ores.
     */
    public static final PlantSet<?> BASIC_NETHER_ORES_URANIUM = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            BASIC_NETHER_ORES, "uranium", URANIUM_PROPERTIES, "netheruranium_ore"
    ));

    /**
     * The plant set for nether tin for Basic Nether Ores.
     */
    public static final PlantSet<?> BASIC_NETHER_ORES_TIN = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            BASIC_NETHER_ORES, "tin", TIN_PROPERTIES, "nethertin_ore"
    ));

    /**
     * The plant set for nether silver for Basic Nether Ores.
     */
    public static final PlantSet<?> BASIC_NETHER_ORES_SILVER = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            BASIC_NETHER_ORES, "silver", SILVER_PROPERTIES, "nethersilver_ore"
    ));

    /**
     * The plant set for nether nickel for Basic Nether Ores.
     */
    public static final PlantSet<?> BASIC_NETHER_ORES_NICKEL = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            BASIC_NETHER_ORES, "nickel", NICKEL_PROPERTIES, "nethernickel_ore"
    ));

    /**
     * The plant set for nether lead for Basic Nether Ores.
     */
    public static final PlantSet<?> BASIC_NETHER_ORES_LEAD = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            BASIC_NETHER_ORES, "lead", LEAD_PROPERTIES, "netherlead_ore"
    ));

    /**
     * The plant set for nether copper for Basic Nether Ores.
     */
    public static final PlantSet<?> BASIC_NETHER_ORES_COPPER = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            BASIC_NETHER_ORES, "copper", COPPER_PROPERTIES, "nethercopper_ore"
    ));

    /**
     * The plant set for nether aluminium for Basic Nether Ores.
     */
    public static final PlantSet<?> BASIC_NETHER_ORES_ALUMINIUM
            = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            BASIC_NETHER_ORES, "aluminium", ALUMINIUM_PROPERTIES, "netheraluminum_ore"
    ));

    //Mekanism

    /**
     * The plant set for Osmium from Mekanism.
     */
    public static final PlantSet<?> MEKANISM_OSMIUM
            = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            MEKANISM, "osmium", OSMIUM_PROPERTIES, "osmium_ore"
    ));

    /**
     * The plant set for Copper from Mekanism.
     */
    public static final PlantSet<?> MEKANISM_COPPER
            = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            MEKANISM, "copper", COPPER_PROPERTIES, "copper_ore"
    ));

    /**
     * The plant set for Tin from Mekanism.
     */
    public static final PlantSet<?> MEKANISM_TIN
            = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            MEKANISM, "tin", TIN_PROPERTIES, "tin_ore"
    ));

    //Mystical Agriculture

    /**
     * The plant set for Prosperity from Mystical Agriculture.
     */
    public static final PlantSet<?> MYSTICAL_AGRICULTURE_PROSPERITY
            = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            MYSTICAL_AGRICULTURE, "prosperity", PROSPERITY_PROPERTIES, "prosperity_ore"
    ));

    /**
     * The plant set for Inferium from Mystical Agriculture.
     */
    public static final PlantSet<?> MYSTICAL_AGRICULTURE_INFERIUM
            = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            MYSTICAL_AGRICULTURE, "inferium", INFERIUM_PROPERTIES, "inferium_ore"
    ));

    /**
     * The plant set for Soulium from Mystical Agriculture.
     */
    public static final PlantSet<?> MYSTICAL_AGRICULTURE_SOULIUM
            = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            MYSTICAL_AGRICULTURE, "soulium", SOULIUM_PROPERTIES, "soulium_ore"
    ));

    /**Private constructor.*/
    private ResynthModdedPlants(){}

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
    private static PlantSet<?> registerIfNotNull(PlantSet<?> set){
        if(set == null)
            return null;

        return set.register();
    }
}
