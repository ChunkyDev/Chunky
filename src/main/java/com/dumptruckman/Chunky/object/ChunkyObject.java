package com.dumptruckman.chunky.object;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.event.object.ChunkyObjectNameEvent;

import java.util.Observable;

/**
 * @author dumptruckman
 */
public class ChunkyObject extends Observable {

    private String name;

    public ChunkyObject() {
        this(null);
    }

    public ChunkyObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        ChunkyObjectNameEvent event = new ChunkyObjectNameEvent(this, name);
        Chunky.getManager().callEvent(event);
        if (event.isCancelled()) return;
        this.name = name;
        setChanged();
        notifyObservers(ChunkyObservableData.NAME_CHANGE);
    }
}
