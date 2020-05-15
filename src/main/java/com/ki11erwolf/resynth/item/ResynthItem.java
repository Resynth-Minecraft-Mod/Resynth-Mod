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
package com.ki11erwolf.resynth.item;

import com.ki11erwolf.resynth.ResynthTabs;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.GeneralConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

/**
 * Base class for all Resynth items.
 *
 * Provides extra utility and item registration.
 *
 * @param <T> the subclass to this class (e.i. the inheriting class).
 */
public class ResynthItem<T extends ResynthItem<?>> extends Item {

    /**
     * Flag to prevent queuing an item
     * more than once.
     */
    private boolean isQueued = false;

    /**
     * Constructor for all Resynth items that takes
     * a properties object.
     *
     * @param properties the items properties. The item
     *                   group will be set by this class.
     */
    ResynthItem(Properties properties, String name){
        super(setItemGroup(properties));
        this.setRegistryName(name);
    }

    /**
     * Default constructor for all Resynth items
     * with a default properties object with the
     * item group set to the default Resynth
     * item group {@link ResynthTabs#TAB_RESYNTH}.
     */
    @SuppressWarnings("WeakerAccess")
    public ResynthItem(String name){
        this(new Properties(), name);
    }

    /**
     * Constructor that allows providing a custom
     * ItemGroup.
     *
     * @param name the item name.
     * @param group the custom ItemGroup.
     */
    public ResynthItem(String name, ItemGroup group){
        super(new Properties().group(group));
        this.setRegistryName(name);
    }

    /**
     * {@inheritDoc}.
     *
     * Sets the tooltip for this item from the lang files.
     */
    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        new CollapseableTooltip().setCtrlForDescription().setExpandedTooltip(
                tooltips -> addBlankLine(tooltips).add(getDescriptiveTooltip(this))
        ).write(tooltip);

