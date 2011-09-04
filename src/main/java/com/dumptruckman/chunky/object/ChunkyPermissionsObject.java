package com.dumptruckman.chunky.object;

import java.util.HashMap;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPermissionsObject extends ChunkyObject {

    protected HashMap<Integer, ChunkyObject> permissibles = new HashMap<Integer, ChunkyObject>();
    
    public ChunkyPermissionsObject(String name) {
        super(name);
    }

    public boolean hasBuildPermission(ChunkyObject chunkyObject) {
        return true; // TODO
    }

    public boolean hasDestroyPermission(ChunkyObject chunkyObject) {
        return true; // TODO
    }

    public boolean hasItemPermission(ChunkyObject chunkyObject) {
        return true; // TODO
    }

    public boolean hasSwitchPermission(ChunkyObject chunkyObject) {
        return true; // TODO
    }
}
