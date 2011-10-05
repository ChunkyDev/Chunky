package org.getchunky.chunky.object;


import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.getchunky.chunky.util.Logging;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyCoordinates {

    private int x;
    private int z;
    private String world;

    /**
     * Creates a ChunkyCoordinates object with the given Location object.
     *
     * @param location Location object for ChunkyCoordinates
     */
    public ChunkyCoordinates(Location location) {
        this(location.getWorld().getName(), location.getBlock().getChunk().getX(), location.getBlock().getChunk().getZ());
    }

    /**
     * Creates a ChunkyCoordinates object with the given Chunk object.
     *
     * @param chunk Chunk for ChunkyCoordinates
     */
    public ChunkyCoordinates(Chunk chunk) {
        this(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
    }

    public ChunkyCoordinates(String coords) {
        String[] strings = coords.split(",");
        try {
            this.world = strings[0];
            this.x = Integer.valueOf(strings[1]);
            this.z = Integer.valueOf(strings[2]);
        } catch (Exception e) {
            Logging.severe("Error parsing chunky coordinates data: " + e.getMessage());
        }
    }

    /**
     * Creates a ChunkyCoordinates object with the given world value and CHUNK X and Z values.
     *
     * @param world World chunk is in
     * @param x     Chunk X (NOT BLOCK)
     * @param z     Chunk Z (NOT BLOCK)
     */
    public ChunkyCoordinates(String world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    /**
     * Returns the X coordinate
     *
     * @return X coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
     * Returns the Z coordinate
     *
     * @return Z coordinate
     */
    public int getZ() {
        return this.z;
    }

    /**
     * Returns the world name
     *
     * @return World name
     */
    public String getWorld() {
        return this.world;
    }

    /**
     * Returns highest block at top left corner of the chunk.
     *
     * @return Top-Left highest block Location
     */
    public Location toLocation() {
        World world = Bukkit.getServer().getWorld(this.world);
        return world.getHighestBlockAt(world.getChunkAt(this.x, this.z).getBlock(0, 0, 0).getLocation()).getLocation();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChunkyCoordinates)) return false;
        ChunkyCoordinates coords = (ChunkyCoordinates) obj;
        return coords.getWorld().equals(this.getWorld()) && coords.getX() == this.x && coords.getZ() == this.z;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return this.getWorld() + "," + this.getX() + "," + this.getZ();
    }
}
