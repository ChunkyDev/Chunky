package com.dumptruckman.chunky.plugin;

/**
 * @author dumptruckman
 */
public interface ChunkyPlugin {

    /**
     * Gets the associated PluginLoader responsible for this plugin
     *
     * @return PluginLoader that controls this plugin
     */
    public ChunkyPluginLoader getChunkyPluginLoader();
}
