package org.getchunky.chunkie.exceptions;

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
