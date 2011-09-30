package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.util.Logging;

public class DatabaseManager {

    public static Database database;

    public static boolean load() {
        loadMySQL();
        database.loadAllChunks();
        Logging.info("Loaded chunks.");
        database.loadAllPlayers();
        Logging.info("Loaded players.");
        database.loadAllChunkOwnership();
        Logging.info("Loaded ownership.");
        database.loadAllPermissions();
        Logging.info("Loaded permissions.");
        return true;
    }

    private static Boolean loadMySQL() {
        database = new MySQLDB();
        return database.connect(Chunky.getInstance());
    }

}
