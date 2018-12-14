package ua.kpi.restaurants.logic.representation;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public final class Dish implements Serializable {
  private static final long serialVersionUID = -3079456736833325296L;

  private final String description;
  private final Restaurant restaurant;
  private final double price;
  private transient int hash;
  private transient boolean cached = false;

  public Dish(@NotNull String description, @NotNull Restaurant restaurant, double price) {
    this.description = description;
    this.restaurant  = restaurant;
    this.price       = price;
  }

  @NotNull
  public String getDescription() {
    return description;
  }

  @NotNull
  public Restaurant getRestaurant() {
    return restaurant;
  }

  public double getPrice() {
    return price;
  }

  public static Comparator<Dish> comparingByPrice() {
    return Comparator.comparingDouble(Dish::getPrice);
  }

  @Override
  public int hashCode() {
    if (!cached) {
      hash = Objects.hash(description, restaurant, price);
      cached = true;
    }

    return hash;
  }

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

  @Override
  public String toString() {
    return String.format("Dish: %s (price: %.3f)", description, price);
  }
}
