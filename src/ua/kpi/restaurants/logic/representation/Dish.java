package ua.kpi.restaurants.logic.representation;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * Represents information about dish.
 *
 * It contains the following information:
 * <ul>
 *   <li>{@code description} of the dish</li>
 *   <li>{@code restaurant} the dish belongs to</li>
 *   <li>{@code price} value if the dish</li>
 * </ul>
 *
 * This class is immutable.
 *
 * @see Restaurant
 */
public final class Dish implements Serializable {
  private static final long serialVersionUID = -3079456736833325296L;

  private final String description;
  private final Restaurant restaurant;
  private final double price;
  private transient int hash;
  private transient boolean cached = false;

  /**
   * Constructs {@code Dish} instance from specified information given
   *
   * @param description of the dish (must not be {@code null})
   * @param restaurant instance the dish belongs to (must not be {@code null})
   * @param price of the dish
   * @see Restaurant
   */
  public Dish(@NotNull String description, @NotNull Restaurant restaurant, double price) {
    this.description = description;
    this.restaurant  = restaurant;
    this.price       = price;
  }

  /**
   * Retrieves information about dish's description
   *
   * @return dish description
   */
  @NotNull
  public String getDescription() {
    return description;
  }

  /**
   * Retrieves information about dish's restaurant
   *
   * @return {@link Restaurant} instance
   */
  @NotNull
  public Restaurant getRestaurant() {
    return restaurant;
  }

  /**
   * Retrieves information about dish's price
   *
   * @return price of the dish
   */
  public double getPrice() {
    return price;
  }

  /**
   * Allows to compare dishes by price
   *
   * @return {@link Comparator} to compare dishes by price
   */
  public static Comparator<Dish> comparingByPrice() {
    return Comparator.comparingDouble(Dish::getPrice);
  }

  /**
   * Computes dish's hash code
   *
   * Hash value is computed once and cached for the future use
   *
   * @return hash value
   */
  @Override
  public int hashCode() {
    if (!cached) {
      hash = Objects.hash(description, restaurant, price);
      cached = true;
    }

    return hash;
  }

  /**
   * Performs dish comparison
   *
   * Two dishes are considered equal iff they have equal description, restaurant and price
   *
   * Price is compared using {@link Double#compare(double, double)}
   *
   * @param o {@code Dish} instance to compare to
   * @return {@code true} if equal, {@code false} otherwise
   * @see Restaurant#equals(Object)
   */
  @Override
  public boolean equals(Object o) {
    if (o == null) return false;
    if (o == this) return true;
    if (o.getClass() != this.getClass()) return false;
    Dish that = (Dish) o;
    return that.description.equals(this.description) &&
        Double.compare(that.price, this.price) == 0 &&
        that.restaurant.equals(this.restaurant);
  }

  /**
   * Retrieves {@link String} representation of the current dish
   *
   * @return {@link String} representation
   */
  @Override
  public String toString() {
    return String.format("Dish: %s (price: %.3f)", description, price);
  }
}
