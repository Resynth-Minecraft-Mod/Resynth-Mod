///*
// * Copyright 2018-2019 Ki11er_wolf
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.ki11erwolf.resynth.igtooltip;
//
//import com.ki11erwolf.resynth.ResynthMod;
//import mcjty.theoneprobe.api.*;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.common.event.FMLInterModComms;
//
//import javax.annotation.Nullable;
//import java.util.function.Function;
//
///**
// * Sets up and initializes The One Probe compatibility for
// * all blocks that provide information to The One Probe.
// */
//public class TOPCompatibility {
//
//    /**
//     * True if this class has been called to initialize.
//     */
//    private static boolean registered;
//
//    /**
//     * Internal register method. Do NOT call!
//     */
//    public static void register() {
//        if (registered)
//            return;
//        registered = true;
//
//        FMLInterModComms.sendFunctionMessage(
//                "theoneprobe",
//                "getTheOneProbe",
//                "com.ki11erwolf.resynth.igtooltip.TOPCompatibility$GetTheOneProbe"
//        );
//    }
//
//    /**
//     * Inner class for handling probe registration.
//     */
//    public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {
//
//        /**
//         * Prove interface.
//         */
//        public static ITheOneProbe probe;
//
//        /**
//         * Handles giving block info to the prove
//         * through the TOPDataProvider interface.
//         *
//         * @param theOneProbe provided prove interface.
//         * @return {@code null}
//         */
//        @Nullable
//        @Override
//        public Void apply(ITheOneProbe theOneProbe) {
//            probe = theOneProbe;
//            ResynthMod.getLogger().info("Enabling support for The One Probe...");
//            probe.registerProvider(new IProbeInfoProvider() {
//
//                /**
//                 * @return Resynth modid with {@code :default} identifier.
//                 */
//                @Override
//                public String getID() {
//                    return ResynthMod.MOD_ID + ":default";
//                }
//
//                /**
//                 * {@inheritDoc}
//                 *
//                 * @param mode
//                 * @param probeInfo
//                 * @param player
//                 * @param world
//                 * @param blockState
//                 * @param data
//                 */
//                @Override
//                public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player,
//                                         World world, IBlockState blockState, IProbeHitData data) {
//                    if (blockState.getBlock() instanceof TOPDataProvider) {
//                        TOPDataProvider provider = (TOPDataProvider) blockState.getBlock();
//                        provider.addProbeInfo(mode, probeInfo, player, world, blockState, data);
//                    }
//
//                }
//            });
//            return null;
//        }
//    }
//}