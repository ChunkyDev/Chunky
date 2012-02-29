package org.getchunky.chunkie.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunkie.ChunkyManager;
import org.getchunky.chunkie.locale.Language;
import org.getchunky.chunkie.module.ChunkyCommand;
import org.getchunky.chunkie.module.ChunkyCommandExecutor;
import org.getchunky.chunkie.object.IChunkyGroup;
import org.getchunky.chunkie.object.IChunkyObject;
import org.getchunky.chunkie.object.IChunkyPlayer;

import java.util.HashSet;

/**
 * @author dumptruckman
 */
public class CommandChunkyGroupAdd implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }
        Player player = (Player) sender;
        IChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(player);

        if (args.length != 2) {
            Language.CMD_CHUNKY_GROUP_ADD_HELP.bad(chunkyPlayer);
            return;
        }

        IChunkyPlayer targetPlayer = ChunkyManager.getChunkyPlayer(args[0]);
        if (targetPlayer == null) {
            Language.NO_SUCH_PLAYER.bad(chunkyPlayer, args[0]);
            return;
        }

        IChunkyGroup group = null;
        HashSet<IChunkyObject> objects = chunkyPlayer.getOwnables().get(IChunkyGroup.class.getName());
        for (IChunkyObject object : objects) {
            if (object.getName().equalsIgnoreCase(args[1])) {
                group = (IChunkyGroup) object;
                break;
            }
        }
        if (group == null) {
            Language.NO_SUCH_GROUP.bad(chunkyPlayer, args[1]);
            return;
        }

        targetPlayer.addToGroup(group);
        Language.GROUP_ADD.good(chunkyPlayer, targetPlayer.getName(), group.getName());
    }
}
