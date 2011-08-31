package com.dumptruckman.chunky.object;

import java.util.HashSet;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyChunk extends ChunkyObject {

    private ChunkyCoordinates coord;

    public ChunkyChunk(ChunkyCoordinates coord) {
        super(null);
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
