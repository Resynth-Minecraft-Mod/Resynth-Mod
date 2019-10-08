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
package com.ki11erwolf.resynth.util;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * Util class for quickly registering objects to
 * the game with a queue. The queue is always
 * emptied after iteration.
 *
 * @param <T> the object the queue holds.
 */
public class QueueRegisterer<T> {

    /**
     * Queue holding the objects that will be
     * iteratively registered to the game.
     *
     * This queue is used to programmatically
     * register objects to the game, after which
     * it is nullified (made {@code null}) as
     * it has no further purpose to store object
     * references.
     */
    private Queue<T> objects = new ArrayDeque<>();

    /**
     * Iterates over the queue and nullifies
     * it afterwards. This method can only be
     * used once.
     *
     * @param action queue iterator.
     * @return true if the queue was iterated,
     * false if the queue has been iterated already.
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean iterateQueue(Consumer<? super T> action){
        if(objects == null)
            return false;

        objects.iterator().forEachRemaining(action);
        objects.clear();
        objects = null;
        return true;
    }

    /**
     * Adds an object to the registration queue.
     *
     * @param object the object to add.
     */
    public void queueForRegistration(T object){
        objects.add(object);
    }
}
