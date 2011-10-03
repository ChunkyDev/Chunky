package org.getchunky.chunky.permission;

import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.config.Config;
import org.getchunky.chunky.exceptions.ChunkyPlayerOfflineException;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.object.ChunkyPermissibleObject;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.bukkit.Permissions;

/**
 * @author dumptruckman
 */
public class ChunkyPermissionChain {

    /**
     * This function checks the permission chain to see if permObject has permission for a specific action (which is indicated by flag.)
     *
     * @param object Object that the permObject is trying to interact with
     * @param permObject PermissibleObject involved in event. (Usually a player)
     * @param flag The permission action occuring
     * @return true if permObject has permission to flag action
     */
    public static ChunkyAccessLevel hasPerm(ChunkyObject object, ChunkyPermissibleObject permObject, ChunkyPermissions.Flags flag) {

        ChunkyAccessLevel accessLevel = ChunkyAccessLevel.NONE;
        accessLevel.setDenied(true);
        
        if (permObject instanceof ChunkyPlayer) {
            try {
                if (Permissions.PLAYER_BUILD_ANYWHERE.hasPerm(((ChunkyPlayer) permObject).getPlayer())) {
                    accessLevel = ChunkyAccessLevel.ADMIN;
                    accessLevel.setDenied(false);
                    return accessLevel;
                }
            } catch (ChunkyPlayerOfflineException ignore) {}
        }

        if (!object.isOwned()) {
            accessLevel = ChunkyAccessLevel.UNOWNED;
            if (Config.canUnowned(flag)) {
                accessLevel.setDenied(false);
                return accessLevel;
            }
            return accessLevel;
        }

        if (object.isOwnedBy(permObject)) {
            accessLevel = ChunkyAccessLevel.OWNER;
            if (object.isDirectlyOwnedBy(permObject)) accessLevel = ChunkyAccessLevel.DIRECT_OWNER;
            accessLevel.setDenied(false);
            return accessLevel;
        }

        Boolean permission = permObject.hasPerm(object, flag);
        if (permission != null) {
            accessLevel = ChunkyAccessLevel.DIRECT_PERMISSION;
            if (permission) {
                accessLevel.setDenied(false);
                return accessLevel;
            }
            return accessLevel;
        }

        ChunkyObject owner = object.getOwner();
        if (owner != null) {
            permission = permObject.hasPerm(object.getOwner(), flag);
            if (permission != null) {
                accessLevel = ChunkyAccessLevel.GLOBAL_PERMISSION;
                if (permission) {
                    accessLevel.setDenied(false);
                    return accessLevel;
                }
                return accessLevel;
            }
        }

        permission = object.hasDefaultPerm(flag);
        if (permission != null) {
            accessLevel = ChunkyAccessLevel.DIRECT_DEFAULT_PERMISSION;
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
                accessLevel = ChunkyAccessLevel.GLOBAL_DEFAULT_PERMISSION;
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