package com.dumptruckman.chunky.listeners;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.config.Config;
import com.dumptruckman.chunky.event.object.player.ChunkyPlayerChunkChangeEvent;
import com.dumptruckman.chunky.event.object.player.ChunkyPlayerItemUseEvent;
import com.dumptruckman.chunky.event.object.player.ChunkyPlayerSwitchEvent;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.permission.ChunkyAccessLevel;
import com.dumptruckman.chunky.permission.ChunkyPermissionChain;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.permission.ChunkyPermissions;
import com.dumptruckman.chunky.permission.bukkit.Permissions;
import com.dumptruckman.chunky.util.Logging;
import com.dumptruckman.chunky.util.MinecraftTools;
import org.bukkit.Location;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

/**
 * @author dumptruckman, SwearWord
 */
public class PlayerEvents extends PlayerListener{

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        if(event.isCancelled()) return;
        if (!Permissions.ENABLED.hasPerm(event.getPlayer())) return;
        ChunkyChunk toChunk = ChunkyManager.getChunk(event.getTo());
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
        ChunkyChunk fromChunk = ChunkyManager.getChunk(event.getFrom());
        if(fromChunk.equals(toChunk)) return;
        onPlayerChunkChange(chunkyPlayer,toChunk,fromChunk);
    }

    public void onPlayerChunkChange(ChunkyPlayer chunkyPlayer, ChunkyChunk toChunk, ChunkyChunk fromChunk) {
        try {
            if (Config.isDebugging()) {
                Location loc = chunkyPlayer.getPlayer().getLocation();
                //Logging.debug(chunkyPlayer.getName() + " x: " + loc.getX() + "  z: " + loc.getZ());
            }
        } catch (Exception ignore) {}
        //Logging.debug(chunkyPlayer.getName() + " moved to chunk: [" + toChunk.getCoord().getX() + ", "+ toChunk.getCoord().getZ() + "]");
        String message = "";
        if(!toChunk.isOwned() && fromChunk.isOwned()) message += Language.UNREGISTERED_CHUNK_NAME.getString();
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
        ChunkyPlayer cPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
        cPlayer.setCurrentChunk(ChunkyManager.getChunk(event.getPlayer().getLocation()));
    }

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;
        if (!Permissions.ENABLED.hasPerm(event.getPlayer())) return;
        String[] commands = event.getMessage().substring(1).split("\\s");
        Chunky.getModuleManager().parseCommand(event.getPlayer(), commands);
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!Permissions.ENABLED.hasPerm(event.getPlayer())) return;
        
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if(event.getItem() != null && MinecraftTools.isUsable(event.getItem().getTypeId())) {
                ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
                ChunkyChunk chunkyChunk = ChunkyManager.getChunk(event.getClickedBlock().getLocation());

                ChunkyAccessLevel permType = ChunkyAccessLevel.NONE;

                boolean isCancelled = !ChunkyPermissionChain.hasPerm(chunkyChunk, chunkyPlayer, ChunkyPermissions.Flags.ITEMUSE, permType);

                ChunkyPlayerItemUseEvent chunkyEvent = new ChunkyPlayerItemUseEvent(chunkyPlayer,chunkyChunk,event.getItem(), permType);
                chunkyEvent.setCancelled(isCancelled);
                Chunky.getModuleManager().callEvent(chunkyEvent);

                event.setCancelled(chunkyEvent.isCancelled());
            }
            if(MinecraftTools.isSwitchable(event.getClickedBlock().getTypeId())) {
                ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
                ChunkyChunk chunkyChunk = ChunkyManager.getChunk(event.getClickedBlock().getLocation());

                ChunkyAccessLevel permType = ChunkyAccessLevel.NONE;

                boolean isCancelled = !ChunkyPermissionChain.hasPerm(chunkyChunk, chunkyPlayer, ChunkyPermissions.Flags.SWITCH, permType);

                ChunkyPlayerSwitchEvent chunkyEvent = new ChunkyPlayerSwitchEvent(chunkyPlayer,chunkyChunk,event.getClickedBlock(), permType);
                chunkyEvent.setCancelled(isCancelled);
                Chunky.getModuleManager().callEvent(chunkyEvent);
                
                event.setCancelled(chunkyEvent.isCancelled());
            }
        }

    }
}
