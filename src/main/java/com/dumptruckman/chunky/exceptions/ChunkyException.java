package com.dumptruckman.chunky.exceptions;

public class ChunkyException extends Exception {

    private String error;

    public ChunkyException(String error) {
        super(error);
        this.error = error;
    }

    public ChunkyException() {
        super("Unknown");
        this.error = "Unknown";
    }

    public String getError() {
        return error;
    }
}
