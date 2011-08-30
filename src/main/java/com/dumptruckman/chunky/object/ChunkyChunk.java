package com.dumptruckman.chunky.object;

import java.util.HashSet;

/**
 * @author dumptruckman
 */
public class ChunkyChunk extends ChunkyObject {

    private ChunkyCoordinates coord;
    private HashSet<ChunkyChunkOwner> owners;

    public ChunkyChunk(ChunkyCoordinates coord) {
        this.coord = coord;
        this.owners = new HashSet<ChunkyChunkOwner>();
    }

    public void setCoord(ChunkyCoordinates coord) {
        this.coord = coord;
    }

    public ChunkyCoordinates getCoord() {
        return coord;
    }

    public void addOwner(ChunkyChunkOwner owner) {
        if (!owners.contains(owner)) {
            owners.add(owner);
        } else {
            // TODO
        }
    }

    public boolean removeOwner(ChunkyChunkOwner owner) {
        return owners.remove(owner);
    }

    public boolean isOwner(ChunkyChunkOwner owner) {
        return owners.contains(owner);
    }

    public int hashCode() {
        return getCoord().hashCode();
    }
}
