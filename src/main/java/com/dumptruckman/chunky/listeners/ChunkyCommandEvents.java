package com.dumptruckman.chunky.listeners;

import com.dumptruckman.chunky.event.command.ChunkyCommandEvent;
import com.dumptruckman.chunky.event.command.ChunkyCommandListener;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.locale.LanguagePath;
import com.dumptruckman.chunky.module.ChunkyCommand;

/**
 * @author dumptruckman
 */
public class ChunkyCommandEvents extends ChunkyCommandListener {

    public void onCommandHelp(ChunkyCommandEvent event) {
        if (event.isCancelled()) return;
        Language.sendMessage(event.getSender(), LanguagePath.CMD_HELP, event.getCommand().getChatName());
        for (String helpLine : event.getCommand().getHelpLines()) {
            event.getSender().sendMessage(helpLine);
        }
    }

    public void onCommandList(ChunkyCommandEvent event) {
        if (event.isCancelled()) return;
        Language.sendMessage(event.getSender(), LanguagePath.CMD_LIST, event.getCommand().getChatName());
        for (ChunkyCommand childCommand : event.getCommand().getChildren()) {
            if (childCommand.getDescription() == null) continue;
            event.getSender().sendMessage(childCommand.getName() + " - " + childCommand.getDescription());
        }
    }
}
