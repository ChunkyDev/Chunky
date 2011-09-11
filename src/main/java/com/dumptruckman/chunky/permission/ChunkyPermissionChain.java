package com.dumptruckman.chunky.permission;

import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPermissibleObject;

/**
 * @author dumptruckman
 */
public class ChunkyPermissionChain {

    public static boolean check(ChunkyObject object, ChunkyPermissibleObject permObject, ChunkyAccessLevel type) {
        if (object.isOwnedBy(permObject)) {
            type = ChunkyAccessLevel.OWNER;
            if (object.isDirectlyOwnedBy(permObject)) type = ChunkyAccessLevel.DIRECT_OWNER;
            return false;
        } else if (permObject.hasPerm(object, ChunkyPermissions.Flags.BUILD)) {
            type = ChunkyAccessLevel.DIRECT_PERMISSION;
            return false;
        } else if (permObject.hasPerm(object.getOwner(), ChunkyPermissions.Flags.BUILD)) {
            type = ChunkyAccessLevel.GLOBAL_PERMISSION;
            return false;
        } else if (object.hasDefaultPerm(ChunkyPermissions.Flags.BUILD)) {
            type = ChunkyAccessLevel.DIRECT_DEFAULT_PERMISSION;
            return false;
        } else if (object.hasDefaultPerm(ChunkyPermissions.Flags.BUILD)) {
            type = ChunkyAccessLevel.GLOBAL_DEFAULT_PERMISSION;
            return false;
        }
        return true;
    }
}
