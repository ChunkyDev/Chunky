package org.getchunky.chunky;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.getchunky.chunky.object.*;
import org.getchunky.chunky.permission.PermissionFlag;
import org.getchunky.chunky.permission.PermissionRelationship;
import org.getchunky.chunky.persistance.DatabaseManager;
import org.getchunky.chunky.util.Logging;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyManager {

    private static HashMap<ChunkyObject, HashMap<ChunkyObject, PermissionRelationship>> permissions = new HashMap<ChunkyObject, HashMap<ChunkyObject, PermissionRelationship>>();
    private static HashMap<String, HashMap<String, ChunkyObject>> OBJECTS = new HashMap<String, HashMap<String, ChunkyObject>>();

    /**
     * Allows objects to be registered for lookup by ID.  You probably shouldn't call this method as all ChunkyObjects are automatically registered.
     *
     * @param object ChunkyObject to register
     * @return true if object was not yet registered
     */
    public static boolean registerObject(ChunkyObject object) {
        HashMap<String, ChunkyObject> ids = OBJECTS.get(object.getType());
        if (ids == null) {
            ids = new HashMap<String, ChunkyObject>();
            OBJECTS.put(object.getType(), ids);
        }
        if (ids.containsKey(object.getId())) return false;
        ids.put(object.getId(), object);
        return true;
    }

    public static void unregisterObject(ChunkyObject chunkyObject) {
        HashMap<String, ChunkyObject> ids = OBJECTS.get(chunkyObject.getType());
        if (ids == null) return;
        ids.remove(chunkyObject.getId());
    }

    /**
     * Looks up an object by ID.  This method will return null if the object has not been initialized.
     *
     * @param type Type of object (Class name)
     * @param id   Object id
     * @return Object associated with id or null
     */
    public static ChunkyObject getObject(String type, String id) {
        HashMap<String, ChunkyObject> ids = getObjectsOfType(type);
        if (ids == null) return null;
        return ids.get(id);
    }

    public static ChunkyObject getObject(String fullId) {
        String[] typeId = fullId.split(":");
        if (typeId.length < 2) return null;
        String id = typeId[1];
        for (int i = 2; i < typeId.length; i++) {
            id += ":" + typeId[i];
        }
        return getObject(typeId[0], typeId[1]);
    }

    public static HashMap<String, ChunkyObject> getObjectsOfType(String type) {
        return OBJECTS.get(type);
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
     * Compares a ChunkyObject id with a class to determine if the id is a for an object of a certain type.
     *
     * @param id   ID of ChunkyObject
     * @param type Class for ChunkyObject to compare to
     * @return true if ID is for object of class type
     */
    public static boolean isType(String id, Class type) {
        String typeName = id.substring(0, id.indexOf(":"));
        return type.getName().equals(typeName);
    }

    /**
     * Gets a ChunkyPlayer object from the given player name.  The given name will first try a case-insensitive match for any online player, and then a case-sensitive match of offline players.  If no player is found, null is returned.  If a player is found, this will retrieve the ChunkyPlayer instance for them.  It will create a new instance if there is no instance already created.
     *
     * @param name Name of the player
     * @return A ChunkyPlayer object for the given name.
     * @see org.getchunky.chunky.ChunkyManager#getChunkyPlayer(OfflinePlayer player) for a quicker lookup method.
     */
    public static ChunkyPlayer getChunkyPlayer(String name) {
        OfflinePlayer player = Bukkit.getServer().getPlayer(name);
        if (player == null) {
            HashMap<String, ChunkyObject> players = getObjectsOfType(ChunkyPlayer.class.getName());
            for (ChunkyObject object : players.values()) {
                if (name.equalsIgnoreCase(object.getName())) {
                    return (ChunkyPlayer) object;
                }
            }
        }
        if (player == null) return null;

        return getChunkyPlayer(player);
    }

    /**
     * The preferred method for retrieving a ChunkyPlayer object.
     *
     * @param player
     * @return A ChunkyPlayer object for the given player.
     */
    public static ChunkyPlayer getChunkyPlayer(OfflinePlayer player) {
        String id = player.getName();
        ChunkyObject chunkyObject = getObject(ChunkyPlayer.class.getName(), id);
        if (chunkyObject != null) {
            ChunkyPlayer cPlayer = (ChunkyPlayer) chunkyObject;
            HashSet<ChunkyObject> groups = cPlayer.getOwnables().get(ChunkyGroup.class.getName());
            if (groups == null || !groups.contains(getObject(ChunkyGroup.class.getName() + ":" + id + "-friends"))) {
                ChunkyGroup friends = new ChunkyGroup();
                friends.setId(id + "-friends").setName("friends");
                friends.setOwner(cPlayer, false, false);
            }
            return cPlayer;
        }
        ChunkyPlayer cPlayer = new ChunkyPlayer();
        cPlayer.setId(id).setName(id);
        ChunkyGroup friends = new ChunkyGroup();
        friends.setId(id + "-friends").setName("friends");
        friends.setOwner(cPlayer, false, false);
        return cPlayer;
    }

    /**
     * Gets the ChunkyChunk associated with the given chunk coordinates
     *
     * @param coords Chunk coordinates object
     * @return ChunkyChunk at the given coordinates
     */
    public static ChunkyChunk getChunkyChunk(ChunkyCoordinates coords) {
        ChunkyObject chunkyObject = getObject(ChunkyChunk.class.getName(), coords.toString());
        if (chunkyObject != null) return (ChunkyChunk) chunkyObject;
        ChunkyChunk chunkyChunk = new ChunkyChunk();
        chunkyChunk.setCoord(coords).setId(coords.toString());
        return chunkyChunk;
    }

    /**
     * Gets the ChunkyChunk associated with the given chunk coordinates
     *
     * @param location Location of chunk
     * @return ChunkyChunk at the given location
     */
    public static ChunkyChunk getChunkyChunk(Location location) {
        return getChunkyChunk(new ChunkyCoordinates(location));
    }

    /**
     * Gets the ChunkyChunk associated with the given chunk coordinates
     *
     * @param block Block within chunk
     * @return ChunkyChunk at the given location
     */
    public static ChunkyChunk getChunkyChunk(Block block) {
        return getChunkyChunk(new ChunkyCoordinates(block.getChunk()));
    }

    /**
     * Gets the permissions object for the permissions relationship between permObject and object.  Altering this permission object will NOT persist changes.
     *
     * @param object     Object being interacted with
     * @param permObject Object doing the interacting
     * @return a PermissionRelationship object containing the permissions for this relationship
     */
    public static PermissionRelationship getPermissions(ChunkyObject object, ChunkyObject permObject) {
        if (!permissions.containsKey(object)) {
            permissions.put(object, new HashMap<ChunkyObject, PermissionRelationship>());
        }
        HashMap<ChunkyObject, PermissionRelationship> perms = permissions.get(object);
        if (!perms.containsKey(permObject)) {
            perms.put(permObject, new PermissionRelationship());
        }
        Logging.debug("ChunkyManager.getPermissions() reports perms as: " + perms.get(permObject).toLongString());
        return perms.get(permObject);
    }

    /**
     * Allows you to set permissions for an object without having the ChunkyObjects.  This will optionally persist changes.
     *
     * @param object     Object being interacted with
     * @param permObject Object doing the interacting
     * @param flags      Flag Set to change permissions to
     */
    public static void setPermissions(ChunkyObject object, ChunkyObject permObject, HashMap<PermissionFlag, Boolean> flags) {
        if (flags == null) {
            ChunkyManager.getPermissions(object, permObject).clearFlags();
            DatabaseManager.getDatabase().removePermissions(object, permObject);
            return;
        }
        PermissionRelationship perms = ChunkyManager.getPermissions(object, permObject);
        for (Map.Entry<PermissionFlag, Boolean> flag : flags.entrySet()) {
            perms.setFlag(flag.getKey(), flag.getValue());
        }

        // persist
        DatabaseManager.getDatabase().updatePermissions(permObject, object, perms);
    }

    /**
     * Sets a new permission relationship for the two given objects.  This will NOT persist changes.
     *
     * @param object     Object being interacted with
     * @param permObject Object doing the interacting
     * @param perms      new PermissionRelationship that will overwrite the old
     */
    public static void putPermissions(ChunkyObject object, ChunkyObject permObject, PermissionRelationship perms) {
        if (!permissions.containsKey(object)) {
            permissions.put(object, new HashMap<ChunkyObject, PermissionRelationship>());
        }
        HashMap<ChunkyObject, PermissionRelationship> permsMap = permissions.get(object);
        permsMap.put(permObject, perms);
    }

    /**
     * Gets a hashmap of all the permissions set on a specific object.  Altering this hashmap can potentially screw up persistence.  Use with caution!
     *
     * @param object Object in question
     * @return HashMap of object permissions
     */
    public static HashMap<ChunkyObject, PermissionRelationship> getAllPermissions(ChunkyObject object) {
        if (!permissions.containsKey(object)) {
            permissions.put(object, new HashMap<ChunkyObject, PermissionRelationship>());
        }
        return permissions.get(object);
    }

    /**
     * Returns a String useable as a unique object id.  The ID is based off the systems nanotime.
     *
     * @return unique ID
     */
    public static String getUniqueId() {
        return Long.toString(System.nanoTime());
    }

    /**
     * Retrieves the ChunkyObject representing a Bukkit World
     *
     * @param worldName name of world
     * @return a ChunkyWorld object if worldName is for a valid world, else null
     */
    public static ChunkyWorld getChunkyWorld(String worldName) {
        ChunkyObject object = getObject(ChunkyWorld.class.getName(), worldName);
        if (object == null) {
            World world = Bukkit.getServer().getWorld(worldName);
            if (world != null) {
                object = new ChunkyWorld();
                object.setId(world.getName()).setName(world.getName());
            }
        }
        return (ChunkyWorld) object;
    }
}
