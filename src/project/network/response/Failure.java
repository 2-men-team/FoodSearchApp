package project.network.response;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.logic.representation.Dish;

import java.util.List;

public final class Failure implements Response {
    private static final long serialVersionUID = -5414544402990104009L;

    private final String message;

    public Failure(@Nullable String message) {
        this.message = "" + message;
    }

    @Override
    public @NotNull List<Dish> getData() {
        throw new UnsupportedOperationException("Failure has no data.");
    }

    @Override
    public @NotNull String getMessage() {
        return message;
    }

    @Override
    public @NotNull Status getStatus() {
        return Status.BAD;
    }

    @Override
    public String toString() {
        return "Failure: " + message;
    }
}
