package com.dumptruckman.chunky;

import com.dumptruckman.chunky.object.*;
import com.dumptruckman.chunky.permission.ChunkyPermissions;
import com.dumptruckman.chunky.persistance.DatabaseManager;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyManager {

    private static HashMap<String, ChunkyPlayer> PLAYERS = new HashMap<String, ChunkyPlayer>();
    private static HashMap<ChunkyCoordinates, ChunkyChunk> CHUNKS = new HashMap<ChunkyCoordinates, ChunkyChunk>();
    private static HashMap<Integer, HashMap<Integer, ChunkyPermissions>> permissions = new HashMap<Integer, HashMap<Integer, ChunkyPermissions>>();

    /**
     * Gets a ChunkyPlayer object from the given player name.  If there is no instance already created, a new one will be created.  It is probably best to only use this if the name is from Player.getName().
     *
     * @param name Name of the player
     * @return A ChunkyPlayer object for the given name.
     */
    public static ChunkyPlayer getChunkyPlayer(String name)
    {
        if(PLAYERS.containsKey(name)) return PLAYERS.get(name);
        ChunkyPlayer player = new ChunkyPlayer(name);
        PLAYERS.put(name,player);
        DatabaseManager.addPlayer(player);
        return player;
    }

    /**
     * This will attempt to get a ChunkyPlayer with the given hash code.
     *
     * @param hashCode Hash code for player
     * @return If exists, a ChunkyPlayer object. If not, null.
     */
    public static ChunkyPlayer getChunkyPlayer(int hashCode) {
        String name = unhashChunkyObject(hashCode);
        if (!name.startsWith(ChunkyPlayer.class.getName())) return null;
        name = name.substring(name.indexOf(":"));
        return getChunkyPlayer(name);
    }

    /**
     * A convenience method for getting a ChunkyPlayer from a Player object.
     *
     * @param player
     * @return A ChunkyPlayer object for the given player.
     * @see com.dumptruckman.chunky.ChunkyManager#getChunkyPlayer(String name)
     */
    public static ChunkyPlayer getChunkyPlayer(Player player)
    {
        return getChunkyPlayer(player.getName());
    }

    /**
     * Gets the ChunkyChunk associated with the given chunk coordinates
     *
     * @param coords Chunk coordinates object
     * @return ChunkyChunk at the given coordinates
     */
    public static ChunkyChunk getChunk(ChunkyCoordinates coords) {
        if(CHUNKS.containsKey(coords)) return CHUNKS.get(coords);
        ChunkyChunk chunkyChunk = new ChunkyChunk(coords);
        CHUNKS.put(coords,chunkyChunk);
        return chunkyChunk;
    }

    /**
     * Gets the ChunkyChunk associated with the given chunk coordinates
     *
     * @param location Location of chunk
     * @return ChunkyChunk at the given location
     */
    public static ChunkyChunk getChunk(Location location)
    {
        return getChunk(new ChunkyCoordinates(location));
    }

    /**
     * Gets the permissions object for the permissions relationship between permObject and object.
     * 
     * @param object Object being interacted with
     * @param permObject Object doing the interacting
     * @return a ChunkyPermissions object containing the permissions for this relationship
     */
    public static ChunkyPermissions getPermissions(int object, int permObject) {
        if (!permissions.containsKey(object)) {
            permissions.put(object, new HashMap<Integer, ChunkyPermissions>());
        }
        HashMap<Integer, ChunkyPermissions> perms = permissions.get(object);
        if (!perms.containsKey(permObject)) {
            perms.put(permObject, new ChunkyPermissions());
        }
        Logging.debug("ChunkyManager.getPermissions() reports perms as: " + perms.get(permObject).toString());
        return perms.get(permObject);
    }

    /**
     * Gets a hashmap of all the permissions set on a specific object.  Altering this hashmap can potentially screw up persistence.  Use with caution!
     *
     * @param object Object in question
     * @return HashMap of object permissions
     */
    public static HashMap<Integer, ChunkyPermissions> getAllPermissions(int object) {
        if (!permissions.containsKey(object)) {
            permissions.put(object, new HashMap<Integer, ChunkyPermissions>());
        }
        return permissions.get(object);
    }

    /**
    \* Returns a string with a hash equal to the argument.
    \* @return string with a hash equal to the argument.
    \*/
    private static String unhashChunkyObject(int target) {
        StringBuilder answer = new StringBuilder();
        if (target < 0) {
            // String with hash of Integer.MIN_VALUE, 0x80000000
            answer.append("\\u0915\\u0009\\u001e\\u000c\\u0002");

            if (target == Integer.MIN_VALUE)
                return answer.toString();
            // Find target without sign bit set
            target = target & Integer.MAX_VALUE;
        }

        unhash0(answer, target);
        return answer.toString();
    }

    private static void unhash0(StringBuilder partial, int target) {
        int div = target / 31;
        int rem = target % 31;

        if (div <= Character.MAX_VALUE) {
            if (div != 0)
                partial.append((char)div);
            partial.append((char)rem);
        } else {
            unhash0(partial, div);
            partial.append((char)rem);
        }
    }
}
