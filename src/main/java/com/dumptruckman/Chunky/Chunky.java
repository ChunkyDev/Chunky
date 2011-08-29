/*
 * Copyright (c) 2011. dumptruckman
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.dumptruckman.chunky;

import com.dumptruckman.chunky.config.Config;
import com.dumptruckman.chunky.data.Data;
import com.dumptruckman.chunky.locale.PluginLanguage;
import com.dumptruckman.chunky.plugin.ChunkyManager;
import com.dumptruckman.chunky.plugin.SimpleChunkyManager;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author dumptruckman
 */
public class Chunky extends JavaPlugin {

    final private static Logger LOG = Logger.getLogger("Minecraft.Chunky");

    private static ChunkyManager CHUNKY_MANAGER;
    private static String NAME_VERSION = "";

    final public void onDisable() {
        // Save the plugin data
        Data.save(true);

        // Display disable message/version info
        getLog().info(NAME_VERSION + "disabled.");
    }

    final public void onEnable() {
        // Grab the PluginManager
        final PluginManager pm = getServer().getPluginManager();

        // Grab the Plugin Description File
        PluginDescriptionFile pdf = getDescription();
        // Create a name and version string
        NAME_VERSION = pdf.getName() + " " + pdf.getVersion() + " ";

        // Loads the configuration
        try {
            Config.load(this);
        } catch (IOException e) {  // Catch errors loading the config file and exit out if found.
            getLog().severe(NAME_VERSION + "Encountered an error while loading the configuration file.  Disabling...");
            pm.disablePlugin(this);
            return;
        }

        // Loads the language
        try {
            PluginLanguage.load(this);
        } catch (IOException e) {  // Catch errors loading the language file and exit out if found.
            getLog().severe(NAME_VERSION + "Encountered an error while loading the language file.  Disabling...");
            pm.disablePlugin(this);
            return;
        }

        // Loads the data
        try {
            Data.load(this);
        } catch (IOException e) {  // Catch errors loading the language file and exit out if found.
            getLog().severe(NAME_VERSION + "Encountered an error while loading the data file.  Disabling...");
            pm.disablePlugin(this);
            return;
        }

        // Register Events
        registerEvents(pm);

        // Initialize ChunkyManager
        CHUNKY_MANAGER = new SimpleChunkyManager(this);

        // Display enable message/version info
        getLog().info(NAME_VERSION + "enabled.");
    }

    /**
     * Gets the logger associated with this plugin
     *
     * @return The logger associated with this plugin
     */
    final public Logger getLog() {
        return LOG;
    }

    final public void registerEvents(PluginManager pm) {
        // Event registering goes here
    }

    final public ChunkyManager getManager() {
        return CHUNKY_MANAGER;
    }
}
