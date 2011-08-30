package com.dumptruckman.chunky.object;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayer extends ChunkyEntity implements ChunkyChunkOwner {
    private ChunkyChunk lastChunk;
    private String name;

    public ChunkyPlayer(String name) {
        setName(name);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastChunk(ChunkyChunk chunk) {
        this.lastChunk = chunk;
    }

    public ChunkyChunk getLastChunk() {
        return lastChunk;
    }
}
