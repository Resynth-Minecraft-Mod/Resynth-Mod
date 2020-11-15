package com.ki11erwolf.resynth;

import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.GeneralConfig;
import com.ki11erwolf.resynth.item.ResynthItems;
import com.ki11erwolf.resynth.plant.set.PublicPlantSetRegistry;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.*;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * The global manager and handler for all custom {@link IRecipe Recipes}
 * created by Resynth programmatically, as opposed to Recipes defined
 * through assets. Allows registering {@link RecipeProvider RecipeProviders}
 * that are queried for lists of Recipes to register to the game.
 */
public enum ResynthRecipes implements ISelectiveResourceReloadListener {

    /**
     * The singleton instance of the recipe manager
     * for the whole of the Resynth mod.
     *
     * <p/> Provided in the constructor is the list
     * of Resynth's recipe providers.
     */
    INSTANCE(// List of custom RecipeProviders
        PublicPlantSetRegistry.getPlantSetRecipes(), CoreRecipes.INSTANCE
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
         */
        private static final boolean ENSURE_RESOURCE_RECIPES = true;

        // Configurable Recipe Definitions

        /**
         * The {@link IRecipe Recipe} instance that will craft Mineral Rocks
         * using a standard alternative recipe.
         */
        private static final IRecipe<?> MINERAL_ROCK_ALT_RECIPE = INSTANCE.newShapelessRecipe(
                "special_mineral_rock", "Resynth's Resources",
                new ItemStack(ResynthItems.ITEM_MINERAL_ROCK, 6), Items.IRON_INGOT, Items.IRON_INGOT,
                Items.INK_SAC, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE
        );

        /**
         * The {@link IRecipe Recipe} instance that will craft Calvinite Crystals
         * using a standard alternative recipe.
         */
        private static final IRecipe<?> CALVINITE_CRYSTAL_ALT_RECIPE = INSTANCE.newShapelessRecipe(
                "special_calvinite_crystal", "Resynth's Resources",
                new ItemStack(ResynthItems.ITEM_CALVINITE, 2), Items.QUARTZ, Items.QUARTZ,
                Items.INK_SAC, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE
        );

        /**
         * The {@link IRecipe Recipe} instance that will craft Sylvanite Crystals
         * using a standard alternative recipe.
         */
        private static final IRecipe<?> SYLVANITE_CRYSTAL_ALT_RECIPE = INSTANCE.newShapelessRecipe(
                "special_sylvanite_crystal", "Resynth's Resources",
                new ItemStack(ResynthItems.ITEM_SYLVANITE, 6), Items.DIAMOND, Items.END_STONE,
                Items.INK_SAC, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE
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
            recipeContainer.add(CALVINITE_CRYSTAL_ALT_RECIPE);
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

                // Add recipe
                recipes.add(recipe);
                LOG.info(String.format(
                        "Registered Resynth recipe: '%s' -> %s",
                        recipe.getId().toString(), (recipe.getRecipeOutput().getItem().getRegistryName() != null ?
                                recipe.getRecipeOutput().getItem().getRegistryName().toString() : "<NO REG NAME>")
                                + recipe.getRecipeOutput().getCount()
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
     * @param resourcePredicate the {@link IResourceType} if applicable.
     */
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        ResynthMod.getNewLogger().info("Injecting Resynth's custom recipes for plants & plant sets...");

        List<IRecipe<?>> combined = new ArrayList<>(getRecipes());
        // IMPORTANT: Include recipes already in the manager
        combined.addAll(getRecipeManager().getRecipes());
        getRecipeManager().func_223389_a(combined);
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

        recipeManager = event.getDataPackRegistries().func_240967_e_();
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
        default IRecipe<IInventory> newFurnaceRecipe(ResourceLocation resource, String group, double experience, int time,
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
        default IRecipe<IInventory> newFurnaceRecipe(String resourcePath, String group, double experience, int time,
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
        default IRecipe<CraftingInventory> newShapelessRecipe(ResourceLocation resource, String group,
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
        default IRecipe<CraftingInventory> newShapelessRecipe(String resourcePath, String group,
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
        default ResourceLocation rrl(String path) {
            return new ResourceLocation(ResynthMod.MODID, path);
        }
    }
}
