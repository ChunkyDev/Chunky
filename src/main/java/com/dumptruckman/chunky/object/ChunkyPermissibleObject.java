package com.dumptruckman.chunky.object;

import com.dumptruckman.chunky.permission.ChunkyPermissions;
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

    public Boolean hasPerm(ChunkyObject object, ChunkyPermissions.Flags type) {
        try {
            return permissions.get(object.hashCode()).contains(type);
        } catch (Exception ignore) {
            return null;
        }
    }

    public EnumSet<ChunkyPermissions.Flags> getFlags(ChunkyObject object) {
        return permissions.get(object.hashCode()).getFlags();
    }

    public void setPerm(ChunkyObject object, ChunkyPermissions.Flags type, boolean status) {
        setPerm(object.hashCode(), type, status, true);
    }

    public void setPerms(ChunkyObject object, EnumSet<ChunkyPermissions.Flags> flags) {
        if (flags == null) {
            permissions.get(object.hashCode()).clearFlags();
            // TODO Need to clear the persistence for this
            return;
        }
        
        setPerms(object, flags);
        EnumSet<ChunkyPermissions.Flags> notSet = EnumSet.complementOf(flags);
        for (ChunkyPermissions.Flags flag : flags) {
            setPerm(object, flag, true);
        }
        for (ChunkyPermissions.Flags flag : notSet) {
            setPerm(object, flag, false);
        }
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
