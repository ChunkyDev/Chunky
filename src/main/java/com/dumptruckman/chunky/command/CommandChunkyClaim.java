package com.dumptruckman.chunky.command;

import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.config.Config;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.locale.LanguagePath;
import com.dumptruckman.chunky.module.ChunkyCommand;
import com.dumptruckman.chunky.module.ChunkyCommandExecutor;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.permission.Permissions;
import com.dumptruckman.chunky.util.Logging;
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
            Language.sendMessage(sender, LanguagePath.IN_GAME_ONLY);
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

            ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(player.getName());
            if (Permissions.PLAYER_NO_CHUNK_LIMIT.hasPerm(player) || chunkyPlayer.getOwnables().get(ChunkyChunk.class.getName().hashCode()).size() < chunkLimit) {
                ChunkyChunk chunkyChunk;
                Location location = player.getLocation();
                chunkyChunk = ChunkyManager.getChunk(location);
                chunkyChunk.setOwner(chunkyPlayer, true);
                chunkyChunk.setName("~" + chunkyPlayer.getName());
                Logging.debug(chunkyPlayer.getName() + " claimed " + chunkyChunk.getCoord().getX() + ":" + chunkyChunk.getCoord().getZ());
            } else {
                // TODO chunk limit reached
            }
        } else {
            Language.sendMessage(player, LanguagePath.NO_COMMAND_PERMISSION);
        }
    }
}
