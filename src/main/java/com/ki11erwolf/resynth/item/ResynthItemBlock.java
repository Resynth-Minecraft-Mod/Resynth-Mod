/*
 * Copyright 2018-2021 Ki11er_wolf
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

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

/**
 * A special Resynth ItemBlock created to easily register
 * ItemBlocks when registering normals blocks.
 */
public class ResynthItemBlock extends BlockItem {

    /**
     * Flag to prevent queuing an item
     * more than once.
     */
    private boolean isQueued = false;

    /**
     * Creates a new ItemBlock instance.
     *
     * @param blockIn the block this itemBlock is for.
     * @param properties the item block properties.
     */
    public ResynthItemBlock(Block blockIn, Properties properties) {
        super(blockIn, properties);
    }

    /**
     * Adds this ItemBlock instance to ItemRegistrationQueue, which will then
     * be registered to the game from the queue.
     *
     * @return {@code this}.
     */
    @SuppressWarnings("UnusedReturnValue")
    public ResynthItemBlock queueRegistration(){
        if(isQueued)
            throw new IllegalStateException(
                    String.format("ItemBlock: %s already queued for registration.",
                            this.getClass().getCanonicalName())
            );

        ResynthItems.INSTANCE.queueForRegistration(this);
        isQueued = true;

        return this;
    }
}
