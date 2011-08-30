package com.dumptruckman.chunky.event.object;


import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPlayer;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerChunkChangeEvent extends ChunkyPlayerEvent{

    private ChunkyChunk toChunk;
    private ChunkyChunk fromChunk;

    public ChunkyPlayerChunkChangeEvent(final ChunkyPlayer chunkyPlayer, ChunkyChunk toChunk, ChunkyChunk fromChunk) {
        super(Type.PLAYER_CHUNK_CHANGE, chunkyPlayer);
        this.toChunk = toChunk;
        this.fromChunk = fromChunk;
    }

    public ChunkyChunk getToChunk() {
        return this.toChunk;
    }

    public ChunkyChunk getFromChunk() {
        return this.fromChunk;
    }
}
