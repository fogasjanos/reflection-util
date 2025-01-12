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

    /**
     * Constructs an exception with the specified detail message and with the cause.
     *
     * @param message – the detail message.
     * @param cause - the cause.
     */
    public ReflectionUtilException(String message, Throwable cause) {
        super(message, cause);
    }
}
