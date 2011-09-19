package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.config.Config;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.permission.ChunkyPermissions;
import com.dumptruckman.chunky.object.ChunkyPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;

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
        return database.getOwned(owner, ownableType);
    }

    public static void addOwnership(ChunkyObject owner, ChunkyObject ownable) {
        database.addOwnership(owner, ownable);
    }

    public static void removeOwnership(ChunkyObject owner, ChunkyObject ownable) {
        database.removeOwnership(owner, ownable);
    }

    public static String getTableTypeName(String type) {
        return type.substring(type.lastIndexOf(".") + 1);
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

    public static ResultSet genericQuery(String query) {
        return database.query(query);
    }

    public static void updatePermissions(String permissibleId, String objectId, EnumSet<ChunkyPermissions.Flags> flags) {
        database.updatePermissions(permissibleId, objectId, flags);
    }

    public static void updateDefaultPermissions(String permissibleId, EnumSet<ChunkyPermissions.Flags> flags) {
        database.updatePermissions(permissibleId, permissibleId, flags);
    }

    public static void removePermissions(String permissibleId, String objectId) {
        database.removePermissions(permissibleId, objectId);
    }

    public static void removeAllPermissions(String objectId) {
        database.removeAllPermissions(objectId);
    }
}
