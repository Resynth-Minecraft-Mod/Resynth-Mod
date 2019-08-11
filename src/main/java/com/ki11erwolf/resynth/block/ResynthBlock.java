/*
 * Copyright 2018-2019 Ki11er_wolf
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
package com.ki11erwolf.resynth.block;

import com.ki11erwolf.resynth.ResynthTabs;
import com.ki11erwolf.resynth.item.ResynthItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

/**
 * Base block class for all Resynth blocks.
 *
 * Provides extra utility, registration and a linked ItemBlock.
 *
 * @param <T> the subclass to this class (e.i. the inheriting class).
 */
public class ResynthBlock<T extends ResynthBlock> extends Block {

    /**
     * Flag to prevent queuing a block
     * more than once.
     */
    private boolean isQueued = false;

    /**
     * The ItemBlock instance for this block instance.
     */
    private final ResynthItemBlock itemBlock;

    /**
     * Creates a new Resynth Block with the given name
     * and properties.
     *
     * @param properties the properties that define the block.
     * @param name the name of the block.
     */
    public ResynthBlock(Properties properties, String name){
        this(properties, new Item.Properties().group(ResynthTabs.TAB_RESYNTH), name);
    }

    /**
     * Creates a new Resynth Block with the given name, ItemBlock
     * properties and block properties.
     *
     * @param properties the properties that define the block.
     * @param itemProperties the properties that defines the blocks item.
     * @param name the name of the block.
     */
    @SuppressWarnings("WeakerAccess")
    public ResynthBlock(Properties properties, Item.Properties itemProperties, String name) {
        super(properties);
        setRegistryName(name);

        itemBlock = new ResynthItemBlock(this, itemProperties);
        itemBlock.setRegistryName(name);
    }

    /**
     * Adds this block instance to ItemRegistrationQueue, which will then
     * be registered to the game from the queue. This will also register
     * the ItemBlock instance associated with this block instance.
     *
     * @return {@code this}.
     */
    @SuppressWarnings("WeakerAccess")//Lies
    protected T queueRegistration(){
        if(isQueued)
            throw new IllegalStateException(
                    String.format("Block: %s already queued for registration.",
                            this.getClass().getCanonicalName())
            );

        ResynthBlocks.INSTANCE.queueForRegistration(this);
        isQueued = true;

        itemBlock.queueRegistration();

        //noinspection unchecked //Should NOT be possible.
        return (T)this;
    }

    // *******
    // Utility
    // *******

    /**
     * Turns a given string into a {@link TextComponentString}
     * containing the given string.
     *
     * @param text the given string text.
     * @return a new TextComponentString containing the given string.
     */
    protected static ITextComponent stringToTextComponent(String text){
        return new TextComponentString(text);
    }
}
