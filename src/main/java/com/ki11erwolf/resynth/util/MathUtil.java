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

    /**
     * Returns {@code true} randomly with the
     * given chance (0.0F - 100.0F).
     *
     * @param percentage the percentage chance
     *                   of the method returning
     *                   true (0.0F - 100.0F)
     * @return {@code true} or {@code false} randomly
     * with the given percentage chance.
     */
    public static boolean chance(float percentage){
        //Hard code 0% and 100%
        if(percentage >= 100.0)
            return true;
        if(percentage <= 0.0F)
            return false;

        float random = RANDOM_INSTANCE.nextFloat();
        return (percentage / 100) > random;
    }
}
