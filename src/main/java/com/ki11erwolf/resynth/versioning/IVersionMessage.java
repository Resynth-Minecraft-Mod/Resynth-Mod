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
 * Interface for chat messages for out of date mods.
 */
public interface IVersionMessage {

    /**
     * @param versionObject The mods version object.
     * @return the message for the mod if it is out of date.
     */
    String getOutOfDateMessage(ModVersionObject versionObject);

    /**
     * @param versionObject The mods version object.
     * @return the message for the mod if it is ahead of the versions.json file.
     */
    String getDevelopmentMessage(ModVersionObject versionObject);

    /**
     * @param versionObject The mods version object.
     * @return the message for the mod if its version.json could not be found.
     */
    String getFailureMessage(ModVersionObject versionObject);
}
