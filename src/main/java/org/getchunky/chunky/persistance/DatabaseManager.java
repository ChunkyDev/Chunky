package org.getchunky.chunky.persistance;

import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.config.Config;
import org.getchunky.chunky.util.Logging;

public class DatabaseManager {

    private static Database database;

    public static boolean load() {
        if (Config.isUsingMySQL()) {
            if (!loadMySQL()) loadSQLite();
        } else
            loadSQLite();
        database.loadAllObjects();
        Logging.info("Loaded objects.");
        database.loadAllOwnership();
        Logging.info("Loaded ownership.");
        database.loadAllPermissions();
        Logging.info("Loaded permissions.");
        database.loadAllGroups();
        Logging.info("Loaded groups.");
        
        database.setLoaded();
        return true;
    }

    private static Boolean loadMySQL() {
        database = new MySQLDB();
        return database.connect(Chunky.getInstance());
    }

    private static Boolean loadSQLite() {
        database = new SQLiteDB();
        return database.connect(Chunky.getInstance());
    }

    public static Database getDatabase() {
        return database;
    }

}
