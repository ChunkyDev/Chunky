package com.dumptruckman.chunky.locale;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.config.Config;
import com.dumptruckman.chunky.exceptions.ChunkyPlayerOfflineException;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dumptruckman, SwearWord
 */
public enum Language {
    NO_COMMAND_PERMISSION ("command.no_permission", "You do not have permission to access this command!", 0),
    IN_GAME_ONLY ("misc.in_game_only", "Only in game players may use this feature!", 0),
    UNREGISTERED_CHUNK_NAME("misc.unregistered_chunk_name", "Wilderness", 0),

    /**
     * Command Language
     */
    CMD_CHUNKY ("command.chunky", Arrays.asList("&e=== Chunky Information ===",
            "&fVersion: %1", "Loaded Modules - %2",
            "You can use &ehelp&f or &e?&f after chunky sub-commands",
            "&e/chunky <sub-cmd> help&f and &e/chunky <sub-cmd> ?&f",
            "Try typing &6/chunky help"), 0),
    CMD_CHUNKY_HELP ("command.chunky_help", Arrays.asList("This command contains the main functions of Chunky.",
            "Type &6/chunky ? &fto see a list of sub-commands!"), 0),
    CMD_CHUNKY_CLAIM_DESC ("command.chunky_claim_desc", "Claims the chunk the user stands in.", 0),
    CMD_CHUNKY_CLAIM_HELP ("command.chunky_claim_help", "Claims the chunk you are standing in.", 0),
    CMD_CHUNKY_UNCLAIM_DESC ("command.chunky_unclaim_desc", "Unclaims the chunk the user stands in.", 0),
    CMD_CHUNKY_UNCLAIM_HELP ("command.chunky_unclaim_help", "Unclaims the chunk you are standing in.", 0),
    //CMD_CHUNKY_PERMISSION_DESC("command.chunky_permission_desc", "Shows permissions for stuff.  Also allows setting permissions.", 0),
    //CMD_CHUNKY_PERMISSION_HELP("command.chunky_permission_help", "", 0),
    CMD_CHUNKY_PERMISSION_DESC("command.chunky_permission_set_desc", "Allows you to set permissions for other players and eventually groups.", 0),
    CMD_CHUNKY_PERMISSION_HELP("command.chunky_permission_set_help",
            Arrays.asList("&eUsage: /chunky permission [*:][+/-]<flags/clear> [player]",
                    //"Note: ChunkName and g:group flags are not yet availiable.",
                    "&e(Optional) *: &fallows setting a player's permissions for ALL of your chunks", // or chunks with a specific name.",
                    "&eFlags: &fThe type of permissions to give.  ",
                    "&eFlags: &fb: build, d: destroy, i: item use, s: switch.  clear: removes all flags.",
                    "&e(Optional) +/-: &fThese will allow you to ADD to or REMOVE from the existing flags.",
                    "&eExample: &f\"&e/c p *:-bd dumptruckman&f\" removes build and destroy permission from all your chunks for dumptruckman"), 0),
    CMD_CHUNKY_PLAYER_DESC ("command.chunky_player_desc", "Contains sub-commands related to players and info regarding yourself", 0),
    CMD_CHUNKY_PLAYER_HELP ("command.chunky_player_help", "This command will tell you a little about yourself and contains some sub-commands related to players.", 0),

    CMD_CHUNKY_CHUNK_DESC ("command.chunky_chunk_desc", "Information about the chunk you are in.", 0),
    CMD_CHUNKY_CHUNK_HELP ("command.chunky_chunk_help", "This command will tell you a little about the chunk you're standing in.", 0),


    CMD_HELP ("command.help", "Help for command: %1 (%2)", 0),
    CMD_LIST ("command.list", "Sub-command list for: %1 (%2)", 0),


    ERROR ("error", "[Error]", 0),
    SUCCESS ("success", "[Success]", 0),
    HELP ("help", "[Help]", 0),
    NO_PERMISSIONS_SET("permissions.not_set", "NOT SET", 0),
    NO_PERMISSIONS_GRANTED("permissions.none", "NONE", 0),
    CHUNK_OWNED ("chunk.owned", "This chunk is owned by: %1", 0),
    CHUNK_NOT_OWNED("chunk.not_owned", "This chunk is not owned!", 0),
    CHUNK_NONE_OWNED("chunk.none_owned", "You do not own any chunks!", 0),
    CHUNK_LIMIT_REACHED ("chunky.limit", "You have claimed have claimed your maximum amount of chunks! (%1)", 0),
    CHUNK_CLAIMED ("chunk.claimed", "You have claimed chunk at [%1, %2]!", 0),
    CHUNK_UNCLAIMED ("chunk.unclaimed", "You have unclaimed chunk at [%1, %2]!", 0),
    CHUNK_AT("chunk_at", "chunk at [%1]", 0),

