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

    // *******************
    // GENERAL DEFINITIONS
    // *******************

    // Crystalline

    /**
     * The set properties for general onyx gems and onyx ore.
     */
    private static final CrystallineSetProperties ONYX_PROPERTIES = new CrystallineSetProperties(
            false, 9, 1,
            7, 5
    );

    // Metallic

    /**
     * The set properties for general copper and copper ore.
     */
    private static final MetallicSetProperties COPPER_PROPERTIES = new MetallicSetProperties(
            false, 14, 7, 7
    );

    /**
     * The set properties for general tin and tin ore.
     */
    private static final MetallicSetProperties TIN_PROPERTIES = new MetallicSetProperties(
            false, 14, 7, 7
    );

    /**
     * The set properties for general mythril and mythril ore.
     */
    private static final MetallicSetProperties MYTHRIL_PROPERTIES = new MetallicSetProperties(
            false, 7, 5, 5
    );

    /**
     * The set properties for general adamantium and adamantium ore.
     */
    private static final MetallicSetProperties ADAMANTIUM_PROPERTIES = new MetallicSetProperties(
            false, 7, 5, 5
    );

    // **********
    // PLANT SETS
    // **********

    //Simple Ores

    /**
     * The plant set instance for onyx from Simple Ores.
     */
    public static final PlantSet SIMPLE_ORES_ONYX = registerIfNotNull(PlantSetFactory.newModdedCrystallineSet(
            SIMPLE_ORES, "onyx", ONYX_PROPERTIES, "onyx_ore"
    ));

    /**
     * The plant set instance for copper ore from Simple Ores.
     */
    public static final PlantSet SIMPLE_ORES_COPPER = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            SIMPLE_ORES, "copper", COPPER_PROPERTIES, "copper_ore"
    ));

    /**
     * The plant set instance for tin ore from Simple Ores.
     */
    public static final PlantSet SIMPLE_ORES_TIN = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            SIMPLE_ORES, "tin", TIN_PROPERTIES, "tin_ore"
    ));

    /**
     * The plant set instance for mythril ore from Simple Ores.
     */
    public static final PlantSet SIMPLE_ORES_MYTHRIL = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            SIMPLE_ORES, "mythril", MYTHRIL_PROPERTIES, "mythril_ore"
    ));

    /**
     * The plant set instance for adamantium ore from Simple Ores.
     */
    public static final PlantSet SIMPLE_ORES_ADAMANTIUM = registerIfNotNull(PlantSetFactory.newModdedMetallicSet(
            SIMPLE_ORES, "adamantium", ADAMANTIUM_PROPERTIES, "adamantium_ore"
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
    private static PlantSet registerIfNotNull(PlantSet set){
        if(set == null)
            return null;

        return set.register();
    }
}
