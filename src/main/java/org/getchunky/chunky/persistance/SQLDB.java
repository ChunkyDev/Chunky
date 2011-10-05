package org.getchunky.chunky.persistance;

import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.permission.ChunkyPermissions;
import org.getchunky.chunky.util.Logging;
import org.json.JSONException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;

public abstract class SQLDB implements Database {

    public abstract ResultSet query(String query);

    private boolean iterateData(ResultSet data) {
        try {
            return data.next();
        } catch (SQLException e) {
            return false;
        }
    }

    private String getString(ResultSet data, String label) {
        try {
            return data.getString(label);
        } catch (SQLException e) {
            return null;
        }
    }

    private int getInt(ResultSet data, String label) {
        try {
            return data.getInt(label);
        } catch (SQLException e) {
            return 0;
        }
    }


    public void loadAllObjects() {
        ResultSet data = query(QueryGen.selectAllObjects());
        while (iterateData(data)) {
            ChunkyObject obj = (ChunkyObject) createObject(getString(data, "type"));
            if (obj == null) continue;
            try {
                obj.setId(getString(data, "id")).load(getString(data, "data"));
            } catch (JSONException e) {
                Logging.severe(e.getMessage());
            }
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
            EnumSet<ChunkyPermissions.Flags> flags = EnumSet.noneOf(ChunkyPermissions.Flags.class);
            if (getInt(data, "BUILD") == 1) flags.add(ChunkyPermissions.Flags.BUILD);
            if (getInt(data, "DESTROY") == 1) flags.add(ChunkyPermissions.Flags.DESTROY);
            if (getInt(data, "SWITCH") == 1) flags.add(ChunkyPermissions.Flags.SWITCH);
            if (getInt(data, "ITEMUSE") == 1) flags.add(ChunkyPermissions.Flags.ITEMUSE);
            String permId = getString(data, "PermissibleId");
            String permType = getString(data, "PermissibleType");
            String objectId = getString(data, "ObjectId");
            String objectType = getString(data, "ObjectType");
            ChunkyObject object = ChunkyManager.getObject(objectType, objectId);
            ChunkyObject permObject = ChunkyManager.getObject(permType, permId);
            if (object != null && permObject != null)
                ChunkyManager.setPermissions(
                        object,
                        permObject,
                        flags, false);
        }
    }

    public void loadAllOwnership() {
        String query = QueryGen.selectAllOwnership();
        ResultSet data = query(query);
        while (iterateData(data)) {
            ChunkyObject owner = ChunkyManager.getObject(getString(data, "OwnerType"), getString(data, "OwnerId"));
            ChunkyObject ownable = ChunkyManager.getObject(getString(data, "OwnableType"), getString(data, "OwnableId"));
            if (owner == null || ownable == null) return;
            Logging.debug(ownable.getId());
            Logging.debug(owner.getId());
            ownable.setOwner(owner, true, false);
        }
    }


    public void updatePermissions(ChunkyObject permObject, ChunkyObject object, EnumSet<ChunkyPermissions.Flags> flags) {
        query(QueryGen.updatePermissions(permObject, object, flags));
    }

    public void removeAllPermissions(ChunkyObject object) {
        query(QueryGen.removeAllPermissions(object));
    }

    public void removePermissions(ChunkyObject permissible, ChunkyObject object) {
        query(QueryGen.removePermissions(permissible, object));
    }

    public void addOwnership(ChunkyObject owner, ChunkyObject ownable) {
        query(QueryGen.addOwnership(owner, ownable));
    }

    public void removeOwnership(ChunkyObject owner, ChunkyObject ownable) {
        query(QueryGen.removeOwnership(owner, ownable));

    }

    public void updateDefaultPermissions(ChunkyObject object, EnumSet<ChunkyPermissions.Flags> flags) {
        query(QueryGen.updatePermissions(object, object, flags));
    }
}
