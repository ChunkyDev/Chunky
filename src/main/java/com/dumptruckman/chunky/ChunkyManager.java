package com.dumptruckman.chunky;

import com.dumptruckman.chunky.exceptions.ChunkyUnregisteredException;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyCoordinates;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.Location;

import java.util.HashMap;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyManager {

    private static HashMap<String, ChunkyPlayer> PLAYERS = new HashMap<String, ChunkyPlayer>();
    private static HashMap<ChunkyCoordinates, ChunkyChunk> CHUNKS = new HashMap<ChunkyCoordinates, ChunkyChunk>();

    public static ChunkyPlayer getChunkyPlayer(String name)
    {
        if(PLAYERS.containsKey(name)) return PLAYERS.get(name);
        ChunkyPlayer player = new ChunkyPlayer(name);
        PLAYERS.put(name,player);
        return player;
    }

    public static ChunkyChunk getChunk(ChunkyCoordinates coords) throws ChunkyUnregisteredException {
        if(CHUNKS.containsKey(coords)) return CHUNKS.get(coords);
        throw new ChunkyUnregisteredException();
    }

    public static ChunkyChunk getChunk(Location location) throws ChunkyUnregisteredException
    {
        return getChunk(new ChunkyCoordinates(location));
    }

    public static void addChunk(ChunkyChunk chunk) {
        CHUNKS.put(chunk.getCoord(),chunk);
    }

}
