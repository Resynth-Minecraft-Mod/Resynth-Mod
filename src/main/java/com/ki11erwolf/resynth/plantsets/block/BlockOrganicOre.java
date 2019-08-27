package com.ki11erwolf.resynth.plantsets.block;

import com.ki11erwolf.resynth.ResynthTabs;
import com.ki11erwolf.resynth.block.ResynthBlock;
import com.ki11erwolf.resynth.plantsets.set.IMetallicSetProperties;
import com.ki11erwolf.resynth.plantsets.set.PlantSetUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The produce type for Metallic plants - a simple
 * block used as a furnace recipe, info provider,
 * and method of obtaining seeds.
 */
public class BlockOrganicOre extends ResynthBlock<BlockOrganicOre> {

    /**
     * The prefix for all blocks of this type.
     */
    private static final String PREFIX = "organic_ore";

    /**
     * The properties of the plant set this block
     * belongs to.
     */
    private final IMetallicSetProperties properties;

    /**
     * @param setTypeName the name of the plant set type.
     * @param name the name of the plant set.
     * @param properties the plant set properties.
     */
    public BlockOrganicOre(String setTypeName, String name, IMetallicSetProperties properties) {
        super(
                Block.Properties.create(Material.ROCK),
                new Item.Properties().group(ResynthTabs.TAB_RESYNTH_PRODUCE),
                setTypeName + "_" + PREFIX + "_" + name
        );
        this.properties = properties;
    }

    /**
     * Constructs the tooltip for the block.
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn){
        PlantSetUtil.PlantSetTooltipUtil.setPropertiesTooltip(tooltip, properties);
        setDescriptiveTooltip(tooltip, PREFIX);
    }
}
