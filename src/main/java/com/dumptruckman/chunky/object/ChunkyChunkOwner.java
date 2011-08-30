package com.dumptruckman.chunky.object;

import java.util.HashSet;

/**
 * @author dumptruckman, SwearWord
 */
public interface ChunkyChunkOwner extends ChunkyEntity {

    public void addChunk(ChunkyChunk chunk);

    public boolean removeChunk(ChunkyChunk chunk);

    public boolean ownsChunk(ChunkyChunk chunk);

    public HashSet<ChunkyChunk> getOwnedChunks();
}
