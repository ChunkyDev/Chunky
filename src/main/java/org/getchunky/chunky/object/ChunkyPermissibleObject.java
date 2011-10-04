package org.getchunky.chunky.object;

import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.persistance.DatabaseManager;
import org.getchunky.chunky.permission.ChunkyPermissionCache;
import org.getchunky.chunky.permission.ChunkyPermissions;

import java.util.EnumSet;

/**
 * @author dumptruckman
 */
public abstract class ChunkyPermissibleObject extends ChunkyObject {

    public ChunkyPermissibleObject() {
    }

    public final Boolean hasPerm(ChunkyObject object, ChunkyPermissions.Flags type) {
        ChunkyPermissions perms = ChunkyManager.getPermissions(object.getId(), this.getId());
        return perms.contains(type);
    }

    public final EnumSet<ChunkyPermissions.Flags> getFlags(ChunkyObject object) {
        return ChunkyManager.getPermissions(object.getId(), this.getId()).getFlags();
    }

    public final void setPerm(ChunkyObject object, ChunkyPermissions.Flags type, boolean status) {
        setPerm(object.getId(), type, status, true);
    }

    public final void setPerms(ChunkyObject object, EnumSet<ChunkyPermissions.Flags> flags) {
        if (flags == null) {
            ChunkyManager.getPermissions(object.getId(), this.getId()).clearFlags();
            DatabaseManager.getDatabase().removePermissions(object.getId(), this.getId());
            return;
        }

        ChunkyManager.getPermissions(object.getId(), this.getId()).setFlags(flags);
        DatabaseManager.getDatabase().updatePermissions(this.getId(), object.getId(), flags);
    }

    public final void setPerm(String objectId, ChunkyPermissions.Flags type, boolean status, boolean persist) {
        ChunkyPermissions perms = ChunkyManager.getPermissions(objectId, this.getId());
        perms.setFlag(type, status);
        
        // Persist if requested
        if (persist) {
            DatabaseManager.getDatabase().updatePermissions(this.getId(), objectId, perms.getFlags());
        }
    }
}
