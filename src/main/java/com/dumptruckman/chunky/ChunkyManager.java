package com.dumptruckman.chunky;

import com.dumptruckman.chunky.object.*;
import com.dumptruckman.chunky.permission.ChunkyPermissions;
import com.dumptruckman.chunky.persistance.DatabaseManager;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.Bukkit;
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
    private static HashMap<String, HashMap<String, ChunkyPermissions>> permissions = new HashMap<String, HashMap<String, ChunkyPermissions>>();
    private static HashMap<String, ChunkyObject> OBJECTS = new HashMap<String, ChunkyObject>();

    public static boolean registerObject(ChunkyObject object) {
        if (OBJECTS.containsKey(object.getId())) {
            return false;
        }
        OBJECTS.put(object.getId(), object);
        return true;
    }

    public static ChunkyObject getObject(String id) {
        return OBJECTS.get(id);
    }

    /**
     * Strips the type from an id and returns just the name.
     *
     * @param id
     * @return
     */
    public static String getNameFromId(String id) {
        try {
            return id.substring(id.indexOf(":") + 1);
        } catch (Exception ignore) {
            return null;
        }
    }

    /**
     * Gets a ChunkyPlayer object from the given player name.  The given name will first try a case-insensitive match for any online player, and then a case-sensitive match of offline players.  If no player is found, null is returned.  If a player is found, this will retrieve the ChunkyPlayer instance for them.  It will create a new instance if there is no instance already created.
     *
     * @param name Name of the player
     * @return A ChunkyPlayer object for the given name.
     */
    public static ChunkyPlayer getChunkyPlayer(String name)
    {
        Player player = Bukkit.getServer().getPlayer(name);
        if (player == null) {
            player = Bukkit.getServer().getPlayerExact(name);
        }
        if (player == null) return null;

        return getChunkyPlayer(player);
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
        String id = ChunkyPlayer.class.getName() + ":" + player.getName();
        if(PLAYERS.containsKey(id)) return PLAYERS.get(id);
        ChunkyPlayer cPlayer = new ChunkyPlayer(player.getName());
        PLAYERS.put(id, cPlayer);
        DatabaseManager.addPlayer(cPlayer);
        return cPlayer;
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
     * Gets the permissions object for the permissions relationship between permObjectId and objectId.
     * 
     * @param objectId Object being interacted with
     * @param permObjectId Object doing the interacting
     * @return a ChunkyPermissions objectId containing the permissions for this relationship
     */
    public static ChunkyPermissions getPermissions(String objectId, String permObjectId) {
        if (!permissions.containsKey(objectId)) {
            permissions.put(objectId, new HashMap<String, ChunkyPermissions>());
        }
        HashMap<String, ChunkyPermissions> perms = permissions.get(objectId);
        if (!perms.containsKey(permObjectId)) {
            perms.put(permObjectId, new ChunkyPermissions());
        }
        Logging.debug("ChunkyManager.getPermissions() reports perms as: " + perms.get(permObjectId).toString());
        return perms.get(permObjectId);
    }

    /**
     * Gets a hashmap of all the permissions set on a specific object.  Altering this hashmap can potentially screw up persistence.  Use with caution!
     *
     * @param object Object in question
     * @return HashMap of object permissions
     */
    public static HashMap<String, ChunkyPermissions> getAllPermissions(String object) {
        if (!permissions.containsKey(object)) {
            permissions.put(object, new HashMap<String, ChunkyPermissions>());
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
