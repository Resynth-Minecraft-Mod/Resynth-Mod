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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ki11erwolf.resynth.ResynthMod;

import java.net.URL;
import java.util.Map;

/**
 * Contains all the version information for a mod.
 */
public class ModVersionObject {

    /**
     * The mods modid.
     */
    private final String modid;

    /**
     * The URL to mods version.json
     */
    private URL url;

    /**
     * True if version checking is disabled server side.
     */
    private boolean isDisabledServerSide;

    /**
     * The mods current version converted to an integer.
     */
    private int iCurrentVersion;

    /**
     * The mods latest released version converted to an integer.
     */
    private int iLatestVersion;

    /**
     * The mods recommended version converted to an integer.
     */
    private int iRecommendedVersion;

    /**
     * The mods current version is string form.
     */
    private String sCurrentVersion;

    /**
     * The mods latest version in string form.
     */
    private String sLatestVersion;

    /**
     * The mods recommended version in string form.
     */
    private String sRecommendedVersion;

    /**
     * The current minecraft version in string form.
     */
    private String minecraftVersion;

    /**
     * The mods update url.
     */
    private String updateURL;

    /**
     * The online version.json as a google gson json object.
     */
    private JsonObject jsonFileContent;

    /**
     * List of released mod versions.
     */
    private JsonArray versions;

    /**
     * The mods version check status.
     */
    private Status status = Status.FAILED;

    /**
     * True if the internal forge version system has been "patched".
     */
    boolean forgeInitialized = false;

    /**
     * The messages to display to the user about out of date versions.
     */
    private IVersionMessage versionMessage;

    /**
     * True if out of date version chat messages should be displayed.
     */
    private boolean enableMessage;

    /**
     * @param modid the mods modid.
     * @param minecraftVersion the current minecraft version.
     * @param url the mods update url.
     * @param currentVersion the mods current version.
     * @param messages the mods out of date chat messages.
     * @param enableMessage true if out of date chat messages are enabled.
     */
    ModVersionObject(final String modid, String minecraftVersion, URL url,
                     String currentVersion, IVersionMessage messages, boolean enableMessage){
        this.modid = modid;
        this.minecraftVersion = minecraftVersion;
        this.sCurrentVersion = currentVersion;
        this.versionMessage = messages;
        this.enableMessage = enableMessage;
    }

    /**
     * Parses the given mods version.json into a usable java object.
     *
     * @param versionJson the mods version.json
     * @return true if the version.json has able to be parsed.
     */
    boolean parse(JsonObject versionJson){
        if(versionJson.has("enable"))
            isDisabledServerSide = !versionJson.get("enable").getAsBoolean();

        if(versionJson.has("homepage"))
            this.updateURL = versionJson.get("homepage").getAsString();

        if(versionJson.has("promos"))
            parseForgeVersionJson(versionJson.getAsJsonObject("promos"));

        if(versionJson.has(minecraftVersion) && versionJson.get(minecraftVersion).isJsonObject()){
            versions = new JsonArray();

            for(Map.Entry<String, JsonElement> set : versionJson.get(minecraftVersion).getAsJsonObject().entrySet()){
                versions.add(set.getKey());
            }
        }

        parseVersions(versionJson);

        if(versionJson.has(modid)){
            parseModidJson(versionJson.getAsJsonObject(modid));
        }

        this.jsonFileContent = versionJson;
        return validate();
    }

    /**
     * @return true if version checking has been disabled server side.
     */
    public boolean isDisabledServerSide() {
        return this.isDisabledServerSide;
    }

    public String getRecommendedVersionAsString() {
        return this.sRecommendedVersion;
    }

    public int getRecommendedVersionAsInteger() {
        return this.iRecommendedVersion;
    }

    public String getModid() {
        return modid;
    }

    public URL getUrl() {
        return url;
    }

    public int getCurrentVersionAsInteger() {
        return iCurrentVersion;
    }

    public int getLatestVersionAsInteger() {
        return iLatestVersion;
    }

    public String getCurrentVersionAsString() {
        return sCurrentVersion;
    }

    public String getLatestVersionAsString() {
        return sLatestVersion;
    }

    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    public String getUpdateURL() {
        return updateURL;
    }

    public JsonObject getJsonFileContent(){
        return jsonFileContent;
    }

    public JsonArray getVersions(){
        return versions;
    }

    public Status getStatus(){
        return status;
    }

    /**
     * Validates this object.
     *
     * @return if this object is valid for further use.
     */
    private boolean validate(){
        if(sLatestVersion == null){
            ResynthMod.getLogger().error("Failed to get latest version for: " + modid + " | url: " + url.getFile());
            return false;
        }

        if(sCurrentVersion == null){
            ResynthMod.getLogger().error("Failed to get current version for: " + modid);
            return false;
        }

        finalizeVariables();
        return jsonFileContent != null;
    }

