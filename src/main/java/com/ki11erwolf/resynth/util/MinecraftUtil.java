package com.ki11erwolf.resynth.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A set of utilities that help with
 * general Minecraft Modding.
 */
public class MinecraftUtil {

    /**Private Constructor.*/
    private MinecraftUtil(){}

    /**
     * Spawns an item in the world as an entity.
     *
     * @return {@code true} if the item was spawned.
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean spawnItemInWorld(Item item, World world, BlockPos pos){
        return spawnItemStackInWorld(new ItemStack(item), world, pos);
    }

    /**
     * Spawns an ItemStack in the world as an entity.
     *
     * @return {@code true} if the ItemStack was spawned.
     */
    @SuppressWarnings("WeakerAccess")
    public static boolean spawnItemStackInWorld(ItemStack stack, World world, BlockPos pos){
        return world.spawnEntity(new EntityItem(
                world,
                pos.getX(), pos.getY(), pos.getZ(),
                stack
        ));
    }
}
