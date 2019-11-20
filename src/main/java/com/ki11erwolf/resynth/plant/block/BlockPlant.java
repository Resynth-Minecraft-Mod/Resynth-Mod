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

import com.ki11erwolf.resynth.block.BlockEnhancer;
import com.ki11erwolf.resynth.block.ResynthBlock;
import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.block.tileEntity.TileEntityMineralSoil;
import com.ki11erwolf.resynth.plant.item.ItemSeeds;
import com.ki11erwolf.resynth.plant.set.PlantSetProperties;
import com.ki11erwolf.resynth.util.*;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

import java.util.List;
import java.util.Random;

/**
 * The base plant block class that all Resynth growable plants
 * inherit. This class sets most of the ground work (Growth, look,
 * placement, ect.), which allows inheriting classes to simply
 * specify growth mechanics and unique properties of the plant.
 * This class also handles random growth, taking into account
 * Mineral Soil Mineral Concentration and plant growth chances.
 *
 * @param <T> the inheriting class (i.e. plant block).
 */
public abstract class BlockPlant<T extends BlockPlant<T>> extends ResynthBlock<T>
        implements IPlantable, IGrowable, IComponentProvider, PlantPatchInfoProvider {

    /**
     * The prefix for all plant blocks.
     */
    private static final String PLANT_PREFIX = "plant";

    /**
     * The unique properties of this specific plant type instance. Should
     * be config specified.
     */
    final PlantSetProperties properties;

    /**
     * Resynth plant block constructor.
     *
     * @param plantTypeName the name of the plant set type (e.g. crystalline).
     * @param plantName the name of the plant (e.g. diamond).
     * @param properties the unique properties of the plant type. Should
     *                   be specified by config.
     */
    BlockPlant(String plantTypeName, String plantName, PlantSetProperties properties) {
        super(
                Properties.create(Material.PLANTS)
                .sound(SoundType.PLANT)
                .tickRandomly()
                .hardnessAndResistance(0.0F),

                plantTypeName + "_" + PLANT_PREFIX + "_" + plantName
        );

        this.properties = properties;
        this.setDefaultState(this.stateContainer.getBaseState().with(this.getGrowthProperty(), 0));
    }

    // *********
    // Placement
    // *********

    /**
     * Used to check if the plant block can be
     * placed on the given ground block.
     *
     * @return {@code true} if the given ground
     * is {@link ResynthBlocks#BLOCK_MINERAL_SOIL}.
     */
    private boolean isValidGround(BlockState state) {
        return state.getBlock() == ResynthBlocks.BLOCK_MINERAL_SOIL;
    }

    /**
     * Used to check if the plant can be placed
     * at the requested position. Takes ground
     * block, light level and sky visibility
     * into account.
     *
     * @return {@code true} only if the plant is
     * being placed on valid ground and has
     * enough light.
     */
    @Override
    @SuppressWarnings("deprecation")
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return (worldIn.getLightSubtracted(pos, 0) >= 8 || worldIn.canBlockSeeSky(pos))
                && this.isValidGround(worldIn.getBlockState(pos.down()));
    }

    /**
     * Handles removing the plant from the world
     * if it is no longer in a valid position.
     *
     * @return the new state of the plant - can be air.
     */
    @Override
    @SuppressWarnings("deprecation")
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState,
                                           IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !stateIn.isValidPosition(worldIn, currentPos) ?
                Blocks.AIR.getDefaultState() :
                super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    // *************
    // Look and Feel
    // *************

    /**
     * @return {@link BlockRenderLayer#CUTOUT}.
     */
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * @return {@code 0}.
     */
    @Override
    @SuppressWarnings("deprecation")
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0;
    }

    /**
     * Allows the plant to be walked through.
     *
     * @return {@link VoxelShapes#empty()}.
     */
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos,
                                        ISelectionContext context) {
        return VoxelShapes.empty();
    }

    /**
     * Takes plant growth into account.
     *
     * @return the bounding box shape of the plant
     * for its growth stage.
     */
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return getShapeByAge()[state.get(this.getGrowthProperty())];
    }

    // *********
    // Particles
    // *********

    /**
     * Handles spawning particles randomly around the plant.
     */
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        EffectsUtil.displayStandardEffectsWithChance(worldIn, pos, 1, 5, ParticleTypes.END_ROD);
    }

    // ************
    // Growth Logic
    // ************

    /**
     * Used to check if the given plant is fully
     * grown.
     *
     * @param state the plant.
     * @return {@code true} if the given plant
     * is fully grown.
     */
    private boolean isFullyGrown(BlockState state){
        return getGrowthStage(state) >= getMaxGrowthStage();
    }

    /**
     * Used to get the amount of growth
     * stages a plant should grow when
     * given bonemeal.
     *
     * @return a random int in the range of 1 to 3 (inclusive).
     */
    private int getBonemealIncrease(){
        return MathUtil.getRandomIntegerInRange(1, 3);
    }

    /**
     * Used to get the Mineral Concentration of the Mineral
     * Soil block underneath the plant.
     *
     * @param pos position of the soil block.
     * @return the Mineral Concentration of the Mineral Soil
     * block below the plant. Will be 0 if no soil block
     * is below the plant.
     */
    private float getMineralPercent(World world, BlockPos pos){
        TileEntityMineralSoil te = null;

        if(world.getTileEntity(pos.down()) instanceof TileEntityMineralSoil)
            te = (TileEntityMineralSoil) world.getTileEntity(pos.down());

        if(te == null)
            return 0;

        return te.getMineralPercentage();
    }

    /**
     * Used to get the Mineral Concentration increase
     * of the Mineral Soil block from any Enhancer
     * blocks underneath the Mineral Soil.
     *
     * @param pos position of the soil block.
     * @return the Mineral Percentage Increase from the soil
     * blocks enhancer block.
     */
    private float getMineralPercentIncrease(World world, BlockPos pos){
        BlockState enhancer = world.getBlockState(pos.down().down());

        if(enhancer.getBlock() instanceof BlockEnhancer){
            return ((BlockEnhancer)enhancer.getBlock()).getIncrease();
        }

        return 0;
    }

    /**
     * @return the growth chance of this specific
     * plant type.
     */
    private float getPlantGrowthChance(){
        return properties.chanceToGrow();
    }

    /**
     * Used to get the growth stage of
     * a given plant block.
     *
     * @param state the given plant block.
     * @return the integer growth stage
     * of the given plant block.
     */
    public int getGrowthStage(BlockState state){
        return state.get(getGrowthProperty());
    }

    /**
     * Called randomly to grow the plant. Takes plant
     * growth chance and Mineral Soil Mineral Concentration
     * into account when determining if the plant should grow.
     * This then allows the implementing class to specify how
     * the plant should grow without worrying about growth
     * chances.
     */
    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
        super.tick(state, worldIn, pos, random);

        if(canGrow(worldIn, pos)){
            callGrowPlant(worldIn, state, pos, 1);
        }
    }

    // ************
    // Growth Check
    // ************

    /**
     * Used to check if conditions are right for the plant to grow
     * (chunk load/light) as well as check to see if the plant can grow by
     * random chance (based on plant growth chance, mineral content
     * and any enhancer blocks).
     *
     * @param pos position of the soil block.
     * @return {@code true} if the plant can grow.
     */
    private boolean canGrow(World world, BlockPos pos){
        if (!world.isAreaLoaded(pos, 1))
            return false;

        if (!(world.getLightSubtracted(pos.up(), 0) >= 9))
            return false;

        float mineralPercentChance = getMineralPercent(world, pos);
        float mineralPercentIncrease = getMineralPercentIncrease(world, pos);

        //With percentage increase if mineral percent is above 49.9
        float combined = mineralPercentChance + ((mineralPercentChance > 49.9) ? mineralPercentIncrease : 0);

        return MathUtil.chance(combined) && MathUtil.chance(getPlantGrowthChance());
    }

    // **********
    // Drop Logic
    // **********

    /**
     * Handles dropping the plants seeds and produce when it is broken.
     */
    @Override
    @SuppressWarnings("deprecation")
    public void spawnAdditionalDrops(BlockState state, World worldIn, BlockPos pos, ItemStack stack) {
        MinecraftUtil.spawnItemInWorld(getSeedsItem(), worldIn, pos);

        if(getGrowthStage(state) == getMaxGrowthStage() && dropsProduceWhenGrown() && getProduce() != null)
            MinecraftUtil.spawnItemStackInWorld(getProduce(), worldIn, pos);
    }

    /**
     * Used to specify what block/item to
     * give the player when they pick
     * (middle click) this plant block.
     *
     * @return this plant types seeds.
     */
    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(getSeedsItem());
    }

    // ***************
    // Growth property
    // ***************

    /**
     * Registers the growth property ({@link #getGrowthProperty()})
     * of the plant.
     */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(getGrowthProperty());
    }

    // *******************
    // Plantable interface
    // *******************

    /**
     * @return the plant block at the given position
     * or the default state of this plant type.
     */
    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() != this) return getDefaultState();
        return state;
    }

    // ******************
    // Growable Interface
    // ******************

    /**
     * Used to check if this plant can be grown
     * (by bonemeal, specifically).
     *
     * @return {@code true} if bonemeal is enabled and the
     * plant is not fully grown.
     */
    @Override
    public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return properties.canBonemeal() && !isFullyGrown(state);
    }

    /**
     * Used to check if bonemeal is enabled
     * for the plant type.
     *
     * @return {@code true} if bonemeal is enabled
     * for the plant type.
     */
    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
        return properties.canBonemeal();
    }

    /**
     * Called when bonemeal is used on the plant.
     * Grows the plant.
     */
    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, BlockState state) {
        callGrowPlant(worldIn, state, pos, getBonemealIncrease());
    }

    // *****
    // Hwyla
    // *****

    /**
     * {@inheritDoc}
     * <p/>
     * Handles displaying the plants growth (in stages) in the
     * Hwyla tooltip.
     */
    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if(tooltip.isEmpty())
            tooltip.add(new StringTextComponent(
                    getGrowthStageMessage(
                            getGrowthStage(accessor.getWorld().getBlockState(accessor.getPosition())),
                            getMaxGrowthStage()
                    )
            ));
    }

    // ************************
    // Public Properties Getter
    // ************************

    public PlantSetProperties getProperties(){
        return this.properties;
    }

    /**
     * Gets the growth stage message from the
     * lang file formatted with the provided
     * info.
     *
     * @param growthStage the plants current growth stage.
     * @param max the plants max number of growth stages.
     * @return the formatted localized message.
     */
    private static String getGrowthStageMessage(int growthStage, int max){
        return TextFormatting.GREEN +
                I18n.format(
                        "misc.resynth.growth_stage",
                        TextFormatting.GOLD + ((growthStage) + 1  + "/" + (max + 1))
                );
    }

    // ****************
    // Abstract Methods
    // ****************

    /**
     * Allows implementing classes to define the
     * growth stages of this plant set type.
     *
     * @return the {@link IntegerProperty} that
     * defines the growth stages of the specific
     * plant set type.
     */
    abstract IntegerProperty getGrowthProperty();

    /**
     * Allows implementing classes to define
     * the bounding box shape of the plant
     * during its growth stages.
     *
     * @return an array of the plant bounding
     * box shapes for all the growth stages.
     */
    abstract VoxelShape[] getShapeByAge();

    /**
     * Allows implementing classes to specify
     * how my growth stages this specific
     * plant set type has.
     *
     * @return the number of growth
     * stages this specific plant set
     * type has.
     */
    public abstract int getMaxGrowthStage();

    /**
     * Allows implementing classes to specify
     * whether this plant set type drops produce
     * when fully grown and broken.
     *
     * @return {@code true} if this plant set type
     * should drop its produce block/item when
     * fully grown and broken, {@code false}
     * otherwise.
     */
    abstract boolean dropsProduceWhenGrown();

    /**
     * SHOULD NEVER BE CALLED DIRECTLY.
     *
     * Allows the implement plant set type class to specify
     * how the plant grows (all plants grow differently)
     * without having to worry about growth chances and all.
     * <p/>
     * This method is only called after Mineral Soil Mineral
     * Concentration and plant growth chances are calculated.
     * <p/>
     * When called, this method should ALWAYS grow the plant.
     *
     * @param increase the amount of growth stages to grow
     *                 the plant by.
     */
    abstract void growPlant(World world, BlockState state, BlockPos pos, int increase);

    /**
     * Used to call {@link #growPlant(World, BlockState, BlockPos, int)}
     * while notifying forge hooks of a plant growth. Also ensures the plant
     * is not already fully grown.
     */
    private void callGrowPlant(World world, BlockState state, BlockPos pos, int increase){
        //Don't grow if fully grown.
        if(
                ((BlockPlant)world.getBlockState(pos).getBlock()).getGrowthStage(world.getBlockState(pos))
                >= ((BlockPlant)world.getBlockState(pos).getBlock()).getMaxGrowthStage()
        ) return;

        ForgeHooks.onCropsGrowPre(world, pos, state, false);
        growPlant(world, state, pos, increase);
        ForgeHooks.onCropsGrowPost(world, pos, state);
    }

    // ***************************
    // Propagated Abstract Methods
    // ***************************

    /**
     * Used by the specific {@link com.ki11erwolf.resynth.plant.set.PlantSet}
     * to specific the seeds item of this plant type
     * as it's only known when creating the plant set.
     *
     * @return the seeds item for this specific plant type.
     */
    protected abstract ItemSeeds getSeedsItem();

    /**
     * Used by the specific {@link com.ki11erwolf.resynth.plant.set.PlantSet}
     * to specify the produce block/item of this plant type
     * as it's only known when creating the plant set.
     *
     * @return the produce block/item for this specific plant type.
     */
    protected abstract ItemStack getProduce();
}
