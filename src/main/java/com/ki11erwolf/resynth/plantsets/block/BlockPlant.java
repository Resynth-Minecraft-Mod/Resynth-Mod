package com.ki11erwolf.resynth.plantsets.block;

import com.ki11erwolf.resynth.block.ResynthBlock;
import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.block.tileEntity.TileEntityMineralSoil;
import com.ki11erwolf.resynth.item.ItemMineralHoe.InfoProvider;
import com.ki11erwolf.resynth.plantsets.set.PlantSetProperties;
import com.ki11erwolf.resynth.plantsets.item.ItemSeeds;
import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

/**
 * The base plant block class that all Resynth growable plants
 * inherit. This class sets most of the ground work (Growth, look,
 * placement, ect.), which allows inheriting classes to simply
 * specify growth mechanics and unique properties of the plant.
 * This class also handles random growth, taking into account
 * Mineral Soil Mineral Concentration and plant growth chances.
 *
 * @param <T> the inheriting class (e.i. plant block).
 */
public abstract class BlockPlant<T extends BlockPlant<T>> extends ResynthBlock<T>
        implements IPlantable, IGrowable, InfoProvider {

    /**
     * The prefix for all plant blocks.
     */
    private static final String PLANT_PREFIX = "plant";

    /**
     * The unique properties of this specific plant type instance. Should
     * be config specified.
     */
    private final PlantSetProperties properties;

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
                .needsRandomTick()
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
    private boolean isValidGround(IBlockState state) {
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
    public boolean isValidPosition(IBlockState state, IWorldReaderBase worldIn, BlockPos pos) {
        return (worldIn.getLightSubtracted(pos, 0) >= 8 || worldIn.canSeeSky(pos))
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
    public IBlockState updatePostPlacement(IBlockState stateIn, EnumFacing facing, IBlockState facingState,
                                           IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !stateIn.isValidPosition(worldIn, currentPos) ?
                Blocks.AIR.getDefaultState() :
                super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    // *************
    // Look and Feel
    // *************

    /**
     * @return {@code false}.
     */
    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    /**
     * @return {@link BlockRenderLayer#CUTOUT}.
     */
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * @return {@link BlockFaceShape#UNDEFINED}.
     */
    @Override
    @SuppressWarnings("deprecation")
    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    /**
     * @return {@code 0}.
     */
    @Override
    @SuppressWarnings("deprecation")
    public int getOpacity(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0;
    }

    /**
     * Allows the plant to be walked through.
     *
     * @return {@link VoxelShapes#empty()}.
     */
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
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
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return getShapeByAge()[state.get(this.getGrowthProperty())];
    }

    // ************
    // Growth Logic
    // ************

    /**
     * Used to check if the given plant is fully
     * growth.
     *
     * @param state the plant.
     * @return {@code true} if the given plant
     * is fully grown.
     */
    private boolean isFullyGrown(IBlockState state){
        return getGrowthStage(state) >= getMaxAge();
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
    int getGrowthStage(IBlockState state){
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
    public void tick(IBlockState state, World worldIn, BlockPos pos, Random random) {
        super.tick(state, worldIn, pos, random);

        if (!worldIn.isAreaLoaded(pos, 1))
            return;

        if (!(worldIn.getLightSubtracted(pos.up(), 0) >= 9))
            return;

        if(MathUtil.chance(getMineralPercent(worldIn, pos)) && MathUtil.chance(getPlantGrowthChance())){
            callGrowPlant(worldIn, state, pos, 1);
        }
    }

    // **********
    // Drop Logic
    // **********

    /**
     * Handles dropping the plants seeds and produce when it is broken.
     */
    @Override
    public void getDrops(IBlockState state, net.minecraft.util.NonNullList<ItemStack> drops,
                         World world, BlockPos pos, int fortune) {
        drops.add(new ItemStack(getSeedsItem(), 1));

        if(getGrowthStage(state) == getMaxAge() && dropsProduceWhenGrown() && getProduce() != null)
            drops.add(getProduce());
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
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, IBlockState state) {
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
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
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
    public IBlockState getPlant(IBlockReader world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
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
    public boolean canGrow(IBlockReader worldIn, BlockPos pos, IBlockState state, boolean isClient) {
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
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return properties.canBonemeal();
    }

    /**
     * Called when bonemeal is used on the plant.
     * Grows the plant.
     */
    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        callGrowPlant(worldIn, state, pos, getBonemealIncrease());
    }

    // *************
    // Info Provider
    // *************

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInfo(World world, BlockPos pos) {
        return I18n.format(
                "misc.resynth.growth_stage",
                (getGrowthStage(world.getBlockState(pos)) + 1) + "/" + (getMaxAge() + 1)
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
    abstract int getMaxAge();

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
    abstract void growPlant(World world, IBlockState state, BlockPos pos, int increase);

    /**
     * Used to call {@link #growPlant(World, IBlockState, BlockPos, int)}
     * while notifying forge hooks of a plant growth.
     */
    private void callGrowPlant(World world, IBlockState state, BlockPos pos, int increase){
        ForgeHooks.onCropsGrowPre(world, pos, state, false);
        growPlant(world, state, pos, increase);
        ForgeHooks.onCropsGrowPost(world, pos, state);
    }

    // ***************************
    // Propagated Abstract Methods
    // ***************************

    /**
     * Used by the specific {@link com.ki11erwolf.resynth.plantsets.set.PlantSet}
     * to specific the seeds item of this plant type
     * as it's only known when creating the plant set.
     *
     * @return the seeds item for this specific plant type.
     */
    protected abstract ItemSeeds getSeedsItem();

    /**
     * Used by the specific {@link com.ki11erwolf.resynth.plantsets.set.PlantSet}
     * to specific the produce block/item of this plant type
     * as it's only known when creating the plant set.
     *
     * @return the produce block/item for this specific plant type.
     */
    protected abstract ItemStack getProduce();
}
