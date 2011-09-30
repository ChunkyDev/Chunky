package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.config.Config;

public class DatabaseManager {

    public static Database database;

    public static boolean load() {
        if(Config.isUsingMySQL()) return loadMySQL();
        database.loadAllChunks();
        database.loadAllPlayers();
        database.loadAllPermissions();
        database.loadAllChunkOwnership();
        return false;
    }

    private static Boolean loadMySQL() {
        database = new MySQLDB();
        return database.connect(Chunky.getInstance());
    }

}
