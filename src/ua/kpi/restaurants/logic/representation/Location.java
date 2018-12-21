package ua.kpi.restaurants.logic.representation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * Represents information about location (obviously).
 *
 * It contains the following information:
 * <ul>
 *   <li>{@code latitude}</li>
 *   <li>{@code longitude}</li>
 *   <li>{@code description} - address or some other information</li>
 * </ul>
 *
 * This class is immutable.
 */
public final class Location implements Serializable {
  private static final long serialVersionUID = 8750380325627601501L;

  /** Represents absence of information */
  public static final Location NONE = new Location(Double.NaN, Double.NaN);

  private final double lat;
  private final double lon;
  private final String description;
  private transient int hash;
  private transient boolean cached = false;

  /**
   * Constructs instance of this class from the given information
   *
   * @param lat - latitude
   * @param lon - longitude
   * @param description of this location
   */
  public Location(double lat, double lon, @Nullable String description) {
    this.lat = lat;
    this.lon = lon;
    this.description = description;
  }

  /**
   * Constructs instance in the case of absence of description information
   *
   * Description is initialized to {@code null}
   *
   * @param lat - latitude
   * @param lon - longitude
   */
  public Location(double lat, double lon) {
    this(lat, lon, null);
  }

  /**
   * Constructs instance in the case of absence of coordinates information
   *
   * {@code Latitude} and {@code longitude} are initialized to {@link Double#NaN}
   *
   * @param description of the location
   */
  public Location(String description) {
    this(Double.NaN, Double.NaN, description);
  }

  /**
   * Factory method to produce an instance of this class from raw information.
   *
   * It uses {@link Double#parseDouble(String)} to retrieve information about coordinates.
   * It returns {@link #NONE} if retrieval fails.
   *
   * @param lat - latitude {@link String} (must not be {@code null})
   * @param lon - longitude {@link String} (must not be {@code null})
   * @param description information of this location
   * @return created instance
   */
  public static Location valueOf(@NotNull String lat, @NotNull String lon, @Nullable String description) {
    try {
      double latitude = Double.parseDouble(lat);
      double longitude = Double.parseDouble(lon);
      return new Location(latitude, longitude, description);
    } catch (NumberFormatException e) {
      return Location.NONE;
    }
  }

  /**
   * Factory method to produce an instance of this class in the case of partial raw information available.
   *
   * It effectively calls {@link #valueOf(String, String, String)} method with {@code description}
   * parameter set to {@code null}.
   *
   * @param lat - latitude {@link String} (must not be {@code null})
   * @param lon - longitude {@link String} (must not be {@code null})
   * @return created instance
   */
  public static Location valueOf(@NotNull String lat, @NotNull String lon) {
    return valueOf(lat, lon, null);
  }

  /**
   * Retrieves current {@code latitude} value of this location
   *
   * @return latitude value
   */
  public double getLatitude() {
    return lat;
  }

  /**
   * Retrieves current {@code longitude} value of this location
   *
   * @return longitude value
   */
  public double getLongitude() {
    return lon;
  }

  /**
   * Retrieves current {@code description} value of this location
   *
   * @return description value
   */
  @Nullable
  public String getDescription() {
    return description;
  }

  /**
   * Retrieves distance from {@code this} location to {@code that} location
   *
   * @param that location to calculate distance to
   * @return calculated distance
   */
  public double distanceTo(@NotNull Location that) {
    return Math.sqrt(Math.pow(this.lat - that.lat, 2) + Math.pow(this.lon - that.lon, 2));
  }

  /**
   * Produces {@link Comparator} to compare locations by {@link #distanceTo(Location)} {@code this} location
   *
   * @return produced {@link Comparator}
   */
  public Comparator<Location> getComparator() {
    return Comparator.comparingDouble(this::distanceTo);
  }

  /**
   * Computes hash code value for this location.
   *
   * Value is computed once during the first call to this function and then cached for future use.
   *
   * @return computed value
   */
  @Override
  public int hashCode() {
    if (!cached) {
      hash = Objects.hash(lat, lon, description);
      cached = true;
    }

    return hash;
  }

  /**
   * Performs comparison operation between two instances.
   *
   * Two locations are considered equal if they have the following information equal:
   * <ul>
   *   <li>{@code latitude}</li>
   *   <li>{@code longitude}</li>
   *   <li>{@code description}</li>
   * </ul>
   *
   * @param o instance to compare to
   * @return {@code true} if equal, {@code false} otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (o == null) return false;
    if (o == this) return true;
    if (o.getClass() != this.getClass()) return false;
    Location that = (Location) o;
    return Double.compare(that.lat, this.lat) == 0
        && Double.compare(that.lon, this.lon) == 0
        && Objects.equals(that.description, this.description);
  }

  /**
   * Produces {@link String} representation of the current {@code Location}
   *
   * @return {@link String} value of the representation
   */
  @Override
  public String toString() {
    return String.format("Location: %s [%.4f; %.4f]", description, lat, lon);
  }
}
