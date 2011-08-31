package com.dumptruckman.chunky.command;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.config.Config;
import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.event.command.ChunkyCommandEvent;
import com.dumptruckman.chunky.exceptions.ChunkyUnregisteredException;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.locale.LanguagePath;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyCoordinates;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.permission.Permissions;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyCommandExecutor implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ChunkyCommandEvent event = new ChunkyCommandEvent(ChunkyEvent.Type.COMMAND_CHUNKY, sender, command, label, args);
        Chunky.getModuleManager().callEvent(event);
        if (event.isCancelled()) return true;

        if (args.length == 0) {
            simpleCommand(sender);
        } else {
            parseCommand(sender, command, label, args);
        }

        return true;
    }

    public void simpleCommand(CommandSender sender) {

    }

    public void parseCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0].equals("claim")) {
            ChunkyCommandEvent event = new ChunkyCommandEvent(ChunkyEvent.Type.COMMAND_CHUNKY_CLAIM, sender, command, label, args);
            Chunky.getModuleManager().callEvent(event);
            if (event.isCancelled()) return;

            if (sender instanceof Player) {
                claimChunk((Player) sender);
            } else {
                Language.sendMessage(sender, LanguagePath.IN_GAME_ONLY);
            }
        } else if (args[0].equals("unclaim")) {
            
        }
    }

    public void claimChunk(Player player) {
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
            if (Permissions.PLAYER_NO_CHUNK_LIMIT.hasPerm(player) || chunkyPlayer.getOwnables(ChunkyChunk.class.getName().hashCode()).size() < chunkLimit) {
                ChunkyChunk chunkyChunk;
                Location location = player.getLocation();
                try{
                    chunkyChunk = ChunkyManager.getChunk(location);
                } catch (ChunkyUnregisteredException e) {
                    chunkyChunk = new ChunkyChunk(new ChunkyCoordinates(location));
                }
                chunkyChunk.addOwner(chunkyPlayer);
                Logging.debug(chunkyPlayer.getName() + " claimed " + chunkyChunk.getCoord().getX() + ":" + chunkyChunk.getCoord().getZ());
            } else {
                // TODO chunk limit reached
            }
        } else {
            Language.sendMessage(player, LanguagePath.NO_COMMAND_PERMISSION);
        }
    }
}
