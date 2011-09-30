package org.getchunky.chunky.persistance;

import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyCoordinates;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.ChunkyPermissions;
import org.getchunky.chunky.util.Logging;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public abstract class SQLDB implements Database{

    public abstract ResultSet query(String query);

    private boolean iterateData(ResultSet data) {
        try {return data.next();}
        catch (SQLException e) {return false;}
    }

    private String getString(ResultSet data, String label) {
        try {return data.getString(label);}
        catch (SQLException e) {return null;}
    }

    private int getInt(ResultSet data, String label) {
        try {return data.getInt(label);}
        catch (SQLException e) {return 0;}
    }

    public void updateChunk(ChunkyChunk chunk, String name) {
        query(QueryGen.updateChunk(chunk,name));
    }

    public void updatePermissions(String permObjectId, String objectId, EnumSet<ChunkyPermissions.Flags> flags) {
        query(QueryGen.updatePermissions(permObjectId,objectId,flags));
    }

    public void removeAllPermissions(String objectId) {
        Logging.info("REMOVING");
        query(QueryGen.removeAllPermissions(objectId));
    }

    public void removePermissions(String permissibleId, String objectId) {
        Logging.info("REMOVING");
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
        ResultSet data = query(QueryGen.selectAllPermissions());
        while (iterateData(data)) {
            EnumSet<ChunkyPermissions.Flags> flags =EnumSet.noneOf(ChunkyPermissions.Flags.class);
            if(getInt(data,"BUILD")==1) flags.add(ChunkyPermissions.Flags.BUILD);
            if(getInt(data,"DESTROY")==1) flags.add(ChunkyPermissions.Flags.DESTROY);
            if(getInt(data,"SWITCH")==1) flags.add(ChunkyPermissions.Flags.SWITCH);
            if(getInt(data,"ITEMUSE")==1) flags.add(ChunkyPermissions.Flags.ITEMUSE);
            String permId = getString(data,"PermissibleId");
            String objectId = getString(data,"ObjectId");;
            ChunkyManager.setPermissions(
                    objectId,
                    permId,
                    flags,false);}}

    public void loadAllChunkOwnership() {
        String query = QueryGen.selectAllOwnership(ChunkyPlayer.class.getName(), ChunkyChunk.class.getName());
        ResultSet data = query(query);
        while(iterateData(data)) {
            ChunkyObject owner = ChunkyManager.getObject(getString(data, "OwnerId"));
            ChunkyObject ownable = ChunkyManager.getObject(getString(data,"OwnableId"));
            if(owner==null || ownable==null) return;
            ownable.setOwner(owner,true,false);
        }
    }

    public void addOwnership(ChunkyObject owner, ChunkyObject ownable) {
        query(QueryGen.addOwnership(owner,ownable));
    }

    public void removeOwnership(ChunkyObject owner, ChunkyObject ownable) {
        query(QueryGen.removeOwnership(owner,ownable));

    }

    public void updateDefaultPermissions(String id, EnumSet<ChunkyPermissions.Flags> flags) {
        query(QueryGen.updatePermissions(id,id,flags));
    }

    public List<String> getOwnablesOfType(ChunkyObject owner, String type) {
        ResultSet data = query(QueryGen.selectOwnablesOfType(owner,type));
        List<String> result = new ArrayList<String>();
        while(iterateData(data))
            result.add(getString(data,"OwnableID"));
        return result;
    }
}
