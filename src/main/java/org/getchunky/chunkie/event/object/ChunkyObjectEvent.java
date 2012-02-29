package org.getchunky.chunkie.event.object;

import org.getchunky.chunkie.event.ChunkyEvent;
import org.getchunky.chunkie.object.IChunkyObject;

/**
 * @author dumptruckman
 */
public class ChunkyObjectEvent extends ChunkyEvent {

    protected IChunkyObject object;

    public ChunkyObjectEvent(final ChunkyEvent.Type type, final IChunkyObject object) {
        super(type);
        this.object = object;
    }

    /**
     * Returns the object involved in this event.
     *
     * @return
     */
    public final IChunkyObject getObject() {
        return object;
    }
}
