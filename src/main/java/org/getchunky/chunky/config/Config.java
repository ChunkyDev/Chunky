package org.getchunky.chunky.config;

import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyPermissions;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.permission.PermissionFlag;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;

import static org.getchunky.chunky.config.ConfigPath.*;

/**
 * @author dumptruckman, SwearWord
 */
public class Config {

    private static Chunky plugin;
    private static CommentedConfiguration config;
    private static HashSet<String> SWITCHABLES;
    private static HashSet<String> USABLES;

    /**
     * Loads the configuration data into memory and sets defaults
     *
     * @param plugin Your module
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
        SWITCHABLES = createHashSet(getString(SWITCH_IDS));
        USABLES = createHashSet(getString(ITEM_USE_IDS));

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

    private static HashSet<String> createHashSet(String line) {
        HashSet<String> ret = new HashSet<String>();
        Collections.addAll(ret, line.split(","));
        return ret;
    }

    private static Boolean getBoolean(ConfigPath path) {
        return config.getBoolean(path.getPath(), (Boolean) path.getDefault());
    }

    private static Integer getInt(ConfigPath path) {
        return config.getInt(path.getPath(), (Integer) path.getDefault());
    }

    private static String getString(ConfigPath path) {
        return config.getString(path.getPath(), (String) path.getDefault());
    }

    /**
     * Retrieves the language file name for this module
     *
     * @return Language file name
     */
    public static String getLanguageFileName() {
        return config.getString(LANGUAGE.getPath());
    }

    /**
     * Retrieves the period at which to save the data file in seconds
     *
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

    public static Boolean isDebugging() {
        return getBoolean(DEBUG);
    }

    public static Boolean canUnowned(PermissionFlag flag) {
        if (flag.equals(ChunkyPermissions.BUILD))
            return getBoolean(UNOWNED_BUILD);
        if (flag.equals(ChunkyPermissions.DESTROY))
            return getBoolean(UNOWNED_DESTROY);
        if (flag.equals(ChunkyPermissions.ITEM_USE))
            return getBoolean(UNOWNED_SWITCH);
        if (flag.equals(ChunkyPermissions.SWITCH))
            return getBoolean(UNOWNED_SWITCH);
        return null;
    }

    public static Integer getPlayerChunkLimitDefault() {
        return getInt(PLAYER_CHUNK_LIMIT);
    }

    public static HashSet<String> getSwitchables() {
        return SWITCHABLES;
    }

    public static HashSet<String> getUsables() {
        return USABLES;
    }

    public static Boolean isChunkNamedAfterOwner() {
        return getBoolean(CHUNK_NAMED_AFTER_OWNER);
    }

    private static String getChunkNameFormat() {
        return getString(CHUNK_NAME_FORMAT_STRING);
    }

    public static String getChunkDisplayName(ChunkyChunk cChunk) {
        String nameFormat = getChunkNameFormat();
        String chunkName = cChunk.getName();
        String ownerName = "";
        String displayName = "";
        ChunkyObject chunkOwner = cChunk.getOwner();
        if (chunkOwner != null) ownerName = chunkOwner.getName();

        int begin = nameFormat.indexOf("{");
        int end = nameFormat.indexOf("}");
        if (begin > -1 && end > -1 && end > begin) {
            if (chunkName.isEmpty()) {
                nameFormat = nameFormat.replace(nameFormat.substring(begin, end + 1), "");
            } else {
                nameFormat = nameFormat.replace("{", "");
                nameFormat = nameFormat.replace("}", "");
            }
        }

        begin = nameFormat.indexOf("\\");
        end = nameFormat.indexOf("/");
        if (begin > -1 && end > -1 && end > begin) {
            if (!chunkName.isEmpty()) {
                nameFormat = nameFormat.replace(nameFormat.substring(begin, end + 1), "");
            } else {
                nameFormat = nameFormat.replace("\\", "");
                nameFormat = nameFormat.replace("/", "");
            }
        }

        displayName = Language.formatString(nameFormat, chunkName, ownerName);
        return displayName;
    }

    public static Boolean getDefaultWorldEnabled() {
        return getBoolean(WORLD_ENABLED);
    }
}
