package com.dumptruckman.chunky.object;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayer extends ChunkyEntity implements ChunkyChunkOwner {
    private ChunkyChunk lastChunk;

    public ChunkyPlayer() {

    }

    public void setLastChunk(ChunkyChunk chunk) {
        this.lastChunk = chunk;
    }

    public ChunkyChunk getLastChunk() {
        return lastChunk;
    }
}
