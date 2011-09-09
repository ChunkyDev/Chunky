package com.dumptruckman.chunky.permission;

import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPermissibleObject;

/**
 * @author dumptruckman
 */
public class ChunkyPermissionChain {

    public static boolean check(ChunkyObject object, ChunkyPermissibleObject permObject, ChunkyPermissionType type) {
        if (object.isOwnedBy(permObject)) {
            type = ChunkyPermissionType.OWNER;
            if (object.isDirectlyOwnedBy(permObject)) type = ChunkyPermissionType.DIRECT_OWNER;
            return false;
        } else if (permObject.hasPerm(object, ChunkyPermissions.Flags.BUILD)) {
            type = ChunkyPermissionType.DIRECT_PERMISSION;
            return false;
        } else if (permObject.hasPerm(object.getOwner(), ChunkyPermissions.Flags.BUILD)) {
            type = ChunkyPermissionType.GLOBAL_PERMISSION;
            return false;
        } else if (object.hasDefaultPerm(ChunkyPermissions.Flags.BUILD)) {
            type = ChunkyPermissionType.DIRECT_DEFAULT_PERMISSION;
            return false;
        } else if (object.hasDefaultPerm(ChunkyPermissions.Flags.BUILD)) {
            type = ChunkyPermissionType.GLOBAL_DEFAULT_PERMISSION;
            return false;
        }
        return true;
    }
}
