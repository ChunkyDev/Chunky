package org.getchunky.chunkie.event.object;

import org.bukkit.event.Cancellable;
import org.getchunky.chunkie.event.ChunkyEvent;
import org.getchunky.chunkie.object.IChunkyObject;

/**
 * @author dumptruckman
 */
public class ChunkyObjectOwnershipEvent extends ChunkyEvent implements Cancellable {

    private boolean cancel = false;
    private IChunkyObject owner;
    private IChunkyObject object;
    private Boolean keepChildren;

    public ChunkyObjectOwnershipEvent(Type type, IChunkyObject object, IChunkyObject owner, boolean keepChildren) {
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

    public IChunkyObject getObject() {
        return object;
    }

    public IChunkyObject getOldOwner() {
        return object.getOwner();
    }

    public IChunkyObject getNewOwner() {
        return owner;
    }

    public Boolean isKeepingChildren() {
        return keepChildren;
    }

    public void setKeepChildren(boolean b) {
        keepChildren = b;
    }
}
