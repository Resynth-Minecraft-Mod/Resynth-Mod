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
//import mcp.mobius.waila.api.IWailaConfigHandler;
//import mcp.mobius.waila.api.IWailaDataAccessor;
//import mcp.mobius.waila.api.IWailaDataProvider;
//import mcp.mobius.waila.api.IWailaRegistrar;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.common.event.FMLInterModComms;
//import org.apache.logging.log4j.Logger;
//
//import java.util.List;
//
///**
// * Sets up and initializes Hwyla compatibility for
// * all blocks that provide information to Hwyla.
// */
//public class HwylaCompatibility implements IWailaDataProvider {
//
//    /**
//     * Resynth-Hwyla interface API instance.
//     */
//    public static final HwylaCompatibility INSTANCE = new HwylaCompatibility();
//
//    /**
//     * An instance of an HwylaDataProvider than does nothing. Used to prevent
//     * excessive instance creation of useless providers.
//     */
//    protected static final HwylaDataProvider BLANK_PROVIDER = (itemStack, tooltip, accessor, config) -> {
//        //NO-OP
//    };
//
//    /**
//     * Resynth logger instance.
//     */
//    public static final Logger LOG = ResynthMod.getLogger();
//
//    /**
//     * True if the APIs register method was called.
//     */
//    private static boolean registered;
//
//    /**
//     * True if the API was successfully loaded (No errors
//     * thrown during registration).
//     */
//    private static boolean loaded;
//
//    //Private constructor.
//    private HwylaCompatibility() {}
//
//    /**
//     * Sets up the Hwyla interface API.
//     */
//    static void register(){
//        if (registered)
//            return;
//
//        registered = true;
//        FMLInterModComms.sendMessage(
//                "waila",
//                "register",
//                //Change this line to reflect the name if you change the name of the method below.
//                "com.ki11erwolf.resynth.igtooltip.HwylaCompatibility.onHwylaLoad"
//        );
//    }
//
//    /**
//     * Called by Hwyla. <b>DO NOT CALL!</b>
//     *
//     * @param registrar Given Hwyla block registrar API.
//     */
//    //MY GOD... this gave problems...
//    public static void onHwylaLoad(IWailaRegistrar registrar) {
//        try{
//            LOG.info("Hwyla register callback! Initializing...");
//
//            for(Class tooltipProvider : ResynthIGTooltips.TOOLTIP_CLASSES){
//                registrar.registerHeadProvider(INSTANCE, tooltipProvider);
//                registrar.registerBodyProvider(INSTANCE, tooltipProvider);
//                registrar.registerTailProvider(INSTANCE, tooltipProvider);
//            }
//
//            for(Class nbtProvider : ResynthIGTooltips.TOOLTIP_TILE_ENTITY_CLASSES){
//                registrar.registerNBTProvider(INSTANCE, nbtProvider);
//            }
//
//            LOG.info("Hwyla register callback initialized without fatal errors!");
//
//            loaded = true;
//        } catch (Exception e){
//            LOG.error("Hwyla register callback ERRORED!", e);
//            loaded = false;
//            throw e;
//        }
//    }
//
//    //****************************
//    //Hwyla Data Provider Methods
//    //****************************
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
//     *
//     * @return the modified given NBT data.
//     */
//    @Override
//    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te,
//                                     NBTTagCompound tag, World world, BlockPos pos) {
//        getProvider(world.getBlockState(pos).getBlock()).onHwylaNBTDataRequest(
//                player, te, tag, world, pos
//        );
//
//        return tag;
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
//    @Override
//    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
//        return getProvider(accessor.getBlock()).onHwylaItemStackRequest(
//                accessor, config
//        );
//    }
//
//    /**
//     * Hwyla request method to get the head (title) of the tooltip.
//     * This is usually the block name.
//     *
//     * @param itemStack the block ItemStack.
//     * @param tooltip the head tooltip array (containing the block name).
//     * @param accessor Hwyla data provider.
//     * @param config current Hwyla config settings.
//     *
//     * @return the modified head tooltip array.
//     */
//    @Override
//    public List<String> getWailaHead(ItemStack itemStack, List<String> tooltip,
//                                     IWailaDataAccessor accessor, IWailaConfigHandler config) {
//        getProvider(accessor.getBlock()).onHwylaHeadRequest(
//                itemStack, tooltip, accessor, config
//        );
//
//        return tooltip;
//    }
//
//    /**
//     * Hwyla request method to get the body of the tooltip.
//     * This is usually the method you want to use to provide
//     * the custom block information.
//     *
//     * @param itemStack the block ItemStack.
//     * @param tooltip the body tooltip array (normally empty).
//     * @param accessor Hwyla data provider.
//     * @param config current Hwyla config settings.
//     *
//     * @return the modified body tooltip array.
//     */
//    @Override
//    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip,
//                                     IWailaDataAccessor accessor, IWailaConfigHandler config) {
//        getProvider(accessor.getBlock()).onHwylaBodyRequest(
//                itemStack, tooltip, accessor, config
//        );
//
//        return tooltip;
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
//     *
//     * @return the modified tail tooltip array.
//     */
//    @Override
//    public List<String> getWailaTail(ItemStack itemStack, List<String> tooltip,
//                                     IWailaDataAccessor accessor, IWailaConfigHandler config) {
//        getProvider(accessor.getBlock()).onHwylaTailRequest(
//                itemStack, tooltip, accessor, config
//        );
//
//        return tooltip;
//    }
//
//    /**
//     * Casts the given object to an HwylaDataProvider
//     * if possible.
//     *
//     * @param object possible HwylaDataProvider object.
//     * @return the object cast to an HwylaDataProvider or a blank
//     * tool provider if the given object could not be cast.
//     */
//    private static HwylaDataProvider getProvider(Object object){
//        if(object instanceof HwylaDataProvider)
//            return (HwylaDataProvider)object;
//        else
//            return BLANK_PROVIDER;
//    }
//}
//
