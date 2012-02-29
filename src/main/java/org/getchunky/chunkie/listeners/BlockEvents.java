package org.getchunky.chunkie.listeners;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.getchunky.chunkie.Chunky;
import org.getchunky.chunkie.ChunkyManager;
import org.getchunky.chunkie.event.object.player.ChunkyPlayerBuildEvent;
import org.getchunky.chunkie.event.object.player.ChunkyPlayerDestroyEvent;
import org.getchunky.chunkie.module.ChunkyPermissions;
import org.getchunky.chunkie.object.IChunkyChunk;
import org.getchunky.chunkie.object.IChunkyPlayer;
import org.getchunky.chunkie.permission.AccessLevel;
import org.getchunky.chunkie.permission.PermissionChain;
import org.getchunky.chunkie.util.Logging;

public class BlockEvents extends BlockListener {

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!ChunkyManager.getChunkyWorld(event.getBlock().getWorld().getName()).isEnabled()) return;

        IChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
        IChunkyChunk chunk = ChunkyManager.getChunkyChunk(event.getBlock().getLocation());

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

        IChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
        IChunkyChunk chunk = ChunkyManager.getChunkyChunk(event.getBlock().getLocation());

        AccessLevel permType = PermissionChain.hasPerm(chunk, chunkyPlayer, ChunkyPermissions.DESTROY);
        Logging.debug(permType + " caused block break denial: " + permType.causedDenial());
        Boolean isCancelled = permType.causedDenial();

        ChunkyPlayerDestroyEvent chunkyEvent = new ChunkyPlayerDestroyEvent(chunkyPlayer, chunk, event.getBlock(), permType);
        chunkyEvent.setCancelled(isCancelled);
        Chunky.getModuleManager().callEvent(chunkyEvent);

        event.setCancelled(chunkyEvent.isCancelled());
    }
}
