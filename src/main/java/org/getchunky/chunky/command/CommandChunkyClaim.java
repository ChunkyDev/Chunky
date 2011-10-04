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
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(player);
        chunkyPlayer.claimCurrentChunk();
    }
}
