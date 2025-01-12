package eu.fogas.reflection.exception.operation;

import eu.fogas.reflection.exception.ReflectionUtilException;

/**
 * This exception is thrown in the case of construction problems.
 */
public class InitializationException extends ReflectionUtilException {

	/**
	 * Constructs an exception with the specified detail message.
	 *
	 * @param message – the detail message.
	 */
	public InitializationException(String message) {
		super(message);
	}

	/**
	 * Constructs an exception with the specified detail message and with the cause.
	 *
	 * @param message – the detail message.
	 * @param cause   - the cause.
	 */
	public InitializationException(String message, Throwable cause) {
		super(message, cause);
	}
}
