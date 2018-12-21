package ua.kpi.restaurants.network.data;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.representation.Location;

/**
 * Contains representation of typical user request.
 *
 * This class is used with JSON serialization to perform communication between server and client.
 *
 * @see ua.kpi.restaurants.network.server.Server
 * @see ua.kpi.restaurants.test.Client
 */
public final class Request {
  private final String query;
  private final Location location;
  private final Ordering ordering;
  private final Ordering.Rule rule;

  /**
   * Constructs request from the specified information.
   *
   * @param query - the query itself (must not be {@code null})
   * @param location - client's {@link Location} (must not be {@code null})
   * @param ordering - {@link Ordering} technique to use (must not be {@code null})
   * @param rule - {@link Ordering.Rule} to apple to ordering (must not be {@code null})
   */
  public Request(
      @NotNull String query,
      @NotNull Location location,
      @NotNull Ordering ordering,
      @NotNull Ordering.Rule rule
  ) {
    this.query = query;
    this.location = location;
    this.ordering = ordering;
    this.rule = rule;
  }

  /**
   * Retrieves query
   *
   * @return current query
   */
  @NotNull
  public String getQuery() {
    return query;
  }

  /**
   * Retrieves location
   *
   * @return current location
   */
  @NotNull
  public Location getLocation() {
    return location;
  }

  /**
   * Retrieves ordering
   *
   * @return current ordering
   */
  @NotNull
  public Ordering getOrdering() {
    return ordering;
  }

  /**
   * Retrieves rule for ordering
   *
   * @return current rule
   */
  @NotNull
  public Ordering.Rule getRule() {
    return rule;
  }

  /**
   * Retrieves {@link String} representation of this request
   *
   * @return {@link String} representation
   */
  @Override
  public String toString() {
    return String.format("Request: { %s, %s }", query, location);
  }
}
