package org.getchunky.chunky.object;

import org.getchunky.chunky.persistance.DatabaseManager;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyGroup extends ChunkyPermissibleObject {

    private HashMap<String, HashSet<ChunkyObject>> members = new HashMap<String, HashSet<ChunkyObject>>();

    public HashMap<String, HashSet<ChunkyObject>> getMembers() {
        return members;
    }

    public void addMember(ChunkyObject member) {
        member._addGroup(this);
        HashSet<ChunkyObject> membersOfType = members.get(member.getType());
        if (membersOfType == null) {
            membersOfType = new HashSet<ChunkyObject>();
            members.put(member.getType(), membersOfType);
        }
        membersOfType.add(member);
        DatabaseManager.getDatabase().addGroupMember(this, member);
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
        DatabaseManager.getDatabase().removeGroupMember(this, member);
    }

    protected void _removeMember(ChunkyObject member) {
        HashSet<ChunkyObject> membersOfType = members.get(member.getType());
        if (membersOfType != null) {
            membersOfType.remove(member);
        }
    }
}
