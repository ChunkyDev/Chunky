package com.dumptruckman.chunky.object;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.event.object.ChunkyObjectNameEvent;
import com.dumptruckman.chunky.event.object.ChunkyObjectOwnershipEvent;
import com.dumptruckman.chunky.persistance.DatabaseManager;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author dumptruckman, SwearWord
 */
public abstract class ChunkyObject extends ChunkyOwnershipNode {

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
        return !(parent==null);
    }

    public void addOwnable(ChunkyObject ownable) {
        if (! allowsChildren)
            throw new IllegalStateException();
        if (ownable == null)
            throw new IllegalArgumentException();
        children.add(ownable);
        ownable.setParent(this);
    }

    public void addOwner(ChunkyObject owner) {
        if (owner == null)
            throw new IllegalArgumentException();
        if (! owner.allowsChildren)
            throw new IllegalStateException();
        parent = owner;
        owner.addOwnable(this);
    }

    final public boolean owns(ChunkyObject ownable) {
        return children.contains(ownable);
    }

    final public boolean isOwnedBy(ChunkyObject owner) {
        if (owner == null)
            return false;
        ChunkyObject current = this;
        while (current != null && current != owner)
            current = current.getParent();
        return current == owner;
    }

    final public boolean isDirectlyOwnnedBy(ChunkyObject owner) {
        return parent == owner;
    }


}
