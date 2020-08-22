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

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * A utility class that simplifies creating item and block
 * tooltips that are collapsed by default but can be shown
 * (expanded) with a conditional (normally bound to  a key
 * such as shift or ctrl). ExpandingTooltips can be chained
 * together and mixed with traditional tooltips.
 */
//TODO: Create "Additional Info" tooltips for plant items.
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

    public List<ITextComponent> write(List<ITextComponent> tooltip){
        if(conditionMet)    expanded.write(tooltip);
        else                collapsed.write(tooltip);
        return tooltip;
    }

    public ExpandingTooltip setCondition(boolean condition){
        this.conditionMet = condition;
        return this;
    }

    //TODO: Fix this mess

    public ExpandingTooltip setConditionToShiftDown(){
        this.conditionMet = Screen.func_231174_t_();//Screen.func_231173_s_();//Screen.func_231172_r_();//Screen.hasShiftDown();
        return this;
    }

    public ExpandingTooltip setConditionToControlDown(){
        this.conditionMet = Screen.func_231174_t_();//Screen.func_231173_s_();//Screen.func_231172_r_();//Screen.hasControlDown();
        return this;
    }

    public ExpandingTooltip setConditionToAltDown(){
        this.conditionMet = Screen.func_231174_t_();//Screen.func_231173_s_();//Screen.func_231172_r_();//Screen.hasAltDown();
        return this;
    }

    public ExpandingTooltip setCollapsedTooltip(Tooltip tooltip){
        this.collapsed = tooltip == null ? CommonTooltips.NULL : tooltip;
        return this;
    }

    public ExpandingTooltip setExpandedTooltip(Tooltip tooltip){
        this.expanded = tooltip == null ? CommonTooltips.NULL : tooltip;
        return this;
    }

    public ExpandingTooltip setShiftForStats(Tooltip expanded){
        setConditionToShiftDown().setCollapsedTooltip(CommonTooltips.SHIFT_FOR_STATS)
                .setExpandedTooltip(expanded);
        return this;
    }

    public ExpandingTooltip setCtrlForDescription(Tooltip expanded){
        setCondition(
                Screen.func_231174_t_()/*Screen.func_231173_s_();//Screen.func_231172_r_();Screen.hasControlDown()*/
                        && Tooltip.areTooltipsEnabled()).setCollapsedTooltip(
                Tooltip.areTooltipsEnabled() ? CommonTooltips.CTRL_FOR_DESCRIPTION : CommonTooltips.NULL
        ).setExpandedTooltip(expanded);
        return this;
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public ExpandingTooltip setAltForAdditionalInfo(Tooltip expanded){
        setConditionToAltDown().setCollapsedTooltip(CommonTooltips.ALT_FOR_ADDITIONAL_INFO)
                .setExpandedTooltip(expanded);
        return this;
    }
    
}
