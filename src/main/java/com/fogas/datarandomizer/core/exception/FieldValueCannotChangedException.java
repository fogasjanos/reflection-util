package com.fogas.datarandomizer.core.exception;

import java.lang.reflect.Field;

public class FieldValueCannotChangedException extends ReflectionUtilException {
    static final long serialVersionUID = 1L;

    public FieldValueCannotChangedException(Field field, Object fieldValue) {
        super("Cannot change " + field + " value to '" + fieldValue + "'.");
    }
}
