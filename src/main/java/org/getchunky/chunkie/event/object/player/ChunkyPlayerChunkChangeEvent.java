package org.getchunky.chunkie.event.object.player;


import org.getchunky.chunkie.object.IChunkyChunk;
import org.getchunky.chunkie.object.IChunkyPlayer;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerChunkChangeEvent extends ChunkyPlayerEvent {

    private IChunkyChunk toChunk;
    private IChunkyChunk fromChunk;
    private String message;
    private String toChunkDisplayName;

    public ChunkyPlayerChunkChangeEvent(final IChunkyPlayer chunkyPlayer, final IChunkyChunk toChunk, final IChunkyChunk fromChunk, final String message) {
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
    public IChunkyChunk getToChunk() {
        return this.toChunk;
    }

    /**
     * Gets the chunk the player is travelling from.
     *
     * @return ChunkyChunk
     */
    public IChunkyChunk getFromChunk() {
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
