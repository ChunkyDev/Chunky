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

package com.dumptruckman.chunky.locale;

import com.dumptruckman.chunky.ChunkyPlugin;
import com.dumptruckman.chunky.config.CommentedConfiguration;
import com.dumptruckman.chunky.config.Config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dumptruckman
 */
public class PluginLanguage {

    private static ChunkyPlugin plugin;
    private static CommentedConfiguration language;

    /**
     * Loads the language data into memory and sets defaults
     * @param plugin Your plugin
     * @throws java.io.IOException
     */
    public static void load(ChunkyPlugin plugin) throws IOException {
        PluginLanguage.plugin = plugin;

        // Make the data folders
        plugin.getDataFolder().mkdirs();

        // Check if the language file exists.  If not, create it.
        File languageFile = new File(plugin.getDataFolder(), Config.getLanguageFileName());
        if (!languageFile.exists()) {
            languageFile.createNewFile();
        }

        // Load the language file into memory
        language = new CommentedConfiguration(new File(plugin.getDataFolder(), "config.yml"));
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
                language.setProperty(path.getPath(), path.getDefault());
            }
        }
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
    public List<String> getStrings(String path, String... args) {
        // Gets the messages for the path submitted
        List<Object> list = language.getList(path);

        List<String> message = new ArrayList<String>();
        // Parse each item in list
        for (int i = 0; i < list.size(); i++) {
            String temp = list.get(i).toString();
            // Replaces & with the Section character
            temp = temp.replaceAll("&", Character.toString((char)167));
            // If there are arguments, %n notations in the message will be
            // replaced
            if (args != null) {
                for (int j = 0; j < args.length; j++) {
                    temp = temp.replace("%" + (j + 1), args[j]);
                }
            }
            // Pass the line into the line breaker
            List<String> lines = Font.splitString(temp);
            // Add the broken up lines into the final message list to return
            for (int j = 0; j < lines.size(); j++) {
                message.add(lines.get(j));
            }
        }
        return message;
    }
}
