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
package com.ki11erwolf.resynth.plant.block;

import com.ki11erwolf.resynth.ResynthConfig;
import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.igtooltip.HwylaDataProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * The block class for all plants that
 * produce mob drops.
 */
public abstract class BlockPlantBiochemical extends BlockPlantBase implements HwylaDataProvider {

    /**
     * Age (growth stage) property of the plant type.
     */
    public static final PropertyInteger GROWTH_STAGE = PropertyInteger.create(
            "age", 0, 7);

    /**
     * List of bounding boxes for each growth stage.
     */
    protected static final AxisAlignedBB[] BIO_FLOWER_AABB = new AxisAlignedBB[] {
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.125D, 0.625D),    //Age=0
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.25D, 0.625D),     //Age=1
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.375D, 0.625D),    //Age=2
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.375D, 0.625D),    //Age=3
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.50D, 0.625D),     //Age=4
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.625D, 0.625D),    //Age=5
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.75D, 0.625D),     //Age=6
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.875D, 0.625D)     //Age=7
    };

    /**
     * Creates a new biochemical plant block instance.
     *
     * @param name the name of the plant block instance.
     */
    public BlockPlantBiochemical(String name){
        super(name);
        this.setDefaultState(
                this.blockState.getBaseState().withProperty(this.getGrowthStageProperty(), 0));
        this.setTickRandomly(true);
        this.setCreativeTab(null);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.disableStats();
    }

    /**
     * @param stage -
     * @return the correct block state for the given growth stage.
     */
    public IBlockState withGrowthStage(int stage){
        return this.getDefaultState().withProperty(this.getGrowthStageProperty(), stage);
    }

    /**
     * @param state -
     * @return true if the given plant is fully grown.
     */
    public boolean isMaxGrowthStage(IBlockState state){
        return state.getValue(this.getGrowthStageProperty()) >= this.getMaxGrowthStage();
    }

    /**
     * @return {@code 7}. The maximum growth stage this plant type can grow to.
     */
    public int getMaxGrowthStage(){
        return 7;
    }

    /**
     * Determines if the given plant is placed on the correct block,
     * has the correct light levels and can see the sky.
     *
     * @param worldIn -
     * @param pos -
     * @param state -
     * @return -
     */
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state){
        IBlockState soil = worldIn.getBlockState(pos.down());
        return (worldIn.getLight(pos) >= 8 || worldIn.canSeeSky(pos))
                && soil.getBlock() == ResynthBlocks.BLOCK_MINERAL_SOIL;
    }

    /**
     * Increases the plants growth stage (age) property.
     *
     * This effectively grows the plant in the world.
     *
     * @param worldIn -
     * @param pos -
     * @param state -
     */
    public void grow(World worldIn, BlockPos pos, IBlockState state){
        int i = this.getGrowthStage(state) + this.getBonemealGrowthStageIncrease(worldIn);
        int j = this.getMaxGrowthStage();

        if (i > j){
            i = j;
        }

        worldIn.setBlockState(pos, this.withGrowthStage(i), 2);
    }

    /**
     * @param state -
     * @return true if the provided block is mineral soil.
     */
    protected boolean canSustainBush(IBlockState state){
        return state.getBlock() == ResynthBlocks.BLOCK_MINERAL_SOIL;
    }

    /**
     * @return the plants growth stage (age) property.
     */
    protected PropertyInteger getGrowthStageProperty(){
        return GROWTH_STAGE;
    }

    /**
     * @param state -
     * @return the metadata growth stage of the plant from its
     * block state.
     */
    protected int getGrowthStage(IBlockState state){
        return state.getValue(this.getGrowthStageProperty());
    }

    /**
     * Calculates the amount of stages to grow a plant
     * using Math.random().
     *
     * @param worldIn -
     * @return the calculated amount of stages to grow the plant.
     */
    protected int getBonemealGrowthStageIncrease(World worldIn){
        return MathHelper.getInt(worldIn.rand, 2, 5);
    }

    /**
     * {@inheritDoc}
     * <p><p>
     * Handles growing the plant on a growth approved random tick.
     * This also triggers the forge crop growing hooks.
     *
     * @param worldIn
     * @param pos
     * @param state
     * @param rand
     */
    @Override
    public void onGrowApproved(World worldIn, BlockPos pos, IBlockState state, Random rand){
        if (!worldIn.isAreaLoaded(pos, 1)) return;

        if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
            int i = this.getGrowthStage(state);

            if (i < this.getMaxGrowthStage()){
                if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(
                        worldIn, pos, state, true)){
                    worldIn.setBlockState(pos, this.withGrowthStage(i + 1), 2);
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(
                            worldIn, pos, state, worldIn.getBlockState(pos));
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * Drops the produce and sets the block state to the fourth growth stage.
     *
     * @param worldIn
     * @param pos
     * @param state
     * @param playerIn
     * @param hand
     * @param facing
     * @param hitX
     * @param hitY
     * @param hitZ
     * @return true if the plant can be harvested.
     */
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
                                    EntityPlayer playerIn, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ){
        if(this.getGrowthStage(state) >= getMaxGrowthStage() && !worldIn.isRemote){
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ,
                    SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,
                    0.5F, 0.4F / (new Random().nextFloat() * 0.4F + 0.8F)
            );

            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(),
                    new ItemStack(getProduce(), 1)));

            worldIn.setBlockState(pos, this.withGrowthStage(3), 2);
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * Drops the plants seed type.
     *
     * @param drops
     * @param world
     * @param pos
     * @param state
     * @param fortune
     */
    @Override
    public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops,
                         net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
        super.getDrops(drops, world, pos, state, 0);
        int age = getGrowthStage(state);

        if (age >= getMaxGrowthStage()){
            drops.add(new ItemStack(this.getSeed(), 1, 0));
        }
    }

    /**
     * Determines the correct bounding box to use for the
     * growth stage of the plant.
     *
     * @param state -
     * @param source -
     * @param pos -
     * @return the correct bounding box for the given plants age/growth stage.
     */
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        return BIO_FLOWER_AABB[state.getValue(this.getGrowthStageProperty())];
    }

    /**
     * {@inheritDoc}
     *
     * @param worldIn
     * @param pos
     * @param state
     * @param chance
     * @param fortune
     */
    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune){
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
    }

    /**
     * {@inheritDoc}
     *
     * @param state
     * @param rand
     * @param fortune
     * @return the produce or the seeds.
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return this.isMaxGrowthStage(state) ? this.getProduce() : this.getSeed();
    }

    /**
     * {@inheritDoc}
     * Returns the item stack the player should get
     * when harvesting/picking the block.
     *
     * @param worldIn
     * @param pos
     * @param state
     * @return the plants seed type.
     */
    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state){
        return new ItemStack(this.getSeed());
    }

    /**
     * {@inheritDoc}
     * Determines if the plant can grow further.
     *
     * @param worldIn
     * @param pos
     * @param state
     * @param isClient
     * @return true if this plant is not at its max growth stage.
     */
    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient){
        return !this.isMaxGrowthStage(state);
    }

    /**
     * {@inheritDoc}
     *
     * @param worldIn
     * @param rand
     * @param pos
     * @param state
     * @return
     */
    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state){
        return ResynthConfig.PLANTS_GENERAL.canBonemeal && this.canBonemeal();
    }

    /**
     * {@inheritDoc}
     * IGrowable interface grow method. Calls
     * {@link #grow(World, BlockPos, IBlockState)}
     *
     * @param worldIn
     * @param rand
     * @param pos
     * @param state
     */
    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state){
        this.grow(worldIn, pos, state);
    }

    /**
     * {@inheritDoc}
     *
     * @param meta
     * @return
     */
    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta){
        return this.withGrowthStage(meta);
    }

    /**
     * {@inheritDoc}
     *
     * @param state
     * @return
     */
    @Override
    public int getMetaFromState(IBlockState state){
        return this.getGrowthStage(state);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, GROWTH_STAGE);
    }

    /**
     * {@inheritDoc}
     *
     * Provides the plants growth stage to the Hwyla tooltip.
     *
     * @param itemStack the item stack returned by the block.
     * @param tooltip list of strings to pass to Hwyla to show in the tooltip.
     * @param accessor accessor used to get data from the block such as NBT.
     * @param config current Hwyla configuration
     */
    @Override
    public void onHwylaBodyRequest(ItemStack itemStack, List<String> tooltip,
                                   IWailaDataAccessor accessor, IWailaConfigHandler config){

        tooltip.add(
                TextFormatting.GREEN
                        + "Growth Stage: "
                        + getStageFromBlockMeta(accessor.getMetadata())
        );
    }

    /**
     * {@inheritDoc}
     *
     * Changes the plant name in the Hwyla tooltip.
     *
     * @param itemStack the block ItemStack.
     * @param tooltip the head tooltip array (containing the block name).
     * @param accessor Hwyla data provider.
     * @param config current Hwyla config settings.
     */
    @Override
    public void onHwylaHeadRequest(ItemStack itemStack, List<String> tooltip,
                                   IWailaDataAccessor accessor, IWailaConfigHandler config){
        String blockTitle = tooltip.get(0);
        if(blockTitle.contains("Seeds")){
            tooltip.clear();
            tooltip.add(0, TextFormatting.GOLD
                    + blockTitle.replace("Seeds", "Flower"));
            tooltip.add(TextFormatting.RED + "Drops: " +  blockTitle);
        }
    }

    /**
     * Used to get the seed item this plant instance
     * will actually drop. This is specified
     * in the class creating this one.
     *
     * @return the seed item instance for this plant.
     */
    protected abstract Item getSeed();

    /**
     * Used to get the produce item this plant instance
     * will actually drop. This is specified
     * in the class creating this one.
     *
     * @return the produce item instance for this plant.
     */
    protected abstract Item getProduce();

}
