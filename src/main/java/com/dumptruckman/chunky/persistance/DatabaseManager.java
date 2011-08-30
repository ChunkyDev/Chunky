package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.config.Config;

/**
 * @author dumptruckman, SwearWord
 */
public class DatabaseManager {

    private static Database database;

    public static Boolean load()
    {
        if(Config.isUsingMySQL()) return loadMySQL();
        else return loadSQLite();
    }

    private static Boolean loadMySQL()
    {
        database = new MySQLDB();
        if(!database.load()) return loadMySQL();
        else return true;
    }

    private static Boolean loadSQLite()
    {
        database = new SQLiteDB();
        return database.load();
    }


}
