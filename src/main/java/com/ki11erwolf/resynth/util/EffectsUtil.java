/*
 * Copyright 2018-2019 Ki11er_wolf
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
package com.ki11erwolf.resynth.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A set of utilities that aid in displaying particle
 * effects and playing sound effect, with an emphasis
 * on client/server differentiation.
 *
 * <p/>This class was created to address the constant issues
 * with client side effects crashing servers.
 *
 * <p/>The full use of the class is to provide a range of
 * util methods that allow playing/displaying effects
 * that ALL take into account physical and logical sides.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class EffectsUtil {

    /**Private constructor - utils.*/
    private EffectsUtil(){}

    // ************
    // Public Utils
    // ************

    //Sound

    /**
     * Will play a given sound effect in the world,
     * at the given position, for the given player,
     * at full volume with no pitch modification.
     * <b>Provided</b> it is safe
     * ({@link #isUnsafe(World)} to do so.
     *
     * <p/>This method is not client or server bound,
     * so calling it in any context is okay.
     *
     * @param world the world to play the sound in.
     * @param player the player to play the sound for.
     * @param pos the position in the world to play the sound.
     * @param soundEvent the specific sound effect to play.
     * @param category the sound category the effect falls under.
     * @return {@code true} if the sound was played after
     * being determined safe, {@code false} if it was determined
     * unsafe.
     */
    public static boolean playSound(World world, PlayerEntity player, BlockPos pos, SoundEvent soundEvent,
                                    SoundCategory category){
        float volume = 1.0F;//Full
        float pitch = 1.0F;//None

        return playSound(world, player, pos.getX(), pos.getY(), pos.getZ(), soundEvent, category, volume, pitch);
    }

    //Visual

    // ************
    // Safe Effects
    // ************

    //Sound

    /**
     * Will play a given sound effect in the world,
     * at the given position, with the given volume
     * and pitch. <b>Provided</b> it is safe
     * ({@link #isUnsafe(World)} to do so.
     *
     * <p/>This method is not client or server bound,
     * so calling it in any context is okay.
     *
     * @param world the world to play the sound in.
     * @param x the x position in the world to play the sound.
     * @param y the y position in the world to play the sound.
     * @param z the z position in the world to play the sound.
     * @param soundEvent the specific sound effect to play.
     * @param category the sound category the effect falls under.
     * @param volume the volume to play the sound at.
     * @param pitch the pitch modification to the sound.
     * @param distanceDelay {@code true} if the sound has
     *                                  a travel time.
     * @return {@code true} if the sound was played after
     * being determined safe, {@code false} if it was determined
     * unsafe.
     */
    private static boolean playSound(World world, double x, double y, double z, SoundEvent soundEvent,
                                  SoundCategory category, float volume, float pitch, boolean distanceDelay){
        if(isUnsafe(world)) return false;//Safety check

        world.playSound(x, y, z, soundEvent, category, volume, pitch, distanceDelay);
        return true;
    }

    /**
     * Will play a given sound effect in the world,
     * at the given position, for the given player,
     * with the given volume and pitch. <b>Provided</b>
     * it is safe ({@link #isUnsafe(World)} to do so.
     *
     * <p/>This method is not client or server bound,
     * so calling it in any context is okay.
     *
     * @param world the world to play the sound in.
     * @param player the player to play the sound for.
     * @param x the x position in the world to play the sound.
     * @param y the y position in the world to play the sound.
     * @param z the z position in the world to play the sound.
     * @param soundEvent the specific sound effect to play.
     * @param category the sound category the effect falls under.
     * @param volume the volume to play the sound at.
     * @param pitch the pitch modification to the sound.
     * @return {@code true} if the sound was played after
     * being determined safe, {@code false} if it was determined
     * unsafe.
     */
    private static boolean playSound(World world, PlayerEntity player, double x, double y, double z,
                                     SoundEvent soundEvent, SoundCategory category, float volume, float pitch){
        if(isUnsafe(world)) return false;//Safety check

        world.playSound(player, x, y, z, soundEvent, category, volume, pitch);
        return true;
    }

    //Visual

    // **********
    // Safe Check
    // **********

    /**
     * Used to check if executing client only code
     * (effects and sound) would be unsafe (cause
     * a crash or be executed on the server side).
     *
     * @param world the world object used to
     *              perform the side check.
     * @return {@code true} if executing the
     * client only code would be unsafe.
     */
    private static boolean isUnsafe(World world){
        return !SideUtil.isClientTrueSafe(world);
    }

    /**
     * Used to check if executing client only code
     * (effects and sound) would be unsafe.
     *
     * @return {@code true} if executing the
     * client only code would be unsafe (cause
     * a crash or be executed on the server side).
     */
    private static boolean isUnsafe(){
        return !SideUtil.isClientTrueSafe();
    }
}
