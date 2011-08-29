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
        this.db = new SQLite(Logging.getLog(), Logging.getNameVersion(), "Data", plugin.getDataFolder().getPath());
        if(!db.open()) return false;
        return true;
    }
}
