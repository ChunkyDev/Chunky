package org.getchunky.chunky.event.object;

import org.bukkit.event.Cancellable;
import org.getchunky.chunky.event.ChunkyEvent;
import org.getchunky.chunky.object.ChunkyObject;

/**
 * @author dumptruckman
 */
public class ChunkyObjectOwnershipEvent extends ChunkyEvent implements Cancellable {

    private boolean cancel = false;
    private ChunkyObject owner;
    private ChunkyObject object;
    private Boolean keepChildren;

    public ChunkyObjectOwnershipEvent(Type type, ChunkyObject object, ChunkyObject owner, boolean keepChildren) {
        super(type);
        this.owner = owner;
        this.object = object;
        this.keepChildren = keepChildren;
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

    public ChunkyObject getObject() {
        return object;
    }

    public ChunkyObject getOldOwner() {
        return object.getOwner();
    }

    public ChunkyObject getNewOwner() {
        return owner;
    }

    public Boolean isKeepingChildren() {
        return keepChildren;
    }

    public void setKeepChildren(boolean b) {
        keepChildren = b;
    }
}
