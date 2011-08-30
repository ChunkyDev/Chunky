package com.dumptruckman.chunky.object;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author dumptruckman
 */
public class ChunkyOwner extends ChunkyObject {

    private HashMap<String, HashSet<ChunkyOwnable>> ownables;

    final public void addOwnable(ChunkyOwnable ownable) {
        if (getOwnablesOfType(ownable.getOwnableType()) != null) {
            if (getOwnablesOfType(ownable.getOwnableType()).add(ownable)) {
                ownable._addOwner(this);
            } else {
                // ownable already exists TODO throw something?
            }
        } else {
            HashSet<ChunkyOwnable> ownables = new HashSet<ChunkyOwnable>();
            ownables.add(ownable);
            ownable._addOwner(this);
            this.ownables.put(ownable.getOwnableType(), ownables);
        }
    }

    final public void removeOwnable(ChunkyOwnable ownable) {
        if (getOwnablesOfType(ownable.getOwnableType()) != null) {
            if (getOwnablesOfType(ownable.getOwnableType()).remove(ownable)) {
                ownable._removeOwner(this);
            } else {
                // ownable did not exist TODO throw something?
            }
        } else {
            // ownable did not exist TODO throw something?
        }
    }

    final public boolean owns(ChunkyOwnable ownable) {
        if (getOwnablesOfType(ownable.getOwnableType()) != null) {
            return getOwnablesOfType(ownable.getOwnableType()).contains(ownable);
        }
        return false;
    }

    final private HashSet<ChunkyOwnable> getOwnablesOfType(String type) {
        return this.ownables.get(type);
    }
    
    final public HashSet<ChunkyOwnable> getOwnables(String type) {
        return (HashSet<ChunkyOwnable>)this.ownables.get(type).clone();
    }
}