    PLAYER_MENU_TITLE("player_menu.title","&8|----------&9%1&8----------|",0),
    PLAYER_MENU_OWNEDCHUNKS("player_menu.ownedchunks","&aOwned Chunks:",0),

    CHUNK_MENU_TITLE("chunk_menu.title","&8|----------&9%1&8----------|",0),
    CHUNK_MENU_OWNER("chunk_menu.owner","&aChunk Owner: &f%1",0),

    PERMISSIONS("permissions.check", "Permissions on %1 are [%2] for %3", 0),
    PERMS_FOR_YOU("permissions.foryou", "%1 set [%2] permissions for you on %3", 0),
    PERMISSIONS_STATUS("permissions.status","BUILD: %1 | DESTROY: %2 | ITEMUSE: %4 | SWITCH: %3",0),

    PLAYER_PERMISSIONS("permissions.player", "&aPlayer Permissions for %1: %2", 0),
    DEFAULT_PERMISSIONS("permissions.default", "&aDefault Permissions for %1:",0),
    YOUR_PERMISSIONS("permissions.yours", "&aYour permissions for %1:", 0),

    YOUR_PROPERTY("your_property", "your property", 0),
    SOMEONES_PROPERTY("someones_property", "%1's property", 0),
    THIS_CHUNK("this_chunk", "this chunk", 0),
    EVERYONE("everyone", "everyone", 0),
    THEIR_PROPERTY("their_property", "their property", 0),
    NO_ONE("no_one", "no one", 0),
    
    FEATURE_NYI("feature_nyi", "Sorry, that feature is not yet implemented.", 0),
    NO_SUCH_PLAYER("no_such_player", "There is no player named: %1", 0),

    ;

    private static List<String> deprecatedPaths = Arrays.asList(
            // Put old unused paths in here to be removed from the language file
            ""
    );


    private String path;
    private List<String> def;
    private int build;
    private static int BUILD;
    private static Configuration language;

    Language(String path, String def, int lastChangedBuild) {
        this.path = path;
        this.def = Arrays.asList(def);
        this.build = lastChangedBuild;
    }

    Language(String path, List<String> def, int lastChangedBuild) {
        this.path = path;
        this.def = def;
        this.build = lastChangedBuild;
    }

    /**
     * Retrieves the path for a config option
     *
     * @return The path for a config option
     */
    public String getPath() {
        return path;
    }

    /**
     * Retrieves the default value for a config path
     *
     * @return The default value for a config path
     */
    public List<String> getDefault() {
        return def;
    }

    /**
     * Retrieves the build number that this language was last changed on
     *
     * @return The build number this language was last changed on
     */
    public int getBuild() {
        return build;
    }

    /**
     * Loads the language data into memory and sets defaults
     *
     * @throws java.io.IOException
     */
    public static void load() throws IOException {

        // Make the data folders
        Chunky.getInstance().getDataFolder().mkdirs();

        // Check if the language file exists.  If not, create it.
        File languageFile = new File(Chunky.getInstance().getDataFolder(), Config.getLanguageFileName());
        if (!languageFile.exists()) {
            languageFile.createNewFile();
        }

        // Load the language file into memory
        language = new Configuration(languageFile);
        language.load();

        BUILD = language.getInt("last_run_build", 0);

        // Sets defaults language values
        setDefaults();
        removeDeprecatedLanguage();
        
        language.setProperty("last_run_build", Chunky.getBuildNumber());

        // Saves the configuration from memory to file
        language.save();
    }

    /**
     * Loads default settings for any missing language strings
     */
    private static void setDefaults() {
        for (Language path : Language.values()) {
            if (language.getString(path.getPath()) == null || path.getBuild() > BUILD) {
                language.setProperty(path.getPath(), path.getDefault());
            }
        }
    }

