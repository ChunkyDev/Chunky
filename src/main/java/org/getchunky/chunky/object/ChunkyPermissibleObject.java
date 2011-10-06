package org.getchunky.chunky.object;

import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.permission.ChunkyPermissions;
import org.getchunky.chunky.persistance.DatabaseManager;
import org.getchunky.chunky.util.Logging;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.EnumSet;
import java.util.HashMap;

/**
 * @author dumptruckman
 */
public abstract class ChunkyPermissibleObject extends ChunkyObject {

    public final Boolean hasPerm(ChunkyObject object, ChunkyPermissions.Flags type) {
        ChunkyPermissions perms = ChunkyManager.getPermissions(object, this);
        return perms.contains(type);
    }

    public final EnumSet<ChunkyPermissions.Flags> getFlags(ChunkyObject object) {
        return ChunkyManager.getPermissions(object, this).getFlags();
    }

    public final void setPerm(ChunkyObject object, ChunkyPermissions.Flags type, boolean status) {
        setPerm(object, type, status, true);
    }

    public final void setPerms(ChunkyObject object, EnumSet<ChunkyPermissions.Flags> flags) {
        if (flags == null) {
            ChunkyManager.getPermissions(object, this).clearFlags();
            DatabaseManager.getDatabase().removePermissions(object, this);
            return;
        }

        ChunkyManager.getPermissions(object, this).setFlags(flags);
        DatabaseManager.getDatabase().updatePermissions(this, object, flags);
    }

    public final void setPerm(ChunkyObject object, ChunkyPermissions.Flags type, boolean status, boolean persist) {
        ChunkyPermissions perms = ChunkyManager.getPermissions(object, this);
        perms.setFlag(type, status);

        // Persist if requested
        if (persist) {
            DatabaseManager.getDatabase().updatePermissions(this, object, perms.getFlags());
        }
    }

    public JSONObject getGroupsMap() {
        try {
            if (!this.has("groups")) {
                this.put("groups", new JSONObject());
            }
            return this.getJSONObject("groups");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<String, ChunkyGroup> getGroups() {
        try {
            if (!this.has("groups")) {
                this.put("groups", new JSONObject());
            }
            HashMap<String, ChunkyGroup> groups = new HashMap<String, ChunkyGroup>();
            if (this.getJSONObject("groups").length() > 0) {
                for (int i = 0; i < this.getJSONObject("groups").names().length(); i++) {
                    String id = this.getJSONObject("groups").get(this.getJSONObject("groups").names().get(i).toString()).toString();
                    ChunkyObject object = ChunkyManager.getObject(id);
                    if (object != null)
                        groups.put(this.getJSONObject("groups").names().get(i).toString(), (ChunkyGroup) object);
                }
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
            if (!this.has("groups")) {
                this.put("groups", new JSONObject());
            }
            this.getJSONObject("groups").put(group.getName(), group.getFullId());
            this.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void removeGroup(ChunkyGroup group) {
        group.removeMember(this);
        try {
            if (this.has("groups")) {
                this.getJSONObject("groups").remove(group.getName());
                this.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
