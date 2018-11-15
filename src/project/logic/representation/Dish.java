package project.logic.representation;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public final class Dish implements Serializable {
    private static final long serialVersionUID = -6210298888765511788L;

    private final String description;
    private final Restaurant restaurant;
    private final double price;
    private transient int hash;
    private transient boolean cached = false;

    public Dish(@NotNull String description, @NotNull Restaurant restaurant, double price) {
        this.description = description;
        this.restaurant = restaurant;
        this.price = price;
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

    @Override
    public int hashCode() {
        if (!cached) { hash = Objects.hash(description, restaurant, price); cached = true; }
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;
        Dish that = (Dish) o;
        return that.description.equals(this.description) &&
               that.price == this.price &&
               that.restaurant == this.restaurant;
    }

    @Override
    public String toString() {
        return description;
    }
}
