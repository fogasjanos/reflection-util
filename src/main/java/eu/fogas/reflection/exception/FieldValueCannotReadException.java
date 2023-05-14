package eu.fogas.reflection.exception;

import java.lang.reflect.Field;

public class FieldValueCannotReadException extends ReflectionUtilException {
    static final long serialVersionUID = 1L;

    public FieldValueCannotReadException(Field field) {
        super("Cannot read " + field + " value.");
    }
}
