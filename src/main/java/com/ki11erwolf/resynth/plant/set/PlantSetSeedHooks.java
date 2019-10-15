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
package com.ki11erwolf.resynth.plant.set;

import com.ki11erwolf.resynth.util.EffectsUtil;
import com.ki11erwolf.resynth.util.MinecraftUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Allows implementing plant sets (e.g. {@link CrystallineSet})
 * to easily register game hooks that allow spawning
 * plant seeds in the world from player actions.
 * <p/>
 * Provides implementing classes with an easy way
 * of registration and some utility methods.
 */
class PlantSetSeedHooks {

    /**
     * {@code true} if this SeedHooks instance
     * has already been registered, {@code false}
     * otherwise.
     */
    private boolean registered = false;

    /**
     * Registers this SeedHooks instance if it
     * has not yet been registered.
     */
    void register(){
        if(registered)
            return;

        MinecraftForge.EVENT_BUS.register(this);
        registered = true;
    }

    // ***************
    // Seed Spawn Util
    // ***************

    /**
     * Handles spawning plant seeds in the world in a given position.
     * This will also spawn the particle and sound effects.
     *
     * @param seeds the seeds item to spawn.
     * @param world the world to spawn the seeds in.
     * @param pos the position in the world to spawn the seeds in.
     */
    static void spawnSeeds(Item seeds, World world, BlockPos pos){
        spawnSeedsStack(seeds, world, pos);

        //Ensure we're on a client as we're calling client only code.
        if(FMLEnvironment.dist.isClient()){
            spawnEffects(pos);
        }
    }

    /**
     * Spawns the given item in the world at the given position.
     */
    private static void spawnSeedsStack(Item seeds, World world, BlockPos pos){
        MinecraftUtil.spawnItemInWorld(seeds, world, pos);
    }

    /**
     * ONLY CALL ON CLIENT!
     *
     * Spawns the seeds spawn effects (particles
     * and sound) in the client world.
     *
     * @param pos the position in the world to spawn the effects.
     */
    @OnlyIn(Dist.CLIENT)
    private static void spawnEffects(BlockPos pos){
        World world = Minecraft.getInstance().world;
        double d = world.rand.nextGaussian() * 0.02D;
        int amount = 10;

        BlockState blockstate = world.getBlockState(pos);

        //Effects
        if (blockstate.getMaterial() != Material.AIR) {
            for (int i = 0; i < amount; ++i){
                world.addParticle(ParticleTypes.FLASH,
                        (float)pos.getX() + world.rand.nextFloat(),
                        (double)pos.getY() + (double)world.rand.nextFloat()
                                * blockstate.getShape(world, pos).getEnd(Direction.Axis.Y),
                        (float)pos.getZ() + world.rand.nextFloat(), d, d, d);
            }
        }
        else {
            for (int i1 = 0; i1 < amount; ++i1) {
                world.addParticle(ParticleTypes.FLASH,
                        (float)pos.getX() + world.rand.nextFloat(),
                        (double)pos.getY() + (double)world.rand.nextFloat() * 1.0f,
                        (float)pos.getZ() + world.rand.nextFloat(), d, d, d);
            }
        }

        //Sound
        EffectsUtil.playNormalSoundWithVolumeOnClient(
                pos, SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.NEUTRAL, 8.0F
        );
    }
}
