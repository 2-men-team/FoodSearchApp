package ua.kpi.restaurants.logic.common.exceptions;

public class InvalidQueryException extends UserCausedException {
  public InvalidQueryException() {
    super();
  }

  public InvalidQueryException(String message) {
    super(message);
  }

  public InvalidQueryException(Throwable throwable) {
    super(throwable);
  }

  public InvalidQueryException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
