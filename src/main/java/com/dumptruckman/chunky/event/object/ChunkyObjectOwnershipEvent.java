package com.dumptruckman.chunky.event.object;

import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.object.ChunkyObject;
import org.bukkit.event.Cancellable;

/**
 * @author dumptruckman
 */
public class ChunkyObjectOwnershipEvent extends ChunkyEvent implements Cancellable {

    private boolean cancel = false;
    private ChunkyObject owner;
    private ChunkyObject ownable;

    public ChunkyObjectOwnershipEvent(Type type, ChunkyObject owner, ChunkyObject ownable) {
        super(type);
        
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

    public ChunkyObject getOwnerObject() {
        return owner;
    }

    public ChunkyObject getOwnableObject() {
        return ownable;
    }
}
