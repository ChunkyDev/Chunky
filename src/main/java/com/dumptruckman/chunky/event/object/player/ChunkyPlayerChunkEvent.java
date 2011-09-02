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
        super(ChunkyEvent.Type.PLAYER_UNOWNED_BUILD, chunkyPlayer);
        this.chunkyChunk = chunkyChunk;
    }

    public ChunkyChunk getChunkyChunk() {
        return this.chunkyChunk;
    }
}
