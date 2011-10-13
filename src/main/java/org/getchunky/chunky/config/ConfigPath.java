package org.getchunky.chunky.config;

/**
 * @author dumptruckman, SwearWord
 */
public enum ConfigPath {
    LANGUAGE("settings.language_file", "english.yml", "# This is the language file you wish to use."),
    DATA_SAVE_PERIOD("settings.data.save_every", 30, "# This is often module data is written to the disk."),
    DEBUG("settings.debug_mode", false, "# Enables debugging mode"),

    USING_MYSQL("settings.mysql.using_mysql", true, "# True for MySQL, flat-files otherwise."),
    MYSQL_USERNAME("settings.mysql.username", "root", "# Username for MySQL database."),
    MYSQL_PASSWORD("settings.mysql.password", "password", "# Password for MySQL database."),
    MYSQL_HOST("settings.mysql.host", "localhost", "# Address for the MySQL server."),
    MYSQL_DATABASE("settings.mysql.database", "minecraft", "# Name of database to use."),
    MYSQL_PORT("settings.mysql.port", "3306", "# MySQL server port."),

    SWITCH_IDS("settings.switch_ids", "25,54,61,62,64,69,70,71,72,77,96,84,93,94", "# Switchable blocks"),
    ITEM_USE_IDS("settings.item_use_ids", "259,325,326,327,351", "# Usable items"),

    CHUNK_NAMED_AFTER_OWNER("chunks.name.named_after_owner", false, "# If true, this will default a chunk's name to the owner's name.", "# If false, a chunk's default name will be blank."),
    CHUNK_NAME_FORMAT_STRING("chunks.name.format", "&f{%1: }\\~/%2",
            "# You may use color codes here.  Precede them with a & symbol.",
            "# %1 will be substituted with the Chunk name while %2 will be substituted with the Owner's name.",
            "# Anything between {} will only be displayed if the Chunk's name IS NOT blank.",
            "# Anything between \\/ will only be displayed if the Chunk's name IS blank.",
            "# If you leave out the owner name, you may want to enable named_after_owner."),

    PLAYER_CHUNK_LIMIT("player.chunk_limit.default", 10, "# The default number of chunks a player list allowed to claim."),

    UNOWNED_BUILD("unowned.build", false, "# Can player build on unowned chunks"),
    UNOWNED_DESTROY("unowned.destroy", false, "# Can player destroy on unowned chunks"),
    UNOWNED_ITEMUSE("unowned.item_use", false, "# Can player use items on unowned chunks"),
    UNOWNED_SWITCH("unowned.switch", false, "# Can player switch on unowned chunks"),

    WORLD_ENABLED("worlds.enabled_for_new_worlds", true, "# Whether or not chunky should be enabled for new worlds or not"),;

    private String path;
    private Object def;
    private String[] comments;

    ConfigPath(String path, Object def, String... comments) {
        this.path = path;
        this.def = def;
        this.comments = comments;
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
    public Object getDefault() {
        return def;
    }

    /**
     * Retrieves the comment for a config path
     *
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
}
