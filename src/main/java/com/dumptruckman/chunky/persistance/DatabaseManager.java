package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.config.Config;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    public static ResultSet getOwned(ChunkyObject owner, int ownableType) {
        return database.getOwned(owner,ownableType);
    }

    public static void addOwnership(ChunkyObject owner, ChunkyObject ownable) {
        database.addOwnership(owner,ownable);
    }

    public static void removeOwnership(ChunkyObject owner, ChunkyObject ownable) {
        database.removeOwnership(owner,ownable);
    }

    public static void addType(int hash, String name) {
        database.addType(hash,name);
    }

    public static String getTypeName(int hash) {
        ResultSet results = database.getTypeName(hash);
        if(results==null) return null;
        try {
            if(!results.next()) return null;
            return results.getString("name");
        } catch (SQLException e) {
            return null;
        }

    }

    public static void closeDB() {
        database.closeDB();
    }

    public static void loadData() {
        database.loadData();
    }

    public static void addPlayer(ChunkyPlayer player) {
        database.addPlayer(player);
    }

    public static void updateChunk(ChunkyChunk chunkyChunk, String name) {
        database.updateChunk(chunkyChunk, name);
    }
}
