package com.dumptruckman.chunky.object;


import org.bukkit.Location;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyCoordinates {
    
    private int x;
    private int z;
    private String world;

    public ChunkyCoordinates(Location location)
    {
        double xPos = (location.getX()/16);
        double zPos = (location.getZ()/16);

        this.x = (int)xPos;
        this.z = (int)zPos;

        if (xPos < 0) {
            this.x--;
        }

        if (zPos < 0) {
            this.z--;
        }
        this.world = location.getWorld().getName();
    }

    public ChunkyCoordinates(String world, double x, double z)
    {
        this.world = world;
        this.x = (int)(x/16)-1;
        this.z = (int)(z/16)-1;
    }

    public ChunkyCoordinates(String world, int x, int z)
    {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

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
