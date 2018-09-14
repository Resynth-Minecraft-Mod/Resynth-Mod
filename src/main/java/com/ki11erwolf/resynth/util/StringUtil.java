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

/**
 * A set of utilities for string manipulation.
 */
public final class StringUtil {

    /**
     * Static utility class.
     */
    private StringUtil(){}

    /**
     * Converts the String to lowercase and inserts
     * and underscore(_) before each capital letter.
     * <p>e.g.<p>
     * {@code toUnderscoreLowercase("helloWorld") => "hello_world"}
     *
     * @param str the string to convert.
     * @return the converted string.
     */
    public static String toUnderscoreLowercase(String str){
        StringBuilder ret = new StringBuilder();

        for(char c : str.toCharArray()){
            if(!String.valueOf(c).toLowerCase().equals(String.valueOf(c)))
                ret.append("_").append(String.valueOf(c).toLowerCase());
            else
                ret.append(c);
        }

        return ret.toString();
    }

    /**
     * Capitalizes the first letter of a String.
     *
     * @param str the string to capitalize.
     * @return the capitalized String.
     */
    public static String capitalize(String str){
        char[] strChar = str.toCharArray();
        strChar[0] = String.valueOf(strChar[0]).toUpperCase().toCharArray()[0];
        return String.valueOf(strChar);
    }
}
