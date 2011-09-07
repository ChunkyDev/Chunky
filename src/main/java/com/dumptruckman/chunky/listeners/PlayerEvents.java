package com.dumptruckman.chunky.listeners;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.event.object.player.ChunkyPlayerChunkChangeEvent;
import com.dumptruckman.chunky.event.object.player.ChunkyPlayerItemUseEvent;
import com.dumptruckman.chunky.event.object.player.ChunkyPlayerSwitchEvent;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPermissionType;
import com.dumptruckman.chunky.object.ChunkyPermissions;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.util.Logging;
import com.dumptruckman.chunky.util.MinecraftTools;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

/**
 * @author dumptruckman, SwearWord
 */
public class PlayerEvents extends PlayerListener{

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        if(event.isCancelled()) return;
        ChunkyChunk toChunk = ChunkyManager.getChunk(event.getTo());
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer().getName());
        ChunkyChunk fromChunk = ChunkyManager.getChunk(event.getFrom());
        if(fromChunk.equals(toChunk)) return;
        onPlayerChunkChange(chunkyPlayer,toChunk,fromChunk);
    }

    public void onPlayerChunkChange(ChunkyPlayer chunkyPlayer, ChunkyChunk toChunk, ChunkyChunk fromChunk) {
        Logging.debug(chunkyPlayer.getName() + " changed chunks.");
        String message = "";
        if(!toChunk.isOwned() && fromChunk.isOwned()) message += "Wilderness ";
        else if(toChunk.isOwned()) {
            if(fromChunk.isOwned() && !fromChunk.getName().equals(toChunk.getName()) ) message += toChunk.getName();
            else if(!fromChunk.isOwned()) message += toChunk.getName();
        }
        ChunkyPlayerChunkChangeEvent event = new ChunkyPlayerChunkChangeEvent(chunkyPlayer,toChunk,fromChunk,message);
        Chunky.getModuleManager().callEvent(event);
        if(!message.equals("")) Language.sendMessage(chunkyPlayer, event.getMessage());
        chunkyPlayer.setCurrentChunk(toChunk);
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        ChunkyManager.getChunkyPlayer(event.getPlayer().getName());
    }

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;
        String[] commands = event.getMessage().substring(1).split("\\s");
        Chunky.getModuleManager().parseCommand(event.getPlayer(), commands);
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(MinecraftTools.isUsable(event.getItem().getTypeId())) {
                ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer().getName());
                ChunkyChunk chunkyChunk = ChunkyManager.getChunk(event.getClickedBlock().getLocation());

                boolean isCancelled = true;
                ChunkyPermissionType permType = ChunkyPermissionType.NONE;

                // Permission chain
                if (chunkyChunk.isOwnedBy(chunkyPlayer)) {
                    permType = ChunkyPermissionType.OWNER;
                    if (chunkyChunk.isDirectlyOwnedBy(chunkyPlayer)) permType = ChunkyPermissionType.DIRECT_OWNER;
                    isCancelled = false;
                } else if (chunkyPlayer.hasPerm(chunkyChunk, ChunkyPermissions.Flags.ITEMUSE)) {
                    permType = ChunkyPermissionType.PERMISSION;
                    isCancelled = false;
                }
                //Groups here?:

                ChunkyPlayerItemUseEvent chunkyEvent = new ChunkyPlayerItemUseEvent(chunkyPlayer,chunkyChunk,event.getItem(), permType);
                chunkyEvent.setCancelled(isCancelled);
                Chunky.getModuleManager().callEvent(chunkyEvent);

                event.setCancelled(chunkyEvent.isCancelled());
            }
            if(MinecraftTools.isSwitchable(event.getClickedBlock().getTypeId())) {
                ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer().getName());
                ChunkyChunk chunkyChunk = ChunkyManager.getChunk(event.getClickedBlock().getLocation());

                boolean isCancelled = true;
                ChunkyPermissionType permType = ChunkyPermissionType.NONE;

                // Permission chain
                if (chunkyChunk.isOwnedBy(chunkyPlayer)) {
                    permType = ChunkyPermissionType.OWNER;
                    if (chunkyChunk.isDirectlyOwnedBy(chunkyPlayer)) permType = ChunkyPermissionType.DIRECT_OWNER;
                    isCancelled = false;
                } else if (chunkyPlayer.hasPerm(chunkyChunk, ChunkyPermissions.Flags.SWITCH)) {
                    permType = ChunkyPermissionType.PERMISSION;
                    isCancelled = false;
                }
                //Groups here?:

                ChunkyPlayerSwitchEvent chunkyEvent = new ChunkyPlayerSwitchEvent(chunkyPlayer,chunkyChunk,event.getClickedBlock(), permType);
                chunkyEvent.setCancelled(isCancelled);
                Chunky.getModuleManager().callEvent(chunkyEvent);

                event.setCancelled(chunkyEvent.isCancelled());
            }
        }
    }
}
