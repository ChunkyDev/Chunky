package com.dumptruckman.chunky.object;

import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.permission.ChunkyPermissions;
import com.dumptruckman.chunky.persistance.DatabaseManager;
import com.dumptruckman.chunky.util.Logging;

import java.util.EnumSet;
import java.util.HashMap;

/**
 * @author dumptruckman
 */
public class ChunkyPermissibleObject extends ChunkyObject {

    public ChunkyPermissibleObject(String name) {
        super(name);
    }

    public Boolean hasPerm(ChunkyObject object, ChunkyPermissions.Flags type) {
        Logging.debug(object.toString());
        ChunkyPermissions perms = ChunkyManager.getPermissions(object.hashCode(), this.hashCode());
        Logging.debug(this + ".hasPerm(" + object + ", " + type + ")  perms: " + perms + " .contains(): " + perms.contains(type));
        return perms.contains(type);
    }

    public EnumSet<ChunkyPermissions.Flags> getFlags(ChunkyObject object) {
        return ChunkyManager.getPermissions(object.hashCode(), this.hashCode()).getFlags();
    }

    public void setPerm(ChunkyObject object, ChunkyPermissions.Flags type, boolean status) {
        setPerm(object.hashCode(), type, status, true);
    }

    public void setPerms(ChunkyObject object, EnumSet<ChunkyPermissions.Flags> flags) {
        if (flags == null) {
            ChunkyManager.getPermissions(object.hashCode(), this.hashCode()).clearFlags();
            DatabaseManager.removePermissions(object.hashCode(), this.hashCode());
            return;
        }

        ChunkyManager.getPermissions(object.hashCode(), this.hashCode()).setFlags(flags);
        DatabaseManager.updatePermissions(this.hashCode(), object.hashCode(), flags);
    }

    public void setPerm(int object, ChunkyPermissions.Flags type, boolean status, boolean persist) {
        ChunkyPermissions perms = ChunkyManager.getPermissions(object, this.hashCode());
        perms.setFlag(type, status);
        
        // Persist if requested
        if (persist) {
            DatabaseManager.updatePermissions(this.hashCode(), object, perms.getFlags());
        }
    }
}
