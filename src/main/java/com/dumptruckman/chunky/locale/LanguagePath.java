package com.dumptruckman.chunky.locale;

/**
 * @author dumptruckman, SwearWord
 */
public enum LanguagePath {
    NO_COMMAND_PERMISSION ("no_cmd_permission", "You do not have permission to access this command!", ""),
    IN_GAME_ONLY ("in_game_only", "Only in game players may use this feature!", ""),
    UNREGISTERED_CHUNK_NAME("unregistered_chunk_name", "Wilderness", ""),
    CMD_CHUNKY_HELP ("cmd_chunky_help", "This command contains the main functions of Chunky.", ""),
    CMD_CHUNKY_CLAIM_DESC ("cmd_chunky_claim_desc", "This command contains the main functions of Chunky.", ""),
    CMD_CHUNKY_CLAIM_HELP ("cmd_chunky_claim_help", "This command contains the main functions of Chunky.", ""),
    CMD_HELP ("cmd_help", "Help for command: %1", ""),
    CMD_LIST ("cmd_list", "Sub-command list for: %1", ""),
    ;

    private String path;
    private String def;
    private String[] comments;

    LanguagePath(String path, String def, String...comments) {
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
}