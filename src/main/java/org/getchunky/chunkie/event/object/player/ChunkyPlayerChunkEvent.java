package org.getchunky.chunkie.event.object.player;

import org.getchunky.chunkie.object.IChunkyChunk;
import org.getchunky.chunkie.object.IChunkyPlayer;
import org.getchunky.chunkie.permission.AccessLevel;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerChunkEvent extends ChunkyPlayerEvent {

    private IChunkyChunk chunkyChunk;
    private AccessLevel accessLevel;

    public ChunkyPlayerChunkEvent(Type type, IChunkyPlayer chunkyPlayer, IChunkyChunk chunkyChunk, AccessLevel accessLevel) {
        super(type, chunkyPlayer);
        this.chunkyChunk = chunkyChunk;
        this.accessLevel = accessLevel;
    }

    /**
     * Gets the ChunkyChunk involved in this event.
     *
     * @return ChunkyChunk involved in this event
     */
    public IChunkyChunk getChunkyChunk() {
        return this.chunkyChunk;
    }

    /**
     * Returns the AccessLevel of this event.
     *
     * @return Type of permission for event, if any
     * @see org.getchunky.chunkie.permission.AccessLevel
     */
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }
}
