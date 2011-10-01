package org.getchunky.chunky.command;

import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.config.Config;
import org.getchunky.chunky.event.ChunkyEvent;
import org.getchunky.chunky.event.object.player.ChunkyPlayerChunkClaimEvent;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.ChunkyAccessLevel;
import org.getchunky.chunky.permission.bukkit.Permissions;
import org.getchunky.chunky.util.Logging;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author dumptruckman
 */
public class CommandChunkyClaim implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }
        Player player = (Player)sender;
        if (Permissions.CHUNKY_CLAIM.hasPerm(player)) {
            // Grab the chunk claim limit for the player
            int chunkLimit = Config.getPlayerChunkLimitDefault();
            for (Map.Entry<String,Integer> limit : Config.getCustomPlayerChunkLimits().entrySet()) {
                if (Permissions.hasPerm(player, Permissions.PLAYER_CHUNK_LIMIT.getNode() + "." + limit.getKey())) {
                    chunkLimit = limit.getValue();
                    break;
                }
            }

            ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(player);
            if (Permissions.PLAYER_NO_CHUNK_LIMIT.hasPerm(player) ||
                    !chunkyPlayer.getOwnables().containsKey(ChunkyChunk.class.getName()) ||
                    chunkyPlayer.getOwnables().get(ChunkyChunk.class.getName()).size() < chunkLimit) {
                ChunkyChunk chunkyChunk;
                Location location = player.getLocation();
                chunkyChunk = ChunkyManager.getChunk(location);
                if (chunkyChunk.isOwned()) {
                    Language.CHUNK_OWNED.bad(player, chunkyChunk.getOwner().getName());
                    return;
                }

                ChunkyPlayerChunkClaimEvent event = new ChunkyPlayerChunkClaimEvent(chunkyPlayer,chunkyChunk, ChunkyAccessLevel.UNOWNED);
                event.setCancelled(false);
                Chunky.getModuleManager().callEvent(event);

                if(event.isCancelled()) return;
                chunkyChunk.setOwner(chunkyPlayer, true,true);
                chunkyChunk.setName("");
                Logging.debug(chunkyPlayer.getName() + " claimed " + chunkyChunk.getCoord().getX() + ":" + chunkyChunk.getCoord().getZ());
                Language.CHUNK_CLAIMED.good(player, chunkyChunk.getCoord().getX(), chunkyChunk.getCoord().getZ());
            } else {Language.CHUNK_LIMIT_REACHED.bad(player, chunkLimit);}
        } else {
            Language.NO_COMMAND_PERMISSION.bad(player);
        }
    }
}
