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

import net.minecraft.world.World;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * A set of utilities that aid in determining
 * the logical and physical side we're executing
 * code on, as well as safety checks.
 */
@SuppressWarnings({"WeakerAccess"})
public class SideUtil {

    /**Private constructor - utils.*/
    private SideUtil(){}

    //Safety Checks

    /**
     * Used to check if we're both running on a
     * client (not dedicated server) jar and that we're
     * running on the client side process.
     *
     * <p/>If this method returns {@code true}, it's
     * both safe to call client-only code as well as
     * perform client side operations, such as sound
     * and effects.
     *
     * <p/>It is recommended to use this method
     * over {@link #isClientTrueSafe()} whenever
     * possible.
     *
     * @param world the world object instance used to
     *              perform the logical side check.
     * @return {@code true} if we're running on both
     * the client jar and client side process.
     */
    public static boolean isClientTrueSafe(World world){
        return isPhysicalSideClient() && isLogicalSideClient(world);
    }

    /**
     * Used to check if we're both running on a
     * client (not dedicated server) jar and that we're
     * running on the client side process.
     *
     * <p/>If this method returns {@code true}, it's
     * both safe to call client-only code as well as
     * perform client side operations, such as sound
     * and effects.
     *
     * <p/>It is recommended to use {@link
     * #isClientTrueSafe(World)} over this method
     * whenever possible.
     *
     * @return {@code true} if we're running on both
     * the client jar and client side process.
     */
    public static boolean isClientTrueSafe(){
        return isPhysicalSideClient() && isLogicalSideClient();
    }

    /**
     * Used to check if we're running on a
     * client (not dedicated server) jar.
     *
     * <p/>If this method returns {@code true}, it's
     * safe to call client-only code, but unsafe to
     * call server-only code.
     *
     * @return {@code true} if we're running on
     * the client jar.
     */
    public static boolean isClientSafe(){
        return isPhysicalSideClient();
    }

    /**
     * Used to check if we're both running on a
     * dedicated server jar and that we're
     * running on the server side process.
     *
     * <p/>If this method returns {@code true}, it's
     * both unsafe to call client-only code, but safe
     * to call server only code, as well as perform
     * server side operations, such as logic and
     * data modification.
     *
     * <p/>It is recommended to use this method
     * over {@link #isServerTrueSafe()} whenever
     * possible.
     *
     * @param world the world object instance used to
     *              perform the logical side check.
     * @return {@code true} if we're running on both
     * the dedicated server jar and server side process.
     */
    public static boolean isServerTrueSafe(World world){
        return isPhysicalSideServer() && isLogicalSideServer(world);
    }

    /**
     * Used to check if we're both running on a
     * dedicated server jar and that we're running
     * on the server side process.
     *
     * <p/>If this method returns {@code true}, it's
     * both unsafe to call client side only code, but
     * safe to call server side only code, well as
     * perform server side operations, such as logic and
     * data modification.
     *
     * <p/>It is recommended to use {@link
     * #isServerTrueSafe(World)} over this method
     * whenever possible.
     *
     * @return {@code true} if we're running on both
     * the dedicated server jar and server side process.
     */
    public static boolean isServerTrueSafe(){
        return isPhysicalSideServer() && isLogicalSideServer();
    }

    /**
     * Used to check if we're running on a
     * dedicated server jar.
     *
     * <p/>If this method returns {@code true}, it's
     * unsafe to call client-only code, but safe to
     * call server-only code.
     *
     * @return {@code true} if we're running on
     * the dedicated server jar.
     */
    public static boolean isServerSafe(){
        return isPhysicalSideServer();
    }

    //Effective/Logical Side (world.isRemote) checks

    /**
     * Used to check if we're executing on the logical
     * server side of a client jar. If this returns
     * {@code true}, it is safe to execute server
     * side operations.
     *
     * <p/>This method uses the {@link EffectiveSide}
     * api to do the check.
     *
     * @return {@code true} if we're running on the server
     * side.
     */
    public static boolean isLogicalSideServer(){
        return EffectiveSide.get().isServer();
    }

    /**
     * Used to check if we're executing on the logical
     * server side of a client jar. If this returns
     * {@code true}, it is safe to execute server
     * side operations.
     *
     * <p/>This method uses the {@code world.isRemote}
     * check.
     *
     * @param world the world object used to determine side.
     * @return {@code true} if we're running on the server
     * side.
     */
    public static boolean isLogicalSideServer(World world){
        return !world.isRemote();
    }

    /**
     * Used to check if we're executing on the logical
     * client side of a client jar. If this returns
     * {@code true}, it is safe to execute client
     * side operations.
     *
     * <p/>This method uses the {@link EffectiveSide}
     * api to do the check.
     *
     * @return {@code true} if we're running on the client
     * side.
     */
    public static boolean isLogicalSideClient(){
        return EffectiveSide.get().isClient();
    }

    /**
     * Used to check if we're executing on the logical
     * client side of a client jar. If this returns
     * {@code true}, it is safe to execute client
     * side operations.
     *
     * <p/>This method uses the {@code world.isRemote}
     * check.
     *
     * @param world the world object used to determine side.
     * @return {@code true} if we're running on the client
     * side.
     */
    public static boolean isLogicalSideClient(World world){
        return world.isRemote();
    }

    //Physical Side (client vs dedicated server jar) checks

    /**
     * Used to check if we're running on a normal client
     * jar. If this returns {@code true}, it's
     * safe to call client only code.
     *
     * @return {@code true} if we're running as a
     * normal client jar.
     */
    public static boolean isPhysicalSideClient(){
        return FMLEnvironment.dist.isClient();
    }

    /**
     * Used to check if we're running on a dedicated
     * server jar. If this returns {@code true}, it's
     * unsafe to call client only code, and will
     * result in a crash.
     *
     * @return {@code true} if we're running as a
     * dedicated server jar and not a client jar.
     */
    public static boolean isPhysicalSideServer(){
        return FMLEnvironment.dist.isDedicatedServer();
    }
}
