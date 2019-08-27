package com.ki11erwolf.resynth.plantsets.item;

import com.ki11erwolf.resynth.ResynthTabs;
import com.ki11erwolf.resynth.item.ResynthItem;
import com.ki11erwolf.resynth.plantsets.set.ICrystallineSetProperties;
import com.ki11erwolf.resynth.plantsets.set.PlantSetUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The produce item for Crystalline plant types (e.g. diamond shard).
 */
public class ItemShard extends ResynthItem {

    /**
     * The prefix given to all shard produce items.
     */
    private static final String PREFIX = "shard";

    /**
     * The name of the plant set (e.g. diamond).
     */
    private final String setName;

    /**
     * The plant set properties.
     */
    private final ICrystallineSetProperties setProperties;

    /**
     * @param setTypeName the name of the plant set type (e.g. crystalline).
     * @param setName the name of the plant set (e.g. diamond).
     * @param properties the properties for the specific plant set.
     */
    public ItemShard(String setTypeName, String setName, ICrystallineSetProperties properties) {
        super(setTypeName + "_" + PREFIX + "_" + setName, ResynthTabs.TAB_RESYNTH_PRODUCE);
        this.setName = setName;
        this.setProperties = properties;
    }

    /**
     * Constructs the tooltip for the item.
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn){
        PlantSetUtil.PlantSetTooltipUtil.setPropertiesTooltip(tooltip, setProperties);
        setDescriptiveTooltip(tooltip, PREFIX, setName);
    }
}
