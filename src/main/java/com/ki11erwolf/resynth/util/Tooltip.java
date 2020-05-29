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
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.List;

/**
 * A utility class that enables creating item & block tooltips
 * as OOP objects that can write to a Minecraft tooltip array.
 * The tooltip class also provides other utilities that aid in
 * the creation and usage of normal Minecraft tooltips.
 */
public interface Tooltip {

    default void write(List<ITextComponent> tooltip, World world, ItemStack stack) {
        write(tooltip);
    }

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

}
