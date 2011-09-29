package com.dumptruckman.chunky.dynamicpersistance;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.config.Config;
import org.bukkit.plugin.Plugin;

public class DatabaseManager {

    public static Database database;

    public static boolean load() {
        if(Config.isUsingMySQL()) return loadMySQL();
        return false;
    }

    private static Boolean loadMySQL() {
        database = new MySQLDB();
        return database.connect(Chunky.getInstance());
    }

}
