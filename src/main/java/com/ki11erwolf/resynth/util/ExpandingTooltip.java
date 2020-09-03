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
package com.ki11erwolf.resynth.util;

import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * A utility class that simplifies creating item and block
 * tooltips that are collapsed by default but can be shown
 * (expanded) with a conditional (normally bound to  a key
 * such as shift or ctrl). ExpandingTooltips can be chained
 * together and mixed with traditional tooltips.
 */
public class ExpandingTooltip {

    /**
     * The condition flag that determines if the collapsed
     * or expanded tooltip is shown. The flag is set to
     * {@code true} when the expanded tooltip should be
     * shown and set to false {@code false} when the
     * collpased tooltip shoud be shown.
     */
    private boolean conditionMet = false;

    /**
     * The {@link Tooltip} written to the item/block
     * toolip when this ExpandingTooltip is in the
     * collapsed state.
     */
    private Tooltip collapsed = CommonTooltips.UNSPECIFIED;

    /**
     * The {@link Tooltip} written to the item/block
     * toolip when this ExpandingTooltip is in the
     * expanded state.
     */
    private Tooltip expanded = CommonTooltips.UNSPECIFIED;

    /**
     * Handles writing the tooltip, either collapsed
     * or extended, to the given {@code tooltip} List.
     *
     * @param tooltip a given tooltip list to write the tooltips to.
     * @return {@code tooltip}. Useful for daisy chaining.
     */
    public List<ITextComponent> write(List<ITextComponent> tooltip) {
        if(conditionMet) expanded.write(tooltip);
        else collapsed.write(tooltip);
        return tooltip;
    }

    /**
     * Sets the tooltips condition, which is needed to be
     * {@code true}, before it can expand.
     *
     * @param condition the boolean condition determining
     *                  if the tooltip expands or not.
     * @return {@code this;}
     */
    public ExpandingTooltip setCondition(boolean condition){
        this.conditionMet = condition;
        return this;
    }

    /**
     * Shortcut method that makes setting the condition
     * {@link #setCondition(boolean)} to be true when
     * a SHIFT key is held down.
     *
     * @return {@code this;}
     */
    public ExpandingTooltip setConditionToShiftDown(){
        this.conditionMet = CommonRKeys.SHIFT.rKey.query();
        return this;
    }

    /**
     * Shortcut method that makes setting the condition
     * {@link #setCondition(boolean)} to be true when
     * a CTRL key is held down.
     *
     * @return {@code this;}
     */
    public ExpandingTooltip setConditionToControlDown(){
        this.conditionMet = CommonRKeys.CONTROL.rKey.query();
        return this;
    }

    /**
     * Shortcut method that makes setting the condition
     * {@link #setCondition(boolean)} to be true when
     * an ALT key is held down.
     *
     * @return {@code this;}
     */
    public ExpandingTooltip setConditionToAltDown(){
        this.conditionMet = CommonRKeys.ALT.rKey.query();
        return this;
    }

    /**
     * Sets what the tooltip will display in its collapsed form.
     *
     * @param tooltip the tooltip text in collapsed form.
     * @return {@code this;}
     */
    public ExpandingTooltip setCollapsedTooltip(Tooltip tooltip){
        this.collapsed = tooltip == null ? CommonTooltips.NULL : tooltip;
        return this;
    }

    /**
     * Sets what the tooltip will display in its expanded form.
     *
     * @param tooltip the tooltip text in expanded form.
     * @return {@code this;}
     */
    public ExpandingTooltip setExpandedTooltip(Tooltip tooltip){
        this.expanded = tooltip == null ? CommonTooltips.NULL : tooltip;
        return this;
    }

    /**
     * A shortcut method that makes setting this tooltip up as
     * a "press shift for stats" tooltip type quick and easy.
     *
     * @param expanded the tooltip to be displayed when expanded.
     * @return {@code this;}
     */
    public ExpandingTooltip setShiftForStats(Tooltip expanded){
        setConditionToShiftDown().setCollapsedTooltip(CommonTooltips.SHIFT_FOR_STATS)
                .setExpandedTooltip(expanded);
        return this;
    }

    /**
     * A shortcut method that makes setting this tooltip up as
     * a "press ctrl for description" tooltip type quick and easy.
     *
     * @param expanded the tooltip to be displayed when expanded.
     * @return {@code this;}
     */
    public ExpandingTooltip setCtrlForDescription(Tooltip expanded){
        boolean isFiring = CommonRKeys.CONTROL.rKey.query();

        setCondition(isFiring && Tooltip.areTooltipsEnabled())
                .setCollapsedTooltip(Tooltip.areTooltipsEnabled() ? CommonTooltips.CTRL_FOR_DESCRIPTION : CommonTooltips.NULL)
                .setExpandedTooltip(expanded);

        return this;
    }

    /**
     * A shortcut method that makes setting this tooltip up as
     * a "press alt for more" tooltip type quick and easy.
     *
     * @param expanded the tooltip to be displayed when expanded.
     * @return {@code this;}
     */
    @SuppressWarnings({"unused", "RedundantSuppression"})
    public ExpandingTooltip setAltForAdditionalInfo(Tooltip expanded){
        setConditionToAltDown().setCollapsedTooltip(
                CommonTooltips.ALT_FOR_ADDITIONAL_INFO
        ).setExpandedTooltip(expanded);
        return this;
    }
    
}
