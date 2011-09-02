package com.dumptruckman.chunky.listeners;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.event.object.player.ChunkyPlayerUnownedBreakEvent;
import com.dumptruckman.chunky.event.object.player.ChunkyPlayerUnownedBuildEvent;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockEvents extends BlockListener {
    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer().getName());
        ChunkyChunk chunk = ChunkyManager.getChunk(event.getBlock().getLocation());
        if(!chunk.isOwnedBy(chunkyPlayer)) onUnownedChunkBuild(event, chunkyPlayer, chunk);
    }

    public void onUnownedChunkBuild(BlockPlaceEvent event, ChunkyPlayer builder, ChunkyChunk chunkyChunk) {
        ChunkyPlayerUnownedBuildEvent chunkyEvent = new ChunkyPlayerUnownedBuildEvent(builder, chunkyChunk, event.getBlock());
        Chunky.getModuleManager().callEvent(chunkyEvent);
        event.setCancelled(chunkyEvent.isCancelled());
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer().getName());
        ChunkyChunk chunk  = ChunkyManager.getChunk(event.getBlock().getLocation());
        if(!chunk.isOwnedBy(chunkyPlayer)) onUnownedChunkBreak(event, chunkyPlayer, chunk);
    }

    public void onUnownedChunkBreak(BlockBreakEvent event, ChunkyPlayer breaker, ChunkyChunk chunkyChunk) {
        event.setCancelled(true);
        ChunkyPlayerUnownedBreakEvent chunkyEvent = new ChunkyPlayerUnownedBreakEvent(breaker,chunkyChunk,event.getBlock());
        Chunky.getModuleManager().callEvent(chunkyEvent);
        event.setCancelled(chunkyEvent.isCancelled());
    }
}
