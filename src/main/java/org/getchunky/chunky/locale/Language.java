package org.getchunky.chunky.locale;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.config.Configuration;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.config.Config;
import org.getchunky.chunky.exceptions.ChunkyPlayerOfflineException;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.util.FileTools;
import org.getchunky.chunky.util.Logging;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dumptruckman, SwearWord
 */
public enum Language {

    /**
     * Generic
     */
    IN_GAME_ONLY("generic.in_game_only"),
    ERROR("generic.error"),
    SUCCESS("generic.success"),
    HELP("generic.help"),
    INFO("generic.info"),
    YOUR_PROPERTY("generic.your_property"),
    ALL_SPECIFIC_PLAYERS("generic.all_specific_players"),
    ALL_THEIR_CURRENT_PROPERTY("generic.all_their_current_property"),
    YOUR_CURRENT_PROPERTY("generic.your_current_property"),
    SOMEONES_PROPERTY("generic.someones_property"),
    THIS_CHUNK("generic.this_chunk"),
    EVERYONE("generic.everyone"),
    THEIR_PROPERTY("generic.their_property"),
    NO_ONE("generic.no_one"),
    CHUNK_AT("generic.chunk_at"),
    FEATURE_NYI("feature_nyi"),

    /**
     * Command
     */
    NO_COMMAND_PERMISSION("command.no_permission"),
    WORLD_DISABLED("command.world_disabled"),
    CMD_HELP("command.help"),
    CMD_LIST("command.list"),
    // Chunky
    CMD_CHUNKY_DESC("command.chunky.description"),
    CMD_CHUNKY_HELP("command.chunky.help"),
    // Chunky Claim
    CMD_CHUNKY_CLAIM_DESC("command.chunky.claim.description"),
    CMD_CHUNKY_CLAIM_HELP("command.chunky.claim.help"),
    // Chunky Unclaim
    CMD_CHUNKY_UNCLAIM_DESC("command.chunky.unclaim.description"),
    CMD_CHUNKY_UNCLAIM_HELP("command.chunky.unclaim.help"),
    // Chunky Permission
    CMD_CHUNKY_PERMISSION_DESC("command.chunky.permission.description"),
    CMD_CHUNKY_PERMISSION_HELP("command.chunky.permission.help"),
    CMD_CHUNKY_PERMISSION_ADD_SUBTRACT("command.chunky.permission.add_subtract"),
    CMD_CHUNKY_PERMISSION_HELP_REMINDER("command.chunky.permission.help_reminder"),
    CMD_CHUNKY_PERMISSION_SPECIFY_FLAGS("command.chunky.permission.specify_flags"),
    // Chunky Player
    CMD_CHUNKY_PLAYER_DESC("command.chunky.player.description"),
    CMD_CHUNKY_PLAYER_HELP("command.chunky.player.help"),
    // Chunky Player Set
    CMD_CHUNKY_PLAYER_SET_DESC("command.chunky.player.set.description"),
    CMD_CHUNKY_PLAYER_SET_HELP("command.chunky.player.set.help"),
    // Chunky Player Set Mode
    CMD_CHUNKY_PLAYER_SET_MODE_DESC("command.chunky.player.set.mode.description"),
    CMD_CHUNKY_PLAYER_SET_MODE_HELP("command.chunky.player.set.mode.help"),
    // Chunky Chunk
    CMD_CHUNKY_CHUNK_DESC("command.chunky.chunk.description"),
    CMD_CHUNKY_CHUNK_HELP("command.chunky.chunk.help"),
    // Chunky Chunk Set
    CMD_CHUNKY_CHUNK_SET_DESC("command.chunky.chunk.set.description"),
    CMD_CHUNKY_CHUNK_SET_HELP("command.chunky.chunk.set.help"),
    // Chunky Chunk Set Name
    CMD_CHUNKY_CHUNK_SET_NAME_DESC("command.chunky.chunk.set.name.description"),
    CMD_CHUNKY_CHUNK_SET_NAME_HELP("command.chunky.chunk.set.name.help"),
    // Chunky Group
    CMD_CHUNKY_GROUP_DESC("command.chunky.group.description"),
    CMD_CHUNKY_GROUP_HELP("command.chunky.group.help"),
    // Chunky Group Add
    CMD_CHUNKY_GROUP_ADD_DESC("command.chunky.group.add.description"),
    CMD_CHUNKY_GROUP_ADD_HELP("command.chunky.group.add.help"),
    // Chunky Group Remove
    CMD_CHUNKY_GROUP_RM_DESC("command.chunky.group.remove.description"),
    CMD_CHUNKY_GROUP_RM_HELP("command.chunky.group.remove.help"),
    // Chunky Admin
    CMD_CHUNKY_ADMIN_HELP("command.chunky.admin.help"),
    CMD_CHUNKY_ADMIN_DESC("command.chunky.admin.description"),
    // Chunky Admin Chunklimit
    CMD_CHUNKY_ADMIN_CHUNKLIMIT_HELP("command.chunky.admin.chunk_limit.help"),
    CMD_CHUNKY_ADMIN_CHUNKLIMIT_DESC("command.chunky.admin.chunk_limit.description"),
    // Chunky Admin Chunklimit
    CMD_CHUNKY_ADMIN_ENABLEWORLD_HELP("command.chunky.admin.enable_world.help"),
    CMD_CHUNKY_ADMIN_ENABLEWORLD_DESC("command.chunky.admin.enable_world.description"),
    // Chunky Admin Chunklimit
    CMD_CHUNKY_ADMIN_DISABLEWORLD_HELP("command.chunky.admin.disable_world.help"),
    CMD_CHUNKY_ADMIN_DISABLEWORLD_DESC("command.chunky.admin.disable_world.description"),
    // Addfriend
    CMD_ADDFRIEND_HELP("command.addfriend.help"),
    // Removefriend
    CMD_RMFRIEND_HELP("command.rmfriend.help"),


