package ua.kpi.restaurants.logic.common.exceptions;

/**
 * Thrown when some property of interest is invalid.
 * Extends {@link ProjectRuntimeException}.
 */
public class InvalidPropertyException extends ProjectRuntimeException {
  /**
   * Default constructor
   */
  public InvalidPropertyException() {
    super();
  }

  /**
   * Constructs from message {@link String}
   * @param message to construct from
   */
  public InvalidPropertyException(String message) {
    super(message);
  }

  /**
   * Constructs from {@link Throwable} instance
   * @param throwable to construct from
   */
  public InvalidPropertyException(Throwable throwable) {
    super(throwable);
  }

  /**
   * Constructs from message {@link String} and {@link Throwable} instance
   * @param message to construct from
   * @param throwable to construct from
   */
  public InvalidPropertyException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
