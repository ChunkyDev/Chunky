package org.getchunky.chunky.exceptions;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyObjectNotInitializedException extends ChunkyException {

    public ChunkyObjectNotInitializedException() {
        super();
        setError("Class not initialized properly.  Missing id?");
    }

    public ChunkyObjectNotInitializedException(String error) {
        super(error);
        setError(error);
    }
}
