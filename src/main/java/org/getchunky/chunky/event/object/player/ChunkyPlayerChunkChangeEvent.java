package org.getchunky.chunky.event.object.player;


import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyPlayer;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerChunkChangeEvent extends ChunkyPlayerEvent {

    private ChunkyChunk toChunk;
    private ChunkyChunk fromChunk;
    private String message;
    private String toChunkDisplayName;

    public ChunkyPlayerChunkChangeEvent(final ChunkyPlayer chunkyPlayer, final ChunkyChunk toChunk, final ChunkyChunk fromChunk, final String message) {
        super(Type.PLAYER_CHUNK_CHANGE, chunkyPlayer);
        this.toChunk = toChunk;
        this.fromChunk = fromChunk;
        this.message = message;
        toChunkDisplayName = toChunk.getChunkDisplayName();
    }

    /**
     * Gets the chunk the player is travelling into.
     *
     * @return ChunkyChunk
     */
    public ChunkyChunk getToChunk() {
        return this.toChunk;
    }

    /**
     * Gets the chunk the player is travelling from.
     *
     * @return ChunkyChunk
     */
    public ChunkyChunk getFromChunk() {
        return this.fromChunk;
    }

    /**
     * Gets the message that is sent to the player on a chunk change.
     *
     * @return String
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Sets the message that is sent to the player on a chunk change.
     *
     * @param message Message to set for this chunk change
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the name that would be displayed for the to chunk if the previous chunk's display name is different.
     *
     * @return display name of to chunk
     */
    public String getToChunkDisplayName() {
        return this.toChunkDisplayName;
    }
}
