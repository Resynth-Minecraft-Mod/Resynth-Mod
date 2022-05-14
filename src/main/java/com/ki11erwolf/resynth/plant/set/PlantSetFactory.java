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
package com.ki11erwolf.resynth.plant.set;

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.config.ConfigFile;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.*;
import com.ki11erwolf.resynth.plant.block.BlockBiochemicalPlant;
import com.ki11erwolf.resynth.plant.block.BlockCrystallinePlant;
import com.ki11erwolf.resynth.plant.block.BlockMetallicPlant;
import com.ki11erwolf.resynth.plant.set.properties.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Objects;

//TODO: Document
@SuppressWarnings("unused")
public class PlantSetFactory {

    /**
     * The logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    /**Private constructor.*/
    private PlantSetFactory(){}

    // ###################
    // Public Set Creators
    // ###################

    //Crystalline

    public static PlantSet<? extends BlockCrystallinePlant, Block> makeCrystallineSet(
            ResourceLocation id, CrystallineProperties defaultConfig, ProduceProperties defaultProduceConfig,
            ResourceLocation outputItemID, ResourceLocation... oreBlockIDs) {
        return createCrystallineSet(
                validate(id), Objects.requireNonNull(defaultConfig), Objects.requireNonNull(defaultProduceConfig),
                validate(outputItemID), Objects.requireNonNull(oreBlockIDs)
        );
    }

    public static PlantSet<? extends BlockCrystallinePlant, Block> makeCrystallineSet(
            ResourceLocation id, CrystallineProperties defaultConfig, ProduceProperties produceConfig,
            IItemProvider outputItem, Block... oreBlocks) {
        //Convert to ResourceLocations
        List<ResourceLocation> oreIDs = new ArrayList<>();
        for(Block block : Objects.requireNonNull(oreBlocks)) {
            oreIDs.add(Objects.requireNonNull(block).getRegistryName());
        }

        return createCrystallineSet(
                validate(id), Objects.requireNonNull(defaultConfig), Objects.requireNonNull(produceConfig),
                Objects.requireNonNull(outputItem).asItem().getRegistryName(), oreIDs.toArray(new ResourceLocation[0])
        );
    }

    public static PlantSet<? extends BlockCrystallinePlant, Block> makeCrystallineSet(
            ResourceLocation id, CrystallineProperties defaultConfig, ProduceProperties produceConfig,
            String outputItemName, String... oreBlockNames) {
        //Convert to ResourceLocations
        List<ResourceLocation> oreIDs = new ArrayList<>();
        for(String ore : Objects.requireNonNull(oreBlockNames)) {
            oreIDs.add(new ResourceLocation(id.getNamespace(), Objects.requireNonNull(ore)));
        }

        return createCrystallineSet(
                validate(id), Objects.requireNonNull(defaultConfig), Objects.requireNonNull(produceConfig),
                 new ResourceLocation(
                        id.getNamespace(), Objects.requireNonNull(outputItemName)
                ), Objects.requireNonNull(oreIDs).toArray(new ResourceLocation[0])
        );
    }

    public static PlantSet<? extends BlockCrystallinePlant, Block> makeCrystallineSet(
            ResourceLocation id, CrystallineProperties defaultConfig, String... oreBlockNames) {
        //Convert to ResourceLocations
        List<ResourceLocation> oreIDs = new ArrayList<>();
        for(String ore : Objects.requireNonNull(oreBlockNames)) {
            oreIDs.add(new ResourceLocation(id.getNamespace(), Objects.requireNonNull(ore)));
        }

        return createCrystallineSet(
                validate(id), Objects.requireNonNull(defaultConfig),
                oreIDs.toArray(new ResourceLocation[0])
        );
    }

    public static PlantSet<? extends BlockCrystallinePlant, Block> makeCrystallineSet(
            ResourceLocation id, CrystallineProperties defaultConfig, Block... oreBlocks) {
        //Convert to ResourceLocations
        List<ResourceLocation> oreIDs = new ArrayList<>();
        for(Block ore : Objects.requireNonNull(oreBlocks)) {
            oreIDs.add(ore.getRegistryName());
        }

        return createCrystallineSet(
                validate(id), Objects.requireNonNull(defaultConfig), oreIDs.toArray(new ResourceLocation[0])
        );
    }

