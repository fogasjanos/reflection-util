package eu.fogas.reflection.exception;

import java.io.Serial;

/**
 * Base exception for ReflectionUtil class
 */
public class ReflectionUtilException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an exception with the specified detail message.
     *
     * @param message – the detail message.
     */
    public ReflectionUtilException(String message) {
        super(message);
    }
}
