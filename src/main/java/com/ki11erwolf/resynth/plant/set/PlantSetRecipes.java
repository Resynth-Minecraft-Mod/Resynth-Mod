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
import com.ki11erwolf.resynth.ResynthRecipes;
import com.ki11erwolf.resynth.plant.block.BlockCrystallinePlant;
import com.ki11erwolf.resynth.block.DamagedBlock;
import com.ki11erwolf.resynth.plant.set.properties.AbstractProduceProperties;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import java.util.*;

enum PlantSetRecipes implements ResynthRecipes.RecipeProvider {

    /**
     * The singleton instance of this class.
     */
    INSTANCE;

    /**
     * The logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    private Queue<RecipeDefinition<?>> recipeDefinitions = new ArrayDeque<>();

    private IRecipe<?>[] finalRecipes;

    // ###################
    // Package private API
    // ###################

    private void addRecipe(RecipeDefinition<?> recipe) {
        if(recipeDefinitions == null) throw new IllegalStateException(
                "Cannot add any more plant set recipes! The recipes have already been finalized"
        );

        recipeDefinitions.add(Objects.requireNonNull(recipe));
    }

    void addCrystallineSeedsRecipe(PlantSet<? extends BlockCrystallinePlant, Block> set, ResourceLocation outputItemID, int count) {
        String recipeType = set.getSetTypeName() + "-plant-set-seeds-to-resource";
        ResourceLocation recipeID = new ResourceLocation(ResynthMod.MODID, set.getSetName() + "-" + recipeType);

        addRecipe(new ShapelessRecipeDefinition(
                set, recipeID, recipeType, outputItemID, 2, set.getSeedsItem().getRegistryName()
        ));
    }

    void addProduceRecipe(PlantSet<?, ?> set, ResourceLocation outputItemID, AbstractProduceProperties properties) {
        addProduceRecipe(set, outputItemID, properties.produceYield(), properties.timePerYield(), properties.experiencePoints());
    }

    void addProduceRecipe(PlantSet<?, ?> set, ResourceLocation outputItemID, int count, int time, double experience) {
        String recipeType = set.getSetTypeName() + "-plant-set-produce-smelting";
        ResourceLocation recipeID = new ResourceLocation(ResynthMod.MODID, set.getSetName() + "-" + recipeType);

        ResourceLocation setProduce;

        if(set.getProduceItem() instanceof Block)
            setProduce = ((Block)set.getProduceItem()).getRegistryName();
        else if (set.getProduceItem() instanceof Item) {
            setProduce = ((Item)set.getProduceItem()).getRegistryName();
        } else throw new IllegalArgumentException("The PlantSet '" + set.getSetName() + "' provided an invalid produce item!");

        addRecipe(new FurnaceRecipeDefinition(
                set, recipeID, recipeType, setProduce, outputItemID, count, experience, time
        ));
    }

    void addWeakOreRecipe(PlantSet<?, ?> set, DamagedBlock<?> weakOre) {
        String recipeType = "weak-ore-recipe";
        ResourceLocation toRecipeID = new ResourceLocation(ResynthMod.MODID, set.getSetName() + "-to-" + recipeType);
        ResourceLocation fromRecipeID = new ResourceLocation(ResynthMod.MODID, set.getSetName() + "-from-" + recipeType);

        addRecipe(new ShapelessRecipeDefinition(
                set, toRecipeID, recipeType, weakOre.getRegistryName(), 1, weakOre.original.getRegistryName()
        ));

        addRecipe(new ShapelessRecipeDefinition(
                set, fromRecipeID, recipeType, weakOre.original.getRegistryName(), 1, weakOre.getRegistryName()
        ));
    }

    // #########
    // Get logic
    // #########

    @Override
    public IRecipe<?>[] get() {
        return getFinalRecipes();
    }

    private IRecipe<?>[] getFinalRecipes() {
        if(recipeDefinitions == null)
            return finalRecipes;

        return finalizeRecipes();
    }

    private IRecipe<?>[] finalizeRecipes() {
        //Create recipes from definitions
        IRecipe<?>[] recipes = initializeRecipes();

        //Null out after use - signals finalization
        recipeDefinitions = null;

        //Check for issues
        if(recipes == null) {
            LOG.warn("No defined plant set recipes have been added...");
        } else if(recipes.length == 0){
            LOG.error("Zero recipes were initialized successfully...");
            return finalRecipes = null;
        }

        //Set created recipes as final and return
        return finalRecipes = recipes;
    }
    
    private IRecipe<?>[] initializeRecipes() {
        List<IRecipe<?>> recipeList = new ArrayList<>();

        if(recipeDefinitions.size() != 0){
            RecipeDefinition<?> nextRecipe;

            //Create and store all recipes
            while((nextRecipe = recipeDefinitions.poll()) != null) {
                //Check if PlantSet is broken
                if(nextRecipe.getPlantSet().isBroken()){
                    LOG.warn("Skipping recipe '" + nextRecipe.getRecipeID()
                            + "' because the PlantSet is flagged as broken!");
                    continue;
                }

                //Attempt to get actual recipe
                try {
                    recipeList.add(nextRecipe.getRecipe());
                } catch (MissingResourceException e) {
                    LOG.error("Failed to create recipe during finalization", e);
                    nextRecipe.getPlantSet().flagAsBroken();
                }
            }
            
            return recipeList.toArray(new IRecipe<?>[0]);
        }

        return null;
    }

    // ############
    // Lazy Recipes
    // ############

    private static class ShapelessRecipeDefinition implements RecipeDefinition<ShapelessRecipe> {

        private final PlantSet<?, ?> plantSet;

        private final ResourceLocation id;

        private final String group;

        private final ResourceLocation[] inputItemIDs;

        private final ResourceLocation outputItemID;

        private final int count;

        protected ShapelessRecipeDefinition(PlantSet<?, ?> plantSet, ResourceLocation id, String group, ResourceLocation outputItemID,
                                            int count, ResourceLocation... inputItemIDs) {
            this.plantSet = Objects.requireNonNull(plantSet);
            this.id = Objects.requireNonNull(id);
            this.group = Objects.requireNonNull(group);
            this.inputItemIDs = Objects.requireNonNull(inputItemIDs);
            this.outputItemID = Objects.requireNonNull(outputItemID);
            this.count = count < 0 ? 1 : Math.min(count, 64); // Don't allow more than 64
        }

        @Override
        public ShapelessRecipe getRecipe() throws MissingResourceException {
            IItemProvider output = RecipeDefinition.getItem(outputItemID);
            if(output == null)
                RecipeDefinition.errorWithMissingResource(id, outputItemID);

            List<IItemProvider> inputItems = new ArrayList<>();

            for(ResourceLocation inputItemID : inputItemIDs) {
                IItemProvider input;
                if((input = RecipeDefinition.getItem(inputItemID)) == null)
                    RecipeDefinition.errorWithMissingResource(id, inputItemID);

                inputItems.add(input);
            }

            return ResynthRecipes.RecipeProvider.newShapelessRecipe(
                    id, group, new ItemStack(output, count), inputItems.toArray(new IItemProvider[0])
            );
        }

        @Override
        public PlantSet<?, ?> getPlantSet() {
            return this.plantSet;
        }

        @Override
        public ResourceLocation getRecipeID() {
            return this.id;
        }
    }

    private static class FurnaceRecipeDefinition implements RecipeDefinition<FurnaceRecipe> {

        private final PlantSet<?, ?> plantSet;

        private final ResourceLocation id;

        private final String group;

        private final ResourceLocation inputItemID;

        private final ResourceLocation outputItemID;

        private final int count;

        private final double experience;

        private final int time;

        protected FurnaceRecipeDefinition(PlantSet<?, ?> plantSet, ResourceLocation id, String group, ResourceLocation inputItemID,
                                          ResourceLocation outputItemID, int count, double experience, int time) {
            this.plantSet = Objects.requireNonNull(plantSet);
            this.id = Objects.requireNonNull(id);
            this.group = Objects.requireNonNull(group);
            this.inputItemID = Objects.requireNonNull(inputItemID);
            this.outputItemID = Objects.requireNonNull(outputItemID);
            this.count = count < 0 ? 1 : Math.min(count, 64); // Don't allow more than 64
            this.experience = experience > 0 ? experience : 0.8;
            this.time = time > 0 ? time : 50;
        }

        @Override
        public FurnaceRecipe getRecipe() throws MissingResourceException {
            IItemProvider inputItem = RecipeDefinition.getItem(inputItemID);
            IItemProvider outputItem = RecipeDefinition.getItem(outputItemID);

            if(inputItem == null)
                RecipeDefinition.errorWithMissingResource(id, inputItemID);

            if(outputItem == null)
                RecipeDefinition.errorWithMissingResource(id, outputItemID);

            return ResynthRecipes.RecipeProvider.newFurnaceRecipe(
                    id, group, experience, time, new ItemStack(outputItem, count), inputItem
            );
        }

        @Override
        public PlantSet<?, ?> getPlantSet() {
            return this.plantSet;
        }

        @Override
        public ResourceLocation getRecipeID() {
            return this.id;
        }
    }

    private interface RecipeDefinition<T extends IRecipe<?>> {

        T getRecipe() throws MissingResourceException;

        PlantSet<?, ?> getPlantSet();

        ResourceLocation getRecipeID();

        static IItemProvider getItem(ResourceLocation itemID) {
            IItemProvider provider;

            if(ForgeRegistries.BLOCKS.containsKey(itemID)) {
                provider = ForgeRegistries.BLOCKS.getValue(itemID);
                if (provider != null && provider != Blocks.AIR)
                    return provider;
            }

            if(ForgeRegistries.ITEMS.containsKey(itemID)){
                provider = ForgeRegistries.ITEMS.getValue(itemID);
                if(provider != null && provider != Items.AIR)
                    return provider;
            }

            return null;
        }

        static void errorWithMissingResource(ResourceLocation recipeID, ResourceLocation resourceID) {
            throw new MissingResourceException(
                    String.format(
                            "Cannot create recipe: '%s'! Required item: '%s' is not registered",
                            recipeID.toString(), resourceID.toString()
                    ),
                    recipeID.toString(), resourceID.toString()
            );
        }
    }
}
