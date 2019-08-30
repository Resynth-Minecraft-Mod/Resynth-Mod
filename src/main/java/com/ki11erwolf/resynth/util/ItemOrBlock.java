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
package com.ki11erwolf.resynth.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

/**
 * Represents either a {@link Block} or an {@link Item}, instead of just one.
 * Also provides a way to check if the Block/Item it represents is a Block
 * or Item.
 */
public class ItemOrBlock {

    /**
     * The Item or Block instance.
     */
    private final Object itemOrBlock;

    /**
     * Creates a new ItemOrBlock instance
     * that represents an Item.
     *
     * @param item the item this instance represents.
     */
    public ItemOrBlock(Item item){
        this.itemOrBlock = item;
    }

    /**
     * Creates a new ItemOrBlock instance
     * that represents a Block.
     *
     * @param block the block this instance represents.
     */
    @SuppressWarnings("unused")
    public ItemOrBlock(Block block){
        this.itemOrBlock = block;
    }

    /**
     * @return {@code true} if this
     * object represents an Item.
     */
    public boolean isItem(){
        return itemOrBlock instanceof Item;
    }

    /**
     * @return the Item this object represents,
     * or {@code null} if this object doesn't
     * represent an item.
     */
    public Item getItem(){
        if(!isItem())
            return null;

        return (Item) itemOrBlock;
    }

    /**
     * @return {@code true} if this
     * object represents a Block.
     */
    public boolean isBlock(){
        return itemOrBlock instanceof Block;
    }

    /**
     * @return the Block this object represents,
     * or {@code null} if this object doesn't
     * represent a Block.
     */
    public Block getBlock(){
        if(!isBlock())
            return null;

        return (Block) itemOrBlock;
    }
}
