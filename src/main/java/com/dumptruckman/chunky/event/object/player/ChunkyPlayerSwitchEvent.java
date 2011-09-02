package com.dumptruckman.chunky.event.object.player;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerSwitchEvent extends ChunkyPlayerChunkEvent implements Cancellable {

    private ItemStack item;
    private boolean cancel = false;

    public ChunkyPlayerSwitchEvent(ChunkyPlayer chunkyPlayer, ChunkyChunk chunkyChunk, ItemStack item) {
        super(Type.PLAYER_SWITCH, chunkyPlayer, chunkyChunk);
        this.item = item;
    }

    public final ItemStack getItem() {
        return item;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean b) {
        cancel = b;
    }
}
