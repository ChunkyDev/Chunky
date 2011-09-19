package com.dumptruckman.chunky.object;

import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.permission.ChunkyPermissions;
import com.dumptruckman.chunky.persistance.DatabaseManager;

import java.util.EnumSet;

/**
 * @author dumptruckman
 */
public class ChunkyPermissibleObject extends ChunkyObject {

    public ChunkyPermissibleObject(String id) {
        super(id);
    }

    public Boolean hasPerm(ChunkyObject object, ChunkyPermissions.Flags type) {
        ChunkyPermissions perms = ChunkyManager.getPermissions(object.getId(), this.getId());
        return perms.contains(type);
    }

    public EnumSet<ChunkyPermissions.Flags> getFlags(ChunkyObject object) {
        return ChunkyManager.getPermissions(object.getId(), this.getId()).getFlags();
    }

    public void setPerm(ChunkyObject object, ChunkyPermissions.Flags type, boolean status) {
        setPerm(object.getId(), type, status, true);
    }

    public void setPerms(ChunkyObject object, EnumSet<ChunkyPermissions.Flags> flags) {
        if (flags == null) {
            ChunkyManager.getPermissions(object.getId(), this.getId()).clearFlags();
            DatabaseManager.removePermissions(object.getId(), this.getId());
            return;
        }

        ChunkyManager.getPermissions(object.getId(), this.getId()).setFlags(flags);
        DatabaseManager.updatePermissions(this.getId(), object.getId(), flags);
    }

    public void setPerm(String objectId, ChunkyPermissions.Flags type, boolean status, boolean persist) {
        ChunkyPermissions perms = ChunkyManager.getPermissions(objectId, this.getId());
        perms.setFlag(type, status);
        
        // Persist if requested
        if (persist) {
            DatabaseManager.updatePermissions(this.getId(), objectId, perms.getFlags());
        }
    }
}
