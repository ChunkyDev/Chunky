package org.getchunky.chunkie.persistance;

import org.bukkit.plugin.Plugin;
import org.getchunky.chunkie.object.IChunkyGroup;
import org.getchunky.chunkie.object.IChunkyObject;
import org.getchunky.chunkie.permission.PermissionRelationship;

public interface Database {

    public boolean isLoaded();

    public void setLoaded();

    public boolean connect(Plugin plugin);

    public void disconnect();

    public void loadAllObjects();

    public void loadAllPermissions();
    
    public void loadAllGroups();

    public void loadAllOwnership();

    public void deleteObject(IChunkyObject chunkyObject);

    public void updateObject(IChunkyObject object);

    public void updatePermissions(IChunkyObject permObject, IChunkyObject objectId, PermissionRelationship perms);

    public void removePermissions(IChunkyObject permissible, IChunkyObject object);

    public void removeAllPermissions(IChunkyObject object);

    public void addOwnership(IChunkyObject owner, IChunkyObject ownable);

    public void removeOwnership(IChunkyObject owner, IChunkyObject ownable);

    public void updateDefaultPermissions(IChunkyObject object, PermissionRelationship perms);

    public void addGroupMember(IChunkyGroup group, IChunkyObject member);

    public void removeGroupMember(IChunkyGroup group, IChunkyObject member);

    public void removeGroup(IChunkyGroup group);

}