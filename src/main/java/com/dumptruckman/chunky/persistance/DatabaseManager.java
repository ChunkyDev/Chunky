package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.config.Config;
import com.dumptruckman.chunky.object.ChunkyObject;

import java.sql.ResultSet;

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
        if(!database.load()) return loadSQLite();
        else return true;
    }

    private static Boolean loadSQLite() {
        database = new SQLiteDB();
        return database.load();
    }

    public static ResultSet getOwned(ChunkyObject owner, String ownableType) {
        return database.getOwned(owner,ownableType);
    }

    public static void addOwnership(ChunkyObject owner, ChunkyObject ownable) {
        database.addOwnership(owner,ownable);
    }

    public static void removeOwnership(ChunkyObject owner, ChunkyObject ownable) {
        database.removeOwnership(owner,ownable);
    }


}
