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

    public JSONObject getGroupsMap() {
        try {
            if (!getData().has("groups")) {
                getData().put("groups", new JSONObject());
            }
            return getData().getJSONObject("groups");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<String, ChunkyGroup> getGroups() {
        try {
            if (!getData().has("groups")) {
                getData().put("groups", new JSONObject());
            }
            HashMap<String, ChunkyGroup> groups = new HashMap<String, ChunkyGroup>();
            if (getData().getJSONObject("groups").length() > 0) {
                for (int i = 0; i < getData().getJSONObject("groups").names().length(); i++) {
                    String id = getData().getJSONObject("groups").get(getData().getJSONObject("groups").names().get(i).toString()).toString();
                    ChunkyObject object = ChunkyManager.getObject(id);
                    if (object != null)
                        groups.put(getData().getJSONObject("groups").names().get(i).toString(), (ChunkyGroup) object);
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
            if (!getData().has("groups")) {
                getData().put("groups", new JSONObject());
            }
            getData().getJSONObject("groups").put(group.getName(), group.getFullId());
            this.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void removeGroup(ChunkyGroup group) {
        group.removeMember(this);
        try {
            if (getData().has("groups")) {
                getData().getJSONObject("groups").remove(group.getName());
                this.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
