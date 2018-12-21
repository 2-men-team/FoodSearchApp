package ua.kpi.restaurants.logic.representation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * Represents information about restaurants.
 *
 * It contains the following information:
 * <ul>
 *   <li>{@code name} of the restaurant</li>
 *   <li>{@code location} of the restaurant</li>
 *   <li>{@code description} of the restaurant (optional)</li>
 * </ul>
 *
 * This class is immutable.
 *
 * @see Location
 */
public final class Restaurant implements Serializable {
  private static final long serialVersionUID = -4006548713350763747L;

  private final String name;
  private final String description;
  private final Location location;
  private transient int hash;
  private transient boolean cached = false;

  /**
   * Constructs {@code Restaurant} instance from the given information
   *
   * @param name of the restaurant (must not be {@code null})
   * @param description of the restaurant (optional)
   * @param location of the restaurant (must not be {@code null})
   * @see Location
   */
  public Restaurant(@NotNull String name, @Nullable String description, @NotNull Location location) {
    this.name = name;
    this.description = description;
    this.location = location;
  }

  /**
   * Retrieves information about restaurant's name
   *
   * @return name of the restaurant
   */
  @NotNull
  public String getName() {
    return name;
  }

  /**
   * Retrieves information about restaurant's description
   *
   * @return description of the restaurant
   */
  @Nullable
  public String getDescription() {
    return description;
  }

  /**
   * Retrieves information about restaurant's location
   *
   * @return {@link Location} instance for the current restaurant
   */
  @NotNull
  public Location getLocation() {
    return location;
  }

  /**
   * Produces {@link Comparator} to compare restaurants by the distance to the specified {@code location}
   *
   * @param location to compare restaurants by (must not be {@code null})
   * @return {@link Comparator} instance
   */
  public static Comparator<Restaurant> comparingByLocation(@NotNull Location location) {
    return Comparator.comparing(Restaurant::getLocation, location.getComparator());
  }

  @Override
  public int hashCode() {
    if (!cached) {
      hash = Objects.hash(name, location);
      cached = true;
    }

    return hash;
  }

  /**
   * Performs comparison operation between two instances of {@code Restaurant} class.
   *
   * Two instances are considered equal if they have both {@code name} and {@code location} equal.
   *
   * @param o instance to compare to
   * @return {@code true} if equal, {@code false} otherwise
   * @see Location#equals(Object)
   */
  @Override
  public boolean equals(Object o) {
    if (o == null) return false;
    if (o == this) return true;
    if (o.getClass() != this.getClass()) return false;
    Restaurant that = (Restaurant) o;
    return this.name.equals(that.name) && this.location.equals(that.location);
  }

  /**
   * Produces {@link String} representation of the current {@code Restaurant}
   *
   * @return {@link String} value of the representation
   */
  @Override
  public String toString() {
    return String.format("Restaurant: %s (%s)", name, location);
  }
}
