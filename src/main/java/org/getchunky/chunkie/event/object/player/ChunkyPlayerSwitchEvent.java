package org.getchunky.chunkie.event.object.player;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.getchunky.chunkie.object.IChunkyChunk;
import org.getchunky.chunkie.object.IChunkyPlayer;
import org.getchunky.chunkie.permission.AccessLevel;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerSwitchEvent extends ChunkyPlayerChunkEvent implements Cancellable {

    private Block switchedBlock;
    private boolean cancel = false;

    public ChunkyPlayerSwitchEvent(IChunkyPlayer chunkyPlayer, IChunkyChunk chunkyChunk, Block switchedBlock, AccessLevel accessLevel) {
        super(Type.PLAYER_SWITCH, chunkyPlayer, chunkyChunk, accessLevel);
        this.switchedBlock = switchedBlock;
    }

    public final Block getSwitchedBlock() {
        return this.switchedBlock;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param b true if you wish to cancel this event
     */
    public void setCancelled(boolean b) {
        cancel = b;
    }
}
