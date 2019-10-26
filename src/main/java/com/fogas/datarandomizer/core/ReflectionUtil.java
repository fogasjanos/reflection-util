package com.fogas.datarandomizer.core;

import com.fogas.datarandomizer.core.exception.FieldNotFoundException;
import com.fogas.datarandomizer.core.exception.FieldValueCannotChangedException;
import lombok.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtil {

    /**
     * Sets the field represented by fieldName on the specified object argument to the specified new value.
     *
     * @param obj        the object whose field should be modified
     * @param fieldName  name fo the field
     * @param fieldValue the new value of the field
     * @throws FieldNotFoundException           if the field with the specified name is not found.
     * @throws FieldValueCannotChangedException when the value cannot changed.
     */
    public static void setFieldValue(@NonNull final Object obj, @NonNull final String fieldName, final Object fieldValue) {
        Field field = getDeclaredField(obj.getClass(), fieldName);
        try {
            field.trySetAccessible();
            field.set(obj, fieldValue);
        } catch (IllegalAccessException e) {
            throw new FieldValueCannotChangedException(field, fieldValue);
        }
    }

    /**
     * Return a Field object.
     *
     * @param type      Class object
     * @param fieldName the simple name of the field
     * @return a Field object
     * @throws FieldNotFoundException if the field with the specified name is not found.
     */
    public static Field getDeclaredField(@NonNull final Class<?> type, @NonNull final String fieldName) {
        Field field;
        try {
            field = type.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            field = null;
        }
        if (field == null) {
            Class<?> superType = type.getSuperclass();
            if (superType == null) {
                throw new FieldNotFoundException(type, fieldName);
            }
            return getDeclaredField(superType, fieldName);
        }
        return field;
    }

    public static Constructor<?>[] getDeclaredConstructors(Class<?> type) {
        return type.getDeclaredConstructors();
    }

    /**
     * Find the constructor with the most parameters.
     * @param type Class object
     * @param <T> type of the Class object
     * @return the first constructor with the most parameters or the default constructor.
     */
    public static <T> Constructor<T> getConstructorWithMostParameters(Class<T> type) {
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        Constructor<?> most = null;
        for (Constructor<?> constructor : constructors) {
            if (most == null || constructor.getParameterCount() > most.getParameterCount()) {
                most = constructor;
            }
        }
        return (Constructor<T>) most;
    }

    /**
     * Check the field is declared as final.
     *
     * @param field field to check
     * @return true if the declared field is final
     */
    public static boolean isFinal(@NonNull final Field field) {
        return Modifier.isFinal(field.getModifiers());
    }

    public static <T> boolean isAbstract(@NonNull final Class<T> type) {
        return Modifier.isAbstract(type.getModifiers());
    }

    public static <T> boolean isInterface(@NonNull final Class<T> type) {
        return Modifier.isInterface(type.getModifiers());
    }

    // Note: it should run before the first time we access the field
    private static void removeFinal(@NonNull final Field field) {
        Field modifiersField = null;
        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.trySetAccessible();
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException nsfe) {
            throw new FieldNotFoundException(Field.class, "modifiers");
        } catch (IllegalAccessException iae) {
            throw new FieldValueCannotChangedException(modifiersField, "remove FINAL modifier");
        }
    }
}