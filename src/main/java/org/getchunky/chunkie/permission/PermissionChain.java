package org.getchunky.chunkie.permission;

import org.getchunky.chunkie.config.Config;
import org.getchunky.chunkie.exceptions.ChunkyPlayerOfflineException;
import org.getchunky.chunkie.module.ChunkyPermissions;
import org.getchunky.chunkie.object.IChunkyGroup;
import org.getchunky.chunkie.object.IChunkyObject;
import org.getchunky.chunkie.object.IChunkyPermissibleObject;
import org.getchunky.chunkie.object.IChunkyPlayer;
import org.getchunky.chunkie.permission.bukkit.Permissions;

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
    public static AccessLevel hasPerm(IChunkyObject object, IChunkyPermissibleObject permObject, PermissionFlag flag) {

        AccessLevel accessLevel = AccessLevel.NONE;
        accessLevel.setDenied(true);

        if (permObject instanceof IChunkyPlayer) {
            try {
                if (Permissions.PLAYER_BUILD_ANYWHERE.hasPerm(((IChunkyPlayer) permObject).getPlayer())) {
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

        Collection<HashSet<IChunkyGroup>> groups = permObject.getGroups().values();

        for (HashSet<IChunkyGroup> groupsOfType : groups) {
            for (IChunkyGroup group : groupsOfType) {
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

        IChunkyObject owner = object.getOwner();
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

        for (HashSet<IChunkyGroup> groupsOfType : groups) {
            for (IChunkyGroup group : groupsOfType) {
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
