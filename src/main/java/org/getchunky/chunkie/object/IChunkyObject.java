package org.getchunky.chunkie.object;

import org.getchunky.chunkie.Chunky;
import org.getchunky.chunkie.ChunkyManager;
import org.getchunky.chunkie.event.object.ChunkyObjectNameEvent;
import org.getchunky.chunkie.exceptions.ChunkyObjectNotInitializedException;
import org.getchunky.chunkie.permission.PermissionFlag;
import org.getchunky.chunkie.permission.PermissionRelationship;
import org.getchunky.chunkie.persistance.DatabaseManager;
import org.getchunky.chunkie.util.Logging;
import org.getchunky.chunky.object.ChunkyObject;
import org.json.JSONException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author dumptruckman, SwearWord
 */
public abstract class IChunkyObject implements ChunkyObject {

    /**
     * Returns the child <code>TreeNode</code> at index
     * <code>childIndex</code>.
     */
    protected IChunkyObject owner;
    private HashMap<String, HashSet<IChunkyObject>> ownables = new HashMap<String, HashSet<IChunkyObject>>();
    private HashMap<String, HashSet<IChunkyGroup>> groups = new HashMap<String, HashSet<IChunkyGroup>>();

    private String id;
    private final String className;

    public IChunkyObject() {
        try {
            getData().put("name", "");
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        className = this.getClass().getName();
    }

    public boolean save() throws ChunkyObjectNotInitializedException {
        if (id == null) throw new ChunkyObjectNotInitializedException("Object cannot be saved without ID!");
        DatabaseManager.getDatabase().updateObject(this);
        return true;
    }

    public void delete() {
        DatabaseManager.getDatabase().deleteObject(this);
        ChunkyManager.unregisterObject(this);
    }

    public String getName() {
        try {
            return getData().getString("name");
        } catch (JSONException e) {
            Logging.severe(e.getMessage());
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public ChunkyObject setId(String id) {
        this.id = id;
        ChunkyManager.registerObject(this);
        return this;
    }

    public String getType() {
        return className;
    }

    public String getFullId() {
        return className + ":" + id;
    }

    public ChunkyObject setName(String name) {
        ChunkyObjectNameEvent event = new ChunkyObjectNameEvent(this, name);
        Chunky.getModuleManager().callEvent(event);
        if (event.isCancelled()) return this;
        try {
            getData().put("name", event.getNewName());
        } catch (JSONException e) {
            //Logging.warning(e.getMessage());
        }
        save();
        return this;
    }

    public final int hashCode() {
        return getFullId().hashCode();
    }

    public final boolean equals(Object obj) {
        return obj instanceof IChunkyObject && ((IChunkyObject) obj).getFullId().equals(this.getFullId());
    }

    public boolean isOwned() {
        return !(owner == null);
    }

    /**
     * Causes o to be owned by this object.
     *
     * @param o object to become owned.
     * @return true if this object did not already own o.
     */
    private boolean addOwnable(IChunkyObject o) {
        if (o == null)
            throw new IllegalArgumentException();

        // If owner is a owned by the child, remove owner from tree first.
        // ex. town.setOwner(Peasant) Peasant is removed from parent (Town).

        if (this.isOwnedBy(o) && this.owner != null) {
            this.getOwner().removeOwnableAndTakeChildren(this);
        }
        if (ownables.containsKey(o.getType())) {
            Boolean exists = ownables.get(o.getType()).add(o);
            if (exists) DatabaseManager.getDatabase().addOwnership(this, o);
            return exists;
        } else {
            HashSet<IChunkyObject> ownables = new HashSet<IChunkyObject>();
            ownables.add(o);
            this.ownables.put(o.getType(), ownables);
            
            DatabaseManager.getDatabase().addOwnership(this, o);
            return true;
        }
    }

    /**
     * Removes an ownable o from this object.
     *
     * @param o the ownable to remove
     * @return true if the set contained the specified element
     */
    private boolean removeOwnable(IChunkyObject o) {
        Boolean removed = ownables.containsKey(o.getType()) && ownables.get(o.getType()).remove(o);
        if (removed) {
            o.owner = null;
            DatabaseManager.getDatabase().removeOwnership(this, o);
        }
        return removed;
    }

    private void removeOwnableAndTakeChildren(IChunkyObject o) {
        if (removeOwnable(o)) takeChildren(o);
    }

    private void takeChildren(IChunkyObject o) {
        // If a child is removed, parent reposesses all their objects.
        // ex. If a child of a town is removed then their plots are owned by town.

        HashMap<String, HashSet<IChunkyObject>> reposess = o.getOwnables();
        o.ownables = new HashMap<String, HashSet<IChunkyObject>>();
        for (String key : reposess.keySet()) {
            for (IChunkyObject co : reposess.get(key)) {
                this.addOwnable(co);
            }
        }
    }

    private void removeChildren() {
        //Removes all children.

        HashMap<String, HashSet<IChunkyObject>> reposess = this.getOwnables();
        this.ownables = new HashMap<String, HashSet<IChunkyObject>>();
        for (String key : reposess.keySet()) {
            for (IChunkyObject co : reposess.get(key)) {
                co.setOwner(null, true, true);}}
    }

    /**
     * Checks if o is owned by this object.
     *
     * @param o object to hasPerm ownership for
     * @return true if this object owns o
     */
    public boolean isOwnerOf(ChunkyObject o) {
        return o.isOwnedBy(this);
    }

    /**
     * @param owner Check if this object is an ancestor.
     * @return
     */
    public boolean isOwnedBy(ChunkyObject owner) {
        if (owner == null)
            return false;
        IChunkyObject current = this;
        while (current != null && current != owner)
            current = current.getOwner();
        return current == owner;
    }

    public boolean isDirectlyOwnedBy(ChunkyObject owner) {
        return this.owner == owner;
    }

    /**
     * Returns the owner <code>TreeNode</code> of the receiver.
     */
    public ChunkyObject getOwner() {
        return owner;
    }

    /**
     * @param object       the object that will become the owner.
     * @param keepChildren false transfers the object's children to current owner.
     */
    public void setOwner(ChunkyObject object, Boolean keepChildren, boolean clearPermissions) {
        if (owner != null) {
            if (keepChildren)
                owner.removeOwnable(this);
            else
                owner.removeOwnableAndTakeChildren(this);
        }
        else if(!keepChildren) {
            this.removeChildren();
        }
        if (object != null) {
            if (object.addOwnable(this))
                owner = object;
        } else {
            owner = null;
        }
        if (clearPermissions) {
            ChunkyManager.getAllPermissions(this).clear();
            DatabaseManager.getDatabase().removeAllPermissions(this);
        }
    }

    public final Boolean hasDefaultPerm(PermissionFlag type) {
        PermissionRelationship perms = ChunkyManager.getPermissions(this, this);
        Logging.debug("default perms: " + perms + " contains " + type + "?");
        return perms.hasFlag(type);
    }

    public final PermissionRelationship getDefaultPerms() {
        return ChunkyManager.getPermissions(this, this);
    }

    public final void setDefaultPerms(HashMap<PermissionFlag, Boolean> flags) {
        if (flags == null) {
            ChunkyManager.getPermissions(this, this).clearFlags();
            DatabaseManager.getDatabase().removePermissions(this, this);
            return;
        }

        for (Map.Entry<PermissionFlag, Boolean> flag : flags.entrySet()) {
            setDefaultPerm(flag.getKey(), flag.getValue());
        }
    }

    public final void setDefaultPerm(PermissionFlag type, boolean status) {
        setDefaultPerm(type, status, true);
    }

    public final void setDefaultPerm(PermissionFlag type, boolean status, boolean persist) {
        PermissionRelationship perms = ChunkyManager.getPermissions(this, this);
        // Set flag
        perms.setFlag(type, status);

        // Persist if requested
        if (persist) {
            DatabaseManager.getDatabase().updateDefaultPermissions(this, perms);
        }
    }

    /**
     * Returns true if the receiver is a leaf.
     */
    public final boolean isLeaf() {
        return ownables.size() == 0;
    }


    /**
     * Returns all ownables of this object.  You may not change the structure/values of this HashMap.
     *
     * @return
     */
    public final HashMap<String, HashSet<IChunkyObject>> getOwnables() {
        @SuppressWarnings("unchecked")
        HashMap<String, HashSet<IChunkyObject>> ownables = (HashMap<String, HashSet<IChunkyObject>>) this.ownables.clone();
        return ownables;
    }

    public void addToGroup(IChunkyGroup group) {
        group._addMember(this);
        HashSet<IChunkyGroup> groupsOfType = groups.get(group.getType());
        if (groupsOfType == null) {
            groupsOfType = new HashSet<IChunkyGroup>();
            groups.put(group.getType(), groupsOfType);
        }
        groupsOfType.add(group);
        DatabaseManager.getDatabase().addGroupMember(group, this);
    }

    protected void _addGroup(IChunkyGroup group) {
        HashSet<IChunkyGroup> groupsOfType = groups.get(group.getType());
        if (groupsOfType == null) {
            groupsOfType = new HashSet<IChunkyGroup>();
            groups.put(group.getType(), groupsOfType);
        }
        groupsOfType.add(group);
    }

    public void removeFromGroup(IChunkyGroup group) {
        group._removeMember(this);
        HashSet<IChunkyGroup> groupsOfType = groups.get(group.getType());
        if (groupsOfType != null) {
            groupsOfType.remove(group);
        }
        DatabaseManager.getDatabase().removeGroupMember(group, this);
    }

    protected void _removeGroup(IChunkyGroup group) {
        HashSet<IChunkyGroup> groupsOfType = groups.get(group.getType());
        if (groupsOfType != null) {
            groupsOfType.remove(group);
        }
    }

    public HashMap<String, HashSet<IChunkyGroup>> getGroups() {
        return groups;
    }
}
