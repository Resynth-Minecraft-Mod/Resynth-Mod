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

import net.minecraft.world.World;

/**
 * A set of utilities to help
 * with general minecraft modding.
 */
public final class MinecraftUtil {

    /**
     * Static util class.
     */
    private MinecraftUtil(){}

    /**
     * Helps make client/server code more distinguished.
     * <p>
     *     This class makes it easier to run client
     *     or server code by providing client/server
     *     abstract methods and a single execute statement.
     * </p>
     */
    public static abstract class SideSensitiveCode{

        /**
         * World object. Used to determine
         * client/server.
         */
        private final World world;

        /**
         * Constructs a new side sensitive
         * object.
         *
         * @param world the world object. Used to
         *              determine if client or
         *              server.
         */
        public SideSensitiveCode(World world){
            this.world = world;
        }

        /**
         * Called if operating on client side.
         */
        public void onClient(){}

        /**
         * Called if operating on server side.
         */
        public void onServer(){}

        /**
         * Executes either {@link #onClient()} or
         * {@link #onServer()} depending on
         * whether we are operating on the
         * client or server side.
         */
        public final void execute(){
            if(!world.isRemote)
                onServer();
            else
                onClient();
        }
    }
}
