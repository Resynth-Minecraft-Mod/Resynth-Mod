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

import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.GeneralConfig;
import com.ki11erwolf.resynth.plant.set.IPlantSetProperties;
import com.ki11erwolf.resynth.plant.set.PlantSetUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

/**
 * A utility class that enables creating item & block tooltips
 * as OOP objects that can write to a Minecraft tooltip array.
 * The tooltip class also provides other utilities that aid in
 * the creation and usage of normal Minecraft tooltips.
 */
public interface Tooltip {

    /**
     * The complete parameters write method called by the tooltip handler
     * for tooltip text when it wants to write the tooltip.
     *
     * <p/>Normally, this method just calls the shortened {@link #write(List)}
     * method, which is most often used. Only use this write method when the
     * additional parameters are needed.
     *
     * @param tooltip the tooltips text to be written. Modified by implementation.
     * @param world the world the player looking at the tooltip is in.
     * @param stack the item stack the tooltip is attached to.
     */
    @SuppressWarnings({"unused", "RedundantSuppression"})
    default void write(List<ITextComponent> tooltip, World world, ItemStack stack) {
        write(tooltip);
    }

    /**
     * The shortened write method called by the tooltip handler
     * for tooltip text when it wants to write the tooltip.
     *
     * Prefer this write method as opposed to
     * {@link #write(List, World, ItemStack)}
     * unless the extra parameters are needed.
     *
     * @param tooltip the tooltips text to be written. Modified by implementation.
     */
    void write(List<ITextComponent> tooltip);

    //Static util

    /**
     * Retrieves the localized String under the given
     * {@code key} from the player specific language
     * file. The localized String is fomatted using
     * the given {@code params} before being returned.
     *
     * @param key the key the localized message is
     *            stored under in the language file.
     * @param params the parameters used when formatting
     *               the localized String. Can be
     *               {@code null}
     * @return the localized & formatted String under
     * the given key, or an error message if the
     * key could not be found.
     */
    static String localize(String key, Object... params){
        return I18n.format(key, params);
    }

    /**
     * A utility method to quickly turn any given String into an
     * {@link ITextComponent} which can be used in item/block
     * tooltips.
     *
     * @param str the given String to turn into an ITextComponent
     *            for a tooltip.
     * @return the given String ({@code str}) as a
     * {@link StringTextComponent} cast as an {@link ITextComponent}.
     */
    static ITextComponent toTextComponent(String str) {
        return new StringTextComponent(str);
    }

    /**
     * A utility method that checks if descriptive tooltips
     * are enabled in the configuration settings.
     *
     * @return {@code true} if descriptive item/block tooltips
     * are enabled in the users active configuration settings,
     * {@code false} otherwise.
     */
    static boolean areTooltipsEnabled(){
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
    static List<ITextComponent> addBlankLine(List<ITextComponent> tooltips){
        tooltips.add(new StringTextComponent(""));
        return tooltips;
    }

    /**
     * Used to get an empty tooltip TextComponent that
     * can effectively be used as a new line in item
     * and block tooltips.
     *
     * @return a new {@link StringTextComponent} that can
     * added to an item/block tooltip to act as a blank
     * new line.
     */
    static ITextComponent newBlankLine(){
        return new StringTextComponent("");
    }

    /**
     * Handles transforming a StringTextComponent that contains line feeds
     * ({@code "\n"}) into an array where each item in the array is a line
     * of text from the original input, wrapped in a StringTextComponent.
     * StringTextComponent.
     *
     * TL;DR: transforms StringTextComponenets with new lines into something
     * Minecraft can display correctly.
     *
     * @param input the given StringTextComponent to split into an array of
     *              StringTextComponents, each of which is a line of text.
     * @param lineFormatting the formatting code(s) to format each line with.
     *                       Allows coloring the text.
     * @return the input StringTextComponent as an Array of
     * StringTextComponents where each item is a line of text from the input.
     */
    static ITextComponent[] formatLineFeeds(ITextComponent input, TextFormatting lineFormatting){
        String[] splitInput = input.getString().split("\n");
        StringTextComponent[] output = new StringTextComponent[splitInput.length];

        for(int i = 0; i < splitInput.length; i++){
            output[i] = new StringTextComponent(lineFormatting.toString() + splitInput[i]);
        }

        return output;
    }

    /**
     * A utility method specifically made for items related to plants
     * (e.g. organic ore / produce).
     *
     * This method is used to set the item/block tooltip to contain
     * both the description and statistics on the plant set passed in.
     *
     * @param tooltip the tooltip we're modifying with the new tooltips.
     * @param setProperties the properties of the specific plant set
     *                      we're using to get the plant statistics.
     * @param descriptiveTooltip the localized tooltip for the
     *                           block/item. Line Feeds will be taken
     *                           care of.
     */
    static void addPlantItemOrBlockTooltips(List<ITextComponent> tooltip, IPlantSetProperties setProperties,
                                          ITextComponent descriptiveTooltip){
        //Stats
        new ExpandingTooltip().setShiftForStats(tooltips -> {
            PlantSetUtil.PlantSetTooltipUtil.setPropertiesTooltip(tooltips, setProperties);
            addBlankLine(tooltips);
        }).write(addBlankLine(tooltip));

        //Description
        new ExpandingTooltip().setCtrlForDescription(tooltips -> addBlankLine(tooltips).addAll(
                Arrays.asList(Tooltip.formatLineFeeds(descriptiveTooltip, TextFormatting.DARK_GRAY))
        )).write(tooltip);

        //Spacing
        addBlankLine(tooltip);
    }
}
