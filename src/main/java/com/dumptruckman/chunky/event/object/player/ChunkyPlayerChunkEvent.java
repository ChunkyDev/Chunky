package com.dumptruckman.chunky.event.object.player;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.permission.ChunkyPermissionType;
import com.dumptruckman.chunky.object.ChunkyPlayer;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerChunkEvent extends ChunkyPlayerEvent {

    private ChunkyChunk chunkyChunk;
    private ChunkyPermissionType permissionType;

    public ChunkyPlayerChunkEvent(Type type, ChunkyPlayer chunkyPlayer, ChunkyChunk chunkyChunk, ChunkyPermissionType permissionType) {
        super(type, chunkyPlayer);
        this.chunkyChunk = chunkyChunk;
        this.permissionType = permissionType;
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
     * Returns the ChunkyPermissionType of this event.
     * @see ChunkyPermissionType
     *
     * @return Type of permission for event, if any
     */
    public ChunkyPermissionType getPermissionType() {
        return permissionType;
    }
}
