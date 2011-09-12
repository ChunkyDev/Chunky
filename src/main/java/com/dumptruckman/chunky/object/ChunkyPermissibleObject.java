package com.dumptruckman.chunky.object;

import com.dumptruckman.chunky.permission.ChunkyPermissions;
import com.dumptruckman.chunky.persistance.DatabaseManager;

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
        return object._hasPerm(this, type);
    }

    public EnumSet<ChunkyPermissions.Flags> getFlags(ChunkyObject object) {
        return object._getFlags(this);
    }

    public void setPerm(ChunkyObject object, ChunkyPermissions.Flags type, boolean status) {
        object._setPerm(this.hashCode(), type, status, true);
    }

    public void setPerms(ChunkyObject object, EnumSet<ChunkyPermissions.Flags> flags) {
        object._setPerms(this, flags);
    }
}
