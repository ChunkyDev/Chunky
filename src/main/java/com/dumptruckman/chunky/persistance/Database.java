package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPlayer;

import java.sql.ResultSet;

/**
 * @author dumptruckman, SwearWord
 */
public interface Database {

    //Load database
    public Boolean load();

    public ResultSet getOwned(ChunkyObject owner, int ownableType);

    public void addOwnership(ChunkyObject owner, ChunkyObject ownable);

    public void removeOwnership(ChunkyObject owner, ChunkyObject ownable);

    public void addType(int hash, String name);

    public ResultSet getTypeName(int hash);

    public void closeDB();

    public void loadData();

    public void updateChunk(ChunkyChunk chunky, String name);

    public void addPlayer(ChunkyPlayer player);
}
