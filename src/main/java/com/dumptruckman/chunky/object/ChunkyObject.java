package com.dumptruckman.chunky.object;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.event.object.ChunkyObjectNameEvent;

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
    public boolean addOwnable(ChunkyObject o) {
        if (o == null)
            throw new IllegalArgumentException();
        if (ownables.containsKey(o.getType())) {
            o.setOwner(this);
            return ownables.get(o.getType()).add(o);
        } else {
            HashSet<ChunkyObject> ownables = new HashSet<ChunkyObject>();
            ownables.add(o);
            this.ownables.put(o.getType(), ownables);
            return true;
        }
    }

    /**
     * Removes an ownable o from this object.
     * @param o the ownable to remove
     * @return true if the set contained the specified element
     */
    public boolean removeOwnable(ChunkyObject o) {
        return ownables.containsKey(o.getType()) && ownables.get(o.getType()).remove(o);
    }

    /**
     * Checks if o is owned by this object.
     * @param o object to check ownership for
     * @return true if this object owns o
     */
    final public boolean owns(ChunkyObject o) {
        return ownables.containsKey(o.getType()) && ownables.get(o.getType()).contains(o);
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
        this.owner = object;
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
