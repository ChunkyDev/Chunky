package org.getchunky.chunkie.event.object.player;

import org.getchunky.chunkie.event.ChunkyEvent;
import org.getchunky.chunkie.object.IChunkyPlayer;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerEvent extends ChunkyEvent {

    protected IChunkyPlayer chunkyPlayer;

    public ChunkyPlayerEvent(final Type type, IChunkyPlayer chunkyPlayer) {
        super(type);
        this.chunkyPlayer = chunkyPlayer;
    }

    /**
     * Returns the player involved in this event.
     *
     * @return
     */
    public final IChunkyPlayer getChunkyPlayer() {
        return this.chunkyPlayer;
    }

}
