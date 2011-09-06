package com.dumptruckman.chunky.locale;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.config.CommentedConfiguration;
import com.dumptruckman.chunky.config.Config;
import com.dumptruckman.chunky.exceptions.ChunkyPlayerOfflineException;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dumptruckman, SwearWord
 */
public enum Language {
    NO_COMMAND_PERMISSION ("command.no_permission", "You do not have permission to access this command!", ""),
    IN_GAME_ONLY ("misc.in_game_only", "Only in game players may use this feature!", ""),
    UNREGISTERED_CHUNK_NAME("misc.unregistered_chunk_name", "Wilderness", ""),

    /**
     * Command Language
     */
    CMD_CHUNKY_HELP ("command.chunky_help", "This command contains the main functions of Chunky.", ""),
    CMD_CHUNKY_CLAIM_DESC ("command.chunky_claim_desc", "Claims the chunk the user stands in.", ""),
    CMD_CHUNKY_CLAIM_HELP ("command.chunky_claim_help", "Claims the chunk you are standing in.", ""),
    CMD_CHUNKY_UNCLAIM_DESC ("command.chunky_unclaim_desc", "Unclaims the chunk the user stands in.", ""),
    CMD_CHUNKY_UNCLAIM_HELP ("command.chunky_unclaim_help", "Unclaims the chunk you are standing in.", ""),
    CMD_CHUNKY_PERMISSION_DESC ("command.chunky_permission_desc", "Allows you to set permissions for other players.", ""),
    CMD_CHUNKY_PERMISSION_HELP ("command.chunky_permission_help", "This command houses all the sub-commands necessary to set up permissions for others.", ""),

    CMD_HELP ("command.help", "Help for command %1", ""),
    CMD_LIST ("command.list", "Sub-command list for %1", ""),


    ERROR ("error", "[Error]"),
    SUCCESS ("success", "[Success]"),
    HELP ("help", "[Help]"),
    CHUNK_OWNED ("chunk.owned", "This chunk is owned by - %1", ""),
    CHUNK_NOT_OWNED("chunk.not_owned", "This chunk is not owned!"),
    CHUNK_NONE_OWNED("chunk.none_owned", "You do not own any chunks!"),
    CHUNK_LIMIT_REACHED ("chunky.limit", "You have claimed have claimed your maximum amount of chunks! (%1)", ""),
    CHUNK_CLAIMED ("chunk.claimed", "You have claimed chunk at [%1, %2]!", ""),
    CHUNK_UNCLAIMED ("chunk.unclaimed", "You have unclaimed chunk at [%1, %2]!", ""),
    ;

    private String path;
    private String def;
    private String[] comments;
    private static CommentedConfiguration language;

    Language(String path, String def, String... comments) {
        this.path = path;
        this.def = def;
        this.comments = comments;
    }

    /**
     * Retrieves the path for a config option
     * @return The path for a config option
     */
    public String getPath() {
        return path;
    }

    /**
     * Retrieves the default value for a config path
     * @return The default value for a config path
     */
    public String getDefault() {
        return def;
    }

    /**
     * Retrieves the comment for a config path
     * @return The comments for a config path
     */
    public String[] getComments() {
        if (comments != null) {
            return comments;
        }

        String[] comments = new String[1];
        comments[0] = "";
        return comments;
    }

    /**
     * Loads the language data into memory and sets defaults
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
        language = new CommentedConfiguration(languageFile);
        language.load();

        // Sets defaults language values
        setDefaults();

        // Saves the configuration from memory to file
        language.save();
    }

    /**
     * Loads default settings for any missing language strings
     */
    private static void setDefaults() {
        for (Language path : Language.values()) {
            language.addComment(path.getPath(), path.getComments());
            if (language.getString(path.getPath()) == null) {
                language.setProperty(path.getPath(), Arrays.asList(path.getDefault()));
            }
        }
        language.setHeader("# Please don't use colons in your strings! :) Enjoy the irony.");
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
     */
    public static void sendMessage(ChunkyPlayer chunkyPlayer, String message) {
        try {
            chunkyPlayer.getPlayer().sendMessage(message);
        } catch (ChunkyPlayerOfflineException ignore) {}
    }
}