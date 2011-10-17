package org.getchunky.chunky.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.bukkit.Permissions;

/**
 * @author dumptruckman, SwearWord
 */
public class CommandChunkyUnclaim implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }
        Player player = (Player) sender;
        if (args.length > 0 && args[0].equalsIgnoreCase("*")) {
            unclaimAll(ChunkyManager.getChunkyPlayer(player.getName()));
            return;
        }
        if (Permissions.CHUNKY_UNCLAIM.hasPerm(player)) {
            ChunkyManager.getChunkyPlayer(player).unclaimCurrentChunk();
        } else {
            Language.NO_COMMAND_PERMISSION.bad(player);
        }


    }

    private void unclaimAll(ChunkyPlayer player) {
        for (ChunkyObject obj : player.getOwnables().get(ChunkyChunk.class.getName())) {
            player.unclaimChunk((ChunkyChunk) obj);
        }
    }
}
