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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Helps build a new mod version manager.
 */
public class VersionManagerBuilder {

    /**
     * The mods modid.
     */
    final String modid;

    /**
     * True if the version manager is enabled.
     */
    boolean enabled = false;

    /**
     * True if version messages are disabled.
     */
    boolean enableConsoleMessage = false;

    /**
     * True if the forge Mod annotations updateURL
     * should be used. If true, this url is used first.
     */
    boolean useForgeUpdateURL = false;

    /**
     * Backup mod update url.
     */
    URL updateURL = null;

    /**
     * List of urls to check for a version.json file.
     */
    List<String> givenUpdateJsonURLS = new ArrayList<>(2);

    /**
     * Constructs a new mod version manager builder.
     *
     * @param modid the mods modid.
     */
    public VersionManagerBuilder(final String modid){
        this.modid = modid;
    }

    /**
     * True if the updateUrl value of the forge Mod annotation
     * should be used. If true, that url will be check first.
     *
     * @param useForge true if the forge updateUrl should be used.
     * @deprecated use {@link #addVersionJsonFileURL(String)} as it provides better URL checking.
     */
    @Deprecated
    public VersionManagerBuilder useForgeModidAnnotationUrl(boolean useForge){
        this.useForgeUpdateURL = useForge;
        return this;
    }

    /**
     * Enables/Disables this version manager.
     *
     * @param enabled true if this version manager should be enabled.
     * @return this.
     */
    public VersionManagerBuilder setEnabled(boolean enabled){
        this.enabled = enabled;
        return this;
    }

    /**
     * @param enable true if out of date messages should be displayed.
     * @return this.
     */
    public VersionManagerBuilder setOutOfDateConsoleWarningEnabled(boolean enable){
        this.enableConsoleMessage = enable;
        return this;
    }

    /**
     * Adds a URL to check for a version.json
     *
     * @param versionJsonFile the version.json file URL
     * @return this.
     */
    public VersionManagerBuilder addVersionJsonFileURL(String versionJsonFile){
        this.givenUpdateJsonURLS.add(versionJsonFile);
        return this;
    }

    /**
     * Sets the mods update url.
     *
     * @param updateURL the mods update url.
     * @return this.
     */
    public VersionManagerBuilder setUpdateURL(URL updateURL){
        this.updateURL = updateURL;
        return this;
    }

    /**
     * Removes a version.json file URL from the list
     * of added URLs.
     *
     * @param versionJsonFile the version.json file URL to remove.
     * @return this.
     */
    public boolean removeVersionJsonFileURL(String versionJsonFile){
        return givenUpdateJsonURLS.remove(versionJsonFile);
    }
}
