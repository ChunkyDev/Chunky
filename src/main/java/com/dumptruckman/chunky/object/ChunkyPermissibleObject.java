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

    public boolean hasPerm(ChunkyObject object, ChunkyPermissions.Flags type) {
        return permissions.containsKey(object.hashCode()) && permissions.get(object.hashCode()).contains(type);
    }

    public EnumSet<ChunkyPermissions.Flags> getFlags(ChunkyObject object) {
        return permissions.get(object.hashCode()).flags;
    }

    public void setPerm(ChunkyObject object, ChunkyPermissions.Flags type, boolean status) {
        setPerm(object.hashCode(), type, status, true);
    }

    public void setPerm(int object, ChunkyPermissions.Flags type, boolean status, boolean persist) {
        ChunkyPermissions perms = permissions.get(object);

        // Create object if non-existant
        if (perms == null) {
            perms = new ChunkyPermissions();
            permissions.put(object, perms);
        }
        // Set flag
        perms.setFlag(type, status);

        // Persist if requested
        if (persist) {
            DatabaseManager.updatePermissions(this.hashCode(), object, type, status);
        }
    }
}
