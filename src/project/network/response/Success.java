package project.network.response;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.logic.representation.Dish;

import java.util.List;

public final class Success implements Response {
    private static final long serialVersionUID = 878919724116157612L;

    private final String message;
    private final List<Dish> data;

    public Success(@Nullable String message, @NotNull List<Dish> data) {
        this.message = "" + message;
        this.data = data;
    }

    @Override
    public @NotNull List<Dish> getData() {
        return data;
    }

    @Override
    public @NotNull String getMessage() {
        return message;
    }

    @Override
    public @NotNull Status getStatus() {
        return Status.GOOD;
    }

    @Override
    public String toString() {
        return "Success: " + message;
    }
}
