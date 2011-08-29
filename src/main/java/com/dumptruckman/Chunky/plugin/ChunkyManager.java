package com.dumptruckman.chunky.plugin;

import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.event.ChunkyListener;
import org.bukkit.plugin.Plugin;

/**
 * @author dumptruckman
 */
public interface ChunkyManager {

    /**
     * Calls an event with the given details
     *
     * @param event Event details
     */
    public void callEvent(ChunkyEvent event);

    /**
     * Registers the given event to the specified listener
     *
     * @param type ChunkyEventType to register
     * @param listener ChunkyListener to register
     * @param priority Priority of this event
     * @param plugin Plugin to register
     */
    public void registerEvent(ChunkyEvent.Type type, ChunkyListener listener,
                              ChunkyEvent.Priority priority, Plugin plugin);

}