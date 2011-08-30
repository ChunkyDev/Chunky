package com.dumptruckman.chunky.event.object;


import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPlayer;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerChunkChangeEvent extends ChunkyPlayerEvent{

    private ChunkyChunk toChunk;
    private ChunkyChunk fromChunk;
    private String message;

    public ChunkyPlayerChunkChangeEvent(final ChunkyPlayer chunkyPlayer, final ChunkyChunk toChunk, final ChunkyChunk fromChunk, final String message) {
        super(Type.PLAYER_CHUNK_CHANGE, chunkyPlayer);
        this.toChunk = toChunk;
        this.fromChunk = fromChunk;
        this.message = message;
    }

    public ChunkyChunk getToChunk() {
        return this.toChunk;
    }

    public ChunkyChunk getFromChunk() {
        return this.fromChunk;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
