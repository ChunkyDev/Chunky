package com.dumptruckman.chunky.event.object.player;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

public class ChunkyPlayerItemUseEvent extends ChunkyPlayerEvent implements Cancellable{

    private ChunkyChunk chunkyChunk;
    private ItemStack itemUsed;
    private boolean cancel = true;

    public ChunkyPlayerItemUseEvent(final ChunkyPlayer chunkyPlayer, final ChunkyChunk chunkyChunk, final ItemStack itemUsed) {
        super(Type.PLAYER_UNOWNED_BUILD, chunkyPlayer);
        this.chunkyChunk = chunkyChunk;
        this.itemUsed= itemUsed;
    }

    public ChunkyChunk getChunkyChunk() {
        return this.chunkyChunk;
    }

    public ItemStack getItemUsed() {
        return this.itemUsed;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean b) {
        cancel = b;
    }
}

