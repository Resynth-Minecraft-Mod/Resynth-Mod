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
package com.ki11erwolf.resynth.analytics;

import com.ki11erwolf.resynth.ResynthMod;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * Used to disable and re-enable native
 * Java SSL verification.
 *
 * Native Java SSL verification can prevent some
 * website files being from being read (such as the
 * {@code versions.json} file). This class provides
 * the solution to the problem by providing methods
 * to disable and renable SSL verification.
 */
class SSLHelper {

    /**
     * Private constructor.
     */
    private SSLHelper(){}

    /**
     * Logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

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
    public static void disableSSL() {
        if(!isSSLDisabled){
            LOG.debug("Disabling SSL...");

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
                ResynthMod.getNewLogger().error("Failed to disable SSL", e);
            }
            isSSLDisabled = true;
        }
    }

    /**
     * Re-enables ssl verification. Disabled by above method.
     */
    //To fix by undoing the above fix
    public static void enableSSL(){
        if(defaultSocketFactory != null && defaultHostnameVerifier != null){
            LOG.debug("Enabling SSL...");

            HttpsURLConnection.setDefaultSSLSocketFactory(defaultSocketFactory);
            HttpsURLConnection.setDefaultSSLSocketFactory(defaultSocketFactory);
            isSSLDisabled = false;
        } else LOG.warn(String.format(
                "Call to 'SSLHelper.enableSSL()' failed! Additional Information: " +
                        "[SocketFactory=%s, HostnameVerifier=%s, isSSLDisabled=%s]",
                defaultSocketFactory, defaultHostnameVerifier, isSSLDisabled
        ));
    }
}
