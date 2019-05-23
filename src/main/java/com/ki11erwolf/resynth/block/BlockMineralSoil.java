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
import com.ki11erwolf.resynth.igtooltip.HwylaDataProvider;
import com.ki11erwolf.resynth.igtooltip.TOPDataProvider;
import com.ki11erwolf.resynth.item.ResynthItems;
import com.ki11erwolf.resynth.util.MinecraftUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.state.IntegerProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Mineral Enriched Soil.
 *
 * The soil block for the mods plants.
 */
@SuppressWarnings("deprecation")
public class BlockMineralSoil extends ResynthTileEntity<TileEntityMineralSoil>
        /*implements HwylaDataProvider, TOPDataProvider*/ {

    /**
     * The metadata value for the blocks appearance.
     */
    public static final IntegerProperty
            //TODO: change string value from percent to stage
            STAGE = IntegerProperty.create("percent", 0, 4);

    /**
     * The bounding box for the block.
     */
    protected static final AxisAlignedBB MINERAL_SOIL_AABB = new AxisAlignedBB(
            0.0D, 0.0D, 0.0D,
            1.0D, 0.9375D, 1.0D
    );

    /**
     * The bounding box (or dimensions) of the block.
     */
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(
            0.0D, 0.0D, 0.0D,
            1.0D, 0.9375D, 1.0D
    );

    /**
     * Standard block constructor.
     */
    protected BlockMineralSoil(){
        super(
                Properties.create(Material.GRASS).hardnessAndResistance(2.0F).sound(SoundType.GROUND),
                "mineralSoil");
        //this.setHardness(2.0F);
        //this.setDefaultState(this.blockState.getBaseState().withProperty(STAGE, 0));
        //this.setLightOpacity(255);
    }

    /**
     * Drops the amount of ItemMineralRock put
     * into it (within a an item or two of accuracy)
     * and a Mineral Crystal.
     *
     * @param worldIn -
     * @param pos -
     * @param state -
     */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state){
        TileEntityMineralSoil te = getTileEntity(worldIn, pos);

        int drops = (int)((te.getMineralPercentage() - 1.0F) / 1.0F /*TODO: Config*/);
        EntityItem items = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(),
                new ItemStack(ResynthItems.ITEM_MINERAL_ROCK, drops));
        worldIn.spawnEntity(items);

        EntityItem mineralCrystal = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(),
                new ItemStack(ResynthItems.ITEM_MINERAL_CRYSTAL, 1));
        worldIn.spawnEntity(mineralCrystal);
    }

    /**
     * Increases the blocks tile entity percentage value
     * by a config specified value at the cost
     * of ItemMineralRocks.
     */
    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){

        if(getTileEntity(worldIn, pos).getMineralPercentage() >= 50)
            return false;

        //Mineral Rock
        if(player.getHeldItemMainhand().getItem().getClass().equals(ResynthItems.ITEM_MINERAL_ROCK.getClass())){
            new MinecraftUtil.SideSensitiveCode(worldIn){
                @Override
                public void onServer(){
                    ItemStack offer = player.getHeldItemMainhand();
                    TileEntityMineralSoil entity = getTileEntity(worldIn, pos);

                    if(hand.equals(EnumHand.MAIN_HAND)){
                        if(!player.isCreative())
                            offer.shrink(1);
                        entity.increaseMineralPercentage(1.0F/*TODO: Config*/);
                    }

                    float percentage = entity.getMineralPercentage();
                    updateState(worldIn, pos, state);
                    ((TileEntityMineralSoil) worldIn.getTileEntity(pos)).setMineralPercentage(percentage);
                    String perc = String.valueOf(entity.getMineralPercentage());

                    if(true/*TODO: Config*/)
                        player.sendMessage(new TextComponentString("Soil mineral content: "
                            + perc
                            .substring(0, perc.length() > 4 ? 4 : perc.length())
                            + "%"));
                }
            }.execute();
            return true;
        }

        //Dense Mineral Rock
        if(player.getHeldItemMainhand().getItem().getClass().equals(ResynthItems.ITEM_DENSE_MINERAL_ROCK.getClass())){
            new MinecraftUtil.SideSensitiveCode(worldIn) {
                @Override
                public void onServer() {
                    ItemStack offer = player.getHeldItemMainhand();
                    TileEntityMineralSoil entity = getTileEntity(worldIn, pos);

                    if(hand.equals(EnumHand.MAIN_HAND)){
                        if(!player.isCreative())
                            offer.shrink(1);
                        entity.increaseMineralPercentage(1.0F /*TODO: Config*/ * 9);
                    }

                    float percentage = entity.getMineralPercentage();
                    updateState(worldIn, pos, state);
                    ((TileEntityMineralSoil) worldIn.getTileEntity(pos)).setMineralPercentage(percentage);
                    String perc = String.valueOf(entity.getMineralPercentage());

                    if(true /*TODO: Config*/)
                        player.sendMessage(new TextComponentString("Soil mineral content: "
                            + perc
                            .substring(0, perc.length() > 4 ? 4 : perc.length())
                            + "%"));
                }
            }.execute();
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param blockState {@inheritDoc}
     * @param blockAccess {@inheritDoc}
     * @param pos {@inheritDoc}
     * @param side {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
                                        EnumFacing side){
        switch (side){
            case UP:
                return true;
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST:
                IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
                Block block = iblockstate.getBlock();
                return !iblockstate.isOpaqueCube() && block != this && block != Blocks.GRASS_PATH;
            default:
                return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Drops Minecraft Dirt instead of this block itself.
     * The extra item drops are handled when the block is broken.
     *
     * @param state {@inheritDoc}
     * @param rand {@inheritDoc}
     * @param fortune {@inheritDoc}
     * @return {@inheritDoc} This block.
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return Item.getItemFromBlock(Blocks.DIRT);
    }

    /**
     * {@inheritDoc}
     *
     * @param meta {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public IBlockState getStateFromMeta(int meta){
        return this.getDefaultState().withProperty(STAGE, meta);
    }

    /**
     * {@inheritDoc}
     *
     * @param state {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(STAGE);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @SuppressWarnings("RedundantArrayCreation")
    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, new IProperty[] {STAGE});
    }

    /**
     * {@inheritDoc}
     *
     * @param worldIn {@inheritDoc}
     * @param state {@inheritDoc}
     * @param pos {@inheritDoc}
     * @param face {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face){
        return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    /**
     * {@inheritDoc}
     *
     * Returns the bounding box.
     *
     * @param state -
     * @param source -
     * @param pos -
     * @return the bounding box. {@link #MINERAL_SOIL_AABB}
     */
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        return MINERAL_SOIL_AABB;
    }

    /**
     * @param state -
     * @return {@code false}.
     */
    @Override
    public boolean isNormalCube(IBlockState state){
        return false;
    }

    /**
     * @param state -
     * @return {@code false}.
     */
    @Override
    public boolean isFullCube(IBlockState state){
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Class<TileEntityMineralSoil> getTileEntityClass() {
        return TileEntityMineralSoil.class;
    }

    /**
     * {@inheritDoc}
     * @param world the world the block is in.
     * @param state the block state of the block.
     * @return {@inheritDoc}
     */
    @Override
    public TileEntityMineralSoil createTileEntity(World world, IBlockState state) {
        return new TileEntityMineralSoil();
    }

    /**
     * {@inheritDoc}
     * Adds a tooltip on how to use the block.
     *
     * @param stack
     * @param worldIn
     * @param tooltip
     * @param flagIn
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn){
        tooltip.add(new TextComponentString(
                "Made by tilling (Right Clicking) dirt/grass with a Mineral Hoe."
        ));
        tooltip.add(new TextComponentString(
                "Right click with mineral rocks or dense mineral rock to increase plant growth."
        ));
    }

    /**
     * Updates the look of the block to match
     * the percentage value in its tile entity.
     *
     * @param worldIn -
     * @param pos -
     * @param state -
     */
    private void updateState(World worldIn, BlockPos pos, IBlockState state){
        worldIn.setBlockState(pos, getStateFromTileEntity(worldIn, pos));
    }

    /**
     * Get's the correct block state for the percentage
     * value in the blocks tile entity.
     *
     * @param worldIn -
     * @param pos -
     * @return the block state matching the tile entities percentage value.
     */
    private static IBlockState getStateFromTileEntity(World worldIn, BlockPos pos){
        float percentage =
                ((TileEntityMineralSoil) Objects.requireNonNull(worldIn.getTileEntity(pos))).getMineralPercentage();

        if(percentage > 49.9){
            return worldIn.getBlockState(pos).with(STAGE, 4/*50%*/);
        }

        if(percentage > 39.9){
            return worldIn.getBlockState(pos).with(STAGE, 3/*40%*/);
        }

        if(percentage > 29.9){
            return worldIn.getBlockState(pos).with(STAGE, 2/*30%*/);
        }

        if(percentage > 19.9){
            return worldIn.getBlockState(pos).with(STAGE, 1/*20%*/);
        }

        return worldIn.getBlockState(pos).with(STAGE, 0/*10%*/);
    }

//    /**
//     * {@inheritDoc}
//     *
//     * Gives the block tile entity information to the Hwyla tooltip.
//     *
//     * @param itemStack the item stack returned by the block.
//     * @param currentTip {@inheritDoc}
//     * @param accessor accessor used to get data from the block such as NBT.
//     * @param config current Hwyla configuration
//     */
//    @Override
//    public void onHwylaBodyRequest(ItemStack itemStack, List<String> currentTip,
//                                   IWailaDataAccessor accessor, IWailaConfigHandler config) {
//
//        TileEntity te = accessor.getTileEntity();
//        if(!(te instanceof TileEntityMineralSoil)) {
//            return;
//        }
//
//        currentTip.add(
//                TextFormatting.GREEN
//                + "Mineral Content: "
//                + accessor.getNBTData().getFloat("mineral-content")
//                + "%"
//        );
//
//        currentTip.add(
//                TextFormatting.DARK_PURPLE
//                + "Mineral Stage: "
//                + percentToStage(accessor.getNBTData().getFloat("mineral-content"))
//        );
//    }
//
//    /**
//     * Gives the blocks tile entity data to the Hwyla client side NBT data
//     *
//     * @param player player looking at a block.
//     * @param te blocks tile entity.
//     * @param tag Hwyla client side NBT data.
//     * @param world current world the player is in.
//     * @param pos position of the block the player is looking at.
//     *
//     */
//    @Override
//    public void onHwylaNBTDataRequest(EntityPlayerMP player, TileEntity te,
//                                      NBTTagCompound tag, World world, BlockPos pos){
//
//        if(!(te instanceof TileEntityMineralSoil))
//            return;
//
//        tag.setFloat(
//                "mineral-content",
//                ((TileEntityMineralSoil)te).getMineralPercentage()
//        );
//    }
//
//    /**
//     * {@inheritDoc}
//     *
//     * @param mode the usage mode the probe is in.
//     * @param probeInfo probe information holder. Add block
//     *                  information to this object.
//     * @param player the player the information will be provided to.
//     * @param world the world the player is in.
//     * @param blockState state of the block the probe is probing.
//     * @param data probe ray tracing results.
//     */
//    @Override
//    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player,
//                             World world, IBlockState blockState, IProbeHitData data) {
//        TileEntity te = world.getTileEntity(data.getPos());
//
//        if (!(te instanceof TileEntityMineralSoil)) {
//            return;
//        }
//
//        TileEntityMineralSoil dataTileEntity = (TileEntityMineralSoil) te;
//
//        probeInfo.horizontal()
//                .text(TextFormatting.GOLD + "Mineral Content: ");
//
//        probeInfo.horizontal()
//                .item(new ItemStack(ResynthBlocks.BLOCK_MINERAL_SOIL))
//                .text(
//                        TextFormatting.GOLD + "Stage: " +
//                        TextFormatting.AQUA + percentToStage(dataTileEntity.getMineralPercentage())
//                );
//
//        probeInfo.horizontal().text(TextFormatting.GOLD + "Percentage:");
//        probeInfo.horizontal()
//                .item(new ItemStack(ResynthItems.ITEM_MINERAL_ROCK))
//                .progress(
//                        Math.round(dataTileEntity.getMineralPercentage())
//                                % 100, 100,
//                        probeInfo.defaultProgressStyle().suffix("%")
//                );
//    }

    /**
     * @param percent mineral content percentage.
     * @return a String containing information about which
     * stage the block is in from its metadata.
     */
    private static String percentToStage(float percent){
        if(percent > 49.9){
            return "5 of 5";
        }

        if(percent > 39.9){
            return "4 of 5";
        }

        if(percent > 29.9){
            return "3 of 5";
        }

        if(percent > 19.9){
            return "2 of 5";
        }

        return "1 of 5";
    }

}
