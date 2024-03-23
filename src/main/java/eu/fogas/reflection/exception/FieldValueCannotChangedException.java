package eu.fogas.reflection.exception;

import java.io.Serial;
import java.lang.reflect.Field;

/**
 * Exception used when a field value is not writable.
 */
public class FieldValueCannotChangedException extends ReflectionUtilException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an exception for the non-writable field.
     *
     * @param field Field object which is not writable.
     * @param fieldValue The desired new value of the Field
     */
    public FieldValueCannotChangedException(Field field, Object fieldValue) {
        super("Cannot change " + field + " value to '" + fieldValue + "'.");
    }
}
