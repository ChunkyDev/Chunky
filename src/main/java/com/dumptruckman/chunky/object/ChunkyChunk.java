package com.dumptruckman.chunky.object;

/**
 * @author dumptruckman
 */
public class ChunkyChunk extends ChunkyObject {

    ChunkyCoordinates coord;

    public ChunkyChunk(ChunkyCoordinates coord) {
        this.coord = coord;
    }

    public void setCoord(ChunkyCoordinates coord) {
        this.coord = coord;
    }

    public ChunkyCoordinates getCoord() {
        return coord;
    }
}
