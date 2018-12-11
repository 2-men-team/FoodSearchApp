package project.logic.representation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public final class Location implements Serializable {
    private static final long serialVersionUID = 8750380325627601501L;

    public static final Location NONE = new Location(Double.NaN, Double.NaN);

    private final double lat;
    private final double lon;
    private final String description;
    private transient int hash;
    private transient boolean cached = false;

    public Location(double lat, double lon, @Nullable String description) {
        this.lat = lat;
        this.lon = lon;
        this.description = description;
    }

    public Location(double lat, double lon) {
        this(lat, lon, null);
    }

    public Location(String description) {
        this(Double.NaN, Double.NaN, description);
    }

    public static Location valueOf(@NotNull String lat, @NotNull String lon, @Nullable String description) {
        try {
            double latitude = Double.parseDouble(lat);
            double longitude = Double.parseDouble(lon);
            return new Location(latitude, longitude, description);
        } catch (NumberFormatException e) {
            return Location.NONE;
        }
    }

    public static Location valueOf(@NotNull String lat, @NotNull String lon) {
        return valueOf(lat, lon, null);
    }

    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return lon;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public double distanceTo(@NotNull Location that) {
        return Math.sqrt(Math.pow(this.lat - that.lat, 2) + Math.pow(this.lon - that.lon, 2));
    }

    public Comparator<Location> getComparator() {
        return Comparator.comparingDouble(this::distanceTo);
    }

    @Override
    public int hashCode() {
        if (!cached) {
            hash = Objects.hash(lat, lon);
            cached = true;
        }

        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;
        Location that = (Location) o;
        return Double.compare(that.lat, this.lat) == 0 && Double.compare(that.lon, this.lon) == 0;
    }

    @Override
    public String toString() {
        return String.format("Location: %s [%.4f; %.4f]", description, lat, lon);
    }
}
