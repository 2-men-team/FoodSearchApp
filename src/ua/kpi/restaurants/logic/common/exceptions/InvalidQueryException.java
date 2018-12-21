package ua.kpi.restaurants.logic.common.exceptions;

/**
 * Thrown if user-produced query is invalid in any way.
 * Extends {@link UserCausedException}.
 */
public class InvalidQueryException extends UserCausedException {
  /**
   * Default constructor
   */
  public InvalidQueryException() {
    super();
  }

  /**
   * Constructs from message {@link String}
   * @param message to construct from
   */
  public InvalidQueryException(String message) {
    super(message);
  }

  /**
   * Constructs from {@link Throwable} instance
   * @param throwable to construct from
   */
  public InvalidQueryException(Throwable throwable) {
    super(throwable);
  }

  /**
   * Constructs from message {@link String} and {@link Throwable} instance
   * @param message to construct from
   * @param throwable to construct from
   */
  public InvalidQueryException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