    public static PlantSet<? extends BlockCrystallinePlant, Block> makeCrystallineSet(
            ResourceLocation id, CrystallineProperties defaultConfig, ResourceLocation... oreBlockIDs) {
        return createCrystallineSet(validate(id), Objects.requireNonNull(defaultConfig), Objects.requireNonNull(oreBlockIDs));
    }

    // Metallic

    public static PlantSet<? extends BlockMetallicPlant, Block> makeMetallicSet(
            ResourceLocation id, MetallicProperties defaultConfig, ProduceProperties produceConfig,
            ResourceLocation outputItemID, ResourceLocation... oreBlockIDs) {
        return createMetallicSet(
                validate(id), Objects.requireNonNull(defaultConfig), Objects.requireNonNull(produceConfig),
                validate(outputItemID), Objects.requireNonNull(oreBlockIDs)
        );
    }

    public static PlantSet<? extends BlockMetallicPlant, Block> makeMetallicSet(
            ResourceLocation id, MetallicProperties defaultConfig, ProduceProperties produceConfig,
            IItemProvider outputItem, Block... oreBlocks) {
        //Convert to ResourceLocations
        List<ResourceLocation> oreIDs = new ArrayList<>();
        for(Block ore : Objects.requireNonNull(oreBlocks)) {
            oreIDs.add(ore.getRegistryName());
        }

        //Create and return set
        return createMetallicSet(
                validate(id), Objects.requireNonNull(defaultConfig), Objects.requireNonNull(produceConfig),
                Objects.requireNonNull(outputItem).asItem().getRegistryName(), oreIDs.toArray(new ResourceLocation[0])
        );
    }

    public static PlantSet<? extends BlockMetallicPlant, Block> makeMetallicSet(
            ResourceLocation id, MetallicProperties defaultConfig, ProduceProperties produceConfig,
            String oreBlockName, String outputItemName) {
        return createMetallicSet(
                validate(id), Objects.requireNonNull(defaultConfig), Objects.requireNonNull(produceConfig),
                new ResourceLocation(
                        id.getNamespace(), Objects.requireNonNull(outputItemName)
                ), new ResourceLocation(
                        id.getNamespace(), Objects.requireNonNull(oreBlockName)
                )
        );
    }

    public static PlantSet<? extends BlockMetallicPlant, Block> makeMetallicSet(
            ResourceLocation id, MetallicProperties defaultConfig, String oreBlockName) {
        return createMetallicSet(
                validate(id), Objects.requireNonNull(defaultConfig),
                new ResourceLocation(id.getNamespace(), Objects.requireNonNull(oreBlockName))
        );
    }

    public static PlantSet<? extends BlockMetallicPlant, Block> makeMetallicSet(
            ResourceLocation id, MetallicProperties defaultConfig, Block... oreBlocks) {
        //Convert to ResourceLocations
        List<ResourceLocation> oreIDs = new ArrayList<>();
        for(Block ore : Objects.requireNonNull(oreBlocks)) {
            oreIDs.add(ore.getRegistryName());
        }

        return createMetallicSet(
                validate(id), Objects.requireNonNull(defaultConfig), oreIDs.toArray(new ResourceLocation[0])
        );
    }

    public static PlantSet<? extends BlockMetallicPlant, Block> makeMetallicSet(
            ResourceLocation id, MetallicProperties defaultConfig, ResourceLocation... oreBlockIDs) {
        return createMetallicSet(validate(id), Objects.requireNonNull(defaultConfig), Objects.requireNonNull(oreBlockIDs));
    }

    // Biochemical

