package org.getchunky.chunky.event.object.player;

import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.ChunkyAccessLevel;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerChunkEvent extends ChunkyPlayerEvent {

    private ChunkyChunk chunkyChunk;
    private ChunkyAccessLevel accessLevel;

    public ChunkyPlayerChunkEvent(Type type, ChunkyPlayer chunkyPlayer, ChunkyChunk chunkyChunk, ChunkyAccessLevel accessLevel) {
        super(type, chunkyPlayer);
        this.chunkyChunk = chunkyChunk;
        this.accessLevel = accessLevel;
    }

    /**
     * Gets the ChunkyChunk involved in this event.
     *
     * @return ChunkyChunk involved in this event
     */
    public ChunkyChunk getChunkyChunk() {
        return this.chunkyChunk;
    }

    /**
     * Returns the ChunkyAccessLevel of this event.
     *
     * @return Type of permission for event, if any
     * @see org.getchunky.chunky.permission.ChunkyAccessLevel
     */
    public ChunkyAccessLevel getAccessLevel() {
        return accessLevel;
    }
}
