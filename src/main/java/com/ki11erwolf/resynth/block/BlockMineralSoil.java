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
package com.ki11erwolf.resynth.block;

import com.ki11erwolf.resynth.block.tileEntity.ResynthTileEntity;
import com.ki11erwolf.resynth.block.tileEntity.TileEntityMineralSoil;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.MineralSoilConfig;
import com.ki11erwolf.resynth.item.ResynthItems;
import com.ki11erwolf.resynth.util.MinecraftUtil;
import com.ki11erwolf.resynth.util.PlantPatchInfoProvider;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

//import mcp.mobius.waila.api.IComponentProvider;
//import mcp.mobius.waila.api.IDataAccessor;
//import mcp.mobius.waila.api.IPluginConfig;
//import mcp.mobius.waila.api.IServerDataProvider;

/**
 * Mineral Enriched Soil.
 * <p/>
 * Resynth's farmland block. Used to plant Resynth Plants.
 * Has a Mineral Content value associated with it that
 * determines plant growth.
 */
@SuppressWarnings("deprecation")
public class BlockMineralSoil extends ResynthTileEntity<TileEntityMineralSoil> implements PlantPatchInfoProvider,
        IComponentProvider, IServerDataProvider<TileEntity> {

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
            = IntegerProperty.create("stage", 0, 4 + 2);

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
    BlockMineralSoil(String name){
        super(Properties.create(Material.EARTH).hardnessAndResistance(2.0F).sound(SoundType.GROUND), name);
        this.setDefaultState(this.stateContainer.getBaseState().with(STAGE, 0));
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
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return MINERAL_SOIL_SHAPE;
    }

//    /**
//     * {@inheritDoc}
//     * @return {@code false}.
//     */
//    @Override
//    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos){
//        return false;
//    }

    /**
     * Handles updating the blocks texture based
     * on the Mineral Content of the block and any
     * Enhancer blocks underneath the Mineral Soil block.
     *
     * @param mineralContent the blocks/tile entities Mineral Content.
     * @param world the world the block is in.
     * @param state the state of the block. i.e. BlockState.
     * @param pos the BlockPos of the block in the world.
     */
    private void updateState(float mineralContent, World world, BlockState state, BlockPos pos){
        if(mineralContent > 49.9){
            world.setBlockState(pos, state.with(STAGE, 4 + getStageIncrease(world, pos)));
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
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    // **********
    // Pick Block
    // **********

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(this);
    }

    // **********
    // Drop Logic
    // **********

    /**
     * {@inheritDoc}
     *
     * Handles dropping the extra drops - Mineral Crystal & Mineral Rocks.
     */
    @Override
    public void spawnAdditionalDrops(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack) {
        if(world.isRemote)
            return;

        //Dirt and Mineral Crystal
        MinecraftUtil.spawnItemInWorld(ResynthItems.ITEM_MINERAL_CRYSTAL, world, pos);
        MinecraftUtil.spawnItemStackInWorld(new ItemStack(Blocks.DIRT), world, pos);

        super.spawnAdditionalDrops(state, world, pos, stack);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Handles dropping the {@link ResynthItems#ITEM_MINERAL_CRYSTAL} required to
     * make Mineral Soil as well the number of {@link ResynthItems#ITEM_MINERAL_ROCK}'s
     * put into the Mineral Soil block.
     */
    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            //Checks
            TileEntity tileentity = world.getTileEntity(pos);
            if (!(tileentity instanceof TileEntityMineralSoil)) {
                super.onReplaced(state, world, pos, newState, isMoving);
            }

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
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityMineralSoil();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasTileEntity(BlockState state){
        return true;
    }

    // *****
    // Hwyla
    // *****

    /**
     * {@inheritDoc}
     * <p/>
     * Handles displaying the soil blocks mineral content
     * and message in the hwyla tooltip.
     */
    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        tooltip.add(new StringTextComponent(""));
        tooltip.add(new StringTextComponent(
                getMineralContentMessage(
                        accessor.getServerData().getFloat(TileEntityMineralSoil.MINERAL_CONTENT_TAG),
                        accessor.getServerData().getFloat("mineralIncrease")
                )
        ));
        tooltip.add(new StringTextComponent(""));
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Handles sending the server side tile entity data (mineral content)
     * to the client side for presentation in the Hwyla tooltip.
     */
    @Override
    public void appendServerData(CompoundNBT clientServerNBT, ServerPlayerEntity serverPlayerEntity,
                                 World world, TileEntity tileEntity){
        if(!(tileEntity instanceof TileEntityMineralSoil))
            return;

        clientServerNBT.putFloat(
                TileEntityMineralSoil.MINERAL_CONTENT_TAG,
                ((TileEntityMineralSoil) tileEntity).getMineralPercentage()
        );

        clientServerNBT.putFloat(
                "mineralIncrease",
                getMineralContentIncrease(world, tileEntity.getPos())
        );
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
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                           Hand hand, BlockRayTraceResult hit){
        ItemStack usedItem = player.getHeldItem(hand);
        TileEntityMineralSoil entityMineralSoil = getTileEntity(world, pos);

        float mineralContent = entityMineralSoil.getMineralPercentage();
        float increase;

        //At max mineral content
        if(mineralContent >= 50){
            return ActionResultType.FAIL;
        }

        //Determined used item.
        if(usedItem.getItem() == ResynthItems.ITEM_MINERAL_ROCK){
             increase = (float)CONFIG.getMineralRockWorth();
        } else if(usedItem.getItem() == ResynthItems.ITEM_DENSE_MINERAL_ROCK){
            increase = (float)CONFIG.getMineralRockWorth() * 9;
        }  else return ActionResultType.FAIL;

        //Do increase
        if(world.isRemote)
            return ActionResultType.SUCCESS;

        if(!player.isCreative()){
            usedItem.shrink(1);
        }

        entityMineralSoil.increaseMineralPercentage(increase);
        updateState(entityMineralSoil.getMineralPercentage(), world, state, pos);

        if(CONFIG.isChatMessageEnabled())
            player.sendMessage(new StringTextComponent(I18n.format(
                    "misc.resynth.mineral_content", entityMineralSoil.getMineralPercentage()
            ) + "%"), player.getUniqueID());

        return ActionResultType.SUCCESS;
    }

    // *********
    // Enhancers
    // *********

    /**
     * Handles what happens a neighboring block changes.
     *
     * Will make sure the block checks for any Enhancer
     * blocks and update its state accordingly.
     */
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn,
                                BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        updateState(getTileEntity(worldIn, pos).getMineralPercentage(), worldIn, state, pos);
    }

    /**
     * The amount of stages (beyond 4) to increase the soil blocks
     * growth stage by based on the Enhancer (of lack of) block
     * underneath the soil block.
     *
     * @param pos position of the soil block.
     * @return the amount of stages to increase the soil blocks
     * growth stage by (beyond 4).
     */
    private static int getStageIncrease(World world, BlockPos pos){
        BlockState enhancer = world.getBlockState(pos.down());

        if(enhancer.getBlock() instanceof BlockEnhancer){
            return ((BlockEnhancer)enhancer.getBlock()).getStageIncrease();
        }

        return 0;
    }

    /**
     * Used to get the amount by which to increase the
     * Mineral Concentration percentage by from the
     * Enhancer block underneath the soil block.
     *
     * @return the amount to increase the Mineral Content by. {@code 0}
     * if no Enhancer block is beneath the soil block.
     */
    private static float getMineralContentIncrease(World world, BlockPos pos){
        BlockState enhancer = world.getBlockState(pos.down());

        if(enhancer.getBlock() instanceof BlockEnhancer){
            return ((BlockEnhancer)enhancer.getBlock()).getIncrease();
        }

        return 0;
    }

    /**
     * Gets the mineral content message from the
     * lang file formatted with the provided
     * info.
     *
     * @param mineralPercentage the soil blocks mineral content
     *                          value.
     * @return the formatted localized message.
     */
    private static String getMineralContentMessage(float mineralPercentage, float increase){
        return TextFormatting.AQUA + I18n.format(
                "misc.resynth.mineral_content",
                TextFormatting.GOLD + String.valueOf(mineralPercentage + increase)
        );
    }
}
