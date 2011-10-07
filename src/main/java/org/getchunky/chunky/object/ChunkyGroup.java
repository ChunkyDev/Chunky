package org.getchunky.chunky.object;

import org.getchunky.chunky.ChunkyManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyGroup extends ChunkyPermissibleObject {

    public HashMap<String, ChunkyPermissibleObject> getMembers() {
        try {
            if (!data.has("members")) {
                data.put("members", new JSONObject());
            }
            HashMap<String, ChunkyPermissibleObject> members = new HashMap<String, ChunkyPermissibleObject>();
            if (data.getJSONObject("members").length() > 0) {
                for (int i = 0; i < data.getJSONObject("members").names().length(); i++) {
                    String id = data.getJSONObject("members").get(data.getJSONObject("members").names().get(i).toString()).toString();
                    ChunkyObject object = ChunkyManager.getObject(id);
                    if (object != null)
                        members.put(data.getJSONObject("members").names().get(i).toString(), (ChunkyGroup) object);
                }
            }
            return members;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getMembersMap() {
        try {
            if (!data.has("members")) {
                data.put("members", new JSONObject());
            }
            return data.getJSONObject("members");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void addMember(ChunkyPermissibleObject object) {
        try {
            if (!data.has("members")) {
                data.put("members", new JSONObject());
            }
            data.getJSONObject("members").put(object.getName(), object.getFullId());
            this.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void removeMember(ChunkyPermissibleObject object) {
        try {
            if (data.has("members")) {
                data.getJSONObject("members").remove(object.getName());
                this.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
