package org.getchunky.chunkie.listeners;

import org.bukkit.Location;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.getchunky.chunkie.Chunky;
import org.getchunky.chunkie.ChunkyManager;
import org.getchunky.chunkie.config.Config;
import org.getchunky.chunkie.event.object.player.ChunkyPlayerChunkChangeEvent;
import org.getchunky.chunkie.event.object.player.ChunkyPlayerItemUseEvent;
import org.getchunky.chunkie.event.object.player.ChunkyPlayerSwitchEvent;
import org.getchunky.chunkie.locale.Language;
import org.getchunky.chunkie.module.ChunkyPermissions;
import org.getchunky.chunkie.object.IChunkyChunk;
import org.getchunky.chunkie.object.IChunkyPlayer;
import org.getchunky.chunkie.permission.AccessLevel;
import org.getchunky.chunkie.permission.PermissionChain;
import org.getchunky.chunkie.util.MinecraftTools;

/**
 * @author dumptruckman, SwearWord
 */
public class PlayerEvents extends PlayerListener {

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;
        if (!ChunkyManager.getChunkyWorld(event.getTo().getWorld().getName()).isEnabled()) return;
        IChunkyChunk toChunk = ChunkyManager.getChunkyChunk(event.getTo());
        IChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
        IChunkyChunk fromChunk = ChunkyManager.getChunkyChunk(event.getFrom());
        if (fromChunk.equals(toChunk)) return;
        onPlayerChunkChange(chunkyPlayer, toChunk, fromChunk);
    }

    public void onPlayerChunkChange(IChunkyPlayer chunkyPlayer, IChunkyChunk toChunk, IChunkyChunk fromChunk) {
        try {
            if (Config.isDebugging()) {
                Location loc = chunkyPlayer.getPlayer().getLocation();
                //Logging.debug(chunkyPlayer.getName() + " x: " + loc.getX() + "  z: " + loc.getZ());
            }
        } catch (Exception ignore) {
        }
        //Logging.debug(chunkyPlayer.getName() + " moved to chunk: [" + toChunk.getCoord().getX() + ", "+ toChunk.getCoord().getZ() + "]");
        String message = "";
        if (!toChunk.isOwned()) {
            if (fromChunk.isOwned())
                message = " " + Language.UNREGISTERED_CHUNK_NAME.getString();
        } else if (fromChunk.isOwned()) {
            if (fromChunk.getOwner().equals(toChunk.getOwner())) {
                if (!fromChunk.getName().equals(toChunk.getName()))
                    message += toChunk.getChunkDisplayName();
            } else
                message += toChunk.getChunkDisplayName();
        } else
            message += toChunk.getChunkDisplayName();

        ChunkyPlayerChunkChangeEvent event = new ChunkyPlayerChunkChangeEvent(chunkyPlayer, toChunk, fromChunk, message);
        Chunky.getModuleManager().callEvent(event);
        chunkyPlayer.setCurrentChunk(toChunk);
        if (IChunkyPlayer.getClaimModePlayers().contains(chunkyPlayer)) {
            chunkyPlayer.claimCurrentChunk();
        } else {
            if (!event.getMessage().equals("")) Language.sendMessage(chunkyPlayer, event.getMessage());
        }
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        IChunkyPlayer cPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
        cPlayer.setCurrentChunk(ChunkyManager.getChunkyChunk(event.getPlayer().getLocation()));
        Long currentTime = System.currentTimeMillis();
        if (cPlayer.getFirstLoginTime() == 0) {
            cPlayer.getData().put("first login time", currentTime);
        }
        cPlayer.getData().put("last login time", currentTime);
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        IChunkyPlayer.getClaimModePlayers().remove(ChunkyManager.getChunkyPlayer(event.getPlayer()));
        IChunkyPlayer cPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
        cPlayer.getData().put("last logout time", System.currentTimeMillis());
    }

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;
        String[] commands = event.getMessage().split("\\s");
        Chunky.getModuleManager().parseCommand(event.getPlayer(), commands);
    }



    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!ChunkyManager.getChunkyWorld(event.getPlayer().getWorld().getName()).isEnabled()) return;

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (event.getItem() != null && MinecraftTools.isUsable(event.getItem().getTypeId())) {
                IChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
                IChunkyChunk chunkyChunk = ChunkyManager.getChunkyChunk(event.getClickedBlock().getLocation());

                AccessLevel permType = PermissionChain.hasPerm(chunkyChunk, chunkyPlayer, ChunkyPermissions.ITEM_USE);
                boolean isCancelled = permType.causedDenial();

                ChunkyPlayerItemUseEvent chunkyEvent = new ChunkyPlayerItemUseEvent(chunkyPlayer, chunkyChunk, event.getItem(), permType);
                chunkyEvent.setCancelled(isCancelled);
                Chunky.getModuleManager().callEvent(chunkyEvent);

                event.setCancelled(chunkyEvent.isCancelled());
            }
            if (MinecraftTools.isSwitchable(event.getClickedBlock().getTypeId())) {
                IChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
                IChunkyChunk chunkyChunk = ChunkyManager.getChunkyChunk(event.getClickedBlock().getLocation());

                AccessLevel permType = PermissionChain.hasPerm(chunkyChunk, chunkyPlayer, ChunkyPermissions.SWITCH);
                boolean isCancelled = permType.causedDenial();

                ChunkyPlayerSwitchEvent chunkyEvent = new ChunkyPlayerSwitchEvent(chunkyPlayer, chunkyChunk, event.getClickedBlock(), permType);
                chunkyEvent.setCancelled(isCancelled);
                Chunky.getModuleManager().callEvent(chunkyEvent);

                event.setCancelled(chunkyEvent.isCancelled());
            }
        }

    }
}
