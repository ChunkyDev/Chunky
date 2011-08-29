package com.dumptruckman.chunky.object;


import org.bukkit.Location;

public class ChunkyCoordinates {
    private int X;
    private int Z;

    public ChunkyCoordinates(Location location)
    {
        this.X = (int)(location.getX()/16);
        this.Z = (int)(location.getZ()/16);
    }

    public int getX() {
        return this.X;
    }

    public int getZ() {
        return this.Z;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ChunkyCoordinates)) return false;
        ChunkyCoordinates coords = (ChunkyCoordinates)obj;
        return coords.getX() == this.X && coords.getZ() == this.Z;
    }

    @Override
    public int hashCode() {
        return (this.getX() + "," + this.getZ()).hashCode();
    }
}
