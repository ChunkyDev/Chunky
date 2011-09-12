package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.object.*;
import com.dumptruckman.chunky.permission.ChunkyPermissions;

import java.util.EnumSet;

public class QueryGen {

    public static String getAllPlayers() {
        return "select * from chunky_ChunkyPlayer";
    }

    public static String getOwned(ChunkyObject owner, int ownableType) {
        return
            String.format("SELECT * FROM chunky_%s WHERE Hash IN " +
            "(SELECT OwnableHash from chunky_ownership " +
            "where OwnerHash = %s " +
            "AND OwnableType = %s AND OwnerType = %s)",DatabaseManager.getTypeName(ownableType),owner.hashCode(),ownableType,owner.getType());
    }

    public static String getCreateTypeTable() {
        return
            "CREATE TABLE chunky_types (" +
            "Hash INT NOT NULL," +
            "Name VARCHAR(50) NOT NULL," +
            "PRIMARY KEY (Hash) )";
    }

    public static String getCreatePermissionsTable() {
        return
            "CREATE TABLE chunky_permissions (" +
            "PermissibleHash INT NOT NULL," +
            "ObjectHash INT NOT NULL," +
            "BUILD TINYINT NOT NULL DEFAULT 0," +
            "DESTROY TINYINT NOT NULL DEFAULT 0," +
            "ITEMUSE TINYINT NOT NULL DEFAULT 0," +
            "SWITCH TINYINT NOT NULL DEFAULT 0," +
            "PRIMARY KEY (PermissibleHash, ObjectHash) )";
    }

    public static String getSelectPermissions(int hash) {
        return "SELECT * FROM chunky_permissions WHERE PermissibleHash = " + hash;
    }

    public static String getSelectDefaultPermissions(int hash) {
        return "SELECT * FROM chunky_permissions WHERE PermissibleHash = " + hash + " && ObjectHash = " + hash;
    }

    public static String getUpdatePermissions(int permissiblehash, int objecthash, EnumSet<ChunkyPermissions.Flags> flags) {
        int build = flags.contains(ChunkyPermissions.Flags.BUILD) ? 1:0;
        int destroy = flags.contains(ChunkyPermissions.Flags.DESTROY) ? 1:0;
        int itemuse = flags.contains(ChunkyPermissions.Flags.ITEMUSE) ? 1:0;
        int sw = flags.contains(ChunkyPermissions.Flags.SWITCH) ? 1:0;
        return
            String.format("INSERT OR REPLACE INTO chunky_permissions (" +
            "PermissibleHash, " +
            "ObjectHash, " +
            "BUILD," +
            "DESTROY," +
            "ITEMUSE," +
            "SWITCH) " +
            "VALUES (%s,%s,%s,%s,%s,%s)", permissiblehash, objecthash, build,destroy,itemuse,sw);
    }
    
    public static String getRemovePermissions(int permissiblehash, int objecthash) {
        return
            String.format("DELETE FROM chunky_permissions where " +
            "PermissibleHash = %s " +
            "&& ObjectHash = %s", permissiblehash, objecthash);

    }

    public static String getCreatePlayerTable() {
        return
            "CREATE TABLE chunky_ChunkyPlayer (" +
            "Hash INT NOT NULL," +
            "Name VARCHAR(16) NOT NULL," +
            "PRIMARY KEY (Hash) )";
    }

    public static String getCreateOwnerShipTable() {
        return
            "CREATE TABLE chunky_ownership (" +
            "OwnerHash INT NOT NULL,  " +
            "OwnableHash INT NOT NULL,  " +
            "OwnerType INT NOT NULL,  " +
            "OwnableType INT NOT NULL,  " +
            "PRIMARY KEY (OwnerHash, OwnableHash) )";
    }

    public static String getCreateChunkTable() {
        return
            "CREATE TABLE chunky_ChunkyChunk (" +
            "Hash INT NOT NULL," +
            "Name VARCHAR(50) NOT NULL," +
            "World VARCHAR(50) NOT NULL," +
            "x INT NOT NULL," +
            "z INT NOT NULL," +
            "PRIMARY KEY (Hash) )";
    }

    public static String getAddOwnership(ChunkyObject owner, ChunkyObject ownable) {
        return
            String.format("REPLACE INTO chunky_ownership (" +
            "OwnerHash, " +
            "OwnableHash, " +
            "OwnerType, " +
            "OwnableType) " +
            "VALUES (%s,%s,%s,%s)",owner.hashCode(), ownable.hashCode(), owner.getType(), ownable.getType());
    }

    public static String getRemoveOwnership(ChunkyObject owner, ChunkyObject ownable) {
        return
            String.format("DELETE FROM chunky_ownership where " +
            "OwnerHash = %s " +
            "&& OwnableHash = %s",owner.hashCode(), ownable.hashCode());

    }

    public static String getAddType(int hash, String name) {
        return String.format("REPLACE INTO chunky_types (Hash, Name) VALUES (%s, '%s')",hash, name);
    }

    public static String getGetType(int Hash) {
        return String.format("SELECT name FROM chunky_types where Hash = %s",Hash);
    }

    public static String getUpdateChunk(ChunkyChunk chunk, String name) {
        return String.format("REPLACE INTO chunky_ChunkyChunk (" +
            "Hash, " +
            "Name, " +
            "World, " +
            "x, " +
            "z) " +
            "VALUES (%s,'%s','%s',%s,%s)",chunk.hashCode(), name, chunk.getCoord().getWorld(), chunk.getCoord().getX(), chunk.getCoord().getZ());
    }

    public static String getAddPlayer(ChunkyPlayer player) {
        return
            String.format("REPLACE INTO chunky_ChunkyPlayer (" +
            "Hash, " +
            "Name) " +
            "VALUES (%s,'%s')",player.hashCode(), player.getName());

    }
}
