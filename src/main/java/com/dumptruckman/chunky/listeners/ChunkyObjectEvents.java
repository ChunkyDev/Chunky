package com.dumptruckman.chunky.listeners;

import com.dumptruckman.chunky.event.object.ChunkyObjectListener;
import com.dumptruckman.chunky.event.object.ChunkyObjectNameEvent;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.persistance.DatabaseManager;

public class ChunkyObjectEvents extends ChunkyObjectListener{
    @Override
    public void onObjectNameChange(ChunkyObjectNameEvent event) {
        if(!(event.getObject() instanceof ChunkyChunk) || event.isCancelled()) return;
        DatabaseManager.updateChunk((ChunkyChunk)event.getObject());
    }
}
