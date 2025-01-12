package eu.fogas.reflection.exception.field;

import eu.fogas.reflection.exception.ReflectionUtilException;

import java.io.Serial;

/**
 * Exception used when a field is not exists with the provided name.
 */
public class FieldNotFoundException extends ReflectionUtilException {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an exception for the non-writable field.
	 *
	 * @param type      The class which does not have the field with the specified name.
	 * @param fieldName Name of the field which was not found.
	 */
	public FieldNotFoundException(final Class<?> type, final String fieldName) {
		super(fieldName + " was not found in " + type.getCanonicalName());
	}
}
