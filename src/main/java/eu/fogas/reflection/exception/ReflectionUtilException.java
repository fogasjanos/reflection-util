package eu.fogas.reflection.exception;

import java.io.Serial;

public class ReflectionUtilException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ReflectionUtilException(String message) {
        super(message);
    }
}
