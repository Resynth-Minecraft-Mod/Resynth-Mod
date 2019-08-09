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
//import mcp.mobius.waila.api.IWailaConfigHandler;
//import mcp.mobius.waila.api.IWailaDataAccessor;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//
//import java.util.List;
//
///**
// * Interface used to provide block information to Hwyla/Waila and
// * The One Probe(in the future).
// */
//public interface HwylaDataProvider {
//
//    /**
//     * Called when Hwyla/Waila wants to get information about the block.
//     *
//     * This is the primary way information about the block is passed to Hwyla.
//     *
//     * @param itemStack the item stack returned by the block.
//     * @param tooltip list of strings to pass to Hwyla to show in the tooltip.
//     * @param accessor accessor used to get data from the block such as NBT.
//     * @param config current Hwyla configuration
//     */
//    void onHwylaBodyRequest(ItemStack itemStack, List<String> tooltip,
//                            IWailaDataAccessor accessor, IWailaConfigHandler config);
//
//    /**
//     * Hwyla request method to get the head (title) of the tooltip.
//     * This is usually the block name.
//     *
//     * @param itemStack the block ItemStack.
//     * @param tooltip the head tooltip array (containing the block name).
//     * @param accessor Hwyla data provider.
//     * @param config current Hwyla config settings.
//     */
//    default void onHwylaHeadRequest(ItemStack itemStack, List<String> tooltip,
//                                    IWailaDataAccessor accessor, IWailaConfigHandler config){
//        //NO-OP
//    }
//
//    /**
//     * Hwyla request method to get the head (title) of the tooltip.
//     * This is usually the block name.
//     *
//     * @param itemStack the block ItemStack.
//     * @param tooltip the tail tooltip array (normally the mod name).
//     * @param accessor Hwyla data provider.
//     * @param config current Hwyla config settings.
//     */
//    default void onHwylaTailRequest(ItemStack itemStack, List<String> tooltip,
//                                    IWailaDataAccessor accessor, IWailaConfigHandler config){
//        //NO-OP
//    }
//
//    /**
//     * Hwyla client side NBT data provider request event.
//     *
//     * Used to pass server side tile entity data to the client
//     * side though NBT data.
//     *
//     * @param player player looking at a block.
//     * @param te blocks tile entity.
//     * @param tag Hwyla client side NBT data.
//     * @param world current world the player is in.
//     * @param pos position of the block the player is looking at.
//     */
//    default void onHwylaNBTDataRequest(EntityPlayerMP player, TileEntity te,
//                                       NBTTagCompound tag, World world, BlockPos pos){
//        //NO-OP.
//    }
//
//    /**
//     * Hwyla request method to get the ItemStack
//     * to show for the block.
//     *
//     * @param accessor Hwyla data provider.
//     * @param config Current Hwyla config settings.
//     * @return the ItemStack to show, or {@code null} to show the default.
//     */
//    default ItemStack onHwylaItemStackRequest(IWailaDataAccessor accessor, IWailaConfigHandler config){
//        return null;//NO-OP
//    }
//
//
//}
