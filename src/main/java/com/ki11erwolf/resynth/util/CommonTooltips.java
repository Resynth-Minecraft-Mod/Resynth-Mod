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
import net.minecraft.util.text.TextFormatting;

import java.util.List;

import static com.ki11erwolf.resynth.util.Tooltip.localize;
import static com.ki11erwolf.resynth.util.Tooltip.toTextComponent;

/**
 * An enum list of some common {@link Tooltip tooltips}
 * used in item and block tooltips.
 */
public enum CommonTooltips implements Tooltip {

    SHIFT_FOR_STATS("shift_for_stats",TextFormatting.GOLD),
    CTRL_FOR_DESCRIPTION("ctrl_for_description", TextFormatting.GREEN),
    ALT_FOR_ADDITIONAL_INFO("alt_for_info", TextFormatting.DARK_PURPLE),
    UNSPECIFIED("unspecified", TextFormatting.RED, TextFormatting.RED),

    NEW_LINE(null, null, null){
        @Override public void write(List<ITextComponent> tooltip) {
            tooltip.add(toTextComponent(""));
        }
    },

    NULL(null, null, null) {
        @Override public void write(List<ITextComponent> tooltip) {
            /* No operaton */
        }
    };

    /**
     * The String prefix of every language key a common tooltip is stored under.
     */
    private static final String KEY_PREFIX = "tooltip.resynth.common.";

    /**
     * The language key of the specific CommonTooltip instance, excluding
     * the {@link #KEY_PREFIX}.
     */
    private final String tooltipKey;

    /**
     * The {@link TextFormatting color} of the whole
     * CommonTooltip, excluding any additional color
     * modifiers.
     */
    private final TextFormatting baseColor;

    /**
     * An optional highlight {@link TextFormatting color}
     * that applies to some CommonTooltips.
     */
    private final TextFormatting highlightColor;

    /**
     * Constructor that takes a key and a tooltip color.
     *
     * @param tooltipKey the language key (excluding prefix)
     *                   of a localized String.
     * @param baseColor the {@link TextFormatting color}
     *                       of the CommonTooltip.
     */
    CommonTooltips(String tooltipKey, TextFormatting baseColor){
        this(tooltipKey, TextFormatting.GRAY, baseColor);
    }

    /**
     * Constructor that takes an optional {@code highlight}
     * {@link TextFormatting color} that applies to some
     * CommonTooltips.
     *
     * @param tooltipKey the language key (excluding prefix)
     *                   of a localized String.
     * @param baseColor the {@link TextFormatting color}
     *                       of the CommonTooltip.
     * @param highlightColor the color used to highlight a
     *                       specified part of the tooltip.
     */
    CommonTooltips(String tooltipKey, TextFormatting baseColor, TextFormatting highlightColor){
        this.tooltipKey = tooltipKey;
        this.baseColor = baseColor;
        this.highlightColor = highlightColor;
    }

    /**
     * {@inheritDoc}
     *
     * Will write the common tooltip to the given
     * item/block tooltip array.
     *
     * @param tooltip the tooltip array obtained from
     *                an item or block.
     */
    @Override
    public void write(List<ITextComponent> tooltip) {
        tooltip.add(toTextComponent(
                 baseColor + localize(KEY_PREFIX + tooltipKey, highlightColor, baseColor)
        ));
    }
}
