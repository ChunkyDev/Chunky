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
import com.dumptruckman.chunky.locale.PluginLanguage;
import com.dumptruckman.chunky.persistance.DatabaseManager;
import com.dumptruckman.chunky.plugin.ChunkyManager;
import com.dumptruckman.chunky.plugin.SimpleChunkyManager;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

/**
 * @author dumptruckman
 */
public class Chunky extends JavaPlugin {

    private static Chunky instance;
    private static ChunkyManager CHUNKY_MANAGER;

    final public void onDisable() {
        // Save the plugin data

        // Display disable message/version info
        Logging.info("Disabled.");
    }

    final public void onEnable() {
        //Load instance for other classes.
        instance = this;


        // Grab the PluginManager
        final PluginManager pm = getServer().getPluginManager();

        //Load Logging class
        Logging.load(this);

        // Grab the Plugin Description File
        PluginDescriptionFile pdf = getDescription();

        // Loads the configuration
        try {
            Config.load(this);
        } catch (IOException e) {  // Catch errors loading the config file and exit out if found.
            Logging.severe("Encountered an error while loading the configuration file.  Disabling...");
            pm.disablePlugin(this);
            return;
        }

        // Loads the language
        try {
            PluginLanguage.load(this);
        } catch (IOException e) {  // Catch errors loading the language file and exit out if found.
            Logging.severe("Encountered an error while loading the language file.  Disabling...");
            pm.disablePlugin(this);
            return;
        }

        //Loads the data.
        if(!DatabaseManager.load())
        {
            Logging.severe("Encoutered an error while loading data. Disabling...");
            pm.disablePlugin(this);
        }

        // Register Events
        registerEvents(pm);

        // Initialize ChunkyManager
        CHUNKY_MANAGER = new SimpleChunkyManager(this);

        // Display enable message/version info
        Logging.info("Enabled.");
    }

    /**
     * Gets the logger associated with this plugin
     *
     * @return The logger associated with this plugin
     */
    //Get instance of the plugin.
    public static Chunky getInstance() {
        return instance;
    }

    final public void registerEvents(PluginManager pm) {
        // Event registering goes here
    }

    static public ChunkyManager getManager() {
        return CHUNKY_MANAGER;
    }



}