    /**
     * Chunk
     */
    UNREGISTERED_CHUNK_NAME("chunk.unowned_chunk_name"),
    NO_SUCH_CHUNKS("chunk.no_such_chunks"),
    CHUNKS_NAMED("chunk.chunks_named"),
    CHUNK_OWNED("chunk.owned"),
    CHUNK_NOT_OWNED("chunk.not_owned"),
    CHUNK_NAME_CHANGED("chunk.name_changed"),
    CHUNK_NONE_OWNED("chunk.none_owned"),
    CHUNK_LIMIT_REACHED("chunk.limit"),
    CHUNK_CLAIMED("chunk.claimed"),
    CHUNK_UNCLAIMED("chunk.unclaimed"),
    // Menu
    CHUNK_MENU_TITLE("chunk.menu.title"),
    CHUNK_MENU_OWNER("chunk.menu.owner"),

    /**
     * Permission
     */
    NO_PERMISSIONS_SET("permission.not_set"),
    NO_PERMISSIONS_GRANTED("permission.none"),
    PERMISSIONS("permission.check"),
    PERMS_FOR_YOU("permission.for_you"),
    PERMISSIONS_STATUS("permission.status"),
    PLAYER_PERMISSIONS("permission.player"),
    DEFAULT_PERMISSIONS("permission.default"),
    YOUR_PERMISSIONS("permission.yours"),

    /**
     * Player
     */
    NO_SUCH_PLAYER("player.no_such_player"),
    // Menu
    PLAYER_MENU_TITLE("player.menu.title"),
    PLAYER_MENU_OWNEDCHUNKS("player.menu.owned_chunks"),
    // Mode
    PLAYER_MODE_SET("player.mode.set"),
    PLAYER_MODE_CLEAR("player.mode.clear"),

    /**
     * Group
     */
    NO_SUCH_GROUP("group.no_such_group"),
    GROUP_ADD("group.add"),
    GROUP_REMOVE("group.remove"),

    /**
     * World
     */
    NO_SUCH_WORLD("world.no_such_world"),
    ENABLED_WORLD("world.enabled_world"),
    DISABLED_WORLD("world.disabled_world"),;

    private String path;

    Language(String path) {
        this.path = path;
    }

    /**
     * Retrieves the path for a config option
     *
     * @return The path for a config option
     */
    public String getPath() {
        return path;
    }

    private static Configuration language;

