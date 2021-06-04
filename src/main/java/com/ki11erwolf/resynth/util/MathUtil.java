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
package com.ki11erwolf.resynth.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.function.Function;

/**
 * A set of utilities for assistance in
 * mathematics functions.
 */
//TODO: Document
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

    public static double within(double in, double min, double max){
        if(in > max) return max;
        else return Math.max(in, min);
    }

    public static int within(int in, int min, int max){
        if(in > max) return max;
        else return Math.max(in, min);
    }

    public static long within(long in, long min, long max){
        if(in > max) return max;
        else return Math.max(in, min);
    }

    public static class Probability {

        private final double probability;

        // ---- Factory ----

        public static Probability newRandomProbability() {
            return new Probability(0.50D);
        }

        public static Probability newProbability(double probability) {
            return new Probability(probability);
        }

        public static Probability newPercentageProbability(double percentage) {
            return new Probability(percentage / 100);
        }

        public static Probability addProbabilities(Probability a, Probability b) {
            return new Probability(a.getProbability() + b.getProbability());
        }

        public static Probability multiplyProbabilities(Probability a, Probability b) {
            return new Probability(a.getProbability() * b.getProbability());
        }

        // ---- Probability ----

        private Probability(double probability) {
            this.probability = probability;
        }

        public Result randomResult() {
            if(probability <= 0.0D)
                return new Result(false);
            else if (probability >= 1.0D)
                return new Result(true);
            else if (probability == 0.50D)
                return new Result(RANDOM_INSTANCE.nextBoolean());

            return new Result(RANDOM_INSTANCE.nextFloat() < probability);
        }

        public double getProbability() {
            return this.probability;
        }

        public double getProbability(int precision) {
            return roundToNDecimals(this.probability, precision);
        }

        public double getPercentageProbability() {
            return this.probability * 100;
        }

        public double getPercentageProbability(int precision) {
            return getProbability(precision) * 100;
        }

        // ---------------------- Result ----------------------

        public static class Result {

            private final boolean value;

            private Result(boolean value) {
                this.value = value;
            }

            public boolean get() {
                return this.value;
            }

            public boolean isTrue() {
                return this.value;
            }

            public boolean isFalse() {
                return !isTrue();
            }

            public <T> T action(Function<Boolean, T> action) {
                return action.apply(get());
            }

            public <T> T ifTrue(Function<Boolean, T> action) {
                if(isTrue())
                    return action.apply(get());
                else return null;
            }

            public <T> T ifFalse(Function<Boolean, T> action) {
                if(isFalse())
                    return action.apply(get());
                else return null;
            }
        }
    }
}
