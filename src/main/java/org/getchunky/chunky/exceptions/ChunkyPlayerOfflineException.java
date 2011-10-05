package org.getchunky.chunky.exceptions;

public class ChunkyPlayerOfflineException extends ChunkyException {

    public ChunkyPlayerOfflineException() {
        super();
        setError("Player offline.");
    }

    public ChunkyPlayerOfflineException(String error) {
        super(error);
        setError(error);
    }
}
