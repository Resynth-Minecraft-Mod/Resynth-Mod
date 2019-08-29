package com.ki11erwolf.resynth.plantsets.set;

import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.BiochemicalPlantSetConfig;
import com.ki11erwolf.resynth.config.categories.CrystallinePlantSetConfig;
import com.ki11erwolf.resynth.config.categories.MetallicPlantSetConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
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

        return new CrystallineSet(setName, config){
            private ItemStack sourceOreStack = null;

            @Override
            ItemStack getSourceOre() {
                if(sourceOreStack == null)
                    sourceOreStack = new ItemStack(sourceOre);

                return sourceOreStack;
            }
        };
    }

    /**
     * Creates a new Metallic plant set for a Vanilla Minecraft resource.
     *
     * @param setName the name of the plant set (e.g. iron).
     * @param properties the properties (e.g. growth chance) of the plant set.
     * @param sourceOre the source ore (from which seeds are obtained) for the set.
     * @return the newly created set. Must still be registered using ({@link PlantSet#register()})!
     */
    public static PlantSet newVanillaMetallicPlantSet(String setName, MetallicSetProperties properties,
                                                      Block sourceOre){
        MetallicPlantSetConfig config = ResynthConfig.VANILLA_PLANTS_CONFIG.loadCategory(
                new MetallicPlantSetConfig(setName, properties)
        );

        return new MetallicSet(setName, config) {
            private ItemStack sourceOreStack = null;

            @Override
            ItemStack getSourceOre() {
                if(sourceOreStack == null)
                    sourceOreStack = new ItemStack(sourceOre);

                return sourceOreStack;
            }
        };
    }

    /**
     * Creates a new Biochemical plant set for a Vanilla Minecraft resource.
     *
     * @param setName the name of the plant set (e.g. string).
     * @param properties the properties (e.g. growth chance) of the plant set.
     * @param sourceMobs the list of mobs seeds can be obtained from.
     * @return the newly created set. Must still be registered using ({@link PlantSet#register()})!
     */
    public static PlantSet newVanillaBiochemicalPlantSet(String setName, BiochemicalSetProperties properties,
                                                         EntityType... sourceMobs){
        BiochemicalPlantSetConfig config = ResynthConfig.VANILLA_PLANTS_CONFIG.loadCategory(
                new BiochemicalPlantSetConfig(setName, properties)
        );

        return new BiochemicalSet(setName, config) {
            private EntityType[] sourceMobEntities = null;

            @Override
            EntityType[] getSourceMobs() {
                if(sourceMobEntities == null)
                    sourceMobEntities = sourceMobs;

                return sourceMobEntities;
            }
        };
    }
}

