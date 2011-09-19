package com.dumptruckman.chunky.object;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyChunk extends ChunkyPermissionsObject {

    private ChunkyCoordinates coord;

    public ChunkyChunk(ChunkyCoordinates coord) {
        super("", coord.toString());
        this.coord = coord;
    }

    public void setCoord(ChunkyCoordinates coord) {
        this.coord = coord;
    }

    public ChunkyCoordinates getCoord() {
        return coord;
    }
}
