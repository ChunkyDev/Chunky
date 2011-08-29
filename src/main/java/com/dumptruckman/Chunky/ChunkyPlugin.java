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

package com.dumptruckman.plugintemplate;

import com.dumptruckman.plugintemplate.config.Config;
import com.dumptruckman.plugintemplate.data.Data;
import com.dumptruckman.plugintemplate.locale.PluginLanguage;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author dumptruckman
 */
public class ChunkyPlugin extends JavaPlugin {

    final private static Logger log = Logger.getLogger("Minecraft.Chunky");
    private static String nameVersion = "";

    final public void onDisable() {
        // Save the plugin data
        Data.save(true);

        // Display disable message/version info
        log.info(nameVersion + "disabled.");
    }

    final public void onEnable() {
        // Grab the PluginManager
        final PluginManager pm = getServer().getPluginManager();

        // Grab the Plugin Description File
        PluginDescriptionFile pdf = getDescription();
        // Create a name and version string
        nameVersion = pdf.getName() + " " + pdf.getVersion() + " ";

        // Loads the configuration
        try {
            Config.load(this);
        } catch (IOException e) {  // Catch errors loading the config file and exit out if found.
            log.severe(nameVersion + "Encountered an error while loading the configuration file.  Disabling...");
            pm.disablePlugin(this);
            return;
        }

        // Loads the language
        try {
            PluginLanguage.load(this);
        } catch (IOException e) {  // Catch errors loading the language file and exit out if found.
            log.severe(nameVersion + "Encountered an error while loading the language file.  Disabling...");
            pm.disablePlugin(this);
            return;
        }

        // Loads the data
        try {
            Data.load(this);
        } catch (IOException e) {  // Catch errors loading the language file and exit out if found.
            log.severe(nameVersion + "Encountered an error while loading the data file.  Disabling...");
            pm.disablePlugin(this);
            return;
        }

        // Register Events
        registerEvents(pm);

        // Display enable message/version info
        log.info(nameVersion + "enabled.");
    }

    /**
     * Gets the logger associated with this plugin
     *
     * @return The logger associated with this plugin
     */
    final public Logger getLog() {
        return log;
    }

    final public void registerEvents(PluginManager pm) {
        // Event registering goes here
    }
}
