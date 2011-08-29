package com.dumptruckman.chunky.plugin.java;

import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.event.ChunkyListener;
import com.dumptruckman.chunky.event.CustomChunkyEventListener;
import com.dumptruckman.chunky.plugin.ChunkyEventExecutor;
import com.dumptruckman.chunky.plugin.ChunkyPluginLoader;

/**
 * @author dumptruckman
 */
public class JavaChunkyPluginLoader implements ChunkyPluginLoader {

    public ChunkyEventExecutor createExecutor(ChunkyEvent.Type type, ChunkyListener listener) {

        switch (type) {

            // Custom Events
            case CUSTOM_EVENT:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((CustomChunkyEventListener) listener).onCustomEvent(event);
                    }
                };
        }

        throw new IllegalArgumentException("Event " + type + " is not supported");
    }
}
