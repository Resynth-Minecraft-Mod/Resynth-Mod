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
package com.ki11erwolf.resynth.plant.item;

import com.ki11erwolf.resynth.ResynthTabSeeds;
import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.item.ResynthItem;
import com.ki11erwolf.resynth.plant.block.BlockPlantBase;
import com.ki11erwolf.resynth.plant.block.BlockPlantBiochemical;
import com.ki11erwolf.resynth.plant.block.BlockPlantCrystalline;
import com.ki11erwolf.resynth.plant.block.BlockPlantMetallic;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The item class for all plant seeds.
 */
public class ItemPlantSeeds extends ResynthItem implements IPlantable {

    /**
     * Prefix for the item.
     */
    protected static final String PREFIX = "seed";

    /**
     * The plant block the seed item places.
     */
    private final Block plant;

    /**
     * Default item constructor.
     *
     * @param plant the plant block to place.
     * @param name the name of the item (e.g. redstoneDust)
     */
    public ItemPlantSeeds(BlockPlantBase plant, String name) {
        super(name, PREFIX);
        this.plant = plant;
        this.setCreativeTab(ResynthTabSeeds.RESYNTH_TAB_SEEDS);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     *     Places the given plant block
     *     in the world.
     * </p>
     *
     * @param player
     * @param worldIn
     * @param pos
     * @param hand
     * @param facing
     * @param hitX
     * @param hitY
     * @param hitZ
     * @return
     */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos,
                                      EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);
        net.minecraft.block.state.IBlockState state = worldIn.getBlockState(pos);
        if (facing == EnumFacing.UP
                && player.canPlayerEdit(pos.offset(facing), facing, itemstack)
                && worldIn.getBlockState(pos).getBlock() == ResynthBlocks.BLOCK_MINERAL_SOIL
                && worldIn.isAirBlock(pos.up())) {
            worldIn.setBlockState(pos.up(), this.plant.getDefaultState());

            if (player instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos.up(), itemstack);
            }

            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param world
     * @param pos
     * @return {@link EnumPlantType#Crop}
     */
    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    /**
     * {@inheritDoc}
     *
     * @param world
     * @param pos
     * @return the default state of the plant block
     * this seed item places.
     */
    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return this.plant.getDefaultState();
    }

    /**
     * {@inheritDoc}
     * Adds a tooltip on how to obtain the item. This tooltip
     * changes depending on what type of plant it places.
     *
     * @param stack
     * @param worldIn
     * @param tooltip
     * @param flagIn
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip,
                               ITooltipFlag flagIn){
        tooltip.add("Place on Mineral Soil.");
        BlockPlantBase base = (BlockPlantBase) plant;

        if(base instanceof BlockPlantCrystalline)
            tooltip.add("Spawns randomly when mining the ore");
        else if (base instanceof BlockPlantMetallic)
            tooltip.add("Spawns randomly when blowing up the ore");
        else if (base instanceof BlockPlantBiochemical)
            tooltip.add("Spawns randomly when killing the mob");

    }

    /**
     * Plays a sound in the world
     * when ever a seed item is dropped
     *
     * @param worldIn the world in which to play the sound.
     * @param pos the location in the world.
     */
    @SideOnly(Side.CLIENT)
    public static void addEffects(World worldIn, BlockPos pos){
        worldIn.playSound(null, pos,
                SoundEvents.BLOCK_NOTE_BELL, SoundCategory.BLOCKS,
                0.5F, 1.0F);
    }
}
