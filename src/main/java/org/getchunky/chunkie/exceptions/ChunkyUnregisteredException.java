package org.getchunky.chunkie.exceptions;

public class ChunkyUnregisteredException extends ChunkyException {

    public ChunkyUnregisteredException() {
        super();
        setError("Not registered.");
    }

    public ChunkyUnregisteredException(String error) {
        super(error);
        setError(error);
    }
}
