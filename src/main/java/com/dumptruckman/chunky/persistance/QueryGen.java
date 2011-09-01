package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPlayer;

public class QueryGen {

    public static String getAllPlayers() {
        return "select * from chunky_ChunkyPlayer";
    }

    public static String getOwned(ChunkyObject owner, int ownableType) {
        return
            String.format("SELECT * FROM chunky_%s WHERE Hash IN " +
            "(SELECT OwnableHash from chunky_ownership " +
            "where OwnerHash = %s " +
            "&& OwnableType = %s && OwnerType = %s)",DatabaseManager.getTypeName(ownableType),owner.hashCode(),ownableType,owner.getType());
    }

    public static String getCreateTypeTable() {
        return
            "CREATE TABLE chunky_types (" +
            "Hash INT NOT NULL," +
            "Name VARCHAR(50) NOT NULL," +
            "PRIMARY KEY (Hash) )";
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
            String.format("INSERT INTO chunky_ownership (" +
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
        return String.format("INSERT INTO chunky_types (Hash, Name) VALUES (%s, '%s')",hash, name);
    }

    public static String getGetType(int Hash) {
        return String.format("SELECT name FROM chunky_types where Hash = %s",Hash);
    }

    public static String getAddChunk(ChunkyChunk chunk) {
        return String.format("INSERT INTO chunky_ChunkyChunk (" +
            "Hash, " +
            "Name, " +
            "World, " +
            "x, " +
            "z) " +
            "VALUES (%s,'%s','%s',%s,%s)",chunk.hashCode(), chunk.getName(), chunk.getCoord().getWorld(), chunk.getCoord().getX(), chunk.getCoord().getZ());
    }

    public static String getAddPlayer(ChunkyPlayer player) {
        return
            String.format("INSERT INTO chunky_ChunkyPlayer (" +
            "Hash, " +
            "Name) " +
            "VALUES (%s,'%s')",player.hashCode(), player.getName());

    }
}