    private static void removeDeprecatedLanguage() {
        for (String path : deprecatedPaths) {
            language.removeProperty(path);
        }
    }

    private static String formatString(String string, Object...args) {
        // Replaces & with the Section character
        string = string.replaceAll("&", Character.toString((char)167));
        // If there are arguments, %n notations in the message will be
        // replaced
        if (args != null) {
            for (int j = 0; j < args.length; j++) {
                string = string.replace("%" + (j + 1), args[j].toString());
            }
        }
        return string;
    }

    /**
     * Gets a list of the messages for a given path.  Color codes will be
     * converted and any lines too long will be split into an extra element in
     * the list.  %n notated variables n the message will be replaced with the
     * optional arguments passed in.
     * 
     * @param path Path of the message in the language yaml file.
     * @param args Optional arguments to replace %n variable notations
     * @return A List of formatted Strings
     */
    public static List<String> getStrings(Language path, Object...args) {
        // Gets the messages for the path submitted
        List<Object> list = language.getList(path.getPath());

        List<String> message = new ArrayList<String>();
        if (list == null) {
            Logging.warning("Missing language for: " + path.getPath());
            return message;
        }
        // Parse each item in list
        for (int i = 0; i < list.size(); i++) {
            String temp = formatString(list.get(i).toString(), args);

            // Pass the line into the line breaker
            List<String> lines = Font.splitString(temp);
            // Add the broken up lines into the final message list to return
            for (int j = 0; j < lines.size(); j++) {
                message.add(lines.get(j));
            }
        }
        return message;
    }

    public static String getString(Language language, Object...args) {
        List<Object> list = Language.language.getList(language.getPath());
        if (list == null) {
            Logging.warning("Missing language for: " + language.getPath());
            return "";
        }
        if (list.isEmpty()) return "";
        return (formatString(list.get(0).toString(), args));
    }

    public String getString(Object...args) {
        return getString(this, args);
    }

    public void bad(CommandSender sender, Object... args) {
        send(ChatColor.RED.toString() + Language.ERROR.getString(), sender, args);
    }

    public void bad(ChunkyPlayer chunkyPlayer, Object... args) {
        try {
            bad(chunkyPlayer.getPlayer(), args);
        } catch (ChunkyPlayerOfflineException ignore) {}
    }

    public void normal(CommandSender sender, Object... args) {
        send("", sender, args);
    }

    public void normal(ChunkyPlayer chunkyPlayer, Object... args) {
        try {
            normal(chunkyPlayer.getPlayer(), args);
        } catch (ChunkyPlayerOfflineException ignore) {}
    }

    private void send(String prefix, CommandSender sender, Object... args) {
        List<String> messages = getStrings(this, args);
        for (int i = 0; i < messages.size(); i++) {
            if (i == 0) {
                sender.sendMessage(prefix + " " + messages.get(i));
            } else {
                sender.sendMessage(messages.get(i));
            }
        }
    }

    public void good(CommandSender sender, Object... args) {
        send(ChatColor.GREEN.toString() + Language.SUCCESS.getString(), sender, args);
    }

    public void good(ChunkyPlayer chunkyPlayer, Object... args) {
        try {
            good(chunkyPlayer.getPlayer(), args);
        } catch (ChunkyPlayerOfflineException ignore) {}
    }

    public void help(CommandSender sender, Object... args) {
        send(ChatColor.YELLOW.toString() + Language.HELP.getString(), sender, args);
    }

    public void help(ChunkyPlayer chunkyPlayer, Object... args) {
        try {
            help(chunkyPlayer.getPlayer(), args);
        } catch (ChunkyPlayerOfflineException ignore) {}
    }

    /**
     * Sends a custom string to a player.
     * 
     * @param chunkyPlayer
     * @param message
     * @param args
     */
    public static void sendMessage(ChunkyPlayer chunkyPlayer, String message, Object...args) {
        try {
            sendMessage(chunkyPlayer.getPlayer(), message);
        } catch (ChunkyPlayerOfflineException ignore) {}
    }

    /**
     * Sends a custom string to a player.
     *
     * @param player
     * @param message
     * @param args
     */
    public static void sendMessage(CommandSender player, String message, Object...args) {
        List<String> messages = Font.splitString(formatString(message, args));
        for (String s : messages) {
            player.sendMessage(s);
        }
    }
}