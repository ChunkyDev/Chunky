package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.object.*;
import com.dumptruckman.chunky.permission.ChunkyPermissions;

import java.util.EnumSet;

public class QueryGen {

    public static String getPlayersWithOwnership() {
        return "SELECT * FROM chunky_ChunkyPlayer WHERE " +
                "Id IN " +
                "(SELECT OwnerId from chunky_ownership group by OwnerId) " +
                "OR Id IN " +
                "(SELECT PermissibleId from chunky_permissions group by PermissibleId)";
    }

    // TODO Totally unsure about this query:
    public static String getOwned(ChunkyObject owner, String ownableType) {
        return
            String.format("SELECT * FROM chunky_%s WHERE Id IN " +
            "(SELECT OwnableId from chunky_ownership " +
            "where OwnerId = %s " +
            "AND OwnableType = %s AND OwnerType = %s)", DatabaseManager.getTableTypeName(ownableType), owner.getId(), ownableType, owner.getType());
    }

    public static String getCreatePermissionsTable() {
        return
            "CREATE TABLE chunky_permissions (" +
            "PermissibleId VARCHAR(255) NOT NULL," +
            "ObjectId VARCHAR(255) NOT NULL," +
            "BUILD TINYINT NOT NULL DEFAULT 0," +
            "DESTROY TINYINT NOT NULL DEFAULT 0," +
            "ITEMUSE TINYINT NOT NULL DEFAULT 0," +
            "SWITCH TINYINT NOT NULL DEFAULT 0," +
            "PRIMARY KEY (PermissibleId, ObjectId) )";
    }

    public static String getSelectPermissions(String id) {
        return "SELECT * FROM chunky_permissions WHERE PermissibleId = " + id;
    }

    public static String getSelectDefaultPermissions(String id) {
        return "SELECT * FROM chunky_permissions WHERE PermissibleId = " + id + " AND ObjectId = " + id;
    }

    public static String getUpdatePermissions(String permissibleId, String objectId, EnumSet<ChunkyPermissions.Flags> flags) {
        int build = flags.contains(ChunkyPermissions.Flags.BUILD) ? 1:0;
        int destroy = flags.contains(ChunkyPermissions.Flags.DESTROY) ? 1:0;
        int itemuse = flags.contains(ChunkyPermissions.Flags.ITEMUSE) ? 1:0;
        int sw = flags.contains(ChunkyPermissions.Flags.SWITCH) ? 1:0;
        return
            String.format("INSERT OR REPLACE INTO chunky_permissions (" +
            "PermissibleId, " +
            "ObjectId, " +
            "BUILD," +
            "DESTROY," +
            "ITEMUSE," +
            "SWITCH) " +
            "VALUES (%s,%s,%s,%s,%s,%s)", permissibleId, objectId, build, destroy, itemuse, sw);
    }
    
    public static String getRemovePermissions(String permissibleId, String objectId) {
        return
            String.format("DELETE FROM chunky_permissions where " +
            "PermissibleId = %s " +
            "AND ObjectId = %s", permissibleId, objectId);

    }

    public static String getRemoveAllPermissions(String objectId) {
        return
            String.format("DELETE FROM chunky_permissions where " +
                    "ObjectId = %s", objectId);
    }

    public static String getCreatePlayerTable() {
        return
            "CREATE TABLE chunky_ChunkyPlayer (" +
            "Id VARCHAR(64) NOT NULL," +
            "Name VARCHAR(16) NOT NULL," +
            "PRIMARY KEY (Hash) )";
    }

    public static String getCreateOwnerShipTable() {
        return
            "CREATE TABLE chunky_ownership (" +
            "OwnerId VARCHAR(255) NOT NULL,  " +
            "OwnableId VARCHAR(255) NOT NULL,  " +
            "OwnerType VARCHAR(128) NOT NULL,  " +
            "OwnableType VARCHAR(128) NOT NULL,  " +
            "PRIMARY KEY (OwnerId, OwnableId) )";
    }

    public static String getCreateChunkTable() {
        return
            "CREATE TABLE chunky_ChunkyChunk (" +
            "Id VARCHAR(255) NOT NULL," +
            "Name VARCHAR(50) NOT NULL," +
            "World VARCHAR(50) NOT NULL," +
            "x INT NOT NULL," +
            "z INT NOT NULL," +
            "PRIMARY KEY (Id) )";
    }

    public static String getAddOwnership(ChunkyObject owner, ChunkyObject ownable) {
        return
            String.format("REPLACE INTO chunky_ownership (" +
            "OwnerId, " +
            "OwnableId, " +
            "OwnerType, " +
            "OwnableType) " +
            "VALUES (%s,%s,%s,%s)",owner.getId(), ownable.getId(), owner.getType(), ownable.getType());
    }

    public static String getRemoveOwnership(ChunkyObject owner, ChunkyObject ownable) {
        return
            String.format("DELETE FROM chunky_ownership where " +
            "OwnerId = %s " +
            "AND OwnableId = %s",owner.getId(), ownable.getId());

    }

    public static String getUpdateChunk(ChunkyChunk chunk, String name) {
        return String.format("REPLACE INTO chunky_ChunkyChunk (" +
            "Id, " +
            "Name, " +
            "World, " +
            "x, " +
            "z) " +
            "VALUES (%s,'%s','%s',%s,%s)", chunk.getId(), name, chunk.getCoord().getWorld(), chunk.getCoord().getX(), chunk.getCoord().getZ());
    }

    public static String getAddPlayer(ChunkyPlayer player) {
        return
            String.format("REPLACE INTO chunky_ChunkyPlayer (" +
            "Id, " +
            "Name) " +
            "VALUES (%s,'%s')", player.getId(), player.getName());

    }
}
