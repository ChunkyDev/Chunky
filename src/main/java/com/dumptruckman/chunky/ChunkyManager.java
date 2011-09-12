package com.dumptruckman.chunky;

import com.dumptruckman.chunky.object.*;
import com.dumptruckman.chunky.permission.ChunkyPermissions;
import com.dumptruckman.chunky.persistance.DatabaseManager;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyManager {

    private static HashMap<String, ChunkyPlayer> PLAYERS = new HashMap<String, ChunkyPlayer>();
    private static HashMap<ChunkyCoordinates, ChunkyChunk> CHUNKS = new HashMap<ChunkyCoordinates, ChunkyChunk>();
    private static HashMap<Integer, HashMap<Integer, ChunkyPermissions>> permissions = new HashMap<Integer, HashMap<Integer, ChunkyPermissions>>();

    public static ChunkyPlayer getChunkyPlayer(String name)
    {
        if(PLAYERS.containsKey(name)) return PLAYERS.get(name);
        ChunkyPlayer player = new ChunkyPlayer(name);
        PLAYERS.put(name,player);
        DatabaseManager.addPlayer(player);
        return player;
    }
    
    public static ChunkyPlayer getChunkyPlayer(Player player)
    {
        return getChunkyPlayer(player.getName());
    }

    public static ChunkyChunk getChunk(ChunkyCoordinates coords) {
        if(CHUNKS.containsKey(coords)) return CHUNKS.get(coords);
        ChunkyChunk chunkyChunk = new ChunkyChunk(coords);
        CHUNKS.put(coords,chunkyChunk);
        return chunkyChunk;
    }

    public static ChunkyChunk getChunk(Location location)
    {
        return getChunk(new ChunkyCoordinates(location));
    }

    public static ChunkyPermissions getPermissions(int object, int permObject) {
        if (!permissions.containsKey(object)) {
            permissions.put(object, new HashMap<Integer, ChunkyPermissions>());
        }
        HashMap<Integer, ChunkyPermissions> perms = permissions.get(object);
        if (!perms.containsKey(permObject)) {
            perms.put(permObject, new ChunkyPermissions());
        }
        Logging.debug("ChunkyManager.getPermissions() reports perms as: " + perms.toString());
        return perms.get(permObject);
    }
}
