package com.dumptruckman.chunky.dynamicpersistance;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyCoordinates;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface Database {

    public boolean connect(Plugin plugin);

    public void disconnect();

    public void loadAllChunks();

    public void loadAllPlayers();

    public void loadAllPermissions();


}
