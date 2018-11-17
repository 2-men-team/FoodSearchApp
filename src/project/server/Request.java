package project.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.logic.representation.Location;

import java.io.Serializable;

public final class Request implements Serializable {
    private final static long serialVersionUID = -7673100093728357558L;

    private final String name;
    private final String query;
    private final Location location;

    public Request(@Nullable String name, @NotNull String query, @NotNull Location location) {
        this.name     = "" + name;
        this.query    = query;
        this.location = location;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getQuery() {
        return query;
    }

    @NotNull
    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return String.format("Request: { %s, %s, %s }", name, query, location);
    }
}
