package com.dumptruckman.chunky.listeners;

import com.dumptruckman.chunky.event.command.ChunkyCommandEvent;
import com.dumptruckman.chunky.event.command.ChunkyCommandListener;

/**
 * @author dumptruckman
 */
public class ChunkyCommandEvents extends ChunkyCommandListener {

    public void onCommandHelp(ChunkyCommandEvent event) {
        if (event.isCancelled()) return;
    }

    public void onCommandList(ChunkyCommandEvent event) {
        if (event.isCancelled()) return;
    }
}
