package org.getchunky.chunky.event.object;

import org.getchunky.chunky.event.ChunkyEvent;
import org.getchunky.chunky.object.ChunkyObject;

/**
 * @author dumptruckman
 */
public class ChunkyObjectEvent extends ChunkyEvent {

    protected ChunkyObject object;

    public ChunkyObjectEvent(final ChunkyEvent.Type type, final ChunkyObject object) {
        super(type);
        this.object = object;
    }

    /**
     * Returns the object involved in this event.
     *
     * @return
     */
    public final ChunkyObject getObject() {
        return object;
    }
}
