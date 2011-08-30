package com.dumptruckman.chunky.listeners;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.event.object.ChunkyPlayerChunkChangeEvent;
import com.dumptruckman.chunky.exceptions.ChunkyPlayerOfflineException;
import com.dumptruckman.chunky.exceptions.ChunkyUnregisteredException;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.locale.LanguagePath;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import sun.plugin2.message.Message;

/**
 * @author dumptruckman, SwearWord
 */
public class PlayerEvents extends PlayerListener{

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        if(event.isCancelled()) return;
        ChunkyChunk toChunk = null;
        try {
            toChunk = ChunkyManager.getChunk(event.getTo());
        } catch (ChunkyUnregisteredException ignored) {
        }
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer().getName());
        ChunkyChunk fromChunk = chunkyPlayer.getLastChunk();

        if(!fromChunk.equals(toChunk)) return;
        onPlayerChunkChange(chunkyPlayer,toChunk,fromChunk);
    }

    public void onPlayerChunkChange(ChunkyPlayer chunkyPlayer, ChunkyChunk toChunk, ChunkyChunk fromChunk) {
        String message = "";
        if(toChunk==null && fromChunk != null) message += LanguagePath.UNREGISTERED_CHUNK_NAME;
        else if(toChunk != null) {
            if(fromChunk != null && !fromChunk.getName().equals(toChunk.getName())) message += toChunk.getName();
            else message += toChunk.getName();
        }

        ChunkyPlayerChunkChangeEvent event = new ChunkyPlayerChunkChangeEvent(chunkyPlayer,toChunk,fromChunk,message);
        Chunky.getModuleManager().callEvent(event);
        try {
            chunkyPlayer.getPlayer().sendMessage(event.getMessage());
        } catch (ChunkyPlayerOfflineException ignored) {
        }
        chunkyPlayer.setLastChunk(toChunk);
    }
}
