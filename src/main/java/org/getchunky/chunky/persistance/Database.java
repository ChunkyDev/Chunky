package org.getchunky.chunky.persistance;

import org.bukkit.plugin.Plugin;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.permission.PermissionRelationship;

public interface Database {

    public boolean connect(Plugin plugin);

    public void disconnect();

    public void loadAllObjects();

    public void loadAllPermissions();

    public void loadAllOwnership();

    public void updateObject(ChunkyObject object);

    public void updatePermissions(ChunkyObject permObject, ChunkyObject objectId, PermissionRelationship perms);

    public void removePermissions(ChunkyObject permissible, ChunkyObject object);

    public void removeAllPermissions(ChunkyObject object);

    public void addOwnership(ChunkyObject owner, ChunkyObject ownable);

    public void removeOwnership(ChunkyObject owner, ChunkyObject ownable);

    public void updateDefaultPermissions(ChunkyObject object, PermissionRelationship perms);

}