package ua.kpi.restaurants.logic.common.exceptions;

/**
 * Exception used throughout the project.
 * Extends {@link RuntimeException}.
 */
public class ProjectRuntimeException extends RuntimeException {
  /**
   * Default constructor
   */
  public ProjectRuntimeException() {
    super();
  }

  /**
   * Constructs from message {@link String}
   * @param message to construct from
   */
  public ProjectRuntimeException(String message) {
    super(message);
  }

  /**
   * Constructs from {@link Throwable} instance
   * @param throwable to construct from
   */
  public ProjectRuntimeException(Throwable throwable) {
    super(throwable);
  }

  /**
   * Constructs from message {@link String} and {@link Throwable} instance
   * @param message to construct from
   * @param throwable to construct from
   */
  public ProjectRuntimeException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
