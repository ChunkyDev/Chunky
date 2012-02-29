package org.getchunky.chunkie.object;

import org.getchunky.chunkie.persistance.DatabaseManager;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author dumptruckman, SwearWord
 */
public class IChunkyGroup extends IChunkyPermissibleObject {

    private HashMap<String, HashSet<IChunkyObject>> members = new HashMap<String, HashSet<IChunkyObject>>();

    public HashMap<String, HashSet<IChunkyObject>> getMembers() {
        return members;
    }

    public void addMember(IChunkyObject member) {
        member._addGroup(this);
        HashSet<IChunkyObject> membersOfType = members.get(member.getType());
        if (membersOfType == null) {
            membersOfType = new HashSet<IChunkyObject>();
            members.put(member.getType(), membersOfType);
        }
        membersOfType.add(member);
        DatabaseManager.getDatabase().addGroupMember(this, member);
    }

    protected void _addMember(IChunkyObject member) {
        HashSet<IChunkyObject> membersOfType = members.get(member.getType());
        if (membersOfType == null) {
            membersOfType = new HashSet<IChunkyObject>();
            members.put(member.getType(), membersOfType);
        }
        membersOfType.add(member);
    }

    public void removeMember(IChunkyObject member) {
        member._removeGroup(this);
        HashSet<IChunkyObject> membersOfType = members.get(member.getType());
        if (membersOfType != null) {
            membersOfType.remove(member);
        }
        DatabaseManager.getDatabase().removeGroupMember(this, member);
    }

    protected void _removeMember(IChunkyObject member) {
        HashSet<IChunkyObject> membersOfType = members.get(member.getType());
        if (membersOfType != null) {
            membersOfType.remove(member);
        }
    }
}
