package com.dumptruckman.chunky.object;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.event.object.ChunkyObjectNameEvent;
import com.dumptruckman.chunky.persistance.DatabaseManager;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author dumptruckman, SwearWord
 */
public abstract class ChunkyObject {

    /**
     * Returns the child <code>TreeNode</code> at index
     * <code>childIndex</code>.
     */
    protected ChunkyObject owner;
    protected HashMap<Integer, HashSet<ChunkyObject>> ownables = new HashMap<Integer, HashSet<ChunkyObject>>();

    private String name;
    private int classHash;

    public ChunkyObject(String name) {
        this.name = name;
        classHash = this.getClass().getName().hashCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        ChunkyObjectNameEvent event = new ChunkyObjectNameEvent(this, name);
        Chunky.getModuleManager().callEvent(event);
        if (event.isCancelled()) return;
        this.name = event.getNewName();
    }

    public int hashCode() {
        return (getType() + ":" + getName()).hashCode();
    }

    public boolean equals(Object obj) {
        return obj instanceof ChunkyObject && ((ChunkyObject)obj).getName().equals(this.getName());
    }

    public Integer getType() {
        return classHash;
    }

    final public boolean isOwned() {
        return !(owner == null);
    }

    /**
     * Causes o to be owned by this object.
     * @param o object to become owned.
     * @return true if this object did not already own o.
     */
    private boolean addOwnable(ChunkyObject o) {
        if (o == null)
            throw new IllegalArgumentException();
        if(this.isOwnedBy(o)) return false;
        if (ownables.containsKey(o.getType())) {
            Boolean exists = ownables.get(o.getType()).add(o);
            if(exists) DatabaseManager.addOwnership(this,o);
            return exists;
        } else {
            HashSet<ChunkyObject> ownables = new HashSet<ChunkyObject>();
            ownables.add(o);
            this.ownables.put(o.getType(), ownables);
            DatabaseManager.addOwnership(this,o);
            return true;
        }
    }

    /**
     * Removes an ownable o from this object.
     * @param o the ownable to remove
     * @return true if the set contained the specified element
     */
    private boolean removeOwnable(ChunkyObject o) {
        Boolean removed = ownables.containsKey(o.getType()) && ownables.get(o.getType()).remove(o);
        if(removed) DatabaseManager.removeOwnership(this,o);
        return removed;
    }

    /**
     * Checks if o is owned by this object.
     * @param o object to check ownership for
     * @return true if this object owns o
     */
    final public boolean isOwnerOf(ChunkyObject o) {
        return o.isOwnedBy(this);
    }

    /**
     * 
     * @param owner
     * @return
     */
    final public boolean isOwnedBy(ChunkyObject owner) {
        if (owner == null)
            return false;
        ChunkyObject current = this;
        while (current != null && current != owner)
            current = current.getOwner();
        return current == owner;
    }

    final public boolean isDirectlyOwnnedBy(ChunkyObject owner) {
        return this.owner == owner;
    }

    /**
     * Returns the owner <code>TreeNode</code> of the receiver.
     */
    public ChunkyObject getOwner() {
        return owner;
    }

    public void setOwner(ChunkyObject object) {
        //TODO IS THIS RIGHT?!?!
        ChunkyObject oldowner= this.owner;
        if (owner != null)
            owner.removeOwnable(this);
        if (object != null) {
            if (object.addOwnable(this)) owner = object;
        } else {
            owner = null;
        }
        if(oldowner.isOwnedBy(this))
        {
            oldowner.ownables = (HashMap<Integer, HashSet<ChunkyObject>>)this.ownables.clone();
            this.ownables = new HashMap<Integer, HashSet<ChunkyObject>>();
        }

    }

    /**
     * Returns true if the receiver is a leaf.
     */
    public boolean isLeaf() {
        return ownables.size() == 0;
    }


    /**
     * Returns all ownables of this object.  You may not change the structure/values of this HashMap.
     * @return
     */
    public HashMap<Integer, HashSet<ChunkyObject>> getOwnables() {
        @SuppressWarnings("unchecked")
        HashMap<Integer, HashSet<ChunkyObject>> ownables = (HashMap<Integer, HashSet<ChunkyObject>>)this.ownables.clone();
        return ownables;
    }
}
