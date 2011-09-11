package com.dumptruckman.chunky.permission;

import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPermissibleObject;

/**
 * @author dumptruckman
 */
public class ChunkyPermissionChain {

    public static boolean check(ChunkyObject object, ChunkyPermissibleObject permObject, ChunkyPermissions.Flags flag, ChunkyAccessLevel accessLevel) {
        if (object.isOwnedBy(permObject)) {
            accessLevel = ChunkyAccessLevel.OWNER;
            if (object.isDirectlyOwnedBy(permObject)) accessLevel = ChunkyAccessLevel.DIRECT_OWNER;
            return false;
        } else if (permObject.hasPerm(object, flag)) {
            accessLevel = ChunkyAccessLevel.DIRECT_PERMISSION;
            return false;
        } else if (permObject.hasPerm(object.getOwner(), flag)) {
            accessLevel = ChunkyAccessLevel.GLOBAL_PERMISSION;
            return false;
        } else if (object.hasDefaultPerm(flag)) {
            accessLevel = ChunkyAccessLevel.DIRECT_DEFAULT_PERMISSION;
            return false;
        } else if (object.hasDefaultPerm(flag)) {
            accessLevel = ChunkyAccessLevel.GLOBAL_DEFAULT_PERMISSION;
            return false;
        }
        return true;
    }
}
