package com.dumptruckman.chunky.exceptions;

public class ChunkyUnregisteredException extends ChunkyException {

    public ChunkyUnregisteredException() {
        super();
        error = "Not registered.";
    }

    public ChunkyUnregisteredException(String error) {
        super(error);
        this.error = error;
    }
}
