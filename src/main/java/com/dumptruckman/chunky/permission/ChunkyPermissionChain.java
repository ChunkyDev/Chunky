package com.dumptruckman.chunky.permission;

import com.dumptruckman.chunky.config.Config;
import com.dumptruckman.chunky.exceptions.ChunkyPlayerOfflineException;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPermissibleObject;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.permission.bukkit.Permissions;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.entity.Player;

import javax.swing.plaf.TreeUI;

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
     * @param accessLevel The source of the permission
     * @return true if permObject has permission to flag action
     */
    public static boolean hasPerm(ChunkyObject object, ChunkyPermissibleObject permObject, ChunkyPermissions.Flags flag, ChunkyAccessLevel accessLevel) {

        if (permObject instanceof ChunkyPlayer) {
            try {
                if (Permissions.PLAYER_BUILD_ANYWHERE.hasPerm(((ChunkyPlayer) permObject).getPlayer())) {
                    accessLevel = ChunkyAccessLevel.ADMIN;
                }
            } catch (ChunkyPlayerOfflineException ignore) {}
        }

        if (!object.isOwned()) {
            if (Config.canUnowned(flag)) {
                accessLevel = ChunkyAccessLevel.UNOWNED;
                return true;
            }
        }

        if (object.isOwnedBy(permObject)) {
            accessLevel = ChunkyAccessLevel.OWNER;
            if (object.isDirectlyOwnedBy(permObject)) accessLevel = ChunkyAccessLevel.DIRECT_OWNER;
            return true;
        }

        ChunkyPermissionCache permCache = permObject.getPermCache();
        permCache.cache(object);

        Boolean permission = permCache.getDirectPerms().contains(flag);
        if (permission != null) {
            if (permission) {
                accessLevel = ChunkyAccessLevel.DIRECT_PERMISSION;
                return true;
            }
        }

        if (permCache.getGlobalPerms() != null) {
            permission = permCache.getGlobalPerms().contains(flag);
            if (permission != null) {
                if (permission) {
                    accessLevel = ChunkyAccessLevel.GLOBAL_PERMISSION;
                    return true;
                }
            }
        }

        permission = permCache.getDirectDefaultPerms().contains(flag);
        if (permission != null) {
            if (permission) {
                accessLevel = ChunkyAccessLevel.DIRECT_DEFAULT_PERMISSION;
                return true;
            }
        }

        if (permCache.getGlobalDefaultPerms() != null) {
            permission = permCache.getGlobalDefaultPerms().contains(flag);
            if (permission != null) {
                if (permission) {
                    accessLevel = ChunkyAccessLevel.GLOBAL_DEFAULT_PERMISSION;
                    return true;
                }
            }
        }
        
        return false;
    }
}