    /**
     * Initializes some of this classes variables
     * from variables set by the version.json.
     */
    private void finalizeVariables(){
        try{
            iLatestVersion = Integer.parseInt(
                    sLatestVersion
                            .replace(" ", "")
                            .replace(".", "")
                            .replace("[", "")
                            .replace("]", "")
                            .replace("v", "")
                            .replace("V", "")
                            .replace("version", "")
                            .replace("Version", "")
                            .replace("VERSION", "")
                            .replace("-", "")
                            .replace("_", "")
            );
        } catch (NumberFormatException e){
            iLatestVersion = 0;
            ResynthMod.getLogger().warn("Failed to process iLatestVersion | mod: " + modid);
        }

        try{
            iRecommendedVersion = Integer.parseInt(
                    sRecommendedVersion
                            .replace(" ", "")
                            .replace(".", "")
                            .replace("[", "")
                            .replace("]", "")
                            .replace("v", "")
                            .replace("V", "")
                            .replace("version", "")
                            .replace("Version", "")
                            .replace("VERSION", "")
                            .replace("-", "")
                            .replace("_", "")
            );
        } catch (NumberFormatException e){
            iRecommendedVersion = 0;
            ResynthMod.getLogger().warn("Failed to process iRecommendedVersion | mod: " + modid);
        }

        try{
            iCurrentVersion = Integer.parseInt(
                    sCurrentVersion
                            .replace(" ", "")
                            .replace(".", "")
                            .replace("[", "")
                            .replace("]", "")
                            .replace("v", "")
                            .replace("V", "")
                            .replace("version", "")
                            .replace("Version", "")
                            .replace("VERSION", "")
                            .replace("-", "")
                            .replace("_", "")
            );
        } catch (NumberFormatException e){
            iCurrentVersion = 0;
            ResynthMod.getLogger().warn("Failed to process iCurrentVersion | mod: " + modid);
        }

        if(iLatestVersion == 0 && iRecommendedVersion == 0 || iCurrentVersion == 0)
            status = Status.FAILED;

        if(iCurrentVersion < iLatestVersion)
            status = Status.OUT_OF_DATE;

        if(iCurrentVersion == iLatestVersion)
            status = Status.UP_TO_DATE;

        if(iCurrentVersion > iLatestVersion)
            status = Status.DEVELOPMENT;
    }

    /**
     * Parses any forge specification version.json
     *
     * @param promos the promos json object.
     */
    private void parseForgeVersionJson(JsonObject promos){
        if(promos.has("latest"))
            sLatestVersion = promos.get("latest").getAsString();

        if(promos.has("recommended"))
            sRecommendedVersion = promos.get("recommended").getAsString();

        if(promos.has(minecraftVersion + "-latest"))
            sLatestVersion = promos.get(minecraftVersion + "-latest").getAsString();

        if(promos.has(minecraftVersion + "-recommended"))
            sRecommendedVersion = promos.get(minecraftVersion + "-recommended").getAsString();
    }

    /**
     * Parses any free floating versions.
     *
     * @param versionJson the version.json.
     */
    private void parseVersions(JsonObject versionJson){
        if(versionJson.has("latest"))
            if(versionJson.get("latest").isJsonObject()){
                JsonObject latest = versionJson.getAsJsonObject("latest");

                if(latest.has("all"))
                    sLatestVersion = latest.get("all").getAsString();

                if(latest.has(minecraftVersion)){
                    sLatestVersion = latest.get(minecraftVersion).getAsString();
                }
            } else if(versionJson.get("latest").isJsonPrimitive()){
                sLatestVersion = versionJson.get("latest").getAsString();
            }

        if(versionJson.has("recommended"))
            if(versionJson.get("recommended").isJsonObject()){
                JsonObject latest = versionJson.getAsJsonObject("recommended");

                if(latest.has("all"))
                    sLatestVersion = latest.get("all").getAsString();

                if(latest.has(minecraftVersion)){
                    sLatestVersion = latest.get(minecraftVersion).getAsString();
                }
            } else if(versionJson.get("recommended").isJsonPrimitive()){
                sLatestVersion = versionJson.get("recommended").getAsString();
            }
    }

    /**
     * Parses the resynth specification modid version.json
     *
     * @param modidJson the modid json object.
     */
    private void parseModidJson(JsonObject modidJson){
        if(modidJson.has("latest"))
            sLatestVersion = modidJson.get("latest").getAsString();

        if(modidJson.has("recommended"))
            sRecommendedVersion = modidJson.get("recommended").getAsString();

        if(modidJson.has(minecraftVersion)){
            JsonObject versionsJson = modidJson.getAsJsonObject(minecraftVersion);

            if(versionsJson.has("latest"))
                sLatestVersion = versionsJson.get("latest").getAsString();

            if(versionsJson.has("recommended"))
                sRecommendedVersion = versionsJson.get("recommended").getAsString();

            if(versionsJson.has("versions"))
                versions = versionsJson.getAsJsonArray("versions");
        }
    }

    /**
     * @return this object as a human readable string.
     */
    public String toString(){
        return String.format(
                modid + ".versioning {status: %s, minecraft-version: %s, " +
                        "latest-version: %s(%s), recommended-version: %s(%s) current-version: %s(%s)}",
                status.toString(), minecraftVersion, sLatestVersion, iLatestVersion, sRecommendedVersion,
                iRecommendedVersion, sCurrentVersion, iCurrentVersion
        );
    }

    /**
     * @return The messages to display when the mod is out of date.
     */
    public IVersionMessage getVersionMessage() {
        return versionMessage;
    }

    /**
     * @return true if out of date version chat messages should be shown.
     */
    public boolean enableOutOfDateVersionChatMessages() {
        return enableMessage;
    }
}
//So close...