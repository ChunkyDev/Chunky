package com.dumptruckman.chunky.plugin;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.event.ChunkyListener;
import com.dumptruckman.chunky.object.ChunkyObject;
import org.bukkit.plugin.Plugin;

/**
 * @author dumptruckman
 */
public class SimpleChunkyManager extends ChunkyObject implements ChunkyManager {

    transient private Chunky plugin;

    public SimpleChunkyManager(Chunky plugin) {
        this.plugin = plugin;
    }

    public void registerEvent(ChunkyEvent.Type type, ChunkyListener listener,
                              ChunkyEvent.Priority priority, Plugin plugin) {
        
    }
}
