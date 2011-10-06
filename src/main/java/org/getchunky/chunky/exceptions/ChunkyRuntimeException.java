package org.getchunky.chunky.exceptions;

/**
 * @author dumptruckman
 */
public class ChunkyRuntimeException extends RuntimeException {

    private String error;

    public ChunkyRuntimeException(String error) {
        super(error);
        setError(error);
    }

    public ChunkyRuntimeException() {
        super("Unknown");
        setError("Unknown");
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
