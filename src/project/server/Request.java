package project.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.logic.representation.Location;

import java.io.Serializable;
import java.util.Objects;

public final class Request implements Serializable {
    private final static long serialVersionUID = -4829133815639764152L;

    private final String name;
    private final String query;
    private final Location location;

    public Request(@Nullable String name, @NotNull String query, Location location) {
        this.name     = "" + name;
        this.query    = Objects.requireNonNull(query);
        this.location = Objects.requireNonNull(location);
    }

    public String getName() {
        return name;
    }

    public String getQuery() {
        return query;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return String.format("Request: { %s, %s, %s }", name, query, location);
    }
}
