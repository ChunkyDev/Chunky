package com.dumptruckman.chunky.object;

import java.util.HashSet;

/**
 * @author dumptruckman, SwearWord
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
            owner.addChunk(this);
        } else {
            // TODO
        }
    }

    public boolean removeOwner(ChunkyChunkOwner owner) {
        if (owners.remove(owner)) {
            owner.removeChunk(this);
            // TODO error checking here? (in case owner for some reason didn't have the chunk)
            return true;
        } else {
            return false;
        }
    }

    public boolean isOwner(ChunkyChunkOwner owner) {
        return owners.contains(owner);
    }

    public int hashCode() {
        return getCoord().hashCode();
    }
}
