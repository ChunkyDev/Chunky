package com.dumptruckman.chunky.config;

import com.dumptruckman.chunky.Chunky;

import java.io.File;
import java.io.IOException;

import static com.dumptruckman.chunky.config.ConfigPath.*;

/**
 * @author dumptruckman, SwearWord
 */
public class Config {

    private static Chunky plugin;
    private static CommentedConfiguration config;

    /**
     * Loads the configuration data into memory and sets defaults
     * @param plugin Your plugin
     * @throws IOException
     */
    public static void load(Chunky plugin) throws IOException {
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

    private static Boolean getBoolean(ConfigPath path) {
        return config.getBoolean(path.getPath(), (Boolean)path.getDefault());
    }

    private static Integer getInt(ConfigPath path) {
        return config.getInt(path.getPath(), (Integer)path.getDefault());
    }

    private static String getString(ConfigPath path) {
        return config.getString(path.getPath(), (String)path.getDefault());
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
    @Deprecated
    public static int getDataSavePeriod() {
        return getInt(DATA_SAVE_PERIOD);
    }

    public static Boolean isUsingMySQL() {
        return getBoolean(USING_MYSQL);
    }

    public static String getUsername() {
        return getString(MYSQL_USERNAME);
    }

    public static String getHost() {
        return getString(MYSQL_HOST);
    }

    public static String getDatabase() {
        return getString(MYSQL_DATABASE);
    }

    public static String getPassword() {
        return getString(MYSQL_PASSWORD);
    }
    public static String getPort() {
        return getString(MYSQL_PORT);
    }

}
