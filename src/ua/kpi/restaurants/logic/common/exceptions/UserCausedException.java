package ua.kpi.restaurants.logic.common.exceptions;

public class UserCausedException extends ProjectRuntimeException {
  public UserCausedException() {
    super();
  }

  public UserCausedException(String message) {
    super(message);
  }

  public UserCausedException(Throwable throwable) {
    super(throwable);
  }

  public UserCausedException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
