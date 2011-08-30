package com.dumptruckman.chunky.event.object;

import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.object.ChunkyObject;

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
     * @return
     */
    public final ChunkyObject getObject() {
        return object;
    }
}
