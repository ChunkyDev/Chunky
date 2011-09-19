package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.permission.ChunkyPermissions;
import com.dumptruckman.chunky.object.ChunkyPlayer;

import java.sql.ResultSet;
import java.util.EnumSet;

/**
 * @author dumptruckman, SwearWord
 */
public interface Database {

    //Load database
    public Boolean load();

    public ResultSet getOwned(ChunkyObject owner, String ownableType);

    public void addOwnership(ChunkyObject owner, ChunkyObject ownable);

    public void removeOwnership(ChunkyObject owner, ChunkyObject ownable);

    public ResultSet query(String query);

    public void closeDB();

    public void loadData();

    public void updateChunk(ChunkyChunk chunky, String name);

    public void addPlayer(ChunkyPlayer player);

    public void updatePermissions(String permissibleId, String objectId, EnumSet<ChunkyPermissions.Flags> flags);

    public void removePermissions(String permissibleId, String objectId);

    void removeAllPermissions(String objectId);
}
