package com.ki11erwolf.resynth.plantsets.item;

import com.ki11erwolf.resynth.ResynthTabs;
import com.ki11erwolf.resynth.item.ResynthItem;
import com.ki11erwolf.resynth.plantsets.set.ICrystallineSetProperties;
import com.ki11erwolf.resynth.plantsets.set.PlantSetProperties;
import com.ki11erwolf.resynth.plantsets.set.PlantSetUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The seeds item for every Growable resynth plant type (plant block).
 * Handles placement of the plant type as well as displays a tooltip
 * with the properties (e.g. growth chance) of the plant type.
 */
public class ItemSeeds extends ResynthItem<ItemSeeds> implements IPlantable {

    /**
     * The prefix for all seeds items.
     */
    private static final String PREFIX = "seeds";

    /**
     * The specific plant type (plant block)
     * this seeds item type will spawn.
     */
    private final IBlockState plant;

    /**
     * The name of the set this seeds item
     * type is for (e.g. diamond).
     */
    private final String setName;

    /**
     * The name of the plant set type
     * this seeds item is for.
     */
    private final String setTypeName;

    /**
     * The properties specific to the plant set
     * this seeds item type is registered to.
     */
    private final PlantSetProperties setProperties;

    /**
     * @param setType the name of the plant set type (e.g. crystalline).
     * @param setName the name of the plant set (e.g. diamond).
     * @param plant the specific plant type (plant block instance) to place.
     * @param setProperties the properties specific to the plant set.
     */
    public ItemSeeds(String setType, String setName, Block plant, PlantSetProperties setProperties){
        super(setType + "_" + PREFIX + "_" + setName, ResynthTabs.TAB_RESYNTH_SEEDS);
        this.setTypeName = setType;
        this.setProperties = setProperties;
        this.setName = setName;
        this.plant = plant.getDefaultState();
    }

    /**
     * Handles placing the plant type (plant block
     * instance) in the world.
     *
     * @return {@link EnumActionResult#SUCCESS} if the plant
     * was placed, {@link EnumActionResult#FAIL} otherwise.
     */
    @Override
    public EnumActionResult onItemUse(ItemUseContext context) {
        IWorld world = context.getWorld();
        BlockPos blockpos = context.getPos().up();

        if (context.getFace() == EnumFacing.UP && world.isAirBlock(blockpos)
                && this.plant.isValidPosition(world, blockpos)) {

            world.setBlockState(blockpos, this.plant, 11);
            ItemStack itemstack = context.getItem();
            EntityPlayer entityplayer = context.getPlayer();

            if (entityplayer instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)entityplayer, blockpos, itemstack);
            }

            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }

    /**
     * Constructs the tooltip for the item.
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn){
        PlantSetUtil.PlantSetTooltipUtil.setPropertiesTooltip(tooltip, setProperties);
        setDescriptiveTooltip(tooltip, setTypeName + "_" + PREFIX, setName);
    }

    /**
     * {@inheritDoc}
     *
     * @return the plant type (plant block) this seeds item type (instance) spawns.
     */
    @Override
    public IBlockState getPlant(IBlockReader world, BlockPos pos) {
        return plant;
    }
}
