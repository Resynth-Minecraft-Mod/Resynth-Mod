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
package com.ki11erwolf.resynth;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.GeneralConfig;
import com.ki11erwolf.resynth.item.ResynthItems;
import com.ki11erwolf.resynth.plant.set.PlantSetAPI;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.*;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The global manager and handler for all custom {@link IRecipe Recipes}
 * created by Resynth programmatically, as opposed to Recipes defined
 * through assets. Allows registering {@link RecipeProvider RecipeProviders}
 * that are queried for lists of Recipes to register to the game.
 */
@SuppressWarnings("deprecation") // We have to use this specific listener, even if deprecated.
public enum ResynthRecipes implements IResourceManagerReloadListener {

    /**
     * The singleton instance of the recipe manager
     * for the whole of the Resynth mod.
     *
     * <p/> Provided in the constructor is the list
     * of Resynths recipe providers.
     */
    INSTANCE(// List of custom RecipeProviders
        PlantSetAPI.getPlantSetRecipes(), CoreRecipes.INSTANCE
    );

    // Core recipes provider

    /**
     * The global {@link RecipeProvider} that provides the
     * core recipes for Resynth.
     */
    private enum CoreRecipes implements RecipeProvider {
        /** Singleton instance */ INSTANCE;

        // Configurable Flags

        /**
         * <b>WARNING: Hard-coded flag allowing the forced registration of {@link
         * #addResourceRecipes(List) recipes for uncraftable resource}.</b>
         */ //TODO: Disable when features are back.
        private static final boolean ENSURE_RESOURCE_RECIPES = false;

        // Configurable Recipe Definitions

        /**
         * The {@link IRecipe Recipe} instance that will craft Mineral Rocks
         * using a standard alternative recipe.
         */
        private static final IRecipe<?> MINERAL_ROCK_ALT_RECIPE = ResynthRecipes.RecipeProvider.newShapelessRecipe(
                "special_mineral_rock", "Resynth's Resources",
                new ItemStack(ResynthItems.ITEM_MINERAL_ROCK, 6), Items.IRON_INGOT, Items.IRON_INGOT,
                Items.INK_SAC, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE
        );

        /**
         * The {@link IRecipe Recipe} instance that will craft Calvinite Crystals
         * using a standard alternative recipe.
         */
        private static final IRecipe<?> CALVINITE_CRYSTAL_ALT_RECIPE = ResynthRecipes.RecipeProvider.newShapelessRecipe(
                "special_calvinite_crystal", "Resynth's Resources",
                new ItemStack(ResynthItems.ITEM_CALVINITE, 2), Items.QUARTZ, Items.QUARTZ,
                Items.INK_SAC, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE
        );

        /**
         * The {@link IRecipe Recipe} instance that will craft Sylvanite Crystals
         * using a standard alternative recipe.
         */
        private static final IRecipe<?> SYLVANITE_CRYSTAL_ALT_RECIPE = ResynthRecipes.RecipeProvider.newShapelessRecipe(
                "special_sylvanite_crystal", "Resynth's Resources",
                new ItemStack(ResynthItems.ITEM_SYLVANITE, 6), Items.DIAMOND, Items.END_STONE,
                Items.INK_SAC, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE
        );

        /**
         * The {@link IRecipe Recipe} instance that will craft Mineral Rocks
         * Seeds using a standard alternative recipe.
         */
        private static final IRecipe<?> MINERAL_ROCK_SEEDS_ALT_RECIPE = ResynthRecipes.RecipeProvider.newShapelessRecipe(
                "special_mineral_rock_seeds", "Resynth's Resources",
                new ItemStack(ResynthPlants.MINERAL_ROCKS.getSeedsItem(), 1), Items.IRON_INGOT, Items.IRON_INGOT,
                Items.INK_SAC, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE, Items.EMERALD, Items.WHEAT_SEEDS
        );

        /**
         * The {@link IRecipe Recipe} instance that will craft Calvinite Crystals
         * Seeds using a standard alternative recipe.
         */
        private static final IRecipe<?> CALVINITE_CRYSTAL_SEEDS_ALT_RECIPE = ResynthRecipes.RecipeProvider.newShapelessRecipe(
                "special_calvinite_crystal_seeds", "Resynth's Resources",
                new ItemStack(ResynthPlants.CALVINITE_CRYSTAL.getSeedsItem(), 1), Items.QUARTZ, Items.QUARTZ,
                Items.INK_SAC, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE, Items.EMERALD, Items.MELON_SEEDS
        );

