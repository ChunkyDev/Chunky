package org.getchunky.chunky.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyWorld;

/**
 * @author dumptruckman
 */
public class CommandChunkyAdminDisableworld implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        String worldName = null;
        if (args.length < 1) {
            if (!(sender instanceof Player)) {
                Language.IN_GAME_ONLY.bad(sender);
                return;
            }
            worldName = ((Player) sender).getWorld().getName();
        } else {
            worldName = Language.combineStringArray(args, " ");
        }
        ChunkyWorld world = ChunkyManager.getChunkyWorld(worldName);
        if (world == null) {
            Language.NO_SUCH_WORLD.bad(sender, worldName);
            return;
        }
        world.setEnabled(false).save();
        Language.DISABLED_WORLD.good(sender);
    }
}
