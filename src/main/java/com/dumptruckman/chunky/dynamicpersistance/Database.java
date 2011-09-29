package com.dumptruckman.chunky.dynamicpersistance;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyCoordinates;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface Database {

    public boolean connect(Plugin plugin);

    public void disconnect();

    public ChunkyPlayer loadChunkyPlayer(String name);

    public ChunkyChunk loadChunk(ChunkyCoordinates coordinates);

    public List<ChunkyChunk> getOwnedChunks(ChunkyObject chunkyObject);


}
