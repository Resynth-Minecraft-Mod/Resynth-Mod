/*
 * Copyright 2018 Ki11er_wolf
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

import java.util.Random;

/**
 * A set of utilities for assistance in
 * mathematics functions.
 */
public final class MathUtil {

    /**
     * Static final instance of {@link Random} to
     * prevent unneeded object creation.
     */
    private static final Random RANDOM_INSTANCE = new Random();

    //Static class.
    private MathUtil(){}

    /**
     * Returns a random integer in the range
     * of {@code min} to {@code max}.
     * <p>
     * e.g. {@code getRandomIntegerInRange(0, 5) => 0, 1, 2, 3, 4 or 5}
     *
     * @param min the minimum number the value can be.
     * @param max the maximum number the value can be.
     * @return the random integer in range.
     */
    public static int getRandomIntegerInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Maximum integer must be greater than minimum integer");
        }

        return RANDOM_INSTANCE.nextInt((max - min) + 1) + min;
    }
}