        // Config

        /**
         * Convenient reference to the {@link GeneralConfig#enableResourceRecipes()}
         * config option.
         */
        private static final boolean ENABLE_RESOURCE_RECIPES = ResynthConfig.
                GENERAL_CONFIG.getCategory(GeneralConfig.class).enableResourceRecipes();

        // Class Impl

        /**
         * Holds, as a cache, the list of recipes to give {@link #get()}, once
         * generated.
         */
        private IRecipe<?>[] recipes;

        /**
         * If either {@link #ENABLE_RESOURCE_RECIPES} or {@link #ENSURE_RESOURCE_RECIPES}
         * flag is {@code true}, the special crafting recipes for Resynth's (normally
         * un-craftable) core resources will be added for registration.
         *
         * @param recipeContainer the list to add the Recipes object instances to.
         */
        private void addResourceRecipes(List<IRecipe<?>> recipeContainer) {
            if(!(ENABLE_RESOURCE_RECIPES || ENSURE_RESOURCE_RECIPES)) {
                return;
            }

            recipeContainer.add(MINERAL_ROCK_ALT_RECIPE);
            recipeContainer.add(MINERAL_ROCK_SEEDS_ALT_RECIPE);
            recipeContainer.add(CALVINITE_CRYSTAL_ALT_RECIPE);
            recipeContainer.add(CALVINITE_CRYSTAL_SEEDS_ALT_RECIPE);
            recipeContainer.add(SYLVANITE_CRYSTAL_ALT_RECIPE);
        }

        /**
         * Builds the {@link #recipes Recipes List} and makes sure it's cached.
         *
         * @return the built Recipes List.
         */
        private IRecipe<?>[] build() {
            List<IRecipe<?>> tempRecipes = new ArrayList<>();
            addResourceRecipes(tempRecipes);

            return (recipes = tempRecipes.toArray(new IRecipe<?>[0]));
        }

        /**
         * {@inheritDoc}
         *
         * @return the list of core crafting/smelting recipes
         * to register for Resynth.
         */
        @Override
        public IRecipe<?>[] get() {
            if(recipes == null){
                return build();
            }

            return recipes;
        }
    }

    // Class Implementation

    /**
     * The logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    /**
     * The list of {@link IRecipe Recipes} created by Resynth.
     * Used to cache the built list.
     */
    private final List<IRecipe<?>> recipes = new ArrayList<>();

    /**
     * The list of Resynth's custom {@link RecipeProvider RecipeProviders}.
     */
    private final RecipeProvider[] recipeProviders;

    /**
     * Minecraft's recipe manager - used to register all created
     * recipes into the game.
     */
    private RecipeManager recipeManager;

    /**
     * @param providers a list of all of Resynth's custom {@link RecipeProvider
     * RecipeProviders} to be queried for additional recipes.
     */
    ResynthRecipes(RecipeProvider... providers) {
        this.recipeProviders = Objects.requireNonNull(providers);
    }

    /**
     * Builds and caches {@link #recipes the complete list of Resynth's custom recipes}, from
     * the {@link IRecipe Recipes} provided by Resynth's {@link RecipeProvider RecipeProviders}
     * defined in the {@link #ResynthRecipes(RecipeProvider...) constructor}. Called this
     * method will always (re)build the list, however, it was meant to be, and should be, built
     * once only and cached.
     *
     * @return the newly (re)built {@link #recipes complete list of Resynth recipes}.
     */
    private List<IRecipe<?>> buildRecipes() {
        LOG.info("Building the list of Resynth's custom recipes...");

        // For each recipe provider
        for(RecipeProvider provider : recipeProviders) {
            IRecipe<?>[] providerRecipes = provider.get();

            // Check if provides a valid list of recipes
            if(providerRecipes == null || providerRecipes.length == 0){
                LOG.warn("Resynth RecipeProvider '" + provider.getClass().getCanonicalName() + "' provided no recipes!");
                continue;
            }

            // Iterate over provided recipes
            for(IRecipe<?> recipe : providerRecipes) {
                // Check recipe
                if(recipe == null){
                    LOG.warn("Resynth RecipeProvider '" + provider.getClass().getCanonicalName() + "' provided a NULL recipe!");
                    continue;
                }

                // Add recipe & log
                recipes.add(recipe);
                LOG.debug(String.format("Registered Resynth recipe: '%s' -> %s",
                    recipe.getId().toString(), (recipe.getRecipeOutput().getItem().getRegistryName() != null ?
                            recipe.getRecipeOutput().getItem().getRegistryName().toString() : "<NO REG NAME>")
                            + "(" + recipe.getRecipeOutput().getCount() + ")"
                ));
            }
        }

        return recipes;
    }

