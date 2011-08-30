package com.dumptruckman.chunky.event.command;

import com.dumptruckman.chunky.event.ChunkyEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyCommandEvent extends ChunkyEvent implements Cancellable {

    private boolean cancel;
    private CommandSender sender;
    private Command command;
    private String label;
    private String[] args;
    
    public ChunkyCommandEvent(Type type, CommandSender sender, Command command, String label, String[] args) {
        super (type);

        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

}
