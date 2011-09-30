package org.getchunky.chunky.exceptions;

public class ChunkyException extends Exception {

    private String error;

    public ChunkyException(String error) {
        super(error);
        setError(error);
    }

    public ChunkyException() {
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
