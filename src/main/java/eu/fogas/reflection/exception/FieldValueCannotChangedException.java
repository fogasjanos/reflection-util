package eu.fogas.reflection.exception;

import java.io.Serial;
import java.lang.reflect.Field;

public class FieldValueCannotChangedException extends ReflectionUtilException {
    @Serial
    private static final long serialVersionUID = 1L;

    public FieldValueCannotChangedException(Field field, Object fieldValue) {
        super("Cannot change " + field + " value to '" + fieldValue + "'.");
    }
}
