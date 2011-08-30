package com.dumptruckman.chunky.plugin;

import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.event.ChunkyListener;

/**
 * @author dumptruckman, SwearWord
 */
public interface ChunkyEventExecutor {
    public void execute(ChunkyListener listener, ChunkyEvent event);
}
