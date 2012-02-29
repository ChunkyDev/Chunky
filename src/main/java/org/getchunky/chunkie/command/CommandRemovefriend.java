package org.getchunky.chunkie.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunkie.Chunky;
import org.getchunky.chunkie.locale.Language;
import org.getchunky.chunkie.module.ChunkyCommand;
import org.getchunky.chunkie.module.ChunkyCommandExecutor;

/**
 * @author dumptruckman
 */
public class CommandRemovefriend implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }

        if (args.length != 1) {
            Language.CMD_RMFRIEND_HELP.bad(sender);
            return;
        }

        String[] newArgs = new String[2];
        newArgs[0] = args[0];
        newArgs[1] = "friends";

        Chunky.getModuleManager().getCommandByName("chunky.group.remove").getExecutor().onCommand(sender, command, label, newArgs);
    }
}
