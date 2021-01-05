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

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A ReferenceKey - RKey for short, references one or more specific
 * keys on the keyboard. The RKey object instance can then be queryed
 * to check if the key/keys referenced are pressed or not. Sadly
 * Listener Callbacks are not a part of this class.
 *
 * <p/>RKeys operate in one of two different {@link Mode modes},
 * either the default IF_ANY: checking if any referenced key is pressed,
 * and IF_ALL: checking if all referenced keys are pressed.
 *
 * <p/>Each key is mapped to a specific positive integer which is
 * what is passed to the RKey constructor to define key refences.
 *
 * The list of what key is mapped to what integer number was taken
 * from Minecrafts {@link net.minecraft.client.util.InputMappings}
 * classes, and copied to a comment at the end of this class file.
 *
 * <p/>Use {@link #query()} to query the pressed status of referenced
 * key/keys in the RKey object. With Singled RKeys the {@code query()}
 * method will then either return the boolean {@code true} if the key
 * is pressed, or {@code false} if the key is not pressed.
 *
 * <p/>Chained RKeys referencing more than one key are more complicated.
 * If they are in the Mode {@link Mode#ANY}, calling {@code query()}
 * will return {@code true} if any referenced key is pressed and {@code
 * false} otherwise. If they are in the Mode {@link Mode#ALL},
 * calling {@code query()}  will return {@code false} unless every
 * referenced key is pressed.
 *
 * <p/>This class was written after the Minecraft code Resynth used
 * to check key presses was changed and obfuscated in 1.16. The
 * difficulty caused by that left me wanting a better system,
 * specifically one that won't change and is readable & understandable.
 * This class, sadly, still relys on the same old Minecraft code under
 * the hood, improving on usability and consistancy, but still prone
 * to always changing always breaking Minecraft source code.
 *
 * @see CommonRKeys CommonRKeys for a list of the most common keyboard
 * keys, each with their own already initialized RKey ready to use.
 */
public class RKey {

    /**
     * The default mode for all chained RKeys when not explicity set.
     */
    public static final Mode DEFAULT_MODE = Mode.ANY;

    /**
     * Enum holding modes of operation for chained RKeys.
     */
    public enum Mode {
        /**
         * Tells the RKey to return {@code true} to querys
         * if any one key reference is pressed.
         */
        ANY,

        /**
         * Tells the RKey to return {@code false} to querys
         * unless all key references are pressed.
         */
        ALL
    }

    // Begin RKey class

    /**
     * The string prefix, all RKey names are expected to begin with
     * in order to be validatable.
     */
    private static final String VALIDATABLE_NAME_PREFIX = "key.keyboard.";

    /**
     * References to every RKeys RKeyInternal object, which holds data
     * and functions regarding integration with Minecraft and query logic.
     */
    private final RKeyInternal rKeyInternal = new RKeyInternal();

    /**
     * The name given to the RKey object.
     *
     * <p/>It may be null in cases wheres it's not needed,
     * it may also be an identifying name or English display name,
     * or it may be used in validation.
     */
    @Nullable
    private final String name;

    /**
     * The one or more key codes that uniquely reference
     * the key/keys this RKey will look for presses from
     * when queried.
     */
    private final int[] keyCodes;

    /**
     * The {@link Mode} of query operation this RKey
     * will use if it is {@link #isChained()} - meaning
     * it has 2 or more key references.
     */
    private final Mode mode;

    //Constructors

    /**
     * Constructs a new blank key reference,
     * referencing one or more keys by their
     * key code. The Reference Key will have
     * no name and will have the {@link #DEFAULT_MODE}
     * {@link Mode#ANY}.
     *
     * @param keyCodes one or more key codes referencing
     *                 keys on the keyboard.
     */
    public RKey(int... keyCodes){
        this(null, null, keyCodes);
    }

    /**
     * Constructs a new key reference,
     * referencing one or more keys by their
     * key code. The Reference Key will have
     * the name specified and will have the {@link #DEFAULT_MODE}
     * {@link Mode#ANY}.
     *
     * @param name the name given to the key reference.
     *             Can be used to identify the key code
     *             or for validation purposes.
     * @param keyCodes one or more key codes referencing
     *                 keys on the keyboard.
     */
    public RKey(@Nullable String name, int... keyCodes){
        this(name, null, keyCodes);
    }

    /**
     * Constructs a new blank key reference,
     * referencing one or more keys by their
     * key code. The Reference Key will have
     * no name and will have the mode specified.
     *
     * @param mode the mode used for RKeys with multiple key codes.
     * @param keyCodes one or more key codes referencing
     *                 keys on the keyboard.
     */
    public RKey(Mode mode, int... keyCodes){
        this(null, mode, keyCodes);
    }

    /**
     * Constructs a new key reference,
     * referencing one or more keys by their
     * key code. The Reference Key will have
     * the name specified and the mode specified.
     *
     * @param name the name given to the key reference.
     *             Can be used to identify the key code
     *             or for validation purposes.
     * @param mode the mode used for RKeys with multiple key codes.
     * @param keyCodes one or more key codes referencing
     *                 keys on the keyboard.
     */
    public RKey(@Nullable String name, Mode mode, int... keyCodes) {
        if(keyCodes == null || keyCodes.length < 1)
            throw new NullPointerException("Cannot construct RKey with null or 0 length keyCodes array.");

        this.mode = (mode == null) ? DEFAULT_MODE : mode;
        this.keyCodes = keyCodes;
        this.name = name;
    }

    // Functions

    /**
     * Gets the unchanging name given to this ReferenceKey instance
     * at creation time.
     *
     * <p/>A ReferenceKey name can be: either {@code null}, a plain-text
     * display name to identify this RKey instance, or a specific uniquely
     * identifying string - which is used by Minecraft - that can be used
     * to validate the RKey & check if it's correctly created. The first
     * two options - a {@code null} name or a display name - have no special
     * meaning. However, the final option is special and offers some
     * functionality.
     *
     * <p/>The final option (a Minecraft specific uniquely identifying string)
     * is a specific String, beginning with {@code "key.keyboard."} ({@link
     * #VALIDATABLE_NAME_PREFIX}), that is specified by Minecraft. It is used
     * by Minecraft to identify & name the keyboard key matching a specific
     * integer keycode. The specific keycodes & matching strings can be
     * found at the bottom of this class, or in {@link InputMappings}.
     * When set, an RKey object can be validated ({@link #validate()})
     * to ensure the RKey is created correctly.
     *
     * @return the (possibly {@code null}) unchanging name given to
     * this ReferenceKey at object creation, used to identify, or
     * identify & validate this RKey.
     */
    @Nullable
    public String getName(){
        return name;
    }

    /**
     * Ensures this single RKey ({@link #isSingle()}) is
     * correctly created - that is - the name and key
     * match eachother (according to Minecraft/Forge).
     * Only for RKey objects that reference one single
     * keyboard key with one single keycode (singled),
     * and that have a {@link #hasValidatableName()
     * validatable name}.
     *
     * @return {@code true} if validation was possible
     * and successful, ensuring the creation of the RKey
     * object was correct, with all input data correct,
     * according to Minecraft/Forge.
     */
    public boolean validate(){
        return rKeyInternal.validate();
    }

    /**
     * The single keyboard key(or keys) pressed check method.
     * Used to check if the referenced key is pressed down,
     * or if the multiple referenced keys can be considered
     * pressed.
     *
     * <p/>For chained ({@link #isChained()}) RKeys, the
     * result is determined by the {@link Mode} of the
     * RKey object instance.
     *
     * @return {@code true} if the keyboard key(s)
     * referenced/represented by this RKey are pressed
     * down, {@code false} otherwise.
     */
    public boolean query(){
        return rKeyInternal.query();
    }

    /**
     * Used to get the keycode (singluar, if {@link #isSingle()}),
     * or keycodes (multiple, if {@link #isChained()}), that this
     * ReferenceKey uses to uniquely identify and reference keys
     * on the keyboard.
     *
     * @return an array of Integers, given at object creation,
     * containing the keycode(s) used to reference keyboard keys.
     * Contains a single element if referencing only one key
     * ({@link #isSingle()}).
     */
    public int[] getKeyCodes(){
        return keyCodes;
    }

    /**
     * @return the mode used to determine if this ReferenceKey
     * is pressed, when referencing multiple keys.
     */
    public Mode getMode(){
        return mode;
    }

    /**
     * Use to check if this ReferenceKey is only
     * referencing one keyboard key. Opposite of
     * {@link #isChained()}.
     *
     * @return {@code true} if and only if this ReferenceKey
     * references a single keyboard key, {@code false} otherwise.
     */
    public boolean isSingle(){
        return this.keyCodes.length == 1;
    }

    /**
     * Use to check if this ReferenceKey references two
     * or more keyboard keys. Opposite of {@link #isSingle()}.
     *
     * @return {@code true} if and only if this ReferenceKey
     * references two or more (multiple) keyboard keys,
     * {@code false} otherwise.
     */
    public boolean isChained(){
        return this.keyCodes.length > 1;
    }

    /**
     * Used to check the unique identifying name of
     * the keycode(s), representing keyboard keys, according
     * to Minecraft. Can help with debugging, and with making
     * sure the correct keycode is used.
     *
     * @return an array containing the unique identifying name
     * of the keycode/keycodes. Names in array are ordered
     * in the same order as the keycodes.
     */
    public String[] getKeyNamesFromKeyCodes() {
        return rKeyInternal.getValidatableKeyCodes();
    }

    /**
     * Checks if a name (any type) was given to
     * this Reference Key.
     *
     * @return {@code true} if and only if this
     * ReferenceKey was given and has a name
     * that is <b>not {@code null}.</b>
     *
     * @see #getName()
     */
    public boolean hasName(){
        return name != null;
    }

    /**
     * Checks if this ReferenceKey both, has a name ({@link #hasName()}),
     * and that the name, if any, can be used in validation. Validatable
     * names begin with {@link #VALIDATABLE_NAME_PREFIX}. Only Single
     * ({@link #isSingle()}) ReferenceKeys can be valildated.
     *
     *
     * @see #validate()
     * @return {@code true} if and only if this ReferenceKey has a name that
     * is non-{@code null}, the name given begins with
     * {@link #VALIDATABLE_NAME_PREFIX}, and the RKey is {@link #isSingle()}.
     * Will return {@code false} otherwise.
     *
     * @see #getName()
     */
    public boolean hasValidatableName(){
        return name != null && name.startsWith(VALIDATABLE_NAME_PREFIX);
    }

    /**
     * Used to check if this ReferenceKey, based upon
     * its properties, can and should be validated.
     *
     * @see #validate()
     * @return {@code true} if and only if, the ReferenceKey has a
     * name that can be used for validation ({@link #hasValidatableName()})
     * and is a Single ReferenceKey ({@link #isSingle()}).
     */
    public boolean canBeValidated(){
        return hasValidatableName() && isSingle();
    }

    // End RKey class (kindof)

    /**
     * A separate part of the RKey class split from the
     * class into its own, that is specifically for handling
     * anything and everything Minecraft InputMappings
     * related, as well as the logic behind how RKeys
     * work.
     */
    private class RKeyInternal {

        /**
         * The Minecraft {@link InputMappings.Input} object
         * that represents the same keyboard key as this
         * ReferenceKey object, if any.
         */
        @Nullable
        private InputMappings.Input asInput;

        /**
         * Stores the flag, stating if this
         * RKey was validated and validation
         * passed.
         */
        private boolean validated = false;

        /**
         * Internal validation method.
         * @see RKey#validate()
         */
        boolean validate(){
            if(isChained()) return false;
            if(asInput != null) return validated;

            InputMappings.Input inputKeyCode = asInput = getInputFromCode();

            if(!hasValidatableName())
                return false;

            InputMappings.Input inputName = getInputFromName();

            if(inputName != null){
                return (validated = (inputName.getKeyCode() == inputKeyCode.getKeyCode()));
            }

            return (validated = false);
        }

        /**
         * @return an array containing the unique identifying name
         * of the keycode/keycodes. Names in array are ordered
         * in the same order as the keycodes.
         */
        public String[] getValidatableKeyCodes() {
            List<String> codes = new ArrayList<>();

            for(int keyCode : keyCodes){
                codes.add(getInputFromCode(keyCode).getTranslationKey());
            }

            return codes.toArray(new String[0]);
        }

        /**
         * Attempts to query the status of this RKey.
         * Determined by Minecraft/Forge.
         */
        boolean query(){
            if(!RKey.this.isChained()){
                validate();

                if(hasValidatableName() && asInput != null && !validated){
                    throw new IllegalStateException(
                        "RKey '" + name + "' failed validation! "
                        + "RKey with code is: '" + asInput.getTranslationKey() + "' instead!"
                    );
                } else return queryGiven();
            } else return queryGiven();
        }

        /**
         * Attempts to query the status of the referenced key this
         * Single RKey references. Determined by Minecraft/Forge.
         */
        private boolean queryGiven(){
            if(RKey.this.isSingle()) return queryKeyCode(RKey.this.keyCodes[0]);
            else if (RKey.this.isChained()) return queryChained();
            else return false;//if possible?
        }

        /**
         * Attempts to query the status of the referenced keys this
         * Chained RKey references. Determined by Minecraft/Forge.
         */
        private boolean queryChained(){
            boolean anyMode = (RKey.this.mode == Mode.ANY);

            for(int keyCode : RKey.this.keyCodes){
                boolean down = queryKeyCode(keyCode);

                if(down && anyMode) return true;
                if(!down && !anyMode) return false;
            }

            return !anyMode;
        }

        /**
         * @return {@code true} if Minecraft has determined the keyboard
         * key, represented/referenced by this RKey, is pressed.
         */
        private boolean queryKeyCode(int code){
            return InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), code);
        }

        /**
         * @return the {@link InputMappings.Input} object created
         * by Minecraft/Forge that represents the same key this
         * RKey represents. Obtained using the RKey keycode.
         */
        private InputMappings.Input getInputFromCode(){
            return getInputFromCode(RKey.this.keyCodes[0]);
        }

        /**
         * @return the {@link InputMappings.Input} object created
         * by Minecraft/Forge that represents the same key this
         * RKey represents. Obtained using the keycode given.
         */
        private InputMappings.Input getInputFromCode(int code){
            return InputMappings.Type.KEYSYM.getOrMakeInput(code);
        }

        /**
         * @return the {@link InputMappings.Input} object created
         * by Minecraft/Forge that represents the same key this
         * RKey represents. Obtained using the RKey name.
         */
        private InputMappings.Input getInputFromName(){
            if(RKey.this.name == null) return null;
            else try {
                return InputMappings.getInputByName(RKey.this.name);
            } catch (Exception e) {
                return null;
            }
        }

    }

    // End of RKey
}

/*
The list that defines what key code a key is mapped to. Includes a UID for each key as well.
Copied (for fear of loss or change) from Minecrafts 1.16.2 client.util.InputMappings class.

As long as this system relys on Minecraft/InputMappings under the hood, the list RKey uses and
the list InputMappings uses must match exactly. Here's hoping Mojang leaves it alone.

"key.keyboard.unknown", -1
"key.keyboard.0", 48
"key.keyboard.1", 49
"key.keyboard.2", 50
"key.keyboard.3", 51
"key.keyboard.4", 52
"key.keyboard.5", 53
"key.keyboard.6", 54
"key.keyboard.7", 55
"key.keyboard.8", 56
"key.keyboard.9", 57
"key.keyboard.a", 65
"key.keyboard.b", 66
"key.keyboard.c", 67
"key.keyboard.d", 68
"key.keyboard.e", 69
"key.keyboard.f", 70
"key.keyboard.g", 71
"key.keyboard.h", 72
"key.keyboard.i", 73
"key.keyboard.j", 74
"key.keyboard.k", 75
"key.keyboard.l", 76
"key.keyboard.m", 77
"key.keyboard.n", 78
"key.keyboard.o", 79
"key.keyboard.p", 80
"key.keyboard.q", 81
"key.keyboard.r", 82
"key.keyboard.s", 83
"key.keyboard.t", 84
"key.keyboard.u", 85
"key.keyboard.v", 86
"key.keyboard.w", 87
"key.keyboard.x", 88
"key.keyboard.y", 89
"key.keyboard.z", 90
"key.keyboard.f1", 290
"key.keyboard.f2", 291
"key.keyboard.f3", 292
"key.keyboard.f4", 293
"key.keyboard.f5", 294
"key.keyboard.f6", 295
"key.keyboard.f7", 296
"key.keyboard.f8", 297
"key.keyboard.f9", 298
"key.keyboard.f10", 299
"key.keyboard.f11", 300
"key.keyboard.f12", 301
"key.keyboard.f13", 302
"key.keyboard.f14", 303
"key.keyboard.f15", 304
"key.keyboard.f16", 305
"key.keyboard.f17", 306
"key.keyboard.f18", 307
"key.keyboard.f19", 308
"key.keyboard.f20", 309
"key.keyboard.f21", 310
"key.keyboard.f22", 311
"key.keyboard.f23", 312
"key.keyboard.f24", 313
"key.keyboard.f25", 314
"key.keyboard.num.lock", 282
"key.keyboard.keypad.0", 320
"key.keyboard.keypad.1", 321
"key.keyboard.keypad.2", 322
"key.keyboard.keypad.3", 323
"key.keyboard.keypad.4", 324
"key.keyboard.keypad.5", 325
"key.keyboard.keypad.6", 326
"key.keyboard.keypad.7", 327
"key.keyboard.keypad.8", 328
"key.keyboard.keypad.9", 329
"key.keyboard.keypad.add", 334
"key.keyboard.keypad.decimal", 330
"key.keyboard.keypad.enter", 335
"key.keyboard.keypad.equal", 336
"key.keyboard.keypad.multiply", 332
"key.keyboard.keypad.divide", 331
"key.keyboard.keypad.subtract", 333
"key.keyboard.down", 264
"key.keyboard.left", 263
"key.keyboard.right", 262
"key.keyboard.up", 265
"key.keyboard.apostrophe", 39
"key.keyboard.backslash", 92
"key.keyboard.comma", 44
"key.keyboard.equal", 61
"key.keyboard.grave.accent", 96
"key.keyboard.left.bracket", 91
"key.keyboard.minus", 45
"key.keyboard.period", 46
"key.keyboard.right.bracket", 93
"key.keyboard.semicolon", 59
"key.keyboard.slash", 47
"key.keyboard.space", 32
"key.keyboard.tab", 258
"key.keyboard.left.alt", 342
"key.keyboard.left.control", 341
"key.keyboard.left.shift", 340
"key.keyboard.left.win", 343
"key.keyboard.right.alt", 346
"key.keyboard.right.control", 345
"key.keyboard.right.shift", 344
"key.keyboard.right.win", 347
"key.keyboard.enter", 257
"key.keyboard.escape", 256
"key.keyboard.backspace", 259
"key.keyboard.delete", 261
"key.keyboard.end", 269
"key.keyboard.home", 268
"key.keyboard.insert", 260
"key.keyboard.page.down", 267
"key.keyboard.page.up", 266
"key.keyboard.caps.lock", 280
"key.keyboard.pause", 284
"key.keyboard.scroll.lock", 281
"key.keyboard.menu", 348
"key.keyboard.print.screen", 283
"key.keyboard.world.1", 161
"key.keyboard.world.2", 162
 */
