package org.getchunky.chunky.listeners;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.event.object.player.ChunkyPlayerBuildEvent;
import org.getchunky.chunky.event.object.player.ChunkyPlayerDestroyEvent;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.ChunkyAccessLevel;
import org.getchunky.chunky.permission.ChunkyPermissionChain;
import org.getchunky.chunky.permission.ChunkyPermissions;
import org.getchunky.chunky.permission.bukkit.Permissions;

public class BlockEvents extends BlockListener {

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!Permissions.ENABLED.hasPerm(event.getPlayer())) return;

        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
        ChunkyChunk chunk = ChunkyManager.getChunk(event.getBlock().getLocation());

        ChunkyAccessLevel permType = ChunkyPermissionChain.hasPerm(chunk, chunkyPlayer, ChunkyPermissions.Flags.BUILD);
        Boolean isCancelled = permType.causedDenial();

        ChunkyPlayerBuildEvent chunkyEvent = new ChunkyPlayerBuildEvent(chunkyPlayer, chunk, event.getBlock(), permType);
        chunkyEvent.setCancelled(isCancelled);
        Chunky.getModuleManager().callEvent(chunkyEvent);

        event.setCancelled(chunkyEvent.isCancelled());
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (!Permissions.ENABLED.hasPerm(event.getPlayer())) return;

        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
        ChunkyChunk chunk = ChunkyManager.getChunk(event.getBlock().getLocation());

        ChunkyAccessLevel permType = ChunkyPermissionChain.hasPerm(chunk, chunkyPlayer, ChunkyPermissions.Flags.DESTROY);
        Boolean isCancelled = permType.causedDenial();

        ChunkyPlayerDestroyEvent chunkyEvent = new ChunkyPlayerDestroyEvent(chunkyPlayer, chunk, event.getBlock(), permType);
        chunkyEvent.setCancelled(isCancelled);
        Chunky.getModuleManager().callEvent(chunkyEvent);

        event.setCancelled(chunkyEvent.isCancelled());
    }
}
