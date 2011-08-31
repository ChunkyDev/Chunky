package com.dumptruckman.chunky;

import com.dumptruckman.chunky.command.ChunkyCommandExecutor;
import com.dumptruckman.chunky.config.Config;
import com.dumptruckman.chunky.listeners.BlockEvents;
import com.dumptruckman.chunky.listeners.PlayerEvents;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.persistance.DatabaseManager;
import com.dumptruckman.chunky.plugin.ChunkyModuleManager;
import com.dumptruckman.chunky.plugin.SimpleChunkyModuleManager;
import com.dumptruckman.chunky.util.Logging;
import com.nijiko.coelho.iConomy.net.Database;
import org.bukkit.event.Event;
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
    // Event listeners
    final private PlayerEvents playerEvents = new PlayerEvents();
    final private BlockEvents blockEvents = new BlockEvents();

    final public void onDisable() {
        // Save the plugin data

        DatabaseManager.closeDB();

        // Display disable message/version info
        Logging.info("disabled.");
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

        DatabaseManager.addType(ChunkyChunk.class.getName().hashCode(), "ChunkyChunk");
        DatabaseManager.addType(ChunkyPlayer.class.getName().hashCode(), "ChunkyPlayer");
        DatabaseManager.loadData();

        // Register Events
        registerEvents(pm);
        // Register Commands
        registerCommands();

        // Initialize ChunkyModuleManager
        CHUNKY_MODULE_MANAGER = new SimpleChunkyModuleManager(this);

        // Display enable message/version info
        Logging.info("enabled.");
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
        // Player events.
        pm.registerEvent(Event.Type.PLAYER_MOVE,playerEvents, Event.Priority.Highest,this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerEvents, Event.Priority.Normal, this);

        // Block events.
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockEvents, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockEvents, Event.Priority.Normal, this);

    }

    final public void registerCommands() {
        getCommand("chunky").setExecutor(new ChunkyCommandExecutor());
    }

    static public ChunkyModuleManager getModuleManager() {
        return CHUNKY_MODULE_MANAGER;
    }
}
