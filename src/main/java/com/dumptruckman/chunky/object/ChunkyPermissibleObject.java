package com.dumptruckman.chunky.object;

import com.dumptruckman.chunky.persistance.DatabaseManager;

import java.util.EnumSet;
import java.util.HashMap;

/**
 * @author dumptruckman
 */
public class ChunkyPermissibleObject extends ChunkyObject {

    public HashMap<Integer, ChunkyPermissions> permissions = new HashMap<Integer, ChunkyPermissions>();

    public ChunkyPermissibleObject(String name) {
        super(name);
    }

    public boolean hasPerm(ChunkyPermissionsObject object, ChunkyPermissions.Flags type) {
        return permissions.containsKey(object.hashCode()) && permissions.get(object.hashCode()).contains(type);
    }

    public EnumSet<ChunkyPermissions.Flags> getFlags(ChunkyPermissionsObject object) {
        return permissions.get(object.hashCode()).flags;
    }

    public void setPerm(ChunkyPermissionsObject object, ChunkyPermissions.Flags type, boolean status) {
        ChunkyPermissions perms = permissions.get(object.hashCode());
        if (perms == null) {
            perms = new ChunkyPermissions();
            permissions.put(object.hashCode(),perms);
        }
        perms.setFlag(type, status);
        DatabaseManager.updatePermissions(this.hashCode(), object.hashCode(), type, status);
    }

    public void loadPermFromDB(int object, ChunkyPermissions.Flags type, boolean status) {
        ChunkyPermissions perms = permissions.get(object);
        if (perms == null) {
            perms = new ChunkyPermissions(ChunkyPermissions.Flags.BUILD);
            permissions.put(object,perms);
        }
        perms.setFlag(type, status);
    }
}
