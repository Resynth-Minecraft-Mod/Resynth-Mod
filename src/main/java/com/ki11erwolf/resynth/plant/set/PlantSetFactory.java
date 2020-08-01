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
package com.ki11erwolf.resynth.plant.set;

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.BiochemicalPlantSetConfig;
import com.ki11erwolf.resynth.config.categories.CrystallinePlantSetConfig;
import com.ki11erwolf.resynth.config.categories.MetallicPlantSetConfig;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to create and obtain references to
 * the various Resynth plant sets.
 */
public class PlantSetFactory {

    /**
     * The logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

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
    public static PlantSet<?> newVanillaCrystallineSet(String setName, CrystallineSetProperties properties,
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
     * Handles creating a Crystalline plant set for an ore
     * block that isn't from Vanilla Minecraft - that is,
     * a block that we don't have a reference to.
     * <p/>
     * This method of registration allows us to check for
     * the presence of the mod before creating the plant
     * set and allows us to provide a block name instead
     * of a block reference.
     *
     * @param modid the modid of the mod the ore block is from.
     * @param setName the name of the plant set (actual name will
     *                have mod-id prepended).
     * @param properties the plant set properties.
     * @param sourceOreRegistryName the registry name of the source
     *                           ore block.
     * @return the newly created plant set.
     */
    public static PlantSet<?> newModdedCrystallineSet(String modid, String setName, CrystallineSetProperties properties,
                                                   String sourceOreRegistryName){
        ResourceLocation sourceOreRL = new ResourceLocation(modid, sourceOreRegistryName);
        LOG.info("Attempting to create modded crystalline plant set: " + modid + ":" + setName);

        //Is mod loaded
        if(ModList.get().isLoaded(modid)){
            LOG.info("Mod: " + modid + " is present! Continuing...");
        } else {
            LOG.info("Mod: " + modid + " is not present! Skipping plant set: " + setName);
            return null;
        }

        //Config
        CrystallinePlantSetConfig config = ResynthConfig.MODDED_PLANTS_CONFIG.loadCategory(
                new CrystallinePlantSetConfig(modid + "-" + setName, properties)
        );

        //Plant set
        //noinspection DuplicatedCode
        return new CrystallineSet(modid + "_" + setName, config) {
            private ItemStack sourceOreStack = null;

            @Override
            ItemStack getSourceOre() {
                if(sourceOreStack != null)
                    return sourceOreStack;

                Block sourceOre = ForgeRegistries.BLOCKS.getValue(sourceOreRL);

                //Failed to get modded block...
                if(!ForgeRegistries.BLOCKS.containsKey(sourceOreRL) || sourceOre == null  || sourceOre == Blocks.AIR){
                    LOG.error("Failed to get source ore: " + sourceOreRL.toString());
                    this.flagAsFailure();
                } else {
                    LOG.info("Found source ore: " + sourceOreRL.toString());
                    sourceOreStack = new ItemStack(sourceOre);
                }

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
    public static PlantSet<?> newVanillaMetallicPlantSet(String setName, MetallicSetProperties properties,
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
     * Handles creating a Metallic plant set for an ore
     * block that isn't from Vanilla Minecraft - that is,
     * a block that we don't have a reference to.
     * <p/>
     * This method of registration allows us to check for
     * the presence of the mod before creating the plant
     * set and allows us to provide a block name instead
     * of a block reference.
     *
     * @param modid the modid of the mod the ore block is from.
     * @param setName the name of the plant set (actual name will
     *                have mod-id prepended).
     * @param properties the plant set properties.
     * @param sourceOreRegistryName the registry name of the source
     *                           ore block.
     * @return the newly created plant set.
     */
    public static PlantSet<?> newModdedMetallicSet(String modid, String setName, MetallicSetProperties properties,
                                                   String sourceOreRegistryName){
        ResourceLocation sourceOreRL = new ResourceLocation(modid, sourceOreRegistryName);
        LOG.info("Attempting to create modded Metallic plant set: " + modid + ":" + setName);

        //Is mod loaded
        if(ModList.get().isLoaded(modid)){
            LOG.info("Mod: " + modid + " is present! Continuing...");
        } else {
            LOG.info("Mod: " + modid + " is not present! Skipping plant set: " + setName);
            return null;
        }

        //Config
        MetallicPlantSetConfig config = ResynthConfig.MODDED_PLANTS_CONFIG.loadCategory(
                new MetallicPlantSetConfig(modid + "-" + setName, properties)
        );

        //Plant set
        //noinspection DuplicatedCode
        return new MetallicSet(modid + "_" + setName, config) {
            private ItemStack sourceOreStack = null;

            @Override
            ItemStack getSourceOre() {
                if(sourceOreStack != null)
                    return sourceOreStack;

                Block sourceOre = ForgeRegistries.BLOCKS.getValue(sourceOreRL);

                //Failed to get modded block...
                if(!ForgeRegistries.BLOCKS.containsKey(sourceOreRL) || sourceOre == null || sourceOre == Blocks.AIR){
                    LOG.error("Failed to get source ore: " + sourceOreRL.toString());
                    this.flagAsFailure();
                } else {
                    LOG.info("Found source ore: " + sourceOreRL.toString());
                    sourceOreStack = new ItemStack(sourceOre);
                }

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
    public static PlantSet<?> newVanillaBiochemicalPlantSet(String setName, BiochemicalSetProperties properties,
                                                         EntityType<?>... sourceMobs){
        BiochemicalPlantSetConfig config = ResynthConfig.VANILLA_PLANTS_CONFIG.loadCategory(
                new BiochemicalPlantSetConfig(setName, properties)
        );

        return new BiochemicalSet(setName, config) {
            private EntityType<?>[] sourceMobEntities = null;

            @Override
            EntityType<?>[] getSourceMobs() {
                if(sourceMobEntities == null)
                    sourceMobEntities = sourceMobs;

                return sourceMobEntities;
            }
        };
    }

    /**
     * Handles creating a Biochemical plant set for an entity
     * that isn't from Vanilla Minecraft - that is,
     * an entity that we don't have a reference to.
     * <p/>
     * This method of registration allows us to check for
     * the presence of the mod before creating the plant
     * set and allows us to provide an entity name instead
     * of an entity reference.
     *
     * @param modid the modid of the mod the ore block is from.
     * @param setName the name of the plant set (actual name will
     *                have mod-id prepended).
     * @param properties the plant set properties.
     * @param sourceEntityRegistryNames the registry names of the source
     *                           entities.
     * @return the newly created plant set.
     */
    @SuppressWarnings("unused")
    public static PlantSet<?> newModdedBiochemicalSet(String modid, String setName, BiochemicalSetProperties properties,
                                                      String... sourceEntityRegistryNames){
        LOG.info("Attempting to create modded Biochemical plant set: " + modid + ":" + setName);

        //Is mod loaded
        if(ModList.get().isLoaded(modid)){
            LOG.info("Mod: " + modid + " is present! Continuing...");
        } else {
            LOG.info("Mod: " + modid + " is not present! Skipping plant set: " + setName);
            return null;
        }

        //Config
        BiochemicalPlantSetConfig config = ResynthConfig.MODDED_PLANTS_CONFIG.loadCategory(
                new BiochemicalPlantSetConfig(modid + "-" + setName, properties)
        );

        //Plant set
        return new BiochemicalSet(modid + "_" + setName, config) {
            private List<EntityType<?>> sourceEntities;

            @Override
            EntityType<?>[] getSourceMobs() {
                if(sourceEntities != null)
                    return sourceEntities.toArray(new EntityType[0]);

                //Look for entities.
                LOG.info("Looking for source entities for plant set: " + this.getSetName());
                for(String registryName : sourceEntityRegistryNames){
                    ResourceLocation entityRL = new ResourceLocation(modid, registryName);
                    LOG.info("Looking for source entity: " + entityRL.toString());

                    EntityType<?> entity = ForgeRegistries.ENTITIES.getValue(entityRL);

                    if(ForgeRegistries.ENTITIES.containsKey(entityRL) && entity != null){
                        LOG.info("Found source entity: " + entity.getName().getUnformattedComponentText());
                        if(sourceEntities == null) sourceEntities = new ArrayList<>();
                        sourceEntities.add(entity);
                    } else {
                        LOG.error("Failed to find source entity: " + entityRL);
                    }
                }

                if(sourceEntities == null){
                    LOG.error("Failed to find any source entities for plant set:" + this.getSetName());
                    flagAsFailure();
                }

                return null;
            }
        };
    }
}
