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
package com.ki11erwolf.resynth.versioning;

/**
 * The status of the version check.
 */
public enum Status {

    /**
     * The mod is up to date.
     */
    UP_TO_DATE(0),

    /**
     * The mod is out of date.
     */
    OUT_OF_DATE(1),

    /**
     * The mod instance is a development version.
     */
    DEVELOPMENT(2),

    /**
     * The version check failed. This shouldn't
     * be the case most of the time as the entire
     * version object isn't created in the event
     * of a failure.
     */
    FAILED(3);

    /**
     * The internal status ID.
     */
    public final int id;

    Status(int id){
        this.id = id;
    }
}
