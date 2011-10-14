package org.getchunky.chunky.listeners;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.event.object.player.ChunkyPlayerBuildEvent;
import org.getchunky.chunky.event.object.player.ChunkyPlayerDestroyEvent;
import org.getchunky.chunky.module.ChunkyPermissions;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.AccessLevel;
import org.getchunky.chunky.permission.PermissionChain;
import org.getchunky.chunky.util.Logging;

public class BlockEvents extends BlockListener {

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!ChunkyManager.getChunkyWorld(event.getBlock().getWorld().getName()).isEnabled()) return;

        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
        ChunkyChunk chunk = ChunkyManager.getChunkyChunk(event.getBlock().getLocation());

        AccessLevel permType = PermissionChain.hasPerm(chunk, chunkyPlayer, ChunkyPermissions.BUILD);
        Logging.debug(permType + " caused block place denial: " + permType.causedDenial());
        Boolean isCancelled = permType.causedDenial();

        ChunkyPlayerBuildEvent chunkyEvent = new ChunkyPlayerBuildEvent(chunkyPlayer, chunk, event.getBlock(), permType);
        chunkyEvent.setCancelled(isCancelled);
        Chunky.getModuleManager().callEvent(chunkyEvent);

        event.setCancelled(chunkyEvent.isCancelled());
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (!ChunkyManager.getChunkyWorld(event.getBlock().getWorld().getName()).isEnabled()) return;

        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
        ChunkyChunk chunk = ChunkyManager.getChunkyChunk(event.getBlock().getLocation());

        AccessLevel permType = PermissionChain.hasPerm(chunk, chunkyPlayer, ChunkyPermissions.DESTROY);
        Logging.debug(permType + " caused block break denial: " + permType.causedDenial());
        Boolean isCancelled = permType.causedDenial();

        ChunkyPlayerDestroyEvent chunkyEvent = new ChunkyPlayerDestroyEvent(chunkyPlayer, chunk, event.getBlock(), permType);
        chunkyEvent.setCancelled(isCancelled);
        Chunky.getModuleManager().callEvent(chunkyEvent);

        event.setCancelled(chunkyEvent.isCancelled());
    }
}
