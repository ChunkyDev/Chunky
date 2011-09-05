package com.dumptruckman.chunky.event.object.player;

import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPlayer;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerChunkEvent extends ChunkyPlayerEvent {

    private ChunkyChunk chunkyChunk;

    public ChunkyPlayerChunkEvent(Type type, ChunkyPlayer chunkyPlayer, ChunkyChunk chunkyChunk) {
        super(type, chunkyPlayer);
        this.chunkyChunk = chunkyChunk;
    }

    public ChunkyChunk getChunkyChunk() {
        return this.chunkyChunk;
    }
}
