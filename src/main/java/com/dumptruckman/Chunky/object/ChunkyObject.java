package com.dumptruckman.chunky.object;

import java.io.Serializable;
import java.util.Observable;

/**
 * @author dumptruckman
 */
public class ChunkyObject extends Observable implements Serializable {

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
        this.name = name;
        setChanged();
        notifyObservers(ChunkyObservableData.NAME_CHANGE);
    }
}
