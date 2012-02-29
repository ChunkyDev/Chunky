package org.getchunky.chunkie.object;

import org.getchunky.chunkie.ChunkyManager;
import org.getchunky.chunkie.permission.PermissionFlag;
import org.getchunky.chunkie.permission.PermissionRelationship;
import org.getchunky.chunkie.persistance.DatabaseManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dumptruckman
 */
public abstract class IChunkyPermissibleObject extends IChunkyObject {

    public final Boolean hasPerm(IChunkyObject object, PermissionFlag type) {
        PermissionRelationship perms = ChunkyManager.getPermissions(object, this);
        return perms.hasFlag(type);
    }

    public final HashMap<PermissionFlag, Boolean> getFlags(IChunkyObject object) {
        return ChunkyManager.getPermissions(object, this).getFlags();
    }

    public final void setPerm(IChunkyObject object, PermissionFlag type, boolean status) {
        setPerm(object, type, status, true);
    }

    public final void setPerms(IChunkyObject object, HashMap<PermissionFlag, Boolean> flags) {
        if (flags == null) {
            ChunkyManager.getPermissions(object, this).clearFlags();
            DatabaseManager.getDatabase().removePermissions(object, this);
            return;
        }

        PermissionRelationship perms = ChunkyManager.getPermissions(object, this);
        for (Map.Entry<PermissionFlag, Boolean> flag : flags.entrySet()) {
            setPerm(object, flag.getKey(), flag.getValue());
        }
        DatabaseManager.getDatabase().updatePermissions(this, object, perms);
    }

    public final void setPerm(IChunkyObject object, PermissionFlag type, boolean status, boolean persist) {
        PermissionRelationship perms = ChunkyManager.getPermissions(object, this);
        perms.setFlag(type, status);

        // Persist if requested
        if (persist) {
            DatabaseManager.getDatabase().updatePermissions(this, object, perms);
        }
    }
}
