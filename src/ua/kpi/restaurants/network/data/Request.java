package ua.kpi.restaurants.network.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.kpi.restaurants.logic.representation.Location;

public final class Request {
  private final String message;
  private final String query;
  private final Location location;
  private final Ordering ordering;
  private final Ordering.Rule rule;

  public Request(
      @Nullable String message,
      @NotNull String query,
      @NotNull Location location,
      @NotNull Ordering ordering,
      @NotNull Ordering.Rule rule
  ) {
    this.message = message;
    this.query = query;
    this.location = location;
    this.ordering = ordering;
    this.rule = rule;
  }

  @Nullable
  public String getMessage() {
    return message;
  }

  @NotNull
  public String getQuery() {
    return query;
  }

  @NotNull
  public Location getLocation() {
    return location;
  }

  @NotNull
  public Ordering getOrdering() {
    return ordering;
  }

  @NotNull
  public Ordering.Rule getRule() {
    return rule;
  }

  @Override
  public String toString() {
    return String.format("Request: { %s, %s, %s }", message, query, location);
  }
}
