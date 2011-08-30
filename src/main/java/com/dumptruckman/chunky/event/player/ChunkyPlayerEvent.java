package com.dumptruckman.chunky.event.player;

import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.object.ChunkyPlayer;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerEvent extends ChunkyEvent{

    protected ChunkyPlayer chunkyPlayer;

    public ChunkyPlayerEvent(final Type type, ChunkyPlayer chunkyPlayer) {
        super(type);
        this.chunkyPlayer = chunkyPlayer;
    }

    /**
     * Returns the player involved in this event.
     * @return
     */
    public final ChunkyPlayer getChunkyPlayer() {
        return this.chunkyPlayer;
    }

}