    /**
     * Obtains the {@link #recipes complete list of Resynth recipes} - which is either built,
     * constructed, and cached, or already cached.
     *
     * @return the built, possibly cached, {@link #recipes complete list of Resynth recipes}.
     */
    private List<IRecipe<?>> getRecipes() {
        if(recipes.size() != 0){
            return recipes;
        }

        return buildRecipes();
    }

    /**
     * Called when Minecraft/Forge wants Resynth to register all its {@link IRecipe Recipes}.
     *
     * <p/> Registers all custom Resynth Recipes to the game using the {@link #recipeManager}
     * instance from the {@link #recipes list of all Resynth recipes}, which is either built
     * if needed, or a cached version of an already built list.
     *
     * @param resourceManager the {@link sun.net.ResourceManager} provided by Minecraft.
     */
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        ResynthMod.getNewLogger().info("Injecting Resynth's custom recipes for plants & plant sets...");
        register(getRecipes());
    }

    /**
     * Registers the given {@link List} of {@link IRecipe Recipes} to the game, using
     * the {@link #recipeManager} object. All old Recipes already defined and registered
     * in the RecipeManager are kept and reregistered. Input list of Recipes is copied
     * before use, so input list is safe from modification.
     *
     * @throws IllegalStateException if any exception is thrown while reflectively
     * accessing or modifying Minecraft's internal recipes. Cause Exception is wrapped.
     * @param recipes a list of newly created {@link IRecipe Recipe} instances to be
     *                registered to the game. Don't copy existing recipes into the list.
     */
    private void register(List<IRecipe<?>> recipes) {
        Field recipesField = getRecipesField(); // Get recipes
        recipes = new ArrayList<>(recipes); // Make copy of input
        recipes.addAll(getRecipeManager().getRecipes()); // Copy over old recipes
        setRecipesField(recipesField, recipesToMap(recipes)); // Set game recipes
    }

    /**
     * Attempts to get the {@link #recipeManager}'s private internal {@link IRecipe
     * Recipe list} that holds the games Recipes.
     *
     * @throws IllegalStateException if any exception is thrown while reflectively
     * accessing or obtaining Minecraft's internal recipes. Cause Exception is wrapped.
     * @return {@code RecipeManager.recipes} as a {@link Field}.
     */
    private Field getRecipesField() {
        Field recipesField;

        // Get RecipeManager.recipes reference
        try {
            recipesField = RecipeManager.class.getDeclaredField("recipes");
            recipesField.setAccessible(true);
            Object fieldObjectRef = recipesField.get(getRecipeManager());

            if(!(fieldObjectRef instanceof Map)){
                LOG.fatal("Obtained 'RecipeManager.recipes' object not instance of Map! Cannot continue");
                throw new IllegalStateException("'RecipeManager.recipes' is not a valid Map implementation");
            }
        } catch (NoSuchFieldException e) {
            LOG.fatal("Failed to obtain 'RecipeManager.recipes' field reference! Cannot continue", e);
            throw new IllegalStateException("'RecipeManager.recipes' is not a valid, obtainable field", e);
        } catch (IllegalAccessException e) {
            LOG.fatal("Failed to obtain 'RecipeManager.recipes' object reference! Cannot continue", e);
            throw new IllegalStateException("'RecipeManager.recipes' is not obtainable", e);
        }

        return recipesField;
    }

    /**
     * Converts a given {@link List} of {@link IRecipe Recipes}, that are wanted
     * registered to the game, into a '{@code Map<IRecipeType<?>, Map<ResourceLocation,
     * IRecipe<?>>>}' object instance, containing all the Recipes provided, which can
     * be used by Minecraft's internal {@link RecipeManager}.
     *
     * @param recipes the list of {@link IRecipe Recipes} for registration.
     * @return the given list of {@link IRecipe Recipes}, contained in and converted to
     * an object type usable by Minecraft's internal {@link RecipeManager}.
     */
    private Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipesToMap(List<IRecipe<?>> recipes) {
        // Create copy
        Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> newRecipes = Maps.newHashMap();

        // Setup copy & Populate with recipes
        recipes.forEach((recipe) -> {
            Map<ResourceLocation, IRecipe<?>> map1 = newRecipes.computeIfAbsent(recipe.getType(), (type) -> Maps.newHashMap());
            IRecipe<?> irecipe = map1.put(recipe.getId(), recipe);

            if (irecipe != null) {
                throw new IllegalStateException("Duplicate recipe ignored with ID " + recipe.getId());
            }
        });

        return newRecipes;
    }

    /**
     * Attempts to set/change the {@link #recipeManager}'s private internal {@link IRecipe
     * Recipe list} that holds the games Recipes, to a new list of Recipes provided
     * - effectively registering the Recipes to the game. <b>Caution: it's possible
     * to completely remove pre-registered Recipes using this method.</b>
     *
     * @param recipesField the {@link Field} object instance pointing to the {@link
     * #recipeManager}'s internal Recipes list.
     * @param recipes the new set of all Recipes to be registered to the game.
     * @throws IllegalStateException if any exception is thrown while reflectively
     * accessing or modifying Minecraft's internal recipes. Cause Exception is wrapped.
     */
    private void setRecipesField(Field recipesField, Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes) {
        // Set - put copy in object reference
        try {
            recipesField.set(getRecipeManager(), ImmutableMap.copyOf(recipes));
        } catch (IllegalAccessException e) {
            LOG.fatal("Failed to modify 'RecipeManager.recipes' object reference! Cannot continue", e);
            throw new IllegalStateException("Could not modify 'RecipeManager.recipes' object reference", e);
        }

        // Attempt to reset the 'someRecipesErrored' flag. Failure not fatal.
        try {
            Field erroredField = RecipeManager.class.getDeclaredField("someRecipesErrored");
            erroredField.setAccessible(true);
            erroredField.set(getRecipeManager(), false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOG.error("Failed to reset 'RecipeManager.someRecipesErrored'! Continuing anyway...");
        }
    }

    /**
     * @return the {@link #recipeManager} used to register recipes to the game.
     * An {@link IllegalStateException} is thrown if the {@link #recipeManager}
     * is {@code null}.
     */
    private RecipeManager getRecipeManager() {
        if(recipeManager == null)
            throw new IllegalStateException("Attempted use of the RecipeManager before construction!");
        else return recipeManager;
    }

    /**
     * Called by forge to allow us to register this class.
     * Registers this ResourceReloadListener and sets the
     * {@link #recipeManager} instance.
     *
     * @param event event provided by forge providing the
     *              needed object instances.
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        LOG.debug("onAddReloadListeners() fired! Setting up RecipeManager & self.");

        recipeManager = event.getDataPackRegistries().getRecipeManager();
        event.addListener(this);
    }

    // Provider interface for custom RecipeProviders

    /**
     * Provides a hook and convenient utility methods for custom Resynth Recipe Provders.
     *
     * <p/> A RecipeProvider is an object instance that can be {@link #get() queried} for
     * a list of {@link IRecipe Recipes} to register to the game. RecipeProviders are
     * registered in  {@link ResynthRecipes#ResynthRecipes(RecipeProvider...) the
     * constructor ResynthRecipes enum constructor}.
     *
     * <p/> RecipeProviders are advised to cache their list of Recipes whenever possible.
     */
    public interface RecipeProvider {

        /**
         * Called when {@link ResynthRecipes Resynth's Recipe Manager} wants
         * to register all custom recipes to the game. This method may be
         * called multiple times, so it's advised the list of {@link IRecipe
         * Recipes} be cached.
         *
         * <p/> Implementations should return an array of the {@link IRecipe
         * Recipes} that the specific implementation wants registered to
         * the game. The returned list may be {@code null} or empty.
         *
         * @return a list of {@link IRecipe Recipes} that the specific
         * RecipeProvider implementation wants registered to the game.
         * This list may be empty or {@code null}.
         */
        @Nullable
        IRecipe<?>[] get();

        /**
         * Convenience method to create and obtain a new {@link FurnaceRecipe}
         * instance from the given input.
         *
         * @param resource the {@link ResourceLocation} used to identify the Recipe.
         * @param group the name of the group this crafting recipe belongs to.
         * @param output the result of the smelting operation, as an {@link ItemStack} specifing the
         *               smelted Item and amount.
         * @param input the Item or Block ingredient needed for the smelting operation.
         * @param experience the amount of experience(XP) to give the player for using this Recipe.
         * @param time the amount of time it takes to complete the smelting Recipe.
         * @return the created {@link FurnaceRecipe}.
         */
        static FurnaceRecipe newFurnaceRecipe(ResourceLocation resource, String group, double experience, int time,
                                                     ItemStack output, IItemProvider input) {
            return new FurnaceRecipe(resource, group, Ingredient.fromItems(input), output, (float) experience, time);
        }

        /**
         * Convenience method to create and obtain a new {@link FurnaceRecipe}
         * instance from the given input.
         *
         * @param resourcePath the String value of the Resynth {@link ResourceLocation#getPath()}.
         * @param group the name of the group this crafting recipe belongs to.
         * @param output the result of the smelting operation, as an {@link ItemStack} specifing the
         *               smelted Item and amount.
         * @param input the Item or Block ingredient needed for the smelting operation.
         * @param experience the amount of experience(XP) to give the player for using this Recipe.
         * @param time the amount of time it takes to complete the smelting Recipe.
         * @return the created {@link FurnaceRecipe}.
         */
        static FurnaceRecipe newFurnaceRecipe(String resourcePath, String group, double experience, int time,
                                                     ItemStack output, IItemProvider input) {
            return newFurnaceRecipe(rrl(resourcePath), group, experience, time, output, input);
        }

        /**
         * Convenience method to create and obtain a new {@link ShapelessRecipe}
         * instance from the given input.
         *
         * @param resource the {@link ResourceLocation} used to identify the Recipe.
         * @param group the name of the group this crafting recipe belongs to.
         * @param output the result of the crafting operation, as an {@link ItemStack} specifing the
         *               crafted Item and amount.
         * @param input the list of Items and/or Blocks to be used as ingredients needed to craft.
         * @return the created {@link ShapelessRecipe}.
         */
        static ShapelessRecipe newShapelessRecipe(ResourceLocation resource, String group,
                                                              ItemStack output, IItemProvider... input) {
            NonNullList<Ingredient> ingredients = NonNullList.create();
            for(IItemProvider in : input){
                ingredients.add(Ingredient.fromItems(in));
            }

            return new ShapelessRecipe(resource, group, output, ingredients);
        }

        /**
         * Convenience method to create and obtain a new {@link ShapelessRecipe}
         * instance from the given input.
         *
         * @param resourcePath the String value of the Resynth {@link ResourceLocation#getPath()}.
         * @param group the name of the group this crafting recipe belongs to.
         * @param output the result of the crafting operation, as an {@link ItemStack} specifing the
         *               crafted Item and amount.
         * @param input the list of Items and/or Blocks to be used as ingredients needed to craft.
         * @return the created {@link ShapelessRecipe}.
         */
        static ShapelessRecipe newShapelessRecipe(String resourcePath, String group,
                                                              ItemStack output, IItemProvider... input) {
            return newShapelessRecipe(rrl(resourcePath), group, output, input);
        }

        /**
         * Creates a new {@link ResourceLocation} object instance, that has the
         * {@link ResourceLocation#getNamespace()} of Resynth, and the provided path.
         *
         * @param path the String value to set as the {@link ResourceLocation#getPath()}
         * @return the new ResourceLocation, for Resynth, with the given path.
         */
        static ResourceLocation rrl(String path) {
            return new ResourceLocation(ResynthMod.MODID, path);
        }
    }
}
