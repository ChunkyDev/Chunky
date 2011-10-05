package org.getchunky.chunky.object;

import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.persistance.DatabaseManager;
import org.getchunky.chunky.permission.ChunkyPermissionCache;
import org.getchunky.chunky.permission.ChunkyPermissions;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author dumptruckman
 */
public abstract class ChunkyPermissibleObject extends ChunkyObject {

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

    public JSONObject getGroupsMap() {
        try {
            return this.getJSONObject("groups");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<String, ChunkyGroup> getGroups() {
        try {
            HashMap<String, ChunkyGroup> groups = new HashMap<String, ChunkyGroup>();
            for (int i = 0; i < this.getJSONObject("groups").names().length(); i++) {
                String id = this.getJSONObject("groups").get(this.getJSONObject("groups").names().get(i).toString()).toString();
                ChunkyObject object = ChunkyManager.getObject(id);
                if (object != null)
                    groups.put(this.getJSONObject("groups").names().get(i).toString(), (ChunkyGroup)object);
            }
            return groups;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addGroup(ChunkyGroup group) {
        group.addMember(this);
        try {
            this.getJSONObject("groups").put(group.getName(), group.getFullId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void removeGroup(ChunkyGroup group) {
        group.removeMember(this);
        try {
            this.getJSONObject("groups").remove(group.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
