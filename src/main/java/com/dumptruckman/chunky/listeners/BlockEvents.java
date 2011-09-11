package com.dumptruckman.chunky.listeners;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.event.object.player.ChunkyPlayerDestroyEvent;
import com.dumptruckman.chunky.event.object.player.ChunkyPlayerBuildEvent;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.permission.ChunkyPermissionChain;
import com.dumptruckman.chunky.permission.ChunkyAccessLevel;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.permission.bukkit.Permissions;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockEvents extends BlockListener {

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!Permissions.ENABLED.hasPerm(event.getPlayer())) return;

        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer().getName());
        ChunkyChunk chunk = ChunkyManager.getChunk(event.getBlock().getLocation());

        boolean isCancelled = true;
        ChunkyAccessLevel permType = ChunkyAccessLevel.NONE;

        isCancelled = ChunkyPermissionChain.check(chunk, chunkyPlayer, permType);

        ChunkyPlayerBuildEvent chunkyEvent = new ChunkyPlayerBuildEvent(chunkyPlayer, chunk, event.getBlock(), permType);
        chunkyEvent.setCancelled(isCancelled);
        Chunky.getModuleManager().callEvent(chunkyEvent);

        event.setCancelled(chunkyEvent.isCancelled());
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (!Permissions.ENABLED.hasPerm(event.getPlayer())) return;
        
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer().getName());
        ChunkyChunk chunk  = ChunkyManager.getChunk(event.getBlock().getLocation());

        boolean isCancelled = true;
        ChunkyAccessLevel permType = ChunkyAccessLevel.NONE;

        isCancelled = ChunkyPermissionChain.check(chunk, chunkyPlayer, permType);

        ChunkyPlayerDestroyEvent chunkyEvent = new ChunkyPlayerDestroyEvent(chunkyPlayer, chunk, event.getBlock(), permType);
        chunkyEvent.setCancelled(isCancelled);
        Chunky.getModuleManager().callEvent(chunkyEvent);
        
        event.setCancelled(chunkyEvent.isCancelled());
    }
}
