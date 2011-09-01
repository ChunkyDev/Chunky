package com.dumptruckman.chunky;

import com.dumptruckman.chunky.exceptions.ChunkyUnregisteredException;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyCoordinates;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.persistance.DatabaseManager;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.Chunk;
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
        DatabaseManager.addPlayer(player);
        return player;
    }

    public static ChunkyChunk getChunk(ChunkyCoordinates coords) {
        if(CHUNKS.containsKey(coords)) return CHUNKS.get(coords);
        ChunkyChunk chunkyChunk = new ChunkyChunk(coords);
        CHUNKS.put(coords,chunkyChunk);
        DatabaseManager.addChunk(chunkyChunk);
        return chunkyChunk;
    }

    public static ChunkyChunk getChunk(Location location)
    {
        return getChunk(new ChunkyCoordinates(location));
    }

    public static void addChunk(ChunkyChunk chunk) {
        CHUNKS.put(chunk.getCoord(),chunk);
        Logging.debug("Loaded chunk: " + chunk.getCoord().getX() + ":" + chunk.getCoord().getZ());
    }

    public static void addChunkyPlayer(ChunkyPlayer chunkyPlayer) {
        PLAYERS.put(chunkyPlayer.getName(), chunkyPlayer);
    }

}
