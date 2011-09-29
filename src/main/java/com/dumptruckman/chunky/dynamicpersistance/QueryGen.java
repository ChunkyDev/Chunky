package com.dumptruckman.chunky.dynamicpersistance;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.persistance.*;

public class QueryGen {
    public static String selectChunk(ChunkyChunk chunk) {
        return String.format(
                "SELECT * FROM chunky_ChunkyChunk WHERE Id='%s'",chunk.getId());}

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
            "PRIMARY KEY (Id) )";}

    public static String getOwned(ChunkyObject owner, String ownableType) {
        return
            String.format("SELECT * FROM chunky_%s WHERE Id IN " +
            "(SELECT OwnableId from chunky_ownership " +
            "where OwnerId = '%s' " +
            "AND OwnableType = '%s' AND OwnerType = '%s')", com.dumptruckman.chunky.persistance.DatabaseManager.getTableTypeName(ownableType), owner.getId(), ownableType, owner.getType());}

}
