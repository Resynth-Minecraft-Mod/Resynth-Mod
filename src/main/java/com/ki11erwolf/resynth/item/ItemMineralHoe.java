/*
 * Copyright 2018-2020 Ki11er_wolf
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
package com.ki11erwolf.resynth.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.MineralHoeConfig;
import com.ki11erwolf.resynth.packet.DisplayHoeInfoPacket;
import com.ki11erwolf.resynth.packet.Packet;
import com.ki11erwolf.resynth.util.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

/**
 * The Mineral Hoe - used as the main tool for the mod.
 *
 * <p/>The Mineral Hoe has many uses, which include:
 * tilling dirt or grass into Mineral Soil, growing
 * plants in creative mode, getting information from
 * blocks and the supporting features the allow
 * completing these functions.
 *
 * <p/>Any block that has information to give to the
 * Mineral Hoe must implement {@link BlockInfoProvider}.
 */
public class ItemMineralHoe extends ResynthItem<ItemMineralHoe> {

    /**
     * The configuration settings for this item.
     */
    private static final MineralHoeConfig CONFIG = ResynthConfig.GENERAL_CONFIG.getCategory(MineralHoeConfig.class);

    /**
     * Logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    /**
     * NBT key used to store the number of charges in the hoe.
     */
    private static final String NBT_TAG_CHARGES = "charges";

    /**
     * Constructs the Mineral Hoe item class.
     *
     * @param name registry name of the item.
     */
    ItemMineralHoe(String name) {
        super(new Properties().maxStackSize(1), name);
    }

    // *******
    // Tooltip
    // *******

