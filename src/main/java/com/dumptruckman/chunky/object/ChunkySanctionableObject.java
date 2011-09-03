package com.dumptruckman.chunky.object;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkySanctionableObject extends ChunkyObject {

    public ChunkySanctionableObject(String name) {
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
