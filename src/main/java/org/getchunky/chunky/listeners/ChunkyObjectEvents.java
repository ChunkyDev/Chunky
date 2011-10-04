package org.getchunky.chunky.listeners;

import org.getchunky.chunky.persistance.DatabaseManager;
import org.getchunky.chunky.event.object.ChunkyObjectListener;
import org.getchunky.chunky.event.object.ChunkyObjectNameEvent;
import org.getchunky.chunky.object.ChunkyChunk;

public class ChunkyObjectEvents extends ChunkyObjectListener{
    @Override
    public void onObjectNameChange(ChunkyObjectNameEvent event) {
        if(!(event.getObject() instanceof ChunkyChunk) || event.isCancelled()) return;
        event.getObject().save();
    }


}
