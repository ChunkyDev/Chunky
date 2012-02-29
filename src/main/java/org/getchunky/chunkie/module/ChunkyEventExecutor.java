package org.getchunky.chunkie.module;

import org.getchunky.chunkie.event.ChunkyEvent;
import org.getchunky.chunkie.event.ChunkyListener;

/**
 * @author dumptruckman, SwearWord
 */
public interface ChunkyEventExecutor {
    public void execute(ChunkyListener listener, ChunkyEvent event);
}
