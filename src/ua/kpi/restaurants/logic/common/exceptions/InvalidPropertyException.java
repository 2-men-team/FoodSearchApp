package ua.kpi.restaurants.logic.common.exceptions;

public class InvalidPropertyException extends ProjectRuntimeException {
  public InvalidPropertyException() {
    super();
  }

  public InvalidPropertyException(String message) {
    super(message);
  }

  public InvalidPropertyException(Throwable throwable) {
    super(throwable);
  }

  public InvalidPropertyException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
