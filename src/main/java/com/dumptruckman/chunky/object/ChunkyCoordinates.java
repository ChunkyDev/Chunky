package com.dumptruckman.chunky.object;


import com.dumptruckman.chunky.Chunky;
import org.bukkit.Location;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyCoordinates {
    
    private int x;
    private int z;
    private String world;

    /**
     * Creates a ChunkyCoordinates object with the given Location object.
     * @param location Location object for ChunkyCoordinates
     */
    public ChunkyCoordinates(Location location)
    {
        this(location.getWorld().getName(), location.getX(), location.getZ());

    }

    /**
     * Creates a ChunkyCoordinates object with the given world name and x and z values.
     * @param world World block is in
     * @param x Block x
     * @param z Block z
     */
    public ChunkyCoordinates(String world, double x, double z)
    {
        this(world, (int)(x/16) - ((x<0) ? 1 : 0), (int)(z/16) - ((z<0) ? 1 : 0));
    }

    /**
     * Creates a ChunkyCoordinates object with the given world value and CHUNK X and Z values.
     * 
     * @param world World chunk is in
     * @param x Chunk X (NOT BLOCK)
     * @param z Chunk Z (NOT BLOCK)
     */
    public ChunkyCoordinates(String world, int x, int z)
    {
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChunkyCoordinates)) return false;
        ChunkyCoordinates coords = (ChunkyCoordinates)obj;
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
