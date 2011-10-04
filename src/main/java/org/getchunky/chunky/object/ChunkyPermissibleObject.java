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
        ChunkyPermissions perms = ChunkyManager.getPermissions(object.getFullId(), this.getFullId());
        return perms.contains(type);
    }

    public final EnumSet<ChunkyPermissions.Flags> getFlags(ChunkyObject object) {
        return ChunkyManager.getPermissions(object.getFullId(), this.getFullId()).getFlags();
    }

    public final void setPerm(ChunkyObject object, ChunkyPermissions.Flags type, boolean status) {
        setPerm(object.getFullId(), type, status, true);
    }

    public final void setPerms(ChunkyObject object, EnumSet<ChunkyPermissions.Flags> flags) {
        if (flags == null) {
            ChunkyManager.getPermissions(object.getFullId(), this.getFullId()).clearFlags();
            DatabaseManager.getDatabase().removePermissions(object.getFullId(), this.getFullId());
            return;
        }

        ChunkyManager.getPermissions(object.getFullId(), this.getFullId()).setFlags(flags);
        DatabaseManager.getDatabase().updatePermissions(this.getFullId(), object.getFullId(), flags);
    }

    public final void setPerm(String objectId, ChunkyPermissions.Flags type, boolean status, boolean persist) {
        ChunkyPermissions perms = ChunkyManager.getPermissions(objectId, this.getFullId());
        perms.setFlag(type, status);
        
        // Persist if requested
        if (persist) {
            DatabaseManager.getDatabase().updatePermissions(this.getFullId(), objectId, perms.getFlags());
        }
    }
}
