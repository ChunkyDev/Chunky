package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.util.Logging;
import lib.PatPeter.SQLibrary.SQLite;

public class SQLiteDB implements Database {

    private SQLite db;
    private Chunky plugin;

    public SQLiteDB() {
        this.plugin = Chunky.getInstance();
    }

    public Boolean load() {
        Logging.info("Connecting to SQLite database.");
        this.db = new SQLite(Logging.getLog(), Logging.getNameVersion(), "data", plugin.getDataFolder().getPath());
        if(!db.open())
        {
            Logging.severe("Unable to access flatfile: " + db.name);
            return false;
        }
        checkTables();
        Logging.severe("Successfully connected to SQLite database.");
        return true;
    }

    private void checkTables() {
        if(!db.checkTable("chunky-chunks")) {
            Logging.info("Creating chunky-chunks table.");
            //TODO Figure out table stuff.
        }

        if(!db.checkTable("chunky-players")) {
            Logging.info("Creating chunky-chunks table.");
            //TODO Figure out table stuff.
        }

    }



}
