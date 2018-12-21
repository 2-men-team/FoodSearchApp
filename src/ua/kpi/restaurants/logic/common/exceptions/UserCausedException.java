package ua.kpi.restaurants.logic.common.exceptions;

/**
 * Base class exception for all user-caused errors.
 * Extends {@link ProjectRuntimeException}.
 */
public class UserCausedException extends ProjectRuntimeException {
  /**
   * Default constructor
   */
  public UserCausedException() {
    super();
  }

  /**
   * Constructs from message {@link String}
   * @param message to construct from
   */
  public UserCausedException(String message) {
    super(message);
  }

  /**
   * Constructs from {@link Throwable} instance
   * @param throwable to construct from
   */
  public UserCausedException(Throwable throwable) {
    super(throwable);
  }

  /**
   * Constructs from message {@link String} and {@link Throwable} instance
   * @param message to construct from
   * @param throwable to construct from
   */
  public UserCausedException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
