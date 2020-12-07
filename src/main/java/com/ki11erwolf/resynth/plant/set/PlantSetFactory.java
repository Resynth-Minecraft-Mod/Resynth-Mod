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
import com.ki11erwolf.resynth.config.ConfigFile;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.BiochemicalPlantSetConfig;
import com.ki11erwolf.resynth.config.categories.CrystallinePlantSetConfig;
import com.ki11erwolf.resynth.config.categories.MetallicPlantSetConfig;
import com.ki11erwolf.resynth.plant.block.BlockBiochemicalPlant;
import com.ki11erwolf.resynth.plant.block.BlockCrystallinePlant;
import com.ki11erwolf.resynth.plant.block.BlockMetallicPlant;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    // Vanilla factory methods

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

    // Modded factory methods

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
        //Check for mod presence
        if(!ensureModLoaded(modid, setName)) return null;

        ResourceLocation sourceOreRL = new ResourceLocation(modid, sourceOreRegistryName);
        LOG.info("Attempting to create modded crystalline plant set: " + modid + ":" + setName);

        //Config
        CrystallinePlantSetConfig config = ResynthConfig.MODDED_PLANTS_CONFIG.loadCategory(
                new CrystallinePlantSetConfig(modid + "-" + setName, properties)
        );

        //Plant set
        return new CrystallineSet(modid + "_" + setName, config) {
            private ItemStack sourceOre;

            @Override
            ItemStack getSourceOre() {
                if(sourceOre != null)
                    return sourceOre;

                if((sourceOre = getRegistryBlock(sourceOreRL)) == null) {
                    LOG.error("Failed to get Mod Ore Block: " + sourceOreRL.toString());
                    this.flagAsFailure();
                }

                return sourceOre;
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
                                                   String sourceOreRegistryName) {
        //Check for mod presence
        if(!ensureModLoaded(modid, setName)) return null;

        ResourceLocation sourceOreRL = new ResourceLocation(modid, sourceOreRegistryName);
        LOG.info("Attempting to create modded Metallic plant set: " + modid + ":" + setName);

        //Config
        MetallicPlantSetConfig config = ResynthConfig.MODDED_PLANTS_CONFIG.loadCategory(
                new MetallicPlantSetConfig(modid + "-" + setName, properties)
        );

        //Plant set
        return new MetallicSet(modid + "_" + setName, config) {
            private ItemStack sourceOreStack = null;

            @Override
            ItemStack getSourceOre() {
                if(sourceOreStack != null)
                    return sourceOreStack;

                if((sourceOreStack = getRegistryBlock(sourceOreRL)) == null) {
                    LOG.error("Failed to get Mod Ore Block: " + sourceOreRL.toString());
                    this.flagAsFailure();
                }

                return sourceOreStack;
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
    public static PlantSet<?> newModdedBiochemicalSet(String modid, String setName, BiochemicalSetProperties properties,
                                                      String... sourceEntityRegistryNames) {
        //Check for mod presence
        if(!ensureModLoaded(modid, setName))
            return null;

        LOG.info("Attempting to create modded Biochemical plant set: " + modid + ":" + setName);

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

    // ###################
    // Public Set Creators
    // ###################

    //Crystalline

    public static PlantSet<? extends BlockCrystallinePlant> makeCrystallineSet(
            ResourceLocation id, CrystallineSetProperties config, String oreBlockName) {
        return createCrystallineSet(
                validate(id), Objects.requireNonNull(config),
                new ResourceLocation(id.getNamespace(), Objects.requireNonNull(oreBlockName))
        );
    }

    public static PlantSet<? extends BlockCrystallinePlant> makeCrystallineSet(
            ResourceLocation id, CrystallineSetProperties config, Block oreBlock) {
        return createCrystallineSet(
                validate(id), Objects.requireNonNull(config), Objects.requireNonNull(oreBlock).getRegistryName()
        );
    }

    public static PlantSet<? extends BlockCrystallinePlant> makeCrystallineSet(
            ResourceLocation id, CrystallineSetProperties config, ResourceLocation oreBlockID) {
        return createCrystallineSet(validate(id), Objects.requireNonNull(config), validate(oreBlockID));
    }

    // Metallic

    public static PlantSet<? extends BlockMetallicPlant> makeMetallicSet(
            ResourceLocation id, MetallicSetProperties config, String oreBlockName) {
        return createMetallicSet(
                validate(id), Objects.requireNonNull(config),
                new ResourceLocation(id.getNamespace(), Objects.requireNonNull(oreBlockName))
        );
    }

    public static PlantSet<? extends BlockMetallicPlant> makeMetallicSet(
            ResourceLocation id, MetallicSetProperties config, Block oreBlock) {
        return createMetallicSet(
                validate(id), Objects.requireNonNull(config), Objects.requireNonNull(oreBlock).getRegistryName()
        );
    }

    public static PlantSet<? extends BlockMetallicPlant> makeMetallicSet(
            ResourceLocation id, MetallicSetProperties config, ResourceLocation oreBlockID) {
        return createMetallicSet(validate(id), Objects.requireNonNull(config), validate(oreBlockID));
    }

    // Biochemical

    public static PlantSet<? extends BlockBiochemicalPlant> makeBiochemicalSet(
            ResourceLocation id, BiochemicalSetProperties config, String... entityNames) {
        //Convert to ResourceLocations
        List<ResourceLocation> entityIDs = new ArrayList<>();
        for(String entityName : Objects.requireNonNull(entityNames)) {
            entityIDs.add(new ResourceLocation(id.getNamespace(), Objects.requireNonNull(entityName)));
        }

        return createBiochemicalSet(
                validate(id), Objects.requireNonNull(config), entityIDs.isEmpty() ? null : entityIDs.toArray(new ResourceLocation[0])
        );
    }

    public static PlantSet<? extends BlockBiochemicalPlant> makeBiochemicalSet(
            ResourceLocation id, BiochemicalSetProperties config, EntityType<?>... entities) {
        //Convert to ResourceLocations
        List<ResourceLocation> entityIDs = new ArrayList<>();
        for(EntityType<?> entity : Objects.requireNonNull(entities)) {
            entityIDs.add(Objects.requireNonNull(entity).getRegistryName());
        }

        return createBiochemicalSet(
                validate(id), Objects.requireNonNull(config), entityIDs.isEmpty() ? null : entityIDs.toArray(new ResourceLocation[0])
        );
    }

    public static PlantSet<? extends BlockBiochemicalPlant> makeBiochemicalSet(
            ResourceLocation id, BiochemicalSetProperties config, ResourceLocation... entityIDs) {
        //Simply validate
        for(ResourceLocation entityID : Objects.requireNonNull(entityIDs))
            validate(entityID);

        return createBiochemicalSet(validate(id), Objects.requireNonNull(config), entityIDs);
    }

    // #####################
    // Internal Set Creators
    // #####################

    // Crystalline

    @SuppressWarnings("DuplicatedCode")
    private static PlantSet<? extends BlockCrystallinePlant> createCrystallineSet(
            ResourceLocation id, CrystallineSetProperties config, ResourceLocation oreBlockID) {
        String setName = isVanilla(id) ? id.getPath() : combine(id, '_');
        String parentModID = id.getNamespace();

        //Check for required mods
        if(!isParentLoaded(id)) {
            logSkippedSet(setName, parentModID);
            return null;
        } else if (!isParentLoaded(oreBlockID)) {
            logSkippedSet(setName, oreBlockID.getNamespace());
            return null;
        }

        //Get player config
        ICrystallineSetProperties properties = getCrystallineProperties(id, config);

        //Create & return new crystalline set
        LOG.info(String.format("Creating Crystalline plant set: '%s' for mod: '%s'.", parentModID, setName));
        return createNewCrystallineSet(setName, properties, oreBlockID);
    }

    private static CrystallinePlantSetConfig getCrystallineProperties(ResourceLocation id, CrystallineSetProperties config) {
        boolean vanilla = isVanilla(id);
        String setName = vanilla ? id.getPath() : combine(id, '-');

        ConfigFile configFile = vanilla ? ResynthConfig.VANILLA_PLANTS_CONFIG : ResynthConfig.MODDED_PLANTS_CONFIG;
        return configFile.loadCategory(new CrystallinePlantSetConfig(setName, config));
    }

    private static PlantSet<? extends BlockCrystallinePlant> createNewCrystallineSet(
            String name, ICrystallineSetProperties config, ResourceLocation oreBlockID) {
        return new CrystallineSet(name, config) {
            private ItemStack ore;

            @Override
            ItemStack getSourceOre() {
                if(ore != null)
                    return ore;

                return ore = getSetSeedBlock(this, oreBlockID);
            }
        };
    }

    // Metallic

    @SuppressWarnings("DuplicatedCode")
    private static PlantSet<? extends BlockMetallicPlant> createMetallicSet(
            ResourceLocation id, MetallicSetProperties config, ResourceLocation oreBlockID) {
        String setName = isVanilla(id) ? id.getPath() : combine(id, '_');
        String parentModID = id.getNamespace();

        //Check for required mods
        if(!isParentLoaded(id)) {
            logSkippedSet(setName, parentModID);
            return null;
        } else if (!isParentLoaded(oreBlockID)) {
            logSkippedSet(setName, oreBlockID.getNamespace());
            return null;
        }

        //Get player config
        IMetallicSetProperties properties = getMetallicProperties(id, config);

        //Create & return new crystalline set
        LOG.info(String.format("Creating Metallic plant set: '%s' for mod: '%s'.", setName, parentModID));
        return createNewMetallicSet(setName, properties, oreBlockID);
    }

    private static MetallicPlantSetConfig getMetallicProperties(ResourceLocation id, MetallicSetProperties config) {
        boolean vanilla = isVanilla(id);
        String setName = vanilla ? id.getPath() : combine(id, '-');

        ConfigFile configFile = vanilla ? ResynthConfig.VANILLA_PLANTS_CONFIG : ResynthConfig.MODDED_PLANTS_CONFIG;
        return configFile.loadCategory(new MetallicPlantSetConfig(setName, config));
    }

    private static PlantSet<? extends BlockMetallicPlant> createNewMetallicSet(
            String name, IMetallicSetProperties config, ResourceLocation oreBlockID) {
        return new MetallicSet(name, config) {
            private ItemStack ore;

            @Override
            ItemStack getSourceOre() {
                if(ore != null)
                    return ore;

                return ore = getSetSeedBlock(this, oreBlockID);
            }
        };
    }

    // Biochemical

    @SuppressWarnings("DuplicatedCode")
    private static PlantSet<? extends BlockBiochemicalPlant> createBiochemicalSet(
            ResourceLocation id, BiochemicalSetProperties config, ResourceLocation... entityIDs) {
        String setName = isVanilla(id) ? id.getPath() : combine(id, '_');
        String parentModID = id.getNamespace();

        //Check for required mods
        if(!isParentLoaded(id)) {
            logSkippedSet(setName, parentModID);
            return null;
        }

        for(ResourceLocation entityID : entityIDs) {
            if (!isParentLoaded(entityID)) {
                logSkippedSet(setName, entityID.getNamespace());
                return null;
            }
        }

        //Get player config
        IBiochemicalSetProperties properties = getBiochemicalProperties(id, config);

        //Create & return new crystalline set
        LOG.info(String.format("Creating Biochemical plant set: '%s' for mod: '%s'.", setName, parentModID));
        return createNewBiochemicalSet(setName, properties, entityIDs);
    }

    private static BiochemicalPlantSetConfig getBiochemicalProperties(ResourceLocation id, BiochemicalSetProperties config) {
        boolean vanilla = isVanilla(id);
        String setName = vanilla ? id.getPath() : combine(id, '-');

        ConfigFile configFile = vanilla ? ResynthConfig.VANILLA_PLANTS_CONFIG : ResynthConfig.MODDED_PLANTS_CONFIG;
        return configFile.loadCategory(new BiochemicalPlantSetConfig(setName, config));
    }


    private static PlantSet<? extends BlockBiochemicalPlant> createNewBiochemicalSet(
            String name, IBiochemicalSetProperties config, ResourceLocation... entityIDs) {
        return new BiochemicalSet(name, config) {
            private EntityType<?>[] entities;

            @Override
            EntityType<?>[] getSourceMobs() {
                if(entities != null)
                    return entities;

                return entities = initialize();
            }

            private EntityType<?>[] initialize() {
                List<EntityType<?>> tempEntities = new ArrayList<>();

                for(ResourceLocation entityID : entityIDs) {
                    EntityType<?> entity = getSetSeedEntity(this, entityID);

                    if(entity == null)
                        return null;
                    else tempEntities.add(entity);
                }

                return tempEntities.toArray(new EntityType<?>[0]);
            }
        };
    }

    // ############
    // General Util
    // ############

    private static void logSkippedSet(String setName, String missingModID) {
        LOG.info(String.format(
                "Skipping plant set '%s' because required mod '%s' is not present.",
                setName, missingModID
        ));
    }

    private static ItemStack getSetSeedBlock(PlantSet<?> set, ResourceLocation blockID) {
        ItemStack ore = getRegistryBlock(blockID);
        if(ore == null || ore.isEmpty() || ore.getItem() == Items.AIR) {
            set.flagAsFailure();
            LOG.error(String.format(
                    "Could not get the seed block '%s' for the plant set '%s'",
                    blockID.toString(), set.toString()
            ));

            return null;
        }

        return ore;
    }

    private static ItemStack getRegistryBlock(ResourceLocation blockID) {
        if(ForgeRegistries.BLOCKS.containsKey(blockID)) {
            Block block = ForgeRegistries.BLOCKS.getValue(blockID);
            if (block != null && block != Blocks.AIR) {
                return new ItemStack(block);
            }
        }

        return null;
    }

    private static EntityType<?> getSetSeedEntity(PlantSet<?> set, ResourceLocation entityID) {
        EntityType<?> entity = getRegistryEntity(entityID);
        if(entity == null || entity.getClassification() == EntityClassification.MISC) {
            set.flagAsFailure();
            LOG.error(String.format(
                    "Could not get the seed entity '%s' for the plant set '%s'",
                    entityID.toString(), set.toString()
            ));

            return null;
        }

        return entity;
    }

    private static EntityType<?> getRegistryEntity(ResourceLocation entityID) {
        if(ForgeRegistries.ENTITIES.containsKey(entityID)) {
            return ForgeRegistries.ENTITIES.getValue(entityID);
        }

        return null;
    }

    private static ResourceLocation validate(ResourceLocation id) throws NullPointerException, IllegalArgumentException {
        if(Objects.requireNonNull(id).getPath().isEmpty())
            throw new IllegalArgumentException("Invalid ResourceLocation ID! The path cannot be empty");

        return id;
    }

    private static String combine(ResourceLocation id, char separator) {
        return Objects.requireNonNull(id).getNamespace() + separator + id.getPath();
    }

    private static boolean isVanilla(ResourceLocation resourceLocation) {
        String namespace = Objects.requireNonNull(resourceLocation).getNamespace();
        return namespace.isEmpty() || namespace.equals("minecraft") || namespace.equals(ResynthMod.MODID);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isParentLoaded(ResourceLocation plantSetID) {
        return isVanilla(plantSetID) || ModList.get().isLoaded(plantSetID.getNamespace());
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean ensureModLoaded(String modid, String plantSetName) {
        if(ModList.get().isLoaded(modid)) {
            LOG.info("Mod: " + modid + " is present! Creating plant set: " + plantSetName);
            return true;
        }

        LOG.info("Mod: " + modid + " is not present! Skipping plant set: " + plantSetName);
        return false;
    }
}
