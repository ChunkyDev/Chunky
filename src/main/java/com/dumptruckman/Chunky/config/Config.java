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

package com.dumptruckman.plugintemplate.config;

import com.dumptruckman.plugintemplate.ChunkyPlugin;

import java.io.File;
import java.io.IOException;

import static com.dumptruckman.plugintemplate.config.ConfigPath.*;

/**
 * @author dumptruckman
 */
public class Config {

    private static ChunkyPlugin plugin;
    private static CommentedConfiguration config;

    /**
     * Loads the configuration data into memory and sets defaults
     * @param plugin Your plugin
     * @throws IOException
     */
    public static void load(ChunkyPlugin plugin) throws IOException {
        Config.plugin = plugin;

        // Make the data folders
        plugin.getDataFolder().mkdirs();

        // Check if the config file exists.  If not, create it.
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.createNewFile();
        }

        // Load the configuration file into memory
        config = new CommentedConfiguration(new File(plugin.getDataFolder(), "config.yml"));
        config.load();

        // Sets defaults config values
        setDefaults();

        // Saves the configuration from memory to file
        config.save();
    }

    /**
     * Loads default settings for any missing config values
     */
    private static void setDefaults() {
        for (ConfigPath path : ConfigPath.values()) {
            config.addComment(path.getPath(), path.getComments());
            if (config.getString(path.getPath()) == null) {
                config.setProperty(path.getPath(), path.getDefault());
            }
        }
    }

    /**
     * Retrieves the language file name for this plugin
     * @return Language file name
     */
    public static String getLanguageFileName() {
        return config.getString(LANGUAGE.getPath());
    }

    /**
     * Retrieves the period at which to save the data file in seconds
     * @return Period to save data file
     */
    public static int getDataSavePeriod() {
        return config.getInt(DATA_SAVE_PERIOD.getPath(), (Integer)DATA_SAVE_PERIOD.getDefault());
    }
}
