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

    public ChunkyPermissions getDirectPerms() {
        return directPerms;
    }

    public ChunkyPermissions getGlobalPerms() {
        return globalPerms;
    }

    public ChunkyPermissions getDirectDefaultPerms() {
        return directDefaultPerms;
    }

    public ChunkyPermissions getGlobalDefaultPerms() {
        return globalDefaultPerms;
    }
}
