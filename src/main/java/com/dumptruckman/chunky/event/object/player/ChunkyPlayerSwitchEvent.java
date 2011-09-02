package com.dumptruckman.chunky.event.object.player;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerSwitchEvent extends ChunkyPlayerChunkEvent implements Cancellable {

    private Block switchedBlock;
    private boolean cancel = false;

    public ChunkyPlayerSwitchEvent(ChunkyPlayer chunkyPlayer, ChunkyChunk chunkyChunk, Block switchedBlock) {
        super(Type.PLAYER_SWITCH, chunkyPlayer, chunkyChunk);
        this.switchedBlock = switchedBlock;
    }

    public final Block getSwitchedBlock() {
        return this.switchedBlock;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean b) {
        cancel = b;
    }
}
