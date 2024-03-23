package eu.fogas.reflection.exception;

import java.io.Serial;
import java.lang.reflect.Field;

/**
 * Exception used when a field value is not readable.
 */
public class FieldValueCannotReadException extends ReflectionUtilException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an exception for the unreadable field.
     *
     * @param field Field object which is not readable.
     */
    public FieldValueCannotReadException(Field field) {
        super("Cannot read " + field + " value.");
    }
}