    /**
     * Loads the language data into memory and sets defaults
     *
     * @throws java.io.IOException
     */
    public static void load() throws IOException {

        // Make the data folders
        Chunky.getInstance().getDataFolder().mkdirs();

        FileTools.extractFromJar("english.yml");

        // Check if the language file exists.  If not, create it.
        File languageFile = new File(Chunky.getInstance().getDataFolder(), Config.getLanguageFileName());
        if (!languageFile.exists()) {
            languageFile.createNewFile();
        }

        // Load the language file into memory
        language = new Configuration(languageFile);
        language.load();

        language.setProperty("last_run_build", Chunky.getBuildNumber());

        // Saves the configuration from memory to file
        language.save();
    }

    public static String formatString(String string, Object... args) {
        // Replaces & with the Section character
        string = string.replaceAll("&", Character.toString((char) 167));
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
    public static List<String> getStrings(Language path, Object... args) {
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

    public static String getString(Language language, Object... args) {
        List<Object> list = Language.language.getList(language.getPath());
        if (list == null) {
            Logging.warning("Missing language for: " + language.getPath());
            return "";
        }
        if (list.isEmpty()) return "";
        return (formatString(list.get(0).toString(), args));
    }

    public String getString(Object... args) {
        return getString(this, args);
    }

    public void bad(CommandSender sender, Object... args) {
        send(ChatColor.RED.toString() + Language.ERROR.getString(), sender, args);
    }

    public void bad(ChunkyPlayer chunkyPlayer, Object... args) {
        try {
            bad(chunkyPlayer.getPlayer(), args);
        } catch (ChunkyPlayerOfflineException ignore) {
        }
    }

    public void normal(CommandSender sender, Object... args) {
        send("", sender, args);
    }

    public void normal(ChunkyPlayer chunkyPlayer, Object... args) {
        try {
            normal(chunkyPlayer.getPlayer(), args);
        } catch (ChunkyPlayerOfflineException ignore) {
        }
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
        } catch (ChunkyPlayerOfflineException ignore) {
        }
    }

    public void help(CommandSender sender, Object... args) {
        send(ChatColor.YELLOW.toString() + Language.HELP.getString(), sender, args);
    }

    public void help(ChunkyPlayer chunkyPlayer, Object... args) {
        try {
            help(chunkyPlayer.getPlayer(), args);
        } catch (ChunkyPlayerOfflineException ignore) {
        }
    }

    /**
     * Sends a custom string to a player.
     *
     * @param chunkyPlayer
     * @param message
     * @param args
     */
    public static void sendMessage(ChunkyPlayer chunkyPlayer, String message, Object... args) {
        try {
            sendMessage(chunkyPlayer.getPlayer(), message, args);
        } catch (ChunkyPlayerOfflineException ignore) {
        }
    }


    /**
     * Sends a custom success string to a player.
     *
     * @param chunkyPlayer
     * @param message
     * @param args
     */
    public static void sendGood(ChunkyPlayer chunkyPlayer, String message, Object... args) {
        sendMessage(chunkyPlayer, ChatColor.GREEN.toString() + Language.SUCCESS.getString() + " " + message);
    }

    /**
     * Sends a custom fail string to a player.
     *
     * @param chunkyPlayer
     * @param message
     * @param args
     */
    public static void sendBad(ChunkyPlayer chunkyPlayer, String message, Object... args) {
        sendMessage(chunkyPlayer, ChatColor.RED.toString() + Language.ERROR.getString() + " " + message);
    }

    /**
     * Sends a custom string to a player.
     *
     * @param player
     * @param message
     * @param args
     */
    public static void sendMessage(CommandSender player, String message, Object... args) {
        List<String> messages = Font.splitString(formatString(message, args));
        for (String s : messages) {
            player.sendMessage(s);
        }
    }

    public static String combineStringArray(String[] words, String separator) {
        String string = "";
        for (String word : words) {
            if (!string.isEmpty())
                string += separator;
            string += word;
        }
        return string;
    }

    public static String combineStringList(List<String> words, String separator) {
        String string = "";
        for (String word : words) {
            if (!string.isEmpty())
                string += separator;
            string += word;
        }
        return string;
    }
}