package org.getchunky.chunky.listeners;

import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.config.Config;
import org.getchunky.chunky.event.object.player.ChunkyPlayerChunkChangeEvent;
import org.getchunky.chunky.event.object.player.ChunkyPlayerItemUseEvent;
import org.getchunky.chunky.event.object.player.ChunkyPlayerSwitchEvent;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.permission.ChunkyAccessLevel;
import org.getchunky.chunky.permission.ChunkyPermissionChain;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.ChunkyPermissions;
import org.getchunky.chunky.permission.bukkit.Permissions;
import org.getchunky.chunky.persistance.DatabaseManager;
import org.getchunky.chunky.util.MinecraftTools;
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
        if (!toChunk.isOwned()) {
            if (fromChunk.isOwned())
                message = " " + Language.UNREGISTERED_CHUNK_NAME.getString();
        } else
            if (fromChunk.isOwned()) {
                if (fromChunk.getOwner().equals(toChunk.getOwner())) {
                    if (!fromChunk.getName().equals(toChunk.getName()))
                        message += Config.getChunkDisplayName(toChunk);
                } else
                    message += Config.getChunkDisplayName(toChunk);
            } else
                message += Config.getChunkDisplayName(toChunk);

        ChunkyPlayerChunkChangeEvent event = new ChunkyPlayerChunkChangeEvent(chunkyPlayer,toChunk,fromChunk,message);
        Chunky.getModuleManager().callEvent(event);
        if(!event.getMessage().equals("")) Language.sendMessage(chunkyPlayer, event.getMessage());
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

                ChunkyAccessLevel permType = ChunkyPermissionChain.hasPerm(chunkyChunk, chunkyPlayer, ChunkyPermissions.Flags.ITEMUSE);
                boolean isCancelled = permType.causedDenial();

                ChunkyPlayerItemUseEvent chunkyEvent = new ChunkyPlayerItemUseEvent(chunkyPlayer,chunkyChunk,event.getItem(), permType);
                chunkyEvent.setCancelled(isCancelled);
                Chunky.getModuleManager().callEvent(chunkyEvent);

                event.setCancelled(chunkyEvent.isCancelled());
            }
            if(MinecraftTools.isSwitchable(event.getClickedBlock().getTypeId())) {
                ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
                ChunkyChunk chunkyChunk = ChunkyManager.getChunk(event.getClickedBlock().getLocation());

                ChunkyAccessLevel permType = ChunkyPermissionChain.hasPerm(chunkyChunk, chunkyPlayer, ChunkyPermissions.Flags.SWITCH);
                boolean isCancelled = permType.causedDenial();

                ChunkyPlayerSwitchEvent chunkyEvent = new ChunkyPlayerSwitchEvent(chunkyPlayer,chunkyChunk,event.getClickedBlock(), permType);
                chunkyEvent.setCancelled(isCancelled);
                Chunky.getModuleManager().callEvent(chunkyEvent);
                
                event.setCancelled(chunkyEvent.isCancelled());
            }
        }

    }
}
