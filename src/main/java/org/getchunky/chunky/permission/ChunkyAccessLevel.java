package org.getchunky.chunky.permission;

/**
 * @author dumptruckman, SwearWord
 */
public enum ChunkyAccessLevel {

    /**
     * This enum shows the level of access that is granted when permissions for an object is checked.
     * Any level except for NONE indicated permission WAS granted and essentially states the reason it was given.
     */

    /**
     * Admin access
     */
    ADMIN,

    /**
     * Unowned/Wilderness areas
     */
    UNOWNED,

    /**
     * An inherited owner of this object. Someone that owns the direct owner.
     */
    OWNER,

    /**
     * The direct owner of the object.  As in: object.getOwner()
     */
    DIRECT_OWNER,

    /**
     * Permission was given for this object to a specific object.
     */
    DIRECT_PERMISSION,

    /**
     * Permission was given for this object to a group
     */
    DIRECT_GROUP_PERMISSION,

    /**
     * Permission was given for this object's owners properties for a specific object.
     */
    GLOBAL_PERMISSION,

    /**
     * Permission was given for this object's owners properties for a specific group.
     */
    GLOBAL_GROUP_PERMISSION,

    /**
     * The default permission for this specific object (aka, what perms anyone has on this specific object.)
     */
    DIRECT_DEFAULT_PERMISSION,

    /**
     * The default permission for all objects owned by this object's owner.
     */
    GLOBAL_DEFAULT_PERMISSION,

    /**
     * No permission given.
     */
    NONE,

    ;
    
    private boolean denied = false;

    public void setDenied(boolean deny) {
        this.denied = deny;
    }

    public boolean causedDenial() {
        return denied;
    }
}
