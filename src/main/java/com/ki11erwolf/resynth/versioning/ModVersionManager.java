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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ki11erwolf.resynth.ResynthMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.gui.NotificationModUpdateScreen;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.versioning.ComparableVersion;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Manages all mods versioning.
 */
@Mod.EventBusSubscriber
public class ModVersionManager {

    /**
     * List of valid mod version objects and their modid.
     */
    private static final Map<String, ModVersionObject> modVersionObjects = new HashMap<>();

    /**
     * This classes logger.
     */
    private static Logger LOG;

    /**
     * The modid for this version manager.
     */
    private final String modid;

    /**
     * If this version manager is enabled.
     */
    private boolean enabled;

    /**
     * If this version manager should send out of date messages.
     */
    private boolean enableConsoleMessage;

    /**
     * True if the url given to the Mod annotation
     * should be used first.
     */
    private boolean useForgeUpdateURL;

    /**
     * the mods update url.
     */
    private URL updateURL;

    /**
     * The list of all given version.json urls.
     */
    private List<String> givenVersionJsonURLS;

    /**
     * The messages to use when a mod is out of date.
     */
    private IVersionMessage updateMessages;

    /**
     * Constructs a new mod version manager with the default chat
     * messages.
     *
     * @param builder the mod version manager build.
     */
    public ModVersionManager(VersionManagerBuilder builder){
        this(builder, DefaultVersionMessage.INSTANCE);
    }

    /**
     * Constructs a new mod version manager with the default chat
     * messages.
     *
     * @param builder the mod version manager build.
     * @param updateMessages the messages to use when a mod is out of date.
     */
    public ModVersionManager(VersionManagerBuilder builder, IVersionMessage updateMessages){
        LOG = ResynthMod.getLogger();
        this.modid = builder.modid;
        this.enabled = builder.enabled;
        this.enableConsoleMessage = builder.enableConsoleMessage;
        this.updateURL = builder.updateURL;
        this.useForgeUpdateURL = builder.useForgeUpdateURL;
        this.givenVersionJsonURLS = builder.givenUpdateJsonURLS;
        this.updateMessages = updateMessages;
    }

    /**
     * Should be called in the preInit phase.
     *
     * Checks, initializes and updates the mods version status.
     *
     * @return the mods version object.
     */
    public ModVersionObject preInit(){
        disableSSLVerification();
        if(!validate()){
            LOG.error("Version checker for mod: " + modid + " failed validation. Skipping...");
            return null;
        }

        LOG.info("Processing: " + givenVersionJsonURLS.size() + " urls for: " + modid);
        JsonObject versionJson = null;

        for(String url : givenVersionJsonURLS){
            try{
                URL _url = new URL(url);
                if(_url.getFile() != null) {
                    versionJson = getJsonFromURL(url);
                }

                LOG.info("Attempting url: " + url + " for: " + modid + "...");

                ModVersionObject attemptVersionObject = new ModVersionObject(
                        modid, Minecraft.getMinecraft().getVersion(),
                        _url, getContainerForMod(modid).getVersion(),
                        updateMessages, enableConsoleMessage
                );

                if(!attemptVersionObject.parse(versionJson)){
                    LOG.error("Invalid version json for mod: " + modid + "");
                }

                if(attemptVersionObject.isDisabledServerSide()){
                    LOG.info("Version checking is disabled server side for: "
                            + modid + " in url" + url + ". Skipping completely...");
                    return null;
                }

                if(updateURL == null)
                    updateURL = new URL(attemptVersionObject.getUpdateURL());

                if(attemptVersionObject != null)
                    LOG.info("Mod version status: " + attemptVersionObject.toString());

                modVersionObjects.put(modid, attemptVersionObject);
                enableSSLVerification();
                return attemptVersionObject;
            } catch (Exception e){
                LOG.warn("Could not get version.json content from url: " + url + " | modid: " + modid, e);
            }
        }

        enableSSLVerification();

        if(versionJson == null){
            LOG.error("No more version.json urls remaining for mod: " + modid + ". Skipping...");
        }

        return null;
    }

