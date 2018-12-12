package project.logic.representation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public final class Restaurant implements Serializable {
    private static final long serialVersionUID = -4006548713350763747L;

    private final String name;
    private final String description;
    private final Location location;
    private transient int hash;
    private transient boolean cached = false;

    public Restaurant(@NotNull String name, @Nullable String description, @NotNull Location location) {
        this.name = name;
        this.description = description;
        this.location = location;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @NotNull
    public Location getLocation() {
        return location;
    }

    public static Comparator<Restaurant> comparingByLocation(@NotNull Location location) {
        return Comparator.comparing(Restaurant::getLocation, location.getComparator());
    }

    @Override
    public int hashCode() {
        if (!cached) { hash = Objects.hash(name, location); cached = true; }
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return this.name.equals(that.name) && this.location.equals(that.location);
    }

    @Override
    public String toString() {
        return String.format("Restaurant: %s (%s)", name, location);
    }
}
