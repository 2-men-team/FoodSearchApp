package project.network.request;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.logic.representation.Location;

import java.io.Serializable;

public final class Request implements Serializable {
    private static final long serialVersionUID = -7673100093728357558L;

    private final String message;
    private final String query;
    private final Location location;

    public Request(@Nullable String message, @NotNull String query, @NotNull Location location) {
        this.message  = "" + message;
        this.query    = query;
        this.location = location;
    }

    @NotNull
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
