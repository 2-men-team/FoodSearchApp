package ua.kpi.restaurants.network.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.kpi.restaurants.logic.strategies.handling.HandlingStrategy.Result;

import java.util.List;

public final class Response {
  private final String message;
  private final List<Result> data;
  private final Status status;

  private Response(String message, List<Result> data, Status status) {
    this.message = message;
    this.data = data;
    this.status = status;
  }

  public static Response success(@Nullable String message, @NotNull List<Result> data) {
    return new Response(message, data, Status.SUCCESS);
  }

  public static Response failure(@Nullable String message) {
    return new Response(message, null, Status.FAILURE);
  }

  public @NotNull List<Result> getData() {
    if (status == Status.FAILURE) {
      throw new UnsupportedOperationException("Failed response has no data.");
    }

    return data;
  }

  public @Nullable String getMessage() {
    return message;
  }

  public @NotNull Status getStatus() {
    return status;
  }

  @Override
  public String toString() {
    return String.format("Response: { %s, %s }", status, message);
  }

  public enum Status {
    FAILURE,
    SUCCESS
  }
}