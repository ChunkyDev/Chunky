package com.dumptruckman.chunky.command;

import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.module.ChunkyCommand;
import com.dumptruckman.chunky.module.ChunkyCommandExecutor;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.permission.bukkit.Permissions;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author dumptruckman, SwearWord
 */
public class CommandChunkyUnclaim implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }
        Player player = (Player)sender;
        if(args.length>0 && args[0].equalsIgnoreCase("*")) {
            unclaimAll(ChunkyManager.getChunkyPlayer(player.getName()));
            return;
        }
        if (Permissions.CHUNKY_UNCLAIM.hasPerm(player)) {
            ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(player);
            HashSet<ChunkyObject> ownables = chunkyPlayer.getOwnables().get(ChunkyChunk.class.getName());
            if (ownables == null || ownables.isEmpty()) {
                Language.CHUNK_NONE_OWNED.bad(player);
                return;
            }
            ChunkyChunk chunkyChunk;
            Location location = player.getLocation();
            chunkyChunk = ChunkyManager.getChunk(location);
            if (!chunkyChunk.isOwned() || !chunkyChunk.getOwner().equals(chunkyPlayer)) {
                Language.CHUNK_NOT_OWNED.bad(player, chunkyChunk.getOwner().getName());
                return;
            }
            chunkyChunk.setOwner(chunkyPlayer.getOwner(), true);
            chunkyChunk.setName(Language.UNREGISTERED_CHUNK_NAME.getString());
            Logging.debug(chunkyPlayer.getName() + " claimed " + chunkyChunk.getCoord().getX() + ":" + chunkyChunk.getCoord().getZ());
            Language.CHUNK_UNCLAIMED.good(player, chunkyChunk.getCoord().getX(), chunkyChunk.getCoord().getZ());
        } else {
            Language.NO_COMMAND_PERMISSION.bad(player);
        }


    }

    private void unclaimAll(ChunkyPlayer player) {
        for(ChunkyObject obj : (HashSet<ChunkyObject>)player.getOwnables().get(ChunkyChunk.class.getName()).clone()) {
            ChunkyChunk chunk = (ChunkyChunk)obj;
            chunk.setOwner(player.getOwner(),true);
            chunk.setName(Language.UNREGISTERED_CHUNK_NAME.getString());

        }
    }
}
