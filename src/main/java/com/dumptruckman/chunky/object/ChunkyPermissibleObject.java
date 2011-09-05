package com.dumptruckman.chunky.object;

import java.util.HashMap;

/**
 * @author dumptruckman
 */
public class ChunkyPermissibleObject extends ChunkyObject {

    public HashMap<Integer, ChunkyPermissible> permissions = new HashMap<Integer, ChunkyPermissible>();

    public ChunkyPermissibleObject(String name) {
        super(name);
    }

    public boolean canBuild(ChunkyPermissionsObject object) {
        return permissions.containsKey(object.hashCode()) && permissions.get(object.hashCode()).contains(ChunkyPermissible.Flags.BUILD);
    }

    public boolean canDestroy(ChunkyPermissionsObject object) {
        return permissions.containsKey(object.hashCode()) && permissions.get(object.hashCode()).contains(ChunkyPermissible.Flags.DESTROY);
    }

    public boolean canItemUse(ChunkyPermissionsObject object) {
        return permissions.containsKey(object.hashCode()) && permissions.get(object.hashCode()).contains(ChunkyPermissible.Flags.ITEM_USE);
    }

    public boolean canSwitch(ChunkyPermissionsObject object) {
        return permissions.containsKey(object.hashCode()) && permissions.get(object.hashCode()).contains(ChunkyPermissible.Flags.SWITCH);
    }
}