    /**
     * Validates the mods version.json
     *
     * @return true if the mods version.json is acceptable.
     */
    private boolean validate(){
        if(!enabled) {
            LOG.info("Version checker for mod: " + modid + " is set to disabled. Skipping...");
            return false;
        }

        if(!Loader.isModLoaded(modid)) {
            LOG.error("Invalid modid: " + modid + ". Skipping version checker initialization for this mod...");
            return false;
        }

        if(useForgeUpdateURL){
            URL updateUrl = getContainerForMod(modid).getUpdateUrl();
            if(updateUrl == null){
                LOG.error("Version checker for mod: " + modid + " is set to use forge update" +
                        " url for mod but is is null or empty. Skipping this url...");
            } else {
                givenVersionJsonURLS.add(0, updateUrl.toString());
            }
        }

        if(givenVersionJsonURLS.size() == 0){
            LOG.error("No valid version.json urls available for mod: " + modid + ". Skipping...");
            return false;
        }

        return true;
    }

    /**
     * Reads a file from a URL.
     *
     * @param url the url
     * @return the file content
     * @throws Exception if the file cannot be read/found.
     */
    private static String readUrl(URL url) throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(url.openStream()))) {
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            return buffer.toString();
        }
    }

    /**
     * Gets a json object from a given url.
     *
     * @param url the url
     * @return the json object given by the url.
     * @throws Exception if the json object cannot be obtained/parsed/found.
     */
    private static JsonObject getJsonFromURL(String url) throws Exception {
        Gson gson = new Gson();
        return gson.fromJson(readUrl(new URL(url)), JsonObject.class);
    }

    /**
     * Gets the forge mod container for the given mod.
     *
     * @param modid the mods modid.
     * @return the mods mod container. Null if it can't be found.
     */
    private static ModContainer getContainerForMod(String modid){
        for(ModContainer container : Loader.instance().getActiveModList()){
            if(container.getModId().equals(modid)){
                return  container;
            }
        }

        for(ModContainer container : Loader.instance().getActiveModList()){
            if(container.getModId().toLowerCase().equals(modid.toLowerCase())){
                return  container;
            }
        }

        return null;
    }

    /**
     * Changes the internal forge update state of a mod.
     *
     * @param modid the mods modid.
     * @param versionObject the mods version object.
     * @param updateURL the mods update url.
     * @param mainMenu the main menu screen.
     */
    //This actually works...
    //Suck it forge private fields.
    @SuppressWarnings({"unchecked", "RedundantStringOperation"})
    private static void updateForgeModVersionState(String modid, ModVersionObject versionObject,
                                                   String updateURL, GuiMainMenu mainMenu){
        ModContainer modContainer = getContainerForMod(modid);

        if(modContainer == null){
            LOG.error("Cannot update " + modid + " mod container as it is null...");
            return;
        }

        ForgeVersion.Status forgeStatus = ForgeVersion.Status.PENDING;

        if(versionObject.getStatus() == Status.OUT_OF_DATE){
            forgeStatus = ForgeVersion.Status.OUTDATED;
        } else if (versionObject.getStatus() == Status.UP_TO_DATE){
            forgeStatus = ForgeVersion.Status.UP_TO_DATE;
        } else if (versionObject.getStatus() == Status.DEVELOPMENT){
            forgeStatus = ForgeVersion.Status.AHEAD;
        } else if(versionObject.getStatus() == Status.FAILED){
            forgeStatus = ForgeVersion.Status.FAILED;
        }

        ComparableVersion forgeComparableVersion;
        if(versionObject.getLatestVersionAsString() != null)
            forgeComparableVersion = new ComparableVersion(versionObject.getRecommendedVersionAsString());
        else
            forgeComparableVersion = new ComparableVersion(versionObject.getLatestVersionAsString());

        Map<ComparableVersion, String> forgeChanges = new LinkedHashMap<>();

        if(versionObject.getVersions() != null){
            for(JsonElement version : versionObject.getVersions()){
                forgeChanges.put(new ComparableVersion(version.getAsString()), "");
            }
        } else {
            forgeChanges.put(new ComparableVersion(
                    versionObject.getLatestVersionAsString()),
                    "");

            forgeChanges.put(new ComparableVersion(
                    versionObject.getRecommendedVersionAsString()),
                    "");

            forgeChanges.put(new ComparableVersion(
                    versionObject.getCurrentVersionAsString()),
                    "");
        }

        ForgeVersion.CheckResult newCheckResult;
        Constructor<ForgeVersion.CheckResult> constructor= (Constructor<ForgeVersion.CheckResult>)
                ForgeVersion.CheckResult.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);

        if(mainMenu == null){
            LOG.error("Failed to get main menu gui. Skipping...");
            return;
        }

        try {
            newCheckResult = constructor.newInstance(
                    forgeStatus, forgeComparableVersion, forgeChanges, updateURL.toString()
            );

            Field f = ForgeVersion.class.getDeclaredField("results");
            f.setAccessible(true);
            Map<ModContainer, ForgeVersion.CheckResult> iWantThis
                    = (Map<ModContainer, ForgeVersion.CheckResult>) f.get("results");
            iWantThis.put(modContainer, newCheckResult);
            f.set(null, iWantThis);

            Field f2 = mainMenu.getClass().getDeclaredField("modUpdateNotification");
            f2.setAccessible(true);
            NotificationModUpdateScreen iWantThisToo
                    = (NotificationModUpdateScreen) f2.get(mainMenu);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            LOG.error("Failed to update forge version status. Skipping...", e);
        }
    }

    /**
     * Called when the main menu is first initialized.
     *
     * This updates the mods internal forge version state.
     *
     * @param event Gui screen initialized event.
     */
    @SubscribeEvent
    public static void onMainMenuInitialized(GuiScreenEvent event){
        if(event.getGui() instanceof GuiMainMenu){
            for(Map.Entry<String, ModVersionObject> versionObject : modVersionObjects.entrySet()) {
                if(!versionObject.getValue().forgeInitialized)
                updateForgeModVersionState(versionObject.getKey(), versionObject.getValue(),
                        versionObject.getValue().getUpdateURL(), (GuiMainMenu) event.getGui());
                versionObject.getValue().forgeInitialized = true;
            }
        }
    }

    //************************
    // SSL Verification Patch
    //************************

    /**
     * True if SSL has been disabled.
     */
    private static boolean isSSLDisabled = false;

    /**
     * The java default SSL socket factory.
     */
    private static SSLSocketFactory defaultSocketFactory;

    /**
     * The java default HostnameVerifier
     */
    private static HostnameVerifier defaultHostnameVerifier;

    /**
     * Disables native java ssl verification.
     *
     * This prevents issues with incorrectly signed website files.
     */
    //https://stackoverflow.com/questions/19540289/how-to-fix-the-java-security-cert-certificateexception-no-subject-alternative
    //MODIFIED
    public static void disableSSLVerification() {
        if(!isSSLDisabled){
            try{
                defaultSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
                defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();

                //Create a trust manager that does not validate certificate chains
                TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }};

                //Install the all-trusting trust manager
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                // Create all-trusting host name verifier
                HostnameVerifier allHostsValid = (hostname, session) -> true;

                //Install the all-trusting host verifier
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                LOG.error("Failed to disable SSL", e);
            }
            isSSLDisabled = true;
        }
    }

    /**
     * Re-enables ssl verification. Disabled by above method.
     */
    //To fix the above fix
    public static void enableSSLVerification(){
        if(defaultSocketFactory != null && defaultHostnameVerifier != null){
            HttpsURLConnection.setDefaultSSLSocketFactory(defaultSocketFactory);
            HttpsURLConnection.setDefaultSSLSocketFactory(defaultSocketFactory);
            isSSLDisabled = false;
        }
    }

    /**
     * Called when a player join a world.
     *
     * This is used to display the out of date chat message.
     *
     * @param event the player join event.
     */
    @SubscribeEvent
    public static void onPlayerJoined(EntityJoinWorldEvent event){
        if(event.getEntity() instanceof EntityPlayerMP){
            LOG.info("player joined");
            for(ModVersionObject object : modVersionObjects.values()){
                LOG.info("Version obj: " + object.getModid());
                if(object != null && object.getVersionMessage() != null
                && object.enableOutOfDateVersionChatMessages()){
                    EntityPlayerMP player = (EntityPlayerMP) event.getEntity();

                    if(object.getStatus() == Status.OUT_OF_DATE){
                        player.sendMessage(new TextComponentString(
                                object.getVersionMessage().getOutOfDateMessage(object)
                        ));
                    }

                    if(object.getStatus() == Status.DEVELOPMENT){
                        player.sendMessage(new TextComponentString(
                                object.getVersionMessage().getDevelopmentMessage(object)
                        ));
                    }

                    if(object.getStatus() == Status.FAILED){
                        player.sendMessage(new TextComponentString(
                                object.getVersionMessage().getFailureMessage(object)
                        ));
                    }
                }
            }
        }
    }
}
