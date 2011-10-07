package org.getchunky.chunky.permission;

/**
 * @author dumptruckman, SwearWord
 */
public enum AccessLevel {

    /**
     * This enum shows the level of access that is granted when permissions for an object is checked.
     * Any level except for NONE indicated permission WAS granted and essentially states the reason it was given.
     */

    /**
     * Admin access
     */
    ADMIN("Admin"),

    /**
     * Unowned/Wilderness areas
     */
    UNOWNED("Unowned"),

    /**
     * An inherited owner of this object. Someone that owns the direct owner.
     */
    OWNER("Owner"),

    /**
     * The direct owner of the object.  As in: object.getOwner()
     */
    DIRECT_OWNER("Direct Owner"),

    /**
     * Permission was given for this object to a specific object.
     */
    DIRECT_PERMISSION("Direct Permission"),

    /**
     * Permission was given for this object to a group
     */
    DIRECT_GROUP_PERMISSION("Direct Group Permission"),

    /**
     * Permission was given for this object's owners properties for a specific object.
     */
    GLOBAL_PERMISSION("Global Permission"),

    /**
     * Permission was given for this object's owners properties for a specific group.
     */
    GLOBAL_GROUP_PERMISSION("Global Group Permission"),

    /**
     * The default permission for this specific object (aka, what perms anyone has on this specific object.)
     */
    DIRECT_DEFAULT_PERMISSION("Direct Default Permission"),

    /**
     * The default permission for all objects owned by this object's owner.
     */
    GLOBAL_DEFAULT_PERMISSION("Global Default Permission"),

    /**
     * No permission given.
     */
    NONE("None"),

    /**
     * Custom Access Level (Usable by modules)
     */
    CUSTOM("Custom") {
        public void setName(String name) {
            this.name = name;
        }
    },;

    private boolean denied = false;
    protected String name;

    AccessLevel(String name) {
        this.name = name;
    }

    /**
     * Sets whether this access level denied permission or not
     *
     * @param deny true to deny
     */
    public void setDenied(boolean deny) {
        this.denied = deny;
    }

    /**
     * Checks whether this access level caused denial or not
     *
     * @return true if denied at this access level
     */
    public boolean causedDenial() {
        return denied;
    }

    /**
     * Gets the name of the access level.  Mostly used for custom modules.
     *
     * @return name of access level
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the access level.  Only sets name for CUSTOM
     *
     * @param name new name for access level
     */
    public void setName(String name) {
    }
}
