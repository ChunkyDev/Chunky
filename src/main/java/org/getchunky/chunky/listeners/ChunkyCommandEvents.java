package org.getchunky.chunky.listeners;

import org.getchunky.chunky.event.command.ChunkyCommandEvent;
import org.getchunky.chunky.event.command.ChunkyCommandListener;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;

import java.util.Map;

/**
 * @author dumptruckman
 */
public class ChunkyCommandEvents extends ChunkyCommandListener {

    public void onCommandHelp(ChunkyCommandEvent event) {
        if (event.isCancelled()) return;
        Language.CMD_HELP.help(event.getSender(), event.getCommand().getChatName(), event.getCommand().getAliasesAsString());
        for (String helpLine : event.getCommand().getHelpLines()) {
            event.getSender().sendMessage(helpLine);
        }
    }

    public void onCommandList(ChunkyCommandEvent event) {
        if (event.isCancelled()) return;
        Language.CMD_LIST.help(event.getSender(), event.getCommand().getChatName(), event.getCommand().getAliasesAsString());
        for (Map.Entry<String, ChunkyCommand> childCommand : event.getCommand().getChildren().entrySet()) {
            if (childCommand.getValue().getDescription() == null) continue;
            Language.sendMessage(event.getSender(), "&a" + childCommand.getValue().getName() + "&f - " + childCommand.getValue().getDescription());
        }
    }
}
