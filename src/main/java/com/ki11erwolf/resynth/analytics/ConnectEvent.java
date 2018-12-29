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
package com.ki11erwolf.resynth.analytics;

/**
 * Event fired when this mod starts up.
 * Provides statistics on number of users
 * and most prevalent Resynth version.
 */
public class ConnectEvent extends Event {

    /**
     * @return {@inheritDoc}
     */
    @Override
    public String getAction() {
        return "Connect";
    }

}
