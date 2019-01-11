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
package com.ki11erwolf.resynth.item;

import java.util.ArrayList;

/**
 * Stores instances of Resynth items that should be registered to forge.
 *
 * Note: This registry does NOT hold any seeds or plant produce items.
 */
public class ResynthItemRegistry {

    /**
     * The array of resynth items to be registered.
     */
    private static final ArrayList<ResynthItem> ITEMS = new ArrayList<>();

    /**
     * Adds an item to the list of items to be registered.
     *
     * @param item the item to add.
     */
    protected static void addItem(ResynthItem item){
        ITEMS.add(item);
    }

    /**
     * @return an array of all the resynth items that should be registered.
     */
    protected static ResynthItem[] getItems(){
        return ITEMS.toArray(new ResynthItem[0]);
    }
}
