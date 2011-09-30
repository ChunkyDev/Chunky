package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.permission.ChunkyPermissions;
import org.bukkit.plugin.Plugin;

import java.util.EnumSet;

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

}
