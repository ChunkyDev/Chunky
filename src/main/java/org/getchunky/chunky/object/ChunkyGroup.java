package org.getchunky.chunky.object;

import org.json.JSONException;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyGroup extends ChunkyPermissibleObject {

    private HashSet<String> members = new HashSet<String>();

    public HashSet<String> getMembers() {
        @SuppressWarnings("unchecked")
        HashSet<String> members = (HashSet<String>)this.members.clone();
        return members;
    }

    protected void addMember(ChunkyPermissibleObject object) {
        members.add(object.getFullId());
        try {
            this.put("members", members);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void removeMember(ChunkyPermissibleObject object) {
        members.remove(object.getFullId());
        try {
            this.put("members", members);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
