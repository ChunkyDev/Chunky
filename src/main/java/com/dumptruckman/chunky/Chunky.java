package com.dumptruckman.chunky;

import com.dumptruckman.chunky.command.CommandChunky;
import com.dumptruckman.chunky.command.CommandChunkyClaim;
import com.dumptruckman.chunky.command.CommandChunkyUnclaim;
import com.dumptruckman.chunky.config.Config;
import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.exceptions.ChunkyUnregisteredException;
import com.dumptruckman.chunky.listeners.*;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.locale.LanguagePath;
import com.dumptruckman.chunky.module.ChunkyCommand;
import com.dumptruckman.chunky.module.ChunkyModuleManager;
import com.dumptruckman.chunky.module.SimpleChunkyModuleManager;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.payment.Method;
import com.dumptruckman.chunky.persistance.DatabaseManager;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author dumptruckman, SwearWord
 */
public class Chunky extends JavaPlugin {

    private static Method METHOD = null;
    private static Chunky INSTANCE;
    private static ChunkyModuleManager CHUNKY_MODULE_MANAGER;
    // Event listeners
    final private PlayerEvents playerEvents = new PlayerEvents();
    final private BlockEvents blockEvents = new BlockEvents();
    final private ServerEvents serverEvents = new ServerEvents();
    final private ChunkyCommandEvents chunkyCommandEvents = new ChunkyCommandEvents();
    final private ChunkyObjectEvents chunkyObjectEvents = new ChunkyObjectEvents();
    final private ChunkyPlayerEvents chunkyPlayerEvents = new ChunkyPlayerEvents();

    final public void onDisable() {
        // Save the module data

        DatabaseManager.closeDB();

        // Display disable message/version info
        Logging.info("disabled.");
    }

    final public void onEnable() {
        //Load INSTANCE for other classes.
        INSTANCE = this;

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

        // Initialize ChunkyModuleManager
        CHUNKY_MODULE_MANAGER = new SimpleChunkyModuleManager(this);

        //Loads the data.
        if(!DatabaseManager.load())
        {
            Logging.severe("Encoutered an error while  loading data. Disabling...");
            pm.disablePlugin(this);
        }

        DatabaseManager.addType(ChunkyChunk.class.getName().hashCode(), "ChunkyChunk");
        DatabaseManager.addType(ChunkyPlayer.class.getName().hashCode(), "ChunkyPlayer");
        DatabaseManager.loadData();

        // Register Events
        registerBukkitEvents(pm);
        registerChunkyEvents();
        // Register Commands
        registerCommands();

        // Display enable message/version info
        Logging.info("enabled.");
    }

    /**
     * Gets the instance of this module.
     *
     * @return The instance of this module
     */
    public static Chunky getInstance() {
        return INSTANCE;
    }

    public static Method getMethod() {
        return METHOD;
    }

    public static void setMethod(Method method) {
        METHOD = method;
    }

    final public void registerBukkitEvents(PluginManager pm) {
        // Player events.
        pm.registerEvent(Event.Type.PLAYER_MOVE, playerEvents, Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerEvents, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerEvents, Event.Priority.Normal, this);

        // Block events.
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockEvents, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockEvents, Event.Priority.Normal, this);

        // Server events
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, serverEvents, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLUGIN_DISABLE, serverEvents, Event.Priority.Normal, this);
    }

    final public void registerChunkyEvents() {
        // Command Events
        getModuleManager().registerEvent(ChunkyEvent.Type.COMMAND_HELP, chunkyCommandEvents, ChunkyEvent.Priority.Highest, this);
        getModuleManager().registerEvent(ChunkyEvent.Type.COMMAND_LIST, chunkyCommandEvents, ChunkyEvent.Priority.Highest, this);

        //Object Events
        getModuleManager().registerEvent(ChunkyEvent.Type.OBJECT_NAME, chunkyObjectEvents, ChunkyEvent.Priority.Monitor, this);

        //Player Events
        getModuleManager().registerEvent(ChunkyEvent.Type.PLAYER_UNOWNED_BUILD, chunkyPlayerEvents, ChunkyEvent.Priority.Lowest, this);
        getModuleManager().registerEvent(ChunkyEvent.Type.PLAYER_UNOWNED_BREAK, chunkyPlayerEvents, ChunkyEvent.Priority.Lowest, this);
        getModuleManager().registerEvent(ChunkyEvent.Type.PLAYER_ITEM_USE, chunkyPlayerEvents, ChunkyEvent.Priority.Lowest, this);
        getModuleManager().registerEvent(ChunkyEvent.Type.PLAYER_SWITCH, chunkyPlayerEvents, ChunkyEvent.Priority.Lowest, this);
    }

    final public void registerCommands() {
        try {
            ChunkyCommand commandChunky = new ChunkyCommand("chunky", Arrays.asList("c"),
                    null, Language.getStrings(LanguagePath.CMD_CHUNKY_HELP),
                    new CommandChunky());
            getModuleManager().registerCommand(commandChunky);
            ChunkyCommand commandChunkyClaim = new ChunkyCommand("claim", Arrays.asList("c"),
                    Language.getString(LanguagePath.CMD_CHUNKY_CLAIM_DESC),
                    Language.getStrings(LanguagePath.CMD_CHUNKY_CLAIM_HELP),
                    new CommandChunkyClaim(), commandChunky);
            getModuleManager().registerCommand(commandChunkyClaim);
            ChunkyCommand commandChunkyUnclaim = new ChunkyCommand("unclaim", Arrays.asList("u", "uc"),
                    Language.getString(LanguagePath.CMD_CHUNKY_UNCLAIM_DESC),
                    Language.getStrings(LanguagePath.CMD_CHUNKY_UNCLAIM_HELP),
                    new CommandChunkyUnclaim(), commandChunky);
            getModuleManager().registerCommand(commandChunkyClaim);
        } catch (ChunkyUnregisteredException ignore) {}
    }

    static public ChunkyModuleManager getModuleManager() {
        return CHUNKY_MODULE_MANAGER;
    }
}
