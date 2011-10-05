package org.getchunky.chunky.event.object.player;

import org.getchunky.chunky.event.ChunkyEvent;
import org.getchunky.chunky.object.ChunkyPlayer;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerEvent extends ChunkyEvent {

    protected ChunkyPlayer chunkyPlayer;

    public ChunkyPlayerEvent(final Type type, ChunkyPlayer chunkyPlayer) {
        super(type);
        this.chunkyPlayer = chunkyPlayer;
    }

    /**
     * Returns the player involved in this event.
     *
     * @return
     */
    public final ChunkyPlayer getChunkyPlayer() {
        return this.chunkyPlayer;
    }

}
