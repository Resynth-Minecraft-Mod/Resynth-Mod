/*
 * Copyright 2018 Ki11er_wolf
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
package com.ki11erwolf.resynth.igtooltip;

import com.ki11erwolf.resynth.ResynthMod;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Class that allows Resynth to interface with Hwyla/Waila
 * through its own in-game tooltip provider api.
 */
public class HwylaCompatibility implements IWailaDataProvider {

    /**
     * Resynth-Hwyla interface API instance.
     */
    public static final HwylaCompatibility INSTANCE = new HwylaCompatibility();

    /**
     * Resynth logger instance.
     */
    public static final Logger LOG = ResynthMod.getLogger();

    /**
     * True if the APIs register method was called.
     */
    private static boolean registered;

    /**
     * True if the API was successfully loaded (No errors
     * thrown during registration).
     */
    private static boolean loaded;

    //Private constructor.
    private HwylaCompatibility() {}

    /**
     * Public init/register method.
     *
     * Sets up the Hwyla interface API.
     */
    static void register(){
        if (registered)
            return;
        registered = true;
        FMLInterModComms.sendMessage(
                "waila",
                "register",
                "com.ki11erwolf.resynth.igtooltip.HwylaCompatibility.load"
        );
    }

    /**
     * Called by Hwyla. <b>DO NOT CALL!</b>
     *
     * @param registrar Given Hwyla block registrar API.
     */
    public static void load(IWailaRegistrar registrar) {
        LOG.info("HwylaCompatibility.load()");

        if (!registered){
            throw new RuntimeException("Please register this handler using the provided method.");
        }

        if (!loaded) {
            ResynthIGTooltips.getTooltipClasses().forEach(item -> {
                registrar.registerHeadProvider(INSTANCE, item);
                registrar.registerBodyProvider(INSTANCE, item);
                registrar.registerTailProvider(INSTANCE, item);
            });

            ResynthIGTooltips.getTileEntityClasses().forEach(
                    item -> registrar.registerNBTProvider(INSTANCE, item)
            );

            loaded = true;
        }
    }

    //****************************
    //Hwyla Data Provider Methods
    //****************************

    /**
     * Hwyla client side NBT data provider request event.
     *
     * Used to pass server side tile entity data to the client
     * side though NBT data.
     *
     * @param player player looking at a block.
     * @param te blocks tile entity.
     * @param tag Hwyla client side NBT data.
     * @param world current world the player is in.
     * @param pos position of the block the player is looking at.
     *
     * @return the modified given NBT data.
     */
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te,
                                     NBTTagCompound tag, World world, BlockPos pos) {

        getProvider(world.getBlockState(pos).getBlock()).onHwylaNBTDataRequest(
                player, te, tag, world, pos
        );

        return tag;
    }

    /**
     * Hwyla request method to get the ItemStack
     * to show for the block.
     *
     * @param accessor Hwyla data provider.
     * @param config Current Hwyla config settings.
     * @return the ItemStack to show, or {@code null} to show the default.
     */
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return getProvider(accessor.getBlock()).onHwylaItemStackRequest(
                accessor, config
        );
    }

    /**
     * Hwyla request method to get the head (title) of the tooltip.
     * This is usually the block name.
     *
     * @param itemStack the block ItemStack.
     * @param tooltip the head tooltip array (containing the block name).
     * @param accessor Hwyla data provider.
     * @param config current Hwyla config settings.
     *
     * @return the modified head tooltip array.
     */
    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> tooltip,
                                     IWailaDataAccessor accessor, IWailaConfigHandler config) {

        getProvider(accessor.getBlock()).onHwylaHeadRequest(
                itemStack, tooltip, accessor, config
        );

        return tooltip;
    }

    /**
     * Hwyla request method to get the body of the tooltip.
     * This is usually the method you want to use to provide
     * the custom block information.
     *
     * @param itemStack the block ItemStack.
     * @param tooltip the body tooltip array (normally empty).
     * @param accessor Hwyla data provider.
     * @param config current Hwyla config settings.
     *
     * @return the modified body tooltip array.
     */
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip,
                                     IWailaDataAccessor accessor, IWailaConfigHandler config) {

        getProvider(accessor.getBlock()).onHwylaBodyRequest(
                itemStack, tooltip, accessor, config
        );

        return tooltip;
    }

    /**
     * Hwyla request method to get the head (title) of the tooltip.
     * This is usually the block name.
     *
     * @param itemStack the block ItemStack.
     * @param tooltip the tail tooltip array (normally the mod name).
     * @param accessor Hwyla data provider.
     * @param config current Hwyla config settings.
     *
     * @return the modified tail tooltip array.
     */
    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> tooltip,
                                     IWailaDataAccessor accessor, IWailaConfigHandler config) {

        getProvider(accessor.getBlock()).onHwylaTailRequest(
                itemStack, tooltip, accessor, config
        );

        return tooltip;
    }

    /**
     * Casts the given object to an IGTooltipProvider
     * if possible.
     *
     * @param object possible IGTooltipProvider object.
     * @return the object cast to an IGTooltipProvider or a blank
     * tool provider if the given object could not be cast.
     */
    private static IGTooltipProvider getProvider(Object object){
        if(object instanceof IGTooltipProvider)
            return (IGTooltipProvider)object;
        else
            return (itemStack, tooltip, accessor, config) -> {
                //NO-OP
            };
    }
}

