package com.ki11erwolf.resynth.plant.set;

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.ResynthRecipes;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipe;
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

    private Queue<LazyRecipe<?>> recipeDefinitions = new ArrayDeque<>();

    private IRecipe<?>[] finalRecipes;

    // ###################
    // Package private API
    // ###################

    private void addRecipe(LazyRecipe<?> recipe) {
        if(recipeDefinitions == null) throw new IllegalStateException(
                "Cannot add any more plant set recipes! The recipes have already been finalized"
        );

        recipeDefinitions.add(Objects.requireNonNull(recipe));
    }

    void addProduceRecipe(PlantSet<?> set, ResourceLocation outputItemID, int count, int time, double experience) {
        String recipeType = set.getSetTypeName() + "-plant-set-produce-recipe";
        ResourceLocation recipeID = new ResourceLocation(ResynthMod.MODID, recipeType + "-" + set.getSetName());

        recipeDefinitions.add(new LazyFurnaceRecipe(
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
        //Check for recipes
        List<IRecipe<?>> recipeList = new ArrayList<>();
        if(recipeDefinitions.size() != 0){
            LazyRecipe<?> nextRecipe;

            //Create and store recipes
            while((nextRecipe = recipeDefinitions.poll()) != null) {
                try {
                    recipeList.add(nextRecipe.getRecipe());
                } catch (MissingResourceException e) {
                    LOG.error("Failed to create recipe during finalization", e);
                }
            }
        } else {
            LOG.warn("No plant set recipes have been defined...");
        }

        //Always null out the recipe definitions queue
        recipeDefinitions = null;

        //Check for created recipes
        if(recipeList.size() == 0){
            LOG.error("No plant set recipes could be created...");
            return finalRecipes = null;
        }

        //Set and return final recipes from created recipes
        return finalRecipes = recipeList.toArray(new IRecipe<?>[0]);
    }

    // ############
    // Lazy Recipes
    // ############

    private static class LazyFurnaceRecipe implements LazyRecipe<FurnaceRecipe> {

        private final ResourceLocation id;

        private final String group;

        private final ResourceLocation inputItemID;

        private final ResourceLocation outputItemID;

        private final int count;

        private final double experience;

        private final int time;

        protected LazyFurnaceRecipe(ResourceLocation id, String group, ResourceLocation inputItemID,
                                    ResourceLocation outputItemID, int count, double experience, int time) {
            this.id = Objects.requireNonNull(id);
            this.group = Objects.requireNonNull(group);
            this.inputItemID = Objects.requireNonNull(inputItemID);
            this.outputItemID = Objects.requireNonNull(outputItemID);
            this.count = count > 0 ? count : 1;
            this.experience = experience > 0 ? experience : 0.8;
            this.time = time > 0 ? time : 50;
        }

        @Override
        public FurnaceRecipe getRecipe() throws MissingResourceException {
            IItemProvider inputItem = LazyRecipe.getItem(inputItemID);
            IItemProvider outputItem = LazyRecipe.getItem(outputItemID);

            if(inputItem == null)
                LazyRecipe.missingResource(id, inputItemID);

            if(outputItem == null)
                LazyRecipe. missingResource(id, outputItemID);

            return ResynthRecipes.RecipeProvider.newFurnaceRecipe(
                    id, group, experience, time, new ItemStack(outputItem, count), inputItem
            );
        }
    }

    private interface LazyRecipe<T extends IRecipe<?>> {

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

        static void missingResource(ResourceLocation recipeID, ResourceLocation resourceID) {
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
