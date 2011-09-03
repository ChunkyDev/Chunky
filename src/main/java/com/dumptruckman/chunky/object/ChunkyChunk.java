package com.dumptruckman.chunky.object;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyChunk extends ChunkyObject implements Sanctionable {

    private ChunkyCoordinates coord;

    public ChunkyChunk(ChunkyCoordinates coord) {
        super("");
        this.coord = coord;
    }

    public void setCoord(ChunkyCoordinates coord) {
        this.coord = coord;
    }

    public ChunkyCoordinates getCoord() {
        return coord;
    }

    public int hashCode() {
        return (getType() + ":" + getCoord().toString()).hashCode();
    }

    public boolean equals(Object obj) {
        return obj != null && obj instanceof ChunkyChunk && ((ChunkyChunk) obj).getCoord().equals(this.getCoord());
    }

    public boolean hasBuildPermission(ChunkyObject chunkyObject) {
        return true; // TODO
    }

    public boolean hasDestroyPermission(ChunkyObject chunkyObject) {
        return true; // TODO
    }

    public boolean hasItemPermission(ChunkyObject chunkyObject) {
        return true; // TODO
    }

    public boolean hasSwitchPermission(ChunkyObject chunkyObject) {
        return true; // TODO
    }
}
