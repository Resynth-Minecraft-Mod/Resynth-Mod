package com.ki11erwolf.resynth.plant.set;

import com.ki11erwolf.resynth.ResynthRecipes;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

/**
 * Aids the creation & registration of any and all crafting/smelting
 * recipes for Plants, PlantSets, and any Plant Items or Blocks.
 */
enum PlantSetRecipes implements ResynthRecipes.RecipeProvider {

    /**
     * The singleton instance of this class.
     */
    INSTANCE;

    /**
     * The array of recipes provided by this {@link com.ki11erwolf.resynth.ResynthRecipes.RecipeProvider}.
     * <br/> Variable acts as a cache - prevents calculating & recreating the list each time it's requested.
     */
    private IRecipe<?>[] recipes;

    /**
     * {@inheritDoc}
     *
     * @return an array of all the recipes created for Resynth's Plant Sets.
     * The array is cached, after being created when this method is first called.
     */
    @Override
    public IRecipe<?>[] get() {
        if(recipes == null){
            recipes = new IRecipe[] {
                    newFurnaceRecipe("recipe_a", "group_a", 0.5, 10,
                            new ItemStack(Blocks.DIAMOND_ORE), Blocks.DIRT)
            };
        }

        return recipes;
    }
}
