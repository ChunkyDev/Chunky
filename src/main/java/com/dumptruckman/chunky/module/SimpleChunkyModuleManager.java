package com.dumptruckman.chunky.module;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.event.ChunkyListener;
import com.dumptruckman.chunky.event.CustomChunkyEventListener;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.logging.Level;

/**
 * @author dumptruckman, SwearWord
 */
public class SimpleChunkyModuleManager implements ChunkyModuleManager {

    private Chunky plugin;
    private final Map<ChunkyEvent.Type, SortedSet<RegisteredChunkyListener>> listeners = new EnumMap<ChunkyEvent.Type, SortedSet<RegisteredChunkyListener>>(ChunkyEvent.Type.class);
    private final Comparator<RegisteredChunkyListener> comparer = new Comparator<RegisteredChunkyListener>() {
        public int compare(RegisteredChunkyListener i, RegisteredChunkyListener j) {
            int result = i.getPriority().compareTo(j.getPriority());

            if ((result == 0) && (i != j)) {
                result = 1;
            }

            return result;
        }
    };

    public SimpleChunkyModuleManager(Chunky plugin) {
        this.plugin = plugin;
    }

    /**
     * Calls an event with the given details
     *
     * @param event Event details
     */
    public synchronized void callEvent(ChunkyEvent event) {
        SortedSet<RegisteredChunkyListener> eventListeners = listeners.get(event.getType());

        if (eventListeners != null) {
            for (RegisteredChunkyListener registration : eventListeners) {
                try {
                    registration.callEvent(event);
                } catch (Throwable ex) {
                    Logging.getLog().log(Level.SEVERE, "Could not pass event " + event.getType() + " to "
                            + registration.getPlugin().getDescription().getName(), ex);
                }
            }
        }
    }

    /**
     * Registers the given event to the specified listener
     *
     * @param type ChunkyEventType to register
     * @param listener ChunkyListener to register
     * @param priority Priority of this event
     * @param plugin Plugin to register
     */
    public void registerEvent(ChunkyEvent.Type type, ChunkyListener listener,
                              ChunkyEvent.Priority priority, Plugin plugin) {
        getEventListeners(type).add(new RegisteredChunkyListener(listener, priority, plugin, type));
    }

    /**
     * Returns a SortedSet of RegisteredChunkyListener for the specified event type creating a new queue if needed
     *
     * @param type ChunkyEventType to lookup
     * @return SortedSet<RegisteredChunkyListener> the looked up or create queue matching the requested type
     */
    private SortedSet<RegisteredChunkyListener> getEventListeners(ChunkyEvent.Type type) {
        SortedSet<RegisteredChunkyListener> eventListeners = listeners.get(type);

        if (eventListeners != null) {
            return eventListeners;
        }

        eventListeners = new TreeSet<RegisteredChunkyListener>(comparer);
        listeners.put(type, eventListeners);
        return eventListeners;
    }

    protected static ChunkyEventExecutor createExecutor(ChunkyEvent.Type type, ChunkyListener listener) {

        switch (type) {

            // Custom Events
            case CUSTOM_EVENT:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((CustomChunkyEventListener) listener).onCustomEvent(event);
                    }
                };
        }

        throw new IllegalArgumentException("Event " + type + " is not supported");
    }
}
