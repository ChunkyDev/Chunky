package org.getchunky.chunky;

import org.blockface.bukkitstats.CallHome;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.getchunky.chunky.command.*;
import org.getchunky.chunky.config.Config;
import org.getchunky.chunky.event.ChunkyEvent;
import org.getchunky.chunky.exceptions.ChunkyUnregisteredException;
import org.getchunky.chunky.listeners.*;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyModuleManager;
import org.getchunky.chunky.module.SimpleChunkyModuleManager;
import org.getchunky.chunky.permission.bukkit.Permissions;
import org.getchunky.chunky.persistance.DatabaseManager;
import org.getchunky.chunky.util.Logging;
import org.getchunky.register.payment.Method;
import org.getchunky.register.payment.Methods;

import java.io.IOException;

/**
 * @author dumptruckman, SwearWord
 */
public class Chunky extends JavaPlugin {

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
        if (!DatabaseManager.load()) {
            Logging.severe("Encountered an error while  loading data. Disabling...");
            pm.disablePlugin(this);
            return;
        }

        // Register Events
        registerBukkitEvents();
        registerChunkyEvents();

        //Load Economy
        Methods.setMethod(this.getServer().getPluginManager());

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
        return Methods.getMethod();
    }

    /**
     * Registers all the bukkit events related to this plugin.
     */
    final private void registerBukkitEvents() {
        PluginManager pm = this.getServer().getPluginManager();
        // Player events.
        pm.registerEvent(Event.Type.PLAYER_MOVE, playerEvents, Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerEvents, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerEvents, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerEvents, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerEvents, Event.Priority.Normal, this);

        // Block events.
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockEvents, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockEvents, Event.Priority.Normal, this);

        // Server events
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, serverEvents, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLUGIN_DISABLE, serverEvents, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.SERVER_COMMAND, serverEvents, Event.Priority.Normal, this);
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
            ChunkyCommand commandChunky = new ChunkyCommand("chunky", new CommandChunky(), null)
                    .setAliases("c")
                    .setHelpLines(Language.getStrings(Language.CMD_CHUNKY_HELP))
                    .register();

            // /chunky claim
            ChunkyCommand commandChunkyClaim = new ChunkyCommand("claim", new CommandChunkyClaim(), commandChunky)
                    .setDescription(Language.getString(Language.CMD_CHUNKY_CLAIM_DESC))
                    .setAliases("c")
                    .setHelpLines(Language.getStrings(Language.CMD_CHUNKY_CLAIM_HELP))
                    .register();

            // /chunky unclaim
            ChunkyCommand commandChunkyUnclaim = new ChunkyCommand("unclaim", new CommandChunkyUnclaim(), commandChunky)
                    .setDescription(Language.getString(Language.CMD_CHUNKY_UNCLAIM_DESC))
                    .setAliases("u", "uc")
                    .setHelpLines(Language.getStrings(Language.CMD_CHUNKY_UNCLAIM_HELP))
                    .register();

            // /chunky player
            ChunkyCommand commandChunkyPlayer = new ChunkyCommand("player", new CommandChunkyPlayer(), commandChunky)
                    .setDescription(Language.getString(Language.CMD_CHUNKY_PLAYER_DESC))
                    .setAliases("pl")
                    .setHelpLines(Language.getStrings(Language.CMD_CHUNKY_PLAYER_HELP))
                    .register();

            // /chunky player set
            ChunkyCommand commandChunkyPlayerSet = new ChunkyCommand("set", new CommandChunkyPlayerSet(), commandChunkyPlayer)
                    .setDescription(Language.getString(Language.CMD_CHUNKY_PLAYER_SET_DESC))
                    .setAliases("s")
                    .setHelpLines(Language.getStrings(Language.CMD_CHUNKY_PLAYER_SET_HELP))
                    .register();

            // /chunky player set mode
            ChunkyCommand commandChunkyPlayerSetMode = new ChunkyCommand("mode", new CommandChunkyPlayerSetMode(), commandChunkyPlayerSet)
                    .setDescription(Language.getString(Language.CMD_CHUNKY_PLAYER_SET_MODE_DESC))
                    .setAliases("m")
                    .setHelpLines(Language.getStrings(Language.CMD_CHUNKY_PLAYER_SET_MODE_HELP))
                    .register();

            // /chunky chunk
            ChunkyCommand commandChunkyChunk = new ChunkyCommand("chunk", new CommandChunkyChunk(), commandChunky)
                    .setDescription(Language.getString(Language.CMD_CHUNKY_CHUNK_DESC))
                    .setAliases("ch")
                    .setHelpLines(Language.getStrings(Language.CMD_CHUNKY_CHUNK_HELP))
                    .register();

            // /chunky chunk set
            ChunkyCommand commandChunkyChunkSet = new ChunkyCommand("set", new CommandChunkyChunkSet(), commandChunkyChunk)
                    .setDescription(Language.getString(Language.CMD_CHUNKY_CHUNK_SET_DESC))
                    .setAliases("s")
                    .setHelpLines(Language.getStrings(Language.CMD_CHUNKY_CHUNK_SET_HELP))
                    .register();

            // /chunky chunk set name
            ChunkyCommand commandChunkyChunkSetName = new ChunkyCommand("name", new CommandChunkyChunkSetName(), commandChunkyChunkSet)
                    .setDescription(Language.getString(Language.CMD_CHUNKY_CHUNK_SET_NAME_DESC))
                    .setAliases("n")
                    .setHelpLines(Language.getStrings(Language.CMD_CHUNKY_CHUNK_SET_NAME_HELP))
                    .register();

            // /chunky permission
            ChunkyCommand commandChunkyPermission = new ChunkyCommand("permission", new CommandChunkyPermission(), commandChunky)
                    .setDescription(Language.getString(Language.CMD_CHUNKY_PERMISSION_DESC))
                    .setAliases("p", "perm", "perms")
                    .setHelpLines(Language.getStrings(Language.CMD_CHUNKY_PERMISSION_HELP))
                    .register();

            // /chunky group
            ChunkyCommand commandChunkyGroup = new ChunkyCommand("group", new CommandChunkyGroup(), commandChunky)
                    .setDescription(Language.getString(Language.CMD_CHUNKY_GROUP_DESC))
                    .setAliases("gr")
                    .setHelpLines(Language.getStrings(Language.CMD_CHUNKY_GROUP_HELP))
                    .register();

            // /chunky group add
            ChunkyCommand commandChunkyGroupAdd = new ChunkyCommand("add", new CommandChunkyGroupAdd(), commandChunkyGroup)
                    .setDescription(Language.getString(Language.CMD_CHUNKY_GROUP_ADD_DESC))
                    .setAliases("a")
                    .setHelpLines(Language.getStrings(Language.CMD_CHUNKY_GROUP_ADD_HELP))
                    .register();

            // /chunky group remove
            ChunkyCommand commandChunkyGroupRemove = new ChunkyCommand("remove", new CommandChunkyGroupRemove(), commandChunkyGroup)
                    .setDescription(Language.getString(Language.CMD_CHUNKY_GROUP_RM_DESC))
                    .setAliases("r", "rm")
                    .setHelpLines(Language.getStrings(Language.CMD_CHUNKY_GROUP_RM_HELP))
                    .register();

            // /addfriend
            ChunkyCommand commandAddfriend = new ChunkyCommand("addfriend", new CommandAddfriend(), null)
                    .setAliases("af", "addf")
                    .setHelpLines(Language.getStrings(Language.CMD_ADDFRIEND_HELP))
                    .register();

            // /addfriend
            ChunkyCommand commandRemovefriend = new ChunkyCommand("removefriend", new CommandRemovefriend(), null)
                    .setAliases("rf", "rmfriend", "rmf")
                    .setHelpLines(Language.getStrings(Language.CMD_RMFRIEND_HELP))
                    .register();

            // /chunky admin
            ChunkyCommand commandChunkyAdmin = new ChunkyCommand("admin", new CommandChunkyAdmin(), commandChunky)
                    .setDescription(Language.getString(Language.CMD_CHUNKY_ADMIN_DESC))
                    .setAliases("a")
                    .setHelpLines(Language.getStrings(Language.CMD_CHUNKY_ADMIN_HELP))
                    .setRequiresEnabledWorld(false)
                    .setPermission(Permissions.CMD_ADMIN.getNode())
                    .register();

            // /chunky admin chunklimit
            ChunkyCommand commandChunkyAdminChunklimit = new ChunkyCommand("chunklimit", new CommandChunkyAdminChunklimit(), commandChunkyAdmin)
                    .setDescription(Language.getString(Language.CMD_CHUNKY_ADMIN_CHUNKLIMIT_DESC))
                    .setAliases("cl", "limit")
                    .setHelpLines(Language.getStrings(Language.CMD_CHUNKY_ADMIN_CHUNKLIMIT_HELP))
                    .setRequiresEnabledWorld(false)
                    .register();

            // /chunky admin enableworld
            ChunkyCommand commandChunkyAdminEnableworld = new ChunkyCommand("enableworld", new CommandChunkyAdminEnableworld(), commandChunkyAdmin)
                    .setDescription(Language.getString(Language.CMD_CHUNKY_ADMIN_ENABLEWORLD_DESC))
                    .setAliases("ew")
                    .setHelpLines(Language.getStrings(Language.CMD_CHUNKY_ADMIN_ENABLEWORLD_HELP))
                    .setRequiresEnabledWorld(false)
                    .register();

            // /chunky admin disableworld
            ChunkyCommand commandChunkyAdminDisableworld = new ChunkyCommand("disableworld", new CommandChunkyAdminDisableworld(), commandChunkyAdmin)
                    .setDescription(Language.getString(Language.CMD_CHUNKY_ADMIN_DISABLEWORLD_DESC))
                    .setAliases("dw")
                    .setHelpLines(Language.getStrings(Language.CMD_CHUNKY_ADMIN_DISABLEWORLD_HELP))
                    .setRequiresEnabledWorld(false)
                    .register();

        } catch (ChunkyUnregisteredException ignore) {
        }
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