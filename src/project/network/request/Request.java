package project.network.request;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.logic.representation.Location;

public final class Request {
    private final String message;
    private final String query;
    private final Location location;

    public Request(@Nullable String message, @NotNull String query, @NotNull Location location) {
        this.message  = message;
        this.query    = query;
        this.location = location;
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

    @Override
    public String toString() {
        return String.format("Request: { %s, %s, %s }", message, query, location);
    }
}
