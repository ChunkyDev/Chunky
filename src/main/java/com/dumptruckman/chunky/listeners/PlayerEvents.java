package com.dumptruckman.chunky.listeners;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.event.player.ChunkyPlayerChunkChangeEvent;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

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
        if(!message.equals("")) Language.sendMessage(chunkyPlayer,event.getMessage());
        chunkyPlayer.setLastChunk(toChunk);
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
}
