package com.dumptruckman.chunky.dynamicpersistance;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyCoordinates;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.Chunk;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public ChunkyPlayer loadChunkyPlayer(String name) {
        ChunkyPlayer chunkyPlayer = new ChunkyPlayer(name);

    }

    public ChunkyChunk loadChunk(ChunkyCoordinates coordinates) {
        ChunkyChunk chunk = new ChunkyChunk(coordinates);
        ResultSet data = query(QueryGen.selectChunk(chunk));
        if(iterateData(data))
            chunk.setName(getString(data,"name"));
        chunk.setOwner();
    }

    public List<ChunkyChunk> getOwnedChunks(ChunkyObject chunkyObject) {
        ResultSet ownedChunks = query(QueryGen.getOwned(chunkyObject,ChunkyChunk.class.getName()));
        List<ChunkyChunk>
        while (iterateData(ownedChunks)) {

        }

    }
}