        addBlankLine(tooltip);
    }

    /**
     * Adds this item instance to ItemRegistrationQueue, which will then
     * be registered to the game from the queue.
     *
     * @return {@code this}.
     */
    T queueRegistration(){
        if(isQueued)
            throw new IllegalStateException(
                    String.format("Item: %s already queued for registration.",
                            this.getClass().getCanonicalName())
            );

        ResynthItems.INSTANCE.queueForRegistration(this);
        isQueued = true;

        //noinspection unchecked //Should NOT be possible.
        return (T)this;
    }

    // ***************
    // Utility Methods
    // ***************

    /**
     * Sets the item group in the properties object
     * to the default Resynth creative tab/item group.
     *
     * @param properties the items properties object.
     * @return properties, the items properties object.
     */
    private static Properties setItemGroup(Properties properties){
        properties.group(ResynthTabs.TAB_RESYNTH);
        return properties;
    }

    /**
     * Turns a given string into a {@link net.minecraft.util.text.StringTextComponent}
     * containing the given string.
     *
     * @param text the given string text.
     * @return a new TextComponentString containing the given string.
     */
    protected static ITextComponent stringToTextComponent(String text){
        return new StringTextComponent(text);
    }

    /**
     * Allows getting an items tooltip from the language file. This method
     * ignores config settings.
     *
     * @param key the tooltip key.
     * @return the text as an {@link ITextComponent} given by the
     * language file.
     */
    protected static ITextComponent getTooltip(String key){
        return stringToTextComponent(TextFormatting.GRAY + I18n.format("tooltip.item.resynth." + key));
    }

    /**
     * Allows getting an items tooltip from the language file. This method
     * ignores config settings.
     *
     * @param key the tooltip key.
     * @param color the color of the tooltip.
     * @param params list of formatting parameters.
     * @return the text as an {@link ITextComponent} given by the
     * language file.
     */
    @SuppressWarnings("SameParameterValue")
    protected static ITextComponent getFormattedTooltip(String key, TextFormatting color, Object... params){
        return stringToTextComponent(color + I18n.format("tooltip.item.resynth." + key, params));
    }

    /**
     * Allows getting an items tooltip from the language file. This method
     * ignores config settings.
     *
     * @param key the tooltip key.
     * @param color the color of the tooltip.
     * @return the text as an {@link ITextComponent} given by the
     * language file.
     */
    @SuppressWarnings({"unused", "SameParameterValue"})
    protected static ITextComponent getTooltip(String key, TextFormatting color){
        return stringToTextComponent(color + I18n.format("tooltip.item.resynth." + key));
    }

    /**
     * Allows getting an items tooltip from the language file. This method
     * ignores config settings.
     *
     * @param item the item who's tooltip we want.
     * @return the text as an {@link ITextComponent} given by the
     * language file.
     */
    @SuppressWarnings("unused")
    protected static ITextComponent getTooltip(@SuppressWarnings("rawtypes") ResynthItem item){
        if(item.getRegistryName() == null){
            return stringToTextComponent(TextFormatting.RED + "Error");
        }

        return getTooltip(item.getRegistryName().toString().replace(":", "."));
    }

    /**
     * Attempts to get the tooltip with the items decription
     * from the user selected language file.
     *
     * @param item the items who's description tooltip we want.
     */
    protected static ITextComponent getDescriptiveTooltip(ResynthItem<?> item){
        if(item == null || item.getRegistryName() == null){
            return stringToTextComponent(TextFormatting.RED + "Error");
        }

        return stringToTextComponent(TextFormatting.DARK_GRAY + I18n.format(
                "tooltip.item." + item.getRegistryName().toString()
                        .replace(":", ".")
        ));
    }

    /**
     * Attempts to get the tooltip with the items decription
     * from the user selected language file. The returned tooltip
     * will be formatted ({@link String#format(String, Object...)})
     * using the given parameters.
     *
     * @param params list of parameters for use in formatting.
     * @param item the name of the items who's tooltip we want and key appended.
     */
    protected static ITextComponent getDescriptiveTooltip(String item, Object... params){
        if(item == null){
            return stringToTextComponent(TextFormatting.RED + "Error");
        }

        return stringToTextComponent(TextFormatting.DARK_GRAY + I18n.format(
                "tooltip.item.resynth." + item, params
        ));
    }

    /**
     * @return {@code true} if the config has enabled the displaying
     * of hightly detailed descriptive toolips.
     */
    protected static boolean areTooltipsEnabled(){
        return ResynthConfig.GENERAL_CONFIG.getCategory(GeneralConfig.class).areTooltipsEnabled();
    }

    /**
     * Will effectively add a blank line tooltip at the
     * end of the given tooltip list.
     *
     * @param tooltips the given list of tooltips.
     * @return the given tooltips list with a blank
     * line appended to the end.
     */
    protected static List<ITextComponent> addBlankLine(List<ITextComponent> tooltips){
        tooltips.add(new StringTextComponent(""));
        return tooltips;
    }

    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("unused")//Additional util methods
    public static class CollapseableTooltip {

        private boolean conditionMet = false;

        private TooltipWriter collapsed = GenericTooltips.UNSPECIFIED;

        private TooltipWriter expanded = GenericTooltips.UNSPECIFIED;

        public void write(List<ITextComponent> tooltip){
            if(conditionMet)    expanded.write(tooltip);
            else                collapsed.write(tooltip);
        }

        public CollapseableTooltip setCondition(boolean condition){
            this.conditionMet = condition;
            return this;
        }

        public CollapseableTooltip setConditionToShiftDown(){
            this.conditionMet = Screen.hasShiftDown();
            return this;
        }

        public CollapseableTooltip setConditionToControlDown(){
            this.conditionMet = Screen.hasControlDown();
            return this;
        }

        public CollapseableTooltip setConditionToAltDown(){
            this.conditionMet = Screen.hasAltDown();
            return this;
        }

        public CollapseableTooltip setCollapsedTooltip(TooltipWriter tooltip){
            this.collapsed = tooltip;
            return this;
        }

        public CollapseableTooltip setExpandedTooltip(TooltipWriter tooltip){
            this.expanded = tooltip;
            return this;
        }

        public CollapseableTooltip setShiftForStats(){
            setConditionToShiftDown().setCollapsedTooltip(GenericTooltips.SHIFT_FOR_STATS);
            return this;
        }

        public CollapseableTooltip setCtrlForDescription(){
            setCondition(Screen.hasControlDown() && areTooltipsEnabled()).setCollapsedTooltip(
                areTooltipsEnabled() ? GenericTooltips.CTRL_FOR_DESCRIPTION : GenericTooltips.NULL
            );
            return this;
        }

        public CollapseableTooltip setAltForAdditionalInfo(){
            setConditionToAltDown().setCollapsedTooltip(GenericTooltips.ALT_FOR_ADDITIONAL_INFO);
            return this;
        }

        public CollapseableTooltip setExpandingSpacing(){
            this.setCollapsedTooltip(CollapseableTooltip.GenericTooltips.NULL)
                    .setExpandedTooltip(ResynthItem::addBlankLine);
            return this;
        }

        public interface TooltipWriter {
            void write(List<ITextComponent> tooltip);
        }

        public enum GenericTooltips implements TooltipWriter {

            SHIFT_FOR_STATS("shift_for_stats", TextFormatting.GOLD),

            CTRL_FOR_DESCRIPTION("ctrl_for_description", TextFormatting.GREEN),

            ALT_FOR_ADDITIONAL_INFO("alt_for_info", TextFormatting.DARK_PURPLE),

            UNSPECIFIED("unspecified", TextFormatting.RED, TextFormatting.RED),

            NULL(null, null, null) {
                @Override public void write(List<ITextComponent> tooltip) { /* No operaton */ }
            };

            private static final String KEY_PREFIX = "general.";

            private final String tooltipKey;

            private final TextFormatting baseColor;

            private final TextFormatting highlightColor;

            GenericTooltips(String tooltipKey, TextFormatting highlightColor){
                this(tooltipKey, TextFormatting.GRAY, highlightColor);
            }

            GenericTooltips(String tooltipKey, TextFormatting baseColor, TextFormatting highlightColor){
                this.tooltipKey = tooltipKey;
                this.baseColor = baseColor;
                this.highlightColor = highlightColor;
            }

            @Override
            public void write(List<ITextComponent> tooltip) {
                tooltip.add(getFormattedTooltip(
                        KEY_PREFIX + tooltipKey, baseColor, highlightColor, baseColor
                ));
            }
        }
    }
}
