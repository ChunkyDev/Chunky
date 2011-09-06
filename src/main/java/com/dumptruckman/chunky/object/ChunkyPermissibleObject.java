package com.dumptruckman.chunky.object;

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
        }
        perms.setFlag(type, status);
    }
}
