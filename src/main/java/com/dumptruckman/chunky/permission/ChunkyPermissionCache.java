package com.dumptruckman.chunky.permission;

import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPermissibleObject;

/**
 * @author dumptruckman
 */
public class ChunkyPermissionCache {

    private ChunkyPermissibleObject permObject;

    private ChunkyObject object = null;
    private ChunkyObject owner = null;
    private ChunkyPermissions directPerms = null;
    private ChunkyPermissions globalPerms = null;
    private ChunkyPermissions directDefaultPerms = null;
    private ChunkyPermissions globalDefaultPerms = null;

    public ChunkyPermissionCache(ChunkyPermissibleObject permObject) {
        this.permObject = permObject;
    }

    /**
     * Checks to see if the permissions cached are for object.  If they are not, the cache is updated with permissions for object.
     *
     * @param object Object permissions are for
     */
    public void cache(ChunkyObject object) {
        if (this.object != null && this.object.equals(object)) {
            if (this.owner != null && this.owner.equals(this.object.getOwner())) {
                return;
            } else {
                setOwnerCache(object);
            }
            return;
        }

        this.object = object;

        directPerms = ChunkyManager.getPermissions(object.getId(), permObject.getId());
        directDefaultPerms = ChunkyManager.getPermissions(object.getId(), object.getId());

    }

    /**
     * Sets up the cache for object's owner.
     * @param object Object whose owner to cache permissions for
     */
    private void setOwnerCache(ChunkyObject object) {
        owner = object.getOwner();
        if (owner == null)  {
            globalPerms = null;
            globalDefaultPerms = null;
            return;
        }
        
        globalPerms = ChunkyManager.getPermissions(owner.getId(), permObject.getId());
        globalDefaultPerms = ChunkyManager.getPermissions(owner.getId(), owner.getId());
    }

    /**
     * Retrieves the direct permissions from the cache.  Make sure to cache() first!
     *
     * @return Permissions
     */
    public ChunkyPermissions getDirectPerms() {
        return directPerms;
    }

    /**
     * Retrieves the global permissions from the cache.  Make sure to cache() first!
     *
     * @return Permissions
     */
    public ChunkyPermissions getGlobalPerms() {
        return globalPerms;
    }

    /**
     * Retrieves the direct default permissions from the cache.  Make sure to cache() first!
     *
     * @return Permissions
     */
    public ChunkyPermissions getDirectDefaultPerms() {
        return directDefaultPerms;
    }

    /**
     * Retrieves the global default permissions from the cache.  Make sure to cache() first!
     *
     * @return Permissions
     */
    public ChunkyPermissions getGlobalDefaultPerms() {
        return globalDefaultPerms;
    }
}
