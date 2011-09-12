package com.dumptruckman.chunky.object;

import com.dumptruckman.chunky.ChunkyManager;
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
        return ChunkyManager.getPermissions(object.hashCode(), this.hashCode()).contains(type);
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

        EnumSet<ChunkyPermissions.Flags> notSet = EnumSet.complementOf(flags);
        for (ChunkyPermissions.Flags flag : flags) {
            setPerm(object, flag, true);
        }
        for (ChunkyPermissions.Flags flag : notSet) {
            setPerm(object, flag, false);
        }
    }

    public void setPerm(int object, ChunkyPermissions.Flags type, boolean status, boolean persist) {
        ChunkyManager.getPermissions(object, this.hashCode()).setFlag(type, status);
        
        // Persist if requested
        if (persist) {
            DatabaseManager.updatePermissions(this.hashCode(),object,type, status);
        }
    }
}
