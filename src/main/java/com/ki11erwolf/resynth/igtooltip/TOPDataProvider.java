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
//import mcjty.theoneprobe.api.IProbeHitData;
//import mcjty.theoneprobe.api.IProbeInfo;
//import mcjty.theoneprobe.api.ProbeMode;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.world.World;
//
///**
// * Allows blocks to provide information
// * about themselves to The One Probe.
// */
//public interface TOPDataProvider {
//
//    /**
//     * Called to get information about the block
//     * that will be given to The One Probe.
//     *
//     * @param mode the usage mode the probe is in.
//     * @param probeInfo probe information holder. Add block
//     *                  information to this object.
//     * @param player the player the information will be provided to.
//     * @param world the world the player is in.
//     * @param blockState state of the block the probe is probing.
//     * @param data probe ray tracing results.
//     */
//    void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player,
//                      World world, IBlockState blockState, IProbeHitData data);
//}
