package org.getchunky.chunkie.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunkie.ChunkyManager;
import org.getchunky.chunkie.locale.Language;
import org.getchunky.chunkie.module.ChunkyCommand;
import org.getchunky.chunkie.module.ChunkyCommandExecutor;
import org.getchunky.chunkie.object.IChunkyChunk;
import org.getchunky.chunkie.object.IChunkyObject;
import org.getchunky.chunkie.object.IChunkyPlayer;
import org.getchunky.chunkie.permission.bukkit.Permissions;

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

    private void unclaimAll(IChunkyPlayer player) {
        for (IChunkyObject obj : player.getOwnables().get(IChunkyChunk.class.getName())) {
            player.unclaimChunk((IChunkyChunk) obj);
        }
    }
}
