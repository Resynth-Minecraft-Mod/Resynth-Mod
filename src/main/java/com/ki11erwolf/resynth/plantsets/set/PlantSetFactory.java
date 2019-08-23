package com.ki11erwolf.resynth.plantsets.set;

import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.CrystallinePlantSetConfig;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * Used to create and obtain references to
 * the various Resynth plant sets.
 */
public class PlantSetFactory {

    /**Private constructor.*/
    private PlantSetFactory(){}

    /**
     * Creates a new Crystalline plant set for a Vanilla Minecraft resource.
     *
     * @param setName the name of the plant set (e.g. diamond).
     * @param properties the properties (e.g. growth chance) of the plant set.
     * @param sourceOre the source ore (from which seeds are obtained) for the set.
     * @return the newly created set. Must still be registered using ({@link PlantSet#register()})!
     */
    public static PlantSet newVanillaCrystallineSet(String setName, CrystallineSetProperties properties,
                                                    Block sourceOre){
        CrystallinePlantSetConfig config = ResynthConfig.VANILLA_PLANTS_CONFIG.loadCategory(
                new CrystallinePlantSetConfig(setName, properties)
        );

        return new CrystallineSet(setName, config, new ItemStack(sourceOre));
    }
}

