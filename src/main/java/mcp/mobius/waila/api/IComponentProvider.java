package mcp.mobius.waila.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public interface IComponentProvider {
    default ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return ItemStack.EMPTY;
    }

    default void appendHead(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
    }

    default void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
    }

    default void appendTail(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
    }
}
