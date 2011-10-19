package org.getchunky.chunky.permission;

import org.getchunky.chunky.config.Config;
import org.getchunky.chunky.exceptions.ChunkyPlayerOfflineException;
import org.getchunky.chunky.module.ChunkyPermissions;
import org.getchunky.chunky.object.ChunkyGroup;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.object.ChunkyPermissibleObject;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.bukkit.Permissions;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author dumptruckman
 */
public class PermissionChain {

    /**
     * This function checks the permission chain to see if permObject has permission for a specific action (which is indicated by flag.)
     *
     * @param object     Object that the permObject is trying to interact with
     * @param permObject PermissibleObject involved in event. (Usually a player)
     * @param flag       The permission action occuring
     * @return true if permObject has permission to flag action
     */
    public static AccessLevel hasPerm(ChunkyObject object, ChunkyPermissibleObject permObject, PermissionFlag flag) {

        AccessLevel accessLevel = AccessLevel.NONE;
        accessLevel.setDenied(true);

        if (permObject instanceof ChunkyPlayer) {
            try {
                if (Permissions.PLAYER_BUILD_ANYWHERE.hasPerm(((ChunkyPlayer) permObject).getPlayer())) {
                    accessLevel = AccessLevel.ADMIN;
                    accessLevel.setDenied(false);
                    return accessLevel;
                }
            } catch (ChunkyPlayerOfflineException ignore) {
            }
        }

        if (!object.isOwned()) {
            Boolean canUnowned = Config.canUnowned(flag);
            if (canUnowned != null) {
                accessLevel = AccessLevel.UNOWNED;
                accessLevel.setDenied(true);
                if (canUnowned) {
                    accessLevel.setDenied(false);
                    return accessLevel;
                }
                return accessLevel;
            }
        }

        if (object.isOwnedBy(permObject) || permObject.hasPerm(object, ChunkyPermissions.OWNER)) {
            accessLevel = AccessLevel.OWNER;
            accessLevel.setDenied(true);
            if (object.isDirectlyOwnedBy(permObject)) accessLevel = AccessLevel.DIRECT_OWNER;
            accessLevel.setDenied(false);
            return accessLevel;
        }

        Boolean permission = permObject.hasPerm(object, flag);
        if (permission != null) {
            accessLevel = AccessLevel.DIRECT_PERMISSION;
            accessLevel.setDenied(true);
            if (permission) {
                accessLevel.setDenied(false);
                return accessLevel;
            }
            return accessLevel;
        }

        Collection<HashSet<ChunkyGroup>> groups = permObject.getGroups().values();

        for (HashSet<ChunkyGroup> groupsOfType : groups) {
            for (ChunkyGroup group : groupsOfType) {
                permission = group.hasPerm(object, flag);
                if (permission != null) {
                    accessLevel = AccessLevel.DIRECT_GROUP_PERMISSION;
                    accessLevel.setDenied(true);
                    if (permission) {
                        accessLevel.setDenied(false);
                        return accessLevel;
                    }
                    return accessLevel;
                }
            }
        }

        ChunkyObject owner = object.getOwner();
        if (owner != null) {
            permission = permObject.hasPerm(object.getOwner(), flag);
            if (permission != null) {
                accessLevel = AccessLevel.GLOBAL_PERMISSION;
                accessLevel.setDenied(true);
                if (permission) {
                    accessLevel.setDenied(false);
                    return accessLevel;
                }
                return accessLevel;
            }
        }

        for (HashSet<ChunkyGroup> groupsOfType : groups) {
            for (ChunkyGroup group : groupsOfType) {
                permission = group.hasPerm(object.getOwner(), flag);
                if (permission != null) {
                    accessLevel = AccessLevel.GLOBAL_GROUP_PERMISSION;
                    accessLevel.setDenied(true);
                    if (permission) {
                        accessLevel.setDenied(false);
                        return accessLevel;
                    }
                    return accessLevel;
                }
            }
        }

        permission = object.hasDefaultPerm(flag);
        if (permission != null) {
            accessLevel = AccessLevel.DIRECT_DEFAULT_PERMISSION;
            accessLevel.setDenied(true);
            if (permission) {
                accessLevel.setDenied(false);
                return accessLevel;
            }
            return accessLevel;
        }

        owner = object.getOwner();
        if (owner != null) {
            permission = object.getOwner().hasDefaultPerm(flag);
            if (permission != null) {
                accessLevel = AccessLevel.GLOBAL_DEFAULT_PERMISSION;
                accessLevel.setDenied(true);
                if (permission) {
                    accessLevel.setDenied(false);
                    return accessLevel;
                }
                return accessLevel;
            }
        }

        return accessLevel;
    }
}
