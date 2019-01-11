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
package com.ki11erwolf.resynth.versioning;

/**
 * The default chat messages for an out of date mod.
 */
public class DefaultVersionMessage implements IVersionMessage {

    /**
     * The static instance to this class.
     */
    public static final DefaultVersionMessage INSTANCE = new DefaultVersionMessage();

    /**
     * The chat message for an out of date mod.
     *
     * @param versionObject the mods version object.
     * @return the message.
     */
    @Override
    public String getOutOfDateMessage(ModVersionObject versionObject) {
        return
                "-----" + versionObject.getModid() + " version checker-----"
                        + "\n  " + versionObject.getModid() + " is out of date!"
                        + "\n  Your version: " + versionObject.getCurrentVersionAsString()
                        + " | Latest version: " + versionObject.getLatestVersionAsString()
                        + "\n  Visit: " + versionObject.getUpdateURL() + " to get an updated mod jar."
                        + "\n-------------------------------";
    }

    /**
     * The chat message for mod ahead of the versions.json file.
     *
     * @param versionObject the mods version object.
     * @return the message.
     */
    @Override
    public String getDevelopmentMessage(ModVersionObject versionObject) {
        return
                "-----" + versionObject.getModid() + " version checker-----"
                        + "\n  You are running a development version of: " + versionObject.getModid()
                        + "\n-------------------------------";
    }

    /**
     * The chat message when the versions.json of a mod could not be found.
     *
     * @param versionObject the mods version object.
     * @return the message.
     */
    @Override
    public String getFailureMessage(ModVersionObject versionObject) {
        return
                "-----" + versionObject.getModid() + " version checker-----"
                        + "\n  Failed to get latest version of: " + versionObject.getModid()
                        + "\n-------------------------------";
    }
}
