package com.dumptruckman.chunky;

import com.dumptruckman.chunky.config.Config;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.persistance.DatabaseManager;
import com.dumptruckman.chunky.plugin.ChunkyModuleManager;
import com.dumptruckman.chunky.plugin.SimpleChunkyModuleManager;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

/**
 * @author dumptruckman, SwearWord
 */
public class Chunky extends JavaPlugin {

    private static Chunky instance;
    private static ChunkyModuleManager CHUNKY_MODULE_MANAGER;

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
            Language.load(this);
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

        // Initialize ChunkyModuleManager
        CHUNKY_MODULE_MANAGER = new SimpleChunkyModuleManager(this);

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

    static public ChunkyModuleManager getModuleManager() {
        return CHUNKY_MODULE_MANAGER;
    }



}
