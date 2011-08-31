package com.dumptruckman.chunky.object;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.event.object.ChunkyObjectNameEvent;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author dumptruckman, SwearWord
 */
public abstract class ChunkyObject {

    private String name;
    private int classHash;
    private HashMap<Integer, HashSet<ChunkyObject>> allOwnables;
    private HashMap<Integer, HashSet<ChunkyObject>> allOwners;

    public ChunkyObject(String name) {
        this.name = name;
        allOwnables = new HashMap<Integer, HashSet<ChunkyObject>>();
        allOwners = new HashMap<Integer, HashSet<ChunkyObject>>();
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

    private boolean _addOwner(ChunkyObject owner) {
        int type = owner.getType();
        if (getOwnersOfType(type) != null) {
            return getOwnersOfType(type).add(owner);
        } else {
            HashSet<ChunkyObject> owners = new HashSet<ChunkyObject>();
            owners.add(owner);
            allOwners.put(type, owners);
            return true;
        }
    }

    private boolean _removeOwner(ChunkyObject owner) {
        int type = owner.getType();
        return getOwnersOfType(type) != null && getOwnersOfType(type).remove(owner);
    }

    /**
     * Adds an object to be owned by this object.  Also adds this object as an owner of the ownable object.
     * @param ownable Object that will become owned by this object.
     */
    final public void addOwnable(ChunkyObject ownable) {
        int type = ownable.getType();
        if (getOwnablesOfType(type) != null) {
            if (getOwnablesOfType(type).add(ownable)) {
                ownable._addOwner(this);
            } else {
                // ownable already exists TODO throw something?
            }
        } else {
            HashSet<ChunkyObject> ownables = new HashSet<ChunkyObject>();
            ownables.add(ownable);
            ownable._addOwner(this);
            allOwnables.put(type, ownables);
        }
    }

    /**
     * Removes an object from this object's ownership.  Also remove this object as an owner of the ownable object.
     * @param ownable Object that will cease to be owned by this object.
     */
    final public void removeOwnable(ChunkyObject ownable) {
        int type = ownable.getType();
        if (getOwnablesOfType(type) != null) {
            if (getOwnablesOfType(type).remove(ownable)) {
                ownable._removeOwner(this);
            } else {
                // ownable did not exist TODO throw something?
            }
        } else {
            // ownable did not exist TODO throw something?
        }
    }

    private boolean _addOwnable(ChunkyObject ownable) {
        int type = ownable.getType();
        if (getOwnablesOfType(type) != null) {
            return getOwnablesOfType(type).add(ownable);
        } else {
            HashSet<ChunkyObject> ownables = new HashSet<ChunkyObject>();
            ownables.add(ownable);
            allOwnables.put(type, ownables);
            return true;
        }
    }

    private boolean _removeOwnable(ChunkyObject ownable) {
        int type = ownable.getType();
        return getOwnablesOfType(type) != null && getOwnablesOfType(type).remove(ownable);
    }

    /**
     * Adds an object to be an owner of this object.  Also adds this object as an owned object of the owner object.
     * @param owner Object that will own this object.
     */
    final public void addOwner(ChunkyObject owner) {
        int type = owner.getType();
        if (getOwnersOfType(type) != null) {
            if (getOwnersOfType(type).add(owner)) {
                owner._addOwnable(this);
            } else {
                // owner already exists TODO throw something?
            }
        } else {
            HashSet<ChunkyObject> owners = new HashSet<ChunkyObject>();
            owners.add(owner);
            owner._addOwnable(this);
            allOwners.put(type, owners);
        }
    }

    /**
     * Removes this object from the ownership of the specified object.  Also removes this object as an owned object of the owner object.
     * @param owner Object that will no longer own this object.
     */
    final public void removeOwner(ChunkyObject owner) {
        int type = owner.getType();
        if (getOwnersOfType(type) != null) {
            if (getOwnersOfType(type).remove(owner)) {
                owner._removeOwnable(this);
            } else {
                // owner did not exist TODO throw something?
            }
        } else {
            // owner did not exist TODO throw something?
        }
    }

    final public boolean isOwned() {
        return (allOwners.size() > 0);
    }

    final public boolean owns(ChunkyObject ownable) {
        int type = ownable.getType();
        if (getOwnablesOfType(type) != null) {
            return getOwnablesOfType(type).contains(ownable);
        }
        return false;
    }

    final public boolean isOwnedBy(ChunkyObject owner) {
        int type = owner.getType();
        if (getOwnersOfType(type) != null) {
            return getOwnersOfType(type).contains(owner);
        }
        return false;
    }

    private HashSet<ChunkyObject> getOwnablesOfType(int type) {
        return allOwnables.get(type);
    }

    private HashSet<ChunkyObject> getOwnersOfType(int type) {
        return allOwners.get(type);
    }

    final public HashSet<ChunkyObject> getOwnables(int type) {
        @SuppressWarnings("unchecked")
        HashSet<ChunkyObject> ownables = (HashSet<ChunkyObject>)allOwnables.get(type).clone();
        return ownables;
    }

    final public HashSet<ChunkyObject> getOwners(int type) {
        @SuppressWarnings("unchecked")
        HashSet<ChunkyObject> owners = (HashSet<ChunkyObject>)allOwners.get(type).clone();
        return owners;
    }
}
