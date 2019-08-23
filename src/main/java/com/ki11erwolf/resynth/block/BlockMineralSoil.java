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
package com.ki11erwolf.resynth.block;

import com.ki11erwolf.resynth.block.tileEntity.ResynthTileEntity;
import com.ki11erwolf.resynth.block.tileEntity.TileEntityMineralSoil;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.MineralSoilConfig;
import com.ki11erwolf.resynth.item.ResynthItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Mineral Enriched Soil.
 * <p/>
 * Resynth's farmland block. Used to plant Resynth Plants.
 * Has a Mineral Content value associated with it that
 * determines plant growth.
 */
@SuppressWarnings("deprecation")
public class BlockMineralSoil extends ResynthTileEntity<TileEntityMineralSoil> {

    /**
     * Configuration settings for this block class.
     */
    private static final MineralSoilConfig CONFIG
            = ResynthConfig.GENERAL_CONFIG.getCategory(MineralSoilConfig.class);

    /**
     * The Mineral Content stage this block is on. Determines
     * the texture of the block based on the Mineral Content.
     */
    private static final IntegerProperty STAGE
            = IntegerProperty.create("stage", 0, 4);

    /**
     * The shape of the block.
     */
    private static final VoxelShape MINERAL_SOIL_SHAPE = Block.makeCuboidShape(
            0.0D, 0.0D, 0.0D,
            16.0D, 15.0D, 16.0D
    );

    /**
     * Default constructor.
     */
    BlockMineralSoil(){
        super(
                Properties.create(Material.GRASS).hardnessAndResistance(2.0F).sound(SoundType.GROUND),
                "mineral_soil"
        );
        this.setDefaultState(this.stateContainer.getBaseState().with(STAGE, 0));
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Sets the tooltip for the block.
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn){
        ResynthBlock.setDescriptiveTooltip(tooltip, this);
    }

    // ***
    // LAF
    // ***

    /**
     * {@inheritDoc}
     * <p/>
     * Sets the shape (i.e. collision/bounding box) of the block.
     *
     * @return {@link #MINERAL_SOIL_SHAPE}.
     */
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return MINERAL_SOIL_SHAPE;
    }

    /**
     * {@inheritDoc}
     * @return {@code false}.
     */
    @Override
    public boolean isNormalCube(IBlockState state){
        return false;
    }

    /**
     * {@inheritDoc}
     * @return {@code false}.
     */
    @Override
    public boolean isFullCube(IBlockState state){
        return false;
    }

    /**
     * Handles updating the blocks texture based
     * on the Mineral Content of the block.
     *
     * @param mineralContent the blocks/tile entities Mineral Content.
     * @param world the world the block is in.
     * @param state the state of the block. i.e. BlockState.
     * @param pos the BlockPos of the block in the world.
     */
    private void updateState(float mineralContent, World world, IBlockState state, BlockPos pos){
        if(mineralContent > 49.9){
            world.setBlockState(pos, state.with(STAGE, 4));
        } else if(mineralContent > 39.9){
            world.setBlockState(pos, state.with(STAGE, 3));
        } else if(mineralContent > 29.9){
            world.setBlockState(pos, state.with(STAGE, 2));
        } else if(mineralContent > 19.9){
            world.setBlockState(pos, state.with(STAGE, 1));
        } else world.setBlockState(pos, state.with(STAGE, 0));
    }

    // *****
    // State
    // *****

    /**
     * {@inheritDoc}.
     * <p/>
     * Makes the {@link #STAGE} property known.
     *
     * @param builder StateContainer builder.
     */
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(STAGE);
    }

    // **********
    // Drop Logic
    // **********

    /**
     * {@inheritDoc}
     *
     * @return {@link Blocks#DIRT}.
     */
    @Override
    public Item getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune){
        return Item.getItemFromBlock(Blocks.DIRT);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Handles dropping the {@link ResynthItems#ITEM_MINERAL_CRYSTAL} required to
     * make Mineral Soil as well the number of {@link ResynthItems#ITEM_MINERAL_ROCK}'s
     * put into the Mineral Soil block.
     */
    public void onReplaced(IBlockState state, World world, BlockPos pos, IBlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            //Checks
            TileEntity tileentity = world.getTileEntity(pos);
            if (!(tileentity instanceof TileEntityMineralSoil)) {
                super.onReplaced(state, world, pos, newState, isMoving);
            }

            //Mineral Crystal
            InventoryHelper.spawnItemStack(
                    world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ResynthItems.ITEM_MINERAL_CRYSTAL)
            );

            //Mineral Rocks
            assert tileentity != null;
            assert tileentity instanceof TileEntityMineralSoil;
            float content = ((TileEntityMineralSoil)tileentity).getMineralPercentage();

            int rocks = (int)((content - CONFIG.getStartingMineralContent())/CONFIG.getMineralRockWorth());
            InventoryHelper.spawnItemStack(
                    world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ResynthItems.ITEM_MINERAL_ROCK, rocks)
            );

            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }

    // ***********
    // Tile Entity
    // ***********

    /**
     * {@inheritDoc}
     * @return {@code {@link TileEntityMineralSoil}.class}.
     */
    @Override
    public Class<TileEntityMineralSoil> getTileEntityClass() {
        return TileEntityMineralSoil.class;
    }

    /**
     * {@inheritDoc}
     * @return {@code new} {@link TileEntityMineralSoil}.
     */
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
        return new TileEntityMineralSoil();
    }

    // *****
    // Logic
    // *****

    /**
     * {@inheritDoc}
     *
     * <p/>
     *
     * Handles adding Mineral Rocks/Dense Mineral Rocks to the Mineral Soil
     * block when a player uses the item on the block. Also handles the changing
     * the texture of the block and displaying the Mineral Content when the block is
     * hit with a stick.
     *
     * @return {@code true} if the item used is an accepted (usable) item.
     */
    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player,
                                    EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
        ItemStack usedItem = player.getHeldItem(hand);
        TileEntityMineralSoil entityMineralSoil = getTileEntity(world, pos);
        float mineralContent = entityMineralSoil.getMineralPercentage();
        float increase;

        //Debug help //TODO: Maybe move to the Mineral Hoe class
        if(usedItem.getItem() == ResynthItems.ITEM_MINERAL_HOE){
            if(!world.isRemote)
                player.sendMessage(new TextComponentString(I18n.format(
                        "misc.resynth.mineral_content", entityMineralSoil.getMineralPercentage()
                )));
            return true;
        }

        //At max mineral content
        if(mineralContent >= 50){
            return false;
        }

        //Determined used item.
        if(usedItem.getItem() == ResynthItems.ITEM_MINERAL_ROCK){
           increase = (float)CONFIG.getMineralRockWorth();
        } else if(usedItem.getItem() == ResynthItems.ITEM_DENSE_MINERAL_ROCK){
            increase = (float)CONFIG.getMineralRockWorth() * 9;
        }  else return false;

        //Do increase
        if(world.isRemote)
            return true;

        if(!player.isCreative()){
            usedItem.shrink(1);
        }

        entityMineralSoil.increaseMineralPercentage(increase);
        updateState(entityMineralSoil.getMineralPercentage(), world, state, pos);

        if(CONFIG.isChatMessageEnabled())
            player.sendMessage(new TextComponentString(I18n.format(
                    "misc.resynth.mineral_content", entityMineralSoil.getMineralPercentage()
            ) + "%"));

        return true;
    }
}
