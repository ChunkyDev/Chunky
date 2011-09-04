package com.dumptruckman.chunky.event.object;

import com.dumptruckman.chunky.event.ChunkyListener;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyObjectListener implements ChunkyListener {

    public ChunkyObjectListener() {}

    public void onObjectNameChange(ChunkyObjectNameEvent event) {}

    //public void onObjectAddOwner(ChunkyObjectOwnershipEvent event) {}

    //public void onObjectRemoveOwner(ChunkyObjectOwnershipEvent event) {}

    public void onObjectSetOwner(ChunkyObjectOwnershipEvent event) {}
}