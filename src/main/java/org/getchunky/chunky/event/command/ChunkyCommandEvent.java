package org.getchunky.chunky.event.command;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.getchunky.chunky.event.ChunkyEvent;
import org.getchunky.chunky.module.ChunkyCommand;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyCommandEvent extends ChunkyEvent implements Cancellable {

    private boolean cancel;
    private CommandSender sender;
    private ChunkyCommand command;
    private String label;
    private String[] args;

    public ChunkyCommandEvent(Type type, CommandSender sender, ChunkyCommand command, String label, String[] args) {
        super(type);

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

    public CommandSender getSender() {
        return sender;
    }

    public ChunkyCommand getCommand() {
        return command;
    }

    public String getLabel() {
        return label;
    }

    public String[] getArgs() {
        return args;
    }
}
