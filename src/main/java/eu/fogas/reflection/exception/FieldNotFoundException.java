package eu.fogas.reflection.exception;

import java.io.Serial;

public class FieldNotFoundException extends ReflectionUtilException {
    @Serial
    private static final long serialVersionUID = 1L;

    public FieldNotFoundException(final Class<?> type, final String fieldName) {
        super(fieldName + " was not found in " + type.getCanonicalName());
    }
}
