package com.dumptruckman.chunky.locale;

/**
 * @author dumptruckman, SwearWord
 */
public enum LanguagePath {
    NO_COMMAND_PERMISSION ("command.no_permission", "You do not have permission to access this command!", ""),
    IN_GAME_ONLY ("misc.in_game_only", "Only in game players may use this feature!", ""),
    UNREGISTERED_CHUNK_NAME("misc.unregistered_chunk_name", "Wilderness", ""),
    CMD_CHUNKY_HELP ("command.chunky_help", "This command contains the main functions of Chunky.", ""),
    CMD_CHUNKY_CLAIM_DESC ("command.chunky_claim_desc", "Claims the chunk the user stands in.", ""),
    CMD_CHUNKY_CLAIM_HELP ("command.chunky_claim_help", "Claims the chunk you are standing in.", ""),
    CMD_CHUNKY_UNCLAIM_DESC ("command.chunky_unclaim_desc", "Unclaims the chunk the user stands in.", ""),
    CMD_CHUNKY_UNCLAIM_HELP ("command.chunky_unclaim_help", "Unclaims the chunk you are standing in.", ""),
    CMD_HELP ("command.help", "Help for command %1", ""),
    CMD_LIST ("command.list", "Sub-command list for %1", ""),
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