package org.getchunky.chunky.module;

import org.getchunky.chunky.event.ChunkyEvent;
import org.getchunky.chunky.event.ChunkyListener;

/**
 * @author dumptruckman, SwearWord
 */
public interface ChunkyEventExecutor {
    public void execute(ChunkyListener listener, ChunkyEvent event);
}
