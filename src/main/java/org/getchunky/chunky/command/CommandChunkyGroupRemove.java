package org.getchunky.chunky.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyGroup;
import org.getchunky.chunky.object.ChunkyPlayer;

/**
 * @author dumptruckman
 */
public class CommandChunkyGroupRemove implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }
        Player player = (Player) sender;
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(player);

        if (args.length != 3) {
            Language.CMD_CHUNKY_GROUP_RM_HELP.bad(chunkyPlayer);
            return;
        }

        ChunkyGroup group = chunkyPlayer.getGroups().get(args[0]);
        if (group == null) {
            Language.NO_SUCH_GROUP.bad(chunkyPlayer, args[0]);
            return;
        }

        ChunkyPlayer targetPlayer = ChunkyManager.getChunkyPlayer(args[1]);
        if (targetPlayer == null) {
            Language.NO_SUCH_PLAYER.bad(chunkyPlayer, args[1]);
            return;
        }

        targetPlayer.removeGroup(group);
        Language.GROUP_REMOVE.good(chunkyPlayer, targetPlayer.getName(), group.getName());
    }
}
