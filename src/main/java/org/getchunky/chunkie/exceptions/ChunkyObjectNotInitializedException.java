package org.getchunky.chunkie.exceptions;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyObjectNotInitializedException extends ChunkyRuntimeException {

    public ChunkyObjectNotInitializedException() {
        super();
        setError("Class not initialized properly.  Missing id?");
    }

    public ChunkyObjectNotInitializedException(String error) {
        super(error);
        setError(error);
    }
}
