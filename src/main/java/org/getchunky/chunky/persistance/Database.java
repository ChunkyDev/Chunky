package org.getchunky.chunky.persistance;

import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.permission.ChunkyPermissions;
import org.bukkit.plugin.Plugin;

import java.util.EnumSet;
import java.util.List;

public interface Database {

    public boolean connect(Plugin plugin);

    public void disconnect();

    public void loadAllChunks();

    public void loadAllPlayers();

    public void loadAllPermissions();

    public void loadAllChunkOwnership();

    public void updateChunk(ChunkyChunk chunk, String name);

    public void updatePermissions(String permObjectId, String objectId, EnumSet<ChunkyPermissions.Flags> flags);

    public void removePermissions(String permissibleId, String objectId);

    public void removeAllPermissions(String objectId);

    public void addOwnership(ChunkyObject owner, ChunkyObject ownable);

    public void removeOwnership(ChunkyObject owner, ChunkyObject ownable);

    public void updateDefaultPermissions(String id, EnumSet<ChunkyPermissions.Flags> flags);

    public List<String> getOwnablesOfType(ChunkyObject owner, String type);
}
