package org.getchunky.chunky.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyPlayer;

import java.util.Arrays;
import java.util.List;

/**
 * @author dumptruckman
 */
public class CommandChunkyPlayerSetMode implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }
        if (args.length == 0) {
            Language.CMD_CHUNKY_PLAYER_SET_MODE_HELP.bad(sender);
            return;
        }

        ChunkyPlayer cPlayer = ChunkyManager.getChunkyPlayer((Player) sender);

        List<String> modes = Arrays.asList(args);
        for (String mode : modes) {
            mode = mode.toLowerCase();
        }

        boolean cleared = false;
        if (modes.contains("claim")) {
            ChunkyPlayer.getClaimModePlayers().add(cPlayer);
        }
        if (modes.contains("clear")) {
            cleared = true;
            ChunkyPlayer.getClaimModePlayers().remove(cPlayer);
        }

        if (!cleared)
            Language.PLAYER_MODE_SET.good(cPlayer);
        else
            Language.PLAYER_MODE_CLEAR.good(cPlayer);
    }
}
