package com.dumptruckman.chunky.locale;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.config.CommentedConfiguration;
import com.dumptruckman.chunky.config.Config;
import com.dumptruckman.chunky.exceptions.ChunkyPlayerOfflineException;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dumptruckman
 */
public class Language {

    private static Chunky plugin;
    private static CommentedConfiguration language;

    /**
     * Loads the language data into memory and sets defaults
     * @param plugin Your module
     * @throws java.io.IOException
     */
    public static void load(Chunky plugin) throws IOException {
        Language.plugin = plugin;

        // Make the data folders
        plugin.getDataFolder().mkdirs();

        // Check if the language file exists.  If not, create it.
        File languageFile = new File(plugin.getDataFolder(), Config.getLanguageFileName());
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
        for (LanguagePath path : LanguagePath.values()) {
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
     * @param path Path of the message in the language yaml file.
     * @param args Optional arguments to replace %n variable notations
     * @return A List of formatted Strings
     */
    public static List<String> getStrings(LanguagePath path, Object...args) {
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

    public static String getString(LanguagePath path, Object...args) {
        List<Object> list = language.getList(path.getPath());
        if (list == null) {
            Logging.warning("Missing language for: " + path.getPath());
            return "";
        }
        if (list.isEmpty()) return "";
        return (formatString(list.get(0).toString(), args));
    }

    public static void sendMessage(CommandSender sender, LanguagePath path, Object...args) {
        List<String> messages = getStrings(path, args);
        for (String message : messages) {
            sender.sendMessage(message);
        }
    }

    public static void sendMessage(ChunkyPlayer chunkyPlayer, LanguagePath path, Object...args) {
        try {
            sendMessage(chunkyPlayer.getPlayer(), path, args);
        } catch (ChunkyPlayerOfflineException ignore) {}
    }

    public static void sendMessage(ChunkyPlayer chunkyPlayer, String message) {
        try {
            chunkyPlayer.getPlayer().sendMessage(message);
        } catch (ChunkyPlayerOfflineException ignore) {}
    }
}
