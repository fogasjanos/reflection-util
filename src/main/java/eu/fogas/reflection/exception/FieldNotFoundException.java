package eu.fogas.reflection.exception;

public class FieldNotFoundException extends ReflectionUtilException {
    static final long serialVersionUID = 1L;

    public FieldNotFoundException(final Class<?> type, final String fieldName) {
        super(fieldName + " was not found in " + type.getCanonicalName());
    }
}
