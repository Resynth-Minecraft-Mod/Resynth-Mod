/*
 * Copyright 2018 Ki11er_wolf
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * The block class for all plants that
 * produce mob drops.
 */
public abstract class BlockPlantBiochemical extends BlockPlantBase{

    /**
     * Age (height) property of the plant type.
     */
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);

    /**
     * List of bounding boxes.
     */
    protected static final AxisAlignedBB[] MC_STEM_AABB = new AxisAlignedBB[] {
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.125D, 0.625D), //Age=0
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.25D, 0.625D),  //Age=1
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.375D, 0.625D), //Age=2
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.375D, 0.625D),   //Age=3
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.50D, 0.625D), //Age=4
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.625D, 0.625D),  //Age=5
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.75D, 0.625D), //Age=6
            new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.875D, 0.625D)    //Age=7
    };

    /**
     * Creates a new generic biochemical plant type.
     *
     * @param name the name of the plants items.
     */
    public BlockPlantBiochemical(String name){
        super(name);
        this.setDefaultState(
                this.blockState.getBaseState().withProperty(this.getAgeProperty(), 0));
        this.setTickRandomly(true);
        this.setCreativeTab(null);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.disableStats();
    }

    /**
     * Determines the correct bounding box to use.
     *
     * @param state -
     * @param source -
     * @param pos -
     * @return the correct bounding box for the given plants age.
     */
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        return MC_STEM_AABB[state.getValue(this.getAgeProperty())];
    }

    /**
     * @param age -
     * @return the correct block state for the given age.
     */
    public IBlockState withAge(int age){
        return this.getDefaultState().withProperty(this.getAgeProperty(), age);
    }

    /**
     * @param state -
     * @return true if the given plant is fully grown.
     */
    public boolean isMaxAge(IBlockState state){
        return state.getValue(this.getAgeProperty()) >= this.getMaxAge();
    }

    /**
     * @return the maximum age (height) this plant type can grow to.
     */
    public int getMaxAge(){
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
     * Increases the plants age (height) property.
     *
     * @param worldIn -
     * @param pos -
     * @param state -
     */
    public void grow(World worldIn, BlockPos pos, IBlockState state){
        int i = this.getAge(state) + this.getBonemealAgeIncrease(worldIn);
        int j = this.getMaxAge();

        if (i > j){
            i = j;
        }

        worldIn.setBlockState(pos, this.withAge(i), 2);
    }

    /**
     * @param state -
     * @return true if the provided block is mineral soil.
     */
    protected boolean canSustainBush(IBlockState state){
        return state.getBlock() == ResynthBlocks.BLOCK_MINERAL_SOIL;
    }

    /**
     * @return the plants age (height) property.
     */
    protected PropertyInteger getAgeProperty(){
        return AGE;
    }

    /**
     * @param state -
     * @return the actual age of the plant (0-7)
     */
    protected int getAge(IBlockState state){
        return state.getValue(this.getAgeProperty());
    }

    /**
     * @param worldIn -
     * @return he age to grow by if bonemeal is used.
     */
    protected int getBonemealAgeIncrease(World worldIn){
        return MathHelper.getInt(worldIn.rand, 2, 5);
    }

    /**
     * {@inheritDoc}
     * Grows the plant.
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
            int i = this.getAge(state);

            if (i < this.getMaxAge()){
                if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(
                        worldIn, pos, state, true)){
                    worldIn.setBlockState(pos, this.withAge(i + 1), 2);
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
        if(this.getAge(state) >= getMaxAge() && !worldIn.isRemote){
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ,
                    SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,
                    0.5F, 0.4F / (new Random().nextFloat() * 0.4F + 0.8F)
            );

            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(),
                    new ItemStack(getProduce(), 1)));

            worldIn.setBlockState(pos, this.withAge(3), 2);
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
        int age = getAge(state);

        if (age >= getMaxAge()){
            drops.add(new ItemStack(this.getSeed(), 1, 0));
        }
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
        return this.isMaxAge(state) ? this.getProduce() : this.getSeed();
    }

    /**
     * {@inheritDoc}
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
     *
     * @param worldIn
     * @param pos
     * @param state
     * @param isClient
     * @return true if this plant is not at its max age.
     */
    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient){
        return !this.isMaxAge(state);
    }

    /**
     * {@inheritDoc}
     * Determines if bonemeal can be used on this plant.
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
        return this.withAge(meta);
    }

    /**
     * {@inheritDoc}
     *
     * @param state
     * @return
     */
    @Override
    public int getMetaFromState(IBlockState state){
        return this.getAge(state);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, AGE);
    }

    /**
     * @return the seed type for this plant.
     */
    protected abstract Item getSeed();

    /**
     * @return the item this plant produces.
     */
    protected abstract Item getProduce();
}
