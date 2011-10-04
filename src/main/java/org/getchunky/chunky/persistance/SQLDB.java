package org.getchunky.chunky.persistance;

import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyCoordinates;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.ChunkyPermissions;
import org.getchunky.chunky.util.Logging;
import org.json.JSONException;

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


    public void loadAllObjects() {
        ResultSet data = query(QueryGen.selectAllObjects());
        while(iterateData(data)) {
            ChunkyObject obj = (ChunkyObject)createObject(getString(data,"type"));
            if(obj==null) continue;
            try {
                obj.setId(getString(data, "id")).load(getString(data,"data"));
            } catch (JSONException e) {Logging.severe(e.getMessage());}
        }
    }

    public void updateObject(ChunkyObject object) {
        query(QueryGen.updateObject(object));
    }

    private Object createObject(String className) {
        Object object = null;
        try {
            Class classDefinition = Class.forName(className);
            object = classDefinition.newInstance();
            return object;
        } catch (Exception e) {
            Logging.severe(e.getMessage());
            Logging.severe("Failed to load object of type:" + className);
        }
        return null;
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
            ChunkyObject owner = ChunkyManager.getObject(getString(data, "OwnerType"),getString(data, "OwnerId"));
            ChunkyObject ownable = ChunkyManager.getObject(getString(data, "OwnableType"),getString(data,"OwnableId"));
            Logging.info(ownable.getId());
            Logging.info(owner.getId());
            if(owner==null || ownable==null) return;
            ownable.setOwner(owner,true,false);}
    }


    public void updatePermissions(String permObjectId, String objectId, EnumSet<ChunkyPermissions.Flags> flags) {
        query(QueryGen.updatePermissions(permObjectId, objectId, flags));
    }

    public void removeAllPermissions(String objectId) {
        query(QueryGen.removeAllPermissions(objectId));
    }

    public void removePermissions(String permissibleId, String objectId) {
        query(QueryGen.removePermissions(permissibleId,objectId));
    }
    public void addOwnership(ChunkyObject owner, ChunkyObject ownable) {
        query(QueryGen.addOwnership(owner, ownable));
    }

    public void removeOwnership(ChunkyObject owner, ChunkyObject ownable) {
        query(QueryGen.removeOwnership(owner, ownable));

    }

    public void updateDefaultPermissions(String id, EnumSet<ChunkyPermissions.Flags> flags) {
        query(QueryGen.updatePermissions(id,id,flags));
    }
}
