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
package com.ki11erwolf.resynth.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * A set of utilities that aid in displaying particle
 * effects and playing sound effect, with an emphasis
 * on client/server differentiation. All the utilities
 * within this class can be called on either client
 * or server, however, the effects will only take
 * effect on the client side world.
 *
 * <p/>This class was created to address the constant issues
 * with client side effects crashing servers. Effects should
 * now be able to be easily utilized without the worry of
 * client and server differences.
 *
 * <p/>Besides the main function, this also also provides a
 * range of utility methods for particle effects and sounds.
 */
@SuppressWarnings({"UnusedReturnValue", "SameParameterValue", "WeakerAccess"})
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
     * ({@link #isUnsafe(World)}) to do so.
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
    public static boolean playNormalSound(World world, PlayerEntity player, BlockPos pos, SoundEvent soundEvent,
                                          SoundCategory category){
        float volume = 1.0F;//Full
        float pitch = 1.0F;//None

        return playNormalSound(world, player, pos.getX(), pos.getY(), pos.getZ(), soundEvent, category, volume, pitch);
    }

    /**
     * Will play a given sound effect in the world,
     * at the given position, for the given player,
     * at full volume with the given pitch modification.
     * <b>Provided</b> it is safe
     * ({@link #isUnsafe(World)}) to do so.
     *
     * <p/>This method is not client or server bound,
     * so calling it in any context is okay.
     *
     * @param world the world to play the sound in.
     * @param player the player to play the sound for.
     * @param pos the position in the world to play the sound.
     * @param soundEvent the specific sound effect to play.
     * @param category the sound category the effect falls under.
     * @param pitch the amount of pitch modification to apply to the sound.
     * @return {@code true} if the sound was played after
     * being determined safe, {@code false} if it was determined
     * unsafe.
     */
    private static boolean playNormalSoundWithPitch(World world, PlayerEntity player, BlockPos pos,
                                                    SoundEvent soundEvent, SoundCategory category, float pitch){
        float volume = 1.0F;//Full
        return playNormalSound(world, player, pos.getX(), pos.getY(), pos.getZ(), soundEvent, category, volume, pitch);
    }

    /**
     * Will play a given sound effect in the world,
     * at the given position, for the given player,
     * at full volume with random pitch in a given
     * range. <b>Provided</b> it is safe
     * ({@link #isUnsafe(World)}) to do so.
     *
     * <p/>This method is not client or server bound,
     * so calling it in any context is okay.
     *
     * @param world the world to play the sound in.
     * @param player the player to play the sound for.
     * @param pos the position in the world to play the sound.
     * @param soundEvent the specific sound effect to play.
     * @param category the sound category the effect falls under.
     * @param basePitch the initial pitch modification.
     * @param pitchMultiplier the random pitch number multiplier.
     * @return {@code true} if the sound was played after
     * being determined safe, {@code false} if it was determined
     * unsafe.
     */
    public static boolean playNormalSoundWithPitchInRage(World world, PlayerEntity player, BlockPos pos,
                                                    SoundEvent soundEvent, SoundCategory category,
                                                    float basePitch, float pitchMultiplier){
        //Full
        float volume = 1.0F;
        //Random pitch in range
        float pitch = basePitch + world.getRandom().nextFloat() * pitchMultiplier;

        return playNormalSound(world, player, pos.getX(), pos.getY(), pos.getZ(), soundEvent, category, volume, pitch);
    }

    /**
     * Will play a given sound effect in the world,
     * at the given players position, for the given player,
     * at full volume with random pitch in a given
     * range. <b>Provided</b> it is safe
     * ({@link #isUnsafe(World)}) to do so.
     *
     * <p/>This method is not client or server bound,
     * so calling it in any context is okay.
     *
     * @param world the world to play the sound in.
     * @param player the player to play the sound for.
     * @param soundEvent the specific sound effect to play.
     * @param category the sound category the effect falls under.
     * @param basePitch the initial pitch modification.
     * @param pitchMultiplier the random pitch number multiplier.
     * @return {@code true} if the sound was played after
     * being determined safe, {@code false} if it was determined
     * unsafe.
     */
    public static boolean playNormalSoundWithPitchInRage(World world, PlayerEntity player,
                                                         SoundEvent soundEvent, SoundCategory category,
                                                         float basePitch, float pitchMultiplier){
        //Full
        float volume = 1.0F;
        //Random pitch in range
        float pitch = basePitch + world.getRandom().nextFloat() * pitchMultiplier;
        BlockPos pos = new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ());

        return playNormalSound(world, player, pos.getX(), pos.getY(), pos.getZ(), soundEvent, category, volume, pitch);
    }

    /**
     * Will play a given sound effect in the world,
     * at the given position, for the given player,
     * at full volume with a standard random pitch
     * (base=0.8F, multiplier=0.3F). <b>Provided</b>
     * it is safe ({@link #isUnsafe(World)}) to do so.
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
    public static boolean playNormalSoundWithRandomPitch(World world, PlayerEntity player, BlockPos pos,
                                                    SoundEvent soundEvent, SoundCategory category){
        return playNormalSoundWithPitchInRage(
                world, player, pos, soundEvent, category, 0.8F, 0.3F
        );
    }

    /**
     * Will play a given sound effect in the CLIENT
     * SIDE world, at the given position, with the
     * given volume and pitch. <b>Provided</b> it is safe
     * ({@link #isUnsafe(World)}) to do so.
     *
     * <p/>This method is not client or server bound,
     * so calling it in any context is okay. However,
     * it will only take effect on client jars and
     * not dedicated servers. This makes it possible
     * to display effects from server side only code.
     *
     * @param pos the position in the world to play the sound.
     * @param soundEvent the specific sound effect to play.
     * @param category the sound category the effect falls under.
     * @param volume the volume to play the sound at.
     * @return {@code true} if the sound was played after
     * being determined safe, {@code false} if it was determined
     * unsafe.
     */
    public static boolean playNormalSoundWithVolumeOnClient(BlockPos pos, SoundEvent soundEvent, SoundCategory category,
                                                            float volume){
        return playNormalSoundOnClient(
                pos.getX(), pos.getY(), pos.getZ(), soundEvent, category, volume,
                1.0F, false
        );
    }

    //Visual

    /**
     * Will display the given particle effects in the world
     * (randomly with the given chance), at the given position,
     * with the given intensity. <b>Provided</b> it is safe
     * ({@link #isUnsafe(World)}) to do so.
     *
     * <p/>This method is not client or server bound,
     * so calling it in any context is okay.
     *
     * @param world the world to display the effects in.
     * @param pos the position in the world to display the effects.
     * @param intensity the amount of particles to spawn.
     * @param chance the chance that the particle effects will spawn.
     * @param particleType the particle type to spawn in the world.
     * @return {@code true} if the sound was played after
     * being determined safe, {@code false} if it was determined
     * unsafe.
     */
    public static boolean displayStandardEffectsWithChance(World world, BlockPos pos, int intensity,
                                                           float chance, IParticleData particleType){
        return displayEffectsWithChance(world, pos, intensity, 0.02D, particleType, chance);
    }

    /**
     * Will display the given particle effects in the world,
     * with the given velocity, at the given position,
     * with the given intensity. <b>Provided</b> it is safe
     * ({@link #isUnsafe(World)}) to do so.
     *
     * <p/>This method is not client or server bound,
     * so calling it in any context is okay.
     *
     * @param world the world to display the effects in.
     * @param pos the position in the world to display the effects.
     * @param intensity the amount of particles to spawn.
     * @param speedMultiplier the speed at which the particles move: velocity.
     * @param particleType the particle type to spawn in the world.
     * @return {@code true} if the sound was played after
     * being determined safe, {@code false} if it was determined
     * unsafe.
     */
    public static boolean displayStandardEffectsWithSpeed(World world, BlockPos pos, int intensity,
                                                          IParticleData particleType, double speedMultiplier){
        return displayEffects(world, pos, intensity, speedMultiplier, particleType);
    }

    /**
     * Will display the given particle effects in the world,
     * at the given position, with the given intensity.
     * <b>Provided</b> it is safe ({@link #isUnsafe(World)})
     * to do so.
     *
     * <p/>This method is not client or server bound,
     * so calling it in any context is okay.
     *
     * @param world the world to display the effects in.
     * @param pos the position in the world to display the effects.
     * @param intensity the amount of particles to spawn.
     * @param particleType the particle type to spawn in the world.
     * @return {@code true} if the sound was played after
     * being determined safe, {@code false} if it was determined
     * unsafe.
     */
    public static boolean displayStandardEffects(World world, BlockPos pos, int intensity, IParticleData particleType){
        return displayEffects(world, pos, intensity, 0.02D, particleType);
    }

    /**
     * Will display the given particle effects in the
     * CLIENT SIDE world, at the given position, with
     * the given intensity. <b>Provided</b> it is safe
     * ({@link #isUnsafe(World)}) to do so.
     *
     * <p/>This method is not client or server bound,
     * so calling it in any context is okay.
     *
     * @param pos the position in the world to display the effects.
     * @param intensity the amount of particles to spawn.
     * @param particleType the particle type to spawn in the world.
     * @return {@code true} if the sound was played after
     * being determined safe, {@code false} if it was determined
     * unsafe.
     */
    public static boolean displayStandardEffectsOnClient(BlockPos pos, int intensity, IParticleData particleType){
        return displayEffectsOnClient(pos, intensity, 0.02D, particleType);
    }

    // ************
    // Safe Effects
    // ************

    //Sound

    /**
     * Will play a given sound effect in the world,
     * at the given position, with the given volume
     * and pitch. <b>Provided</b> it is safe
     * ({@link #isUnsafe(World)}) to do so.
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
    private static boolean playNormalSound(World world, double x, double y, double z, SoundEvent soundEvent,
                                           SoundCategory category, float volume, float pitch, boolean distanceDelay){
        if(isUnsafe(world)) return false;//Safety check

        world.playSound(x, y, z, soundEvent, category, volume, pitch, distanceDelay);
        return true;
    }

    /**
     * Will play a given sound effect in the world,
     * at the given position, for the given player,
     * with the given volume and pitch. <b>Provided</b>
     * it is safe ({@link #isUnsafe(World)}) to do so.
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
    private static boolean playNormalSound(World world, PlayerEntity player, double x, double y, double z,
                                           SoundEvent soundEvent, SoundCategory category, float volume, float pitch){
        if(isUnsafe(world)) return false;//Safety check

        world.playSound(player, x, y, z, soundEvent, category, volume, pitch);
        return true;
    }

    /**
     * Will play a given sound effect in the CLIENT
     * SIDE world, for the given player, at the given
     * position, with the given volume and pitch.
     * <b>Provided</b> it is safe
     * ({@link #isUnsafe(World)}) to do so.
     *
     * <p/>This method is not client or server bound,
     * so calling it in any context is okay. However,
     * it will only take effect on client jars and
     * not dedicated servers. This makes it possible
     * to display effects from server side only code.
     *
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
    private static boolean playNormalSoundOnClient(PlayerEntity player, double x, double y, double z,
                                           SoundEvent soundEvent, SoundCategory category, float volume, float pitch){
        World world = getClientWorld();
        if(world == null) return false;//Safe check.

        world.playSound(player, x, y, z, soundEvent, category, volume, pitch);
        return true;
    }

    /**
     * Will play a given sound effect in the CLIENT
     * SIDE world, at the given position, with the
     * given volume and pitch. <b>Provided</b> it is safe
     * ({@link #isUnsafe(World)}) to do so.
     *
     * <p/>This method is not client or server bound,
     * so calling it in any context is okay. However,
     * it will only take effect on client jars and
     * not dedicated servers. This makes it possible
     * to display effects from server side only code.
     *
     * @param x the x position in the world to play the sound.
     * @param y the y position in the world to play the sound.
     * @param z the z position in the world to play the sound.
     * @param soundEvent the specific sound effect to play.
     * @param category the sound category the effect falls under.
     * @param volume the volume to play the sound at.
     * @param pitch the pitch modification to the sound.
     * @param distanceDelay {@code true} if the sound
     *                                  has to travel.
     * @return {@code true} if the sound was played after
     * being determined safe, {@code false} if it was determined
     * unsafe.
     */
    private static boolean playNormalSoundOnClient(double x, double y, double z, SoundEvent soundEvent,
                                                   SoundCategory category, float volume, float pitch,
                                                   boolean distanceDelay){
        World world = getClientWorld();
        if(world == null) return false;//Safety check.

        world.playSound(x, y, z, soundEvent, category, volume, pitch, distanceDelay);
        return true;
    }

    //Visual

    /**
     * Will display the given particle effects in the
     * given world, at the given position, with the
     * given intensity and speed. <b>Provided</b> it is safe
     * ({@link #isUnsafe(World)}) to do so.
     *
     * <p/>This method is not client or server bound,
     * so calling it in any context is okay. However,
     * it will only take effect on client jars and
     * not dedicated servers. This makes it possible
     * to display effects from server side only code.
     *
     * @param world the world to display the effects in.
     * @param pos the position in the world to spawn the particles.
     * @param intensity the amount of particles to spawn.
     * @param speedMultiplier the speed at which the particles travel.
     * @param particleType the particle effects to spawn.
     * @return {@code true} if the effects were displayed after
     * being determined safe, {@code false} if it was determined
     * unsafe.
     */
    private static boolean displayEffects(World world, BlockPos pos, int intensity, double speedMultiplier,
                                          IParticleData particleType){
        if(isUnsafe(world)) return false;//Safety check

        Random random = world.rand;
        double d = random.nextGaussian() * speedMultiplier;
        BlockState blockstate = world.getBlockState(pos);

        if (blockstate.getMaterial() != Material.AIR) {
            for (int i = 0; i < intensity; ++i){
                world.addParticle(
                        particleType,
                        (float)pos.getX() + random.nextFloat(),
                        (double)pos.getY() + (double)random.nextFloat()
                                * blockstate.getShape(world, pos).getEnd(Direction.Axis.Y),
                        (float)pos.getZ() + random.nextFloat(), d, d, d
                );
            }
        }
        else {
            for (int i = 0; i < intensity; ++i) {
                world.addParticle(
                        particleType,
                        (float)pos.getX() + random.nextFloat(),
                        (double)pos.getY() + (double)random.nextFloat() * 1.0f,
                        (float)pos.getZ() + random.nextFloat(), d, d, d
                );
            }
        }

        return true;
    }

    /**
     * Will display the given particle effects in the
     * given world, randomly with the given chance, at
     * the given position, with the given intensity
     * and speed. <b>Provided</b> it is safe
     * ({@link #isUnsafe(World)}) to do so.
     *
     * <p/>This method is not client or server bound,
     * so calling it in any context is okay. However,
     * it will only take effect on client jars and
     * not dedicated servers. This makes it possible
     * to display effects from server side only code.
     *
     * @param world the world to display the effects in.
     * @param pos the position in the world to spawn the particles.
     * @param intensity the amount of particles to spawn.
     * @param speedMultiplier the speed at which the particles travel.
     * @param chance the change that particles will spawn (0.0-100.0).
     * @param particleType the particle effects to spawn.
     * @return {@code true} if the effects were displayed after
     * being determined safe, {@code false} if it was determined
     * unsafe. This will return {@code true} even if the particles
     * weren't displayed because of random chance.
     */
    private static boolean displayEffectsWithChance(World world, BlockPos pos, int intensity, double speedMultiplier,
                                                    IParticleData particleType, float chance){
        if(isUnsafe(world)) return false;//Safety check

        if(MathUtil.Probability.newPercentageProbability(chance).randomResult().isTrue())
            displayEffects(world, pos, intensity, speedMultiplier, particleType);

        //We return true even if we don't actually display the effects.
        //This just indicates the call didn't fail.
        return true;
    }

    /**
     * Will display the given particle effects in the
     * CLIENT SIDE world, at the given position, with
     * the given intensity and speed. <b>Provided</b>
     * it is safe ({@link #isUnsafe(World)}) to do so.
     *
     * <p/>This method is not client or server bound,
     * so calling it in any context is okay. However,
     * it will only take effect on client jars and
     * not dedicated servers. This makes it possible
     * to display effects from server side only code.
     *
     * @param pos the position in the world to spawn the particles.
     * @param intensity the amount of particles to spawn.
     * @param speedMultiplier the speed at which the particles travel.
     * @param particleType the particle effects to spawn.
     * @return {@code true} if the effects were displayed after
     * being determined safe, {@code false} if it was determined
     * unsafe. This will return {@code true} even if the particles
     * weren't displayed because of random chance.
     */
    private static boolean displayEffectsOnClient(BlockPos pos, int intensity, double speedMultiplier,
                                                  IParticleData particleType){
        World world = getClientWorld();
        if(world == null) return false;//Safety check.

        return displayEffects(world, pos, intensity, speedMultiplier, particleType);
    }

    // **********
    // Safe Check
    // **********

    /**
     * Used to obtain the client side world reference.
     * The reference will only be returned if we're
     * not running on a dedicated server.
     *
     * @return the Minecraft client world instance,
     * or {@code null} if we're on a dedicated server.
     */
    private static World getClientWorld(){
        if(isClientUnsafe()) return null;//Safe check

        //Don't obtain the reference directly, use the
        //dedicated util class.
        World world = ClientWorldGetter.getClientWorld();
        return (isUnsafe(world) ? null : world);//Second safe check
    }

    /**
     * Used to check if executing client only code
     * effects and sound would be unsafe (cause
     * a crash). This method differs from the rest
     * in that it doesn't do a logical side check
     * (world.isRemote).
     *
     * @return {@code true} if executing the client only
     * code would be unsafe.
     */
    private static boolean isClientUnsafe(){
        return !SideUtil.isClientSafe();
    }

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

    /**
     * A utility class that enables us to obtain
     * a reference to the client side world instance
     * ({@link Minecraft#world}), without bring up
     * errors on servers.
     *
     * <p/>This class enables us to check for the client
     * side classes AT EXECUTION TIME, after the needed
     * checks have been performed.
     *
     * <p/>The end result: this allows us to still
     * reference client side code within code
     * that is meant to run on a server and a client.
     *
     * <p/>WARNING: this class will still crash
     * dedicated servers if referenced before
     * the necessary safety checks are performed.
     */
    private static final class ClientWorldGetter {
        //Private constructor.
        private ClientWorldGetter(){}

        /**
         * Used to get a reference to {@link Minecraft#world}
         * without causing errors on server code, PROVIDED
         * the necessary checks are performed before hand.
         *
         * <p/>This method is UNSAFE and will crash a server
         * if called on a dedicated server. Checks for this
         * must be performed before this method is called.
         *
         * @return the {@link Minecraft#world} instance.
         */
        private static World getClientWorld(){
            return net.minecraft.client.Minecraft.getInstance().world;
        }
    }
}
