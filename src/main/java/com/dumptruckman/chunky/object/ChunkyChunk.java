package com.dumptruckman.chunky.object;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyChunk extends ChunkyPermissionsObject {

    private ChunkyCoordinates coord;

    public ChunkyChunk(ChunkyCoordinates coord) {
        super(coord.toString());
        this.coord = coord;
    }

    /**
     * Sets the coordinates of this chunk.  You probably don't need to use this.
     * 
     * @param coord Coordinates of chunk
     */
    public final void setCoord(ChunkyCoordinates coord) {
        this.coord = coord;
    }

    /**
     * Gets the coordinates of this chunk.
     *
     * @return Chunk coordinates
     */
    public final ChunkyCoordinates getCoord() {
        return coord;
    }
}
