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
package com.ki11erwolf.resynth.util;

import com.ki11erwolf.resynth.ResynthMod;
import org.apache.logging.log4j.Logger;

/**
 * An enum list of some of the most common keys on the
 * keyboard as {@link RKey}s.
 *
 * <p/>Each enum has a single variable, {@link CommonRKeys#rKey}
 * which will return the enums RKey pointing to the key/keys the enum
 * says it does. TLDR; CommonKeys enums each hold an RKey for itself,
 * the key.
 */
public enum CommonRKeys {

    //Ctrl
    RIGHT_CONTROL (new RKey("key.keyboard.right.control", 345)),
    LEFT_CONTROL (new RKey("key.keyboard.left.control", 341)),
    CONTROL (RKey.Mode.ANY, RIGHT_CONTROL, LEFT_CONTROL),

    //Alt
    RIGHT_ALT (new RKey("key.keyboard.right.alt", 346)),
    LEFT_ALT (new RKey("key.keyboard.left.alt", 342)),
    ALT (RKey.Mode.ANY, RIGHT_ALT, LEFT_ALT),

    //Shift
    RIGHT_SHIFT (new RKey("key.keyboard.right.shift", 344)),
    LEFT_SHIFT (new RKey("key.keyboard.left.shift", 340)),
    SHIFT (RKey.Mode.ANY, RIGHT_SHIFT, LEFT_SHIFT);

    /* Handles validating all validatable RKeys within the enums. */
    static {
        Logger log = ResynthMod.getNewLogger();
        log.info("Validating all CommonRKeys that qualify...");

        for(CommonRKeys commonRKey : CommonRKeys.values()){
            if(commonRKey.rKey.hasValidatableName() && commonRKey.rKey.isSingle())
                if(!commonRKey.rKey.validate()) log.error("CommonRKeys '" + commonRKey.rKey.getName() + "' failed validation!");
        }
    }

    /**
     * The given RKey this enum object holds.
     */
    public final RKey rKey;

    /**
     * Creates a new RKey holding enum basically.
     *
     * @param key the RKey the enum holds.
     * @throws IllegalStateException if the given RKey from the enum is null.
     */
    CommonRKeys(RKey key){
        if((this.rKey = key) == null)
            throw new IllegalStateException("CommonKeys enums cannot return a null RKey, yet one did.");
    }

    /**
     * Combines two CommonRKeys by creating a new Chained RKey
     * referencing the RKeys rom any two CommonRKeys.
     *
     * @param mode the query {@link RKey.Mode} of the RKey.
     * @param left first CommonRKey
     * @param right second CommonRKey
     */
    CommonRKeys(RKey.Mode mode, CommonRKeys left, CommonRKeys right){
        this(new RKey(mode, left.rKey.getKeyCodes()[0], right.rKey.getKeyCodes()[0]));
    }
}
