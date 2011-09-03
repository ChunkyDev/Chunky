package com.dumptruckman.chunky.object;

/**
 * @author dumptruckman, SwearWord
 */
public interface ChunkyPermissible {

    public boolean hasBuildPermission(ChunkyObject chunkyObject);

    public boolean hasDestroyPermission(ChunkyObject chunkyObject);

    public boolean hasItemPermission(ChunkyObject chunkyObject);

    public boolean hasSwitchPermission(ChunkyObject chunkyObject);

}