    public static PlantSet<? extends BlockBiochemicalPlant, EntityType<?>> makeBiochemicalSet(
            ResourceLocation id, BiochemicalProperties defaultConfig, ResourceLocation outputItemID,
            ProduceProperties produceConfig, ResourceLocation... entityIDs) {
        return createBiochemicalSet(
                validate(id), Objects.requireNonNull(defaultConfig), validate(outputItemID),
                Objects.requireNonNull(produceConfig), Objects.requireNonNull(entityIDs)
        );
    }

    public static PlantSet<? extends BlockBiochemicalPlant, EntityType<?>> makeBiochemicalSet(
            ResourceLocation id, BiochemicalProperties defaultConfig, ProduceProperties produceConfig,
            IItemProvider outputItem, EntityType<?>... entities) {
        //Convert to ResourceLocations
        List<ResourceLocation> entityIDs = new ArrayList<>();
        for(EntityType<?> entity : Objects.requireNonNull(entities)) {
            entityIDs.add(Objects.requireNonNull(entity).getRegistryName());
        }

        return createBiochemicalSet(
                validate(id), Objects.requireNonNull(defaultConfig), Objects.requireNonNull(outputItem).asItem().getRegistryName(),
                Objects.requireNonNull(produceConfig), entityIDs.toArray(new ResourceLocation[0])
        );
    }

    public static PlantSet<? extends BlockBiochemicalPlant, EntityType<?>> makeBiochemicalSet(
            ResourceLocation id, BiochemicalProperties defaultConfig, ProduceProperties produceConfig,
            String outputItemName, String... entityNames) {
        //Convert to ResourceLocations
        List<ResourceLocation> entityIDs = new ArrayList<>();
        for(String entityName : Objects.requireNonNull(entityNames)) {
            entityIDs.add(new ResourceLocation(id.getNamespace(), Objects.requireNonNull(entityName)));
        }

        return createBiochemicalSet(
                validate(id), Objects.requireNonNull(defaultConfig),
                new ResourceLocation(id.getNamespace(), Objects.requireNonNull(outputItemName)),
                Objects.requireNonNull(produceConfig), entityIDs.toArray(new ResourceLocation[0])
        );
    }

    public static PlantSet<? extends BlockBiochemicalPlant, EntityType<?>> makeBiochemicalSet(
            ResourceLocation id, BiochemicalProperties defaultConfig, String... entityNames) {
        //Convert to ResourceLocations
        List<ResourceLocation> entityIDs = new ArrayList<>();
        for(String entityName : Objects.requireNonNull(entityNames)) {
            entityIDs.add(new ResourceLocation(id.getNamespace(), Objects.requireNonNull(entityName)));
        }

        return createBiochemicalSet(
                validate(id), Objects.requireNonNull(defaultConfig), entityIDs.isEmpty()
                        ? null : entityIDs.toArray(new ResourceLocation[0])
        );
    }

    public static PlantSet<? extends BlockBiochemicalPlant, EntityType<?>> makeBiochemicalSet(
            ResourceLocation id, BiochemicalProperties defaultConfig, EntityType<?>... entities) {
        //Convert to ResourceLocations
        List<ResourceLocation> entityIDs = new ArrayList<>();
        for(EntityType<?> entity : Objects.requireNonNull(entities)) {
            entityIDs.add(Objects.requireNonNull(entity).getRegistryName());
        }

        return createBiochemicalSet(
                validate(id), Objects.requireNonNull(defaultConfig), entityIDs.isEmpty()
                        ? null : entityIDs.toArray(new ResourceLocation[0])
        );
    }

    public static PlantSet<? extends BlockBiochemicalPlant, EntityType<?>> makeBiochemicalSet(
            ResourceLocation id, BiochemicalProperties defaultConfig, ResourceLocation... entityIDs) {
        //Simply validate
        for(ResourceLocation entityID : Objects.requireNonNull(entityIDs))
            validate(entityID);

        return createBiochemicalSet(validate(id), Objects.requireNonNull(defaultConfig), entityIDs);
    }

    // #####################
    // Internal Set Creators
    // #####################

    // Crystalline

