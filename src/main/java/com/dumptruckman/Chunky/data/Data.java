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

package com.dumptruckman.plugintemplate.data;

import com.dumptruckman.plugintemplate.ChunkyPlugin;

import com.dumptruckman.plugintemplate.config.Config;
import com.dumptruckman.plugintemplate.util.MinecraftTools;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.io.IOException;

/**
 * @author dumptruckman
 */
public class Data {

    private static ChunkyPlugin plugin;
    private static Configuration data;
    private static int dataSaveTaskId = -1;

    /**
     * Loads the language data into memory and sets defaults
     * @param plugin Your plugin
     * @throws java.io.IOException
     */
    public static void load(ChunkyPlugin plugin) throws IOException {
        Data.plugin = plugin;

        // Make the data folders
        plugin.getDataFolder().mkdirs();

        // Check if the language file exists.  If not, create it.
        File languageFile = new File(plugin.getDataFolder(), "data.yml");
        if (!languageFile.exists()) {
            languageFile.createNewFile();
        }

        // Load the language file into memory
        data = new Configuration(new File(plugin.getDataFolder(), "config.yml"));
        data.load();

        // Start the data save timer
        dataSaveTaskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin,
                new DataSaveTimer(plugin),
                MinecraftTools.convertSecondsToTicks(Config.getDataSavePeriod()),
                MinecraftTools.convertSecondsToTicks(Config.getDataSavePeriod()));
    }

    public static void save(boolean isReload) {
        if (isReload) {
            plugin.getServer().getScheduler().cancelTask(dataSaveTaskId);
            dataSaveTaskId = -1;
        }
        data.save();
    }
}
