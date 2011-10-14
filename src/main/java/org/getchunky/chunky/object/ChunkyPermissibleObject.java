package org.getchunky.chunky.object;

import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.permission.PermissionFlag;
import org.getchunky.chunky.permission.PermissionRelationship;
import org.getchunky.chunky.persistance.DatabaseManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dumptruckman
 */
public abstract class ChunkyPermissibleObject extends ChunkyObject {

    public final Boolean hasPerm(ChunkyObject object, PermissionFlag type) {
        PermissionRelationship perms = ChunkyManager.getPermissions(object, this);
        return perms.hasFlag(type);
    }

    public final HashMap<PermissionFlag, Boolean> getFlags(ChunkyObject object) {
        return ChunkyManager.getPermissions(object, this).getFlags();
    }

    public final void setPerm(ChunkyObject object, PermissionFlag type, boolean status) {
        setPerm(object, type, status, true);
    }

    public final void setPerms(ChunkyObject object, HashMap<PermissionFlag, Boolean> flags) {
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

    public final void setPerm(ChunkyObject object, PermissionFlag type, boolean status, boolean persist) {
        PermissionRelationship perms = ChunkyManager.getPermissions(object, this);
        perms.setFlag(type, status);

        // Persist if requested
        if (persist) {
            DatabaseManager.getDatabase().updatePermissions(this, object, perms);
        }
    }
}