    private static PlantSet<? extends BlockCrystallinePlant, Block> createCrystallineSet(
            ResourceLocation id, CrystallineProperties config, ProduceProperties produceConfig,
            ResourceLocation outputItemID, ResourceLocation... oreBlockIDs) {
        String setName = isVanilla(id) ? id.getPath() : combine(id, '_');

        //Check for additional required mod
        if(!isParentLoaded(outputItemID)) {
            logSkippedSet(setName, outputItemID.getNamespace());
            return null;
        }

        //Basic plant set
        PlantSet<? extends BlockCrystallinePlant, Block> plantSet;
        if((plantSet = createCrystallineSet(id, config, oreBlockIDs)) == null)
            return null;

        //Produce config...
        ProducePropertiesConfig produceProperties = getSetProduceConfig(id, produceConfig);
        plantSet.setProduceProperties(produceProperties);

        //Create & add produce recipe
        PlantSetRecipes.INSTANCE.addProduceRecipe(
                plantSet, outputItemID, produceProperties
        );

        //Create & add seeds recipe if count not 0
        int seedsRecipeCount = ((CrystallinePropertiesConfig) plantSet.getPlantSetProperties()).seedCraftingYield();
        if(seedsRecipeCount > 0) {
            seedsRecipeCount = Math.min(seedsRecipeCount, 64);

            if (ResynthConfig.GENERAL_CONFIG.getCategory(GeneralConfig.class).enableCraftingCrystallineSeeds()) {
                PlantSetRecipes.INSTANCE.addCrystallineSeedsRecipe(
                        plantSet, outputItemID, seedsRecipeCount
                );
            }
        }

        return plantSet;
    }

    @SuppressWarnings("DuplicatedCode")
    private static PlantSet<? extends BlockCrystallinePlant, Block> createCrystallineSet(
            ResourceLocation id, CrystallineProperties config, ResourceLocation... oreBlockIDs) {
        String setName = isVanilla(id) ? id.getPath() : combine(id, '_');
        String parentModID = id.getNamespace();

        //Check for required mods
        if(!isParentLoaded(id)) {
            logSkippedSet(setName, parentModID);
            return null;
        }

        for(ResourceLocation entityID : oreBlockIDs) {
            if (!isParentLoaded(entityID)) {
                logSkippedSet(setName, entityID.getNamespace());
                return null;
            }
        }

        //Get player config
        AbstractCrystallineProperties properties = getCrystallineProperties(id, config);

        //Create & return new crystalline set
        LOG.info(String.format("Creating Crystalline plant set: '%s' for mod: '%s'.", setName, parentModID));
        return createNewCrystallineSet(setName, properties, oreBlockIDs);
    }

    private static AbstractCrystallineProperties getCrystallineProperties(ResourceLocation id, CrystallineProperties config) {
        boolean vanilla = isVanilla(id);
        String setName = vanilla ? id.getPath() : combine(id, '-');

        ConfigFile configFile = vanilla ? ResynthConfig.VANILLA_PLANTS_CONFIG : ResynthConfig.MODDED_PLANTS_CONFIG;
        return configFile.loadCategory(new CrystallinePropertiesConfig(setName, config));
    }

    private static PlantSet<? extends BlockCrystallinePlant, Block> createNewCrystallineSet(
            String name, AbstractCrystallineProperties config, ResourceLocation... oreBlockIDs) {
        return new CrystallineSet(name, config) {
            @Override
            Block[] onSeedSourcesRequest() {
                List<Block> ores = new ArrayList<>();

                for(ResourceLocation entityID : oreBlockIDs) {
                    Block ore = getSetSeedBlock(this, entityID);

                    if(ore == null)
                        throw new MissingResourceException(String.format(
                                "Could not get Crystalline seed source entity '%s' for the PlantSet '%s'",
                                entityID.toString(), name
                        ), null, null);
                    else ores.add(ore);
                }

                return ores.toArray(new Block[0]);
            }
        };
    }

    // Metallic

