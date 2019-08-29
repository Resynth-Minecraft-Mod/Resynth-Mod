package com.ki11erwolf.resynth.plantsets.block;

import com.ki11erwolf.resynth.plantsets.set.IBiochemicalSetProperties;
import com.ki11erwolf.resynth.util.MinecraftUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

/**
 * The Biochemical set plant block - used to grow resources that
 * drop when a mob is killed. Specific plant blocks
 * are usually referred to as plant set types, while their instances
 * (diamond vs redstone) are referred to as the plant type.
 * <p/>
 * This class defines the look and feel (how the plant looks and behaves)
 * of the Biochemical plant block. This class DOES NOT handle any logic
 * other the way in which the plant type grows and is harvested.
 */
public abstract class BlockBiochemicalPlant extends BlockPlant<BlockBiochemicalPlant> {

    /**
     * @param plantTypeName the name of the plant set type (e.g. crystalline).
     * @param plantName the name of the plant type (e.g. diamond).
     * @param properties the properties (e.g. growth chances) of the
     *                   plant type.
     */
    @SuppressWarnings("WeakerAccess")//Lies
    public BlockBiochemicalPlant(String plantTypeName, String plantName, IBiochemicalSetProperties properties) {
        super(plantTypeName, plantName, properties);
    }

    // *************************
    // Abstract method overrides
    // *************************

    /**
     * {@inheritDoc}
     */
    @Override
    IntegerProperty getGrowthProperty() {
        return IntegerProperty.create("growth_stage", 0, 7);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    VoxelShape[] getShapeByAge() {
        return new VoxelShape[]{
                Block.makeCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 2.0D, 9.0D),   //1
                Block.makeCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 2.0D, 9.0D),   //2
                Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 3.0D, 11.0D), //3
                Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 5.0D, 11.0D), //4
                Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 7.0D, 13.0D), //5
                Block.makeCuboidShape(3.0D, 0.5D, 3.0D, 13.0D, 9.0D, 13.0D),//6
                Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 11.0D, 13.0D),//7
                Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D) //8
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    int getMaxAge() {
        return 7;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean dropsProduceWhenGrown(){
        return true;
    }

    // *************
    // Harvest Logic
    // *************

    /**
     * {@inheritDoc}
     *
     * Handles what happens when the player tries
     * to harvest the plant by right-clicking.
     *
     * @return {@code true} if the plant was harvested.
     */
    @Override
    @SuppressWarnings("deprecation")
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player,
                                    EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
        int growth = world.getBlockState(pos).get(getGrowthProperty());
        int postHarvestGrowth = growth - 4;

        if(growth >= ((BlockPlant)world.getBlockState(pos).getBlock()).getMaxAge()){
            if(world.setBlockState(pos, world.getBlockState(pos)
                    .with(getGrowthProperty(), postHarvestGrowth), 2)){
                if(!world.isRemote)
                    MinecraftUtil.spawnItemInWorld(getProduce().getItem(), world, pos);

                playPopSound(world, player);
                return true;
            }
        }

        return false;
    }

    /**
     * Plays the pop sound when a biochemical
     * plant is harvested.
     */
    @OnlyIn(Dist.CLIENT)
    private static void playPopSound(World world, EntityPlayer player){
        world.playSound(null, player.posX, player.posY, player.posZ,
                SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,
                0.5F, 0.4F / (new Random().nextFloat() * 0.4F + 0.8F)
        );
    }

    // ************
    // Growth Logic
    // ************

    /**
     * {@inheritDoc}
     * <p/>
     * Grows the plant in the world. Specifically, increases
     * the plant growth property by the given amount.
     */
    @Override
    void growPlant(World world, IBlockState state, BlockPos pos, int increase) {
        int growth = increase + getGrowthStage(state);

        if(growth > getMaxAge())
            growth = getMaxAge();

        world.setBlockState(pos, this.getDefaultState().with(this.getGrowthProperty(), growth), 2);
    }
}
