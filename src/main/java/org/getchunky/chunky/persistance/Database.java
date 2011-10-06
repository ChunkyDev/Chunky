package org.getchunky.chunky.persistance;

import org.bukkit.plugin.Plugin;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.permission.ChunkyPermissions;
import org.getchunky.chunky.permission.PermissionFlag;

import java.util.EnumSet;
import java.util.HashMap;

public interface Database {

    public boolean connect(Plugin plugin);

    public void disconnect();

    public void loadAllObjects();

    public void loadAllPermissions();

    public void loadAllOwnership();

    public void updateObject(ChunkyObject object);

    public void updatePermissions(ChunkyObject permObject, ChunkyObject objectId, ChunkyPermissions perms);

    public void removePermissions(ChunkyObject permissible, ChunkyObject object);

    public void removeAllPermissions(ChunkyObject object);

    public void addOwnership(ChunkyObject owner, ChunkyObject ownable);

    public void removeOwnership(ChunkyObject owner, ChunkyObject ownable);

    public void updateDefaultPermissions(ChunkyObject object, ChunkyPermissions perms);

}