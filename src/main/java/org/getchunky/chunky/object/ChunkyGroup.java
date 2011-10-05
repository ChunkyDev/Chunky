package org.getchunky.chunky.object;

import org.getchunky.chunky.ChunkyManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyGroup extends ChunkyPermissibleObject {

    public HashMap<String, ChunkyPermissibleObject> getMembers() {
        try {
            HashMap<String, ChunkyPermissibleObject> members = new HashMap<String, ChunkyPermissibleObject>();
            for (int i = 0; i < this.getJSONObject("members").names().length(); i++) {
                String id = this.getJSONObject("members").get(this.getJSONObject("members").names().get(i).toString()).toString();
                ChunkyObject object = ChunkyManager.getObject(id);
                if (object != null)
                    members.put(this.getJSONObject("members").names().get(i).toString(), (ChunkyGroup)object);
            }
            return members;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getMembersMap() {
        try {
            return this.getJSONObject("members");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void addMember(ChunkyPermissibleObject object) {
        try {
            this.getJSONObject("members").put(object.getName(), object.getFullId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void removeMember(ChunkyPermissibleObject object) {
        try {
            this.getJSONObject("members").remove(object.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