    private static PlantSet<? extends BlockMetallicPlant, Block> createMetallicSet(
            ResourceLocation id, MetallicProperties config, ProduceProperties produceConfig,
            ResourceLocation outputItemID, ResourceLocation... oreBlockIDs) {
        String setName = isVanilla(id) ? id.getPath() : combine(id, '_');

        //Check for additional required mod
        if(!isParentLoaded(outputItemID)) {
            logSkippedSet(setName, outputItemID.getNamespace());
            return null;
        }

        //Basic plant set
        PlantSet<? extends BlockMetallicPlant, Block> plantSet;
        if((plantSet = createMetallicSet(id, config, oreBlockIDs)) == null)
            return null;

        //Produce config...
        ProducePropertiesConfig produceProperties = getSetProduceConfig(id, produceConfig);
        plantSet.setProduceProperties(produceProperties);

        //Registry recipe
        PlantSetRecipes.INSTANCE.addProduceRecipe(
                plantSet, outputItemID, produceProperties
        );

        return plantSet;
    }

    @SuppressWarnings("DuplicatedCode")
    private static PlantSet<? extends BlockMetallicPlant, Block> createMetallicSet(
            ResourceLocation id, MetallicProperties config, ResourceLocation... oreBlockIDs) {
        String setName = isVanilla(id) ? id.getPath() : combine(id, '_');
        String parentModID = id.getNamespace();

        //Check for required mods
        if(!isParentLoaded(id)) {
            logSkippedSet(setName, parentModID);
            return null;
        }

        for(ResourceLocation entityID : oreBlockIDs) {
            if (!isParentLoaded(entityID)) {
                logSkippedSet(setName, entityID.getNamespace());
                return null;
            }
        }

        //Get player config
        AbstractMetallicProperties properties = getMetallicProperties(id, config);

        //Create & return new crystalline set
        LOG.info(String.format("Creating Metallic plant set: '%s' for mod: '%s'.", setName, parentModID));
        return createNewMetallicSet(setName, properties, oreBlockIDs);
    }

    private static AbstractMetallicProperties getMetallicProperties(ResourceLocation id, MetallicProperties config) {
        boolean vanilla = isVanilla(id);
        String setName = vanilla ? id.getPath() : combine(id, '-');

        ConfigFile configFile = vanilla ? ResynthConfig.VANILLA_PLANTS_CONFIG : ResynthConfig.MODDED_PLANTS_CONFIG;
        return configFile.loadCategory(new MetallicPropertiesConfig(setName, config));
    }

    private static PlantSet<? extends BlockMetallicPlant, Block> createNewMetallicSet(
            String name, AbstractMetallicProperties config, ResourceLocation... oreBlockIDs) {
        return new MetallicSet(name, config) {
            @Override
            Block[] onSeedSourcesRequest() {
                List<Block> ores = new ArrayList<>();

                for(ResourceLocation entityID : oreBlockIDs) {
                    Block ore = getSetSeedBlock(this, entityID);

                    if(ore == null)
                        throw new MissingResourceException(String.format(
                                "Could not get Metallic seed source entity '%s' for the PlantSet '%s'",
                                entityID.toString(), name
                        ), null, null);
                    else
                        ores.add(ore);
                }

                return ores.toArray(new Block[0]);
            }
        };
    }

    // Biochemical

    private static PlantSet<? extends BlockBiochemicalPlant, EntityType<?>> createBiochemicalSet(
            ResourceLocation id, BiochemicalProperties config, ResourceLocation outputItemID,
            ProduceProperties produceConfig, ResourceLocation... entityIDs) {
        String setName = isVanilla(id) ? id.getPath() : combine(id, '_');

        //Check for additional required mod
        if(!isParentLoaded(outputItemID)) {
            logSkippedSet(setName, outputItemID.getNamespace());
            return null;
        }

        //Basic plant set
        PlantSet<? extends BlockBiochemicalPlant, EntityType<?>> plantSet;
        if((plantSet = createBiochemicalSet(id, config, entityIDs)) == null)
            return null;

        //Produce config...
        ProducePropertiesConfig produceProperties = getSetProduceConfig(id, produceConfig);
        plantSet.setProduceProperties(produceProperties);

        //Registry recipe
        PlantSetRecipes.INSTANCE.addProduceRecipe(
                plantSet, outputItemID, produceProperties
        );

        return plantSet;
    }

