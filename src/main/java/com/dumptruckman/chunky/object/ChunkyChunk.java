package com.dumptruckman.chunky.object;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyChunk extends ChunkyPermissionsObject {

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
}
