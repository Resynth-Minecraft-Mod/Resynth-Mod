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

import com.ki11erwolf.resynth.packet.ClientAVEffectPacket;
import com.ki11erwolf.resynth.packet.Packet;
import com.ki11erwolf.resynth.util.MinecraftUtil;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * Allows implementing plant sets (e.g. {@link CrystallineSet})
 * to easily register game hooks that allow spawning
 * plant seeds in the world from player actions.
 * <p/>
 * Provides implementing classes with an easy way
 * of registration and some utility methods.
 */
class PlantSetSeedHooks {

    private static final double PLAYER_EFFECT_RADIUS = 7;

    /**
     * {@code true} if this SeedHooks instance
     * has already been registered, {@code false}
     * otherwise.
     */
    private boolean registered = false;

    /**
     * Registers this SeedHooks instance to the Forge
     * event bus if it has not yet been registered,
     * allowing subscribing and responding to Minecraft
     * events.
     */
    void register(){
        if(registered)
            return;

        registered = true;
        MinecraftForge.EVENT_BUS.register(this);
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
    static void dropSeeds(Item seeds, World world, BlockPos pos){
        spawnSeedsStack(seeds, world, pos);
        playEffects(pos, world);
    }

    /**
     * Spawns the given item in the world at the given position.
     */
    private static void spawnSeedsStack(Item seeds, World world, BlockPos pos){
        MinecraftUtil.spawnItemInWorld(seeds, world, pos);
    }

    /**
     * Plays a bell sound and displays flash particle
     * effects at the given position to indicate that
     * seeds have been spawned.
     *
     * <p/>This doesn't work on dedicated servers.
     *
     * @param pos the position to play/display the effects.
     */
    private static void playEffects(BlockPos pos, World world){
        Packet.send(
                PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(
                        pos.getX(), pos.getY(), pos.getZ(), PLAYER_EFFECT_RADIUS, world.dimension()
                )),
                new ClientAVEffectPacket(ClientAVEffectPacket.AVEffect.SEEDS_SPAWNED, pos)
        );
    }
}
