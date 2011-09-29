package com.dumptruckman.chunky.dynamicpersistance;

import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyCoordinates;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.Chunk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SQLDB implements Database{

    public abstract ResultSet query(String query);

    private boolean iterateData(ResultSet data) {
        try {return data.next();}
        catch (SQLException e) {return false;}
    }

    private String getString(ResultSet data, String label) {
        try {return data.getString("name");}
        catch (SQLException e) {return null;}
    }

    private int getInt(ResultSet data, String label) {
        try {return data.getInt("name");}
        catch (SQLException e) {return 0;}
    }

    public void loadAllPlayers() {
        ResultSet data = query(QueryGen.selectAllPlayers());
        while (iterateData(data)) {
            ChunkyManager.getChunkyPlayer(getString(data,"name"));}
    }

    public void loadAllChunks() {
        ResultSet data = query(QueryGen.selectAllChunks());
        while (iterateData(data)) {
            ChunkyChunk chunk = ChunkyManager.getChunk(new ChunkyCoordinates(getString(data,"world"),getInt(data,"x"),getInt(data,"z")));
            chunk.setName(getString(data,"name"));}
    }

    public void loadAllPermissions() {
        ResultSet data = query(QueryGen.selectAllChunks());
        while (iterateData(data)) {
            ChunkyManager.set
        }

    }
}
