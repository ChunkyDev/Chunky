package org.getchunky.chunkie.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunkie.ChunkyManager;
import org.getchunky.chunkie.locale.Language;
import org.getchunky.chunkie.module.ChunkyCommand;
import org.getchunky.chunkie.module.ChunkyCommandExecutor;
import org.getchunky.chunkie.object.IChunkyPlayer;

/**
 * @author dumptruckman
 */
public class CommandChunkyClaim implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }
        Player player = (Player) sender;
        IChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(player);
        chunkyPlayer.claimCurrentChunk();
    }
}
