package com.dumptruckman.chunky.object;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.event.object.ChunkyObjectNameEvent;

import java.util.Observable;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyObject extends Observable implements ChunkyEntity {

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
        Chunky.getModuleManager().callEvent(event);
        if (event.isCancelled()) return;
        this.name = event.getNewName();
        setChanged();
        notifyObservers(ChunkyObservableData.NAME_CHANGE);
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public boolean equals(Object obj) {
        return obj instanceof ChunkyObject && ((ChunkyObject)obj).getName().equals(this.getName());
    }
}
