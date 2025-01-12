package eu.fogas.reflection;

import eu.fogas.reflection.exception.field.FieldNotFoundException;
import eu.fogas.reflection.exception.field.FieldValueCannotChangedException;
import eu.fogas.reflection.exception.field.FieldValueCannotReadException;
import eu.fogas.reflection.exception.operation.InitializationException;
import lombok.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.BiFunction;
import java.util.random.RandomGenerator;

/**
 * Helper class to make java's reflection easy and fun to use.
 */
public class ReflectionUtil {

    /**
     * Sets the field represented by fieldName on the specified object argument to the specified new value.
     *
     * @param obj        the object whose field should be modified
     * @param fieldName  name fo the field
     * @param fieldValue the new value of the field
     * @throws FieldNotFoundException           if the field with the specified name is not found.
     * @throws FieldValueCannotChangedException when the value cannot be changed.
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
     * Return the value of the field of an object
     *
     * @param obj       the object with the field
     * @param fieldName name of the field
     * @param <V>       the type of the return value
     * @return the value of the field with the given name
     */
    public static <V> V getFieldValue(@NonNull final Object obj, @NonNull final String fieldName) {
        Field field = getDeclaredField(obj.getClass(), fieldName);
        try {
            field.trySetAccessible();
            return (V) field.get(obj);
        } catch (IllegalAccessException e) {
            throw new FieldValueCannotReadException(field);
        }
    }

    /**
     * Return the Field object with the declared name.
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

    /**
     * Return all the fields from the class and from all the super classes.
     *
     * @param type Class object
     * @return all the fields from the class and from all the super classes.
     */
    public static List<Field> getAllFields(@NonNull final Class<?> type) {
        List<Field> fields = new ArrayList<>();
        var t = type;
        do {
            fields.addAll(Arrays.asList(t.getDeclaredFields()));
            t = t.getSuperclass();
        } while (t != null);
        return fields;
    }

    /**
     * Return declared constructors array.
     *
     * @param type Class object
     * @return Declared constructors array
     */
    public static Constructor<?>[] getDeclaredConstructors(Class<?> type) {
        return type.getDeclaredConstructors();
    }

    /**
     * Find the constructor with the most parameters.
     *
     * @param type Class object
     * @return the first constructor with the most parameters or the default constructor.
     */
    public static Constructor<?> getConstructorWithMostParameters(Class<?> type) {
        return getConstructor(type, (a, b) -> a.getParameterCount() > b.getParameterCount());
    }

    /**
     * Find the default constructor.
     *
     * @param type Class object
     * @return the default constructor.
     */
    public static Constructor<?> getDefaultConstructor(Class<?> type) {
        return getConstructor(type, (a, b) -> a.getParameterCount() < b.getParameterCount());
    }

    /**
     * Creates a new instance of the given type with the default constructor.
     *
     * @param type Class object
     * @param <T>  the type of the return value
     * @return a new instance of the given type
     */
    public static <T> T newInstance(Class<T> type) {
        try {
            return (T) getDefaultConstructor(type).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            var msg = String.format("Could not create instance of %s because %s", type.getCanonicalName(), e.getMessage());
            throw new InitializationException(msg, e);
        }
    }

