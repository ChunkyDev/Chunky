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

    private HashMap<String, HashSet<ChunkyObject>> members = new HashMap<String, HashSet<ChunkyObject>>();

    public HashMap<String, ChunkyPermissibleObject> getMembers() {
        try {
            if (!getData().has("members")) {
                getData().put("members", new JSONObject());
            }
            HashMap<String, ChunkyPermissibleObject> members = new HashMap<String, ChunkyPermissibleObject>();
            if (getData().getJSONObject("members").length() > 0) {
                for (int i = 0; i < getData().getJSONObject("members").names().length(); i++) {
                    String id = getData().getJSONObject("members").get(getData().getJSONObject("members").names().get(i).toString()).toString();
                    ChunkyObject object = ChunkyManager.getObject(id);
                    if (object != null)
                        members.put(getData().getJSONObject("members").names().get(i).toString(), (ChunkyGroup) object);
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
            if (!getData().has("members")) {
                getData().put("members", new JSONObject());
            }
            return getData().getJSONObject("members");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addMember(ChunkyObject member) {
        member._addGroup(this);
        HashSet<ChunkyObject> membersOfType = members.get(member.getType());
        if (membersOfType == null) {
            membersOfType = new HashSet<ChunkyObject>();
            members.put(member.getType(), membersOfType);
        }
        membersOfType.add(member);
    }

    protected void _addMember(ChunkyObject member) {
        HashSet<ChunkyObject> membersOfType = members.get(member.getType());
        if (membersOfType == null) {
            membersOfType = new HashSet<ChunkyObject>();
            members.put(member.getType(), membersOfType);
        }
        membersOfType.add(member);
    }

    public void removeMember(ChunkyObject member) {
        member._removeGroup(this);
        HashSet<ChunkyObject> membersOfType = members.get(member.getType());
        if (membersOfType != null) {
            membersOfType.remove(member);
        }
    }

    protected void _removeMember(ChunkyObject member) {
        HashSet<ChunkyObject> membersOfType = members.get(member.getType());
        if (membersOfType != null) {
            membersOfType.remove(member);
        }
    }
}
