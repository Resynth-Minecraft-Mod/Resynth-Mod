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

import javafx.util.Callback;

import java.math.RoundingMode;
import java.text.DecimalFormat;
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
     * Rounds any double value (normal, half up rounding) to a certain number of
     * decimal places. Correctly shortens floating point values with too many
     * trailing decimals to a reasonable length. Particularly useful when displaying
     * values.
     *
     * @param input the given floating point value to round and remove excess decimals.
     * @param decimals the amount of decimals allowed in the output.
     * @return the given {@code input} floating point value, with any excess trailing
     * decimals removed, and rounded to the last decimal.
     */
    public static double roundToNDecimals(double input, int decimals){
        if(decimals < 1) throw new IllegalArgumentException("Cannot have less than 1 decimal point of precision");
        if(decimals > 16) throw new IllegalArgumentException("Cannot have more than 16 decimal points of precision");

        StringBuilder pattern = new StringBuilder("#.");
        for(int i = 0; i < decimals; i++){
            pattern.append("#");
        }

        DecimalFormat df = new DecimalFormat(pattern.toString());
        df.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(df.format(input).replace(",", "."));
    }

    /**
     * Returns a random integer in the range of {@code min} to {@code max}.
     *
     * <p/> e.g. {@code getRandomIntegerInRange(0, 5) => 0, 1, 2, 3, 4 or 5}
     *
     * @param min the minimum number the value can be.
     * @param max the maximum number the value can be.
     * @return the random integer in range.
     */
    public static int getRandomIntegerInRange(int min, int max) {
        return getRandomIntegerInRange(RANDOM_INSTANCE, min, max);
    }

    /**
     * Returns a random integer in the range of {@code min} to {@code max}.
     *
     * <p/>e.g. {@code getRandomIntegerInRange(0, 5) => 0, 1, 2, 3, 4 or 5}
     *
     * @param min the minimum number the value can be.
     * @param max the maximum number the value can be.
     * @param random instance of Minecrafts random object.
     * @return the random integer in range.
     */
    public static int getRandomIntegerInRange(Random random, int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Maximum integer must be greater than minimum integer");
        }

        return random.nextInt((max - min) + 1) + min;
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
    public static boolean chanceOfTrueBoolean(float percentage){
        //Hard code 0% and 100%
        if(percentage >= 100.0) return true;
        if(percentage <= 0.0F) return false;

        //Do chance calculation
        float random = RANDOM_INSTANCE.nextFloat();
        return (percentage / 100) > random;
    }

    public static <R> R probableChance(Callback<Boolean, R> action, double probability) {
        if(probability <= 0.0F) return action.call(false);
        else if (probability >= 1.0F) return action.call(true);
        else return action.call((probability) > RANDOM_INSTANCE.nextFloat());
    }

    public static <R> R percentageChance(Callback<Boolean, R> action, double percentage) {
        if(percentage <= 0.0F) return action.call(false);
        else if (percentage >= 100.0F) return action.call(true);
        else return action.call((percentage / 100) > RANDOM_INSTANCE.nextFloat());
    }

    public static class Chance {

        private double probability;

        public Chance() {
            this(0.0D);
        }

        public Chance(double probability) {
            if(probability <= 0.0D)
                throw new IllegalArgumentException("Chance must be above 0");
            if(probability > 1.0D)
                throw new IllegalArgumentException("Chance must be above 1");

            this.probability = probability;
        }

        public static class Result {

            private final boolean value;

            private final Chance creator;

            private Result(boolean value, Chance creator) {
                this.value = value;
                this.creator = creator;
            }


        }

    }
}
