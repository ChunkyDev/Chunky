package com.dumptruckman.chunky.plugin;

import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.event.ChunkyListener;
import org.bukkit.plugin.Plugin;

/**
 * @author dumptruckman, SwearWord
 */
public class RegisteredChunkyListener {
    private final ChunkyListener listener;
    private final ChunkyEvent.Priority priority;
    private final Plugin plugin;
    private final ChunkyEventExecutor executor;

    public RegisteredChunkyListener(final ChunkyListener pluginListener, final ChunkyEventExecutor eventExecutor, final ChunkyEvent.Priority eventPriority, final Plugin registeredPlugin) {
        listener = pluginListener;
        priority = eventPriority;
        plugin = registeredPlugin;
        executor = eventExecutor;
    }

    public RegisteredChunkyListener(final ChunkyListener pluginListener, final ChunkyEvent.Priority eventPriority, final Plugin registeredPlugin, ChunkyEvent.Type type) {
        listener = pluginListener;
        priority = eventPriority;
        plugin = registeredPlugin;
        executor = SimpleChunkyModuleManager.createExecutor(type, pluginListener);
    }

    /**
     * Gets the listener for this registration
     * @return Registered Listener
     */
    public ChunkyListener getListener() {
        return listener;
    }

    /**
     * Gets the plugin for this registration
     * @return Registered Plugin
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Gets the priority for this registration
     * @return Registered Priority
     */
    public ChunkyEvent.Priority getPriority() {
        return priority;
    }

    /**
     * Calls the event executor
     * @return Registered Priority
     */
    public void callEvent(ChunkyEvent event) {
        executor.execute(listener, event);
    }
}
