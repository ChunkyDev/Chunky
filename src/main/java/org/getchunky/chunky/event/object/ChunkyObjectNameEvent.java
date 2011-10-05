package org.getchunky.chunky.event.object;

import org.bukkit.event.Cancellable;
import org.getchunky.chunky.event.ChunkyEvent;
import org.getchunky.chunky.object.ChunkyObject;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyObjectNameEvent extends ChunkyObjectEvent implements Cancellable {

    private boolean cancel = false;

    protected String oldName;
    protected String newName;

    public ChunkyObjectNameEvent(final ChunkyObject object, final String newName) {
        super(Type.OBJECT_NAME, object);
        this.object = object;
        this.oldName = object.getName();
        this.newName = newName;
    }

    public ChunkyObjectNameEvent(final ChunkyEvent.Type type, final ChunkyObject object, final String newName) {
        super(type, object);
        this.object = object;
        this.oldName = object.getName();
        this.newName = newName;
    }

    /**
     * Returns the old name of this object
     *
     * @return Name of object prior to this event
     */
    public final String getOldName() {
        return oldName;
    }

    /**
     * Returns the new name of this object
     *
     * @return Name of object after this event
     */
    public final String getNewName() {
        return newName;
    }

    /**
     * Sets the new name for the object
     *
     * @param name New name for object
     */
    public final void setName(String name) {
        this.newName = name;
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
