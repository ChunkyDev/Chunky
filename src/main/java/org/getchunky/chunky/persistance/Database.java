package org.getchunky.chunky.persistance;

import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.object.ChunkyPermissibleObject;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.ChunkyPermissions;
import org.bukkit.plugin.Plugin;

import java.util.EnumSet;
import java.util.List;

public interface Database {

    public boolean connect(Plugin plugin);

    public void disconnect();

    public void loadAllObjects();

    public void loadAllPermissions();

    public void loadAllOwnership();

    public void updateObject(ChunkyObject object);

    public void updatePermissions(ChunkyObject permObject, ChunkyObject objectId, EnumSet<ChunkyPermissions.Flags> flags);

    public void removePermissions(ChunkyObject permissible, ChunkyObject object);

    public void removeAllPermissions(ChunkyObject object);

    public void addOwnership(ChunkyObject owner, ChunkyObject ownable);

    public void removeOwnership(ChunkyObject owner, ChunkyObject ownable);

    public void updateDefaultPermissions(ChunkyObject object, EnumSet<ChunkyPermissions.Flags> flags);

}