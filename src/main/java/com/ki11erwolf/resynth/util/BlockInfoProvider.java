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

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Allows implementing block classes to provide
 * information about themselves to the Mineral
 * Hoe when asked to do so.
 */
public interface BlockInfoProvider {

    /**
     * Called when the Mineral Hoe wants to get information
     * from the block to display to the user. This method
     * is called on the server side so localization is
     * not recommended.
     *
     * @param information array that information text is written to.
     * @param world the world the block is in.
     * @param pos the position of the block in the world.
     * @param block the state of the block in the world.
     */
    void appendBlockInformation(List<String> information, World world, BlockPos pos, BlockState block);
}
