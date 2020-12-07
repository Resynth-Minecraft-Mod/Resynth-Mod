package com.ki11erwolf.resynth.plant.set;

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.ResynthRecipes;
import com.ki11erwolf.resynth.plant.block.BlockCrystallinePlant;
import net.minecraft.block.Blocks;
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

    void addCrystallineSeedsRecipe(PlantSet<? extends BlockCrystallinePlant> set, ResourceLocation outputItemID, int count) {
        String recipeType = set.getSetTypeName() + "-plant-set-seeds-to-resource";
        ResourceLocation recipeID = new ResourceLocation(ResynthMod.MODID, set.getSetName() + "-" + recipeType);

        addRecipe(new ShapelessRecipeDefinition(recipeID, recipeType, outputItemID, 2, set.getSeedsItem().getRegistryName()));
    }

    void addProduceRecipe(PlantSet<?> set, ResourceLocation outputItemID, IPlantSetProduceProperties properties) {
        addProduceRecipe(set, outputItemID, properties.resourceCount(), properties.smeltingTime(), properties.experienceWorth());
    }

    void addProduceRecipe(PlantSet<?> set, ResourceLocation outputItemID, int count, int time, double experience) {
        String recipeType = set.getSetTypeName() + "-plant-set-produce-smelting";
        ResourceLocation recipeID = new ResourceLocation(ResynthMod.MODID, set.getSetName() + "-" + recipeType);

        addRecipe(new FurnaceRecipeDefinition(
                recipeID, recipeType, set.getProduceItem().asItem().getRegistryName(), outputItemID, count, experience, time
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
            LOG.error("Zero recipes were initialized sucessfully...");
            return finalRecipes = null;
        }

        //Set created recipes as final and return
        return finalRecipes = recipes;
    }
    
    private IRecipe<?>[] initializeRecipes() {
        List<IRecipe<?>> recipeList = new ArrayList<>();

        if(recipeDefinitions.size() != 0){
            RecipeDefinition<?> nextRecipe;

            //Create and store recipes
            while((nextRecipe = recipeDefinitions.poll()) != null) {
                try {
                    recipeList.add(nextRecipe.getRecipe());
                } catch (MissingResourceException e) {
                    LOG.error("Failed to create recipe during finalization", e);
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

        private final ResourceLocation id;

        private final String group;

        private final ResourceLocation[] inputItemIDs;

        private final ResourceLocation outputItemID;

        private final int count;

        protected ShapelessRecipeDefinition(ResourceLocation id, String group, ResourceLocation outputItemID,
                                            int count, ResourceLocation... inputItemIDs) {
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
    }

    private static class FurnaceRecipeDefinition implements RecipeDefinition<FurnaceRecipe> {

        private final ResourceLocation id;

        private final String group;

        private final ResourceLocation inputItemID;

        private final ResourceLocation outputItemID;

        private final int count;

        private final double experience;

        private final int time;

        protected FurnaceRecipeDefinition(ResourceLocation id, String group, ResourceLocation inputItemID,
                                          ResourceLocation outputItemID, int count, double experience, int time) {
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
    }

    private interface RecipeDefinition<T extends IRecipe<?>> {

        T getRecipe() throws MissingResourceException;

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
