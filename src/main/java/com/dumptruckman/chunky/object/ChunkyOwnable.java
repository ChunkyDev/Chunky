package com.dumptruckman.chunky.object;

/**
 * @author dumptruckman
 */
interface ChunkyOwnable {

    public String getOwnableType();

    /**
     * Adds a ChunkyOwner to this object.  This method should NOT be called EXCEPT by ChunkyOwner.
     * @param owner
     */
    void _addOwner(ChunkyOwner owner); //TODO throws exception

    /**
     * Removes a ChunkyOwner from this object.  This method should NOT be called EXCEPT by ChunkyOwner.
     * @param owner
     */
    void _removeOwner(ChunkyOwner owner); //TODO throws exception
}
