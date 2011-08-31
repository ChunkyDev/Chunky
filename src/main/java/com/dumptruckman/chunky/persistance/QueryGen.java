package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPlayer;

public class QueryGen {

    public static String getAllPlayers() {
        return "select * from chunky-ChunkyPlayer";
    }

    public static String getOwned(ChunkyObject owner, int ownableType) {
        return
            String.format("SELECT * FROM `chunky-%s` WHERE `Hash` IN " +
            "(SELECT `OwnableHash` from `chunky-ownership` " +
            "where `OwnerHash` = %s " +
            "&& `OwnableType` = '%s' && `OwnerType` = %s)",DatabaseManager.getTypeName(ownableType),owner.hashCode(),ownableType,owner.getType());
    }

    public static String getCreateTypeTable() {
        return
            "CREATE TABLE `chunky-types` (" +
            "`Hash` INT NOT NULL," +
            "`Name` VARCHAR(50) NOT NULL," +
            "PRIMARY KEY (`Hash`) )";
    }

    public static String getCreateOwnerShipTable() {
        return
            "CREATE TABLE `chunky-ownership` (" +
            "`OwnerHash` INT NOT NULL,  " +
            "`OwnableHash` INT NOT NULL,  " +
            "`OwnerType` INT NOT NULL,  " +
            "`OwnableType` INT NOT NULL,  " +
            "PRIMARY KEY (`OwnerHash`, `OwnableHash`) )";
    }

    public static String getCreateChunkTable() {
        return
            String.format("CREATE TABLE `chunky-%s` (" +
            "`Hash` INT NOT NULL," +
            "`Name` VARCHAR(50) NOT NULL," +
            "`x` INT NOT NULL," +
            "`z` INT NOT NULL," +
            "PRIMARY KEY (`Hash`) )", DatabaseManager.getTypeName(ChunkyChunk.class.getName().hashCode()));
    }

    public static String getAddOwnership(ChunkyObject owner, ChunkyObject ownable) {
        return
            String.format("INSERT INTO `chunky-ownership` (" +
            "`OwnerHash`, " +
            "`OwnableHash`, " +
            "`OwnerType`, " +
            "`OwnableType`) " +
            "VALUES (%s,%s,%s,%s)",ownable.hashCode(), ownable.hashCode(), ownable.getType(), ownable.getType());
    }

    public static String getRemoveOwnership(ChunkyObject owner, ChunkyObject ownable) {
        return
            String.format("DELETE FROM `chunky-ownership where " +
            "`OwnerHash` = %s " +
            "&& `OwnableHash` = %s",owner.hashCode(), ownable.hashCode());

    }

    public static String getAddType(int hash, String name) {
        return String.format("INSERT INTO `chunky-types` (`Hash`, `Name`) VALUES (%s, '%s')",hash, name);
    }

    public static String getGetType(int Hash) {
        return String.format("SELECT `name` FROM chunky-types where Hash = %s",Hash);
    }

}
