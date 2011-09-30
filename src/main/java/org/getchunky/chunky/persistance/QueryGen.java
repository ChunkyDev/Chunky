package org.getchunky.chunky.persistance;

import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.permission.ChunkyPermissions;

import java.util.EnumSet;

public class QueryGen {
    public static String selectAllChunks() {
        return "SELECT * FROM chunky_ChunkyChunk";}

    public static String selectAllPlayers() {
        return "SELECT * FROM chunky_ChunkyPlayer";}

    public static String selectAllPermissions() {
        return "SELECT * FROM chunky_permissions";}

    public static String selectAllOwnership(String ownerType, String ownableType) {
        return String.format("SELECT * FROM chunky_ownership WHERE " +
                "OwnerType='%s' AND " +
                "OwnableType='%s'",ownerType,ownableType);
    }

    public static String selectOwnablesOfType(ChunkyObject owner, String ownableType) {
        return String.format("SELECT * FROM chunky_ownership WHERE " +
                "OwnerType='%s' AND " +
                "OwnableType='%s' AND " +
                "OwnerId='%s'",
                owner.getClass().getName(),
                ownableType,
                owner.getName());
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
            "PRIMARY KEY (PermissibleId, ObjectId) )";}

    public static String getCreatePlayerTable() {
        return
            "CREATE TABLE chunky_ChunkyPlayer (" +
            "Id VARCHAR(255) NOT NULL," +
            "Name VARCHAR(16) NOT NULL," +
            "PRIMARY KEY (Id) )";}

    public static String getCreateOwnerShipTable() {
        return
            "CREATE TABLE chunky_ownership (" +
            "OwnerId VARCHAR(255) NOT NULL,  " +
            "OwnableId VARCHAR(255) NOT NULL,  " +
            "OwnerType VARCHAR(128) NOT NULL,  " +
            "OwnableType VARCHAR(128) NOT NULL,  " +
            "PRIMARY KEY (OwnerId, OwnableId) )";}

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

    public static String updateChunk(ChunkyChunk chunk, String name) {
        return String.format("REPLACE INTO chunky_ChunkyChunk (" +
            "Id, " +
            "Name, " +
            "World, " +
            "x, " +
            "z) " +
            "VALUES ('%s','%s','%s',%s,%s)", chunk.getId(), name, chunk.getCoord().getWorld(), chunk.getCoord().getX(), chunk.getCoord().getZ());
    }

    public static String updatePermissions(String permissibleId, String objectId, EnumSet<ChunkyPermissions.Flags> flags) {
        int build = flags.contains(ChunkyPermissions.Flags.BUILD) ? 1:0;
        int destroy = flags.contains(ChunkyPermissions.Flags.DESTROY) ? 1:0;
        int itemuse = flags.contains(ChunkyPermissions.Flags.ITEMUSE) ? 1:0;
        int sw = flags.contains(ChunkyPermissions.Flags.SWITCH) ? 1:0;
        return
            String.format("REPLACE INTO chunky_permissions (" +
            "PermissibleId, " +
            "ObjectId, " +
            "BUILD," +
            "DESTROY," +
            "ITEMUSE," +
            "SWITCH) " +
            "VALUES ('%s','%s',%s,%s,%s,%s)", permissibleId, objectId, build, destroy, itemuse, sw);
    }

    public static String removePermissions(String permissibleId, String objectId) {
        return
            String.format("DELETE FROM chunky_permissions where " +
            "PermissibleId = '%s' " +
            "AND ObjectId = '%s'", permissibleId, objectId);
    }

    public static String removeAllPermissions(String objectId) {
        return
            String.format("DELETE FROM chunky_permissions where " +
                    "ObjectId = '%s'", objectId);
    }

     public static String addOwnership(ChunkyObject owner, ChunkyObject ownable) {
        return
            String.format("REPLACE INTO chunky_ownership (" +
            "OwnerId, " +
            "OwnableId, " +
            "OwnerType, " +
            "OwnableType) " +
            "VALUES ('%s','%s','%s','%s')",owner.getId(), ownable.getId(), owner.getType(), ownable.getType());
    }

    public static String removeOwnership(ChunkyObject owner, ChunkyObject ownable) {
        return
            String.format("DELETE FROM chunky_ownership where " +
            "OwnerId = '%s' " +
            "AND OwnableId = '%s'",owner.getId(), ownable.getId());

    }
}
