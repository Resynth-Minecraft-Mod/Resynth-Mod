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
import com.ki11erwolf.resynth.plant.set.IPlantSetProperties;
import com.ki11erwolf.resynth.plant.set.PlantSet;
import com.ki11erwolf.resynth.util.EffectsUtil;
import com.ki11erwolf.resynth.util.MathUtil;
import com.ki11erwolf.resynth.util.MinecraftUtil;
import com.ki11erwolf.resynth.util.PlantPatchInfoProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
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
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

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
public abstract class BlockPlant<T extends BlockPlant<T>> extends ResynthBlock<T> implements
        IPlantable, IGrowable, PlantPatchInfoProvider, IComponentProvider, IProbeInfoAccessor {

    /**
     * The prefix for all plant blocks.
     */
    private static final String PLANT_PREFIX = "plant";

    /**
     * The unique properties of this specific plant type instance. Should
     * be config specified.
     */
    final IPlantSetProperties properties;

    BlockPlant(PlantSet<?, ?> parentSet) {
        super(Properties.create(Material.PLANTS).sound(SoundType.PLANT).tickRandomly()
                        .hardnessAndResistance(0.0F).doesNotBlockMovement(),
                parentSet.getSetTypeName() + "_" + PLANT_PREFIX + "_" + parentSet.getSetName());

        this.properties = parentSet.getPlantSetProperties();
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
        return !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() :
                super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    // *************
    // Look and Feel
    // *************

    /**
     * Allows skylight to pass through this block.
     *
     * @return {@code true}
     */
    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
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
    public boolean isTransparent(BlockState state) {
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
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
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

    /**
     * @return the specific way of rendering the layers of Resynth plants.
     * Affects {@link com.ki11erwolf.resynth.block.BlockSeedPod} as well.
     */
    public static RenderType getResynthPlantRenderType() {
        return RenderType.getCutoutMipped();
    }

    // *********
    // Particles
    // *********

    /**
     * Handles spawning particles randomly around the plant.
     */
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        EffectsUtil.displayStandardEffectsWithChance(worldIn, pos, 1, 5, ParticleTypes.SMOKE);
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
    protected boolean attemptAutoHarvest(int growth, World world, BlockPos pos){
        //IF enabled
        if(ResynthConfig.GENERAL_CONFIG.getCategory(GeneralConfig.class).isHopperAutofarmingEnabled()) {
            //AND       Plant is fully grown     AND      Produce was hoppered.
            if (growth >= getMaxGrowthStage() && tryHopperProduce(world, pos)) {
                //Success - with particles!
                com.ki11erwolf.resynth.util.EffectsUtil.displayStandardEffectsOnClient(
                        pos, 3, ParticleTypes.POOF
                );
                return true;
            }
        }

        return false;
    }

    /**
     * Looks for a hopper tile entity 2 and then 3 blocks below
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

        if((a = world.getTileEntity(posB)) instanceof HopperTileEntity)
            return (HopperTileEntity) a;

        return null;
    }

    /**
     * A simple check to see if the plant has any hoppers
     * below it.
     *
     * @param world the world the plant is in.
     * @param pos the coords of the plant in the world.
     * @return {@code true} if a hopper was found 2 or 3
     * blocks beneath the plant. Returns {@code false} if
     * no hoppers were found.
     */
    private boolean hasHopper(World world, BlockPos pos) {
        return getHopper(world, pos) != null;
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
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                             Hand hand, BlockRayTraceResult hit) {
        return tryRightclickHarvest(world, pos, player) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
    }

    /**
     * Will try and harvest the given plant in the given world if the plant
     * is fully grown.
     *
     * <p/>A successful harvest will drop the plants produce in the world and
     * reset the plants growth to its specific post harvest stage.
     *
     * <p/>Behaviour is dependent on: {@link #getGrowthPostHarvest()}, {@link
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
        if(getGrowthPostHarvest() == -1 || postHarvestNumberOfProduceDrops() == -1
                || postHarvestSoundEvent() == null) return false;//Not harvestable!

        int growth = world.getBlockState(pos).get(getGrowthProperty());
        int postHarvestGrowth = getGrowthPostHarvest();

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
     * Used to give a plant in the world a chance at growing.
     *
     * <p/><b>Call from the onRandomTick method</b> native to Minecraft!
     * Which will then give the plant its chance at growing, while still
     * having growth speed determined by Minecraft ticks. <b>Without it</b>,
     * the plant has <b>no way to grow</b> randomly in the world.
     *
     * <p/>This is the first call in the long chain that is growth
     * of a plant, and the determining of its growth probability
     * and the conditions required. Ultimately ending in either
     * the growth of a plant, or absolutely nothing.
     *
     * <p/>Growth is determined <b>mostly by:</b>
     * <br/>{@link #getPlantGrowthChance()},
     * <br/>{@link #getMineralPercentIncrease(World, BlockPos)},
     * <br/>{@link #getMineralPercent(World, BlockPos)},
     * <br/>{@link #isLucky(World, BlockPos)},
     * <br/>and to a lesser degree:
     * <br/>{@link #canGrow(World, BlockPos)},
     * <br/>{@link #getLightPenalty(World, BlockPos)},
     * <br/>{@link World#isAreaLoaded(BlockPos, int)}.
     *
     * @param world the world the plant is in.
     * @param state the block state as it is in the world.
     * @param pos the position of the plant in the world.
     */
    public void giveChanceToGrow(BlockState state, IWorld world, BlockPos pos){
        //Check world object for nullity
        if((!(world instanceof World)))
            LOGGER.error("IWorld not a World object!", new IllegalArgumentException());
        else onGrowthChance(state, (World)world, pos);
    }

    /**
     * Called when the plant is given a chance to grow, to try
     * and grow the plant and perform auto harvesting.
     *
     * <p/>When called, will attempt to grow the plant,
     * if possible, based off all the required criteria
     * and probability. If the plant does get to grow,
     * an auto harvest will be attempted with its new growth.
     * If the plant is already fully grown, an auto harvest
     * will still be attempted, however it will skip its
     * chance to grow.
     *
     * @param world the world the plant is in.
     * @param state the block state as it is in the world.
     * @param pos the position of the plant in the world.
     */
    protected void onGrowthChance(BlockState state, World world, BlockPos pos) {
        int growth = getGrowthStage(state);
        int harvestGrowth = getGrowthPostHarvest();

        if(growth < 0)
            growth = 0;

        //If fully grown, skip growth for auto harvest.
        if(isFullyGrown(state)){
            if(attemptAutoHarvest(growth, world, pos)) {
                setGrowthStage(world, pos, harvestGrowth);
            }

            return;
        }

        //Otherwise, if can grow and is lucky.
        if(canGrow(world, pos)){
            if(isLucky(world, pos)){
                //Grow the plant.
                grow(world, state, pos, 1);
                onPlantGrow(state, world, pos.down(), pos);
                //And try a harvest with the new growth
                if(attemptAutoHarvest(growth, world, pos))
                    setGrowthStage(world, pos, harvestGrowth);
            }
        }
    }

    // ************************
    //     Growth Logic Hook
    // ************************

    /**
     * Called by Minecraft, when its randomly determins that this plant block is due
     * for a random update tick.
     *
     * <p/>When called, it will call {@link #giveChanceToGrow(BlockState, IWorld, BlockPos)},
     * which is how the plant determines its own growth and ultimately grows.
     *
     * @param world the world the plant is in.
     * @param state the block state as it is in the world.
     * @param pos the position of the plant in the world.
     */
    @Override @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.giveChanceToGrow(state, world, pos);
    }

    /**
     * Ensures the plant block is ticked randomly.
     * @return {@code true} - ensuring the plant block gets random tick updates.
     */
    @Override
    public boolean ticksRandomly(BlockState state) { return true; }

    // ******
    // Growth
    // ******

    /**
     * Used to check if the given plant is fully grown.
     *
     * @param state the plant state in the world.
     * @return {@code true} if the given plant is fully grown.
     */
    private boolean isFullyGrown(BlockState state){
        return getGrowthStage(state) >= getMaxGrowthStage();
    }

    /**
     * Used to get the amount of growth stages a plant
     * should grow when given bonemeal.
     *
     * @return a random int in the range of 1 to 3 (inclusive).
     */
    private int getBonemealIncrease(Random random){
        return MathUtil.getRandomIntegerInRange(random, 1, 3);
    }

    /**
     * Used to get the Mineral Concentration of the Mineral
     * Soil block underneath the plant.
     *
     * @param pos position of the plant block.
     * @return the Mineral Concentration of the Mineral Soil
     * block below the plant. Will be 0 if no soil block
     * is below the plant.
     */
    private float getMineralPercent(World world, BlockPos pos){
        TileEntityMineralSoil te = null;

        if(world.getTileEntity(pos.down()) instanceof TileEntityMineralSoil)
            te = (TileEntityMineralSoil) world.getTileEntity(pos.down());

        return te == null ? 0 : te.getMineralPercentage();
    }

    /**
     * Used to get the Enhancer Mineral Concentration, increase,
     * if any, of the Mineral Soil underneath the plant.
     *
     * @param pos position of the plant block.
     * @return the Mineral Percentage Increase from the soil
     * blocks enhancer block. Can be {@code 0}.
     */
    private float getMineralPercentIncrease(World world, BlockPos pos){
        BlockState enhancer = world.getBlockState(pos.down().down());

        if(enhancer.getBlock() instanceof BlockEnhancer){
            return ((BlockEnhancer)enhancer.getBlock()).getIncrease();
        }

        return 0;
    }

    /**
     * Returns the growth chance of unique to this
     * specific plant type/instance.
     */
    private float getPlantGrowthChance(){
        return properties.growthProbability();
    }

    /**
     * Used to get the growth stage of a given plant block,
     * as a state in the world.
     *
     * @param state the given plant block.
     * @return the integer growth stage of plant block state.
     */
    public int getGrowthStage(BlockState state) {
        return state.get(getGrowthProperty());
    }

    /**
     * Used to set the growth stage of a specific plant in the world.
     *
     * @param world the world the plant is in.
     * @param pos the position of the plant block in the world.
     * @param growthStage the new growth stage of the plant.
     * @return {@code true} if the plants state was changed,
     * {@code false} otherwise.
     */
    @SuppressWarnings("UnusedReturnValue")
    protected boolean setGrowthStage(World world, BlockPos pos, int growthStage) {
        return world.setBlockState(pos, world.getBlockState(pos).getBlock().getDefaultState().with(
                getGrowthProperty(), growthStage
            ), 2
        );
    }

    /**
     * <b>Only ever override this in subclasses!</b> Used by
     * specific plant types to define the plant block growth
     * logic.
     *
     * <p/><b>SHOULD NEVER BE CALLED DIRECTLY!</b>
     *
     * <p/>Allows the implementing plant type to specify how
     * the plant grows (as all plants grow differently)
     * without having to worry about growth chances and
     * the like.
     *
     * <p/><b>This method SHOULD also do the auto-farm check
     * ({@link #attemptAutoHarvest(int, World, BlockPos)})
     * and reset the plants growth if it returns
     * {@code true}.</b>
     *
     * <p/> When called, this method should ALWAYS grow the plant.
     * unless already fully grown. This method is only called after
     * Mineral Soil Mineral Concentration and plant growth chances
     * are calculated, so no logic or checks need to be performed here.
     *
     * <p/>
     * @param world the world the plant is in.
     * @param state the block state as it is in the world.
     * @param pos the position of the plant in the world.
     * @param increase the amount of stages to grow the plant by.
     */
    abstract void growPlant(World world, BlockState state, BlockPos pos, int increase);

    /**
     * Always grows the plant type in the world,
     * unless already fully grown.
     *
     * <p/>Handles calling the specific plant types grow method, and
     * ensures the Forge growth hooks are notified as well. Does NOT
     * handle growth checks and luck.
     *
     * @param world the world the plant is in.
     * @param state the block state as it is in the world.
     * @param pos the position of the plant in the world.
     * @param increase the amount of stages to grow the plant by.
     */
    private void grow(World world, BlockState state, BlockPos pos, int increase){
        //Don't grow if fully grown.
        if(isFullyGrown(world.getBlockState(pos)))
            return;

        ForgeHooks.onCropsGrowPre(world, pos, state, false);
        growPlant(world, state, pos, increase);
        ForgeHooks.onCropsGrowPost(world, pos, state);
    }

    // ******************
    // Determining Growth
    // ******************

    /**
     * Checks if the conditions of the world & plant block
     * are suitable. If so, growth can be allowed.
     *
     * <p/>In order to pass, the chunk must be loaded,
     * the light available must be bright enough, and
     * the plant cannot already be fully grown.
     *
     * @param world the world the plant is in.
     * @param pos the position of the plant in the world.
     * @return {@code true} if the plant can grow.
     */
    private boolean canGrow(World world, BlockPos pos){
        if (!world.isAreaLoaded(pos, 1))
            return false; //Chunk load prevention

        if (!(world.getNeighborAwareLightSubtracted(pos.up(), 0) >= 9))
            return false; //Lighting

        //Growth
        return !isFullyGrown(world.getBlockState(pos));
    }

    /**
     * Performs a once off check to determine if the plant in
     * the world at the given position is lucky enough to grow.
     *
     * <p/>Check is based upon <b>random chance</b>, soil
     * concentration, plant growth chance, and light available.
     *
     * @param world the world the plant is in.
     * @param pos the position of the plant in the world.
     * @return {@code true} if the plant got lucky, and
     * can now possibly grow, otherwise {@code false} is
     * returned.
     */
    private boolean isLucky(World world, BlockPos pos){
        float mineralPercentChance = getMineralPercent(world, pos);
        float combined = mineralPercentChance + ((mineralPercentChance > 49.9)
                ? getMineralPercentIncrease(world, pos) : 0);

        //Apply light penalty
        combined = applyLightPenalty(world, pos, combined);

        //If lucky with soil and plant growth chance
        if(MathUtil.chance(combined)){
            return MathUtil.chance(getPlantGrowthChance());
        }

        return false;
    }

    /**
     * Gets an integer penalty based upon the amount
     * of light in the given world at the given position.
     * Used by {@link #isLucky(World, BlockPos)}.
     *
     * @param world the given world to check the light in.
     * @param pos the position in the world to check the light value at.
     * @return the integer penalty ranging from 0 to 5, based apon light level,
     * where a bigger number means a bigger penalty. Will be {@code -1} if too
     * low to grow crops.
     */
    public int getLightPenalty(World world, BlockPos pos){
        if(!ResynthConfig.GENERAL_CONFIG.getCategory(GeneralConfig.class).enableLightPenalty())
            return 0;

        int lightPenalty = world.getMaxLightLevel() - world.getLight(pos.up());

        if(lightPenalty >= 1){
            if(lightPenalty >= 7)
                return -1;
            return lightPenalty;
        } else return 0;
    }

    /**
     * Gives a growth chance for a plant in the world that takes
     * the light level near the plant into account.
     *
     * @param world the world the plant is in.
     * @param pos the position of the plant in the world.
     * @param in the input growth chance of the plant.
     * @return the growth chance of the plant with the light penalty
     * applied.
     */
    public float applyLightPenalty(World world, BlockPos pos, float in){
        if(ResynthConfig.GENERAL_CONFIG.getCategory(GeneralConfig.class).enableLightPenalty())
            if(in >= 25){
                int penalty = getLightPenalty(world, pos);

                if(penalty != -1)
                    return in - penalty;
            }

        return in;
    }

    // **********
    // Drop Logic
    // **********

    /**
     * Handles dropping the plants seeds and produce
     * (if grown) when it is broken.
     *
     * @param world the world the plant is in.
     * @param state the block state as it is in the world.
     * @param pos the position of the plant in the world.
     */
    @Override
    @SuppressWarnings("deprecation")
    public void spawnAdditionalDrops(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack) {
        MinecraftUtil.spawnItemInWorld(getSeedsItem(), world, pos);

        if(getGrowthStage(state) == getMaxGrowthStage() && dropsProduceWhenGrown() && getProduce() != null)
            MinecraftUtil.spawnItemStackInWorld(getProduce(), world, pos);
    }

    /**
     * Used to specify what block/item to give the player
     * when they pick (middle click) this plant block.
     *
     * @return this plant types seeds - {@link #getSeedsItem()}.
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
     * Informs Minecraft that Resynth plants are
     * crop types.
     *
     * @param world the world the plant is in.
     * @param pos the coords of the plant in the world.
     * @return {@link PlantType#CROP}. All growable Resynth
     * plant types are crops.
     */
    @Override
    public PlantType getPlantType(IBlockReader world, BlockPos pos) {
        return PlantType.CROP;
    }

    /**
     * @param world the world the plant is in.
     * @param pos the position of the plant in the world.
     * @return the state of the plant block at the given
     * position, otherwise, the default state of this plant type.
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
     * @param world the world the plant is in.
     * @param state the block state as it is in the world.
     * @param pos the position of the plant in the world.
     * @return {@code true} if bonemeal is enabled and the
     * plant is not fully grown.
     */
    @Override
    public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean isClient) {
        return properties.bonemealGrowth() && !isFullyGrown(state);
    }

    /**
     * Used to check if bonemeal is enabled for the plant type.
     *
     * @param world the world the plant is in.
     * @param state the block state as it is in the world.
     * @param pos the position of the plant in the world.
     * @return {@code true} if bonemeal is enabled
     * for the plant type.
     */
    @Override
    public boolean canUseBonemeal(World world, Random rand, BlockPos pos, BlockState state) {
        return properties.bonemealGrowth();
    }

    /**
     * Always grows the plant type in the world, <b>as though
     * grown by/with Bonemeal</b>, unless already fully grown.
     *
     * <p/>Consider {@link #grow(World, BlockState, BlockPos, int)}
     * instead of this method.
     *
     * @param world the world the plant is in.
     * @param state the block state as it is in the world.
     * @param pos the position of the plant in the world.
     */
    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        grow(world, state, pos, getBonemealIncrease(random));
    }

    // *************
    // The One Probe
    // *************

    /**
     * {@inheritDoc}
     *
     * <p/> Handles adding and displaying the additional data related to
     * Plant Blocks, specificlly growth, on The One Probe tooltips.
     *
     * @param probeInfo - used to modify the tooltip displayed for this block.
     * @param player - the player looking at the tooltip.
     * @param world - the world the player and block are in.
     * @param blockState - the state of the block in the world.
     * @param data - additional data, such as block positions.
     */
    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player,
                             World world, BlockState blockState, IProbeHitData data) {
        int stage = getGrowthStage(blockState);
        int max = getMaxGrowthStage();
        int percent = (int)(((float) stage / max) * 100);

        probeInfo.text(new StringTextComponent(""));
        probeInfo.text(new StringTextComponent(String.format(TextFormatting.GREEN.toString()
                        + "G: %s / %s %s", stage, max, TextFormatting.DARK_GREEN + "(" + percent + "%)"
        )));
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
        if(tooltip.isEmpty()){
            int stage = getGrowthStage(accessor.getWorld().getBlockState(accessor.getPosition()));
            int max = getMaxGrowthStage();

            tooltip.add(new StringTextComponent(""));
            tooltip.add(new StringTextComponent(getGrowthStageMessage(stage, max)
                    + String.format("  (%s%%)", (int)(((float) stage / max) * 100))
            ));
            tooltip.add(new StringTextComponent(""));
        }

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
        return TextFormatting.GREEN + I18n.format("misc.resynth.growth_stage",
                TextFormatting.GOLD + ((growthStage) + 1  + "/" + (max + 1))
        );
    }

    // *****
    // Other
    // *****

    /**
     * @return the properties object for the plant
     * that specifies the values the plant uses,
     * like growth rate and seed drop chances.
     */
    public IPlantSetProperties getProperties(){
        return this.properties;
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
     * to after being harvested, either by a right-click
     * or by auto farming.
     *
     * <p/>Will return {@code -1} if the plant cannot be
     * harvested with right-clicks.
     */
    protected int getGrowthPostHarvest() {
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
