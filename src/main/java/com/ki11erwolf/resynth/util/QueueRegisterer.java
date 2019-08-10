package com.ki11erwolf.resynth.util;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * Util class for quickly registering objects to
 * the game with a queue.
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
