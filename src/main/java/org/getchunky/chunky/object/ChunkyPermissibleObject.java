package org.getchunky.chunky.object;

import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.permission.PermissionFlag;
import org.getchunky.chunky.permission.PermissionRelationship;
import org.getchunky.chunky.persistance.DatabaseManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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
        perms.setFlags(flags);
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

    public JSONObject getGroupsMap() {
        try {
            if (!data.has("groups")) {
                data.put("groups", new JSONObject());
            }
            return data.getJSONObject("groups");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<String, ChunkyGroup> getGroups() {
        try {
            if (!data.has("groups")) {
                data.put("groups", new JSONObject());
            }
            HashMap<String, ChunkyGroup> groups = new HashMap<String, ChunkyGroup>();
            if (data.getJSONObject("groups").length() > 0) {
                for (int i = 0; i < data.getJSONObject("groups").names().length(); i++) {
                    String id = data.getJSONObject("groups").get(data.getJSONObject("groups").names().get(i).toString()).toString();
                    ChunkyObject object = ChunkyManager.getObject(id);
                    if (object != null)
                        groups.put(data.getJSONObject("groups").names().get(i).toString(), (ChunkyGroup) object);
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
            if (!data.has("groups")) {
                data.put("groups", new JSONObject());
            }
            data.getJSONObject("groups").put(group.getName(), group.getFullId());
            this.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void removeGroup(ChunkyGroup group) {
        group.removeMember(this);
        try {
            if (data.has("groups")) {
                data.getJSONObject("groups").remove(group.getName());
                this.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
