package ua.kpi.restaurants.network.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.kpi.restaurants.logic.representation.Dish;

import java.util.List;

/**
 * Contains representation of typical server response.
 *
 * This class is used with JSON serialization to perform communication between server and client.
 *
 * Response has two possible states: success and failure. Failed response has no data to retrieve.
 *
 * @see ua.kpi.restaurants.network.server.Server
 * @see ua.kpi.restaurants.test.Client
 * @see Status
 */
public final class Response {
  private final String message;
  private final List<Dish> data;
  private final Status status;

  private Response(String message, List<Dish> data, Status status) {
    this.message = message;
    this.data = data;
    this.status = status;
  }

  /**
   * Constructs successful response
   *
   * @param message to send to the client (optional)
   * @param data - computation result (must not be {@code null})
   * @return initialized instance
   */
  public static Response success(@Nullable String message, @NotNull List<Dish> data) {
    return new Response(message, data, Status.SUCCESS);
  }

  /**
   * Constructs failed response
   *
   * @param message to send to the client (optional)
   * @return initialized instance
   */
  public static Response failure(@NotNull String message) {
    return new Response(message, null, Status.FAILURE);
  }

  /**
   * Retrieves data sent with response
   *
   * @return response data
   * @throws UnsupportedOperationException if called on failed response
   */
  public @NotNull List<Dish> getData() {
    if (status == Status.FAILURE) {
      throw new UnsupportedOperationException("Failed response has no data.");
    }

    return data;
  }

  /**
   * Retrieves message sent with response
   *
   * @return message retrieved
   */
  public @Nullable String getMessage() {
    return message;
  }

  /**
   * Retrieves response {@link Status}
   *
   * @return response status
   */
  public @NotNull Status getStatus() {
    return status;
  }

  /**
   * Retrieves {@link String} representation of the current response
   *
   * @return string representation
   */
  @Override
  public String toString() {
    return String.format("Response: { %s, %s }", status, message);
  }

  /**
   * Denotes status of the {@link Response}
   */
  public enum Status {
    FAILURE,
    SUCCESS
  }
}