    @SuppressWarnings("DuplicatedCode")
    private static PlantSet<? extends BlockBiochemicalPlant, EntityType<?>> createBiochemicalSet(
            ResourceLocation id, BiochemicalProperties config, ResourceLocation... entityIDs) {
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
        AbstractBiochemicalProperties properties = getBiochemicalProperties(id, config);

        //Create & return new crystalline set
        LOG.info(String.format("Creating Biochemical plant set: '%s' for mod: '%s'.", setName, parentModID));
        return createNewBiochemicalSet(setName, properties, entityIDs);
    }

    private static BiochemicalPropertiesConfig getBiochemicalProperties(ResourceLocation id, BiochemicalProperties config) {
        boolean vanilla = isVanilla(id);
        String setName = vanilla ? id.getPath() : combine(id, '-');

        ConfigFile configFile = vanilla ? ResynthConfig.VANILLA_PLANTS_CONFIG : ResynthConfig.MODDED_PLANTS_CONFIG;
        return configFile.loadCategory(new BiochemicalPropertiesConfig(setName, config));
    }


    private static PlantSet<? extends BlockBiochemicalPlant, EntityType<?>> createNewBiochemicalSet(
            String name, AbstractBiochemicalProperties config,
            ResourceLocation... entityIDs) {
        return new BiochemicalSet(name, config) {
            @Override
            EntityType<?>[] onSeedSourcesRequest() {
                List<EntityType<?>> tempEntities = new ArrayList<>();

                for(ResourceLocation entityID : entityIDs) {
                    EntityType<?> entity = getSetSeedEntity(this, entityID);

                    if(entity == null)
                        throw new MissingResourceException(String.format(
                                "Could not get Biochemical seed source entity '%s' for the PlantSet '%s'",
                                entityID.toString(), name
                        ), null, null);
                    else tempEntities.add(entity);
                }

                return tempEntities.toArray(new EntityType<?>[0]);
            }
        };
    }

    // ############
    // General Util
    // ############

    private static ProducePropertiesConfig getSetProduceConfig(ResourceLocation id, AbstractProduceProperties produceConfig) {
        boolean vanilla = isVanilla(id);

        String categoryName = vanilla ? id.getPath() : combine(id, '-');
        ConfigFile configFile = vanilla ? ResynthConfig.VANILLA_PLANTS_CONFIG : ResynthConfig.MODDED_PLANTS_CONFIG;

        return configFile.loadCategory(new ProducePropertiesConfig(
                categoryName, produceConfig
        ));
    }

    private static void logSkippedSet(String setName, String missingModID) {
        LOG.info(String.format(
                "Skipping plant set '%s' because the required mod '%s' is not present.",
                setName, missingModID
        ));
    }

    private static Block getSetSeedBlock(PlantSet<?, ?> set, ResourceLocation blockID) {
        Block ore = getRegistryBlock(blockID);
        if(ore == null || ore == Blocks.AIR) {
            LOG.error(String.format(
                    "Could not get the seed block '%s' for the plant set '%s'",
                    blockID.toString(), set.toString()
            ));

            return null;
        }

        return ore;
    }

    private static Block getRegistryBlock(ResourceLocation blockID) {
        if(ForgeRegistries.BLOCKS.containsKey(blockID)) {
            Block block = ForgeRegistries.BLOCKS.getValue(blockID);
            if (block != null && block != Blocks.AIR) {
                return block;
            }
        }

        return null;
    }

    private static EntityType<?> getSetSeedEntity(PlantSet<?, ?> set, ResourceLocation entityID) {
        EntityType<?> entity = getRegistryEntity(entityID);
        if(entity == null || entity.getCategory() == EntityClassification.MISC) {
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
}

