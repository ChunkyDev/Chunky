package com.dumptruckman.chunky.plugin;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.event.ChunkyListener;

/**
 * @author dumptruckman
 */
public interface ChunkyManager {

    public void registerEvent(ChunkyEvent.Type type, ChunkyListener listener,
                              ChunkyEvent.Priority priority, Chunky plugin);
    
}
