package ua.kpi.restaurants.logic.common.exceptions;

public class ProjectRuntimeException extends RuntimeException {
  public ProjectRuntimeException() {
    super();
  }

  public ProjectRuntimeException(String message) {
    super(message);
  }

  public ProjectRuntimeException(Throwable throwable) {
    super(throwable);
  }

  public ProjectRuntimeException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
