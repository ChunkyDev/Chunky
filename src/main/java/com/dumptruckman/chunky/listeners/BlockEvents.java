package com.dumptruckman.chunky.listeners;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.event.object.player.ChunkyPlayerUnownedBreakEvent;
import com.dumptruckman.chunky.event.object.player.ChunkyPlayerUnownedBuildEvent;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPermissionType;
import com.dumptruckman.chunky.object.ChunkyPermissions;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockEvents extends BlockListener {

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer().getName());
        ChunkyChunk chunk = ChunkyManager.getChunk(event.getBlock().getLocation());

        boolean isCancelled = true;
        ChunkyPermissionType permType = ChunkyPermissionType.NONE;

        // Permission chain
        if (chunk.isOwnedBy(chunkyPlayer)) {
            permType = ChunkyPermissionType.OWNER;
            if (chunk.isDirectlyOwnnedBy(chunkyPlayer)) permType = ChunkyPermissionType.DIRECT_OWNER;
            isCancelled = false;
        } else if (chunkyPlayer.hasPerm(chunk, ChunkyPermissions.Flags.BUILD)) {
            permType = ChunkyPermissionType.PERMISSION;
            isCancelled = false;
        }
        //Groups here?:

        ChunkyPlayerUnownedBuildEvent chunkyEvent = new ChunkyPlayerUnownedBuildEvent(chunkyPlayer, chunk, event.getBlock(), permType);
        chunkyEvent.setCancelled(isCancelled);
        Chunky.getModuleManager().callEvent(chunkyEvent);

        event.setCancelled(chunkyEvent.isCancelled());
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer().getName());
        ChunkyChunk chunk  = ChunkyManager.getChunk(event.getBlock().getLocation());

        boolean isCancelled = true;
        ChunkyPermissionType permType = ChunkyPermissionType.NONE;

        // Permission chain
        if (chunk.isOwnedBy(chunkyPlayer)) {
            permType = ChunkyPermissionType.OWNER;
            if (chunk.isDirectlyOwnnedBy(chunkyPlayer)) permType = ChunkyPermissionType.DIRECT_OWNER;
            isCancelled = false;
        } else if (chunkyPlayer.hasPerm(chunk, ChunkyPermissions.Flags.DESTROY)) {
            permType = ChunkyPermissionType.PERMISSION;
            isCancelled = false;
        }
        //Groups here?:

        ChunkyPlayerUnownedBreakEvent chunkyEvent = new ChunkyPlayerUnownedBreakEvent(chunkyPlayer, chunk, event.getBlock(), permType);
        chunkyEvent.setCancelled(isCancelled);
        Chunky.getModuleManager().callEvent(chunkyEvent);
        
        event.setCancelled(chunkyEvent.isCancelled());
    }
}
