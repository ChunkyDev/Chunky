package org.getchunky.chunkie.event.command;

import org.getchunky.chunkie.event.ChunkyListener;

/**
 * @author dumptruckman
 */
public class ChunkyCommandListener implements ChunkyListener {

    public ChunkyCommandListener() {
    }

    /**
     * Called when a command registered with chunky is processed
     *
     * @param event Relevant event details
     */
    public void onCommandProcess(ChunkyCommandEvent event) {
    }

    public void onCommandHelp(ChunkyCommandEvent event) {
    }

    public void onCommandList(ChunkyCommandEvent event) {
    }
}
