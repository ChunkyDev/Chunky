package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyCoordinates;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.permission.ChunkyPermissions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;

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

    public void updateChunk(ChunkyChunk chunk, String name) {
        query(QueryGen.updateChunk(chunk,name));
    }

    public void updatePermissions(String permObjectId, String objectId, EnumSet<ChunkyPermissions.Flags> flags) {
        query(QueryGen.updatePermissions(permObjectId,objectId,flags));
    }

    public void removeAllPermissions(String objectId) {
        query(QueryGen.removeAllPermissions(objectId));
    }

    public void removePermissions(String permissibleId, String objectId) {
        query(QueryGen.removePermissions(permissibleId,objectId));
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
            EnumSet<ChunkyPermissions.Flags> flags = null;
            if(getInt(data,"BUILD")==1) flags.add(ChunkyPermissions.Flags.BUILD);
            if(getInt(data,"DESTROY")==1) flags.add(ChunkyPermissions.Flags.BUILD);
            if(getInt(data,"SWITCH")==1) flags.add(ChunkyPermissions.Flags.BUILD);
            if(getInt(data,"ITEM")==1) flags.add(ChunkyPermissions.Flags.BUILD);
            ChunkyManager.setPermissions(
                    getString(data,"PermissibleId"),
                    getString(data,"ObjectId"),
                    flags,false);}}

    public void loadAllChunkOwnership() {
        ResultSet data = query(QueryGen.selectAllOwnership(ChunkyPlayer.class.getName(), ChunkyChunk.class.getName()));
        while(iterateData(data)) {
            ChunkyObject owner = ChunkyManager.getObject(getString(data, "OwnerId"));
            ChunkyObject ownable = ChunkyManager.getObject(getString(data,"OwnableId"));
            if(owner==null || ownable==null) return;
            ownable.setOwner(owner,true);
        }
    }

}
