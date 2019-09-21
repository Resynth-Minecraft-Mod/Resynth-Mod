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
package com.ki11erwolf.resynth.config.categories;

import com.ki11erwolf.resynth.config.ConfigCategory;
import com.ki11erwolf.resynth.config.DoubleConfigValue;

/**
 * Configuration settings for the Enhancer blocks.
 */
public class EnhancersConfig extends ConfigCategory {

    /**
     * The mineral concentration percentage increase that a Calvinite
     * Enhancer block gives the Mineral Soil block it's placed under.
     */
    private final DoubleConfigValue calviniteMineralConcentrationIncrease = new DoubleConfigValue(
            "calvinite-mineral-concentration-increase",
            "The mineral concentration percentage increase that a Calvinite\nEnhancer block gives the " +
                    "Mineral Soil block it's placed under.",
            10,
            1, 40,
            this
    );

    /**
     * The mineral concentration percentage increase that a Sylvanite
     * Enhancer block gives the Mineral Soil block it's placed under.
     */
    private final DoubleConfigValue sylvaniteMineralConcentrationIncrease = new DoubleConfigValue(
            "sylvanite-mineral-concentration-increase",
            "The mineral concentration percentage increase that a Sylvanite\nEnhancer block gives the " +
                    "Mineral Soil block it's placed under.",
            25,
            2, 50,
            this
    );

    /**
     * Creates a new, unique configuration category
     * grouping.
     */
    public EnhancersConfig() {
        super("enhancers");
    }

    /**
     * @return The mineral concentration percentage increase that a Calvinite
     * Enhancer block gives the Mineral Soil block it's placed under.
     */
    public float getCalviniteMineralConcentrationIncrease(){
        return (float) calviniteMineralConcentrationIncrease.getValue();
    }

    /**
     * @return The mineral concentration percentage increase that a Sylvanite
     * Enhancer block gives the Mineral Soil block it's placed under.
     */
    public float getSylvaniteMineralConcentrationIncrease(){
        return (float) sylvaniteMineralConcentrationIncrease.getValue();
    }
}
