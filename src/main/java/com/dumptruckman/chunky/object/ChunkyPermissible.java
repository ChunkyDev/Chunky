package com.dumptruckman.chunky.object;

import java.util.EnumSet;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPermissible {

    protected ChunkyObject object;
    protected EnumSet<PermissionFlags> permissionFlags;

    public ChunkyPermissible(ChunkyObject object) {
        this.object = object;
    }
}