    /**
     * {@inheritDoc}.
     *
     * Displays a tooltip with the amount of charges left
     * and how to use the item.
     */
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){

        //Check stack
        ensureChargesNBT(stack);
        if(stack.getTag() == null){
            super.addInformation(stack, worldIn, tooltip, flagIn);
            return;
        }

        //Line spacing
        new ExpandingTooltip().setConditionToControlDown().setExpandedTooltip(CommonTooltips.NEW_LINE)
                .setCollapsedTooltip(null).write(tooltip);

        //Add charges tooltip
        tooltip.add(
                getFormattedTooltip(
                        "mineral_hoe_charges", TextFormatting.GOLD,
                        getCharges(stack) > 1 ? TextFormatting.AQUA : TextFormatting.RED,
                        getCharges(stack)
                )
        );

        //Add default information tooltip.
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    // ***********
    // Charges NBT
    // ***********

    /**
     * Ensures that the charges NBT tag and
     * value is set on the given ItemStack.
     *
     * <p/>This should ALWAYS be called before
     * interacting with the charges NBT.
     *
     * @param stack the given ItemStack.
     */
    private static void ensureChargesNBT(ItemStack stack){
        CompoundNBT nbt = stack.getTag();

        if(nbt == null){
            LOG.info("Setting NBT tag on a Mineral Hoe item stack...");
            nbt = new CompoundNBT();
            nbt.putInt(NBT_TAG_CHARGES, (Math.min(CONFIG.getInitialCharges(), 2)));
            stack.setTag(nbt);
        }
    }

    /**
     * Used to get the amount of charges available
     * to the given Mineral Hoe item stack. Will
     * ensure charges NBT is correct on the hoe
     * as well.
     *
     * @param stack the Mineral Hoe item stack.
     * @return the number of charges available
     * to the hoe.
     */
    private static int getCharges(ItemStack stack){
        ensureChargesNBT(stack);

        CompoundNBT tag = stack.getTag();
        return tag == null ? -1 : tag.getInt(NBT_TAG_CHARGES);
    }

    /**
     * Sets the number of charges on the
     * given Mineral Hoe item stack. Will
     * ensure charges NBT is correct on
     * the hoe as well.
     *
     * @param stack the Mineral Hoe item stack.
     * @param charges the number of charges the
     *                Mineral Hoe should have.
     * @return {@code true} if the operation
     * succeeded.
     */
    private static boolean setCharges(ItemStack stack, int charges){
        ensureChargesNBT(stack);

        CompoundNBT tag = stack.getTag();
        if(tag == null)
            return false;

        tag.putInt(NBT_TAG_CHARGES, charges);
        stack.setTag(tag);
        return true;
    }

    /**
     * Increments the number of charges on the
     * given Mineral Hoe item stack by 1. Will
     * ensure the charges NBT is correct on
     * the hoe as well.
     *
     * @param stack the Mineral Hoe item stack.
     * @return {@code true} if the operation
     * succeeded.
     */
    private static boolean incrementCharges(ItemStack stack){
        int charges = getCharges(stack);

        if(charges >= CONFIG.getMaxCharges())
            return false;

        return setCharges(stack, charges + 1);
    }

    /**
     * Decrements the number of charges on the
     * given Mineral Hoe item stack by 1. Will
     * ensure the charges NBT is correct on
     * the hoe as well.
     *
     * @param stack the Mineral Hoe item stack.
     * @return {@code true} if the operation
     * succeeded.
     */
    @SuppressWarnings("UnusedReturnValue")
    private static boolean decrementCharges(ItemStack stack){
        int charges = getCharges(stack);

        if(charges <= 0)
            return false;

        return setCharges(stack, charges - 1);
    }

    // ****************
    // MC Action Events
    // ****************

    /**
     * Called when this item type is used on a block in the
     * world (excluding air). This method simply delegates
     * the task to the more generalized
     * {@link #onBlockClick(World, BlockPos, BlockState, ItemStack, PlayerEntity, Direction)}
     * and {@link #onBlockShiftClick(World, BlockPos, BlockState, ItemStack, PlayerEntity)}
     * methods which actually handle the task.
     *
     * @param context the context in which the item was used.
     * @return {@link ActionResultType#SUCCESS} if the use action
     * resulted in an action being performed, {@link ActionResultType#FAIL}
     * if it did not.
     */
    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        boolean success;

        if(context.getPlayer() == null) {
            success = false;
        } else if(context.getPlayer().isCrouching()) {
            success = onBlockShiftClick(context.getWorld(), context.getPos(),
                context.getWorld().getBlockState(context.getPos()), context.getItem(), context.getPlayer()
            );
        } else {
            success = onBlockClick(
                    context.getWorld(), context.getPos(), context.getWorld().getBlockState(context.getPos()),
                    context.getItem(), context.getPlayer(), context.getFace()
            );
        }

        return (success) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
    }

    /**
     * Called when this item is used by itself (not targeting a block).
     * This method simply delegates the task to the more generalized
     * {@link #onItemClick(World, PlayerEntity, ItemStack)}
     * and {@link #onItemShiftClick(World, PlayerEntity, ItemStack)}
     * methods which actually handle the task.
     *
     * @param world the world the item was used in.
     * @param player the player that used the item.
     * @param hand the hand the item was in when used.
     * @return {@link ActionResultType#SUCCESS} if the use action
     * resulted in an action being performed, {@link ActionResultType#FAIL}
     * if it did not.
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        boolean success;

        if(player.isCrouching())    success = onItemShiftClick(world, player, stack);
        else                        success = onItemClick(world, player, stack);

        return (success ? ActionResult.resultSuccess(stack) : ActionResult.resultFail(stack));
    }

    // **********************
    // Specific Action Events
    // **********************

    /**
     * Called when this item (in a stack) is used when targeting a
     * block (excluding air). This method will not be called if
     * the player is sneaking!
     *
     * <p/>Attempts to till the targeted block when called
     * or get information from the block if it is not tillable.
     *
     * @param world the world the item was used in.
     * @param pos the position of the targeted block.
     * @param block the state of the targeted block.
     * @param item the item stack used.
     * @param player the player that used the item.
     * @param face the direction the block was targeted from.
     * @return {@code true} if the call resulted in an action,
     * {@code false} otherwise.
     */
    private boolean onBlockClick(World world, BlockPos pos, BlockState block, ItemStack item, PlayerEntity player,
                                 Direction face){
        boolean tilled = tryTillBlock(world, pos, block, item, player, face);

        if(!tilled) //return tryGetInfo(world, pos, block, player);
            return getBlockInformation(world, pos, block, player);

        return true;
    }

    /**
     * Called when this item (in a stack) is used when targeting a
     * block (excluding air).
     *
     * <br>
     * This method will not be called if
     * the player is NOT sneaking!
     *
     * <p/>Attempts to grow the targeted plant or
     * charge the Mineral Hoe.
     *
     * @param world the world the item was used in.
     * @param pos the position of the targeted block.
     * @param block the state of the targeted block.
     * @param item the item stack used.
     * @param player the player that used the item.
     * @return {@code true} if the call resulted in an action,
     * {@code false} otherwise.
     */
    private boolean onBlockShiftClick(World world, BlockPos pos, BlockState block,
                                      ItemStack item, PlayerEntity player){
        if(tryGrowPlant(player, world, block, pos))
            return true;

        return tryCharge(world, player, item);
    }

    /**
     * Called when this item (in a stack) is used when not targeting a
     * block(i.e. air). This method will not be called if
     * the player is sneaking!
     *
     * <p/>Attempts to charge the Mineral Hoe item stack.
     *
     * @param world the world the item was used in.
     * @param item the item stack used.
     * @param player the player that used the item.
     * @return {@code true} if the call resulted in an action,
     * {@code false} otherwise.
     */
    private boolean onItemClick(World world, PlayerEntity player, ItemStack item){
        return tryCharge(world, player, item);
    }

    /**
     * Called when this item (in a stack) is used when not targeting a
     * block(i.e. air). This method will not be called if
     * the player NOT is sneaking!
     *
     * <p/>Attempts to charge the Mineral Hoe item stack
     * or display the number of charges on the Hoe.
     *
     * @param world the world the item was used in.
     * @param item the item stack used.
     * @param player the player that used the item.
     * @return {@code true} if the call resulted in an action,
     * {@code false} otherwise.
     */
    private boolean onItemShiftClick(World world, PlayerEntity player, ItemStack item){
        boolean charged = tryCharge(world, player, item);

        if(!charged)
            displayCharges(world, player, item);

        return charged;
    }

    // ********
    // Charging
    // ********

    /**
     * Will try and charge the given Mineral Hoe item stack
     * from a Mineral Crystal in the given players inventory.
     * If no crystal can be found the Hoe won't be charged.
     *
     * @param world the world the player is in.
     * @param player the player itself.
     * @param item the Mineral Hoe item stack.
     * @return {@code true} if a crystal was found
     * and the Hoe was charged, {@code false} otherwise.
     */
    private boolean tryCharge(World world, PlayerEntity player, ItemStack item){
        int charges = getCharges(item);

        if(charges >= CONFIG.getMaxCharges()){
            return false;
        }

        if(player.isCreative() || tryTakeCrystal(player)){
            if(incrementCharges(item)){
                if(CONFIG.playChargeSound())
                    EffectsUtil.playNormalSound(
                            world, player, new BlockPos(player.getPositionVec()),
                            SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.BLOCKS
                    );

                displayCharges(world, player, item);
                return true;
            }
        }

        return false;
    }

    /**
     * Will try and take a charge (Mineral Crystal) from
     * anywhere within the given players inventory.
     *
     * @param player the player to take the crystal from.
     * @return {@code true} if a crystal was found and taken,
     * {@code false} otherwise.
     */
    private boolean tryTakeCrystal(PlayerEntity player){
        for(ItemStack itemStack : player.container.getInventory()){
            if(itemStack.getItem() == ResynthItems.ITEM_MINERAL_CRYSTAL){
                itemStack.shrink(1);
                return true;
            }
        }

        return false;
    }

    /**
     * Will send a message to the given player telling
     * them how many charges are on the given Mineral
     * Hoe item stack.
     *
     * @param world the world the player is in.
     * @param player the player to send the message to.
     * @param item the Mineral Hoe item stack.
     */
    private void displayCharges(World world, PlayerEntity player, ItemStack item){
        if(!world.isRemote)
            return;

        player.sendMessage(
            new StringTextComponent(TextFormatting.GOLD + getFormattedTooltip(
                    "mineral_hoe_charges", TextFormatting.GOLD,
                    getCharges(item) > 1 ? TextFormatting.AQUA : TextFormatting.RED,
                    getCharges(item)).getString()
            ), player.getUniqueID()
        );
    }

    // *******
    // Tilling
    // *******

    /**
     * Will attempt to till the given block into Mineral Soil.
     * If all checks pass (input block, direction, charges, ect),
     * the block will be tilled, effects will be played and a charge
     * will be taken.
     *
     * @param world the world the block to till is in.
     * @param pos the position of the block to till in the world.
     * @param state the state of the block to till in the world.
     * @param item the item used to till the block (Mineral Hoe stack).
     * @param player the player tilling the block.
     * @param face the direction the block is being tilled from.
     * @return {@code true} if the given block was successfully
     * tilled into Mineral Soil, {@code false} otherwise.
     */
    private boolean tryTillBlock(World world, BlockPos pos, BlockState state, ItemStack item,
                                 PlayerEntity player, Direction face){

        //Check for dirt and grass.
        if(!(state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.GRASS_BLOCK))
            return false;

        //Check direction
        if (!(face != Direction.DOWN && world.isAirBlock(pos.up()))) {
            return false;
        }

        //Check for creative and charges.
        if(!(player.isCreative() || getCharges(item) > 0)){
            if(CONFIG.playFailSound())
                EffectsUtil.playNormalSound(
                        world, player, pos, SoundEvents.BLOCK_NOTE_BLOCK_HAT, SoundCategory.BLOCKS
                );
            return false;
        }

        //If all checks pass

        //Replace
        boolean replaced = tillBlock(world, pos, player);

        //Finally remove charge if replaced.
        if(replaced && !player.isCreative()){
            decrementCharges(item);
        }

        return replaced;
    }

    /**
     * Will till the given block into Mineral Soil regardless of block.
     * Effects will also be played along with the tilling action.
     *
     * @param world the world the block to till is in.
     * @param pos the position of the block in the world to till.
     * @param player the player tilling the block.
     * @return {@code true} if the block was successfully tilled/replaced.
     */
    private boolean tillBlock(World world, BlockPos pos, PlayerEntity player){
        boolean replaced = world.setBlockState(pos, ResynthBlocks.BLOCK_MINERAL_SOIL.getDefaultState());

        if(replaced){
            //Particles
            if(CONFIG.showParticles())
                EffectsUtil.displayStandardEffects(world, pos.up(), 5, ParticleTypes.FLAME);

            //Sound
            EffectsUtil.playNormalSound(
                    world, player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS
            );
        }

        return replaced;
    }

    // ********************
    // Information Provider
    // ********************

    /**
     * Will attempt to get the provided information from
     * the given block if it a {@link BlockInfoProvider},
     * if it is not, the method will simply return.
     *
     * @param world the world the block is in.
     * @param pos the position of the block in the world.
     * @param block the state of the block in the world.
     * @param player the player using the Mineral Hoe.
     * @return {@code true} if the given block is a
     * {@link BlockInfoProvider} and information could
     * be obtained successfully, {@code false} otherwise.
     */
    private boolean tryGetInfo(World world, BlockPos pos, BlockState block, PlayerEntity player){
        if(!(block.getBlock() instanceof BlockInfoProvider))
            return false;

        //We want this done on the server side to get correct information.
        if(world.isRemote)
            return false;

        BlockInfoProvider infoProvider = (BlockInfoProvider)block.getBlock();
        player.sendMessage(new StringTextComponent(
                getInfoFromProvider(infoProvider, world, pos, block)
        ), player.getUniqueID());

        return true;
    }

    /**
     * Will get the information given from the given
     * BlockInfoProvider Block.
     *
     * @param provider the specific info provider block.
     * @param world the world the block is in.
     * @param pos the position of the block in the world.
     * @param block the state of the block in the world.
     * @return the information from the provider as a
     * complete string.
     */
    private String getInfoFromProvider(BlockInfoProvider provider, World world, BlockPos pos, BlockState block){
        List<String> informationList = new ArrayList<>();
        provider.appendBlockInformation(informationList, world, pos, block);

        StringBuilder informationText = new StringBuilder();

        for(String informationLine : informationList){
            informationText.append(informationLine).append("\n");
        }

        return informationText.toString();
    }

    // ************
    // Plant Growth
    // ************

    /**
     * Attempts to grow a plant similar to bonemeal,
     * however it ignores {@link IGrowable#canGrow(IBlockReader, BlockPos, BlockState, boolean)}
     * and {@link IGrowable#canUseBonemeal(World, Random, BlockPos, BlockState)}. This method
     * performs a creative check and only works for players in creative.
     *
     * @return {@code true} if the plant could be grown.
     */
    private boolean tryGrowPlant(PlayerEntity player, World world, BlockState block, BlockPos pos){
        if(!player.isCreative())
            return false;//Creative check.

        if(block.getBlock() instanceof IGrowable){
            if(!world.isRemote)
                ((IGrowable)block.getBlock()).grow((ServerWorld)world, random, pos, world.getBlockState(pos));
            return true;
        }

        return false;
    }

    // ********************
    // Information Provider
    // ********************

    private boolean getBlockInformation(World world, BlockPos pos, BlockState block, PlayerEntity player) {
        MineralHoeInfoProvider provider;
        Map<String, String[]> information;

        // Check if block is provider and has provided
        if((provider = asProvider(block)) != null){
            if((information = callProvider(provider, block, world, pos)) == null) {
                return false;
            }
        } else return false;

        // Information obtained! Convert to serializable HoeInformation
        MineralHoeInformation hoeInformation = new MineralHoeInformation(information);

        if(player instanceof ServerPlayerEntity) {
            Packet.send(PacketDistributor.PLAYER.with(
                    () -> (ServerPlayerEntity) player), new DisplayHoeInfoPacket(hoeInformation)
            );

            return true;
        }

        return false;
    }

    @Nullable
    private MineralHoeInfoProvider asProvider(BlockState blockState) {
        if(blockState.getBlock() instanceof MineralHoeInfoProvider){
            return (MineralHoeInfoProvider) blockState.getBlock();
        }

        return null;
    }

    @Nullable
    private static Map<String, String[]> callProvider(MineralHoeInfoProvider provider, BlockState state, World world, BlockPos pos) {
        Map<String, String[]> cleanMap = new HashMap<>();
        boolean passed;

        if(world.isRemote)
            return null;

        try{
            passed = provider.provideHoeInformation(cleanMap, state, world, pos);
        } catch (Exception e){
            LOG.warn("Information provider '" + provider.getClass().getCanonicalName() + "' threw exception when queried", e);
            return null;
        }

        if(!passed || cleanMap.isEmpty())
            return null;

        return cleanMap;
    }

    // *************************
    // Information Serialization
    // *************************

    public static class MineralHoeInformation implements JSerializer.JSerializable<MineralHoeInformation> {

        public static JSerializer<MineralHoeInformation> INFORMATION_SERIALIZER_INSTANCE = new Serializer();

        private static final String SERIALIZER_IDENTIFICATION = "mineral-hoe-information";

        private static final String INFORMATION_MAP_KEY = "information-map";

        private final Map<String, String[]> information;

        private MineralHoeInformation() {
            this(new HashMap<>());
        }

        private MineralHoeInformation(Map<String, String[]> information) {
            this.information = Objects.requireNonNull(information);
        }

        @Override
        public JSerializer<MineralHoeInformation> getSerializer() {
            return INFORMATION_SERIALIZER_INSTANCE;
        }

        public Map<String, String[]> getInformation() {
            return this.information;
        }

        // Serializer implementation

        private static final class Serializer extends JSerializer<MineralHoeInformation> {

            private Serializer() {
                super(SERIALIZER_IDENTIFICATION);
            }

            @Override
            protected void objectToData(MineralHoeInformation object, JSerialDataIO dataIO) {
                Map<String, String[]> information = object.getInformation();

                if(information.isEmpty())
                    throw new IllegalArgumentException("HoeInformation object contains empty information map.");

                JsonObject jsonInformationMap = new JsonObject();
                for(Map.Entry<String, String[]> info : information.entrySet()) {
                    JsonArray array = new JsonArray(); String key = info.getKey();

                    for(String element : info.getValue()){
                        array.add(element);
                    }

                    jsonInformationMap.add(key, array);
                }

                dataIO.add(INFORMATION_MAP_KEY, jsonInformationMap);
            }

            @Override
            protected MineralHoeInformation dataToObject(MineralHoeInformation newObject, JSerialDataIO dataIO) {
                JsonObject jsonInformation = dataIO.getObject(INFORMATION_MAP_KEY, null);
                Map<String, String[]> information = new HashMap<>();

                if(jsonInformation == null || jsonInformation.size() == 0)
                    throw new IllegalArgumentException("HoeInformation object data gave an empty map.");

                for(Map.Entry<String, JsonElement> info : jsonInformation.entrySet()) {
                    String key = info.getKey(); JsonArray jValues = info.getValue().getAsJsonArray();
                    List<String> values = new ArrayList<>();

                    for(JsonElement jElement : jValues) {
                        values.add(jElement.getAsString());
                    }

                    information.put(key, values.toArray(new String[0]));
                }

                return new MineralHoeInformation(information);
            }

            @Override
            protected MineralHoeInformation createInstance() {
                return new MineralHoeInformation();
            }
        }
    }

    // *************************
    // Information Provder Impl
    // *************************

    /**
     * An interface provided by the {@link ItemMineralHoe} that allows any clickable {@link net.minecraft.block.Block}
     * to provide any information or data, to the MineralHoe when requested as part of its information feature.
     *
     * <p/> The {@link MineralHoeInfoProvider} is designed to be client-side safe while still providing server-side information
     * and data. This means the information provided to the MineralHoe is not plant text, but rather as a map of
     * language translation keys mapped to a list data used to format the translation text referenced by key.
     */
    public interface MineralHoeInfoProvider {

        /**
         * Called when an {@link ItemMineralHoe} is used on this block with a right-click,
         * signaling a request for the this Block implmentation to provide the data it would
         * normally.
         *
         * <p/> Given the client-side language translation safe nature of the MineralHoeInfo
         * provider interface, text cannot be sent directly through this interface, nor can
         * client-side classes be used. Instead, a list of language translation keys are
         * added to the {@code information} map for each line of text, which is then formatted
         * using the array of data mapped to the key.
         *
         * @param information the map of language translation keys along the data used when
         * formatting the language translation text.
         * @param state the state of this specific block in the world as it exists currently.
         * @param world the world the block and player are in.
         * @param pos the position of the specific block clicked in the world provided.
         *
         * @return {@code true} if the information providing feature should continue as normal,
         * and provide the information to the player. Returning {@code false} will signal to the
         * MineralHoe to instead not provide the information to the player.
         */
        boolean provideHoeInformation(Map<String, String[]> information, BlockState state, World world, BlockPos pos);

    }
}
