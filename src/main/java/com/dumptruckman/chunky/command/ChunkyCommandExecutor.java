package com.dumptruckman.chunky.command;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.event.command.ChunkyCommandEvent;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.locale.LanguagePath;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyCommandExecutor implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ChunkyCommandEvent event = new ChunkyCommandEvent(ChunkyEvent.Type.CHUNKY_COMMAND, sender, command, label, args);
        Chunky.getModuleManager().callEvent(event);
        if (event.isCancelled()) return true;

        if (args.length == 0) {
            simpleCommand(sender);
        } else {
            parseCommand(sender, args);
        }

        return true;
    }

    public void simpleCommand(CommandSender sender) {

    }

    public void parseCommand(CommandSender sender, String[] args) {
        if (args[0].equals("claim")) {
            if (sender instanceof Player) {
                Player player = (Player)sender;
                if (Chunky.hasPerm(player, "chunky.claim")) {
                    //TODO
                } else {
                    Language.sendMessage(player, LanguagePath.NO_COMMAND_PERMISSION);
                }
            } else {
                Language.sendMessage(sender, LanguagePath.IN_GAME_ONLY);
            }
        } //else if (args[0].equals()) {
            
        //}
    }
}
