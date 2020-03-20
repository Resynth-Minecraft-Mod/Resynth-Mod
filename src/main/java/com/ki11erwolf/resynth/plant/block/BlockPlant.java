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
package com.ki11erwolf.resynth.plant.block;

import com.ki11erwolf.resynth.block.BlockEnhancer;
import com.ki11erwolf.resynth.block.ResynthBlock;
import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.block.tileEntity.TileEntityMineralSoil;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.GeneralConfig;
import com.ki11erwolf.resynth.plant.item.ItemSeeds;
import com.ki11erwolf.resynth.plant.set.PlantSetProperties;
import com.ki11erwolf.resynth.util.EffectsUtil;
import com.ki11erwolf.resynth.util.MathUtil;
import com.ki11erwolf.resynth.util.MinecraftUtil;
import com.ki11erwolf.resynth.util.PlantPatchInfoProvider;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
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
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

import java.util.List;
import java.util.Random;

/**
 * The base plant block class that all Resynth growable plants
 * inherit. This class sets most of the ground work (Growth, look,
 * placement, behaviour, ect.), which allows inheriting classes to simply
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
                .hardnessAndResistance(0.0F)
                .doesNotBlockMovement(),

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
        return (worldIn.getNeighborAwareLightSubtracted(pos, 0) >= 8
                || worldIn.canBlockSeeSky(pos))
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
     * Sets the opacity of this block to full (1.0),
     * which makes the block see-through.
     *
     * @return {@code 1.0F}.
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("deprecation")
    public float func_220080_a(BlockState state, IBlockReader reader, BlockPos pos) {
        return 1.0F;
    }

    /**
     * Allows skylight to pass through this block.
     *
     * @return {@code true}
     */
    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    /**
     * Tells Minecraft that this block is abnormal,
     * which allows us to render it as transparent.
     *
     * @return {@code false}
     */
    @Override
    @SuppressWarnings("deprecation")
    public boolean isNormalCube(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
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

    // *******************
    // Hopper Auto Farming
    // *******************

    /**
     * Auto-Farming check and attempt for all plants.
     * <b>Should be called in every plant types {@link
     * #growPlant(World, BlockState, BlockPos, int)}
     * method, AFTER the growth has been calculated.</b>
     *
     * <p/><b>If - and only if the plant is fully grown and
     * config allows,</b> will this method check for any
     * hoppers below the plants soil block (1 and then 2
     * blocks below), and if one is found, will try and
     * dump the produce into the hopper tile entity.
     *
     * @param growth the final growth stage of the plant.
     * @param world the world the plant is in.
     * @param pos the position of the plant in the world.
     * @return {@code true} if hopper auto-farming is enabled,
     * the plant is fully grown, and the plants produce was
     * dumped into a hopper below the plant.
     *
     * <p/>Will only return {@code true} if the produce was
     * successfully placed in a hopper below the plant. If the
     * produce could NOT be placed in a hopper below
     * the plant, this will return {@code false}.
     *
     * <p/><b>The plants growth should be reset if this
     * returns {@code true}!</b>
     */
    protected boolean tryAutoFarmDump(int growth, World world, BlockPos pos){
        //IF enabled
        if(ResynthConfig.GENERAL_CONFIG.getCategory(GeneralConfig.class).isHopperAutofarmingEnabled())
            //AND       Plant is fully grown     AND      Produce was hoppered.
            return growth >= getMaxGrowthStage() && tryHopperProduce(world, pos);
        else return false;
    }

    /**
     * Looks a hopper tile entity 2 and then 3 blocks below
     * the given plants block position. The FIRST hopper found
     * will be returned.
     *
     * @param world the world the plant block is in.
     * @param pos the position of the plant in the world.
     * @return the FIRST {@link HopperTileEntity} found either
     * 2 or 3 blocks below the plant, or {@code null} if no
     * hoppers were found.
     */
    private HopperTileEntity getHopper(World world, BlockPos pos){
        BlockPos posA = pos.down(2), posB = posA.down(1);

        TileEntity a;
        if((a = world.getTileEntity(posA)) instanceof HopperTileEntity)
                return (HopperTileEntity) a;

        TileEntity b;
        if((b = world.getTileEntity(posB)) instanceof HopperTileEntity)
            return (HopperTileEntity) b;

        return null;
    }

    /**
     * Will try and dump this plant types produce into any hoppers
     * (found with {@link #getHopper(World, BlockPos)}) below the
     * plant.
     *
     * @param world the world the plant is in.
     * @param pos the position of the plant in the world.
     * @return {@code true} if and only if, both a hopper
     * was found below the plant and the plants produce
     * was successfully placed within the found hopper, {@code
     * false} otherwise, such as if the hopper if full.
     */
    private boolean tryHopperProduce(World world, BlockPos pos){
        HopperTileEntity hopper = getHopper(world, pos);
        ItemStack produce = getProduce();

        //No hopper
        if(hopper == null) return false;

        //For each hopper slot
        for(int i = 0; i <= 4; i++){
            ItemStack occupied = hopper.getStackInSlot(i);

            //Combined deposit if produce already in hopper
            if(occupied.getItem() == produce.getItem()){
                int newCount = produce.getCount() + occupied.getCount();

                //Check for stack count above 64
                if(! (newCount > 64)){
                    ItemStack combinedProduce = new ItemStack(
                            produce.getItem(), newCount
                    );
                    hopper.setInventorySlotContents(i, combinedProduce);
                    return true;
                }
            }

            //Deposit if slot is empty
            if(occupied == ItemStack.EMPTY || occupied.getItem() == Items.AIR
                    || occupied.getCount() == 0){
                hopper.setInventorySlotContents(i, produce);
                return true;
            }
        }

        //Could not deposit
        return false;
    }

    // *************************
    // Right-Click Harvest Logic
    // *************************

    /**
     * {@inheritDoc}
     *
     * Called when a player right-clicks this plant.
     * Used to implement right-click harvesting ({@link
     * #tryRightclickHarvest(World, BlockPos, PlayerEntity)}).
     *
     * @return {@link ActionResultType#SUCCESS} if the plant was harvested,
     * {@link ActionResultType#FAIL} if the plant could not be harvested.
     */
    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType func_225533_a_(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                           Hand hand, BlockRayTraceResult hit){
        return tryRightclickHarvest(world, pos, player) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
    }

    /**
     * Will try and harvest the given plant in the given world if the plant
     * is fully grown.
     *
     * <p/>A successful harvest will drop the plants produce in the world and
     * reset the plants growth to its specific post harvest stage.
     *
     * <p/>Behaviour is dependent on: {@link #postHarvestGrowthStageReset()}, {@link
     * #postHarvestNumberOfProduceDrops()}, and {@link #postHarvestSoundEvent()}.
     *
     * @param world the world the plant is in.
     * @param pos the position of the plant in the world.
     * @param player the player trying to harvest the plant.
     * @return {@code true} if the harvest was successful,
     * {@code false} otherwise.
     */
    private boolean tryRightclickHarvest(World world, BlockPos pos, PlayerEntity player){
        //Check if harvestable.
        if(postHarvestGrowthStageReset() == -1 || postHarvestNumberOfProduceDrops() == -1
                || postHarvestSoundEvent() == null) return false;//Not harvestable!

        int growth = world.getBlockState(pos).get(getGrowthProperty());
        int postHarvestGrowth = postHarvestGrowthStageReset();

        //noinspection rawtypes
        if(growth >= ((BlockPlant)world.getBlockState(pos).getBlock()).getMaxGrowthStage()){
            if(setGrowthStage(world, pos, postHarvestGrowth)){

                if(!world.isRemote) {
                    MinecraftUtil.spawnItemStackInWorld(
                            new ItemStack(
                                    getProduce().getItem(),
                                    postHarvestNumberOfProduceDrops()
                            ), world, pos
                    );
                }

                EffectsUtil.playNormalSoundWithRandomPitch(
                        world, player, pos, postHarvestSoundEvent(), SoundCategory.BLOCKS
                );

                return true;
            }
        }

        return false;
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
    public int getGrowthStage(BlockState state) {
        return state.get(getGrowthProperty());
    }

    /**
     * Used to set the growth stage of a specific
     * plant in the world.
     *
     * @param world the world the plant is in.
     * @param pos the position of the plant
     *            block in the world.
     * @param growthStage the new growth stage
     *                    of the plant.
     * @return {@code true} if the plants state was
     * changed, {@code false} otherwise.
     */
    @SuppressWarnings("UnusedReturnValue")
    protected boolean setGrowthStage(World world, BlockPos pos, int growthStage) {
        return world.setBlockState(
                pos, world.getBlockState(pos).getBlock().getDefaultState().with(
                        getGrowthProperty(), growthStage
                ), 2
        );
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
    public void func_225534_a_(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        super.func_225534_a_(state, worldIn, pos, random);

        if(canGrow(worldIn, pos)){
            callGrowPlant(worldIn, state, pos, 1);
        }

        //If not grown, try and perform auto-farm dump anyway.
        //This prevents plants from getting stuck if a hopper is full.
        if(tryAutoFarmDump(getGrowthStage(state), worldIn, pos))
            //Set back to initial growth stage - worst case.
            setGrowthStage(worldIn, pos, postHarvestGrowthStageReset() == -1 ? 0 : postHarvestGrowthStageReset());
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

        if (!(world.getNeighborAwareLightSubtracted(pos.up(), 0) >= 9))
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
    public void func_225535_a_(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
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

    // *****
    // Other
    // *****

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
     * <b>This method SHOULD also do the auto-farm check
     * ({@link #tryAutoFarmDump(int, World, BlockPos)})
     * and reset the plants growth if it returns
     * {@code true}.</b>
     * <p/>
     * This method is only called after Mineral Soil Mineral
     * Concentration and plant growth chances are calculated.
     * <p/>
     * When called, this method should ALWAYS grow the plant,
     * UNLESS already fully grown. No logic or checks need to
     * be performed here.
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
        //noinspection rawtypes
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

    // *******************************************
    // Optional Opt-in Propagated Abstract Methods
    // *******************************************

    /*
        Optionally overridable methods that allow a plant
        type to define its right-click harvest behaviour.
     */

    /**
     * @return the growth stage the plant should be reset
     * to after being harvested with a right-click.
     * {@code -1} if the plant cannot be harvested with
     * right-clicks.
     */
    protected int postHarvestGrowthStageReset() {
        return -1;
    }

    /**
     * @return the number of produce items to drop
     * when the plant is being harvested with a right-click.
     * {@code -1} if the plant cannot be harvested with
     * right-clicks.
     */
    protected int postHarvestNumberOfProduceDrops(){
        return -1;
    }

    /**
     * @return the specific sound ({@link SoundEvents}
     * made when the plant is harvested with a right-click.
     * {@code null} if the plant cannot be harvested with
     * right-clicks.
     */
    protected SoundEvent postHarvestSoundEvent(){
        return null;
    }
}
