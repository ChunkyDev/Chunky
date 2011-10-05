package org.getchunky.chunky.object;

import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.persistance.DatabaseManager;
import org.getchunky.chunky.permission.ChunkyPermissionCache;
import org.getchunky.chunky.permission.ChunkyPermissions;
import org.json.JSONException;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author dumptruckman
 */
public abstract class ChunkyPermissibleObject extends ChunkyObject {

    private HashSet<String> groups = new HashSet<String>();

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

    public HashSet<String> getGroups() {
        @SuppressWarnings("unchecked")
        HashSet<String> groups = (HashSet<String>)this.groups.clone();
        return groups;
    }

    public void addGroup(ChunkyGroup group) {
        group.addMember(this);
        groups.add(group.getFullId());
        try {
            this.put("groups", groups);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void removeGroup(ChunkyGroup group) {
        group.removeMember(this);
        groups.remove(group.getFullId());
        try {
            this.put("groups", groups);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
