package com.dumptruckman.chunky.object;

import com.dumptruckman.chunky.persistance.DatabaseManager;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyChunk extends ChunkyObject {

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

    @Override
    public void setName(String name) {
        super.setName(name);
        DatabaseManager.updateChunk(this);
    }

    public int hashCode() {
        return (getType() + ":" + getCoord().toString()).hashCode();
    }

    public boolean equals(Object obj) {
        return obj != null && obj instanceof ChunkyChunk && ((ChunkyChunk) obj).getCoord().equals(this.getCoord());
    }
}
