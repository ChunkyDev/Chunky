package com.dumptruckman.chunky.persistance;

public class SQLstatements {

    public static String getAllPlayers() {
        return "select * from chunky-players";
    }

    public static String getOwnedChunks(String player) {
        return String.format("SELECT * FROM chunky-players WHERE chunkid IN (select chunkid from chunky-ownership where player = '%s')",player);
    }
}
