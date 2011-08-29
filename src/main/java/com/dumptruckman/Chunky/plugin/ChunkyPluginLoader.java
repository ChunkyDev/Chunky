package com.dumptruckman.chunky.plugin;

import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.event.ChunkyListener;

/**
 * @author dumptruckman
 */
public interface ChunkyPluginLoader {

    /**
     * Creates and returns an event executor
     *
     * @param type Type of the event executor to create
     * @param listener the object that will handle the eventual call back
     */
    public ChunkyEventExecutor createExecutor(ChunkyEvent.Type type, ChunkyListener listener);
}
