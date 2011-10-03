package org.getchunky.chunky;

import org.getchunky.chunky.command.*;
import org.getchunky.chunky.config.Config;
import org.getchunky.chunky.persistance.DatabaseManager;
import org.getchunky.chunky.event.ChunkyEvent;
import org.getchunky.chunky.exceptions.ChunkyUnregisteredException;
import org.getchunky.chunky.listeners.*;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyModuleManager;
import org.getchunky.chunky.module.SimpleChunkyModuleManager;
import org.getchunky.chunky.payment.Method;
import org.getchunky.chunky.util.Logging;
import org.blockface.bukkitstats.CallHome;
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
    private static Integer BUILD;
    private static ChunkyModuleManager CHUNKY_MODULE_MANAGER;
    // Event listeners
    final private PlayerEvents playerEvents = new PlayerEvents();
    final private BlockEvents blockEvents = new BlockEvents();
    final private ServerEvents serverEvents = new ServerEvents();
    final private ChunkyCommandEvents chunkyCommandEvents = new ChunkyCommandEvents();
    final private ChunkyObjectEvents chunkyObjectEvents = new ChunkyObjectEvents();
    final private ChunkyPlayerEvents chunkyPlayerEvents = new ChunkyPlayerEvents();

    /**
     * Called when this plugin is disabled.
     */
    final public void onDisable() {
        // Save the module data

        DatabaseManager.getDatabase().disconnect();

        // Display disable message/version info
        Logging.info("disabled.", true);
    }

    /**
     * Called when this plugin is enabled.
     */
    final public void onEnable() {
        //Load INSTANCE for other classes.
        INSTANCE = this;

        //Call Home
        CallHome.load(this);

        // Grab the PluginManager
        final PluginManager pm = getServer().getPluginManager();

        //Load Logging class
        Logging.load();

        // Grab the Plugin Description File
        PluginDescriptionFile pdf = getDescription();

        // Store the build number
        try {
            BUILD = Integer.valueOf(pdf.getVersion().substring(pdf.getVersion().indexOf("-b") + 2));
        } catch (NumberFormatException ignore) {
            Logging.warning("Build number unattainable.");
            BUILD = 0;
        }

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
            Language.load();
        } catch (IOException e) {  // Catch errors loading the language file and exit out if found.
            Logging.severe("Encountered an error while loading the language file.  Disabling...");
            pm.disablePlugin(this);
            return;
        }

        // Initialize ChunkyModuleManager
        CHUNKY_MODULE_MANAGER = new SimpleChunkyModuleManager();

        //Loads the data.
        if(!DatabaseManager.load()) {
            Logging.severe("Encountered an error while  loading data. Disabling...");
            pm.disablePlugin(this);
            return;
        }

        // Register Events
        registerBukkitEvents();
        registerChunkyEvents();
        // Register Commands
        registerChunkyCommands();

        // Display enable message/version info
        Logging.info("enabled.", true);
    }

    /**
     * Gets the instance of this plugin.
     *
     * @return The instance of this plugin
     */
    public static Chunky getInstance() {
        return INSTANCE;
    }

    /**
     * Gets the build number of this plugin.
     *
     * @return Build number of this plugin
     */
    public static Integer getBuildNumber() {
        return BUILD;
    }

    /**
     * Returns the Payment Method (Register)
     *
     * @return Payment Method
     */
    public static Method getMethod() {
        return METHOD;
    }

    /**
     * Sets the Payment Method (Register)
     * @param method new Payment Method
     */
    public static void setMethod(Method method) {
        METHOD = method;
    }

    /**
     * Registers all the bukkit events related to this plugin.
     */
    final private void registerBukkitEvents() {
        PluginManager pm = this.getServer().getPluginManager();
        // Player events.
        pm.registerEvent(Event.Type.PLAYER_MOVE, playerEvents, Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerEvents, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerEvents, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerEvents, Event.Priority.Normal, this);

        // Block events.
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockEvents, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockEvents, Event.Priority.Normal, this);

        // Server events
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, serverEvents, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLUGIN_DISABLE, serverEvents, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.MAP_INITIALIZE, serverEvents, Event.Priority.Normal, this);
    }

    /**
     * Registers all the Chunky events related to this plugin.
     */
    final private void registerChunkyEvents() {
        // Command Events
        getModuleManager().registerEvent(ChunkyEvent.Type.COMMAND_HELP, chunkyCommandEvents, ChunkyEvent.Priority.Monitor, this);
        getModuleManager().registerEvent(ChunkyEvent.Type.COMMAND_LIST, chunkyCommandEvents, ChunkyEvent.Priority.Monitor, this);

        //Object Events
        getModuleManager().registerEvent(ChunkyEvent.Type.OBJECT_NAME, chunkyObjectEvents, ChunkyEvent.Priority.Monitor, this);

        //Player Events
        //getModuleManager().registerEvent(ChunkyEvent.Type.PLAYER_BUILD, chunkyPlayerEvents, ChunkyEvent.Priority.Lowest, this);
        //getModuleManager().registerEvent(ChunkyEvent.Type.PLAYER_DESTROY, chunkyPlayerEvents, ChunkyEvent.Priority.Lowest, this);
        //getModuleManager().registerEvent(ChunkyEvent.Type.PLAYER_ITEM_USE, chunkyPlayerEvents, ChunkyEvent.Priority.Lowest, this);
        //getModuleManager().registerEvent(ChunkyEvent.Type.PLAYER_SWITCH, chunkyPlayerEvents, ChunkyEvent.Priority.Lowest, this);
    }

    /**
     * Registers all the Chunky Comands for this plugin.
     */
    final private void registerChunkyCommands() {
        try {
            // /chunky
            ChunkyCommand commandChunky = new ChunkyCommand("chunky", Arrays.asList("c"),
                    null, Language.getStrings(Language.CMD_CHUNKY_HELP),
                    new CommandChunky());
            getModuleManager().registerCommand(commandChunky);

            // /chunky claim
            ChunkyCommand commandChunkyClaim = new ChunkyCommand("claim", Arrays.asList("c"),
                    Language.getString(Language.CMD_CHUNKY_CLAIM_DESC),
                    Language.getStrings(Language.CMD_CHUNKY_CLAIM_HELP),
                    new CommandChunkyClaim(), commandChunky);
            getModuleManager().registerCommand(commandChunkyClaim);

            // /chunky unclaim
            ChunkyCommand commandChunkyUnclaim = new ChunkyCommand("unclaim", Arrays.asList("u", "uc"),
                    Language.getString(Language.CMD_CHUNKY_UNCLAIM_DESC),
                    Language.getStrings(Language.CMD_CHUNKY_UNCLAIM_HELP),
                    new CommandChunkyUnclaim(), commandChunky);
            getModuleManager().registerCommand(commandChunkyUnclaim);

            // /chunky player
            ChunkyCommand commandChunkyPlayer = new ChunkyCommand("player", Arrays.asList("pl"),
                    Language.getString(Language.CMD_CHUNKY_PLAYER_DESC),
                    Language.getStrings(Language.CMD_CHUNKY_PLAYER_HELP),
                    new CommandChunkyPlayer(), commandChunky);
            getModuleManager().registerCommand(commandChunkyPlayer);

            // /chunky chunk
            ChunkyCommand commandChunkyChunk = new ChunkyCommand("chunk", Arrays.asList("ch"),
                    Language.getString(Language.CMD_CHUNKY_CHUNK_DESC),
                    Language.getStrings(Language.CMD_CHUNKY_CHUNK_HELP),
                    new CommandChunkyChunk(), commandChunky);
            getModuleManager().registerCommand(commandChunkyChunk);

            // /chunky chunk set
            ChunkyCommand commandChunkyChunkSet = new ChunkyCommand("set", Arrays.asList("s"),
                    Language.getString(Language.CMD_CHUNKY_CHUNK_SET_DESC),
                    Language.getStrings(Language.CMD_CHUNKY_CHUNK_SET_HELP),
                    new CommandChunkyChunkSet(), commandChunkyChunk);
            getModuleManager().registerCommand(commandChunkyChunkSet);

            // /chunky chunk set name
            ChunkyCommand commandChunkyChunkSetName = new ChunkyCommand("name", Arrays.asList("n"),
                    Language.getString(Language.CMD_CHUNKY_CHUNK_SET_NAME_DESC),
                    Language.getStrings(Language.CMD_CHUNKY_CHUNK_SET_NAME_HELP),
                    new CommandChunkyChunkSetName(), commandChunkyChunkSet);
            getModuleManager().registerCommand(commandChunkyChunkSetName);


            // /chunky permission
            ChunkyCommand commandChunkyPermission = new ChunkyCommand("permission", Arrays.asList("p", "perm", "perms"),
                    Language.getString(Language.CMD_CHUNKY_PERMISSION_DESC),
                    Language.getStrings(Language.CMD_CHUNKY_PERMISSION_HELP),
                    new CommandChunkyPermission(), commandChunky);
            getModuleManager().registerCommand(commandChunkyPermission);
            
        } catch (ChunkyUnregisteredException ignore) {}
    }

    /**
     * Returns the ChunkyModuleManager.  With this manager you may register your plugins Chunky events and Chunky commands.
     * 
     * @return the ModuleManager for Chunky
     */
    static public ChunkyModuleManager getModuleManager() {
        return CHUNKY_MODULE_MANAGER;
    }
}