    public static <T> T newInstance(Class<T> type, Object... initargs) {
        try {
            Class<?>[] paramTypes = Arrays.stream(initargs)
                    .map(Object::getClass)
                    .toArray(size -> new Class<?>[initargs.length]);

            return (T) Arrays.stream(type.getDeclaredConstructors())
                    .filter(con -> con.getParameterCount() == 1
                            && Arrays.equals(paramTypes, con.getParameterTypes()))
                    .findFirst()
                    .orElseThrow(() -> new InitializationException(
                            String.format("Could not create instance of %s because not suitable constructor was found", type.getCanonicalName())))
                    .newInstance(initargs);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            var msg = String.format("Could not create instance of %s because %s", type.getCanonicalName(), e.getMessage());
            throw new InitializationException(msg, e);
        }
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

    /**
     * Check the class is declared as final.
     *
     * @param type class to check
     * @return true if the declared class is final
     */
    public static boolean isFinal(@NonNull final Class<?> type) {
        return Modifier.isFinal(type.getModifiers());
    }

    /**
     * Check the field is declared as static.
     *
     * @param field field to check
     * @return true if the declared field is static
     */
    public static boolean isStatic(@NonNull final Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    /**
     * Check the class is declared as static.
     *
     * @param type class to check
     * @return true if the declared class is static
     */
    public static boolean isStatic(@NonNull final Class<?> type) {
        return Modifier.isStatic(type.getModifiers());
    }

    /**
     * Check the class is declared as abstract.
     *
     * @param type class to check
     * @param <T>  the type of the class
     * @return true if the declared type is abstract
     */
    public static <T> boolean isAbstract(@NonNull final Class<T> type) {
        return Modifier.isAbstract(type.getModifiers());
    }

    /**
     * Check the class is declared as interface.
     *
     * @param type class to check
     * @param <T>  the type of the class
     * @return true if the declared type is interface
     */
    public static <T> boolean isInterface(@NonNull final Class<T> type) {
        return Modifier.isInterface(type.getModifiers());
    }

    /**
     * Check the class is declared as enum.
     *
     * @param type class to check
     * @param <T>  the type of the class
     * @return true if the declared type is enum
     */
    public static <T> boolean isEnum(@NonNull final Class<T> type) {
        return isAssignableFrom(Enum.class, type);
    }

    /**
     * Check the class is declared as enum.
     *
     * @param type class to check
     * @param <T>  the type of the class
     * @return true if the declared type is enum
     */
    public static <T> boolean isRecord(@NonNull final Class<T> type) {
        return isAssignableFrom(Record.class, type);
    }

    /**
     * Check the class is declared as Set.
     *
     * @param type class to check
     * @param <T>  the type of the class
     * @return true if the declared type is Set
     */
    public static <T> boolean isSet(@NonNull final Class<T> type) {
        return isAssignableFrom(Set.class, type);
    }

    /**
     * Check the field is a Set.
     *
     * @param field field to check
     * @return true if the declared field is a Set
     */
    public static boolean isSet(@NonNull final Field field) {
        return isSet(field.getType());
    }

    /**
     * Check the class is declared as List.
     *
     * @param type class to check
     * @param <T>  the type of the class
     * @return true if the declared type is List
     */
    public static <T> boolean isList(@NonNull final Class<T> type) {
        return isAssignableFrom(List.class, type);
    }

    /**
     * Check the field is a List.
     *
     * @param field field to check
     * @return true if the declared field is a List
     */
    public static boolean isList(@NonNull final Field field) {
        return isList(field.getType());
    }

    /**
     * Check the class is declared as Map.
     *
     * @param type class to check
     * @param <T>  the type of the class
     * @return true if the declared type is Map
     */
    public static <T> boolean isMap(@NonNull final Class<T> type) {
        return isAssignableFrom(Map.class, type);
    }

    /**
     * Check the field is a Map.
     *
     * @param field field to check
     * @return true if the declared field is a Map
     */
    public static boolean isMap(@NonNull final Field field) {
        return isMap(field.getType());
    }

    /**
     * Check the field is an array.
     *
     * @param field field to check
     * @return true if the declared field is an array
     */
    public static boolean isArray(@NonNull final Field field) {
        return field.getType().isArray();
    }

    private static <T> boolean isAssignableFrom(@NonNull final Class<?> cls, @NonNull final Class<T> type) {
        return cls.isAssignableFrom(type);
    }

    private static Constructor<?> getConstructor(Class<?> type, BiFunction<Constructor<?>, Constructor<?>, Boolean> condition) {
        Constructor<?>[] constructors = getDeclaredConstructors(type);
        Constructor<?> least = null;
        for (Constructor<?> constructor : constructors) {
            if (least == null || condition.apply(constructor, least)) {
                least = constructor;
            }
        }
        return least;
    }
}